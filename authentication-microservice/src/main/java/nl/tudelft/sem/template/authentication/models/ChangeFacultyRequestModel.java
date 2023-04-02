package nl.tudelft.sem.template.authentication.models;

import lombok.Data;

/**
 * Model for changing the faculty of a user.
 */
@Data
public class ChangeFacultyRequestModel {
    private String netId;
    private String faculty;
}
