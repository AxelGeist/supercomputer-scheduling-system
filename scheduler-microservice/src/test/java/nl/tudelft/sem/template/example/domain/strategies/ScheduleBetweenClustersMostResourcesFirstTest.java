package nl.tudelft.sem.template.example.domain.strategies;

import static org.assertj.core.api.Assertions.assertThat;

import commons.Faculty;
import commons.FacultyResource;
import commons.ScheduleJob;
import java.time.LocalDate;
import java.util.List;
import nl.tudelft.sem.template.example.domain.ResourceGetter;
import nl.tudelft.sem.template.example.domain.db.ScheduledInstance;
import nl.tudelft.sem.template.example.domain.db.ScheduledInstanceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ScheduleBetweenClustersMostResourcesFirstTest {

    @MockBean
    ResourceGetter resourceGetter;

    @Autowired
    ScheduledInstanceRepository scheduledInstanceRepository;

    ScheduleBetweenClustersMostResourcesFirst scheduler;

    @BeforeEach
    public void setup() {
        scheduler = new ScheduleBetweenClustersMostResourcesFirst(resourceGetter, scheduledInstanceRepository);
    }

    @Test
    void scheduleJob_inClusterWithMostResourcesFirst_worksCorrectly() {
        String facultyConstant = "EEMCS";
        String facultyConstant2 = "3mE";
        String facultyConstant3 = "4mE";
        LocalDate dateConstant = LocalDate.now().plusDays(1);

        List<FacultyResource> dayOne = List.of(
                new FacultyResource(facultyConstant, dateConstant, 10, 10, 10),
                new FacultyResource(facultyConstant2, dateConstant, 5, 5, 5),
                new FacultyResource(facultyConstant3, dateConstant, 4, 4, 4));
        Mockito.when(resourceGetter.getAvailableResources(facultyConstant, dateConstant))
                .thenReturn(dayOne);

        ScheduleJob scheduleJob = new ScheduleJob(1, new Faculty(facultyConstant), dateConstant.plusDays(1),
                17, 17, 17);
        List<ScheduledInstance> answer = scheduler.scheduleBetween(scheduleJob,
                dateConstant, dateConstant.plusDays(1));

        assertThat(answer.size()).isEqualTo(3);
        ScheduledInstance expected1 = new ScheduledInstance(1L, facultyConstant,
                10, 10, 10, dateConstant);
        ScheduledInstance expected2 = new ScheduledInstance(1L, facultyConstant2,
                5, 5, 5, dateConstant);
        ScheduledInstance expected3 = new ScheduledInstance(1L, facultyConstant3,
                2, 2, 2, dateConstant);

        for (ScheduledInstance instance : answer) {
            if (instance.getFaculty().equals(facultyConstant)) {
                assertThat(compareScheduledInstances(instance, expected1)).isEqualTo(true);
            } else if (instance.getFaculty().equals(facultyConstant2)) {
                assertThat(compareScheduledInstances(instance, expected2)).isEqualTo(true);
            } else {
                assertThat(compareScheduledInstances(instance, expected3)).isEqualTo(true);
            }
        }
    }

    private boolean compareScheduledInstances(ScheduledInstance a, ScheduledInstance b) {
        return a.getJobId().equals(b.getJobId())
                && a.getFaculty().equals(b.getFaculty())
                && a.getCpuUsage() == b.getCpuUsage()
                && a.getGpuUsage() == b.getGpuUsage()
                && a.getMemoryUsage() == b.getMemoryUsage()
                && a.getDate().equals(b.getDate());
    }
}