package nl.tudelft.sem.template.example.models;

import lombok.Data;

/**
 * Model representing a registration request.
 */
@Data
public class RegistrationRequestModel {
    private String netId;
    private String password;
    private String role;
    private String faculty;
    //private Set<Faculty> faculties;
}