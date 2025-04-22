package hopla.routesmart.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GraphDTO {
    List<NodeDTO> nodes;
    List<EdgeDTO> edges;

    public static GraphDTO from(List<NodeDTO> nodes, List<EdgeDTO> edges) {
        return new GraphDTO(nodes, edges);
    }
}
