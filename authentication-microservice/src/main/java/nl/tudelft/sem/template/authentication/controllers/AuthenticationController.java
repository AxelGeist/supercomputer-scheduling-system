package nl.tudelft.sem.template.authentication.controllers;

import commons.Faculty;
import commons.FacultyRequestModel;
import commons.FacultyResponseModel;
import commons.NetId;
import commons.Role;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import nl.tudelft.sem.template.authentication.authentication.AuthManager;
import nl.tudelft.sem.template.authentication.authentication.JwtTokenGenerator;
import nl.tudelft.sem.template.authentication.authentication.JwtUserDetailsService;
import nl.tudelft.sem.template.authentication.domain.user.GetFacultyService;
import nl.tudelft.sem.template.authentication.domain.user.Password;
import nl.tudelft.sem.template.authentication.domain.user.RegistrationService;
import nl.tudelft.sem.template.authentication.domain.user.UserRepository;
import nl.tudelft.sem.template.authentication.models.AuthenticationRequestModel;
import nl.tudelft.sem.template.authentication.models.AuthenticationResponseModel;
import nl.tudelft.sem.template.authentication.models.ChangeFacultyRequestModel;
import nl.tudelft.sem.template.authentication.models.RegistrationRequestModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class AuthenticationController {

    private final transient AuthenticationManager authenticationManager;

    private final transient AuthManager authManager;


    private final transient JwtTokenGenerator jwtTokenGenerator;

    private final transient JwtUserDetailsService jwtUserDetailsService;

    private final transient RegistrationService registrationService;

    private final transient GetFacultyService getFacultyService;

    private final transient UserRepository userRepository;

    /**
     * Instantiates a new UsersController.
     *
     * @param authenticationManager the authentication manager
     * @param jwtTokenGenerator     the token generator
     * @param jwtUserDetailsService the user service
     * @param registrationService   the registration service
     * @param getFacultyService     the getFaculty service
     * @param userRepository        the user repository
     */
    @Autowired
    public AuthenticationController(AuthenticationManager authenticationManager,
                                    JwtTokenGenerator jwtTokenGenerator,
                                    JwtUserDetailsService jwtUserDetailsService,
                                    RegistrationService registrationService,
                                    GetFacultyService getFacultyService,
                                    UserRepository userRepository,
                                    AuthManager authManager) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenGenerator = jwtTokenGenerator;
        this.jwtUserDetailsService = jwtUserDetailsService;
        this.registrationService = registrationService;
        this.getFacultyService = getFacultyService;
        this.userRepository = userRepository;
        this.authManager = authManager;
    }

    /**
     * Endpoint for authentication.
     *
     * @param request The login model
     * @return JWT token if the login is successful
     * @throws Exception if the user does not exist or the password is incorrect
     */
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponseModel> authenticate(@RequestBody AuthenticationRequestModel request)
            throws Exception {

        try {

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getNetId(),
                            request.getPassword()));
        } catch (DisabledException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "INVALID_CREDENTIALS", e);
        }

        final UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(request.getNetId());
        final String jwtToken = jwtTokenGenerator.generateToken(userDetails);
        return ResponseEntity.ok(new AuthenticationResponseModel(jwtToken));
    }

    /**
     * Endpoint for registration.
     *
     * @param request The registration model
     * @return 200 OK if the registration is successful
     * @throws Exception if a user with this netid already exists
     */
    @PostMapping("/register")
    public ResponseEntity register(@RequestBody RegistrationRequestModel request) throws Exception {

        try {
            Role role = new Role(request.getRole());
            ArrayList<Faculty> faculties = new ArrayList<>();
            for (String f : request.getFaculty().split(";")) {
                faculties.add(new Faculty(f));
            }
            System.out.println(faculties);
            System.out.println(role.toString());
            NetId netId = new NetId(request.getNetId());
            Password password = new Password(request.getPassword());
            registrationService.registerUser(netId, password, role, faculties);
        } catch (Exception e) {
            System.out.println(e);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }

        return ResponseEntity.ok().build();
    }

    /**
     * Endpoint for retrieving the faculty of a user.
     *
     * @param request The registration model
     * @return 200 OK if the registration is successful
     * @throws Exception if a user with this netId already exists
     */
    @PostMapping ("/faculty")
    public ResponseEntity<FacultyResponseModel> retrieveFaculty(@RequestBody FacultyRequestModel request) throws Exception {
        try {
            NetId netId = new NetId(request.getNetId());
            List<Faculty> faculty = getFacultyService.getFaculty(netId);
            FacultyResponseModel facultyResponseModel = new FacultyResponseModel();
            facultyResponseModel.setFaculty(faculty.stream().map(Faculty::toString).collect(Collectors.toList()));
            return ResponseEntity.ok(facultyResponseModel);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    /**
     * Endpoint for retrieving the faculty of a user.
     *
     * @return 200 OK if the registration is successful
     * @throws Exception if a user with this netid already exists
     */
    @GetMapping("/faculties")
    public ResponseEntity<FacultyResponseModel> retrieveFaculties() {
        List<String> all = getFacultyService.getFaculties();
        FacultyResponseModel facultyResponseModel = new FacultyResponseModel();
        facultyResponseModel.setFaculty(all);
        return ResponseEntity.ok(facultyResponseModel);
    }

    /**
     * Endpoint for retrieving the faculty of a user.
     *
     * @param request The registration model
     * @return 200 OK if the registration is successful
     * @throws Exception if a user with this netid already exists
     */
    @PostMapping("/changeFaculty")
    public ResponseEntity changeFaculty(@RequestBody ChangeFacultyRequestModel request) throws Exception {
        System.out.println(authManager.getRole().getRoleValue());
        if (!authManager.getRole().isAdmin()) {
            return ResponseEntity.badRequest().body("Unauthorized");
        }
        try {
            NetId netId = new NetId(request.getNetId());
            List<Faculty> faculties =
                Arrays.stream(request.getFaculty().split(";")).map(Faculty::new).collect(Collectors.toList());
            getFacultyService.changeFaculty(netId, faculties);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

}
