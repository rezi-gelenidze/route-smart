package hopla.routesmart.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.Duration;
import java.time.LocalDateTime;


@Data
@Entity
public class Trip {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "from_node")
    private Node from;

    @ManyToOne
    @JoinColumn(name = "to_node")
    private Node to;

    @Column
    LocalDateTime startTime;

    @Column
    Duration duration;
}
