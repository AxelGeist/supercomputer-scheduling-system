package nl.tudelft.sem.template.example.controllers;


import commons.Faculty;
import commons.Job;
import commons.NetId;
import commons.Role;
import commons.RoleValue;
import commons.Status;
import commons.UpdateJob;
import exceptions.InvalidIdException;
import exceptions.InvalidNetIdException;
import exceptions.InvalidResourcesException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import nl.tudelft.sem.template.example.authentication.AuthManager;
import nl.tudelft.sem.template.example.domain.JobService;
import nl.tudelft.sem.template.example.models.IdRequestModel;
import nl.tudelft.sem.template.example.models.JobRequestModel;
import nl.tudelft.sem.template.example.models.JobResponseModel;
import nl.tudelft.sem.template.example.models.NetIdRequestModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;


@RestController
public class JobController {

    private final transient AuthManager authManager;
    private final transient JobService jobService;
    private static final String invalidId = "INVALID_ID";


    /**
     * Constructor of the Job controller.
     *
     * @param jobService  the service which handles the communication with the database & scheduler microservice
     * @param authManager Spring Security component used to authenticate and authorize the user
     */
    @Autowired
    public JobController(AuthManager authManager, JobService jobService) {
        this.authManager = authManager;
        this.jobService = jobService;
    }

    /**
     * The api GET endpoint to get all Jobs in the database.
     *
     * @return list of Jobs to be scheduled
     */
    @GetMapping(path = "/getAllJobs")
    public ResponseEntity<List<JobResponseModel>> getAllJobs() throws Exception {
        try {
            NetId netId = new NetId(authManager.getNetId());
            NetId authNetId = new NetId(authManager.getNetId());
            RoleValue role = authManager.getRole().getRoleValue();
            List<Job> jobs = this.jobService.getAllJobs(netId, authNetId, role);
            List<JobResponseModel> responseModels = jobs.stream()
                    .map(x -> jobService.populateJobResponseModel(x.getJobId(), x.getStatus(), x.getNetId().toString()))
                .collect(Collectors.toList());
            return ResponseEntity.ok(responseModels);
        } catch (InvalidNetIdException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, invalidId, e);
        } catch (BadCredentialsException e) {
            throw new ResponseStatusException(HttpStatus.METHOD_NOT_ALLOWED, "BAD_CREDENTIALS", e);
        }
    }

    /**
     * The api GET endpoint to get all Jobs belonging to the given netId (user).
     *
     * @param request the parameters for NetId
     * @return list of Jobs belonging to the given netId (user)
     */
    @GetMapping(path = "/getJobs")
    public ResponseEntity<List<JobResponseModel>> getJobsByNetId(@RequestBody NetIdRequestModel request) throws Exception {
        try {
            NetId netId = new NetId(request.getNetId());
            NetId authNetId = new NetId(authManager.getNetId());
            List<Job> jobs = this.jobService.collectJobsByNetId(netId, authNetId);
            List<JobResponseModel> responseModels = jobs.stream()
                .map(x -> jobService.populateJobResponseModel(x.getJobId(), x.getStatus(), x.getNetId().toString()))
                .collect(Collectors.toList());
            return ResponseEntity.ok(responseModels);
        } catch (InvalidNetIdException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, invalidId, e);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "EXCEPTION", e);
        }
    }

    /**
     * The API POST endpoint to create a Job using the JobRequestModel.
     *
     * @param request the parameters used to create a new job.
     * @return 200 ok
     */
    @PostMapping("/addJob")
    public ResponseEntity addJob(@RequestBody JobRequestModel request) throws Exception {
        try {
            Job createdJob = this.jobService.createJob(
                new NetId(request.getNetId()), new NetId(authManager.getNetId()),
                new Faculty(request.getFaculty()), request.getDescription(),
                request.getCpuUsage(), request.getGpuUsage(), request.getMemoryUsage(),
                authManager.getRole().getRoleValue(), LocalDate.now());
            JobResponseModel jobResponseModel = jobService.populateJobResponseModel(createdJob.getJobId(),
                Status.PENDING, createdJob.getNetId().toString());
            return ResponseEntity.ok(jobResponseModel);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }
    
    /**
     * The api POST endpoint to delete a Job.
     *
     * @param jobId the jobId which identifies the job that needs to be deleted
     */
    @PostMapping("/deleteJob")
    public ResponseEntity deleteJob(@RequestBody IdRequestModel jobId) throws Exception {
        try {
            this.jobService.deleteJob(authManager.getNetId(), authManager.getRole().getRoleValue(), jobId.getId());
        } catch (InvalidIdException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, invalidId, e);
        }
        return ResponseEntity.ok().build();
    }

    /**
     * Allow sysadmin to see all scheduled jobs for all days.
     *
     * @return response indicating if the operation was successful
     */
    @GetMapping(path = "/getAllScheduledJobs")
    public ResponseEntity<List<JobResponseModel>> getAllScheduledJobs() throws Exception {
        try {
            NetId netId = new NetId(authManager.getNetId());
            NetId authNetId = new NetId(authManager.getNetId());
            RoleValue role = authManager.getRole().getRoleValue();
            List<Job> jobs = this.jobService.getAllScheduledJobs(netId, authNetId, role);
            List<JobResponseModel> responseModels = jobs.stream()
                .map(x -> jobService.populateJobResponseModel(x.getJobId(), x.getStatus(), x.getNetId().toString()))
                .collect(Collectors.toList());
            return ResponseEntity.ok(responseModels);
        } catch (InvalidNetIdException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, invalidId, e);
        } catch (BadCredentialsException e) {
            throw new ResponseStatusException(HttpStatus.METHOD_NOT_ALLOWED, "BAD_CREDENTIALS", e);
        }
    }




}
