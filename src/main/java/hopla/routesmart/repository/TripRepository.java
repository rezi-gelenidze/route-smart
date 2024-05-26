package hopla.routesmart.repository;

import hopla.routesmart.dto.TripDTO;
import hopla.routesmart.entity.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TripRepository extends JpaRepository<Trip, Long> {
    String FIND_MATCHING_TRIPS_QUERY = """
        SELECT t.id AS tripId, t.from_node_id AS fromId, n1.name AS fromDisplay, t.to_node_id AS toId, n2.name AS toDisplay
            FROM trip t
                JOIN node n1 ON t.from_node_id = n1.id
                JOIN node n2 ON t.to_node_id = n2.id
            WHERE EXISTS(
                SELECT 1 FROM precomputed_path pp
                    WHERE pp.path ~ CONCAT('(^|,)', :startNodeId, '(,|$).*?(^|,)', :endNodeId, '(,|$)')
            )
    """;

    @Query(value = FIND_MATCHING_TRIPS_QUERY, nativeQuery = true)
    List<TripDTO> findMatchingTrips(@Param("startNodeId") Long startNodeId, @Param("endNodeId") Long endNodeId);
}
