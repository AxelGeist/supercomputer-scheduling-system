package nl.tudelft.sem.template.example.models;


import commons.Status;
import lombok.Data;

/**
 * Response Entity model for Job class.
 */
@Data
public class JobResponseModel {

    private String netId;
    private Status status;
    private Long id;
}
