package com.riwi.m2w1.service;

import com.riwi.m2w1.exceptions.ResourceNotFoundException;
import com.riwi.m2w1.models.Event;
import com.riwi.m2w1.repositories.EventRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Servicio para la lógica de negocio de Events.
 *
 * Ahora usa JpaRepository → la persistencia es real (H2 en archivo).
 * Los métodos devuelven Page<Event> para soportar paginación y ordenamiento.
 */
@Service
public class EventService {

    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    // ── READ ALL (paginado + ordenable) ──────────────────────────────────────
    /**
     * Devuelve una página de eventos.
     * El cliente puede pasar ?page=0&size=10&sort=name,asc
     */
    public Page<Event> findAll(Pageable pageable) {
        return eventRepository.findAll(pageable);
    }

    // ── READ (búsqueda por nombre parcial) ───────────────────────────────────
    public Page<Event> findByName(String name, Pageable pageable) {
        return eventRepository.findByNameContainingIgnoreCase(name, pageable);
    }

    // ── READ ONE ─────────────────────────────────────────────────────────────
    /**
     * Busca un evento por ID.
     * Lanza ResourceNotFoundException (→ 404) si no existe.
     */
    public Event getById(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Evento con ID " + id + " no encontrado"));
    }

    // ── CREATE ───────────────────────────────────────────────────────────────
    /**
     * Guarda un nuevo evento en la BD.
     * El ID es generado automáticamente por Hibernate (@GeneratedValue).
     */
    public Event createEvent(Event event) {
        // El ID lo asigna la BD; asegurar que no venga del cliente
        event.setId(null);
        return eventRepository.save(event);
    }

    // ── UPDATE ───────────────────────────────────────────────────────────────
    /**
     * Actualiza un evento existente.
     * Valida que el recurso exista antes de actualizar (→ 404 si no existe).
     */
    public Event updateEvent(Long id, Event updatedEvent) {
        // Verifica existencia (lanza 404 si no existe)
        Event existing = getById(id);

        // Actualizar los campos (conservar el ID original)
        existing.setName(updatedEvent.getName());
        existing.setDate(updatedEvent.getDate());
        existing.setDescription(updatedEvent.getDescription());

        return eventRepository.save(existing);
    }

    // ── DELETE ───────────────────────────────────────────────────────────────
    /**
     * Elimina físicamente un evento por ID.
     * Lanza 404 si no existe.
     * El controller devuelve 204 No Content al borrar exitosamente.
     */
    public void deleteById(Long id) {
        // Verifica que existe antes de borrar
        if (!eventRepository.existsById(id)) {
            throw new ResourceNotFoundException(
                    "Evento con ID " + id + " no encontrado");
        }
        eventRepository.deleteById(id);
    }
}
