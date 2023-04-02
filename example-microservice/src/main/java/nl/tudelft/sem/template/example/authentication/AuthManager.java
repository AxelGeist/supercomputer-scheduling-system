package nl.tudelft.sem.template.example.authentication;

import commons.Faculties;
import commons.Faculty;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
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

    /**
     * Interfaces with spring security to get the role of the user in the current context.
     *
     * @return The role of the user.
     */
    public Object getRole() {
        return SecurityContextHolder.getContext().getAuthentication().getCredentials();
    }

    /**
     * Interfaces with spring security to get the faculties of the user in the current context.
     *
     * @return The faculties of the user.
     */
    public Faculties getFaculty() {
        List<? extends GrantedAuthority> f =
            new ArrayList<>(SecurityContextHolder.getContext().getAuthentication().getAuthorities());
        return (Faculties) f.get(0);
    }

}
