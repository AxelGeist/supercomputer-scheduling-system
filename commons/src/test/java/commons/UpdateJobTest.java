package commons;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


class UpdateJobTest {

    UpdateJob uj;

    @BeforeEach
    void setUp() {
        uj = new UpdateJob(1L, "PENDING", LocalDate.now());
    }

    @Test
    void getId() {
        assertThat(uj.getId()).isEqualTo(1L);
    }

    @Test
    void getStatus() {
        assertThat(uj.getStatus()).isEqualTo("PENDING");
    }

    @Test
    void getScheduleDate() {
        assertThat(uj.getScheduleDate()).isEqualTo(LocalDate.now());
    }

    @Test
    void setId() {
        uj.setId(99L);
        assertThat(uj.getId()).isEqualTo(99L);
    }

    @Test
    void setStatus() {
        uj.setStatus("DONE");
        assertThat(uj.getStatus()).isEqualTo("DONE");
    }

    @Test
    void setScheduleDate() {
        uj.setScheduleDate(LocalDate.now().plusWeeks(1));
        assertThat(uj.getScheduleDate()).isEqualTo(LocalDate.now().plusWeeks(1));
    }
}