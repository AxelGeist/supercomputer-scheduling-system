package nl.tudelft.sem.template.authentication.integration.utils;

import static org.assertj.core.api.Assertions.assertThat;

import nl.tudelft.sem.template.authentication.domain.user.HashedPassword;
import nl.tudelft.sem.template.authentication.domain.user.HashedPasswordAttributeConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class HashedPasswordAttributeConverterTest {
    HashedPasswordAttributeConverter converter;

    @BeforeEach
    public void init() {
        converter = new HashedPasswordAttributeConverter();
    }

    @Test
    public void convertToDatabaseColumnTest() {
        assertThat(converter.convertToDatabaseColumn(new HashedPassword("hash"))).isEqualTo("hash");
    }

    @Test
    public void convertToEntityAttributeTest() {
        assertThat(converter.convertToEntityAttribute("hash")).isEqualTo(new HashedPassword("hash"));
    }
}
