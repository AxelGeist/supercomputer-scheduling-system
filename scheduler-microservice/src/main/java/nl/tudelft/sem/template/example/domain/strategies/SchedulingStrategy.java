package nl.tudelft.sem.template.example.domain.strategies;

import commons.ScheduleJob;
import java.time.LocalDate;
import java.util.List;
import nl.tudelft.sem.template.example.domain.db.ScheduledInstance;

public interface SchedulingStrategy {
    List<ScheduledInstance> scheduleBetween(ScheduleJob job, LocalDate start, LocalDate end);
}
