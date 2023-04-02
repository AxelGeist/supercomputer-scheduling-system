package nl.tudelft.sem.template.example.db;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;
import nl.tudelft.sem.template.example.domain.db.ScheduledInstance;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ScheduledInstanceTest {
    ScheduledInstance scheduledInstance;

    @BeforeEach
    public void init() {
        scheduledInstance = new ScheduledInstance(1L, "EEMCS", "EEMCS",
                10, 10, 10, LocalDate.now());
    }

    @Test
    public void getJobIdTest() {
        assertThat(scheduledInstance.getJobId()).isEqualTo(1L);
    }

    @Test
    public void getJobFacultyTest() {
        assertThat(scheduledInstance.getJobFaculty()).isEqualTo("EEMCS");
    }

    @Test
    public void getFacultyTest() {
        assertThat(scheduledInstance.getFaculty()).isEqualTo("EEMCS");
    }

    @Test
    public void getCpuUsageTest() {
        assertThat(scheduledInstance.getCpuUsage()).isEqualTo(10);
    }

    @Test
    public void getGpuUsageTest() {
        assertThat(scheduledInstance.getGpuUsage()).isEqualTo(10);
    }

    @Test
    public void getMemoryUsageTest() {
        assertThat(scheduledInstance.getMemoryUsage()).isEqualTo(10);
    }
}
