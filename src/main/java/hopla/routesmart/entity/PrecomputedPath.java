package hopla.routesmart.entity;

import jakarta.persistence.*;

@Entity
public class PrecomputedPath {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "start_node_id")
    private Node startNode;

    @ManyToOne
    @JoinColumn(name = "end_node_id")
    private Node endNode;

    @Column(columnDefinition = "TEXT")
    private String path; // Comma-separated sequence list of node IDs

    private double totalDistance;
    private double averageComplexity;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Node getStartNode() {
        return startNode;
    }

    public void setStartNode(Node startNode) {
        this.startNode = startNode;
    }

    public Node getEndNode() {
        return endNode;
    }

    public void setEndNode(Node endNode) {
        this.endNode = endNode;
    }

    public String getPath() {
        return this.path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public double getTotalDistance() {
        return totalDistance;
    }

    public void setTotalDistance(double totalDistance) {
        this.totalDistance = totalDistance;
    }

    public double getAverageComplexity() {
        return averageComplexity;
    }

    public void setAverageComplexity(double averageComplexity) {
        this.averageComplexity = averageComplexity;
    }
}
