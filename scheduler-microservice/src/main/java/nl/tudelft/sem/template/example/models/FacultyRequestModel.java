package nl.tudelft.sem.template.example.models;

import lombok.Data;

/**
 * Model for retrieving the faculty of a user.
 */
@Data
public class FacultyRequestModel {
    private String netId;

    public FacultyRequestModel(String netId) {
        this.netId = netId;
    }
}
