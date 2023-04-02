package nl.tudelft.sem.template.example.domain;

import commons.Faculty;
import commons.Job;
import commons.NetId;
import commons.RoleValue;
import commons.ScheduleJob;
import commons.Status;
import exceptions.InvalidIdException;
import exceptions.InvalidNetIdException;
import exceptions.InvalidResourcesException;
import exceptions.ResourceBiggerThanCpuException;
import java.time.LocalDate;
import java.util.Optional;
import nl.tudelft.sem.template.example.models.JobIdRequestModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.client.RestTemplate;

public class JobServiceBasic extends Checks {
    protected final transient JobRepository jobRepository;
    protected final transient RestTemplate restTemplate;
    protected static final String nullValue = "null";
    protected final transient String schedulerUrl = "http://localhost:8084";
    protected final transient String url = "http://localhost:8083";

    /**
     * Instantiates a new JobService.
     *
     * @param jobRepository               the job repository
     * @param restTemplate                the template to make REST API calls
     */
    public JobServiceBasic(JobRepository jobRepository, RestTemplate restTemplate) {
        this.jobRepository = jobRepository;
        this.restTemplate = restTemplate;
    }

    /**
     * Makes a POST request to the Scheduler, to schedule Jobs.
     *
     * @param scheduleJob the Job object to be scheduled
     * @return the response message of the Scheduler
     * @throws InvalidScheduleJobException if scheduleJob is null
     */
    public String scheduleJob(ScheduleJob scheduleJob) throws InvalidScheduleJobException, ResponseEntityException {
        if (scheduleJob == null) {
            throw new InvalidScheduleJobException(null);
        }

        ResponseEntity<String> response = restTemplate
            .postForEntity(schedulerUrl + "/schedule", scheduleJob, String.class);

        if (response.getBody() == null) {
            throw new ResponseEntityException();
        }
        return response.getBody();
    }

    /**
     * Makes a POST request to the Scheduler to unschedule Jobs.
     *
     * @param jobId - the id of the job to be unscheduled
     * @return the response message of the scheduler
     * @throws ResponseEntityException - exception that handles empty response from the Scheduler
     */
    public String unscheduleJob(JobIdRequestModel jobId) throws ResponseEntityException {
        ResponseEntity<String> response = restTemplate
            .postForEntity(schedulerUrl + "/unschedule", jobId, String.class);

        if (response.getBody() == null) {
            throw new ResponseEntityException();
        }
        return response.getBody();
    }

    /**
     * Create a new job.
     *
     * @param netId NetId of the job creator
     * @param authNetId NetId of the authenticated user
     * @param cpuUsage CPU usage
     * @param gpuUsage GPU usage
     * @param memoryUsage memory usage
     * @return a new Job
     * @throws Exception if the resources of NetId are invalid
     */

    public Job createJob(NetId netId, NetId authNetId, Faculty faculty, String desc, int cpuUsage, int gpuUsage,
                         int memoryUsage, RoleValue role, LocalDate preferredDate) throws Exception {

        Job newJob = new Job(netId, faculty, desc, cpuUsage, gpuUsage, memoryUsage, preferredDate);
        checkResourcesJob(newJob);
        checkNetIdNull(netId);
        checkNetIdAuth(netId, authNetId);
        checkIsEmployee(role);
        jobRepository.save(newJob);

        return newJob;
    }

    /**
     * Create a new job.
     *
     * @param authNetId NetId of the authenticated user
     * @param job a job
     * @param role a role of a user who creates a job
     * @return a new Job
     * @throws Exception if the resources of NetId are invalid
     */
    public Job createJob(NetId authNetId, Job job, RoleValue role) throws Exception {
        checkResourcesJob(job);
        checkNetIdNull(job.getNetId());
        checkNetIdAuth(job.getNetId(), authNetId);
        checkIsEmployee(role);

        jobRepository.save(job);

        return job;
    }

    /**
     * Remove a job from the database.
     *
     * @param id the unique id of the Job.
     * @throws Exception if there is no Job with the provided id.
     */
    public void deleteJob(String netId, RoleValue role, long id) throws Exception {
        if (!jobRepository.existsById(id)) {
            throw new InvalidIdException(id);
        }

        if (role == RoleValue.ADMIN || ((role == RoleValue.EMPLOYEE)
            && netId.equals(jobRepository.getOne(id).getNetId().toString()))) {
            jobRepository.deleteById(id);
        } else {
            throw new BadCredentialsException("Not admin or not your job");
        }
    }


    /**
     * Update information about the Job specified by a microservice.
     *
     * @param id id of the Job
     * @param status the new status of the Job
     * @param localDate the time the Job is scheduled to start
     * @throws Exception if the id does not exist in the database
     */
    public void updateJob(long id, Status status, LocalDate localDate) throws Exception {
        Optional<Job> jobOptional = jobRepository.findById(id);
        if (jobOptional.isEmpty()) {
            throw new InvalidIdException(id);
        }
        Job job = jobOptional.get();
        job.setStatus(status);
        job.setPreferredDate(localDate);
        jobRepository.save(job);
    }

}
