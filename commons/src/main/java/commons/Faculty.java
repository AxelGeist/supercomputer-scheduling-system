package commons;

import lombok.EqualsAndHashCode;
import org.springframework.security.core.GrantedAuthority;

/**
 * A DDD value object representing a Faculty in our domain.
 */
@EqualsAndHashCode
public class Faculty {
    private final transient String facultyName;

    public Faculty(String facultyName) {
        // validate Faculty
        this.facultyName = facultyName;
    }

    @Override
    public String toString() {
        return facultyName;
    }
}
