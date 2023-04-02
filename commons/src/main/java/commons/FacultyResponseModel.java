package commons;

import java.util.List;
import lombok.Data;
/**
 * Model for retrieving the faculty of a user.
 */

@Data
public class FacultyResponseModel {
    private List<String> faculty;
}
