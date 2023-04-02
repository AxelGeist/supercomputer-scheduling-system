package nl.tudelft.sem.template.example.models;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ApproveRequestModelTest {
    ApproveRequestModel model;

    @BeforeEach
    public void init() {
        model = new ApproveRequestModel();
        model.setId(1L);
    }

    @Test
    public void constructorTest() {
        model = new ApproveRequestModel();
        model.setId(1L);

        assertNotNull(model);
        assertThat(model.getId()).isEqualTo(1L);
    }

    @Test
    public void getIdTest() {
        assertThat(model.getId()).isEqualTo(1L);
    }
}
