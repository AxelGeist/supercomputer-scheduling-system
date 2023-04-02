package commons;

import lombok.EqualsAndHashCode;
import org.springframework.security.core.GrantedAuthority;

/**
 * A DDD value object representing a NetID in our domain.
 */
@EqualsAndHashCode
public class Role implements GrantedAuthority {
    private static final long serialVersionUID = 4L;      //Default serial version uid
    private final transient RoleValue roleValue;

    public Role(RoleValue role) {
        roleValue = role;
    }

    public Role(String role) {
        roleValue = RoleValue.valueOf(role);
    }

    public RoleValue getRoleValue() {
        return roleValue;
    }

    @Override
    public String getAuthority() {
        return roleValue.toString();
    }

    public boolean isAdmin() {
        return roleValue == RoleValue.ADMIN;
    }
}