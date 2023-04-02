package commons;

import lombok.EqualsAndHashCode;
import org.springframework.security.core.GrantedAuthority;

/**
 * A DDD value object representing a Faculty in our domain.
 */
@EqualsAndHashCode
public class Faculties implements GrantedAuthority {
    private final transient String facultyName;

    private static final long serialVersionUID = 4L;      //Default serial version uid


    public Faculties(String facultyName) {
        // validate Faculty
        this.facultyName = facultyName;
    }

    @Override
    public String getAuthority() {
        return facultyName;
    }
}
