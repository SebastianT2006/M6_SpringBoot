package com.riwi.m2w1.repositories;

import com.riwi.m2w1.models.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio JPA para Event.
 *
 * Al heredar de JpaRepository<Event, Long> obtenemos gratis:
 *   - save(), findById(), findAll(), deleteById(), count(), existsById()…
 *
 * Derived Query: Spring genera el SQL a partir del nombre del método.
 *   findByNameContainingIgnoreCase → WHERE LOWER(name) LIKE %:name%
 */
@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    /**
     * Busca eventos cuyo nombre contenga el texto dado (sin distinguir mayúsculas).
     * Soporta paginación directamente via Pageable.
     */
    Page<Event> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
