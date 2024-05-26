package hopla.routesmart.service;

import hopla.routesmart.entity.Edge;
import hopla.routesmart.entity.Node;
import hopla.routesmart.repository.EdgeRepository;
import hopla.routesmart.repository.NodeRepository;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GraphService {
    @Autowired
    private NodeRepository nodeRepository;

    @Autowired
    private EdgeRepository edgeRepository;

    private final GeometryFactory geometryFactory = new GeometryFactory();

    /**
     * Saves a new node with the given name, latitude, and longitude.
     *
     * @param name the name of the node
     * @param latitude the latitude of the node
     * @param longitude the longitude of the node
     * @return the saved node
     */
    public Node saveNode(String name, double latitude, double longitude) {
        Point point = geometryFactory.createPoint(new Coordinate(longitude, latitude));
        Node node = new Node();

        node.setName(name);
        node.setGeom(point);

        return nodeRepository.save(node);
    }

    /**
     * Saves a new edge with the given start and end nodes, distance, complexity, and geometry.
     *
     * @param fromNode the starting node of the edge
     * @param toNode the ending node of the edge
     * @param distance the distance of the edge
     * @param complexity the complexity of the edge
     * @return the saved edge
     */
    public Edge saveEdge(Node fromNode, Node toNode, double distance, int complexity, LineString geom) {
        Edge edge = new Edge();
        edge.setFromNode(fromNode);
        edge.setToNode(toNode);
        edge.setDistance(distance);
        edge.setComplexity(complexity);
        edge.setGeom(geom);

        return edgeRepository.save(edge);
    }


    /**
     * Finds a node by its name.
     *
     * @param name the name of the node
     * @return the node with the given name, or null if not found
     */
    public Node findNodeByName(String name) {
        return nodeRepository.findByName(name);
    }

    /**
     * Finds edges originating from a specific node.
     *
     * @param fromNode the starting node of the edges
     * @return the list of edges originating from the given node
     */
    public List<Edge> findEdgesByFromNode(Node fromNode) {
        return edgeRepository.findByFromNode(fromNode);
    }

    public Node findNodeById(Long id) {
        return nodeRepository.findById(id).orElse(null);
    }

    /**
     * Finds all nodes.
     * @return List of all nodes.
     */
    public List<Node> getAllNodes() {
        return nodeRepository.findAll();
    }

    /**
     * Finds all edges.
     * @return List of all edges.
     */
    public List<Edge> getAllEdges() {
        return edgeRepository.findAll();
    }

    /**
     * Finds edge between two nodes.
     * @param fromNode The starting node.
     * @param toNode The ending node.
     * @return The edge.
     */
    public Edge findEdgeByNodes(Node fromNode, Node toNode) {
        return edgeRepository.findByFromNodeAndToNode(fromNode, toNode);
    }
}
