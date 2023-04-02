package nl.tudelft.sem.template.authentication.authentication;

import commons.Faculties;
import commons.Faculty;
import commons.NetId;
import commons.Role;
import java.util.ArrayList;
import java.util.List;
import nl.tudelft.sem.template.authentication.domain.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * User details service responsible for retrieving the user from the DB.
 */
@Service
public class JwtUserDetailsService implements UserDetailsService {

    private final transient UserRepository userRepository;

    @Autowired
    public JwtUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Loads user information required for authentication from the DB.
     *
     * @param username The username of the user we want to authenticate
     * @return The authentication user information of that user
     * @throws UsernameNotFoundException Username was not found
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var optionalUser = userRepository.findByNetId(new NetId(username));

        if (optionalUser.isEmpty()) {
            throw new UsernameNotFoundException("User does not exist");
        }

        var user = optionalUser.get();
        List<GrantedAuthority> authorities = new ArrayList<>(2);
        authorities.add(0, user.getRole());
        String s = "";
        for (Faculty f : user.getFaculty()) {
            s += f.toString() + ";";
        }

        authorities.add(1, new Faculties(s));

        return new User(user.getNetId().toString(), user.getPassword().toString(),
                authorities);
    }
}
