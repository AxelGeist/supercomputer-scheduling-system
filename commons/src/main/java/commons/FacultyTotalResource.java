package commons;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FacultyTotalResource {
    private String faculty;
    private LocalDate date;
    private int cpuUsage;
    private int gpuUsage;
    private int memoryUsage;
    private int cpuTotal;
    private int gpuTotal;
    private int memoryTotal;
}
