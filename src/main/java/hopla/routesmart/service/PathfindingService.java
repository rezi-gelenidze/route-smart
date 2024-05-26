package hopla.routesmart.service;

import hopla.routesmart.entity.Edge;
import hopla.routesmart.entity.Node;
import hopla.routesmart.entity.PrecomputedPath;
import hopla.routesmart.repository.EdgeRepository;
import hopla.routesmart.repository.PrecomputedPathRepository;

import org.locationtech.jts.geom.Coordinate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PathfindingService {
    @Autowired
    private EdgeRepository edgeRepository;

    @Autowired
    private PrecomputedPathRepository precomputedPathRepository;

    @Autowired
    private GraphService graphService;


    private Map<Long, Node> nodeMap;
    private Map<Long, List<Edge>> edgeMap;

    @PostConstruct
    private void preloadData() {
        List<Node> nodes = graphService.getAllNodes();
        List<Edge> edges = graphService.getAllEdges();

        nodeMap = nodes.stream().collect(Collectors.toMap(Node::getId, node -> node));
        edgeMap = edges.stream().collect(Collectors.groupingBy(edge -> edge.getFromNode().getId()));
    }

    public void precomputePaths() {

        for (Node startNode : nodeMap.values()) {
            for (Node endNode : nodeMap.values()) {
                if (!startNode.equals(endNode)) {
                    List<PrecomputedPath> paths = findPaths(startNode, endNode);
                    System.out.println("Found " + paths.size() + " paths between " + startNode.getName() + " and " + endNode.getName());

                    // Batch save the precomputed paths
                    precomputedPathRepository.saveAll(paths);
                }
            }
        }


    }

    private List<PrecomputedPath> findPaths(Node startNode, Node endNode) {
        System.out.println("Finding paths from " + startNode.getName() + " to " + endNode.getName());
        List<PrecomputedPath> foundPaths = new ArrayList<>(); // Accumulator for found paths

        // Explored set of node ids
        Set<Long> explored = new HashSet<>();

        // Frontier of nodes to explore, sorted by score
        PriorityQueue<PathNode> frontier = new PriorityQueue<>(Comparator.comparingDouble(PathNode::getScore));

        // Node to Score mapping
        Map<Node, Double> gScores = new HashMap<>();
        gScores.put(startNode, 0.0);    // Start node has a score of 0

        // Add first link to frontier
        frontier.add(new PathNode(startNode, null, 0, heuristic(startNode, endNode), 0));

        while (!frontier.isEmpty() && foundPaths.size() < 3) {
            PathNode current = frontier.poll();

            if (current.getNode().getId().equals(endNode.getId())) {
                foundPaths.add(reconstructPath(current));
                continue;
            }

            // Mark node as explored
            explored.add(current.getNode().getId());

            List<Edge> neighbors = edgeMap.getOrDefault(current.getNode().getId(), Collections.emptyList()).stream()
                    .filter(edge -> !explored.contains(edge.getToNode().getId()))
                    .toList();

            for (Edge edge : neighbors) {
                Node neighbor = edge.getToNode();

                double tentativeGScore = current.getGScore() + edge.getDistance();
                double complexityScore = current.getComplexityScore() + edge.getComplexity();
                double fScore = tentativeGScore + heuristic(neighbor, endNode) + complexityScore;

                if (!gScores.containsKey(neighbor) || tentativeGScore < gScores.get(neighbor)) {
                    gScores.put(neighbor, tentativeGScore);
                    frontier.add(new PathNode(neighbor, current, tentativeGScore, fScore, complexityScore));
                }
            }
        }

        return foundPaths;
    }

    private double heuristic(Node current, Node end) {
        // Euclidean distance between two nodes
        Coordinate currentCoord = current.getGeom().getCoordinate();
        Coordinate endCoord = end.getGeom().getCoordinate();
        return currentCoord.distance(endCoord);
    }

    private PrecomputedPath reconstructPath(PathNode node) {
        List<Long> nodeIds = new ArrayList<>();
        double totalDistance = 0;
        double totalComplexity = 0;

        PathNode current = node;
        while (current != null) {
            nodeIds.add(0, current.getNode().getId());
            if (current.getPrevious() != null) {
                Node fromNode = current.getPrevious().getNode();
                Node toNode = current.getNode();
                Edge edge = graphService.findEdgeByNodes(fromNode, toNode);

                // Ensure the edge is managed and persisted before setting in the precomputed path
                if (!edgeRepository.existsById(edge.getId())) {
                    edge = edgeRepository.save(edge);
                }

                totalDistance += edge.getDistance();
                totalComplexity += edge.getComplexity();
            }
            current = current.getPrevious();
        }

        PrecomputedPath precomputedPath = new PrecomputedPath();
        precomputedPath.setStartNode(nodeMap.get(nodeIds.get(0)));
        precomputedPath.setEndNode(nodeMap.get(nodeIds.get(nodeIds.size() - 1)));

        String nodeIdsString = nodeIds.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));
        precomputedPath.setPath(nodeIdsString);

        double totalDistanceRounded = Math.round(totalDistance * 100.0) / 100.0;
        double averageComplexity = totalComplexity / (nodeIds.size() - 1);

        precomputedPath.setTotalDistance(totalDistanceRounded);
        precomputedPath.setAverageComplexity(Math.round(averageComplexity * 100.0) / 100.0);


        return precomputedPath;
    }


    // Helper class to represent a node in the pathfinding algorithm
    private static class PathNode {
        private final Node node;
        private final PathNode previous;
        private final double gScore;
        private final double score;
        private final double complexityScore;

        public PathNode(Node node, PathNode previous, double gScore, double score, double complexityScore) {
            this.node = node;
            this.previous = previous;
            this.gScore = gScore;
            this.score = score;
            this.complexityScore = complexityScore;
        }

        public Node getNode() {
            return node;
        }

        public PathNode getPrevious() {
            return previous;
        }

        public double getGScore() {
            return gScore;
        }

        public double getScore() {
            return score;
        }

        public double getComplexityScore() {
            return complexityScore;
        }
    }
}
