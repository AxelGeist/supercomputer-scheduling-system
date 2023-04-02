package nl.tudelft.sem.template.example.domain;

import commons.FacultyResource;
import commons.Resource;
import java.time.LocalDate;
import java.util.List;
import nl.tudelft.sem.template.example.controllers.NodeUtil;
import nl.tudelft.sem.template.example.exceptions.InvalidDateException;
import nl.tudelft.sem.template.example.exceptions.InvalidFacultyException;
import nl.tudelft.sem.template.example.exceptions.InvalidOwnerException;
import nl.tudelft.sem.template.example.exceptions.InvalidPeriodException;
import nl.tudelft.sem.template.example.exceptions.NullValueException;
import nl.tudelft.sem.template.example.exceptions.ObjectIsNullException;
import nl.tudelft.sem.template.example.exceptions.ResourceMismatchException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestTemplate;

@Service
public class ModifyRepoService extends ModifyRepoHelper {

    private final transient NodeRepository repo;
    private final transient RestTemplate restTemplate;

    ModifyRepoService(NodeRepository repo, RestTemplate restTemplate) {
        this.repo = repo;
        this.restTemplate = restTemplate;
    }

    /**
     * Marks Node with the id as deleted.
     * Later when database cleaner is called it will actually delete from database.
     *
     * @param tokenOfAccess token of access
     * @param faculties faculties of the user
     */
    public String disableNodeFromRepo(String tokenOfAccess, List<String> faculties) throws ObjectIsNullException {
        checkIfObjectIsNull(tokenOfAccess);
        if (repo.getNodeByToken(tokenOfAccess).isEmpty()) {
            System.out.println("There are no nodes that can be accessed with this token");
            return null;
        } else if (!faculties.contains(repo.getNodeByToken(tokenOfAccess).get().getFaculty())) {
            System.out.println("Faculties don't match");
            return null;
        }
        repo.setAsDeleted(tokenOfAccess, LocalDate.now().plusDays(1L));
        Node n = repo.getNodeByToken(tokenOfAccess).get();

        try {
            String response = notifySchedulerOfResourceChange(LocalDate.now().plusDays(1L), n.getFaculty());
            return response + " " + n.getRemovedDate().toString();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to notify but " + n.getRemovedDate().toString() + " updated remove date");
            return null;
        }
    }

    private String notifySchedulerOfResourceChange(LocalDate date, String faculty) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        List<Node> n;
        if (repo.getAvailableResources(faculty, date).isPresent()) {
            n = repo.getAvailableResources(faculty, date).get();
        } else {
            n = List.of(new Node("", "", "", "", 0, 0, 0));
        }
        Resource r = NodeUtil.resourceCreator(n);
        FacultyResource f = new FacultyResource(faculty, date, r.getCpu(), r.getGpu(), r.getMem());
        HttpEntity<FacultyResource> requestEntity = new HttpEntity<>(f, headers);
        ResponseEntity<String> updated = restTemplate.postForEntity("http://localhost:8084/resource-update",
                requestEntity, String.class);
        return updated.getBody();
    }

    /**
     * Modify repo nodes so that they belong to the free pool. Only faculty accounts are allowed to do this.
     * Only sets the date its released from and till
     *
     * @param faculty faculty of release model
     * @param date date to release from
     * @param days number of days to release for
     */
    public String releaseFaculty(String faculty, LocalDate date, long days, List<String> faculties)
            throws InvalidDateException, InvalidPeriodException, InvalidFacultyException, ObjectIsNullException {
        dateChecks(date);
        periodCheck(days);
        facultyCheck(faculty, faculties);
        repo.updateRelease(faculty, date, date.plusDays(days));
        return "Released from " + date + " to " + date.plusDays(days);
    }

    /**
     * Endpoint where you can add node. The node has to belong to the faculty you are in.
     * The nodes resources has to match cpu >= gpu || cpu >= mem
     *
     * @param node you want to add
     */
    @PostMapping(path = {"/addNode"})
    public Node addNode(Node node, String netId, List<String> faculties) throws NullValueException,
            ResourceMismatchException, InvalidOwnerException, InvalidFacultyException {
        checkNullValues(node);
        checkResources(node);
        checkNameAndId(node, netId);
        facultyContained(node, faculties);
        repo.save(node);
        return node;
    }

}

