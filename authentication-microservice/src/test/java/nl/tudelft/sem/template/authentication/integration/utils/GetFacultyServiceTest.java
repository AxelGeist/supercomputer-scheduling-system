package nl.tudelft.sem.template.authentication.integration.utils;

import static org.assertj.core.api.Assertions.assertThat;

import commons.Faculty;
import commons.NetId;
import commons.Role;
import commons.RoleValue;
import java.util.ArrayList;
import nl.tudelft.sem.template.authentication.domain.user.AppUser;
import nl.tudelft.sem.template.authentication.domain.user.GetFacultyService;
import nl.tudelft.sem.template.authentication.domain.user.HashedPassword;
import nl.tudelft.sem.template.authentication.domain.user.NetIdDoesNotExistException;
import nl.tudelft.sem.template.authentication.domain.user.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class GetFacultyServiceTest {
    @Autowired
    private transient UserRepository userRepository;

    @Autowired
    private transient GetFacultyService getFacultyService;

    @Test
    public void emptyRepositoryTest() throws NetIdDoesNotExistException {
        NetId netId = new NetId("user1");

        Assertions.assertThrows(NetIdDoesNotExistException.class, () -> {
            getFacultyService.getFaculty(netId);
        });
    }

    @Test
    public void userNotInRepoTest() throws NetIdDoesNotExistException {
        final NetId testUser = new NetId("SomeUser");
        final HashedPassword existingTestPassword = new HashedPassword("password123");
        final Role role = new Role(RoleValue.EMPLOYEE);
        final ArrayList<Faculty> faculties = new ArrayList<>();
        faculties.add(new Faculty("EEMCS"));
        faculties.add(new Faculty("3ME"));

        AppUser appUser = new AppUser(testUser, existingTestPassword, role, faculties);

        userRepository.save(appUser);

        NetId netId = new NetId("user1");

        Assertions.assertThrows(NetIdDoesNotExistException.class, () -> {
            getFacultyService.getFaculty(netId);
        });
    }

    @Test
    public void userInRepoTest() throws NetIdDoesNotExistException {
        final NetId testUser = new NetId("SomeUser");
        final HashedPassword existingTestPassword = new HashedPassword("password123");
        final Role role = new Role(RoleValue.EMPLOYEE);
        final ArrayList<Faculty> faculties = new ArrayList<>();
        faculties.add(new Faculty("EEMCS"));
        faculties.add(new Faculty("3ME"));

        AppUser appUser = new AppUser(testUser, existingTestPassword, role, faculties);

        userRepository.save(appUser);

        NetId netId = new NetId("SomeUser");

        assertThat(getFacultyService.getFaculty(netId).size()).isEqualTo(2);
        assertThat(getFacultyService.getFaculty(netId).get(0)).isEqualTo(new Faculty("EEMCS"));
        assertThat(getFacultyService.getFaculty(netId).get(1)).isEqualTo(new Faculty("3ME"));
    }
}
