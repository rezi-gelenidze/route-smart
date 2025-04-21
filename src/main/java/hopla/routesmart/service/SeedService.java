package hopla.routesmart.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import hopla.routesmart.entity.Node;
import hopla.routesmart.entity.Parcel;
import hopla.routesmart.entity.Trip;
import hopla.routesmart.repository.NodeRepository;
import hopla.routesmart.repository.ParcelRepository;
import hopla.routesmart.repository.TripRepository;

import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class SeedService {
    private final NodeRepository nodeRepository;
    private final ParcelRepository parcelRepository;
    private final TripRepository tripRepository;

    private final GraphService graphService;

    private final ResourceLoader resourceLoader;
    private final GeometryFactory geometryFactory = new GeometryFactory();

    public void seedNodes(String geoJsonPath) throws IOException {
        Resource resource = resourceLoader.getResource(geoJsonPath);
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> geoJson = mapper.readValue(resource.getInputStream(), Map.class);

        List<Map<String, Object>> features = (List<Map<String, Object>>) geoJson.get("features");
        for (Map<String, Object> feature : features) {
            Map<String, Object> properties = (Map<String, Object>) feature.get("properties");
            Map<String, Object> geometry = (Map<String, Object>) feature.get("geometry");

            String name = (String) properties.get("name");
            List<Double> coordinates = (List<Double>) geometry.get("coordinates");
            double longitude = coordinates.get(0);
            double latitude = coordinates.get(1);

            graphService.saveNode(name, latitude, longitude);
        }
    }

    public void seedEdges(String geoJsonPath) throws IOException {
        Resource resource = resourceLoader.getResource(geoJsonPath);
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> geoJson = mapper.readValue(resource.getInputStream(), Map.class);

        List<Map<String, Object>> features = (List<Map<String, Object>>) geoJson.get("features");
        for (Map<String, Object> feature : features) {
            Map<String, Object> properties = (Map<String, Object>) feature.get("properties");
            Map<String, Object> geometry = (Map<String, Object>) feature.get("geometry");

            long fromId = ((Number) properties.get("from")).longValue();
            long toId = ((Number) properties.get("to")).longValue();
            double distance = ((Number) properties.get("distance")).doubleValue();
            int complexity = ((Number) properties.get("complexity")).intValue();
            LineString geom = geometryFactory.createLineString(
                    ((List<List<Double>>) geometry.get("coordinates")).stream()
                            .map(coord -> new Coordinate(coord.get(0), coord.get(1)))
                            .toArray(Coordinate[]::new)
            );
            Node fromNode = graphService.findNodeById(fromId);
            Node toNode = graphService.findNodeById(toId);

            graphService.saveEdge(fromNode, toNode, distance, complexity, geom);
        }
    }

    // Generates random A to B parcels and trips
    public void seedDummyTripsAndParcels() {
        int amount = 10000;

        List<Node> nodes = nodeRepository.findAll();
        Random random = new Random();

        for (int i = 1; i <= amount; i++) {
            // Generate random parcels
            Parcel parcel = new Parcel();
            Node fromNode = nodes.get(random.nextInt(nodes.size()));
            Node toNode = nodes.get(random.nextInt(nodes.size()));
            while (fromNode.equals(toNode)) {
                toNode = nodes.get(random.nextInt(nodes.size()));
            }

            parcel.setFrom(fromNode);
            parcel.setTo(toNode);

            parcelRepository.save(parcel);

            // Generate random trips
            Trip trip = new Trip();
            fromNode = nodes.get(random.nextInt(nodes.size()));

            do {
                toNode = nodes.get(random.nextInt(nodes.size()));
            } while (fromNode.equals(toNode));

            trip.setFrom(fromNode);
            trip.setTo(toNode);
            trip.setStartTime(LocalDateTime.now());
            trip.setDuration(Duration.ofHours(1));

            tripRepository.save(trip);
        }
    }

}
