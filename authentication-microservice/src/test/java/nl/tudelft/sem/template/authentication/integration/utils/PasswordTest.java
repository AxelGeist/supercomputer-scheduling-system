package nl.tudelft.sem.template.authentication.integration.utils;

import static org.assertj.core.api.Assertions.assertThat;

import nl.tudelft.sem.template.authentication.domain.user.Password;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PasswordTest {
    Password password;

    @BeforeEach
    public void init() {
        password = new Password("password");
    }

    @Test
    public void toStringTest() {
        assertThat(password.toString()).isEqualTo("password");
    }
}
