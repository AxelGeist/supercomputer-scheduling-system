package nl.tudelft.sem.template.example.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import nl.tudelft.sem.template.example.dtos.AddNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NodeTest {

    String name;
    String url;
    String faculty;
    String token;
    int cpu;
    int gpu;
    int memory;
    Node node;
    AddNode addNodeValid;
    AddNode addNodeInvalidResources;
    AddNode addNodeInvalidStrings;

    @BeforeEach
    void setUp() {
        name = "name";
        url = "url";
        faculty = "faculty";
        token = "token";
        cpu = 0;
        gpu = 0;
        memory = 0;

        node = new Node(name, url, faculty, token, cpu, gpu, memory);
        addNodeValid = new AddNode(url, faculty, token, 0, 0, 0);
        addNodeInvalidResources = new AddNode(url, faculty, token, -1, -1, 0);
        addNodeInvalidStrings = new AddNode(null, faculty, null, 1, 1, 0);
    }

    @Test
    void getSetIdTest() {
        node.setId(1L);

        assertThat(node.getId()).isEqualTo(1L);
    }

    @Test
    void getNameTest() {
        assertThat(node.getName()).isEqualTo("name");
    }

    @Test
    void getUrlTest() {
        assertThat(node.getUrl()).isEqualTo("url");
    }

    @Test
    void getFacultyTest() {
        assertThat(node.getFaculty()).isEqualTo("faculty");
    }

    @Test
    void getTokenTest() {
        assertThat(node.getToken()).isEqualTo("token");
    }

    @Test
    void setTokenTest() {
        node.setToken("newToken");
        assertThat(node.getToken()).isEqualTo("newToken");
    }

    @Test
    void getCpuTest() {
        assertThat(node.getCpu()).isEqualTo(0);
    }

    @Test
    void getGpuTest() {
        assertThat(node.getGpu()).isEqualTo(0);
    }

    @Test
    void getMemoryTest() {
        assertThat(node.getMemory()).isEqualTo(0);
    }

    @Test
    void setReleaseTimeTest() {
        node.setReleaseTime(LocalDate.of(2023, 2, 8));

        assertThat(node.getReleaseEndTime()).isEqualTo(LocalDate.of(2023, 2, 8));
    }

    @Test
    void nodeCreatorTestValid() {
        Node nd = Node.nodeCreator(addNodeValid, "myName");


        assertThat(nd).isNotNull();
        assertThat(nd.getName()).isEqualTo("myName");
        assertThat(nd.getUrl()).isEqualTo("url");
        assertThat(nd.getFaculty()).isEqualTo("faculty");
        assertThat(nd.getToken()).isEqualTo("token");
        assertThat(nd.getCpu()).isEqualTo(0);
        assertThat(nd.getGpu()).isEqualTo(0);
        assertThat(nd.getMemory()).isEqualTo(0);
    }

    @Test
    void nodeCreatorTestInvalidResources() {
        Node nd = Node.nodeCreator(addNodeInvalidResources, "invalid");

        assertThat(nd).isNull();
    }

    @Test
    void nodeCreatorTestInvalidStrings() {
        Node nd = Node.nodeCreator(addNodeInvalidStrings, "invalid");

        assertThat(nd).isNull();
    }

    @Test
    void compareToTestNotNodeType() {
        assertThat(node.compareTo("I am a String")).isEqualTo(-1);
    }

    @Test
    void compareToTestReturn0() {
        node.setId(1L);
        Node newNode = new Node("newNode", url, faculty, token, 0, 0, 0);
        newNode.setId(1L);
        assertThat(node.compareTo(newNode)).isEqualTo(0);
    }

    @Test
    void compareToTestReturn1() {
        node.setId(2L);
        Node newNode = new Node("newNode", url, faculty, token, 0, 0, 0);
        newNode.setId(1L);
        assertThat(node.compareTo(newNode)).isEqualTo(1);
    }
}