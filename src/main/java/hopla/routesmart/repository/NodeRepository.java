package hopla.routesmart.repository;

import hopla.routesmart.entity.Node;
import org.springframework.data.jpa.repository.JpaRepository;



public interface NodeRepository extends JpaRepository<Node, Long> {
    Node findByName(String name);
    Node findById(long id);
}