package hopla.routesmart.controller;

import hopla.routesmart.dto.ParcelDTO;
import hopla.routesmart.dto.TripDTO;
import hopla.routesmart.service.MatchingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/match")
public class MatchingController {

    @Autowired
    private MatchingService matchingService;

    @GetMapping("/trip/{tripId}")
    public List<ParcelDTO> matchTripWithParcels(@PathVariable Long tripId) {
        return matchingService.matchTripWithParcels(tripId);
    }

    @GetMapping("/parcel/{parcelId}")
    public List<TripDTO> matchParcelWithTrips(@PathVariable Long parcelId) {
        return matchingService.matchParcelWithTrips(parcelId);
    }
}
