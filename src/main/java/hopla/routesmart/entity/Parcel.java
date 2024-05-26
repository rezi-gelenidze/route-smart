package hopla.routesmart.entity;

import jakarta.persistence.*;

@Entity
public class Parcel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private Long identifier;

    @ManyToOne
    @JoinColumn(name = "from_node_id")
    private Node fromLocation;

    @ManyToOne
    @JoinColumn(name = "to_node_id")
    private Node toLocation;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdentifier() {
        return identifier;
    }

    public void setIdentifier(Long identifier) {
        this.identifier = identifier;
    }

    public Node getFromLocation() {
        return fromLocation;
    }

    public void setFromLocation(Node fromLocation) {
        this.fromLocation = fromLocation;
    }

    public Node getToLocation() {
        return toLocation;
    }

    public void setToLocation(Node toLocation) {
        this.toLocation = toLocation;
    }
}
