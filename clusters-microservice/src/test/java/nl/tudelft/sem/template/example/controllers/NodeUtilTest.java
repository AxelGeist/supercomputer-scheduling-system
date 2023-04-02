package nl.tudelft.sem.template.example.controllers;

import static org.assertj.core.api.Assertions.assertThat;

import commons.Resource;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import nl.tudelft.sem.template.example.domain.Node;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class NodeUtilTest {
    NodeUtil nodeUtil;

    List<Node> nodes;

    /**
     * Sets up the parameters before each test method.
     */
    @BeforeEach
    public void setup() {
        nodeUtil = new NodeUtil();

        Node node1 = new Node("name", "url", "faculty", "token", 1, 1, 2);
        Node node2 = new Node("name", "url", "faculty", "token", 2, 0, 2);
        Node node3 = new Node("name", "url", "faculty", "token", 1, 3, 2);
        Node node4 = new Node("name", "url", "faculty", "token", 0, 0, 0);

        nodes = List.of(node1, node2, node3, node4);
    }

    @Test
    public void resourceCreatorTestNullList() {
        Resource resource = NodeUtil.resourceCreator(null);

        assertThat(resource.getCpu()).isEqualTo(0);
        assertThat(resource.getGpu()).isEqualTo(0);
        assertThat(resource.getMem()).isEqualTo(0);
    }

    @Test
    public void resourceCreatorEmptyList() {
        List<Node> list = new LinkedList<>();

        Resource resource = NodeUtil.resourceCreator(list);

        assertThat(resource.getCpu()).isEqualTo(0);
        assertThat(resource.getGpu()).isEqualTo(0);
        assertThat(resource.getMem()).isEqualTo(0);
    }

    @Test
    public void resourceCreatorSumZero() {
        List<Node> list = new LinkedList<>();

        Node n1 = new Node("name1", "url", "faculty", "token", 0, 0, 0);
        Node n2 = new Node("name2", "url", "faculty", "token", 0, 0, 0);
        Node n3 = new Node("name3", "url", "faculty", "token", 0, 0, 0);
        Node n4 = new Node("name4", "url", "faculty", "token", 0, 0, 0);

        list = List.of(n1, n2, n3, n4);

        Resource resource = NodeUtil.resourceCreator(list);

        assertThat(resource.getCpu()).isEqualTo(0);
        assertThat(resource.getGpu()).isEqualTo(0);
        assertThat(resource.getMem()).isEqualTo(0);
    }

    @Test
    public void resourceCreatorTestValid() {
        Resource resource = NodeUtil.resourceCreator(nodes);

        assertThat(resource.getCpu()).isEqualTo(4);
        assertThat(resource.getGpu()).isEqualTo(4);
        assertThat(resource.getMem()).isEqualTo(6);
    }

    @Test
    public void resourceCreatorForDifferentClustersTestNullNodes() {
        assertThat(NodeUtil.resourceCreatorForDifferentClusters(null)).isEqualTo(Collections.emptyList());
    }

    @Test
    public void resourceCreatorForDifferentClustersTestOneFaculty() {
        List<Resource> answer = NodeUtil.resourceCreatorForDifferentClusters(nodes);

        assertThat(answer.size()).isEqualTo(1);
        assertThat(answer.get(0).getCpu()).isEqualTo(4);
        assertThat(answer.get(0).getGpu()).isEqualTo(4);
        assertThat(answer.get(0).getMem()).isEqualTo(6);
    }

    @Test
    public void resourceCreatorForDifferentClustersTestMultipleFaculties() {
        List<Node> list = new LinkedList<>();

        Node n1 = new Node("name1", "url", "EEMCS", "token", 1, 2, 3);
        Node n2 = new Node("name2", "url", "EEMCS", "token", 2, 3, 4);
        Node n3 = new Node("name3", "url", "3ME", "token", 3, 4, 5);
        Node n4 = new Node("name4", "url", "3ME", "token", 4, 5, 6);

        list = List.of(n1, n2, n3, n4);

        List<Resource> answer = NodeUtil.resourceCreatorForDifferentClusters(list);

        assertThat(answer.size()).isEqualTo(2);
        assertThat(answer.get(0).getCpu()).isEqualTo(3);
        assertThat(answer.get(0).getGpu()).isEqualTo(5);
        assertThat(answer.get(0).getMem()).isEqualTo(7);
        assertThat(answer.get(1).getCpu()).isEqualTo(7);
        assertThat(answer.get(1).getGpu()).isEqualTo(9);
        assertThat(answer.get(1).getMem()).isEqualTo(11);
    }
}
