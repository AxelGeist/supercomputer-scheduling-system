package nl.tudelft.sem.template.authentication.domain.user;

import commons.Faculty;
import commons.NetId;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

/**
 * A DDD service for obtaining the faculty of a user.
 */
@Service
public class GetFacultyService {
    private final transient UserRepository userRepository;

    /**
     * Initiates a new getFaculty service.
     *
     * @param userRepository the user repository
     */
    public GetFacultyService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Retrieves the faculty of a user.
     *
     * @param netId the netID of the user
     * @return the faculty of the user
     * @throws NetIdDoesNotExistException if the netID does not exist in the database
     */
    public List<Faculty> getFaculty(NetId netId) throws NetIdDoesNotExistException {
        if (checkNetIdExists(netId)) {
            Optional<AppUser> user = userRepository.findByNetId(netId);
            if (user.isPresent()) {
                return user.get().getFaculty();
            }

            throw new NetIdDoesNotExistException(netId);
        }

        throw new NetIdDoesNotExistException(netId);
    }

    /**
     * Retrieves all the faculties in the system.
     *
     * @return the faculties or an empty arraylist
     */
    public List<String> getFaculties() {
        List<AppUser> users = userRepository.findAll();

        List<Faculty> faculties = new ArrayList<>();
        Set<String> set = new HashSet<>();
        for (AppUser u : users) {
            faculties.addAll(u.getFaculty());
        }

        for (Faculty f : faculties) {
            set.add(f.toString());
        }
        return new ArrayList<>(set);
    }

    /**
     * Retrieves all the faculties in the system.
     *
     * @return the faculties or an empty arraylist
     */
    public AppUser changeFaculty(NetId netId, List<Faculty> faculties) throws NetIdDoesNotExistException {
        if (checkNetIdExists(netId)) {
            Optional<AppUser> user = userRepository.findByNetId(netId);

            if (user.isPresent()) {
                AppUser u = user.get();
                u.setFaculty(faculties);
                userRepository.save(u);
                return u;
            }

            throw new NetIdDoesNotExistException(netId);
        }

        throw new NetIdDoesNotExistException(netId);
    }


    public boolean checkNetIdExists(NetId netId) {
        return userRepository.existsByNetId(netId);
    }
}
