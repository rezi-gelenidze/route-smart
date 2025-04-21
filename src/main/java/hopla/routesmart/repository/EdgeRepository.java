package hopla.routesmart.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import hopla.routesmart.entity.Edge;
import hopla.routesmart.entity.Node;


public interface EdgeRepository extends JpaRepository<Edge, Long> {
    Edge findByFromAndTo(Node from, Node to);
}
