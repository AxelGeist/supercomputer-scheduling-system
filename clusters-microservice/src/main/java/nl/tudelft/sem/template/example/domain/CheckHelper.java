package nl.tudelft.sem.template.example.domain;

import commons.Resource;
import commons.RoleValue;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import nl.tudelft.sem.template.example.authentication.AuthManager;
import nl.tudelft.sem.template.example.exceptions.InvalidDateException;
import nl.tudelft.sem.template.example.exceptions.InvalidFacultyException;
import nl.tudelft.sem.template.example.exceptions.InvalidPeriodException;
import nl.tudelft.sem.template.example.exceptions.NullValueException;
import nl.tudelft.sem.template.example.exceptions.ObjectIsNullException;
import nl.tudelft.sem.template.example.models.ReleaseFacultyModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class CheckHelper {
    /**
     * Sets the faculty of a node to a different value.
     *
     * @param node the node used
     * @param authManager contains details about the user
     */
    public void setNodeFaculty(Node node, AuthManager authManager) {
        if (authManager.getRole().getRoleValue() == RoleValue.ADMIN) {
            node.setFaculty("FreePool");
        }
    }

    /**
     * Checks is a node is null.
     *
     * @param node the node to be checked
     * @return ok is the node is not null
     * @throws ObjectIsNullException throws an exception when a node is null
     */
    public ResponseEntity<Node> checkIfNodeIsNull(Node node) throws ObjectIsNullException {
        if (node == null) {
            throw new ObjectIsNullException();
        }

        return ResponseEntity.ok(node);
    }

    /**
     * Checks is a object is null.
     *
     * @param o the object to be checked
     * @throws ObjectIsNullException throws an exception when a node is null
     */
    public void checkIfObjectIsNull(Object o) throws ObjectIsNullException {
        if (o == null) {
            throw new ObjectIsNullException();
        }
    }

    /**
     * A helper method that gets the total resources for a given faculty.
     *
     * @param authManager contains details about the user
     * @param getResourceService a getFacultyService
     * @param faculty a String value containing the name of the faculty
     * @return a Resource value containing the resources
     */
    public ResponseEntity<Resource> getTotalResourcesForFacultyHelper(AuthManager authManager,
                                      GetResourceService getResourceService, String faculty) {
        if (authManager.getRole().getRoleValue() != RoleValue.ADMIN) {
            System.out.println("Admin privileges required. Current Role:" + authManager.getRole().toString());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Resource r = getResourceService.getTotalResourcesForFaculty(faculty);
        return ResponseEntity.ok(r);
    }

    /**
     * A helper method for getting all the nodes.
     *
     * @param authManager contains details about the user
     * @param getResourceService a getFacultyService
     * @return a list of all nodes
     */
    public ResponseEntity<List<Node>> getAllNodesHelper(AuthManager authManager, GetResourceService getResourceService) {
        if (authManager.getRole().getRoleValue() != RoleValue.ADMIN) {
            System.out.println("Admin privileges required. Current Role:" + authManager.getRole().toString());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        List<Node> nodes = getResourceService.getAllNodes();
        return ResponseEntity.ok(nodes);
    }

    /**
     * A helper method for releasing a faculty.
     *
     * @param authManager contains details about the user
     * @param modifyRepoService a modifyRepoService
     * @param releaseModel a releaseModel object
     * @return the response of the modifyRepoService
     */
    public ResponseEntity<String> releaseFacultyHelper(AuthManager authManager,
                                               ModifyRepoService modifyRepoService, ReleaseFacultyModel releaseModel)
            throws InvalidDateException, InvalidFacultyException, InvalidPeriodException, NullValueException,
            ObjectIsNullException {
        if (authManager.getRole().getRoleValue() != RoleValue.FAC_ACC) {
            System.out.println("Account is not faculty account. Current: " + getFaculty(authManager));
            return ResponseEntity.badRequest().build();
        }
        String response = modifyRepoService.releaseFaculty(releaseModel.getFaculty(),
                releaseModel.getDate(), releaseModel.getDays(), getFaculty(authManager));
        if (response == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(response);
    }

    public List<String> getFaculty(AuthManager authManager) {
        String facultiesString = authManager.getFaculty().getAuthority();
        return new ArrayList<>(Arrays.asList(facultiesString.split(";")));
    }
}
