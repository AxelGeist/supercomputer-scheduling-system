package nl.tudelft.sem.template.example.controllers;

import commons.FacultyResource;
import commons.FacultyResourceModel;
import commons.Resource;
import java.util.List;
import nl.tudelft.sem.template.example.authentication.AuthManager;
import nl.tudelft.sem.template.example.domain.CheckHelper;
import nl.tudelft.sem.template.example.domain.GetResourceService;
import nl.tudelft.sem.template.example.domain.ModifyRepoService;
import nl.tudelft.sem.template.example.domain.Node;
import nl.tudelft.sem.template.example.exceptions.InvalidDateException;
import nl.tudelft.sem.template.example.exceptions.InvalidFacultyException;
import nl.tudelft.sem.template.example.exceptions.InvalidOwnerException;
import nl.tudelft.sem.template.example.exceptions.InvalidPeriodException;
import nl.tudelft.sem.template.example.exceptions.NullValueException;
import nl.tudelft.sem.template.example.exceptions.ObjectIsNullException;
import nl.tudelft.sem.template.example.exceptions.ResourceMismatchException;
import nl.tudelft.sem.template.example.models.ReleaseFacultyModel;
import nl.tudelft.sem.template.example.models.ToaRequestModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/cluster")
public class NodeController extends CheckHelper {
    private final transient AuthManager authManager;
    private final transient ModifyRepoService modifyRepoService;
    private final transient GetResourceService getResourceService;

    /**
     * Constructor for the NodeController.
     *
     * @param authManager  contains details about the user
     */
    @Autowired
    public NodeController(AuthManager authManager,
                          ModifyRepoService modifyRepoService,
                          GetResourceService getResourceService) {
        this.authManager = authManager;
        this.modifyRepoService = modifyRepoService;
        this.getResourceService = getResourceService;
    }

    /**
     * Admin utilises this path to get resources for each faculty.
     *
     * @param faculty the faculty admin wants resources for
     */
    @GetMapping(path = {"/resources/{faculty}"})
    public ResponseEntity<Resource> getTotalResourcesForFaculty(@PathVariable("faculty") String faculty) {
        return getTotalResourcesForFacultyHelper(authManager, getResourceService, faculty);
    }

    /**
     * Returns all the nodes available for admin to see.
     */
    @GetMapping(path = {"/resources"})
    public ResponseEntity<List<Node>> getAllNodes() {
        return getAllNodesHelper(authManager, getResourceService);
    }

    /**
     * Gets the number of free resources available for faculty and day.
     * Only allowed to access if you belong to the faculty.
     *
     * @param facDay request model for faculty and date
     */
    @PostMapping(path = {"/facultyDayResource"})
    public ResponseEntity<FacultyResource[]> getFacultyAvailableResourcesForDay(@RequestBody FacultyResourceModel facDay) {
        FacultyResource[] facultyResources = (FacultyResource[]) getResourceService.getFacultyAvailableResourcesForDay(
                facDay.getFaculty(), facDay.getDate());
        return ResponseEntity.ok(facultyResources);
    }

    /**
     * Endpoint where you can add node; The node has to belong to the faculty you are in.
     * The nodes resources has to match cpu >= gpu && cpu >= mem.
     *
     * @param node you want to add
     */
    @PostMapping(path = {"/addNode"})
    public ResponseEntity<Node> addNode(@RequestBody Node node) throws ObjectIsNullException, NullValueException,
            InvalidOwnerException, InvalidFacultyException, ResourceMismatchException {
        List<String> faculties = getFaculty(authManager);
        faculties.add("FreePool");
        setNodeFaculty(node, authManager);
        Node newNode = modifyRepoService.addNode(node, authManager.getNetId(), faculties);
        return checkIfNodeIsNull(newNode);
    }

    /**
     * Endpoint to release nodes to the free pool.
     * Only faculty accounts are allowed to do this.
     * Only sets the date its released from and till
     */
    @PostMapping("/releaseFaculty")
    public ResponseEntity<String> releaseFaculty(@RequestBody ReleaseFacultyModel releaseModel)
            throws InvalidDateException, InvalidFacultyException, InvalidPeriodException,
            NullValueException, ObjectIsNullException {
        return releaseFacultyHelper(authManager, modifyRepoService, releaseModel);
    }

    /**
     * Marks Node with the id as deleted.
     * Later when database clearer is called it will actually delete it from database.
     *
     * @param token token of access
     */
    @PostMapping("/delete")
    public ResponseEntity<String> deleteNode(@RequestBody ToaRequestModel token) throws ObjectIsNullException {
        checkIfObjectIsNull(token);
        List<String> faculties = getFaculty(authManager);
        String response = modifyRepoService.disableNodeFromRepo(token.getToken(), faculties);
        if (response == null) {
            return new ResponseEntity<>("Failed to notify", HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(response);
    }

    /**
     * The api GET endpoint to get a list of all resources in all clusters that are available
     * to the user.
     *
     * @return list of all resources in all clusters available to the user.
     */
    @GetMapping(path = "/resourcesNextDay")
    public ResponseEntity<List<FacultyResource>> getResourcesNextDay() {
        List<FacultyResource> res = getResourceService.getResourcesNextDay(getFaculty(authManager));
        return ResponseEntity.ok(res);
    }
}

