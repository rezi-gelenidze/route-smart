package hopla.routesmart.repository;

import hopla.routesmart.dto.TripDTO;
import hopla.routesmart.entity.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TripRepository extends JpaRepository<Trip, Long> {
    // Retrieves detailed information about trips that match the given start and end node IDs
    String QUERY_FIND_MATCHING_TRIPS = """
        SELECT t.id AS id, t.from_node AS fromId, t.to_node AS toId, t.start_time AS startTime, t.duration AS duration
        FROM trip t
        WHERE EXISTS(
            SELECT 1 FROM precomputed_path pp
                WHERE pp.path ~ CONCAT('(^|,)', :startNodeId, '(,|$).*?(^|,)', :endNodeId, '(,|$)')
                AND pp.start_node = t.from_node
                AND pp.end_node = t.to_node
        )
    """;

    @Query(value = QUERY_FIND_MATCHING_TRIPS, nativeQuery = true)
    List<TripDTO> findMatchingTrips(@Param("startNodeId") Long startNodeId, @Param("endNodeId") Long endNodeId);
}
