package hopla.routesmart.controller;

import hopla.routesmart.dto.ParcelDTO;
import hopla.routesmart.dto.TripDTO;
import hopla.routesmart.service.ParcelService;
import hopla.routesmart.dto.ParcelCreateDTO;


import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;


@RestController
@RequestMapping("/parcels")
@RequiredArgsConstructor
public class ParcelController {
    private final ParcelService parcelService;

    @GetMapping
    @Operation(summary = "Find parcels matching the from/to node IDs")
    public ResponseEntity<List<ParcelDTO>> getMatchingParcels(@RequestParam Long from, @RequestParam Long to) {
        return ResponseEntity.ok(parcelService.getMatchingParcels(from, to));
    }

    @PostMapping
    @Operation(summary = "Create a new parcel")
    public ResponseEntity<ParcelDTO> createParcel(@RequestBody @Valid ParcelCreateDTO parcelCreateDTO) {
        ParcelDTO created = parcelService.createParcel(parcelCreateDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Find trips matching the given parcel's path")
    public ResponseEntity<List<TripDTO>> matchParcelWithTrips(@PathVariable Long id) {
        return ResponseEntity.ok(parcelService.matchParcelWithTrips(id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a parcel by ID")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteParcel(@PathVariable Long id) {
        parcelService.deleteParcel(id);
    }
}
