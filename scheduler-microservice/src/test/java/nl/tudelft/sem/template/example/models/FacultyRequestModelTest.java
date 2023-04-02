package nl.tudelft.sem.template.example.models;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.junit.jupiter.api.Test;

public class FacultyRequestModelTest {
    @Test
    void test() {
        FacultyRequestModel fr = new FacultyRequestModel("filip");
        assertThat(fr.getNetId()).isEqualTo("filip");
    }
}
