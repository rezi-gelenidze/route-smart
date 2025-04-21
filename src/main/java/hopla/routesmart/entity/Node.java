package hopla.routesmart.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.locationtech.jts.geom.Point;


@Data
@Entity
public class Node {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    // Display name of the node
    @Column(nullable = false)
    private String name;

    // Geometry field for the node's location on the map
    @Column(columnDefinition = "GEOMETRY(Point, 4326)")
    private Point geom;
}
