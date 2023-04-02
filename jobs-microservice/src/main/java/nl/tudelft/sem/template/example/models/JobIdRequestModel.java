package nl.tudelft.sem.template.example.models;

import lombok.Data;

/**
 * Request model for Job database id (primary key).
 */
@Data
public class JobIdRequestModel {

    private long id;

    /**
     * Constructor of IdRequestModel.
     *
     * @param id the requested id of a Job in the database
     */
    public JobIdRequestModel(long id) {
        this.id = id;
    }
}
