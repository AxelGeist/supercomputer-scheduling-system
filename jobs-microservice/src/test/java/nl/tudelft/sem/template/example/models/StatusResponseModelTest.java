package nl.tudelft.sem.template.example.models;



import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import commons.Status;
import org.junit.jupiter.api.Test;

public class StatusResponseModelTest {
    @Test
    void test() {
        StatusResponseModel jm = new StatusResponseModel();
        jm.setStatus(Status.ACCEPTED.toString());
        assertThat(jm.getStatus()).isEqualTo(Status.ACCEPTED.toString());
    }
}
