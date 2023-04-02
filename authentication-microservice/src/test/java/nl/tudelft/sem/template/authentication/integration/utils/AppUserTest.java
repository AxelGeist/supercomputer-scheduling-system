package nl.tudelft.sem.template.authentication.integration.utils;

import static org.assertj.core.api.Assertions.assertThat;

import commons.Faculty;
import commons.NetId;
import commons.Role;
import java.util.ArrayList;
import nl.tudelft.sem.template.authentication.domain.user.AppUser;
import nl.tudelft.sem.template.authentication.domain.user.HashedPassword;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AppUserTest {
    NetId netId;
    HashedPassword password;
    Role role;
    ArrayList<Faculty> faculties = new ArrayList<>();

    AppUser user;

    @BeforeEach
    void init() {
        netId = new NetId("user1");
        password = new HashedPassword("pwd");
        role = new Role("EMPLOYEE");

        Faculty faculty1 = new Faculty("EEMCS");
        Faculty faculty2 = new Faculty("3ME");
        Faculty faculty3 = new Faculty("Architecture");

        faculties.add(faculty1);
        faculties.add(faculty2);
        faculties.add(faculty3);

        user = new AppUser(netId, password, role, faculties);
    }

    @Test
    public void getNetIdTest() {
        assertThat(user.getNetId()).isEqualTo(new NetId("user1"));
    }

    @Test
    public void getPasswordTest() {
        assertThat(user.getPassword()).isEqualTo(new HashedPassword("pwd"));
    }

    @Test
    public void getRoleTest() {
        assertThat(user.getRole()).isEqualTo(new Role("EMPLOYEE"));
    }

    @Test
    public void getFacultyTest() {
        assertThat(user.getFaculty().size()).isEqualTo(3);
        assertThat(user.getFaculty().get(0)).isEqualTo(new Faculty("EEMCS"));
        assertThat(user.getFaculty().get(1)).isEqualTo(new Faculty("3ME"));
        assertThat(user.getFaculty().get(2)).isEqualTo(new Faculty("Architecture"));
    }
}
