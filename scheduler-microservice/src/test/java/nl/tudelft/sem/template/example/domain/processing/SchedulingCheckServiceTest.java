package nl.tudelft.sem.template.example.domain.processing;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import commons.Faculty;
import commons.ScheduleJob;
import exceptions.ResourceBiggerThanCpuException;
import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SchedulingCheckServiceTest {

    private SchedulingCheckService schedulingCheckService;
    private ProcessingJobsService processingJobsService;

    @BeforeEach
    void setUp() {
        schedulingCheckService = new SchedulingCheckService();
    }

    @Test
    public void verifyCpuBiggerThanMaxOfGpuOrMemoryTest() {
        ScheduleJob scheduleJob = new ScheduleJob(1, new Faculty("EEMCS"), LocalDate.now().plusDays(1),
                1, 1, 2);
        Exception e = assertThrows(ResourceBiggerThanCpuException.class,
                () -> schedulingCheckService.verifyCpuBiggerThanMaxOfGpuOrMemory(scheduleJob));
        assertThat(e.getMessage()).isEqualTo("GPU or Memory usage cannot be greater than the CPU usage.");

        ScheduleJob scheduleJob2 = new ScheduleJob(1, new Faculty("EEMCS"), LocalDate.now().plusDays(1),
                1, 2, 1);
        Exception e2 = assertThrows(ResourceBiggerThanCpuException.class,
                () -> schedulingCheckService.verifyCpuBiggerThanMaxOfGpuOrMemory(scheduleJob2));
        assertThat(e2.getMessage()).isEqualTo("GPU or Memory usage cannot be greater than the CPU usage.");
    }

    @Test
    public void scheduleAfterInclusiveTest() {
        assertThat(schedulingCheckService.scheduleAfterInclusive(LocalTime.of(23, 55)))
                .isEqualTo(LocalDate.now().plusDays(2));
        assertThat(schedulingCheckService.scheduleAfterInclusive(LocalTime.of(23, 59)))
                .isEqualTo(LocalDate.now().plusDays(2));
        assertThat(schedulingCheckService.scheduleAfterInclusive(LocalTime.of(23, 55).minusMinutes(1)))
                .isEqualTo(LocalDate.now().plusDays(1));
        assertThat(schedulingCheckService.scheduleAfterInclusive(LocalTime.of(23, 59).plusMinutes(1)))
                .isEqualTo(LocalDate.now().plusDays(1));
    }

    @Test
    public void isFiveMinutesTrue() {
        assertThat(schedulingCheckService.isFiveMinutesBeforeDayStarts(LocalTime.of(23, 55))).isTrue();
    }

    @Test
    public void isFiveMinutesTrue2() {
        assertThat(schedulingCheckService.isFiveMinutesBeforeDayStarts(LocalTime.of(23, 59))).isTrue();
    }

    @Test
    public void isFiveMinutesFalse() {
        assertThat(schedulingCheckService.isFiveMinutesBeforeDayStarts(LocalTime.of(23, 55).minusMinutes(1))).isFalse();
    }

    @Test
    public void isFiveMinutesFalse2() {
        assertThat(schedulingCheckService.isFiveMinutesBeforeDayStarts(LocalTime.of(23, 59).plusMinutes(1))).isFalse();
    }
}