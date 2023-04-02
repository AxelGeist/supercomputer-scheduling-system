package nl.tudelft.sem.template.example.domain.processing;

import commons.ScheduleJob;
import exceptions.ResourceBiggerThanCpuException;
import java.time.LocalDate;
import java.time.LocalTime;

public class SchedulingCheckService {

    /**
     * checks if CPU is at least as big as CPU and Memory.
     *
     * @param j a ScheduleJob DTO of a Job to be scheduled
     * @throws ResourceBiggerThanCpuException if CPU is smaller than GPU or Memory
     */
    public void verifyCpuBiggerThanMaxOfGpuOrMemory(ScheduleJob j) throws ResourceBiggerThanCpuException {
        // verify the CPU >= Max(GPU, Memory) requirement
        if (j.getCpuUsage() < Math.max(j.getGpuUsage(), j.getMemoryUsage())) {
            throw new ResourceBiggerThanCpuException("GPU or Memory");
        }
    }

    /**
     * Find the day, after which a Job can potentially be scheduled (including the day).
     *
     * @param currentTime the time right now
     * @return the day, after which a Job can potentially be scheduled (including the day).
     */
    public LocalDate scheduleAfterInclusive(LocalTime currentTime) {
        if (isFiveMinutesBeforeDayStarts(currentTime)) {
            return LocalDate.now().plusDays(2);
        } else {
            return LocalDate.now().plusDays(1);
        }
    }

    /**
     * Checks if it is 5 minutes before a new day starts.
     *
     * @return true if the current time is between 25:55 (including) and 00:00 (excluding)
     */
    public boolean isFiveMinutesBeforeDayStarts(LocalTime currentTime) {
        LocalTime startTime = LocalTime.of(23, 55);
        return currentTime.isAfter(startTime) || currentTime.equals(startTime);
    }



}
