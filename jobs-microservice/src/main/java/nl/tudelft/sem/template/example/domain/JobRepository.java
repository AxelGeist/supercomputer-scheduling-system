package nl.tudelft.sem.template.example.domain;

import commons.Job;
import commons.NetId;
import commons.Status;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface JobRepository extends JpaRepository<Job, Long> {

    /**
     * Find all the jobs associated to a netId.
     *
     * @param netId netId of a user
     * @return Optional list of jobs
     */
    Optional<List<Job>> findAllByNetId(NetId netId);

    /**
     * Find all the Job entities associated with a certain status and preferredDate.
     *
     * @param status the status of the Job
     * @param preferredDate the due date of the Job
     * @return a list of Job entities containing all "PENDING" jobs in the database.
     */
    List<Job> findByStatusAndPreferredDate(Status status, LocalDate preferredDate);

}
