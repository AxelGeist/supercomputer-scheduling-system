package commons;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class FacultyResourceModelTest {
    FacultyResourceModel model;

    /**
     * Initialising a FacultyResourceModel.
     */
    @BeforeEach
    public void init() {
        model = new FacultyResourceModel();
        model.setFaculty("EEMCS");
        model.setDate(LocalDate.now());
    }

    @Test
    public void constructorTest() {
        model = new FacultyResourceModel();
        model.setFaculty("EEMCS");
        model.setDate(LocalDate.now());

        assertNotNull(model);
        assertThat(model.getFaculty()).isEqualTo("EEMCS");
        assertThat(model.getDate()).isEqualTo(LocalDate.now());
    }

    @Test
    public void getFacultyTest() {
        assertThat(model.getFaculty()).isEqualTo("EEMCS");
    }

    @Test
    public void getDateTest() {
        assertThat(model.getDate()).isEqualTo(LocalDate.now());
    }
}
