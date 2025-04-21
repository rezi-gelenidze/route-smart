package hopla.routesmart.dto;

import hopla.routesmart.entity.Parcel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParcelDTO {
    private Long id;
    private Long fromId;
    private Long toId;

    public static ParcelDTO from(Parcel parcel) {
        return new ParcelDTO(parcel.getId(), parcel.getFrom().getId(), parcel.getTo().getId());
    }
}
