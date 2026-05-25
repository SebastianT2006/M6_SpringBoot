package com.riwi.m2w1;

import com.riwi.m2w1.models.Event;
import com.riwi.m2w1.repositories.EventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Pruebas de integración para EventRepository.
 *
 * @DataJpaTest:
 *   - Levanta solo el contexto JPA (sin servidor HTTP).
 *   - Usa H2 en memoria para tests (aislado del H2 de desarrollo).
 *   - Cada test se ejecuta en una transacción que se revierte al terminar.
 */
@DataJpaTest
@DisplayName("EventRepository - Pruebas de integración JPA")
class EventRepositoryTest {

    @Autowired
    private EventRepository eventRepository;

    private Event event1;
    private Event event2;
    private Event event3;

    /** Inserta datos frescos antes de cada test */
    @BeforeEach
    void setUp() {
        event1 = eventRepository.save(new Event(null, "Concierto de Rock",   "2024-07-15", "Música en vivo"));
        event2 = eventRepository.save(new Event(null, "Feria de Comida",     "2024-08-20", "Gastronomía"));
        event3 = eventRepository.save(new Event(null, "Concierto de Jazz",   "2024-09-01", "Jazz y blues"));
    }

    // ── TASK 1: guardar y recuperar ──────────────────────────────────────────

    @Test
    @DisplayName("save() debe persistir el evento y generar un ID")
    void shouldSaveAndGenerateId() {
        assertThat(event1.getId()).isNotNull();
        assertThat(event1.getId()).isGreaterThan(0);
    }

    @Test
    @DisplayName("findById() debe recuperar un evento existente")
    void shouldFindById() {
        Optional<Event> found = eventRepository.findById(event1.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Concierto de Rock");
    }

    @Test
    @DisplayName("findAll() debe devolver todos los eventos insertados")
    void shouldFindAllEvents() {
        assertThat(eventRepository.findAll()).hasSize(3);
    }

    // ── TASK 2: CRUD completo ────────────────────────────────────────────────

    @Test
    @DisplayName("save() sobre entidad existente debe actualizar el registro")
    void shouldUpdateEvent() {
        event1.setName("Concierto de Rock (Actualizado)");
        Event updated = eventRepository.save(event1);

        assertThat(updated.getName()).isEqualTo("Concierto de Rock (Actualizado)");
        // Mismo ID, solo un registro en BD
        assertThat(eventRepository.count()).isEqualTo(3);
    }

    @Test
    @DisplayName("deleteById() debe eliminar físicamente el evento")
    void shouldDeleteEvent() {
        Long idToDelete = event1.getId();
        eventRepository.deleteById(idToDelete);

        assertThat(eventRepository.findById(idToDelete)).isEmpty();
        assertThat(eventRepository.count()).isEqualTo(2);
    }

    @Test
    @DisplayName("findById() con ID inexistente debe devolver Optional vacío")
    void shouldReturnEmptyForNonExistentId() {
        Optional<Event> result = eventRepository.findById(9999L);
        assertThat(result).isEmpty();
    }

    // ── TASK 3: Paginación y Derived Query ────────────────────────────────────

    @Test
    @DisplayName("findAll(Pageable) debe respetar el tamaño de página")
    void shouldPageResults() {
        // Escenario 3: página 0 con tamaño 2 → devuelve exactamente 2 elementos
        Page<Event> page = eventRepository.findAll(PageRequest.of(0, 2));

        assertThat(page.getContent()).hasSize(2);
        assertThat(page.getTotalElements()).isEqualTo(3);
        assertThat(page.getTotalPages()).isEqualTo(2);
    }

    @Test
    @DisplayName("findAll(Pageable) página 1 debe devolver el elemento restante")
    void shouldReturnSecondPage() {
        Page<Event> page = eventRepository.findAll(PageRequest.of(1, 2));

        assertThat(page.getContent()).hasSize(1);
        assertThat(page.isLast()).isTrue();
    }

    @Test
    @DisplayName("findByNameContainingIgnoreCase() debe buscar parcialmente y sin importar mayúsculas")
    void shouldFindByNameContaining() {
        Page<Event> result = eventRepository.findByNameContainingIgnoreCase(
                "concierto", PageRequest.of(0, 10, Sort.by("name")));

        // event1 ("Concierto de Rock") y event3 ("Concierto de Jazz") deben coincidir
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent())
                .extracting(Event::getName)
                .containsExactlyInAnyOrder("Concierto de Rock", "Concierto de Jazz");
    }

    @Test
    @DisplayName("findByNameContainingIgnoreCase() sin coincidencias debe devolver página vacía")
    void shouldReturnEmptyPageWhenNoMatch() {
        Page<Event> result = eventRepository.findByNameContainingIgnoreCase(
                "zzznomatch", PageRequest.of(0, 10));

        assertThat(result.getContent()).isEmpty();
        assertThat(result.getTotalElements()).isEqualTo(0);
    }

    @Test
    @DisplayName("findAll con Sort debe ordenar por nombre ascendente")
    void shouldSortByName() {
        Page<Event> sorted = eventRepository.findAll(
                PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "name")));

        // "Concierto de Jazz" < "Concierto de Rock" < "Feria de Comida" (orden alfabético)
        assertThat(sorted.getContent().get(0).getName()).isEqualTo("Concierto de Jazz");
        assertThat(sorted.getContent().get(2).getName()).isEqualTo("Feria de Comida");
    }
}
