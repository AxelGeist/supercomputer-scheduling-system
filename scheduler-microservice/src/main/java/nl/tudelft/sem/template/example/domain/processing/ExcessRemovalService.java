package nl.tudelft.sem.template.example.domain.processing;

import commons.Faculty;
import commons.ScheduleJob;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import nl.tudelft.sem.template.example.domain.db.ScheduledInstance;
import nl.tudelft.sem.template.example.domain.db.ScheduledInstanceRepository;
import org.springframework.stereotype.Service;

@Service
public class ExcessRemovalService {
    private final transient RemovingJobsService removingJobsService;
    private final transient ScheduledInstanceRepository scheduledInstanceRepository;

    public ExcessRemovalService(RemovingJobsService removingJobsService,
                                ScheduledInstanceRepository scheduledInstanceRepository) {
        this.removingJobsService = removingJobsService;
        this.scheduledInstanceRepository = scheduledInstanceRepository;
    }

    /**
     * Function to reduce excess of resources in the faculty of ScheduledInstances.
     *
     * @param jobs jobs from the cluster that uses too many resources
     * @param cpu the number of excessive cpu
     * @param gpu the number of excessive gpu
     * @param memory the number of excessive memory
     * @return jobs that should be rescheduled or cancelled.
     */
    public List<ScheduleJob> reduceExcess(List<ScheduledInstance> jobs, int cpu, int gpu, int memory) {
        jobs.sort((o1, o2) -> o2.getCreatedAt().compareTo(o1.getCreatedAt()));

        List<ScheduleJob> jobsToReschedule = new ArrayList<>();

        for (ScheduledInstance instance : jobs) {
            if (!(cpu > 0 || gpu > 0 || memory > 0)) {
                break;
            }

            if (!(cpu > 0 && instance.getGpuUsage() > 0 || gpu > 0 && instance.getCpuUsage() > 0
                    || memory > 0 && instance.getMemoryUsage() > 0)) {
                // skip instances that don't use the excess
                continue;
            }

            ScheduleJob job = recreateScheduleJobFromScheduledInstances(instance.getJobId(), instance.getDate());
            if (job == null || !removingJobsService.removeJob(instance.getJobId())) {
                // something went wrong...
                // TODO: proper error handling with exceptions
                continue;
            }

            cpu -= instance.getCpuUsage();
            gpu -= instance.getGpuUsage();
            memory -= instance.getMemoryUsage();

            jobsToReschedule.add(job);
        }

        return jobsToReschedule;
    }

    private ScheduleJob recreateScheduleJobFromScheduledInstances(long jobId, LocalDate scheduleBefore) {
        List<ScheduledInstance> instances = scheduledInstanceRepository.findAllByJobId(jobId);
        if (instances.isEmpty()) {
            return null;
        }

        int cpu = instances.stream().mapToInt(ScheduledInstance::getCpuUsage).sum();
        int gpu  = instances.stream().mapToInt(ScheduledInstance::getGpuUsage).sum();
        int memory = instances.stream().mapToInt(ScheduledInstance::getMemoryUsage).sum();

        return new ScheduleJob(jobId, new Faculty(instances.get(0).getJobFaculty()), scheduleBefore, cpu, gpu, memory);
    }
}
