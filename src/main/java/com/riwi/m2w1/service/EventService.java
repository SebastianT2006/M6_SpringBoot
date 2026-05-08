package com.riwi.m2w1.service;
import java.util.List;

import org.springframework.stereotype.Service;

import com.riwi.m2w1.models.Event;
import com.riwi.m2w1.repositories.EventRepository;


@Service
public class EventService {
    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public String ValidationDate(String date) {
        if (date == null || date.trim().isEmpty()) {
            return "La fecha del evento no puede estar vacía.";
        }
        // Validar formato de fecha (YYYY-MM-DD)
        if (!date.matches("\\d{4}-\\d{2}-\\d{2}")) {
            return "La fecha del evento debe tener el formato YYYY-MM-DD.";
        }
        return null; // No hay errores
    }

    public String ValidationDescription(String description) {
        if (description == null || description.trim().isEmpty()) {
            return "La descripción del evento no puede estar vacía.";
        }
        return null; // No hay errores
    }

    public String EventExists(Integer id) {
        if (id == null) {
            return "El ID del evento no puede ser nulo.";
        }
        boolean exists = eventRepository.getAllEvents().stream()
                .anyMatch(event -> event.getId().equals(id));
        if (!exists) {
            return "El evento con ID " + id + " no existe.";
        }
        return null; // No hay errores
    }

    public List<Event> findAll() {
        return eventRepository.getAllEvents();
    }

public Event getByID(Integer id) {
    return eventRepository.getByID(id);
}

public void deleteById(Integer id) {
    if (!eventRepository.existsByID(id)) {
        throw new RuntimeException("El evento no existe");
    }
    eventRepository.deleteByID(id);
}
public void updateEvent(Integer id, Event updatedEvent) {

    Event existing = eventRepository.getByID(id);

    if (existing == null) {
        throw new RuntimeException("El evento no existe");
    }

    eventRepository.update(id, updatedEvent);
}
public void createEvent(Event event) {

    // Validaciones de campos
    if (event.getName() == null || event.getName().isEmpty()) {
        throw new RuntimeException("Nombre requerido");
        
    }

    // generar ID automático
    int newId = eventRepository.getAllEvents().size() + 1;
    event.setId(newId);

    eventRepository.createEvent(event);
}

}
