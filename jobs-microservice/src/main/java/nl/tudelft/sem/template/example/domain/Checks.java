package nl.tudelft.sem.template.example.domain;

import commons.Job;
import commons.NetId;
import commons.RoleValue;
import exceptions.InvalidIdException;
import exceptions.InvalidNetIdException;
import exceptions.InvalidResourcesException;
import exceptions.ResourceBiggerThanCpuException;
import java.util.Optional;
import org.springframework.security.authentication.BadCredentialsException;

public class Checks {

    /**
     * Checks if net id provided is null.
     *
     * @param netId netId
     * @throws InvalidNetIdException exception
     */
    public void checkNetIdNull(NetId netId) throws InvalidNetIdException {
        if (netId == null) {
            throw new InvalidNetIdException("null");
        }
    }

    /**
     * Checks if net id matches with authenticated net id.
     *
     * @param netId netId
     * @param auth authenticated netId
     * @throws InvalidNetIdException exception
     */
    public void checkNetIdAuth(NetId netId, NetId auth) throws InvalidNetIdException {
        if (!netId.toString().equals(auth.toString())) {
            throw new InvalidNetIdException(netId.toString());
        }
    }

    /**
     * Check if role is admin.
     *
     * @param roleValue role of the user
     * @throws BadCredentialsException exception
     */
    public void checkIsAdmin(RoleValue roleValue) throws BadCredentialsException {
        if (roleValue != RoleValue.ADMIN) {
            throw new BadCredentialsException(roleValue.toString());
        }
    }

    /**
     * Check if role is employee.
     *
     * @param roleValue role of the user
     * @throws BadCredentialsException exception
     */
    public void checkIsEmployee(RoleValue roleValue) throws BadCredentialsException {
        if (roleValue != RoleValue.EMPLOYEE) {
            throw new BadCredentialsException(roleValue.toString());
        }
    }

    /**
     * Check if job is empty.
     *
     * @param job job
     * @param jobId job id
     * @throws InvalidIdException exception
     */
    public void checkJobEmpty(Optional<Job> job, long jobId) throws InvalidIdException {
        if (job.isEmpty()) {
            throw new InvalidIdException(jobId);
        }
    }

    /**
     * Checks if none of the resources are negative and if cpu usage is greater than memory usage or gpu usage.
     *
     * @param job job
     * @throws InvalidResourcesException exception
     * @throws ResourceBiggerThanCpuException exception
     */
    public void checkResourcesJob(Job job) throws InvalidResourcesException, ResourceBiggerThanCpuException {
        if (job.getCpuUsage() < 0 || job.getGpuUsage() < 0 || job.getMemoryUsage() < 0) {
            throw new InvalidResourcesException(Math.min(job.getCpuUsage(),
                Math.min(job.getGpuUsage(), job.getMemoryUsage())));
        }
        if (job.getCpuUsage() < Math.max(job.getGpuUsage(), job.getMemoryUsage())) {
            String resource = job.getGpuUsage() > job.getMemoryUsage() ? "GPU" : "Memory";
            throw new ResourceBiggerThanCpuException(resource);
        }
    }

}
