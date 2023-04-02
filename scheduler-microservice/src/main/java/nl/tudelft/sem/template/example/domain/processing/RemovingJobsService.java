package nl.tudelft.sem.template.example.domain.processing;

import java.util.List;
import nl.tudelft.sem.template.example.domain.db.ScheduledInstance;
import nl.tudelft.sem.template.example.domain.db.ScheduledInstanceRepository;
import org.springframework.stereotype.Service;

@Service
public class RemovingJobsService {

    private final transient ScheduledInstanceRepository scheduledInstanceRepository;

    RemovingJobsService(ScheduledInstanceRepository scheduledInstanceRepository) {
        this.scheduledInstanceRepository = scheduledInstanceRepository;
    }

    /**
     * Removes the job and frees the allocated resources.
     *
     * @param jobId ID of a job that is to be removed
     * @return true if the job was properly removed
     */
    public boolean removeJob(long jobId) {
        // 1. Search for Requests in the database
        List<ScheduledInstance> instances = scheduledInstanceRepository.findAllByJobId(jobId);
        if (instances.isEmpty()) {
            return false;
        }

        // 2. Remove them from the db
        for (var i : instances) {
            System.out.println("Removing scheduled instance " + i.getId());
            scheduledInstanceRepository.deleteById(i.getId());
        }

        // 3. Return true if everything went well
        return true;
    }
}
