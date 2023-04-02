package commons;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FacultyResource {
    private String faculty;
    private LocalDate date;
    private int cpuUsage;
    private int gpuUsage;
    private int memoryUsage;
}
