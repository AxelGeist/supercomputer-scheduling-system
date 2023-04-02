package nl.tudelft.sem.template.authentication.integration.utils;

import static org.assertj.core.api.Assertions.assertThat;

import commons.Faculty;
import commons.NetId;
import commons.Role;
import commons.RoleValue;
import java.util.ArrayList;
import nl.tudelft.sem.template.authentication.domain.user.AppUser;
import nl.tudelft.sem.template.authentication.domain.user.HashedPassword;
import nl.tudelft.sem.template.authentication.domain.user.PasswordWasChangedEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PasswordWasChangedEventTest {
    AppUser user;
    PasswordWasChangedEvent event;

    /**
     * Initialising a PasswordWasChangedEvent.
     */
    @BeforeEach
    public void init() {
        NetId netId = new NetId("itomov");
        HashedPassword password = new HashedPassword("password");
        Role role = new Role(RoleValue.EMPLOYEE);

        ArrayList<Faculty> faculties = new ArrayList<>();

        faculties.add(new Faculty("EEMCS"));
        faculties.add(new Faculty("3ME"));

        user = new AppUser(netId, password, role, faculties);

        event = new PasswordWasChangedEvent(user);
    }

    @Test
    public void getUserTest() {
        assertThat(event.getUser()).isEqualTo(user);
    }
}
