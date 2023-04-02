package nl.tudelft.sem.template.authentication.models;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import commons.NetId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AuthenticationRequestModelTest {
    AuthenticationRequestModel model;

    /**
     * Initialising an AuthenticationRequestModel.
     */
    @BeforeEach
    public void init() {
        model = new AuthenticationRequestModel();
        model.setNetId("itomov");
        model.setPassword("password");
        model.setRole("EMPLOYEE");
    }

    @Test
    public void constructorTest() {
        model = new AuthenticationRequestModel();
        model.setNetId("itomov");
        model.setPassword("password");
        model.setRole("EMPLOYEE");

        assertNotNull(model);

        assertThat(model.getNetId()).isEqualTo("itomov");
        assertThat(model.getPassword()).isEqualTo("password");
        assertThat(model.getRole()).isEqualTo("EMPLOYEE");
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
}
