package nl.tudelft.sem.template.example.domain.processing;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import commons.Faculty;
import commons.ScheduleJob;
import commons.UpdateJob;
import commons.Url;
import exceptions.ResourceBiggerThanCpuException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import nl.tudelft.sem.template.example.domain.db.ScheduledInstance;
import nl.tudelft.sem.template.example.domain.db.ScheduledInstanceRepository;
import nl.tudelft.sem.template.example.domain.strategies.ScheduleBetweenClusters;
import nl.tudelft.sem.template.example.domain.strategies.ScheduleBetweenClustersMostResourcesFirst;
import nl.tudelft.sem.template.example.domain.strategies.ScheduleOneCluster;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.client.RestTemplate;


@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ProcessingJobsServiceTest {

    @MockBean
    private RestTemplate restTemplate;

    @MockBean
    private ScheduleBetweenClusters scheduleBetweenClusters;

    @Autowired
    private transient ProcessingJobsService processingJobsService;

    @Autowired
    private transient ScheduledInstanceRepository scheduledInstanceRepository;

    private SchedulingCheckService schedulingCheckService;

    @BeforeEach
    public void setup() {
        schedulingCheckService = new SchedulingCheckService();
    }

    @Test
    public void scheduleJob_forNextDay_worksCorrectly() throws ResourceBiggerThanCpuException {
        Faculty facultyConstant = new Faculty("EEMCS");
        LocalDate dateConstant = LocalDate.now().plusDays(1);

        List<ScheduledInstance> scheduledInstances = List.of(
            new ScheduledInstance(1L, facultyConstant.toString(), 5, 2, 2, dateConstant)
        );
        ScheduleJob scheduleJob = new ScheduleJob(1L, facultyConstant, dateConstant.plusDays(2),
                5, 2, 2);
        Mockito.when(scheduleBetweenClusters.scheduleBetween(Mockito.eq(scheduleJob),
                        Mockito.any(LocalDate.class), Mockito.any(LocalDate.class))).thenReturn(scheduledInstances);
        processingJobsService.setSchedulingStrategy(scheduleBetweenClusters);
        processingJobsService.scheduleJob(scheduleJob);

        // verify that there was a proper call to the strategy
        if (schedulingCheckService.isFiveMinutesBeforeDayStarts(LocalTime.now())) {
            Mockito.verify(scheduleBetweenClusters).scheduleBetween(scheduleJob, dateConstant.plusDays(1),
                    dateConstant.plusDays(2));
        } else {
            Mockito.verify(scheduleBetweenClusters).scheduleBetween(scheduleJob, dateConstant,
                    dateConstant.plusDays(2));
        }

        // verify that scheduledInstances were saved to the db
        List<ScheduledInstance> inDb = scheduledInstanceRepository.findAll();
        assertThat(inDb.size()).isEqualTo(1);
        assertThat(compareScheduledInstances(scheduledInstances.get(0), inDb.get(0))).isEqualTo(true);

        // verify that an update was sent
        Mockito.verify(restTemplate).postForEntity(Url.getJobsUrl() + "/updateStatus",
                new UpdateJob(1L, "scheduled", scheduledInstances.get(0).getDate()), Void.class);
    }

    @Test
    public void scheduleJob_couldNotSchedule_worksCorrectly() throws ResourceBiggerThanCpuException {
        String facultyConstant = "EEMCS";
        LocalDate dateConstant = LocalDate.now().plusDays(1);

        ScheduleJob scheduleJob = new ScheduleJob(1L, new Faculty(facultyConstant), dateConstant.plusDays(2),
                5, 2, 2);
        Mockito.when(scheduleBetweenClusters.scheduleBetween(Mockito.eq(scheduleJob),
                Mockito.any(LocalDate.class), Mockito.any(LocalDate.class))).thenReturn(new ArrayList<>());
        processingJobsService.setSchedulingStrategy(scheduleBetweenClusters);
        processingJobsService.scheduleJob(scheduleJob);

        // verify that NO scheduledInstances were saved to the db
        List<ScheduledInstance> inDb = scheduledInstanceRepository.findAll();
        assertThat(inDb.size()).isEqualTo(0);

        // verify that an update was sent
        Mockito.verify(restTemplate).postForEntity(Url.getJobsUrl() + "/updateStatus",
                new UpdateJob(1L, "unscheduled", null), Void.class);
    }

    @Test
    public void scheduleJob_withGpuGreaterThanCpu_throwsException() {
        ScheduleJob scheduleJob = new ScheduleJob(1, new Faculty("EEMCS"), LocalDate.now().plusDays(1),
                1, 2, 1);
        Exception e = assertThrows(ResourceBiggerThanCpuException.class,
                () -> processingJobsService.scheduleJob(scheduleJob));
        assertThat(e.getMessage()).isEqualTo("GPU or Memory usage cannot be greater than the CPU usage.");
    }

    @Test
    public void scheduleJob_withMemoryGreaterThanCpu_throwsException() {
        ScheduleJob scheduleJob = new ScheduleJob(1, new Faculty("EEMCS"), LocalDate.now().plusDays(1),
                1, 1, 2);
        Exception e = assertThrows(ResourceBiggerThanCpuException.class,
                () -> processingJobsService.scheduleJob(scheduleJob));
        assertThat(e.getMessage()).isEqualTo("GPU or Memory usage cannot be greater than the CPU usage.");
    }

    @Test
    public void setSchedulingStrategyTest() throws InvalidStrategyNameException {
        processingJobsService.setSchedulingStrategy("one-cluster");
        assertThat(processingJobsService.getSchedulingStrategy() instanceof ScheduleOneCluster).isTrue();
        processingJobsService.setSchedulingStrategy("multiple-clusters");
        assertThat(processingJobsService.getSchedulingStrategy() instanceof ScheduleBetweenClusters).isTrue();
        processingJobsService.setSchedulingStrategy("multiple-clusters-most-resources-first");
        assertThat(processingJobsService.getSchedulingStrategy() instanceof ScheduleBetweenClustersMostResourcesFirst)
                .isTrue();

        Exception e = assertThrows(InvalidStrategyNameException.class,
                () -> processingJobsService.setSchedulingStrategy("undefined-useless-string"));
        assertThat(e.getMessage()).isEqualTo("Strategy undefined-useless-string does not exist.");
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
