package hopla.routesmart.controller;

import hopla.routesmart.entity.Node;
import hopla.routesmart.entity.Parcel;
import hopla.routesmart.entity.Trip;
import hopla.routesmart.repository.NodeRepository;
import hopla.routesmart.repository.ParcelRepository;
import hopla.routesmart.repository.TripRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


@RestController
@RequestMapping("/match")
public class MatchingController {

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private ParcelRepository parcelRepository;

    @Autowired
    private NodeRepository nodeRepository;

    @GetMapping("/trip/{tripId}")
    public Object matchTripWithParcels(
            @PathVariable Long tripId,
            @RequestParam(required = false, defaultValue = "false") Boolean verbose
    ) {
        Trip trip = tripRepository.findById(tripId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Trip not found with ID " + tripId)
        );

        Long fromNodeId = trip.getFromLocation().getId();
        Long toNodeId = trip.getToLocation().getId();

        return findMatchingParcelsWithVerboseFlag(fromNodeId, toNodeId, verbose);
    }

    @GetMapping("/trip/")
    public Object matchTripWithParcels(
            @RequestParam(required = false) Long fromId,
            @RequestParam(required = false) String fromDisplay,
            @RequestParam(required = false) Long toId,
            @RequestParam(required = false) String toDisplay,
            @RequestParam(required = false, defaultValue = "false") Boolean verbose
    ) {
        Long fromNodeId = getNodeId(fromId, fromDisplay);
        Long toNodeId = getNodeId(toId, toDisplay);

        return findMatchingParcelsWithVerboseFlag(fromNodeId, toNodeId, verbose);
    }

    @GetMapping("/parcel/{parcelId}")
    public Object matchParcelWithTrips(
            @PathVariable Long parcelId,
            @RequestParam(required = false, defaultValue = "false") Boolean verbose
    ) {
        Parcel parcel = parcelRepository.findById(parcelId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Parcel not found with ID " + parcelId)
        );

        Long fromNodeId = parcel.getFromLocation().getId();
        Long toNodeId = parcel.getToLocation().getId();

        return findMatchingTripsWithVerboseFlag(fromNodeId, toNodeId, verbose);
    }

    @GetMapping("/parcel/")
    public Object matchParcelWithTrips(
            @RequestParam(required = false) Long fromId,
            @RequestParam(required = false) String fromDisplay,
            @RequestParam(required = false) Long toId,
            @RequestParam(required = false) String toDisplay,
            @RequestParam(required = false, defaultValue = "false") Boolean verbose
    ) {
        Long fromNodeId = getNodeId(fromId, fromDisplay);
        Long toNodeId = getNodeId(toId, toDisplay);

        return findMatchingTripsWithVerboseFlag(fromNodeId, toNodeId, verbose);
    }

    // Helper method to get node ID from either ID or display name
    private Long getNodeId(Long id, String display) {
        if (id == null && display == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing 'id' or 'display'");
        }
        if (id != null && display != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Provide either 'id' or 'display', not both");
        }

        if (id != null) {
            return id;
        } else {
            Node node = nodeRepository.findByName(display);
            if (node == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Node not found with name " + display);
            }
            return node.getId();
        }
    }

    // Conditional return type based on verbose flag for matching trip with parcels
    private Object findMatchingParcelsWithVerboseFlag(Long fromNodeId, Long toNodeId, Boolean verbose) {
        if (verbose) {
            return parcelRepository.findMatchingParcelsVerbose(fromNodeId, toNodeId);
        }
        return parcelRepository.findMatchingParcelsSimple(fromNodeId, toNodeId);
    }

    // Conditional return type based on verbose flag for matching parcel with trips
    private Object findMatchingTripsWithVerboseFlag(Long fromNodeId, Long toNodeId, Boolean verbose) {
        if (verbose) {
            return tripRepository.findMatchingTripsVerbose(fromNodeId, toNodeId);
        }
        return tripRepository.findMatchingTripsSimple(fromNodeId, toNodeId);
    }
}
