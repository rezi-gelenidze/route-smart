package hopla.routesmart.dto;

import hopla.routesmart.entity.Edge;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.LineString;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EdgeDTO {
    private Long id;

    private Long fromId;
    private Long toId;

    private Double distance;
    private Integer complexity;

    private Double[][] coordinates;

    public static EdgeDTO from(Edge edge) {
        return new EdgeDTO(
                edge.getId(),
                edge.getFrom().getId(),
                edge.getTo().getId(),
                edge.getDistance(),
                edge.getComplexity(),
                convertLineStringToCoordinates(edge.getGeom())
        );
    }

    private static Double[][] convertLineStringToCoordinates(LineString line) {
        Double[][] coordinates = new Double[2][2];

        // Start point
        coordinates[0][0] = line.getCoordinateN(0).x; // longitude p1
        coordinates[0][1] = line.getCoordinateN(0).y; // latitude p1

        // End point
        coordinates[1][0] = line.getCoordinateN(1).x; // longitude p2
        coordinates[1][1] = line.getCoordinateN(1).y; // latitude p2

        return coordinates;
    }
}
