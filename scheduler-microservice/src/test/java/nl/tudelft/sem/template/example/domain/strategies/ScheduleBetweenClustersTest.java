package nl.tudelft.sem.template.example.domain.strategies;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
public class ScheduleBetweenClustersTest {

    @MockBean
    ResourceGetter resourceGetter;

    @Autowired
    ScheduledInstanceRepository scheduledInstanceRepository;

    ScheduleBetweenClusters scheduleBetweenClusters;

    @BeforeEach
    public void setup() {
        scheduleBetweenClusters = new ScheduleBetweenClusters(resourceGetter, scheduledInstanceRepository);
    }

    @Test
    public void scheduleJob_forNextDay_worksCorrectly() {
        String facultyConstant = "EEMCS";
        LocalDate dateConstant = LocalDate.now().plusDays(1);

        List<FacultyResource> dayOne = List.of(new FacultyResource(facultyConstant,
                dateConstant, 10, 7, 7));
        Mockito.when(resourceGetter.getAvailableResources(facultyConstant, dateConstant))
                .thenReturn(dayOne);

        ScheduleJob scheduleJob = new ScheduleJob(1, new Faculty(facultyConstant), dateConstant.plusDays(1),
                5, 2, 2);
        List<ScheduledInstance> answer = scheduleBetweenClusters.scheduleBetween(scheduleJob,
                dateConstant, dateConstant.plusDays(1));

        assertThat(answer.size()).isEqualTo(1);
        ScheduledInstance expected = new ScheduledInstance(1L, facultyConstant,
                5, 2, 2, dateConstant);
        assertThat(compareScheduledInstances(answer.get(0), expected)).isEqualTo(true);

    }

    @Test
    public void scheduleJob_forOtherDay_worksCorrectly() {
        String facultyConstant = "EEMCS";
        LocalDate dateConstant = LocalDate.now().plusDays(1);

        List<FacultyResource> dayOne = List.of(new FacultyResource(facultyConstant,
                dateConstant, 10, 7, 7));
        List<FacultyResource> dayTwo = List.of(new FacultyResource(facultyConstant,
                dateConstant.plusDays(1), 10, 7, 7));

        Mockito.when(resourceGetter.getAvailableResources(facultyConstant, dateConstant))
                .thenReturn(dayOne);
        Mockito.when(resourceGetter.getAvailableResources(facultyConstant, dateConstant.plusDays(1)))
                .thenReturn(dayTwo);

        List<ScheduledInstance> inDb = List.of(
                new ScheduledInstance(5L, facultyConstant, 6, 2, 2, dateConstant),
                new ScheduledInstance(6L, facultyConstant, 5, 5, 1, dateConstant.plusDays(1))
        );
        scheduledInstanceRepository.saveAll(inDb);

        ScheduleJob scheduleJob = new ScheduleJob(1, new Faculty(facultyConstant), dateConstant.plusDays(2),
                5, 2, 2);
        List<ScheduledInstance> answer = scheduleBetweenClusters.scheduleBetween(scheduleJob,
                dateConstant, dateConstant.plusDays(2));

        assertThat(answer.size()).isEqualTo(1);
        ScheduledInstance expected = new ScheduledInstance(1L, facultyConstant,
                5, 2, 2, dateConstant.plusDays(1));
        assertThat(compareScheduledInstances(answer.get(0), expected)).isEqualTo(true);
    }

    @Test
    public void scheduleJob_splitBetweenFaculties_worksCorrectly() {
        String facultyConstant = "EEMCS";
        String facultyConstant2 = "3mE";
        LocalDate dateConstant = LocalDate.now().plusDays(1);

        List<FacultyResource> dayOne = List.of(
                new FacultyResource(facultyConstant, dateConstant, 3, 3, 3),
                new FacultyResource(facultyConstant2, dateConstant, 5, 5, 5));
        Mockito.when(resourceGetter.getAvailableResources(facultyConstant, dateConstant))
                .thenReturn(dayOne);

        ScheduleJob scheduleJob = new ScheduleJob(1, new Faculty(facultyConstant), dateConstant.plusDays(1),
                5, 2, 2);
        List<ScheduledInstance> answer = scheduleBetweenClusters.scheduleBetween(scheduleJob,
                dateConstant, dateConstant.plusDays(1));

        assertThat(answer.size()).isEqualTo(2);
        ScheduledInstance expected1 = new ScheduledInstance(1L, facultyConstant,
                3, 2, 2, dateConstant);
        ScheduledInstance expected2 = new ScheduledInstance(1L, facultyConstant2,
                2, 0, 0, dateConstant);

        for (ScheduledInstance instance : answer) {
            if (instance.getFaculty().equals(facultyConstant)) {
                assertThat(compareScheduledInstances(instance, expected1)).isEqualTo(true);
            } else {
                assertThat(compareScheduledInstances(instance, expected2)).isEqualTo(true);
            }
        }

    }

    @Test
    public void scheduleJob_notAbleToSchedule_worksCorrectly() {
        String facultyConstant = "EEMCS";
        LocalDate dateConstant = LocalDate.now().plusDays(1);

        List<FacultyResource> dayOne = List.of(new FacultyResource(facultyConstant,
                dateConstant, 10, 7, 7));
        List<FacultyResource> dayTwo = List.of(new FacultyResource(facultyConstant,
                dateConstant.plusDays(1), 12, 1, 2));

        Mockito.when(resourceGetter.getAvailableResources(facultyConstant, dateConstant))
                .thenReturn(dayOne);
        Mockito.when(resourceGetter.getAvailableResources(facultyConstant, dateConstant.plusDays(1)))
                .thenReturn(dayTwo);

        ScheduleJob scheduleJob = new ScheduleJob(1, new Faculty(facultyConstant), dateConstant.plusDays(1),
                11, 2, 2);
        List<ScheduledInstance> answer = scheduleBetweenClusters.scheduleBetween(scheduleJob,
                dateConstant, dateConstant.plusDays(1));

        assertThat(answer.size()).isEqualTo(0);
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
