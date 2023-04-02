package nl.tudelft.sem.template.example.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.NoArgsConstructor;
import nl.tudelft.sem.template.example.dtos.AddNode;
import org.jetbrains.annotations.NotNull;

@Entity
@Table(name = "NODE")
@NoArgsConstructor
public class Node implements Comparable {

    @Id
    //@Column(name = "NODEID", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "netId", nullable = false)
    private String name; //netId?

    @Column(name = "url", nullable = false)
    private String url;

    @Column(name = "faculty", nullable = false)
    private String faculty;

    @Column(name = "token", nullable = false)
    private String token; //user auth token?

    @Column(name = "cpu", nullable = false)
    private int cpu;

    @Column(name = "gpu", nullable = false)
    private int gpu;

    @JsonProperty("memory")
    @Column(name = "memory", nullable = false)
    private int memory;

    @Column(name = "RELEASEDSTART")
    private LocalDate released = null;

    @Column(name = "RELEASEDEND")
    private LocalDate releaseEnd = null;
    @Column(name = "REMOVEDDATE")
    private LocalDate removedDate = null;

    /**
     * Constructor for the Node class, represent the nodes in the culster.
     *
     * @param name the netId of the user creating the cluster
     * @param faculty the faculty the user belongs to
     * @param token token of access
     * @param cpuUsage the amount of cpu units needed
     * @param gpuUsage the amount of gpu units needed
     * @param memUsage the amount of memory units needed
     */
    public Node(String name, String url, String faculty, String token, int cpuUsage, int gpuUsage, int memUsage) {
        this.name = name;
        this.url = url;
        this.faculty = faculty;
        this.token = token;
        this.cpu = cpuUsage;
        this.gpu  = gpuUsage;
        this.memory = memUsage;
    }

    /**
     * Node creator that verified requirements.
     *
     * @param node note data
     * @param name name for the node
     * @return ready to be saved to a database node.
     */
    public static Node nodeCreator(AddNode node, String name) {
        if (node.getCpu() < 0 || node.getGpu() < 0 || node.getMemory() < 0) {
            return null;
        }
        if (node.getFaculty() == null || node.getToken() == null || node.getUrl() == null) {
            return null;
        }
        return new Node(name, node.getUrl(), node.getFaculty(), node.getToken(),
                node.getCpu(), node.getGpu(), node.getMemory());
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public String getFaculty() {
        return faculty;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getCpu() {
        return cpu;
    }

    public int getGpu() {
        return gpu;
    }

    //Json is very stupid. i have to ignore annotate this due to json parsing
    @JsonIgnore
    public int getMemory() {
        return memory;
    }

    public LocalDate getReleased() {
        return released;
    }

    public void setReleased(LocalDate released) {
        this.released = released;
    }

    public LocalDate getReleaseEndTime() {
        return releaseEnd;
    }

    public void setReleaseTime(LocalDate releaseEnd) {
        this.releaseEnd = releaseEnd;
    }

    public LocalDate getRemovedDate() {
        return removedDate;
    }

    public void setRemovedDate(LocalDate removedDate) {
        this.removedDate = removedDate;
    }

    /**
     * Comparator for Node.
     *
     * @param otherNode other node you are comparing to
     */
    @Override
    public int compareTo(@NotNull Object otherNode) {
        if (otherNode instanceof Node) {
            Node o = (Node) otherNode;
            return ((int) (this.id - o.id));
        } else {
            return -1;
        }
    }

}
