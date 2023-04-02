package nl.tudelft.sem.template.example.domain.processing;

import commons.FacultyResource;
import commons.ScheduleJob;
import commons.UpdateJob;
import commons.Url;
import java.time.LocalDate;
import java.util.List;
import nl.tudelft.sem.template.example.domain.db.ScheduledInstance;
import nl.tudelft.sem.template.example.domain.db.ScheduledInstanceRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@SuppressWarnings("PMD")
@Service
public class UpdatingJobsService {
    private final transient ScheduledInstanceRepository scheduledInstanceRepository;
    private final transient ExcessRemovalService excessRemovalService;
    private final transient ProcessingJobsService processingJobsService;
    private final transient RestTemplate restTemplate;

    /**
     * Constructor for the service.
     *
     * @param scheduledInstanceRepository .
     * @param restTemplate .
     * @param excessRemovalService .
     * @param processingJobsService .
     */
    public UpdatingJobsService(ScheduledInstanceRepository scheduledInstanceRepository, RestTemplate restTemplate,
                               ExcessRemovalService excessRemovalService, ProcessingJobsService processingJobsService) {
        this.scheduledInstanceRepository = scheduledInstanceRepository;
        this.excessRemovalService = excessRemovalService;
        this.processingJobsService = processingJobsService;
        this.restTemplate = restTemplate;
    }

    /**
     * Function to update the schedule based on a new amount of resources.
     *
     * @param resource New amount of resources. resource.date indicates the starting date of the change.
     */
    public void updateSchedule(FacultyResource resource) {
        LocalDate currentDate = resource.getDate();

        while (true) {
            List<ScheduledInstance> instancesInDb =
                    scheduledInstanceRepository.findByDateAndFaculty(currentDate, resource.getFaculty());

            if (!instancesInDb.isEmpty()) {
                int cpuUsageSum = instancesInDb.stream().mapToInt(ScheduledInstance::getCpuUsage).sum();
                int gpuUsageSum = instancesInDb.stream().mapToInt(ScheduledInstance::getGpuUsage).sum();
                int memoryUsageSum = instancesInDb.stream().mapToInt(ScheduledInstance::getMemoryUsage).sum();

                int cpuExcess = Math.max(cpuUsageSum - resource.getCpuUsage(), 0);
                int gpuExcess = Math.max(gpuUsageSum - resource.getGpuUsage(), 0);
                int memoryExcess = Math.max(memoryUsageSum - resource.getMemoryUsage(), 0);

                if (cpuExcess > 0 || gpuExcess > 0 || memoryExcess > 0) {
                    List<ScheduleJob> toReschedule =
                            excessRemovalService.reduceExcess(instancesInDb, cpuExcess, gpuExcess, memoryExcess);

                    for (ScheduleJob job : toReschedule) {
                        // try to schedule the job again the same day
                        if (!rescheduleJob(job)) {
                            // could not reschedule, inform the Jobs microservice
                            restTemplate.postForEntity(Url.getJobsUrl() + "/updateStatus",
                                    new UpdateJob(job.getJobId(), "cancelled", null), Void.class);
                        }
                    }
                }
            }

            ScheduledInstance next =
                    scheduledInstanceRepository.findFirstByFacultyEqualsAndDateIsGreaterThanEqualOrderByDateAsc(
                            resource.getFaculty(), currentDate);

            if (next == null) {
                // nothing else to process
                break;
            }
            currentDate = next.getDate().plusDays(1);
        }
    }

    private boolean rescheduleJob(ScheduleJob job) {
        List<ScheduledInstance> scheduledInstances =
                processingJobsService.getSchedulingStrategy().scheduleBetween(job, job.getScheduleBefore(),
                        job.getScheduleBefore().plusDays(1));

        if (scheduledInstances.size() == 0) {
            return false;
        }

        scheduledInstanceRepository.saveAll(scheduledInstances);
        return true;
    }

}
