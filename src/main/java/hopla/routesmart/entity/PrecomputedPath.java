package hopla.routesmart.entity;

import jakarta.persistence.*;
import lombok.Data;


@Data
@Entity
public class PrecomputedPath {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "start_node")
    private Node start;

    @ManyToOne
    @JoinColumn(name = "end_node")
    private Node end;

    @Column(columnDefinition = "TEXT")
    private String path; // Comma-separated sequence list of node IDs

    // Precomputed fields of sum of path edge distances and average complexities
    @Column
    private Double totalDistance;

    @Column
    private Double averageComplexity;
}
