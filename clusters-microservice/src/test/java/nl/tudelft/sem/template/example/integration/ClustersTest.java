package nl.tudelft.sem.template.example.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.JsonProcessingException;
import commons.Faculties;
import commons.Faculty;
import commons.FacultyResourceModel;
import commons.NetId;
import commons.Role;
import commons.RoleValue;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import nl.tudelft.sem.template.example.authentication.AuthManager;
import nl.tudelft.sem.template.example.authentication.JwtTokenVerifier;
import nl.tudelft.sem.template.example.controllers.NodeController;
import nl.tudelft.sem.template.example.domain.Node;
import nl.tudelft.sem.template.example.domain.NodeRepository;
import nl.tudelft.sem.template.example.integration.utils.JsonUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@SpringBootTest
@ExtendWith(SpringExtension.class)
// activate profiles to have spring use mocks during auto-injection of certain beans.
@ActiveProfiles({"test", "mockTokenVerifier", "mockAuthManager", "mockAuthenticationManager", "nodeController"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class ClustersTest {
    @Autowired
    private transient NodeRepository nodeRepository;

    @Autowired
    private transient NodeController nodeController;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private transient AuthManager mockAuthManager;

    @Autowired
    private transient JwtTokenVerifier mockTokenVerifier;

    /**
     * Init method for tests.
     *
     * @throws JsonProcessingException an exception
     */
    @BeforeEach
    public void init() throws JsonProcessingException {
        nodeRepository.deleteAll();
        when(mockTokenVerifier.validateToken(anyString())).thenReturn(true);
        when(mockTokenVerifier.getNetIdFromToken(anyString())).thenReturn("SomeUser");
        when(mockAuthManager.getNetId()).thenReturn("SomeUser");
        when(mockAuthManager.getFaculty()).thenReturn(new Faculties("EEMCS"));


    }

    @Test
    public void addNodeTest_to_free_pool() throws Exception {
        // Arrange
        final NetId testUser = new NetId("SomeUser");
        final Faculty faculty = new Faculty("EEMCS");

        final Node node1 = new Node(testUser.toString(), "url", "EEMCS", "token", 10, 10, 10);

        when(mockAuthManager.getRole()).thenReturn(new Role(RoleValue.ADMIN));

        // Act
        ResultActions resultActions = mockMvc.perform(post("/cluster/addNode")
            .contentType(MediaType.APPLICATION_JSON)
            .content(JsonUtil.serialize(node1))
            .header("Authorization", "Bearer MockedToken")
        );

        // Assert
        resultActions.andExpect(status().isOk());

        List<Node> savedNodes = nodeRepository.getAllNodes().orElseThrow();

        assertThat(savedNodes.size()).isEqualTo(1);
        Node firstNode = savedNodes.get(0);

        assertThat(firstNode.getName()).isEqualTo(testUser.toString());
        assertThat(firstNode.getFaculty()).isEqualTo("FreePool");
    }

    @Test
    public void addNodeTest_to_faculty() throws Exception {
        // Arrange
        final NetId testUser = new NetId("SomeUser");
        final Faculty faculty = new Faculty("EEMCS");

        final Node node1 = new Node(testUser.toString(), "url", "EEMCS", "token", 10, 10, 10);

        when(mockAuthManager.getRole()).thenReturn(new Role(RoleValue.EMPLOYEE));

        // Act
        ResultActions resultActions = mockMvc.perform(post("/cluster/addNode")
            .contentType(MediaType.APPLICATION_JSON)
            .content(JsonUtil.serialize(node1))
            .header("Authorization", "Bearer MockedToken")
        );

        // Assert
        resultActions.andExpect(status().isOk());

        List<Node> savedNodes = nodeRepository.getAllNodes().orElseThrow();

        assertThat(savedNodes.size()).isEqualTo(1);
        Node firstNode = savedNodes.get(0);

        assertThat(firstNode.getName()).isEqualTo(testUser.toString());
        assertThat(firstNode.getFaculty()).isEqualTo("EEMCS");
    }

    /**
     * Not yet working test to get all resources.
     *
     * @throws Exception exception.
     */
    public void resourcesTest() throws Exception {
        // Arrange
        final NetId testUser = new NetId("SomeUser");

        final Node node1 = new Node(testUser.toString(), "url", "EEMCS", "token", 10, 10, 10);
        final Node node2 = new Node("SomeUser", "url", "EE", "token", 30, 15, 10);

        when(mockAuthManager.getRole()).thenReturn(new Role(RoleValue.ADMIN));

        nodeRepository.save(node1);
        nodeRepository.save(node2);

        LocalDate date = LocalDate.now();

        FacultyResourceModel fr = new FacultyResourceModel();
        fr.setFaculty("EE");
        fr.setDate(date);

        ResultActions r1 = mockMvc.perform(post("/cluster/addNode")
            .contentType(MediaType.APPLICATION_JSON)
            .content(JsonUtil.serialize(node1))
            .header("Authorization", "Bearer MockedToken")
        );
        ResultActions r2 = mockMvc.perform(post("/cluster/addNode")
            .contentType(MediaType.APPLICATION_JSON)
            .content(JsonUtil.serialize(node2))
            .header("Authorization", "Bearer MockedToken")
        );
        // Act
        ResultActions resultActions = mockMvc.perform(get("/cluster/resources")
            .header("Authorization", "Bearer MockedToken")
        );

        // Assert
        resultActions.andExpect(status().isOk());

    }

}
