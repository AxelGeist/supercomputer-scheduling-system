package nl.tudelft.sem.template.example.domain.processing;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyString;

import commons.FacultyResource;
import commons.ScheduleJob;
import commons.UpdateJob;
import commons.Url;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import nl.tudelft.sem.template.example.domain.db.ScheduledInstance;
import nl.tudelft.sem.template.example.domain.db.ScheduledInstanceRepository;
import nl.tudelft.sem.template.example.domain.strategies.ScheduleBetweenClusters;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.client.RestTemplate;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class UpdatingJobsServiceTest {

    @MockBean
    private RestTemplate restTemplate;

    @Autowired
    private ProcessingJobsService processingJobsService;

    @MockBean
    private ScheduleBetweenClusters scheduleBetweenClusters;

    @Autowired
    private transient ScheduledInstanceRepository scheduledInstanceRepository;

    @Autowired
    private transient UpdatingJobsService updatingJobsService;

    @Test
    void updateSchedule_nothingToChange_worksCorrectly() {
        List<ScheduledInstance> instances = List.of(
              new ScheduledInstance(1L, "EEMCS", 10, 5, 3, LocalDate.now().plusDays(1)),
              new ScheduledInstance(2L, "EEMCS", 5, 5, 2, LocalDate.now().plusDays(1)),
              new ScheduledInstance(3L, "EEMCS", 10, 10, 9, LocalDate.now().plusDays(1))
        );
        scheduledInstanceRepository.saveAll(instances);

        FacultyResource update = new FacultyResource("EEMCS", LocalDate.now().plusDays(1), 25, 25, 15);
        updatingJobsService.updateSchedule(update);

        Mockito.verify(restTemplate, Mockito.never()).postForEntity(anyString(),
                Mockito.any(UpdateJob.class), Mockito.eq(Void.class));
        assertThat(scheduledInstanceRepository.findAll().size()).isEqualTo(3);
    }

    @Test
    void updateSchedule_jobRescheduledForTheSameDay_worksCorrectly() {
        List<ScheduledInstance> instances = List.of(
                new ScheduledInstance(1L, "EEMCS", 10, 5, 3, LocalDate.now().plusDays(1)),
                new ScheduledInstance(2L, "EEMCS", 5, 5, 2, LocalDate.now().plusDays(1)),
                new ScheduledInstance(3L, "EEMCS", 10, 10, 9, LocalDate.now().plusDays(1))
        );
        scheduledInstanceRepository.saveAll(instances);

        try {
            // wait to make the next ScheduledInstance scheduled later than the previous ones
            Thread.sleep(500);
        } catch (Exception e) {
            System.out.println("EXCEPTION: " + e.getMessage());
        }
        scheduledInstanceRepository.save(
                new ScheduledInstance(4L, "3ME", "EEMCS", 10, 2, 2, LocalDate.now().plusDays(1)));

        List<ScheduledInstance> scheduledInstanceList = List.of(
                new ScheduledInstance(4L, "3ME", 10, 0, 1, LocalDate.now().plusDays(1)));
        Mockito.when(scheduleBetweenClusters.scheduleBetween(Mockito.any(ScheduleJob.class),
                Mockito.eq(LocalDate.now().plusDays(1)),
                Mockito.eq(LocalDate.now().plusDays(2)))).thenReturn(scheduledInstanceList);
        processingJobsService.setSchedulingStrategy(scheduleBetweenClusters);

        FacultyResource update = new FacultyResource("EEMCS", LocalDate.now().plusDays(1), 25, 25, 15);
        updatingJobsService.updateSchedule(update);

        Mockito.verify(restTemplate, Mockito.never()).postForEntity(anyString(),
                Mockito.any(UpdateJob.class), Mockito.eq(Void.class));

        assertThat(scheduledInstanceRepository.findAll().size()).isEqualTo(4);
        var answer = scheduledInstanceRepository.findAllByJobId(4L);
        assertThat(answer.size()).isEqualTo(1);
        assertThat(compareScheduledInstances(answer.get(0), scheduledInstanceList.get(0))).isEqualTo(true);
    }

    @Test
    void updateSchedule_jobsFromDifferentDaysNotRescheduled_worksCorrectly() {
        List<ScheduledInstance> instances = List.of(
                new ScheduledInstance(1L, "EEMCS", 10, 5, 3, LocalDate.now().plusDays(1)),
                new ScheduledInstance(2L, "EEMCS", 5, 5, 2, LocalDate.now().plusDays(2))
        );
        scheduledInstanceRepository.saveAll(instances);

        try {
            // wait to make the next ScheduledInstance scheduled later than the previous ones
            Thread.sleep(500);
        } catch (Exception e) {
            System.out.println("EXCEPTION: " + e.getMessage());
        }
        scheduledInstanceRepository.save(
                new ScheduledInstance(3L, "EEMCS", "EEMCS", 10, 2, 2, LocalDate.now().plusDays(1)));
        scheduledInstanceRepository.save(
                new ScheduledInstance(4L, "EEMCS", "EEMCS", 10, 2, 2, LocalDate.now().plusDays(2)));

        Mockito.when(scheduleBetweenClusters.scheduleBetween(Mockito.any(ScheduleJob.class),
                Mockito.eq(LocalDate.now().plusDays(1)),
                Mockito.eq(LocalDate.now().plusDays(2)))).thenReturn(new ArrayList<>());
        Mockito.when(scheduleBetweenClusters.scheduleBetween(Mockito.any(ScheduleJob.class),
                Mockito.eq(LocalDate.now().plusDays(2)),
                Mockito.eq(LocalDate.now().plusDays(3)))).thenReturn(new ArrayList<>());

        processingJobsService.setSchedulingStrategy(scheduleBetweenClusters);
        FacultyResource update = new FacultyResource("EEMCS", LocalDate.now().plusDays(1), 10, 5, 3);
        updatingJobsService.updateSchedule(update);

        Mockito.verify(restTemplate).postForEntity(Url.getJobsUrl() + "/updateStatus",
                new UpdateJob(3L, "cancelled", null), Void.class);
        Mockito.verify(restTemplate).postForEntity(Url.getJobsUrl() + "/updateStatus",
                new UpdateJob(4L, "cancelled", null), Void.class);

        assertThat(scheduledInstanceRepository.findAll().size()).isEqualTo(2);
        var answerId4 = scheduledInstanceRepository.findAllByJobId(4L);
        assertThat(answerId4.size()).isEqualTo(0);
        var answerId3 = scheduledInstanceRepository.findAllByJobId(3L);
        assertThat(answerId3.size()).isEqualTo(0);
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