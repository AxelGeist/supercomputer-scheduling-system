package nl.tudelft.sem.template.example.domain.processing;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import nl.tudelft.sem.template.example.domain.db.ScheduledInstance;
import nl.tudelft.sem.template.example.domain.db.ScheduledInstanceRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class RemovingJobsServiceTest {
    @Autowired
    private transient RemovingJobsService removingJobsService;

    @Autowired
    private transient ScheduledInstanceRepository scheduledInstanceRepository;

    @Test
    public void removeJob_worksCorrectly() {
        LocalDate dateConstant = LocalDate.now();

        ScheduledInstance si1 = new ScheduledInstance(1L, "EEMCS", 5, 2, 2, dateConstant);
        ScheduledInstance si2 = new ScheduledInstance(1L, "3ME", 3, 8, 1, dateConstant);
        ScheduledInstance si3 = new ScheduledInstance(1L, "Architecture", 2, 1, 3, dateConstant);
        ScheduledInstance si4 = new ScheduledInstance(1L, "3ME", 4, 1, 1, dateConstant);

        List<ScheduledInstance> instances = new LinkedList<>();

        instances.add(si1);
        instances.add(si2);
        instances.add(si3);
        instances.add(si4);

        scheduledInstanceRepository.saveAll(instances);

        long jobId = 1L;

        assertThat(removingJobsService.removeJob(jobId)).isEqualTo(true);
        assertThat(scheduledInstanceRepository.findAllByJobId(jobId)).isEmpty();
        assertThat(scheduledInstanceRepository.findAll()).isEmpty();
    }

    @Test
    public void removeJob_worksCorrectly_withMoreJobs() {
        LocalDate dateConstant = LocalDate.now();

        ScheduledInstance si1 = new ScheduledInstance(1L, "EEMCS", 5, 2, 2, dateConstant);
        ScheduledInstance si2 = new ScheduledInstance(1L, "3ME", 3, 8, 1, dateConstant);
        ScheduledInstance si3 = new ScheduledInstance(1L, "Architecture", 2, 1, 3, dateConstant);
        ScheduledInstance si4 = new ScheduledInstance(1L, "3ME", 4, 1, 1, dateConstant);
        ScheduledInstance si5 = new ScheduledInstance(2L, "EEMCS", 5, 2, 2, dateConstant);
        ScheduledInstance si6 = new ScheduledInstance(2L, "3ME", 3, 8, 1, dateConstant);
        ScheduledInstance si7 = new ScheduledInstance(2L, "Architecture", 2, 1, 3, dateConstant);
        ScheduledInstance si8 = new ScheduledInstance(2L, "3ME", 4, 1, 1, dateConstant);

        List<ScheduledInstance> instances = new LinkedList<>();

        instances.add(si1);
        instances.add(si2);
        instances.add(si3);
        instances.add(si4);
        instances.add(si5);
        instances.add(si6);
        instances.add(si7);
        instances.add(si8);

        scheduledInstanceRepository.saveAll(instances);

        long jobId = 1L;

        assertThat(removingJobsService.removeJob(jobId)).isEqualTo(true);
        assertThat(scheduledInstanceRepository.findAll()).isNotEmpty();
    }

    @Test
    public void removeJob_emptyList() {
        long jobId = 1L;

        // db is empty now

        assertThat(scheduledInstanceRepository.findAllByJobId(jobId)).isEmpty();
        assertThat(removingJobsService.removeJob(jobId)).isEqualTo(false);
    }
}
