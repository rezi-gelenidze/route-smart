package hopla.routesmart.repository;

import hopla.routesmart.dto.ParcelDTO;
import hopla.routesmart.entity.Parcel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ParcelRepository extends JpaRepository<Parcel, Long> {
    // Retrieves detailed information about parcels that are part of a precomputed path
    String FIND_MATCHING_PARCELS_QUERY_VERBOSE = """
        SELECT p.identifier AS parcelId, p.from_node_id AS fromId, n1.name AS fromDisplay, p.to_node_id AS toId, n2.name AS toDisplay
        FROM parcel p
        JOIN node n1 ON p.from_node_id = n1.id
        JOIN node n2 ON p.to_node_id = n2.id
        WHERE EXISTS (
            SELECT 1 FROM precomputed_path pp
            WHERE pp.start_node_id = :startNodeId
            AND pp.end_node_id = :endNodeId
            AND pp.path ~ CONCAT('(^|,)', p.from_node_id, '(,|$).*?(^|,)', p.to_node_id, '(,|$)')
        )
    """;

    // Retrieves only the IDs of parcels that are part of a precomputed path
    String FIND_MATCHING_PARCELS_QUERY_SIMPLE = """
        SELECT p.identifier FROM parcel p
        WHERE EXISTS (
            SELECT 1 FROM precomputed_path pp
            WHERE pp.start_node_id = :startNodeId
            AND pp.end_node_id = :endNodeId
            AND pp.path ~ CONCAT('(^|,)', p.from_node_id, '(,|$).*?(^|,)', p.to_node_id, '(,|$)')
        )
    """;

    @Query(value = FIND_MATCHING_PARCELS_QUERY_VERBOSE, nativeQuery = true)
    List<ParcelDTO> findMatchingParcelsVerbose(@Param("startNodeId") Long startNodeId, @Param("endNodeId") Long endNodeId);

    @Query(value = FIND_MATCHING_PARCELS_QUERY_SIMPLE, nativeQuery = true)
    List<Long> findMatchingParcelsSimple(@Param("startNodeId") Long startNodeId, @Param("endNodeId") Long endNodeId);
}
