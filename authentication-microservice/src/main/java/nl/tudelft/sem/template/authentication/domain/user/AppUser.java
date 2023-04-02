package nl.tudelft.sem.template.authentication.domain.user;

import commons.Faculty;
import commons.FacultyListAttributeConverter;
import commons.NetId;
import commons.NetIdAttributeConverter;
import commons.Role;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.NoArgsConstructor;
import nl.tudelft.sem.template.authentication.domain.HasEvents;

/**
 * A DDD entity representing an application user in our domain.
 */
@Entity
@Table(name = "users")
@NoArgsConstructor
public class AppUser extends HasEvents {
    /**
     * Identifier for the application user.
     */
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "net_id", nullable = false, unique = true)
    @Convert(converter = NetIdAttributeConverter.class)
    private NetId netId;

    @Column(name = "password_hash", nullable = false)
    @Convert(converter = HashedPasswordAttributeConverter.class)
    private HashedPassword password;

    @Column(name = "role", nullable = false)
    @Convert(converter = RoleAttributeConverter.class)
    private Role role;

    @Column(name = "faculty", nullable = false)
    @Convert(converter = FacultyListAttributeConverter.class)
    private List<Faculty> faculty;

    /**
     * Create new application user.
     *
     * @param netId The NetId for the new user
     * @param password The password for the new user
     * @param role The role of the new user(employee, admin, faculty account)
     */
    public AppUser(NetId netId, HashedPassword password, Role role, ArrayList<Faculty> faculty) {
        this.netId = netId;
        this.password = password;
        this.role = role;
        this.faculty = faculty;
        this.recordThat(new UserWasCreatedEvent(this.netId));
    }

    public void changePassword(HashedPassword password) {
        this.password = password;
        this.recordThat(new PasswordWasChangedEvent(this));
    }

    public NetId getNetId() {
        return netId;
    }

    public HashedPassword getPassword() {
        return password;
    }

    public Role getRole() {
        return role;
    }

    public List<Faculty> getFaculty() {
        return faculty;
    }

    /**
     * Equality is only based on the identifier.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AppUser appUser = (AppUser) o;
        return id == (appUser.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(netId);
    }

    public void setFaculty(List<Faculty> f) {
        this.faculty = f;
    }
}
