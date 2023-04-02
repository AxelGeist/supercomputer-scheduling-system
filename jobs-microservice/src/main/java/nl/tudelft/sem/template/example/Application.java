package nl.tudelft.sem.template.example;

import commons.Job;
import commons.NetId;
import nl.tudelft.sem.template.example.domain.JobRepository;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Example microservice application.
 */
@SpringBootApplication
@EntityScan(basePackages = {"commons"})
@EnableScheduling
public class Application {

    private final transient JobRepository jobRepository;

    public Application(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
