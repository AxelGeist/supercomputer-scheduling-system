package commons;

import java.time.LocalDate;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "jobs")
@NoArgsConstructor
public class Job {

    @Id
    @Column(name = "job_id", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long jobId;

    @Column(name = "net_id", nullable = false)
    @Convert(converter = NetIdAttributeConverter.class)
    private NetId netId;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "faculty", nullable = false)
    @Convert(converter = FacultyAttributeConverter.class)
    private Faculty faculty;

    @Column(name = "cpu_usage", nullable = false)
    private int cpuUsage;

    @Column(name = "gpu_usage", nullable = false)
    private int gpuUsage;

    @Column(name = "memory_usage", nullable = false)
    private int memoryUsage;

    @Column(name = "status", nullable = false)
    private Status status;

    @Column(name = "preferred_date", nullable = false)
    private LocalDate preferredDate;

    @Column(name = "date_created", nullable = false)
    private LocalDate dateCreated;


    /**
     * Constructor for the Job class, which represents jobs that need to be done.
     *
     * @param netId the netId of the user creating the job
     * @param cpuUsage the amount of cpu units needed
     * @param gpuUsage the amount of gpu units needed
     * @param memoryUsage the amount of memory units needed
     */
    public Job(NetId netId, Faculty faculty, String description,
               int cpuUsage, int gpuUsage, int memoryUsage, LocalDate preferredDate) {
        this.netId = netId;
        this.description = description;
        this.faculty = faculty;
        this.cpuUsage = cpuUsage;
        this.gpuUsage = gpuUsage;
        this.memoryUsage = memoryUsage;
        this.status = Status.PENDING;
        this.preferredDate = preferredDate;
        this.dateCreated = LocalDate.now();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getJobId() {
        return jobId;
    }

    public void setJobId(long jobId) {
        this.jobId = jobId;
    }

    public NetId getNetId() {
        return netId;
    }

    public void setNetId(NetId netId) {
        this.netId = netId;
    }

    public Faculty getFaculty() {
        return this.faculty;
    }

    public void setFaculty(Faculty faculty) {
        this.faculty = faculty;
    }

    public int getCpuUsage() {
        return cpuUsage;
    }

    public void setCpuUsage(int cpuUsage) {
        this.cpuUsage = cpuUsage;
    }

    public int getGpuUsage() {
        return gpuUsage;
    }

    public void setGpuUsage(int gpuUsage) {
        this.gpuUsage = gpuUsage;
    }

    public int getMemoryUsage() {
        return memoryUsage;
    }

    public void setMemoryUsage(int memoryUsage) {
        this.memoryUsage = memoryUsage;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public LocalDate getPreferredDate() {
        return preferredDate;
    }

    public void setPreferredDate(LocalDate preferredDate) {
        this.preferredDate = preferredDate;
    }

    public LocalDate getDateCreated() {
        return dateCreated;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Job job = (Job) o;
        return jobId == job.jobId && Objects.equals(faculty, job.faculty) && cpuUsage == job.cpuUsage
                && gpuUsage == job.gpuUsage && memoryUsage == job.memoryUsage
                && Objects.equals(netId, job.netId) && Objects.equals(status, job.status)
                && Objects.equals(preferredDate, job.preferredDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(jobId, netId, faculty, cpuUsage,
                gpuUsage, memoryUsage, status, preferredDate, dateCreated);
    }
}
