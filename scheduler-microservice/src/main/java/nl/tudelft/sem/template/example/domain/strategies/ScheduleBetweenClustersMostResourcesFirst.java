package nl.tudelft.sem.template.example.domain.strategies;

import commons.FacultyResource;
import commons.ScheduleJob;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import nl.tudelft.sem.template.example.domain.ResourceGetter;
import nl.tudelft.sem.template.example.domain.db.ScheduledInstance;
import nl.tudelft.sem.template.example.domain.db.ScheduledInstanceRepository;

public class ScheduleBetweenClustersMostResourcesFirst extends ScheduleWithClusterPriority implements SchedulingStrategy {
    public ScheduleBetweenClustersMostResourcesFirst(ResourceGetter resourceGetter,
                                                     ScheduledInstanceRepository scheduledInstanceRepository) {
        super(resourceGetter, scheduledInstanceRepository);
    }

    @Override
    public List<ScheduledInstance> scheduleBetween(ScheduleJob job, LocalDate start, LocalDate end) {
        return scheduleBetween(job, start, end, Comparator
                .comparingDouble((FacultyResource fc) -> (fc.getCpuUsage() + fc.getGpuUsage() + fc.getMemoryUsage()) / 3.0)
                .reversed());
    }
}
