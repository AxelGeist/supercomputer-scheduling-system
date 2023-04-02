package nl.tudelft.sem.template.example.controllers;

import commons.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.NoArgsConstructor;
import nl.tudelft.sem.template.example.domain.Node;


@NoArgsConstructor
public class NodeUtil {

    /**
     * Converts a list of nodes int a resource object.
     * Objects CPU, GPU, MEM are sum of themselves from the nodes
     *
     * @param nodes nodes to sum up the resources
     */

    public static Resource resourceCreator(List<Node> nodes) {
        if (nodes == null) {
            return new Resource(0, 0, 0);
        }
        int cpu = 0;
        int gpu = 0;
        int mem = 0;
        for (Node n : nodes) {
            cpu += n.getCpu();
            gpu += n.getGpu();
            mem += n.getMemory();
        }
        return new Resource(cpu, gpu, mem);
    }
    /**
     * Creates an array of resources for each faculty.
     */

    public static List<Resource> resourceCreatorForDifferentClusters(List<Node> nodes) {
        if (nodes == null) {
            return Collections.emptyList();
        }

        List<Resource> answer = new ArrayList<>();

        Set<String> faculties = new HashSet<>();
        for (Node n : nodes) {
            faculties.add(n.getFaculty());
        }

        for (String faculty : faculties) {
            List<Node> nodesOfFaculty = nodes.stream().filter(n -> n.getFaculty().equals(faculty))
                    .collect(Collectors.toList());
            int cpuSum = nodesOfFaculty.stream().mapToInt(Node::getCpu).sum();
            int gpuSum = nodesOfFaculty.stream().mapToInt(Node::getGpu).sum();
            int memorySum = nodesOfFaculty.stream().mapToInt(Node::getMemory).sum();

            answer.add(new Resource(cpuSum, gpuSum, memorySum));
        }
        return answer;
    }
}
