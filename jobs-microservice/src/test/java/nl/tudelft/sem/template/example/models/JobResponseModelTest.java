package nl.tudelft.sem.template.example.models;



import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import commons.Status;
import org.junit.jupiter.api.Test;

public class JobResponseModelTest {
    @Test
    void test() {
        JobResponseModel jm = new JobResponseModel();
        jm.setNetId("filip");
        jm.setStatus(Status.ACCEPTED);
        assertThat(jm.getNetId()).isEqualTo("filip");
        assertThat(jm.getStatus()).isEqualTo(Status.ACCEPTED);
    }
}
