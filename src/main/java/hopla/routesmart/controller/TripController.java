package hopla.routesmart.controller;

import hopla.routesmart.dto.*;
import hopla.routesmart.service.TripService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/trips")
@RequiredArgsConstructor
public class TripController {
    private final TripService tripService;

    @GetMapping
    @Operation(summary = "Find trips matching the from/to node IDs")
    public ResponseEntity<List<TripDTO>> getMatchingTrips(@RequestParam Long from, @RequestParam Long to) {
        return ResponseEntity.ok(tripService.getMatchingTrips(from, to));
    }

    @PostMapping
    @Operation(summary = "Create a new trip")
    public ResponseEntity<TripDTO> createTrip(@RequestBody @Valid TripCreateDTO tripCreateDTO) {
        TripDTO created = tripService.createTrip(tripCreateDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Find parcels matching the given trip's path")
    public ResponseEntity<List<ParcelDTO>> matchTripWithParcels(@PathVariable Long id) {
        return ResponseEntity.ok(tripService.matchTripWithParcels(id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a trip by ID")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTrip(@PathVariable Long id) {
        tripService.deleteTrip(id);
    }
}
