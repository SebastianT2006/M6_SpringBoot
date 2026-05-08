package com.riwi.m2w1.repositories;

import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;
import com.riwi.m2w1.models.Venue;

@Repository
public class VenueRepository {
    private List<Venue> venues = new ArrayList<>();

    public List<Venue> getAllVenues() {
        return venues;
    }

    public Venue getByID(Integer id) {
        return venues.stream()
                .filter(v -> v.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Lugar no encontrado"));
    }
     public void deleteByID(Integer id) {
        venues.removeIf(venue -> venue.getId().equals(id));
    }
    public boolean existsByID(Integer id) {
        return venues.stream()
                .anyMatch(venue -> venue.getId().equals(id));
    }

    public void update(Integer id, Venue updatedVenue) {
    for (int i = 0; i < venues.size(); i++) {
        if (venues.get(i).getId().equals(id)) {
            updatedVenue.setId(id);
            venues.set(i, updatedVenue);
            return;
        }
    }
}
public void createVenue(Venue venue) {
    venues.add(venue);
}
}