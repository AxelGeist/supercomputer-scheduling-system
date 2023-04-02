package nl.tudelft.sem.template.example.domain;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;
import nl.tudelft.sem.template.example.exceptions.InvalidDateException;
import nl.tudelft.sem.template.example.exceptions.InvalidFacultyException;
import nl.tudelft.sem.template.example.exceptions.InvalidOwnerException;
import nl.tudelft.sem.template.example.exceptions.InvalidPeriodException;
import nl.tudelft.sem.template.example.exceptions.NullValueException;
import nl.tudelft.sem.template.example.exceptions.ObjectIsNullException;
import nl.tudelft.sem.template.example.exceptions.ResourceMismatchException;

public class ModifyRepoHelper {
    /**
     * A method that checks whether a node's fields are null.
     *
     * @param node the node under consideration
     * @throws NullValueException if a node's name or url is null
     */
    public void checkNullValues(Node node) throws NullValueException {
        if (Stream.of(node.getName(), node.getUrl(), node.getFaculty(), node.getToken())
                .anyMatch(Objects::isNull)) {
            System.out.println("Value is null");
            throw new NullValueException();
        }
    }

    /**
     * Checks is a object is null.
     *
     * @param o the object to be checked
     * @throws ObjectIsNullException throws an exception when a node is null
     */
    public void checkIfObjectIsNull(Object o) throws ObjectIsNullException {
        if (o == null) {
            throw new ObjectIsNullException();
        }
    }

    /**
     * Checks the constraints for the resources of a node.
     *
     * @param node the node under consideration
     * @throws ResourceMismatchException if the constraints are not met
     */
    public void checkResources(Node node) throws ResourceMismatchException {
        if (node.getCpu() < node.getGpu() || node.getCpu() < node.getMemory()) {
            System.out.println("CPU resource smaller than GPU or MEMORY");
            throw new ResourceMismatchException();
        }
    }

    /**
     * Checks whether the name and the id of a node match.
     *
     * @param node the node under consideration
     * @param netId the netId
     * @throws InvalidOwnerException if the two values do not match
     */
    public void checkNameAndId(Node node, String netId) throws InvalidOwnerException {
        if (!node.getName().equals(netId)) {
            System.out.println("Node doesnt belong to " + node.getName());
            throw new InvalidOwnerException();
        }
    }

    /**
     * Checks whether the faculty of a node is contained in a list of faculties.
     *
     * @param node the node under consideration
     * @param faculties the list of faculties
     * @throws InvalidFacultyException if the faculty is not contained in the list
     */
    public void facultyContained(Node node, List<String> faculties) throws InvalidFacultyException {
        if (!(faculties.contains(node.getFaculty()))) {
            System.out.println("failed after get faculty");
            throw new InvalidFacultyException();
        }
    }

    /**
     * Checks if the date is null or obsolete.
     *
     * @param date the date under consideration
     * @throws InvalidDateException if the date is invalid
     */
    public void dateChecks(LocalDate date) throws InvalidDateException {
        if (date == null || date.isBefore(LocalDate.now())) {
            System.out.println("Null or date is before today");
            throw new InvalidDateException();
        }
    }

    /**
     * Checks if the period is shorter than a day.
     *
     * @param days the number of days
     * @throws InvalidPeriodException if the period is invalid
     */
    public void periodCheck(long days) throws InvalidPeriodException {
        long allowed = 1L;
        if (days < allowed) {
            System.out.println("Length is less than 1");
            throw new InvalidPeriodException();
        }
    }

    /**
     * Checks if a faculty is null and if it is contained in a list of faculties.
     *
     * @param faculty the faculty to check
     * @param faculties the list of faculties
     * @throws InvalidFacultyException if the faculty is not contained in the list of faculties
     * @throws ObjectIsNullException if the faculty is null
     */
    public void facultyCheck(String faculty, List<String> faculties) throws InvalidFacultyException, ObjectIsNullException {
        if (faculty == null) {
            System.out.println("Faculty is null");
            throw new ObjectIsNullException();
        }

        if (!faculties.contains(faculty)) {
            System.out.println("Releasing someone else's faculty");
            throw new InvalidFacultyException();
        }
    }
}
