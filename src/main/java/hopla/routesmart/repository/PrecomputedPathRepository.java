package hopla.routesmart.repository;

import hopla.routesmart.entity.Parcel;
import hopla.routesmart.entity.PrecomputedPath;
import hopla.routesmart.entity.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PrecomputedPathRepository extends JpaRepository<PrecomputedPath, Long> {

    String FIND_MATCHING_PARCELS_QUERY =
        "SELECT p.id FROM parcel p " +
            "WHERE EXISTS(" +
                "SELECT 1 FROM precomputed_path pp " +
                    "WHERE pp.start_node_id = :startNodeId " +
                        "AND pp.end_node_id = :endNodeId " +
                        "AND pp.path ~ CONCAT('(^|,)', p.from_node_id, '(,|$).*?(^|,)', p.to_node_id, '(,|$)')" +
            ")";

    String FIND_MATCHING_TRIPS =
            "SELECT t.* FROM Trip t " +
                    "WHERE EXISTS ( " +
                    "    SELECT 1 FROM PrecomputedPath pp " +
                    "    WHERE pp.start_node_id = :startNodeId " +
                    "    AND pp.end_node_id = :endNodeId " +
                    "    AND pp.path ~ CONCAT('(^|,)', t.from_location_id, '(,|$).*?(^|,)', t.to_location_id, '(,|$)') " +
                    ")";


    @Query(value = FIND_MATCHING_PARCELS_QUERY, nativeQuery = true)
    List<Long> findMatchingParcels(@Param("startNodeId") Long startNodeId, @Param("endNodeId") Long endNodeId);

    @Query(value = FIND_MATCHING_TRIPS, nativeQuery = true)
    List<Trip> findMatchingTrips(@Param("startNodeId") Long startNodeId, @Param("endNodeId") Long endNodeId);
}
