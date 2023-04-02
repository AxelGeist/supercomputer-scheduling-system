package nl.tudelft.sem.template.example.chain;

import commons.Job;
import nl.tudelft.sem.template.example.models.JobChainModel;

public abstract class BaseValidator implements Validator {

    private transient Validator next;

    /**
     * Set the next handler in the chain of responsibility.
     *
     * @param validator the next validator in the chain
     */
    public void setNext(Validator validator) {
        this.next = validator;
    }

    /**
     * Check if there is any handlers left in the chain of responsibility.
     * If there is a next handler, continue the chain of responsibility, otherwise the chain is finished and return true.
     *
     * @param jobChainModel the Job model that is passed through the chain of responsibility.
     * @return if the job passed all the checks or not.
     * @throws JobRejectedException if the job does not adhere to the requirements of the chain
     */
    protected boolean checkNext(JobChainModel jobChainModel) throws JobRejectedException {
        if (next == null) {
            return true;
        }
        return next.handle(jobChainModel);
    }
}
