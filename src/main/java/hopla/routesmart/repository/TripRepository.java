package hopla.routesmart.repository;

import hopla.routesmart.dto.TripDTO;
import hopla.routesmart.entity.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TripRepository extends JpaRepository<Trip, Long> {
    // Retrieves detailed information about trips that match the given start and end node IDs
    String FIND_MATCHING_TRIPS_QUERY_VERBOSE = """
        SELECT t.identifier AS tripId, t.from_node_id AS fromId, n1.name AS fromDisplay, t.to_node_id AS toId, n2.name AS toDisplay
            FROM trip t
                JOIN node n1 ON t.from_node_id = n1.id
                JOIN node n2 ON t.to_node_id = n2.id
            WHERE EXISTS(
                SELECT 1 FROM precomputed_path pp
                    WHERE pp.path ~ CONCAT('(^|,)', :startNodeId, '(,|$).*?(^|,)', :endNodeId, '(,|$)')
                        AND pp.start_node_id = t.from_node_id
                        AND pp.end_node_id = t.to_node_id
            )
    """;

    // Retrieves IDs of trips that match the given start and end node IDs
    String FIND_MATCHING_TRIPS_QUERY_SIMPLE = """
        SELECT t.identifier FROM trip t
            WHERE EXISTS(
                SELECT 1 FROM precomputed_path pp
                    WHERE pp.path ~ CONCAT('(^|,)', :startNodeId, '(,|$).*?(^|,)', :endNodeId, '(,|$)')
                        AND pp.start_node_id = t.from_node_id
                        AND pp.end_node_id = t.to_node_id
            )
    """;

    @Query(value = FIND_MATCHING_TRIPS_QUERY_VERBOSE, nativeQuery = true)
    List<TripDTO> findMatchingTripsVerbose(@Param("startNodeId") Long startNodeId, @Param("endNodeId") Long endNodeId);

    @Query(value = FIND_MATCHING_TRIPS_QUERY_SIMPLE, nativeQuery = true)
    List<Long> findMatchingTripsSimple(@Param("startNodeId") Long startNodeId, @Param("endNodeId") Long endNodeId);
}
