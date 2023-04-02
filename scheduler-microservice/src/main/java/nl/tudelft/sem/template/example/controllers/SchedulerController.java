package nl.tudelft.sem.template.example.controllers;

import commons.FacultyResource;
import commons.FacultyTotalResource;
import commons.Job;
import commons.RoleValue;
import commons.ScheduleJob;
import java.util.List;
import nl.tudelft.sem.template.example.authentication.AuthManager;
import nl.tudelft.sem.template.example.domain.dto.ChangeSchedulingStrategy;
import nl.tudelft.sem.template.example.domain.processing.ProcessingJobsService;
import nl.tudelft.sem.template.example.domain.processing.RemovingJobsService;
import nl.tudelft.sem.template.example.domain.processing.UpdatingJobsService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class SchedulerController {

    private final transient ProcessingJobsService processingJobsService;
    private final transient RemovingJobsService removingJobsService;
    private final transient AuthManager authManager;
    private final transient UpdatingJobsService updatingJobsService;

    /**
     * Constructor for the controller.
     *
     * @param processingJobsService .
     * @param removingJobsService .
     * @param updatingJobsService .
     * @param authManager Spring Security component used to authenticate and authorize the user
     */
    @Autowired
    public SchedulerController(ProcessingJobsService processingJobsService,
                               RemovingJobsService removingJobsService,
                               UpdatingJobsService updatingJobsService,
                               AuthManager authManager) {
        this.processingJobsService = processingJobsService;
        this.removingJobsService = removingJobsService;
        this.updatingJobsService = updatingJobsService;
        this.authManager = authManager;
    }

    /**
     * Allows to request a job to be scheduled.
     *
     * @param job job to be scheduled
     * @return confirmation that a job is now being processed
     */
    @PostMapping("/schedule")
    public ResponseEntity<String> scheduleJob(@RequestBody ScheduleJob job) {
        try {
            processingJobsService.scheduleJob(job);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }

        return ResponseEntity.ok("Processing");
    }

    /**
     * Allow to unschedule a job.
     *
     * @param jobId ID of a job that is to be unscheduled
     * @return response indicating if the operation was successful
     */
    @PostMapping("/unschedule")
    public ResponseEntity<String> unscheduleJob(@RequestBody long jobId) {
        boolean status = removingJobsService.removeJob(jobId);
        if (!status) {
            return ResponseEntity.badRequest().body("Job with " + jobId + " id could not be unscheduled.");
        }

        return ResponseEntity.ok("Job was unscheduled.");
    }

    /**
     * The api GET endpoint to get all faculty resources from the next day.
     *
     * @return list of Jobs to be scheduled
     */
    @GetMapping(path = "/all-resources-next-day")
    public ResponseEntity<List<FacultyTotalResource>> getAllResourcesNextDay() {
        if (authManager.getRole().getRoleValue() != RoleValue.ADMIN) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        List<FacultyTotalResource> res = processingJobsService.getAllResourcesNextDay();
        return ResponseEntity.ok(res);
    }

    @PostMapping("/resource-update")
    public ResponseEntity<String> updateScheduledJobs(@RequestBody FacultyResource facultyResource) {
        updatingJobsService.updateSchedule(facultyResource);
        return ResponseEntity.ok("Updated");
    }

    /**
     * Allows users with ADMIN role to change the scheduling strategy used by the
     * scheduler.
     *
     * @param changeSchedulingStrategy DTO with a strategy name
     * @return OK if successfully changed, error otherwise.
     */
    @PostMapping("/change-scheduling-strategy")
    public ResponseEntity<String> changeSchedulingStrategy(@RequestBody ChangeSchedulingStrategy
                                                                       changeSchedulingStrategy) {
        if (!authManager.getRole().toString().equals("ADMIN")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            processingJobsService.setSchedulingStrategy(changeSchedulingStrategy.getStrategy());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }

        return ResponseEntity.ok("Changed");
    }
}
