package nl.tudelft.sem.template.example.chain;

import commons.Job;
import nl.tudelft.sem.template.example.models.JobChainModel;

public interface Validator {

    void setNext(Validator handler);

    boolean handle(JobChainModel jobChainModel) throws JobRejectedException;
}
