package nl.tudelft.sem.template.example.chain;

import commons.Faculty;
import commons.FacultyResource;
import commons.FacultyResourceModel;
import commons.FacultyResourcesRequestModel;
import commons.Resource;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public abstract class BaseResourceValidator extends BaseValidator {

    /**
     * Get available resources for a specific faculty from the Clusters microservice.
     *
     * @param faculty the faculty to get the resources from
     * @param localDate the date of the free resources
     * @return a response with the requested Resource entity.
     */
    public List<FacultyResource> getFacultyResources(List<Faculty> faculty,
                                                     LocalDate localDate) throws JobRejectedException {
        List<FacultyResource> resources = new ArrayList<>();
        for (Faculty f : faculty) {
            resources.add(getFacultyResource(f, localDate));
        }
        return resources;
    }

    /**
     * Get available resources for a specific faculty from the Clusters microservice.
     *
     * @param faculty the faculty to get the resources from
     * @param localDate the date of the free resources
     * @return a response with the requested Resource entity.
     */
    public FacultyResource getFacultyResource(Faculty faculty, LocalDate localDate) throws JobRejectedException {
        RestTemplate restTemplate = new RestTemplate();
        String requestPath = "http://localhost:8085/cluster/facultyDayResource";
        FacultyResourceModel facultyResourcesRequestModel = new FacultyResourceModel();
        facultyResourcesRequestModel.setFaculty(faculty.toString());
        facultyResourcesRequestModel.setDate(localDate);
        ResponseEntity<FacultyResource> resourceResponseEntity = restTemplate
            .postForEntity(requestPath, facultyResourcesRequestModel, FacultyResource.class);
        if (!resourceResponseEntity.getStatusCode().is2xxSuccessful()) {
            throw new JobRejectedException("BAD_REQUEST");
        }
        FacultyResource resource = resourceResponseEntity.getBody();
        if (resource == null) {
            throw new JobRejectedException("INVALID_FACULTY");
        }
        return resource;
    }
}
