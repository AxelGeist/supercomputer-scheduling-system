package nl.tudelft.sem.template.example.domain;


import commons.FacultyResource;
import commons.Resource;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
public interface NodeRepository extends JpaRepository<Node, Long> {
    /**
     * Deletes the Node from the database.
     *
     * @param id of the node you want to delete
     */
    @Query(
            nativeQuery = true,
            value = "DELETE FROM Node WHERE id = ?1")
    void deleteNode(long id);


    /**
     * Gets all Nodes currently available.
     * Can only be done by an admin.
     */
    @Query(
            nativeQuery = true,
            value = "SELECT * FROM Node ORDER BY faculty")
    Optional<List<Node>> getAllNodes();

    /**
     * Gets all nodes belonging to the specific family.
     *
     * @param  faculty you want to get the nodes of
     * @return Optional of a Node list from the specific faculty
     */
    @Query(
            nativeQuery = true,
            value = "SELECT * FROM Node WHERE faculty = ?1")
    Optional<List<Node>> getNodesByFaculty(String faculty);

    /**
     * Gets all nodes that belong to faculty.
     * And Nodes that are released.
     *
     * @param  faculty you want to get the nodes of
     * @param date date you want to get the resources on
     * @return Optional of a Node list from the specific faculty
     */
    @Query(
            nativeQuery = true,
            value = "SELECT SUM(CPU), SUM(GPU), SUM(MEMORY) FROM Node "
                    + "WHERE faculty = ?1 OR "
                    + "(releasedStart <= ?2 AND releasedEND >= ?2)")
    Optional<Resource> getFreeResources1(String faculty, LocalDate date);

    /**
     * Gets all nodes that belong to faculty.
     * And Nodes that are released.
     *
     * @param  faculty you want to get the nodes of
     * @param date date you want to get the resources on
     * @return Optional of a Node list from the specific faculty
     */
    @Query(
            nativeQuery = true,
            value = "SELECT * FROM Node "
                    + "WHERE removedDate IS NULL AND (faculty = ?1 OR "
                    + "(releasedStart <= ?2 AND releasedEND >= ?2))")
    Optional<List<Node>> getAvailableResources(String faculty, LocalDate date);

    /**
     * Gets all nodes that belong to faculty.
     * And Nodes that are released.
     *
     * @param  faculty you want to get the nodes of
     * @param date date you want to get the resources on
     * @return Optional of a Node list from the specific faculty
     */
    @Query(
            nativeQuery = true,
            value = "SELECT SUM(CPU), SUM(GPU), SUM(MEMORY) FROM Node "
                    + "WHERE faculty = ?1 OR "
                    + "(released <= ?2 AND releaseEND >= ?2)")
    Optional<FacultyResource> getReservedResources(String faculty, String date);

    /**
     * Meant to return in a FacultyResource model.
     *
     * @param  facultyToUpdate faculty of nodes you want to update
     * @param  start date you want to free resouces on
     * @param  end date the free will end for
     */
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(
            nativeQuery = true,
            value = "UPDATE Node SET RELEASEDSTART = ?2, RELEASEDEND = ?3 WHERE faculty = ?1")
    void updateRelease(String facultyToUpdate, LocalDate start, LocalDate end);

    /**
     * Returns node with the id.
     *
     * @param  id of the node you want to return
     */
    @Query(
            nativeQuery = true,
            value = "SELECT * FROM NODE WHERE id = ?1")
    Optional<Node> getNodeById(long id);

    /**
     * flag the ndoe with id as deleted from tomorrow.
     *
     * @param  token of access todelete it
     * @param  date deleted from tomorrow
     */
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(
            nativeQuery = true,
            value = "UPDATE NODE SET removedDate = ?2 WHERE token = ?1")
    void setAsDeleted(String token, LocalDate date);

    /**
     * Returns node with the token.
     *
     * @param toke of the node you want to return
     */
    @Query(
            nativeQuery = true,
            value = "SELECT * FROM NODE WHERE token = ?1 LIMIT 1")
    Optional<Node> getNodeByToken(String toke);
    
}