package nl.tudelft.sem.template.example.dtos;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class AddNode {
    private String url;
    private String faculty;
    private String token;
    private int cpu;
    private int gpu;
    private int memory;

    public String getUrl() {
        return url;
    }

    public String getFaculty() {
        return faculty;
    }

    public String getToken() {
        return token;
    }

    public int getCpu() {
        return cpu;
    }

    public int getGpu() {
        return gpu;
    }

    public int getMemory() {
        return memory;
    }
}
