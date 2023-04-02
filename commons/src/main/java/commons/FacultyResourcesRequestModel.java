package commons;

import java.time.LocalDate;
import lombok.Data;

@Data
public class FacultyResourcesRequestModel {

    private String faculty;
    private LocalDate date;
}
