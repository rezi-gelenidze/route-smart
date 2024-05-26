package hopla.routesmart.repository;

import hopla.routesmart.entity.Edge;
import hopla.routesmart.entity.Node;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EdgeRepository extends JpaRepository<Edge, Long> {
    List<Edge> findByFromNode(Node fromNode);
    Edge findByFromNodeAndToNode(Node fromNode, Node toNode);
}
