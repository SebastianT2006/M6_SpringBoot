package com.riwi.m2w1.service;
import java.util.List;

import org.springframework.stereotype.Service;

import com.riwi.m2w1.models.Venue;
import com.riwi.m2w1.repositories.VenueRepository;

@Service
public class VenueService {
    private final VenueRepository venueRepository;

    public VenueService(VenueRepository venueRepository) {
        this.venueRepository = venueRepository;
    }

    public List<Venue> findAll() {
        return venueRepository.getAllVenues();
    }

    public Venue getByID(Integer id) {
        return venueRepository.getByID(id);
    }

    public void deleteById(Integer id) {
        if (!venueRepository.existsByID(id)) {
            throw new RuntimeException("El lugar no existe");
        }
        venueRepository.deleteByID(id);
    }


    public void updateVenue(Integer id, Venue updatedVenue) {

    Venue existing = venueRepository.getByID(id);

    if (existing == null) {
        throw new RuntimeException("El lugar no existe");
    }

    venueRepository.update(id, updatedVenue);
}

public void createVenue(Venue venue) {

    // Validaciones de campos
    if (venue.getName() == null || venue.getName().isEmpty()) {
        throw new RuntimeException("Nombre requerido");
    }

    // generar ID automático
    int newId = venueRepository.getAllVenues().size() + 1;
    venue.setId(newId);

    venueRepository.createVenue(venue);
}
}