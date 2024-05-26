package hopla.routesmart.service;

import hopla.routesmart.entity.Parcel;
import hopla.routesmart.entity.Trip;
import hopla.routesmart.repository.ParcelRepository;
import hopla.routesmart.repository.PrecomputedPathRepository;
import hopla.routesmart.repository.TripRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.HashSet;

@Service
public class MatchingService {

    @Autowired
    private PrecomputedPathRepository precomputedPathRepository;

    @Autowired
    private ParcelRepository parcelRepository;

    @Autowired
    private TripRepository tripRepository;

    /**
     * Matches trips with parcels based on precomputed paths.
     *
     * @param tripId Trip ID to match
     * @return Set of matched parcels
     */
    public Set<Long> matchTripWithParcels(Long tripId) {
        Set<Long> matchingParcels = new HashSet<>();
        Trip trip = tripRepository.findById(tripId).orElse(null);

        if (trip != null) {
            List<Long> parcels = precomputedPathRepository.findMatchingParcels(
                    trip.getFromLocation().getId(),
                    trip.getToLocation().getId()
            );

            matchingParcels.addAll(parcels);
        }

        return matchingParcels;
    }

    /**
     * Matches parcels with trips based on precomputed paths.
     *
     * @param parcelId Parcel ID to match
     * @return Set of matched trips
     */
    public Set<Trip> matchParcelWithTrips(Long parcelId) {
        Set<Trip> matchingTrips = new HashSet<>();
        Parcel parcel = parcelRepository.findById(parcelId).orElse(null);

        if (parcel != null) {
            List<Trip> trips = precomputedPathRepository.findMatchingTrips(
                    parcel.getFromLocation().getId(),
                    parcel.getToLocation().getId()
            );
            matchingTrips.addAll(trips);
        }

        return matchingTrips;
    }
}
