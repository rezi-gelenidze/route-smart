package hopla.routesmart.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TripCreateDTO {
    private Long from;
    private Long to;
    private LocalDateTime startTime;
    private Integer durationMinutes;
}
