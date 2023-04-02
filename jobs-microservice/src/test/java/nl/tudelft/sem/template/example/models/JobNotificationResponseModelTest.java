package nl.tudelft.sem.template.example.models;

import static org.assertj.core.api.Assertions.assertThat;

import commons.Status;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


class JobNotificationResponseModelTest {

    JobNotificationResponseModel jnrm;

    @BeforeEach
    void setUp() {
        jnrm = new JobNotificationResponseModel(1L, Status.PENDING, LocalDate.now().plusDays(2));
    }

    @Test
    void getJobId() {
        assertThat(jnrm.getJobId()).isEqualTo(1L);
    }

    @Test
    void getStatus() {
        assertThat(jnrm.getStatus()).isEqualTo(Status.PENDING);
    }

    @Test
    void getScheduleDate() {
        assertThat(jnrm.getScheduleDate()).isEqualTo(LocalDate.now().plusDays(2));
    }
}