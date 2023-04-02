package nl.tudelft.sem.template.example.models;

import java.time.LocalDate;
import lombok.Data;

/**
 * Model for sending data to release faculty nodes.
 */
@Data
public class ReleaseFacultyModel {
    String faculty;
    LocalDate date;
    long days;
}
