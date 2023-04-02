package nl.tudelft.sem.template.example.domain.processing;

import commons.FacultyResource;
import commons.FacultyResponseModel;
import commons.FacultyTotalResource;
import commons.ScheduleJob;
import commons.UpdateJob;
import commons.Url;
import exceptions.ResourceBiggerThanCpuException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Synchronized;
import nl.tudelft.sem.template.example.domain.ResourceGetter;
import nl.tudelft.sem.template.example.domain.db.ScheduledInstance;
import nl.tudelft.sem.template.example.domain.db.ScheduledInstanceRepository;
import nl.tudelft.sem.template.example.domain.strategies.ScheduleBetweenClusters;
import nl.tudelft.sem.template.example.domain.strategies.ScheduleBetweenClustersMostResourcesFirst;
import nl.tudelft.sem.template.example.domain.strategies.ScheduleOneCluster;
import nl.tudelft.sem.template.example.domain.strategies.SchedulingStrategy;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@SuppressWarnings("PMD")
@Service
public class ProcessingJobsService {

    private final transient ScheduledInstanceRepository scheduledInstanceRepository;
    private final RestTemplate restTemplate;
    private final ResourceGetter resourceGetter;
    private SchedulingStrategy schedulingStrategy;
    private final SchedulingCheckService schedulingCheckService;

    ProcessingJobsService(ScheduledInstanceRepository scheduledInstanceRepository, RestTemplate restTemplate) {
        this.scheduledInstanceRepository = scheduledInstanceRepository;
        this.restTemplate = restTemplate;
        this.schedulingCheckService = new SchedulingCheckService();
        this.resourceGetter = new ResourceGetter(this.restTemplate, Url.getClustersUrl());
        schedulingStrategy = new ScheduleBetweenClusters(this.resourceGetter,
                this.scheduledInstanceRepository);
    }

    /**
     * This method tries to schedule a job defined by a ScheduleJob object.
     * Does not return anything, but sends a proper message to the Jobs
     * microservice (whether the job was scheduled or not).
     *
     * @param j a ScheduleJob DTO of a Job to be scheduled
     */
    @Synchronized
    public void scheduleJob(ScheduleJob j) throws ResourceBiggerThanCpuException {
        schedulingCheckService.verifyCpuBiggerThanMaxOfGpuOrMemory(j);

        List<ScheduledInstance> scheduledInstances =
                schedulingStrategy.scheduleBetween(j, schedulingCheckService
                        .scheduleAfterInclusive(LocalTime.now()), j.getScheduleBefore());

        if (scheduledInstances.isEmpty()) {
            // inform the Job microservice that the job was not scheduled
            restTemplate.postForEntity(Url.getJobsUrl() + "/updateStatus",
                    new UpdateJob(j.getJobId(), "unscheduled", null), Void.class);
            return;
        }

        scheduledInstanceRepository.saveAll(scheduledInstances);

        // inform the Job microservice about a success!
        restTemplate.postForEntity(Url.getJobsUrl() + "/updateStatus",
                new UpdateJob(j.getJobId(), "scheduled", scheduledInstances.get(0).getDate()), Void.class);
    }



    /**
     * Gets the number of available resources out of total resources for the next day. Only admin can access it.
     *
     * @return  List of faculty resource
     */
    public List<FacultyTotalResource> getAllResourcesNextDay() {

        ResponseEntity<FacultyResponseModel> fac = restTemplate.getForEntity(Url.getAuthenticationUrl()
                + "/faculties", FacultyResponseModel.class);

        List<String> faculties = new ArrayList<>();
        if (fac.hasBody()) {
            faculties = fac.getBody().getFaculty();
        }

        List<FacultyTotalResource> res = new ArrayList<>();

        LocalDate tmrw = LocalDate.now().plusDays(1);
        for (String f : faculties) {
            List<ScheduledInstance> instancesInDb =
                    scheduledInstanceRepository.findByDateAndFaculty(tmrw, f);
            int cpuUsageSum = instancesInDb.stream().mapToInt(ScheduledInstance::getCpuUsage).sum();
            int gpuUsageSum = instancesInDb.stream().mapToInt(ScheduledInstance::getGpuUsage).sum();
            int memoryUsageSum = instancesInDb.stream().mapToInt(ScheduledInstance::getMemoryUsage).sum();

            List<FacultyResource> availableResources = resourceGetter.getAvailableResources(f, tmrw);
            int cpuAvailableSum = availableResources.stream().mapToInt(FacultyResource::getCpuUsage).sum();
            int gpuAvailableSum = availableResources.stream().mapToInt(FacultyResource::getGpuUsage).sum();
            int memoryAvailableSum = availableResources.stream().mapToInt(FacultyResource::getMemoryUsage).sum();

            FacultyTotalResource fr = new FacultyTotalResource(f, tmrw, cpuUsageSum, gpuUsageSum, memoryUsageSum,
                    cpuAvailableSum, gpuAvailableSum, memoryAvailableSum);
            res.add(fr);
        }
        return res;
    }







    public SchedulingStrategy getSchedulingStrategy() {
        return schedulingStrategy;
    }

    public void setSchedulingStrategy(SchedulingStrategy schedulingStrategy) {
        this.schedulingStrategy = schedulingStrategy;
    }

    /**
     * Sets specified scheduling strategy.
     *
     * @param strategy name of the strategy
     */
    public void setSchedulingStrategy(String strategy) throws InvalidStrategyNameException {
        switch (strategy) {
            case "one-cluster":
                setSchedulingStrategy(new ScheduleOneCluster(resourceGetter, scheduledInstanceRepository));
                break;
            case "multiple-clusters":
                setSchedulingStrategy(new ScheduleBetweenClusters(resourceGetter, scheduledInstanceRepository));
                break;
            case "multiple-clusters-most-resources-first":
                setSchedulingStrategy(new ScheduleBetweenClustersMostResourcesFirst(resourceGetter,
                        scheduledInstanceRepository));
                break;
            default:
                throw new InvalidStrategyNameException(strategy);
        }
    }
}
