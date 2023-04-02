package commons;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FacultyTest {

    Faculty faculty;

    @BeforeEach
    void setUp() {
        faculty = new Faculty("EEMCS");
    }

    @Test
    void testToString() {
        assertThat(faculty.toString()).isEqualTo("EEMCS");
    }
}