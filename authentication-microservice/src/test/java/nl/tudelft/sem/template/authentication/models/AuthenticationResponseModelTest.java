package nl.tudelft.sem.template.authentication.models;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AuthenticationResponseModelTest {
    AuthenticationResponseModel model;

    @BeforeEach
    public void init() {
        model = new AuthenticationResponseModel();
        model.setToken("token");
    }

    @Test
    public void constructorTest() {
        model = new AuthenticationResponseModel();
        model.setToken("token");

        assertNotNull(model);
        assertThat(model.getToken()).isEqualTo("token");
    }

    @Test
    public void getTokenTest() {
        assertThat(model.getToken()).isEqualTo("token");
    }
}
