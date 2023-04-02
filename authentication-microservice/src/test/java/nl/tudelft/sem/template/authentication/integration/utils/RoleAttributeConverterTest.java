package nl.tudelft.sem.template.authentication.integration.utils;

import static org.assertj.core.api.Assertions.assertThat;

import commons.Role;
import commons.RoleValue;
import nl.tudelft.sem.template.authentication.domain.user.RoleAttributeConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RoleAttributeConverterTest {
    RoleAttributeConverter converter;

    @BeforeEach
    public void init() {
        converter = new RoleAttributeConverter();
    }

    @Test
    public void convertToDatabaseColumnTest() {
        assertThat(converter.convertToDatabaseColumn(new Role(RoleValue.EMPLOYEE))).isEqualTo("EMPLOYEE");
    }

    @Test
    public void convertToEntityAttributeTest() {
        assertThat(converter.convertToEntityAttribute("EMPLOYEE")).isEqualTo(new Role("EMPLOYEE"));
    }
}
