package hopla.routesmart.dto;

import hopla.routesmart.entity.Trip;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TripDTO {
    private Long id;
    private Long fromId;
    private Long toId;
    private LocalDateTime startTime;
    private Long durationMinutes;

    public static TripDTO from(Trip trip) {
        return new TripDTO(
                trip.getId(),
                trip.getFrom().getId(),
                trip.getTo().getId(),
                trip.getStartTime(),
                trip.getDuration().toMinutes()
        );
    }
}
