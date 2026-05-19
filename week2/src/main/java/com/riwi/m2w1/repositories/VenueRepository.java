package com.riwi.m2w1.repositories;

import com.riwi.m2w1.models.Venue;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio JPA para Venue.
 * Derived Query: busca venues por nombre (parcial, case-insensitive).
 */
@Repository
public interface VenueRepository extends JpaRepository<Venue, Long> {

    Page<Venue> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
