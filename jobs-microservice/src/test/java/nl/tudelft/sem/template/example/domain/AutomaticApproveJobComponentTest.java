package nl.tudelft.sem.template.example.domain;

import static org.assertj.core.api.Assertions.assertThat;

import commons.Faculty;
import commons.Job;
import commons.NetId;
import commons.Status;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;


@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class AutomaticApproveJobComponentTest {

    @MockBean
    private RestTemplate restTemplate;

    @MockBean
    private transient JobRepository mockJobRepository;

    @MockBean
    private transient JobService mockJobService;

    @Autowired
    private transient AutomaticApproveJobsComponent aajc;

    List<Job> pendingJobs;
    Job job1;
    Job job2;
    Job job3;
    Job job4;
    Job job5;

    @BeforeEach
    void setUp() {
        job1 = new Job(new NetId("mlica"), new Faculty("EEMCS"), "d", 10, 10, 10, LocalDate.now().plusDays(1));
        job1.setStatus(Status.PENDING);
        job2 = new Job(new NetId("ppolitowicz"), new Faculty("EEMCS"), "d", 1, 2, 3, LocalDate.now().plusDays(1));
        job2.setStatus(Status.PENDING);
        job3 = new Job(new NetId("mlica"), new Faculty("EEMCS"), "d", 20, 10, 1, LocalDate.now().plusDays(1));
        job3.setStatus(Status.PENDING);
        job4 = new Job(new NetId("mlica"), new Faculty("EEMCS"), "d", 20, 10, 1, LocalDate.now().plusDays(1));
        job4.setStatus(Status.PENDING);
        job5 = new Job(new NetId("mlica"), new Faculty("EEMCS"), "d", 20, 10, 1, LocalDate.now().plusDays(1));
        job5.setStatus(Status.PENDING);

        pendingJobs = new ArrayList<>();
        pendingJobs.add(job5);
        pendingJobs.add(job2);
        pendingJobs.add(job1);
        pendingJobs.add(job3);
        pendingJobs.add(job4);

        mockJobRepository.save(job5);
        mockJobRepository.save(job2);
        mockJobRepository.save(job1);
        mockJobRepository.save(job3);
        mockJobRepository.save(job4);
    }

    @Test
    void sortPendingJobs() {
        List<Job> filterSortedPendingJobs = aajc.sortJobsAccordingCreationDate(pendingJobs);
        assertThat(filterSortedPendingJobs.size()).isEqualTo(5);
        assertThat(filterSortedPendingJobs.get(0)).isEqualTo(job5);
        assertThat(filterSortedPendingJobs.get(1)).isEqualTo(job2);
        assertThat(filterSortedPendingJobs.get(2)).isEqualTo(job1);
    }

    @Test
    public void approveJobsAfter6pmTest() throws InvalidScheduleJobException, ResponseEntityException {
        Mockito.when(mockJobRepository.findByStatusAndPreferredDate(
                Status.PENDING, LocalDate.now().plusDays(1))).thenReturn(pendingJobs);

        // Inject the mock jobService into the approveJobsAfter6pm method
        aajc.setJobService(mockJobService);

        // Invoke the approveJobsAfter6pm method
        aajc.approveJobsAfter6pm();

        // Verify that the mock jobService's scheduleJob method was called three times
        Mockito.verify(mockJobService, Mockito.times(5)).scheduleJob(Mockito.any());
    }
}