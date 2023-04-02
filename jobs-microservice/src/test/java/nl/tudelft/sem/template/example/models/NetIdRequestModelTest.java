package nl.tudelft.sem.template.example.models;



import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import commons.Status;
import org.junit.jupiter.api.Test;

public class NetIdRequestModelTest {
    @Test
    void test() {
        NetIdRequestModel jm = new NetIdRequestModel();
        jm.setNetId("filip");
        assertThat(jm.getNetId()).isEqualTo("filip");
    }
}
