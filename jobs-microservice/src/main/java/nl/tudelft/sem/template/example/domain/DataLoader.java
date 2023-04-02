package nl.tudelft.sem.template.example.domain;

import commons.Job;
import commons.NetId;
import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private final transient JobRepository jobRepository;

    @Autowired
    public DataLoader(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    @Override
    public void run(String ...args) throws Exception {
        loadUsers();
    }

    public void loadUsers() {
        /*jobRepository.save(new Job(new NetId("mlica"), 10, 10, 10, LocalDate.now()));
        jobRepository.save(new Job(new NetId("mlica"), 100, 2000, 1, LocalDate.now()));
        jobRepository.save(new Job(new NetId("mlica"), 100, 20, 200, LocalDate.now()));*/
    }
}
