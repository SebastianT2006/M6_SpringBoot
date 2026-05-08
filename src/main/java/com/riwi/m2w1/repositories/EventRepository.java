package com.riwi.m2w1.repositories;

import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;
import com.riwi.m2w1.models.Event;

@Repository
public class EventRepository {
    private List<Event> events = new ArrayList<>();

    public List<Event> getAllEvents() {
        return events;
    }
public Event getByID(Integer id) {
    return events.stream()
            .filter(e -> e.getId().equals(id))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Evento no encontrado"));
}

    public void deleteByID(Integer id) {
        events.removeIf(event -> event.getId().equals(id));
    }
    public boolean existsByID(Integer id) {
        return events.stream()
                .anyMatch(event -> event.getId().equals(id));
    }

public void update(Integer id, Event updatedEvent) {
    for (int i = 0; i < events.size(); i++) {
        if (events.get(i).getId().equals(id)) {
            updatedEvent.setId(id);
            events.set(i, updatedEvent);
            return;
        }
    }
}
public void createEvent(Event event) {
    events.add(event);
}
}
