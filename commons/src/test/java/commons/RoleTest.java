package commons;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RoleTest {
    Role role;

    @BeforeEach
    void init() {
        role = new Role(RoleValue.EMPLOYEE);
    }

    @Test
    public void constructorTest() {

        role = new Role(RoleValue.EMPLOYEE);
        assertNotNull(role);
        assertThat(role.getAuthority()).isEqualTo(RoleValue.EMPLOYEE.toString());
    }

    @Test
    public void constructorTestString() {

        role = new Role("EMPLOYEE");
        assertNotNull(role);
        assertThat(role.getAuthority()).isEqualTo(RoleValue.EMPLOYEE.toString());
    }


    @Test
    public void getAuthorityTest() {
        assertThat(role.getAuthority()).isEqualTo(RoleValue.EMPLOYEE.toString());
    }
}
