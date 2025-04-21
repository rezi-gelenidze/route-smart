package hopla.routesmart.repository;

import hopla.routesmart.dto.ParcelDTO;
import hopla.routesmart.entity.Parcel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ParcelRepository extends JpaRepository<Parcel, Long> {
    // Retrieves detailed information about parcels that are part of a precomputed path
    String QUERY_FIND_MATCHING_PARCELS = """
        SELECT p.id AS id, p.from_node AS fromId, p.to_node AS toId
        FROM parcel p
        WHERE EXISTS (
            SELECT 1 FROM precomputed_path pp
                WHERE pp.path ~ CONCAT('(^|,)', p.from_node, '(,|$).*?(^|,)', p.to_node, '(,|$)')
                AND pp.start_node = :startNodeId
                AND pp.end_node = :endNodeId
        )
    """;

    @Query(value = QUERY_FIND_MATCHING_PARCELS, nativeQuery = true)
    List<ParcelDTO> findMatchingParcels(@Param("startNodeId") Long startNodeId, @Param("endNodeId") Long endNodeId);
}
