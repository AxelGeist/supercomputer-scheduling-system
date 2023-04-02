package nl.tudelft.sem.template.example.models;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RegistrationRequestModelTest {
    RegistrationRequestModel model;

    /**
     * Initialising a RegistrationRequestModel.
     */
    @BeforeEach
    public void init() {
        model = new RegistrationRequestModel();
        model.setNetId("itomov");
        model.setPassword("password");
        model.setRole("EMPLOYEE");
        model.setFaculty("EEMCS");
    }

    @Test
    public void constructorTest() {
        model = new RegistrationRequestModel();
        model.setNetId("itomov");
        model.setPassword("password");
        model.setRole("EMPLOYEE");
        model.setFaculty("EEMCS");

        assertNotNull(model);
        assertThat(model.getNetId()).isEqualTo("itomov");
        assertThat(model.getPassword()).isEqualTo("password");
        assertThat(model.getRole()).isEqualTo("EMPLOYEE");
        assertThat(model.getFaculty()).isEqualTo("EEMCS");
    }

    @Test
    public void getNetIdTest() {
        assertThat(model.getNetId()).isEqualTo("itomov");
    }

    @Test
    public void getPasswordTest() {
        assertThat(model.getPassword()).isEqualTo("password");
    }

    @Test
    public void getRoleTest() {
        assertThat(model.getRole()).isEqualTo("EMPLOYEE");
    }

    @Test
    public void getFacultyTest() {
        assertThat(model.getFaculty()).isEqualTo("EEMCS");
    }
}
