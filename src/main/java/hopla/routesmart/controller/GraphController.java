package hopla.routesmart.controller;

import hopla.routesmart.dto.GraphDTO;
import hopla.routesmart.service.GraphService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/graph")
@RequiredArgsConstructor
public class GraphController
{
    private final GraphService graphService;

    @GetMapping
    public ResponseEntity<GraphDTO> getGraph()
    {
        return ResponseEntity.ok(graphService.getGraph());
    }
}
