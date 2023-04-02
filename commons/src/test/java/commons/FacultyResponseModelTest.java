package commons;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class FacultyResponseModelTest {
    FacultyResponseModel model;

    /**
     * Initialising a FacultyResponseModel.
     */
    @BeforeEach
    public void init() {
        model = new FacultyResponseModel();

        List<String> faculties = new ArrayList<>();
        faculties.add("EEMCS");
        faculties.add("3ME");

        model.setFaculty(faculties);
    }

    @Test
    public void constructorTest() {
        model = new FacultyResponseModel();

        List<String> faculties = new ArrayList<>();
        faculties.add("EEMCS");
        faculties.add("3ME");

        model.setFaculty(faculties);

        assertNotNull(model);
        assertThat(model.getFaculty()).isEqualTo(faculties);
    }

    @Test
    public void getFacultyTest() {
        List<String> faculties = new ArrayList<>();
        faculties.add("EEMCS");
        faculties.add("3ME");

        assertThat(model.getFaculty()).isEqualTo(faculties);
    }
}
