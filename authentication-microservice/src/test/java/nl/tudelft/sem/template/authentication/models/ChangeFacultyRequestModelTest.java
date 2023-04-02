package nl.tudelft.sem.template.authentication.models;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ChangeFacultyRequestModelTest {
    ChangeFacultyRequestModel model;

    /**
     * Initialising a ChangeFacultyRequestModel.
     */
    @BeforeEach
    public void init() {
        model = new ChangeFacultyRequestModel();
        model.setNetId("itomov");
        model.setFaculty("EEMCS");
    }

    @Test
    public void constructorTest() {
        model = new ChangeFacultyRequestModel();
        model.setNetId("itomov");
        model.setFaculty("EEMCS");

        assertNotNull(model);
        assertThat(model.getNetId()).isEqualTo("itomov");
        assertThat(model.getFaculty()).isEqualTo("EEMCS");
    }

    @Test
    public void getNetIdTest() {
        assertThat(model.getNetId()).isEqualTo("itomov");
    }

    @Test
    public void getFacultyTest() {
        assertThat(model.getFaculty()).isEqualTo("EEMCS");
    }
}
