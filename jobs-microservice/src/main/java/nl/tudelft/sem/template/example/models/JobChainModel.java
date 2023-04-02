package nl.tudelft.sem.template.example.models;

import commons.Faculty;
import commons.Job;
import commons.RoleValue;
import java.util.List;
import lombok.Data;
import nl.tudelft.sem.template.example.chain.DirectiveJob;



@Data
public class JobChainModel {

    private Job job;
    private RoleValue authRole;
    private List<Faculty> authFaculty;
    private DirectiveJob directiveJob;

}
