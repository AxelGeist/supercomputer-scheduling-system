package nl.tudelft.sem.template.authentication.authentication;

import commons.Role;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * Authentication Manager.
 */
@Component
public class AuthManager {
    /**
     * Interfaces with spring security to get the name of the user in the current context.
     *
     * @return The name of the user.
     */
    public String getNetId() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    public Role getRole() {
        Role r = new Role(SecurityContextHolder.getContext().getAuthentication().getCredentials().toString());
        return r;
    }

    public Object getFaculty() {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities();
    }

}
