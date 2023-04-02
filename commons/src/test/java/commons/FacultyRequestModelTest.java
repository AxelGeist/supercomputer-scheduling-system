package commons;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class FacultyRequestModelTest {
    FacultyRequestModel model;

    @BeforeEach
    public void init() {
        model = new FacultyRequestModel();
        model.setNetId("itomov");
    }

    @Test
    public void constructorTest() {
        model = new FacultyRequestModel();
        model.setNetId("itomov");

        assertNotNull(model);
        assertThat(model.getNetId()).isEqualTo("itomov");
    }

    @Test
    public void getNetIdTest() {
        assertThat(model.getNetId()).isEqualTo("itomov");
    }
}
