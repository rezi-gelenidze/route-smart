package hopla.routesmart.service;

import hopla.routesmart.dto.TripCreateDTO;
import hopla.routesmart.dto.TripDTO;
import hopla.routesmart.dto.ParcelDTO;
import hopla.routesmart.entity.Node;
import hopla.routesmart.entity.Trip;
import hopla.routesmart.exception.NotFoundException;
import hopla.routesmart.repository.NodeRepository;
import hopla.routesmart.repository.ParcelRepository;
import hopla.routesmart.repository.TripRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TripService {
    private final TripRepository tripRepository;
    private final NodeRepository nodeRepository;
    private final ParcelRepository parcelRepository;

    public List<TripDTO> getMatchingTrips(Long from, Long to) {
        return tripRepository.findMatchingTrips(from, to);
    }

    public TripDTO createTrip(TripCreateDTO tripCreateDTO) {
        Node fromNode = nodeRepository.findById(tripCreateDTO.getFrom())
                .orElseThrow(() -> new NotFoundException("node_not_found", "From node not found"));

        Node toNode = nodeRepository.findById(tripCreateDTO.getTo())
                .orElseThrow(() -> new NotFoundException("node_not_found", "To node not found"));

        // Instantiate a new Trip object
        Trip trip = new Trip();
        trip.setFrom(fromNode);
        trip.setTo(toNode);
        trip.setDuration(Duration.ofMinutes(tripCreateDTO.getDurationMinutes()));
        trip.setStartTime(tripCreateDTO.getStartTime());

        Trip saved = tripRepository.save(trip);
        return TripDTO.from(saved);
    }

    public List<ParcelDTO> matchTripWithParcels(Long tripId) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new NotFoundException("trip_not_found", "Trip not found"));

        Long from = trip.getFrom().getId();
        Long to = trip.getTo().getId();

        return parcelRepository.findMatchingParcels(from, to);
    }

    public void deleteTrip(Long id) {
        if (!tripRepository.existsById(id))
            throw new NotFoundException("trip_not_found", "Trip not found");

        tripRepository.deleteById(id);
    }
}
