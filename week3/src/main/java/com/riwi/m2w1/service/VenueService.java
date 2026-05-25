package com.riwi.m2w1.service;

import com.riwi.m2w1.exceptions.ResourceNotFoundException;
import com.riwi.m2w1.models.Venue;
import com.riwi.m2w1.repositories.VenueRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Servicio para la lógica de negocio de Venues.
 */
@Service
public class VenueService {

    private final VenueRepository venueRepository;

    public VenueService(VenueRepository venueRepository) {
        this.venueRepository = venueRepository;
    }

    // ── READ ALL (paginado) ───────────────────────────────────────────────────
    public Page<Venue> findAll(Pageable pageable) {
        return venueRepository.findAll(pageable);
    }

    // ── READ (búsqueda por nombre parcial) ───────────────────────────────────
    public Page<Venue> findByName(String name, Pageable pageable) {
        return venueRepository.findByNameContainingIgnoreCase(name, pageable);
    }

    // ── READ ONE ─────────────────────────────────────────────────────────────
    public Venue getById(Long id) {
        return venueRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Lugar con ID " + id + " no encontrado"));
    }

    // ── CREATE ───────────────────────────────────────────────────────────────
    public Venue createVenue(Venue venue) {
        venue.setId(null);
        return venueRepository.save(venue);
    }

    // ── UPDATE ───────────────────────────────────────────────────────────────
    public Venue updateVenue(Long id, Venue updatedVenue) {
        Venue existing = getById(id); // lanza 404 si no existe

        existing.setName(updatedVenue.getName());
        existing.setAddress(updatedVenue.getAddress());
        existing.setCapacity(updatedVenue.getCapacity());

        return venueRepository.save(existing);
    }

    // ── DELETE ───────────────────────────────────────────────────────────────
    public void deleteById(Long id) {
        if (!venueRepository.existsById(id)) {
            throw new ResourceNotFoundException(
                    "Lugar con ID " + id + " no encontrado");
        }
        venueRepository.deleteById(id);
    }
}
