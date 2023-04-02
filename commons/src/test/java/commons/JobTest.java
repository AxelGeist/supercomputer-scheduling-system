package commons;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JobTest {

    NetId netId1;
    Job job1;
    LocalDate pref;
    Faculty faculty1;

    @BeforeEach
    void setUp() throws Exception {
        netId1 = new NetId("ageist");
        pref = LocalDate.now();
        faculty1 = new Faculty("EEMCS");
        job1 = new Job(netId1, faculty1, "d", 3, 2, 1, pref);
    }

    @Test
    void constructorTest() {
        job1 = new Job(netId1, faculty1, "d", 3, 2, 1, LocalDate.now());
        assertNotNull(job1);
        job1 = new Job(netId1, faculty1, "d", 3, 2, 1, LocalDate.now());
    }

    @Test
    void getJobId() {
        assertThat(job1.getJobId()).isEqualTo(0L);
    }

    @Test
    void setJobId() {
        job1.setJobId(2L);
        assertThat(job1.getJobId()).isEqualTo(2L);
    }

    @Test
    void getNetId() {
        assertThat(job1.getNetId()).isEqualTo(netId1);
    }

    @Test
    void setNetId() {
        NetId newNetId = new NetId("piotr");
        job1.setNetId(newNetId);
        assertThat(job1.getNetId()).isEqualTo(newNetId);
    }

    @Test
    void getFaculty() {
        assertThat(job1.getFaculty()).isEqualTo(new Faculty("EEMCS"));
    }

    @Test
    void setFaculty() {
        Faculty faculty2 = new Faculty("ARCH");
        job1.setFaculty(faculty2);
        assertThat(job1.getFaculty()).isEqualTo(new Faculty("ARCH"));
    }

    @Test
    void getCpuUsage() {
        assertThat(job1.getCpuUsage()).isEqualTo(3);
    }

    @Test
    void setCpuUsage() {
        job1.setCpuUsage(4);
        assertThat(job1.getCpuUsage()).isEqualTo(4);
    }

    @Test
    void getGpuUsage() {
        assertThat(job1.getGpuUsage()).isEqualTo(2);
    }

    @Test
    void setGpuUsage() {
        job1.setGpuUsage(1);
        assertThat(job1.getGpuUsage()).isEqualTo(1);
    }

    @Test
    void getMemoryUsage() {
        assertThat(job1.getMemoryUsage()).isEqualTo(1);
    }

    @Test
    void setMemoryUsage() {
        job1.setMemoryUsage(2);
        assertThat(job1.getMemoryUsage()).isEqualTo(2);
    }

    @Test
    void getPreferedDate() {
        assertThat(job1.getPreferredDate()).isEqualTo(pref);
    }

    @Test
    void setPreferedDate() {
        LocalDate newD = LocalDate.now().plusDays(1);
        job1.setPreferredDate(newD);
        assertThat(job1.getPreferredDate()).isEqualTo(newD);
    }

    @Test
    void getScheduleDate() {
        assertThat(job1.getPreferredDate()).isEqualTo(LocalDate.now());
    }

    @Test
    void setScheduleDate() {
        job1.setPreferredDate(LocalDate.now().plusDays(3));
        assertThat(job1.getPreferredDate()).isEqualTo(LocalDate.now().plusDays(3));
    }

    @Test
    void getStatus() {
    }

    @Test
    void setStatus() {
        job1.setStatus(Status.ACCEPTED);
        assertThat(job1.getStatus()).isEqualTo(Status.ACCEPTED);
    }

    @Test
    void equals() {
        Job job2 = new Job(netId1, faculty1, "d", 3, 2, 1, LocalDate.now());
        assertTrue(job1.equals(job2));

        job2.setPreferredDate(LocalDate.now().plusDays(3));
        assertFalse(job1.equals(job2));

        Job job3 = null;
        assertFalse(job1.equals(job3));

        Job job4 = new Job(netId1, faculty1, "d", 2, 2, 1, LocalDate.now());
        assertFalse(job1.equals(job4));

        Job job5 = new Job(netId1, faculty1, "d", 1, 2, 1, LocalDate.now());
        assertFalse(job1.equals(job4));

        Job job6 = job1;
        assertTrue(job1.equals(job6));


    }

}