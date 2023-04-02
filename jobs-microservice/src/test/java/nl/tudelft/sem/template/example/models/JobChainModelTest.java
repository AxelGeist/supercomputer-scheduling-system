package nl.tudelft.sem.template.example.models;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import commons.Faculty;
import commons.Job;
import commons.NetId;
import commons.RoleValue;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import nl.tudelft.sem.template.example.chain.DirectiveJob;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JobChainModelTest {
    JobChainModel model;

    /**
     * Initialising a JobChainModel.
     */
    @BeforeEach
    public void init() {
        model = new JobChainModel();
        model.setJob(new Job(new NetId("itomov"), new Faculty("EEMCS"), "d", 10, 10, 10, LocalDate.now()));
        model.setAuthRole(RoleValue.EMPLOYEE);

        List<Faculty> faculties = new ArrayList<>();
        faculties.add(new Faculty("EEMCS"));
        faculties.add(new Faculty("3ME"));

        model.setAuthFaculty(faculties);
        model.setDirectiveJob(DirectiveJob.Approve);
    }

    @Test
    public void constructorTest() {
        model = new JobChainModel();
        model.setJob(new Job(new NetId("itomov"), new Faculty("EEMCS"), "d", 10, 10, 10, LocalDate.now()));
        model.setAuthRole(RoleValue.EMPLOYEE);

        List<Faculty> faculties = new ArrayList<>();
        faculties.add(new Faculty("EEMCS"));
        faculties.add(new Faculty("3ME"));

        model.setAuthFaculty(faculties);
        model.setDirectiveJob(DirectiveJob.Approve);

        assertNotNull(model);
        assertThat(model.getJob()).isEqualTo(new Job(new NetId("itomov"),
                new Faculty("EEMCS"), "d", 10, 10, 10, LocalDate.now()));
        assertThat(model.getAuthRole()).isEqualTo(RoleValue.EMPLOYEE);
        assertThat(model.getAuthFaculty()).isEqualTo(faculties);
        assertThat(model.getDirectiveJob()).isEqualTo(DirectiveJob.Approve);
    }

    @Test
    public void getJobTest() {
        assertThat(model.getJob()).isEqualTo(new Job(new NetId("itomov"),
                new Faculty("EEMCS"), "d", 10, 10, 10, LocalDate.now()));
    }

    @Test
    public void getAuthRoleTest() {
        assertThat(model.getAuthRole()).isEqualTo(RoleValue.EMPLOYEE);
    }

    @Test
    public void getAuthFacultyTest() {
        List<Faculty> faculties = new ArrayList<>();
        faculties.add(new Faculty("EEMCS"));
        faculties.add(new Faculty("3ME"));

        assertThat(model.getAuthFaculty()).isEqualTo(faculties);
    }

    @Test
    public void getDirectiveJobTest() {
        assertThat(model.getDirectiveJob()).isEqualTo(DirectiveJob.Approve);
    }
}
