package nl.tudelft.sem.template.authentication.integration.utils;

import static org.assertj.core.api.Assertions.assertThat;

import nl.tudelft.sem.template.authentication.domain.user.HashedPassword;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class HashedPasswordTest {
    HashedPassword hashedPassword;

    @BeforeEach
    public void init() {
        hashedPassword = new HashedPassword("hash");
    }

    @Test
    public void toStringTest() {
        assertThat(hashedPassword.toString()).isEqualTo("hash");
    }
}
