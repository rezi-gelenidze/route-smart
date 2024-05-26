package hopla.routesmart.controller;

import hopla.routesmart.entity.Parcel;
import hopla.routesmart.entity.Trip;
import hopla.routesmart.service.MatchingService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;
import java.util.Set;

@RestController
@RequestMapping("/match")
public class MatchingController {

    @Autowired
    private MatchingService matchingService;

    @GetMapping("/trip/{tripId}")
    public Set<Long> matchTripWithParcels(@PathVariable Long tripId) {
        return matchingService.matchTripWithParcels(tripId);

    }

    @GetMapping("/parcel/{parcelId}")
    public Set<Long> matchParcelWithTrips(@PathVariable Long parcelId) {
        Set<Trip> matchingTrips  = matchingService.matchParcelWithTrips(parcelId);

        return matchingTrips.stream()
                .map(Trip::getId)
                .collect(Collectors.toSet());
    }
}
