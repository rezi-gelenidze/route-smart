package hopla.routesmart.service;

import hopla.routesmart.entity.Parcel;
import hopla.routesmart.entity.PrecomputedPath;
import hopla.routesmart.entity.Trip;
import hopla.routesmart.repository.PrecomputedPathRepository;
import hopla.routesmart.repository.TripRepository;
import hopla.routesmart.repository.ParcelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class MatchingService {
    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private ParcelRepository parcelRepository;

    @Autowired
    private PrecomputedPathRepository precomputedPathRepository;

    /**
     * Matches trips with parcels based on precomputed paths.
     *
     * @param tripId Trip ID to match
     * @return Set of parcel IDs that match the trip
     */
    public Set<Long> matchTripWithParcels(Long tripId) {
        Set<Long> matchingParcelIds = new HashSet<>();

        Trip trip = tripRepository.findById(tripId).orElse(null);
        if (trip == null) {
            return matchingParcelIds;
        }

        List<PrecomputedPath> paths = precomputedPathRepository.findByStartNodeIdAndEndNodeId(
                trip.getFromLocation().getId(),
                trip.getToLocation().getId()
        );

        for (PrecomputedPath path : paths) {
            String pathNodeIds = path.getPath();

            List<Parcel> parcels = parcelRepository.findAll();
            for (Parcel parcel : parcels) {
                String parcelPathRegex = ".*" + parcel.getFromLocation().getId() + ".*" + parcel.getToLocation().getId() + ".*";
                if (pathNodeIds.matches(parcelPathRegex)) {
                    matchingParcelIds.add(parcel.getId());
                }
            }
        }

        return matchingParcelIds;
    }

    /**
     * Matches parcels with trips based on precomputed paths.
     *
     * @param parcelId Parcel ID to match
     * @return Set of trip IDs that match the parcel
     */
    public Set<Long> matchParcelWithTrips(Long parcelId) {
        Set<Long> matchingTripIds = new HashSet<>();

        Parcel parcel = parcelRepository.findById(parcelId).orElse(null);
        if (parcel == null) {
            return matchingTripIds;
        }

        List<PrecomputedPath> paths = precomputedPathRepository.findPathsBySubpath(
                "%" + parcel.getFromLocation().getId() + "%",
                "%" + parcel.getToLocation().getId() + "%"
        );

        for (PrecomputedPath path : paths) {
            List<Trip> trips = tripRepository.findAll();
            for (Trip trip : trips) {
                if (path.getStartNode().getId().equals(trip.getFromLocation().getId()) &&
                        path.getEndNode().getId().equals(trip.getToLocation().getId())) {
                    matchingTripIds.add(trip.getId());
                }
            }
        }

        return matchingTripIds;
    }
}
