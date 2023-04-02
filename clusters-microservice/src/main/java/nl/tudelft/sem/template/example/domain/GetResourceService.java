package nl.tudelft.sem.template.example.domain;

import commons.FacultyResource;
import commons.Resource;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import nl.tudelft.sem.template.example.controllers.NodeUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;


@Service
public class GetResourceService {

    private final transient NodeRepository repo;

    GetResourceService(NodeRepository repo) {
        this.repo = repo;
    }

    /**
     * Gets all nodes from faculty and returns the amount it owns.
     *
     * @param faculty the faculty admin wants resources for
     */
    @GetMapping(path = {"/resources/{faculty}"})
    public Resource getTotalResourcesForFaculty(String faculty) {
        List<Node> facultyNodes = repo.getNodesByFaculty(faculty).get();
        Resource r = NodeUtil.resourceCreator(facultyNodes);
        return r;
    }

    /**
     * Returns all the nodes available for admin to see.
     */
    public List<Node> getAllNodes() {
        if (repo.getAllNodes().isEmpty()) {
            System.out.println("Db is empty");
            return new ArrayList<Node>();
        }
        return repo.getAllNodes().get();
    }

    /**
     * Gets the number of free resources available for facculty and day.
     *
     * @param faculty faculty requested
     * @param date free resources on this day
     */
    public Object[] getFacultyAvailableResourcesForDay(String faculty, LocalDate date) {
        List<FacultyResource> answer = new ArrayList<>();
        if (repo.getAvailableResources(faculty, date).isPresent()) {
            List<Node> n = repo.getAvailableResources(faculty, date).get();
            List<Resource> resources = NodeUtil.resourceCreatorForDifferentClusters(n);

            for (Resource r : resources) {
                answer.add(new FacultyResource(faculty, date, r.getCpu(), r.getGpu(), r.getMem()));
            }
        }
        return answer.toArray();
    }

    /**
     * Get resources for next day of every faculty.
     */
    public List<FacultyResource> getResourcesNextDay(List<String> faculties) {
        List<FacultyResource> res = new ArrayList<>();

        for (String f : faculties) {
            List<Node> n = repo.getAvailableResources(f, LocalDate.now().plusDays(1)).get();
            Resource r = NodeUtil.resourceCreator(n);
            FacultyResource facultyResources = new FacultyResource(f, LocalDate.now().plusDays(1),
                    r.getCpu(), r.getGpu(), r.getMem());
            res.add(facultyResources);
        }
        return res;
    }



}
