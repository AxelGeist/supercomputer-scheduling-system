package commons;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


class FacultyAttributeConverterTest {

    FacultyAttributeConverter fac;
    Faculty faculty;
    String dbValue;

    @BeforeEach
    void setUp() {
        fac = new FacultyAttributeConverter();
        faculty = new Faculty("EEMCS");
        dbValue = "EEMCS";
    }

    @Test
    void convertToDatabaseColumn() {
        String actualDbValue = fac.convertToDatabaseColumn(faculty);
        assertThat(actualDbValue).isEqualTo(dbValue);
    }

    @Test
    void convertToEntityAttribute() {
        Faculty actualFaculty = fac.convertToEntityAttribute(dbValue);
        assertThat(actualFaculty).isEqualTo(faculty);
    }
}