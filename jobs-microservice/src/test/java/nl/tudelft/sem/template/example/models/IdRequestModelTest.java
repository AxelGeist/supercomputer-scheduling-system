package nl.tudelft.sem.template.example.models;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.junit.jupiter.api.Test;

public class IdRequestModelTest {
    @Test
    void test() {
        IdRequestModel fr = new IdRequestModel();
        fr.setId(53);
        assertThat(fr.getId()).isEqualTo(53);
    }
}
