package hopla.routesmart.repository;

import hopla.routesmart.entity.PrecomputedPath;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PrecomputedPathRepository extends JpaRepository<PrecomputedPath, Long> {

    @Query("SELECT p FROM PrecomputedPath p " +
            "WHERE p.startNode.id = :startNodeId " +
            "AND p.endNode.id = :endNodeId")
    List<PrecomputedPath> findByStartNodeIdAndEndNodeId(
            @Param("startNodeId") Long startNodeId,
            @Param("endNodeId") Long endNodeId
    );

    @Query("SELECT p FROM PrecomputedPath p " +
            "WHERE p.path LIKE %:parcelFromNodeId% " +
            "AND p.path LIKE %:parcelToNodeId% " +
            "AND POSITION(:parcelFromNodeId IN p.path) < POSITION(:parcelToNodeId IN p.path)")
    List<PrecomputedPath> findPathsBySubpath(
            @Param("parcelFromNodeId") String parcelFromNodeId,
            @Param("parcelToNodeId") String parcelToNodeId
    );
}
