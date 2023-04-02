package commons;

import java.time.LocalDate;

public class ScheduleJob {
    private final long jobId;
    private final Faculty faculty;
    private final LocalDate scheduleBefore;
    private final int cpuUsage;
    private final int gpuUsage;
    private final int memoryUsage;

    /**
     * Constructs ScheduleJob object.
     *
     * @param jobId id of a job
     * @param faculty faculty of the user that scheduled the job
     * @param scheduleBefore date before which the job should be scheduled
     * @param cpuUsage the number of cpu units needed
     * @param gpuUsage the number of gpu units needed
     * @param memoryUsage the number of memory units needed
     */
    public ScheduleJob(long jobId, Faculty faculty, LocalDate scheduleBefore, int cpuUsage, int gpuUsage, int memoryUsage) {
        this.jobId = jobId;
        this.faculty = faculty;
        this.scheduleBefore = scheduleBefore;
        this.cpuUsage = cpuUsage;
        this.gpuUsage = gpuUsage;
        this.memoryUsage = memoryUsage;
    }

    public long getJobId() {
        return jobId;
    }

    public Faculty getFaculty() {
        return faculty;
    }

    public LocalDate getScheduleBefore() {
        return scheduleBefore;
    }

    public int getCpuUsage() {
        return cpuUsage;
    }

    public int getGpuUsage() {
        return gpuUsage;
    }

    public int getMemoryUsage() {
        return memoryUsage;
    }
}
