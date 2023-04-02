package nl.tudelft.sem.template.authentication.domain;

import nl.tudelft.sem.template.authentication.domain.user.PasswordHashingService;
import nl.tudelft.sem.template.authentication.domain.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private final transient UserRepository userRepository;

    private final transient PasswordHashingService passwordHashingService;

    @Autowired
    public DataLoader(UserRepository userRepository, PasswordHashingService passwordHashingService) {
        this.userRepository = userRepository;
        this.passwordHashingService = passwordHashingService;
    }

    @Override
    public void run(String ...args) throws Exception {
        loadUsers();
    }

    public void loadUsers() {
        /*userRepository.save(new AppUser(new NetId("mlica"),
            passwordHashingService.hash(new Password("passwd")),
            new Role(RoleValue.FAC_ACC), new ArrayList<>(List.of(new Faculty("EEMCS")))));
        userRepository.save(new AppUser(new NetId("test"),
            passwordHashingService.hash(new Password("passwd")),
            new Role(RoleValue.FAC_ACC), new ArrayList<>(List.of(new Faculty("EEMCS")))));
        userRepository.save(new AppUser(new NetId("test1"),
            passwordHashingService.hash(new Password("passwd")),
            new Role(RoleValue.FAC_ACC), new ArrayList<>(List.of(new Faculty("EEMCS")))));*/
    }
}
