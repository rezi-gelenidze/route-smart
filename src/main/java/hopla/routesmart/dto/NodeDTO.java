package hopla.routesmart.dto;

import hopla.routesmart.entity.Node;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NodeDTO {
    private Long id;
    private String name;
    private Double[] location;

    public static NodeDTO from(Node node) {
        return new NodeDTO(
                node.getId(),
                node.getName(),
                new Double[]{
                        node.getGeom().getCoordinate().getX(),
                        node.getGeom().getCoordinate().getY()
                }
        );
    }
}
