package nl.tudelft.sem.template.example.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import commons.Faculty;
import commons.Job;
import commons.NetId;
import commons.RoleValue;
import commons.Status;
import exceptions.InvalidNetIdException;
import exceptions.ResourceBiggerThanCpuException;
import java.time.LocalDate;
import java.util.List;
import nl.tudelft.sem.template.example.authentication.AuthManager;
import nl.tudelft.sem.template.example.authentication.JwtTokenVerifier;
import nl.tudelft.sem.template.example.domain.JobRepository;
import nl.tudelft.sem.template.example.domain.JobService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

@SpringBootTest
@ExtendWith(SpringExtension.class)
// activate profiles to have spring use mocks during auto-injection of certain beans.
@ActiveProfiles({"test", "mockTokenVerifier", "mockAuthenticationManager"})
//@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class AddJobTest {
    @MockBean
    private RestTemplate restTemplate;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private transient JwtTokenVerifier mockJwtTokenVerifier;

    @Autowired
    private transient AuthManager mockAuthenticationManager;

    @Autowired
    private transient JobService jobService;

    String facultyConstant;
    LocalDate dateConstant;

    NetId u1;
    NetId u2;
    Job j1;
    Job j2;
    Faculty f1;
    @Autowired
    private JobRepository jobRepository;

    /**
     * Set variables before each test and clear database.
     */
    @BeforeEach
    public void before() throws Exception {
        jobRepository.deleteAll();
        jobRepository.flush();
        facultyConstant = "EEMCS";
        dateConstant = LocalDate.now().plusDays(1);

        u1 = new NetId("User");
        u2 = new NetId("User2");
        f1 = new Faculty("EEMCS");
        j1 = new Job(u1, f1, "description", 10, 10, 10, LocalDate.now());
        j2 = new Job(u2, f1, "description", 12, 10, 10, LocalDate.now());

    }

    @Test
    public void addJobTest() throws Exception {
        String url = "http://localhost:8083" + "/addJob";

        Mockito.when(restTemplate.getForEntity(url, Job.class))
                .thenReturn(new ResponseEntity<>(j1, HttpStatus.OK));

        jobService.createJob(u1, u1, f1,  "description", 10, 10, 10, RoleValue.EMPLOYEE, LocalDate.now());
        List<Job> fromDb = jobService.getAllJobs(u1, u1, RoleValue.ADMIN);
        j1.setJobId(fromDb.get(0).getJobId());
        assertThat(fromDb.size()).isEqualTo(1);
        assertThat(j1.equals(fromDb.get(0))).isEqualTo(true);
    }

    @Test
    public void getAllJobsTest() throws Exception {
        String url = "http://localhost:8083" + "/addJob";

        Mockito.when(restTemplate.getForEntity(url, Job.class))
                .thenReturn(new ResponseEntity<>(j1, HttpStatus.OK));

        jobService.createJob(u1, u1, f1,  "d", 10, 10, 10, RoleValue.EMPLOYEE, LocalDate.now());
        jobService.createJob(new NetId("Tmp"), new NetId("Tmp"), f1, "d", 12, 10, 10, RoleValue.EMPLOYEE, LocalDate.now());
        List<Job> fromDb = jobService.getAllJobs(u1, u1, RoleValue.ADMIN);
        assertThat(fromDb.size()).isEqualTo(2);
    }


    @Test
    public void getJobStatusTest() throws Exception {
        String url = "http://localhost:8083" + "/addJob";

        Mockito.when(restTemplate.getForEntity(url, Job.class))
                .thenReturn(new ResponseEntity<>(j1, HttpStatus.OK));

        jobService.createJob(u1, u1, f1, "d", 10, 10, 10, RoleValue.EMPLOYEE, LocalDate.now());
        jobService.createJob(u2, u2, f1, "d", 12, 10, 10, RoleValue.EMPLOYEE, LocalDate.now());
        List<Job> fromDb = jobService.getAllJobs(u1, u1, RoleValue.ADMIN);
        j2.setJobId(2);
        j1.setJobId(1);
        assertThat(fromDb.size()).isEqualTo(2);
        assertSame(jobService.getJobStatus(u1, u1, fromDb.get(0).getJobId()), Status.PENDING);
        assertSame(jobService.getJobStatus(u2, u2, fromDb.get(1).getJobId()), Status.PENDING);
    }

    @Test
    public void getJobStatusTest_Exception() throws Exception {
        String url = "http://localhost:8083" + "/updateJob";

        Mockito.when(restTemplate.getForEntity(url, Job.class))
                .thenReturn(new ResponseEntity<>(j1, HttpStatus.OK));


        assertThrows(InvalidNetIdException.class, () -> {
            jobService.getJobStatus(null, u1, 1);
        });
    }

    @Test
    public void addJobWithGpuGreaterThanCpu_throwsException() {
        Exception e = assertThrows(ResourceBiggerThanCpuException.class, () -> {
            jobService.createJob(u1, u1, f1, "d", 1, 2, 0, RoleValue.EMPLOYEE, LocalDate.now());
        });
        assertThat(e.getMessage()).isEqualTo("GPU usage cannot be greater than the CPU usage.");
    }

    @Test
    public void addJobWithMemoryGreaterThanCpu_throwsException() {
        Exception e = assertThrows(ResourceBiggerThanCpuException.class, () -> {
            jobService.createJob(u1, u1, f1, "d", 1, 0, 2, RoleValue.EMPLOYEE, LocalDate.now());
        });
        assertThat(e.getMessage()).isEqualTo("Memory usage cannot be greater than the CPU usage.");
    }
}