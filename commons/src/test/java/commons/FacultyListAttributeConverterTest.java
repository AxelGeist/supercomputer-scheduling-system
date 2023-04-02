package commons;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;



class FacultyListAttributeConverterTest {

    FacultyListAttributeConverter fac;
    ArrayList<Faculty> faculties;
    String dbValue;

    @BeforeEach
    void setUp() {
        fac = new FacultyListAttributeConverter();
        Faculty faculty1 = new Faculty("EEMCS");
        Faculty faculty2 = new Faculty("ARCH");
        faculties = new ArrayList<>(List.of(faculty1, faculty2));
        dbValue = "EEMCS;ARCH;";
    }

    @Test
    void convertToDatabaseColumn() {
        String actualDbValue = fac.convertToDatabaseColumn(faculties);
        assertThat(actualDbValue).isEqualTo(dbValue);
    }

    @Test
    void convertToEntityAttribute() {
        List<Faculty> actualFaculties = fac.convertToEntityAttribute(dbValue);
        assertThat(actualFaculties).isEqualTo(faculties);
    }
}