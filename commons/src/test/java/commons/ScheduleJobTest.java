package commons;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


class ScheduleJobTest {

    ScheduleJob sj;

    @BeforeEach
    void setUp() {
        sj = new ScheduleJob(1L, new Faculty("ARCH"), LocalDate.now().plusDays(4), 3, 2, 1);
    }

    @Test
    void getJobId() {
        assertThat(sj.getJobId()).isEqualTo(1L);
    }

    @Test
    void getFaculty() {
        assertThat(sj.getFaculty()).isEqualTo(new Faculty("ARCH"));
    }

    @Test
    void getScheduleBefore() {
        assertThat(sj.getScheduleBefore()).isEqualTo(LocalDate.now().plusDays(4));
    }

    @Test
    void getCpuUsage() {
        assertThat(sj.getCpuUsage()).isEqualTo(3);
    }

    @Test
    void getGpuUsage() {
        assertThat(sj.getGpuUsage()).isEqualTo(2);
    }

    @Test
    void getMemoryUsage() {
        assertThat(sj.getMemoryUsage()).isEqualTo(1);
    }
}