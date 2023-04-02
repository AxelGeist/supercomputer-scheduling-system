package nl.tudelft.sem.template.example.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private final transient NodeRepository nodeRepository;

    @Autowired
    public DataLoader(NodeRepository nodeRepository) {
        this.nodeRepository = nodeRepository;
    }

    @Override
    public void run(String ...args) throws Exception {
        loadUsers();
    }

    public void loadUsers() {
        nodeRepository.save(new Node("node1", "node1", "EEMCS", "token", 1000, 1000, 1000));
        nodeRepository.save(new Node("node2", "node2", "Pool", "token", 1000, 1000, 1000));
    }
}
