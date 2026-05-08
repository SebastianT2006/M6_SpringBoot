package com.riwi.m2w1;

import com.riwi.m2w1.models.Event;
import com.riwi.m2w1.repositories.EventRepository;
import com.riwi.m2w1.service.EventService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {

    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private EventService eventService;

    // ─────────────────────────────────────────────
    // findAll
    // ─────────────────────────────────────────────

    @Test
    void findAll_retornaListaDeEventos() {
        List<Event> eventos = Arrays.asList(
            new Event(1, "Concierto de Rock", "2026-05-10", "Evento musical"),
            new Event(2, "Feria del Libro", "2026-06-01", "Evento cultural")
        );
        when(eventRepository.getAllEvents()).thenReturn(eventos);

        List<Event> resultado = eventService.findAll();

        assertEquals(2, resultado.size());
        assertEquals("Concierto de Rock", resultado.get(0).getName());
        verify(eventRepository, times(1)).getAllEvents();
    }

    @Test
    void findAll_listaVacia_retornaListaVacia() {
        when(eventRepository.getAllEvents()).thenReturn(Collections.emptyList());

        List<Event> resultado = eventService.findAll();

        assertTrue(resultado.isEmpty());
        verify(eventRepository, times(1)).getAllEvents();
    }

    // ─────────────────────────────────────────────
    // getByID
    // ─────────────────────────────────────────────

    @Test
    void getByID_idExistente_retornaEvento() {
        Event evento = new Event(1, "Concierto", "2026-05-10", "Música en vivo");
        when(eventRepository.getByID(1)).thenReturn(evento);

        Event resultado = eventService.getByID(1);

        assertNotNull(resultado);
        assertEquals(1, resultado.getId());
        assertEquals("Concierto", resultado.getName());
        verify(eventRepository, times(1)).getByID(1);
    }

    @Test
    void getByID_idInexistente_lanzaExcepcion() {
        when(eventRepository.getByID(999)).thenThrow(new RuntimeException("Evento no encontrado"));

        RuntimeException ex = assertThrows(RuntimeException.class,
            () -> eventService.getByID(999));

        assertEquals("Evento no encontrado", ex.getMessage());
        verify(eventRepository, times(1)).getByID(999);
    }

    // ─────────────────────────────────────────────
    // createEvent
    // ─────────────────────────────────────────────

    @Test
    void createEvent_datosValidos_asignaIdYGuarda() {
        Event evento = new Event(null, "Festival", "2026-07-01", "Gran festival");
        when(eventRepository.getAllEvents()).thenReturn(Collections.emptyList());

        eventService.createEvent(evento);

        assertEquals(1, evento.getId()); // lista vacía → newId = 1
        verify(eventRepository, times(1)).createEvent(evento);
    }

    @Test
    void createEvent_idIncrementalSegunListaExistente() {
        List<Event> existentes = Arrays.asList(
            new Event(1, "E1", "2026-01-01", "Desc1"),
            new Event(2, "E2", "2026-02-01", "Desc2")
        );
        when(eventRepository.getAllEvents()).thenReturn(existentes);

        Event nuevo = new Event(null, "E3", "2026-03-01", "Desc3");
        eventService.createEvent(nuevo);

        assertEquals(3, nuevo.getId());
        verify(eventRepository, times(1)).createEvent(nuevo);
    }

    @Test
    void createEvent_nombreNulo_lanzaExcepcion() {
        Event evento = new Event(null, null, "2026-05-10", "Descripción");

        RuntimeException ex = assertThrows(RuntimeException.class,
            () -> eventService.createEvent(evento));

        assertEquals("Nombre requerido", ex.getMessage());
        verify(eventRepository, never()).createEvent(any());
    }

    @Test
    void createEvent_nombreVacio_lanzaExcepcion() {
        Event evento = new Event(null, "", "2026-05-10", "Descripción");

        RuntimeException ex = assertThrows(RuntimeException.class,
            () -> eventService.createEvent(evento));

        assertEquals("Nombre requerido", ex.getMessage());
        verify(eventRepository, never()).createEvent(any());
    }

    // ─────────────────────────────────────────────
    // deleteById
    // ─────────────────────────────────────────────

    @Test
    void deleteById_idExistente_eliminaEvento() {
        when(eventRepository.existsByID(1)).thenReturn(true);

        eventService.deleteById(1);

        verify(eventRepository, times(1)).deleteByID(1);
    }

    @Test
    void deleteById_idInexistente_lanzaExcepcion() {
        when(eventRepository.existsByID(999)).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class,
            () -> eventService.deleteById(999));

        assertEquals("El evento no existe", ex.getMessage());
        verify(eventRepository, never()).deleteByID(anyInt());
    }

    // ─────────────────────────────────────────────
    // updateEvent
    // ─────────────────────────────────────────────

    @Test
    void updateEvent_idExistente_actualizaEvento() {
        Event existente = new Event(1, "Viejo", "2026-01-01", "Desc vieja");
        Event actualizado = new Event(null, "Nuevo", "2026-12-31", "Desc nueva");
        when(eventRepository.getByID(1)).thenReturn(existente);

        eventService.updateEvent(1, actualizado);

        verify(eventRepository, times(1)).update(1, actualizado);
    }

    @Test
    void updateEvent_idInexistente_lanzaExcepcion() {
        when(eventRepository.getByID(999)).thenThrow(new RuntimeException("Evento no encontrado"));

        RuntimeException ex = assertThrows(RuntimeException.class,
            () -> eventService.updateEvent(999, new Event(null, "X", "2026-01-01", "Y")));

        assertEquals("Evento no encontrado", ex.getMessage());
        verify(eventRepository, never()).update(anyInt(), any());
    }

    // ─────────────────────────────────────────────
    // ValidationDate
    // ─────────────────────────────────────────────

    @Test
    void validationDate_formatoCorrecto_retornaNull() {
        assertNull(eventService.ValidationDate("2026-05-10"));
    }

    @Test
    void validationDate_fechaNula_retornaMensajeError() {
        String resultado = eventService.ValidationDate(null);
        assertNotNull(resultado);
        assertTrue(resultado.contains("vacía"));
    }

    @Test
    void validationDate_fechaVacia_retornaMensajeError() {
        String resultado = eventService.ValidationDate("  ");
        assertNotNull(resultado);
        assertTrue(resultado.contains("vacía"));
    }

    @Test
    void validationDate_formatoIncorrecto_retornaMensajeError() {
        String resultado = eventService.ValidationDate("10-05-2026");
        assertNotNull(resultado);
        assertTrue(resultado.contains("YYYY-MM-DD"));
    }

    // ─────────────────────────────────────────────
    // ValidationDescription
    // ─────────────────────────────────────────────

    @Test
    void validationDescription_descripcionValida_retornaNull() {
        assertNull(eventService.ValidationDescription("Gran evento musical"));
    }

    @Test
    void validationDescription_descripcionNula_retornaMensajeError() {
        String resultado = eventService.ValidationDescription(null);
        assertNotNull(resultado);
        assertTrue(resultado.contains("vacía"));
    }

    @Test
    void validationDescription_descripcionVacia_retornaMensajeError() {
        String resultado = eventService.ValidationDescription("   ");
        assertNotNull(resultado);
        assertTrue(resultado.contains("vacía"));
    }

    // ─────────────────────────────────────────────
    // EventExists
    // ─────────────────────────────────────────────

    @Test
    void eventExists_idExistente_retornaNull() {
        List<Event> eventos = List.of(new Event(1, "E1", "2026-01-01", "D1"));
        when(eventRepository.getAllEvents()).thenReturn(eventos);

        assertNull(eventService.EventExists(1));
    }

    @Test
    void eventExists_idInexistente_retornaMensajeError() {
        when(eventRepository.getAllEvents()).thenReturn(Collections.emptyList());

        String resultado = eventService.EventExists(99);
        assertNotNull(resultado);
        assertTrue(resultado.contains("99"));
    }

    @Test
    void eventExists_idNulo_retornaMensajeError() {
        String resultado = eventService.EventExists(null);
        assertNotNull(resultado);
        assertTrue(resultado.contains("nulo"));
    }
}