package nl.tudelft.sem.template.example.domain;

import static org.assertj.core.api.Assertions.assertThat;

import commons.FacultyResource;
import commons.FacultyResourceModel;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.client.RestTemplate;


@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ResourceGetterTest {
    @MockBean
    private RestTemplate restTemplate;

    ResourceGetter resourceGetter;

    @BeforeEach
    void setup() {
        resourceGetter = new ResourceGetter(restTemplate, "TEST-URL");
    }

    @Test
    void getAvailableResources() {
        FacultyResource[] facultyResources = {
            new FacultyResource("EEMCS", LocalDate.now().plusDays(1), 10, 10, 10),
            new FacultyResource("3ME", LocalDate.now().plusDays(1), 20, 20, 20)
        };
        Mockito.when(restTemplate.postForEntity("TEST-URL/cluster/facultyDayResource",
                new FacultyResourceModel("EEMCS", LocalDate.now().plusDays(1)), FacultyResource[].class))
                .thenReturn(new ResponseEntity<>(facultyResources, HttpStatus.OK));

        List<FacultyResource> answer =
                resourceGetter.getAvailableResources("EEMCS", LocalDate.now().plusDays(1));

        for (FacultyResource r : answer) {
            if (r.getFaculty().equals("EEMCS")) {
                assertThat(r).isEqualTo(facultyResources[0]);
            } else {
                assertThat(r).isEqualTo(facultyResources[1]);
            }
        }

    }
}