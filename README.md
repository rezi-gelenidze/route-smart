# Route Smart (v1.0-alpha) 🚚

Route Smart is a powerful algorithm designed to efficiently match senders (with parcels or autostop persons) and
shippers (with trips) in
Georgia. This solution leverages graph-based pathfinding to provide optimal matches, ensuring that parcels can find
appropriate trips and vice versa.

## Problem Statement 🔴

In a logistics network, we often encounter the challenge of matching parcels with trips and trips with parcels. The goal
is to ensure that parcels can be transported from their origin to their destination by finding compatible trips that
pass through their route. Similarly, trips should be able to find parcels along their path to maximize efficiency and
utilization.

## Solution Overview 💡

The solution involves several key steps:

1. **Data Model**: Representing destinations and routes as a graph.
2. **Pathfinding**: Implementing the A* search algorithm to find optimal paths.
3. **Matching**: Matching parcels with trips and trips with parcels based on the computed paths.
4. **Querying**: Efficiently querying the results to provide matches.

## Data Model 🗺️

### Destinations

The system covers major destinations in Georgia (96 in total), each represented as a node in the graph. These
destinations include cities and towns mapped with unique identifiers (1-96 Primary Key).

### Graph Representation

Each destination (node) is connected to other destinations via edges. These edges are weighted with the distance and
complexity of the route. The graph is represented as an adjacency list where each node has a list of tuples representing
its neighbors, the distance to them, and the complexity of the route.

```java
public class Node {
    private Long id;
    private String name;
    private Point geom; // Geometry point for location
}

public class Edge {
    private Long id;
    private Node fromNode;
    private Node toNode;
    private double distance;
    private int complexity;
    private LineString geom; // Geometry line for route
}

public class PrecomputedPath {
    private Long id;
    private Node startNode;
    private Node endNode;
    private String path; // Comma-separated sequence list of node IDs
    private double totalDistance;
    private double averageComplexity;
}
```

## Pathfinding 🧭

### A* Search Algorithm

The A* search algorithm is used to find the best possible routes from a start to an end destination. This algorithm
combines the strengths of Dijkstra's algorithm and Greedy Best-First-Search. It uses a heuristic function (Euclidean
distance) and road complexity and road (non-linear) distance from A to B constraints to estimate the cost from the
current node to the destination.

## Matching 🚚📦

### Matching Trips with Parcels

When a trip is created, the system needs to find all parcels that can be picked up along the trip's route. The algorithm
checks if the parcel's route is a subpath of any of the precomputed paths for the trip.

### Matching Parcels with Trips

Similarly, when a parcel is created, the system finds all trips that can transport the parcel along their route. The
algorithm checks if the trip's route contains the parcel's route as a subpath.

## Precomputing Paths

To optimize the matching process, the system precomputes all possible paths between destinations. This ensures that the
matching algorithm can quickly find the best possible routes without having to compute them on the fly. (Memoization)

## Querying The Results

The system uses optimized SQL queries to efficiently find matching trips and parcels. This ensures that the results are
returned quickly and accurately.

## Running the Application

### Prerequisites

- Java 17
- Spring Boot
- PostGIS-enabled PostgreSQL

### Running the Application with Command Line Arguments

To seed the edges and nodes (edges.geojson and nodes.geojson must be present in the resources folder):

```sh
mvn spring-boot:run -Dspring-boot.run.arguments=seed
```

To precompute paths (Memoization step for optimized pathfinding):

```sh
mvn spring-boot:run -Dspring-boot.run.arguments=precompute
```

### API Endpoints

- **GET /match/trip/{tripId}**: Returns the IDs of parcels that match the given trip.
- **GET /match/parcel/{parcelId}**: Returns the IDs of trips that match the given parcel.

## Conclusion

Route Smart is a comprehensive solution for efficiently matching parcels with trips and vice versa. By leveraging
graph-based pathfinding and optimized querying, it ensures that logistics operations are streamlined and efficient. This
system was first prototyped in Python and then implemented in Spring Boot for enhanced performance and scalability.