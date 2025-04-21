package hopla.routesmart.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.locationtech.jts.geom.LineString;


@Data
@Entity
public class Edge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "from_node")
    private Node from;

    @ManyToOne
    @JoinColumn(name = "to_node")
    private Node to;

    // Road distance between the two nodes in kilometers (edge cost factor)
    @Column
    private Double distance;

    // Weighted complexity of the edge (edge cost factor)
    @Column
    private Integer complexity;

    @Column(columnDefinition = "GEOMETRY(LineString, 4326)")
    private LineString geom;
}
