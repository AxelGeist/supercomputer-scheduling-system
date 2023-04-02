package nl.tudelft.sem.template.example.controllers;

import commons.Job;
import commons.NetId;
import commons.Role;
import commons.ScheduleJob;
import commons.Status;
import exceptions.InvalidIdException;
import exceptions.InvalidNetIdException;
import nl.tudelft.sem.template.example.authentication.AuthManager;
import nl.tudelft.sem.template.example.chain.ChainService;
import nl.tudelft.sem.template.example.domain.JobRepository;
import nl.tudelft.sem.template.example.domain.JobService;
import nl.tudelft.sem.template.example.models.ApproveRequestModel;
import nl.tudelft.sem.template.example.models.JobResponseModel;
import nl.tudelft.sem.template.example.models.RejectRequestModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class ChainController {

    private final transient AuthManager authManager;
    private final transient ChainService chainService;
    private final transient JobService jobService;
    private final transient JobRepository jobRepository;


    /**
     * Constructor of the Chain controller.
     *
     * @param authManager Spring Security component used to authenticate and authorize the user
     * @param chainService the service which handles the approval/rejection of jobs
     * @param jobService  the service which handles the communication with the database & scheduler microservice
     */
    @Autowired
    public ChainController(AuthManager authManager, ChainService chainService,
                           JobService jobService, JobRepository jobRepository) {
        this.authManager = authManager;
        this.chainService = chainService;
        this.jobService = jobService;
        this.jobRepository = jobRepository;
    }

    /**
     * Approve the scheduling of a Job by a faculty account using Chain of Responsibility.
     *
     * @param request ApproveRequestModel needed to identify the Job
     * @return JobResponseModel with information about the approved Job
     */
    @PostMapping("/approve")
    public ResponseEntity<JobResponseModel> approveJob(@RequestBody ApproveRequestModel request) {
        System.out.println(request.getId());
        try {
            NetId netId = new NetId(authManager.getNetId());
            Role role = authManager.getRole();
            Long id = request.getId();
            Job approvedJob = chainService.approveJob(netId, role.getRoleValue(), id);
            jobRepository.save(approvedJob);
            if (approvedJob.getStatus() == Status.ACCEPTED) {
                ScheduleJob scheduleJob = new ScheduleJob(id, approvedJob.getFaculty(),
                        approvedJob.getPreferredDate(), approvedJob.getCpuUsage(), approvedJob.getGpuUsage(),
                        approvedJob.getMemoryUsage());
                jobService.scheduleJob(scheduleJob);
            }
            JobResponseModel jobResponseModel = new JobResponseModel();
            jobResponseModel.setId(id);
            jobResponseModel.setStatus(approvedJob.getStatus());
            jobResponseModel.setNetId(approvedJob.getNetId().toString());
            return ResponseEntity.ok(jobResponseModel);
        } catch (InvalidIdException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "INVALID_ID", e);
        } catch (InvalidNetIdException e) {
            throw new ResponseStatusException(HttpStatus.METHOD_NOT_ALLOWED, "BAD_CREDENTIALS", e);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "BAD_REQUEST", e);
        }
    }

    /**
     * Reject the scheduling of a Job by a faculty account using Chain of Responsibility.
     *
     * @param request RejectRequestModel needed to identify a Job
     * @return JobResponseModel with information about the rejected Job.
     */
    @PostMapping("/reject")
    public ResponseEntity<JobResponseModel> rejectJob(@RequestBody RejectRequestModel request) {
        try {
            NetId netId = new NetId(authManager.getNetId());
            Role role = authManager.getRole();
            Long id = request.getId();
            Job rejectedJob = chainService.rejectJob(netId, role.getRoleValue(), id);
            jobRepository.save(rejectedJob);
            JobResponseModel jobResponseModel = new JobResponseModel();
            jobResponseModel.setNetId(rejectedJob.getNetId().toString());
            jobResponseModel.setStatus(rejectedJob.getStatus());
            jobResponseModel.setId(id);
            return ResponseEntity.ok(jobResponseModel);
        } catch (InvalidIdException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "INVALID_ID", e);
        } catch (InvalidNetIdException e) {
            throw new ResponseStatusException(HttpStatus.METHOD_NOT_ALLOWED, "BAD_CREDENTIALS", e);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "BAD_REQUEST", e);
        }
    }
}
