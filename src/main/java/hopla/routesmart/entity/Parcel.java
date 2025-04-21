package hopla.routesmart.entity;

import jakarta.persistence.*;
import lombok.Data;


@Data
@Entity
public class Parcel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "from_node")
    private Node from;

    @ManyToOne
    @JoinColumn(name = "to_node")
    private Node to;
}
