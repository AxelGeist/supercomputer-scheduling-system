package nl.tudelft.sem.template.example.models;

import lombok.Data;

/**
 * Request model for the Job class.
 */
@Data
public class JobRequestModel {
    private String netId;
    private String faculty;
    private String description;
    private int cpuUsage;
    private int gpuUsage;
    private int memoryUsage;

    public JobRequestModel() {
    }

    /**
     * Constructor for the request model when creating a job.
     *
     * @param netId netId of the job issuer
     * @param cpuUsage CPU usage of the job.
     * @param gpuUsage GPU usage of the job.
     * @param memoryUsage memory usage of the job.
     */
    public JobRequestModel(String netId, String faculty, int cpuUsage, int gpuUsage, int memoryUsage) {
        this.netId = netId;
        this.faculty = faculty;
        this.cpuUsage = cpuUsage;
        this.gpuUsage = gpuUsage;
        this.memoryUsage = memoryUsage;
    }

    public String getNetId() {
        return netId;
    }

    public void setNetId(String netId) {
        this.netId = netId;
    }

    public String getFaculty() {
        return this.faculty;
    }

    public void setFaculty(String faculty) {
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
}
