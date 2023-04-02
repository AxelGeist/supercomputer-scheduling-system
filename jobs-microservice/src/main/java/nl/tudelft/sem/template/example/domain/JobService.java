package nl.tudelft.sem.template.example.domain;

import commons.Faculty;
import commons.Job;
import commons.NetId;
import commons.Role;
import commons.RoleValue;
import commons.ScheduleJob;
import commons.Status;
import exceptions.InvalidIdException;
import exceptions.InvalidNetIdException;
import exceptions.InvalidResourcesException;
import exceptions.ResourceBiggerThanCpuException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import nl.tudelft.sem.template.example.models.JobIdRequestModel;
import nl.tudelft.sem.template.example.models.JobResponseModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * A DDD service for handling jobs.
 */

@Service
public class JobService extends JobServiceBasic {
    /**
     * Instantiates a new JobService.
     *
     * @param jobRepository               the job repository
     * @param restTemplate                the template to make REST API calls
     */
    public JobService(JobRepository jobRepository, RestTemplate restTemplate) {
        super(jobRepository, restTemplate);
    }


    /**
     * Collect all the jobs in the database created by a specific user.
     *
     * @param netId NetId of the request creator
     * @param authNetId NetId of the authenticated user
     * @return a list of Job corresponding to the NetId provided
     * @throws Exception if the NetId is invalid or there is no associated Job to the NetId
     */
    public List<Job> collectJobsByNetId(NetId netId, NetId authNetId) throws Exception {
        checkNetIdNull(netId);
        Optional<List<Job>> jobs = jobRepository.findAllByNetId(netId);
        if (jobs.isEmpty() || !netId.toString().equals(authNetId.toString())) {
            throw new InvalidNetIdException(netId.toString());
        }
        return jobs.get();
    }

    /**
     * Retrieve the status of a specific Job stored in the database.
     *
     * @param netId NetId of the request creator
     * @param authNetId NetId of the authenticated user
     * @param jobId the unique id of the Job
     * @return a String with the status of the Job
     * @throws Exception if the NetId is invalid or the NetId does not have permission to access the requested job.
     */
    public Status getJobStatus(NetId netId, NetId authNetId, long jobId) throws Exception {
        checkNetIdNull(netId);

        Optional<Job> job = jobRepository.findById(jobId);
        checkJobEmpty(job, jobId);

        if (!netId.toString().equals(authNetId.toString()) || !job.get().getNetId().toString().equals(netId.toString())) {
            throw new InvalidNetIdException(netId.toString());
        }
        return job.get().getStatus();
    }

    /**
     * Retrieve all the Job entities from the database.
     *
     * @param netId NetId of the request creator
     * @param authNetId NetId of the authenticated user
     * @param role role of the request creator
     * @return a list of Job entities containing all jobs in the database.
     * @throws Exception if the NetId is invalid or the creator of the request does not have the admin role.
     */
    public List<Job> getAllJobs(NetId netId, NetId authNetId, RoleValue role) throws Exception {
        checkNetIdNull(netId);
        checkNetIdAuth(netId, authNetId);
        checkIsAdmin(role);

        return jobRepository.findAll();
    }


    /**
     * Retrieve all the Job entities from the database.
     *
     * @param netId NetId of the request creator
     * @param authNetId NetId of the authenticated user
     * @param role role of the request creator
     * @return a list of Job entities containing all "ACCEPTED" jobs in the database.
     * @throws Exception if the NetId is invalid or the creator of the request does not have the admin role.
     */
    public List<Job> getAllScheduledJobs(NetId netId, NetId authNetId, RoleValue role) throws Exception {
        checkNetIdNull(netId);
        checkNetIdAuth(netId, authNetId);
        checkIsAdmin(role);

        List<Job> jobs = new ArrayList<>();
        List<Job> all = jobRepository.findAll();
        for (Job j : all) {
            if (j.getStatus() == Status.ACCEPTED) {
                System.out.println(j.getJobId());
                jobs.add(j);
            }
        }

        return jobs;
    }

    /**
     * Populate a JobResponseModel DTO.
     *
     * @param id id of the Job
     * @param status status of the Job
     * @param netId netId of the user that created the Job
     * @return JobResponseModel
     */
    public JobResponseModel populateJobResponseModel(long id, Status status, String netId) {
        JobResponseModel jobResponseModel = new JobResponseModel();
        jobResponseModel.setId(id);
        jobResponseModel.setStatus(status);
        jobResponseModel.setNetId(netId);
        return jobResponseModel;
    }
}
