package commons;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


class FacultyResourceTest {

    FacultyResource fr;

    @BeforeEach
    void setUp() {
        LocalDate today = LocalDate.now();
        fr = new FacultyResource();
        fr.setFaculty("EEMCS");
        fr.setDate(today);
        fr.setCpuUsage(3);
        fr.setGpuUsage(2);
        fr.setMemoryUsage(1);
    }

    @Test
    void constructorTest() {
        LocalDate tmrw = LocalDate.now().plusDays(1);
        fr = new FacultyResource();
        fr.setFaculty("EE");
        fr.setDate(tmrw);
        fr.setCpuUsage(10);
        fr.setGpuUsage(2);
        fr.setMemoryUsage(1);

        assertNotNull(fr);

        assertThat(fr.getFaculty()).isEqualTo("EE");
        assertThat(fr.getDate()).isEqualTo(tmrw);
        assertThat(fr.getCpuUsage()).isEqualTo(10);
        assertThat(fr.getGpuUsage()).isEqualTo(2);
        assertThat(fr.getMemoryUsage()).isEqualTo(1);
    }


    @Test
    void getFaculty() {
        assertThat(fr.getFaculty()).isEqualTo("EEMCS");
    }

    @Test
    void getDate() {
        assertThat(fr.getDate()).isEqualTo(LocalDate.now());
    }

    @Test
    void getCpuUsage() {
        assertThat(fr.getCpuUsage()).isEqualTo(3);
    }

    @Test
    void getGpuUsage() {
        assertThat(fr.getGpuUsage()).isEqualTo(2);
    }

    @Test
    void getMemoryUsage() {
        assertThat(fr.getMemoryUsage()).isEqualTo(1);
    }
}