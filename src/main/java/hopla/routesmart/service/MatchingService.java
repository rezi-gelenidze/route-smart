package hopla.routesmart.service;

import hopla.routesmart.dto.ParcelDTO;
import hopla.routesmart.dto.TripDTO;
import hopla.routesmart.entity.Parcel;
import hopla.routesmart.entity.Trip;
import hopla.routesmart.repository.ParcelRepository;
import hopla.routesmart.repository.TripRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MatchingService {

    @Autowired
    private ParcelRepository parcelRepository;

    @Autowired
    private TripRepository tripRepository;

    /**
     * Matches trips with parcels based on precomputed paths.
     *
     * @param tripId Trip ID to match
     * @return List of matched parcels as DTOs
     */
    public List<ParcelDTO> matchTripWithParcels(Long tripId) {
        Trip trip = tripRepository.findById(tripId).orElse(null);

        if (trip != null) {
            return parcelRepository.findMatchingParcels(
                    trip.getFromLocation().getId(), trip.getToLocation().getId()
            );
        }

        return List.of();
    }

    /**
     * Matches parcels with trips based on precomputed paths.
     *
     * @param parcelId Parcel ID to match
     * @return List of matched trips as DTOs
     */
    public List<TripDTO> matchParcelWithTrips(Long parcelId) {
        Parcel parcel = parcelRepository.findById(parcelId).orElse(null);

        if (parcel != null) {
            return tripRepository.findMatchingTrips(
                    parcel.getFromLocation().getId(), parcel.getToLocation().getId()
            );
        }

        return List.of();
    }
}
