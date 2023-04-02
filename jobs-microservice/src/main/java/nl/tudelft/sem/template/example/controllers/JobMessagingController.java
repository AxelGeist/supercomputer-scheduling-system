package nl.tudelft.sem.template.example.controllers;

import commons.Job;
import commons.NetId;
import commons.Status;
import commons.UpdateJob;
import exceptions.InvalidIdException;
import exceptions.InvalidNetIdException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import nl.tudelft.sem.template.example.authentication.AuthManager;
import nl.tudelft.sem.template.example.domain.JobService;
import nl.tudelft.sem.template.example.models.IdRequestModel;
import nl.tudelft.sem.template.example.models.JobNotificationResponseModel;
import nl.tudelft.sem.template.example.models.StatusResponseModel;
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
public class JobMessagingController {


    private final transient AuthManager authManager;
    private final transient JobService jobService;
    private static final String invalidId = "INVALID_ID";


    /**
     * Constructor of JobMessagingController.
     *
     * @param jobService  the service which handles the communication with the database & scheduler microservice
     * @param authManager Spring Security component used to authenticate and authorize the user
     */
    @Autowired
    public JobMessagingController(AuthManager authManager, JobService jobService) {
        this.authManager = authManager;
        this.jobService = jobService;
    }

    /**
     * The api GET endpoint to notify the User about the Job status and schedule date.
     *
     * @return list of Jobs to be scheduled
     */
    @GetMapping(path = "/getJobNotification")
    public ResponseEntity<List<JobNotificationResponseModel>> getJobNotification() throws Exception {
        try {
            NetId netId = new NetId(authManager.getNetId());
            NetId authNetId = new NetId(authManager.getNetId());

            List<Job> jobs = this.jobService.collectJobsByNetId(netId, authNetId);
            List<JobNotificationResponseModel> responseModels = jobs.stream()
                .map(x -> new JobNotificationResponseModel(x.getJobId(),
                    x.getStatus(), x.getPreferredDate())).collect(Collectors.toList());

            return ResponseEntity.ok(responseModels);
        } catch (InvalidNetIdException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, invalidId, e);
        } catch (BadCredentialsException e) {
            throw new ResponseStatusException(HttpStatus.METHOD_NOT_ALLOWED, "BAD_CREDENTIALS", e);
        }
    }


    /**
     * The api GET endpoint to get the status of the requested Job.
     *
     * @param request the id of a Job stored in the database.
     * @return status of the job
     */
    @GetMapping(path = "/jobStatus")
    public ResponseEntity<StatusResponseModel> getJobStatusById(@RequestBody IdRequestModel request) throws Exception {
        try {
            NetId authNetId = new NetId(authManager.getNetId());
            long jobId = request.getId();
            Status status = this.jobService.getJobStatus(authNetId, authNetId, jobId);
            StatusResponseModel statusResponseModel = new StatusResponseModel();
            statusResponseModel.setStatus(status.toString());
            return ResponseEntity.ok(statusResponseModel);
        } catch (InvalidNetIdException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, invalidId, e);
        }
    }

    /**
     * REST API post request to update the information about a Job.
     *
     * @param request the parameters to find and update
     * @return 200 HTTP CODE if everything works as planned
     */
    @PostMapping("/update")
    public ResponseEntity updateJob(@RequestBody UpdateJob request) throws Exception {
        try {
            long id = request.getId();
            Status status = Status.valueOf(request.getStatus());
            System.out.println(request.getScheduleDate());
            LocalDate localDate = request.getScheduleDate();
            System.out.println(localDate);

            this.jobService.updateJob(id, status, localDate);
        } catch (InvalidIdException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, invalidId, e);
        }
        return ResponseEntity.ok().build();
    }
}
