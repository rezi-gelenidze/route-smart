package hopla.routesmart.service;

import hopla.routesmart.entity.Edge;
import hopla.routesmart.entity.Node;
import hopla.routesmart.repository.EdgeRepository;
import hopla.routesmart.repository.NodeRepository;

import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;

import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class GraphService {
    private final NodeRepository nodeRepository;
    private final EdgeRepository edgeRepository;

    private final GeometryFactory geometryFactory = new GeometryFactory();


    public void saveNode(String name, double latitude, double longitude) {
        Point point = geometryFactory.createPoint(new Coordinate(longitude, latitude));
        Node node = new Node();

        node.setName(name);
        node.setGeom(point);

        nodeRepository.save(node);
    }

    public void saveEdge(Node fromNode, Node toNode, double distance, int complexity, LineString geom) {
        Edge edge = new Edge();
        edge.setFrom(fromNode);
        edge.setTo(toNode);
        edge.setDistance(distance);
        edge.setComplexity(complexity);
        edge.setGeom(geom);

        edgeRepository.save(edge);
    }

    public Node findNodeById(Long id) {
        return nodeRepository.findById(id).orElse(null);
    }

    public List<Node> getAllNodes() {
        return nodeRepository.findAll();
    }

    public List<Edge> getAllEdges() {
        return edgeRepository.findAll();
    }

    public Edge findEdgeByNodes(Node fromNode, Node toNode) {
        return edgeRepository.findByFromAndTo(fromNode, toNode);
    }
}
