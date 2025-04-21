package hopla.routesmart.service;

import hopla.routesmart.entity.Edge;
import hopla.routesmart.entity.Node;
import hopla.routesmart.entity.PrecomputedPath;
import hopla.routesmart.repository.EdgeRepository;
import hopla.routesmart.repository.PrecomputedPathRepository;

import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.springframework.stereotype.Service;


import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PathfindingService {
    private final EdgeRepository edgeRepository;
    private final PrecomputedPathRepository precomputedPathRepository;

    private final GraphService graphService;

    private Map<Long, Node> nodeMap;
    private Map<Long, List<Edge>> edgeMap;


    public void precomputePaths() {
        System.out.println("Loading graph data...");
        List<Node> nodes = graphService.getAllNodes();
        List<Edge> edges = graphService.getAllEdges();

        nodeMap = nodes.stream().collect(Collectors.toMap(Node::getId, node -> node));
        edgeMap = edges.stream().collect(Collectors.groupingBy(edge -> edge.getFrom().getId()));

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
        PriorityQueue<PathNode> frontier = new PriorityQueue<>(Comparator.comparingDouble(PathNode::score));

        // Node to Score mapping
        Map<Node, Double> gScores = new HashMap<>();
        gScores.put(startNode, 0.0);    // Start node has a score of 0

        // Add first link to frontier
        frontier.add(new PathNode(startNode, null, 0, heuristic(startNode, endNode), 0));

        while (!frontier.isEmpty() && foundPaths.size() < 3) {
            PathNode current = frontier.poll();

            if (current.node().getId().equals(endNode.getId())) {
                foundPaths.add(reconstructPath(current));
                continue;
            }

            // Mark node as explored
            explored.add(current.node().getId());

            List<Edge> neighbors = edgeMap.getOrDefault(current.node().getId(), Collections.emptyList()).stream().filter(edge -> !explored.contains(edge.getTo().getId())).toList();

            for (Edge edge : neighbors) {
                Node neighbor = edge.getTo();

                double tentativeGScore = current.gScore() + edge.getDistance();
                double complexityScore = current.complexityScore() + edge.getComplexity();
                double fScore = tentativeGScore + heuristic(neighbor, endNode) + complexityScore;

                if (!gScores.containsKey(neighbor) || tentativeGScore < gScores.get(neighbor)) {
                    gScores.put(neighbor, tentativeGScore);
                    frontier.add(new PathNode(neighbor, current, tentativeGScore, fScore, complexityScore));
                }
            }
        }

        return foundPaths;
    }

    private double heuristic(Node a, Node b) {
        // Euclidean distance between two nodes
        Coordinate aCoordinate = a.getGeom().getCoordinate();
        Coordinate bCoordinate = b.getGeom().getCoordinate();
        return aCoordinate.distance(bCoordinate);
    }

    private PrecomputedPath reconstructPath(PathNode node) {
        List<Long> nodeIds = new ArrayList<>();
        double totalDistance = 0;
        double totalComplexity = 0;

        PathNode current = node;
        while (current != null) {
            nodeIds.add(0, current.node().getId());
            if (current.previous() != null) {
                Node fromNode = current.previous().node();
                Node toNode = current.node();
                Edge edge = graphService.findEdgeByNodes(fromNode, toNode);

                // Ensure the edge is managed and persisted before setting in the precomputed path
                if (!edgeRepository.existsById(edge.getId())) {
                    edge = edgeRepository.save(edge);
                }

                totalDistance += edge.getDistance();
                totalComplexity += edge.getComplexity();
            }
            current = current.previous();
        }

        PrecomputedPath precomputedPath = new PrecomputedPath();
        precomputedPath.setStart(nodeMap.get(nodeIds.get(0)));
        precomputedPath.setEnd(nodeMap.get(nodeIds.get(nodeIds.size() - 1)));

        String nodeIdsString = nodeIds.stream().map(String::valueOf).collect(Collectors.joining(","));
        precomputedPath.setPath(nodeIdsString);

        double totalDistanceRounded = Math.round(totalDistance * 100.0) / 100.0;
        double averageComplexity = totalComplexity / (nodeIds.size() - 1);

        precomputedPath.setTotalDistance(totalDistanceRounded);
        precomputedPath.setAverageComplexity(Math.round(averageComplexity * 100.0) / 100.0);


        return precomputedPath;
    }


    // Helper class to represent a node in the pathfinding algorithm
    private record PathNode(Node node, PathfindingService.PathNode previous, double gScore, double score,
                            double complexityScore) {
    }
}
