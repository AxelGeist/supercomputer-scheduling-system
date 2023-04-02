package nl.tudelft.sem.template.authentication.models;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class FacultyResponseModelTest {
    FacultyResponseModel model;

    @BeforeEach
    public void init() {
        model = new FacultyResponseModel();
        model.setFaculties("EEMCS;3ME");
    }

    @Test
    public void constructorTest() {
        model = new FacultyResponseModel();
        model.setFaculties("EEMCS;3ME");

        assertNotNull(model);
        assertThat(model.getFaculties()).isEqualTo("EEMCS;3ME");
    }

    @Test
    public void getFacultiesTest() {
        assertThat(model.getFaculties()).isEqualTo("EEMCS;3ME");
    }
}
