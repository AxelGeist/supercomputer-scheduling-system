package nl.tudelft.sem.template.authentication.integration.utils;

import static org.assertj.core.api.Assertions.assertThat;

import nl.tudelft.sem.template.authentication.domain.user.HashedPassword;
import nl.tudelft.sem.template.authentication.domain.user.Password;
import nl.tudelft.sem.template.authentication.domain.user.PasswordHashingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordHashingServiceTest {
    PasswordHashingService service;
    PasswordEncoder passwordEncoder;

    /**
     * Initialising a PasswordHashingService.
     */
    @BeforeEach
    public void init() {
        passwordEncoder = new PasswordEncoder() {
            @Override
            public String encode(CharSequence rawPassword) {
                return null;
            }

            @Override
            public boolean matches(CharSequence rawPassword, String encodedPassword) {
                return false;
            }
        };

        service = new PasswordHashingService(passwordEncoder);
    }

    @Test
    public void hashTest() {
        Password password = new Password("password");

        HashedPassword expected = new HashedPassword(passwordEncoder.encode(password.toString()));

        assertThat(service.hash(password)).isEqualTo(expected);
    }
}
