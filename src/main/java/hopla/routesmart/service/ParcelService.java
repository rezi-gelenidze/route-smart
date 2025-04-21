package hopla.routesmart.service;

import hopla.routesmart.dto.ParcelCreateDTO;
import hopla.routesmart.dto.ParcelDTO;
import hopla.routesmart.dto.TripDTO;
import hopla.routesmart.entity.Node;
import hopla.routesmart.entity.Parcel;
import hopla.routesmart.exception.NotFoundException;
import hopla.routesmart.repository.NodeRepository;
import hopla.routesmart.repository.ParcelRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class ParcelService {
    private final TripService tripService;

    private final ParcelRepository parcelRepository;
    private final NodeRepository nodeRepository;


    public List<ParcelDTO> getMatchingParcels(Long from, Long to) {
        return parcelRepository.findMatchingParcels(from, to);
    }


    public ParcelDTO createParcel(ParcelCreateDTO parcelCreateDTO) {
        Node fromNode = nodeRepository.findById(parcelCreateDTO.getFrom())
                .orElseThrow(() -> new NotFoundException("node_not_found", "From node not found"));

        Node toNode = nodeRepository.findById(parcelCreateDTO.getTo())
                .orElseThrow(() -> new NotFoundException("node_not_found", "To node not found"));

        Parcel parcel = new Parcel();
        parcel.setFrom(fromNode);
        parcel.setTo(toNode);

        Parcel created = parcelRepository.save(parcel);

        return ParcelDTO.from(created);
    }


    public List<TripDTO> matchParcelWithTrips(Long id) {
        Parcel parcel = parcelRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("parcel_not_found", "Parcel not found"));

        Long from = parcel.getFrom().getId();
        Long to = parcel.getTo().getId();

        return tripService.getMatchingTrips(from, to);
    }


    public void deleteParcel(Long id) {
        if (!parcelRepository.existsById(id))
            throw new NotFoundException("parcel_not_found", "Parcel not found");

        parcelRepository.deleteById(id);
    }
}
