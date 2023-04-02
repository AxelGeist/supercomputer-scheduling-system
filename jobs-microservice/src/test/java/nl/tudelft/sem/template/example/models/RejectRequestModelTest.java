package nl.tudelft.sem.template.example.models;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RejectRequestModelTest {
    RejectRequestModel model;

    @BeforeEach
    public void init() {
        model = new RejectRequestModel();
        model.setId(1L);
    }

    @Test
    public void constructorTest() {
        model = new RejectRequestModel();
        model.setId(1L);

        assertNotNull(model);
        assertThat(model.getId()).isEqualTo(1L);
    }

    @Test
    public void getIdTest() {
        assertThat(model.getId()).isEqualTo(1L);
    }
}
