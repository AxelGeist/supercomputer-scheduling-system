package nl.tudelft.sem.template.example.domain;


import commons.Job;
import commons.ScheduleJob;
import commons.Status;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
public class AutomaticApproveJobsComponent {

    public transient JobService jobService;
    public transient JobRepository jobRepository;


    @Autowired
    public AutomaticApproveJobsComponent(JobService jobService, JobRepository jobRepository) {
        this.jobService = jobService;
        this.jobRepository = jobRepository;
    }

    public void setJobService(JobService jobservice) {
        this.jobService = jobService;
    }

    /**
     * Approves & schedule automatically every day at 6pm all PENDING jobs that are due tomorrow.
     *
     * @throws InvalidScheduleJobException if scheduleJob is null
     */
    @Scheduled(cron = "0 0 18 * * ?")
    public void approveJobsAfter6pm() throws InvalidScheduleJobException, ResponseEntityException {

        // 1. get all pending Jobs that are due tomorrow
        List<Job> filteredPendingJobs = jobRepository.findByStatusAndPreferredDate(
                Status.PENDING, LocalDate.now().plusDays(1));

        // 2. sort jobs according to their creation date
        List<Job> filteredSortedPendingJobs = sortJobsAccordingCreationDate(filteredPendingJobs);

        // 3. approve & send jobs to scheduler
        for (Job job : filteredSortedPendingJobs) {
            ScheduleJob scheduleJob = new ScheduleJob(job.getJobId(),
                    job.getFaculty(), job.getPreferredDate(),
                    job.getCpuUsage(), job.getGpuUsage(), job.getMemoryUsage());

            jobService.scheduleJob(scheduleJob);
        }
    }

    /**
     * Filter the pending jobs only.
     *
     * @param pendingJobs list of Jobs that have the status "PENDING"
     * @return a sorted (according to creationDate) list of jobs that are due tomorrow
     */
    public List<Job> sortJobsAccordingCreationDate(List<Job> pendingJobs) {
        return pendingJobs.stream()
                .sorted((j1, j2) -> j1.getDateCreated().compareTo(j2.getDateCreated()))
                .collect(Collectors.toList());
    }
}
