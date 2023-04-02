package nl.tudelft.sem.template.example.models;

import commons.Status;
import java.time.LocalDate;
import lombok.Data;

/**
 * Response Entity model to notify the User about the Job status.
 */
@Data
public class JobNotificationResponseModel {

    private long jobId;
    private Status status;
    private LocalDate scheduleDate;

    /**
     * Response Entity model for the Notification of a Job.
     *
     * @param jobId the id of the Job
     * @param status the status of the Job
     * @param scheduleDate the date of executing the Job
     */
    public JobNotificationResponseModel(long jobId, Status status, LocalDate scheduleDate) {
        this.jobId = jobId;
        this.status = status;
        this.scheduleDate = scheduleDate;
    }

    @Override
    public String toString() {
        String text = "Job with job id " + jobId
                + " is " + status + ".";
        if (status == Status.ACCEPTED) {
            text += " The Job will be executed on the " + scheduleDate.toString() + ".";
        } else if (status == Status.FINISHED) {
            text += " The Job was executed on the " + scheduleDate.toString() + ".";
        } else if (status == Status.PENDING) {
            text += " Please check at another time, if the Job has been scheduled!";
        } else if (status == Status.RUNNING) {
            text += " The Job is executed today.";
        } else if (status == Status.REJECTED) {
            text += " The Job is rejected, since no resources were available.";
        }

        return text;
    }
}
