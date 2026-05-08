package com.riwi.m2w1;

import com.riwi.m2w1.models.Venue;
import com.riwi.m2w1.repositories.VenueRepository;
import com.riwi.m2w1.service.VenueService;

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
class VenueServiceTest {

    @Mock
    private VenueRepository venueRepository;

    @InjectMocks
    private VenueService venueService;

    // ─────────────────────────────────────────────
    // findAll
    // ─────────────────────────────────────────────

    @Test
    void findAll_retornaListaDeVenues() {
        List<Venue> venues = Arrays.asList(
            new Venue(1, "Teatro Principal", "Cra. 65 #8B-91", 500),
            new Venue(2, "Estadio El Campín", "Calle 63 #35-18", 36000)
        );
        when(venueRepository.getAllVenues()).thenReturn(venues);

        List<Venue> resultado = venueService.findAll();

        assertEquals(2, resultado.size());
        assertEquals("Teatro Principal", resultado.get(0).getName());
        verify(venueRepository, times(1)).getAllVenues();
    }

    @Test
    void findAll_listaVacia_retornaListaVacia() {
        when(venueRepository.getAllVenues()).thenReturn(Collections.emptyList());

        List<Venue> resultado = venueService.findAll();

        assertTrue(resultado.isEmpty());
        verify(venueRepository, times(1)).getAllVenues();
    }

    // ─────────────────────────────────────────────
    // getByID
    // ─────────────────────────────────────────────

    @Test
    void getByID_idExistente_retornaVenue() {
        Venue venue = new Venue(1, "Teatro Principal", "Cra. 65 #8B-91", 500);
        when(venueRepository.getByID(1)).thenReturn(venue);

        Venue resultado = venueService.getByID(1);

        assertNotNull(resultado);
        assertEquals(1, resultado.getId());
        assertEquals("Teatro Principal", resultado.getName());
        verify(venueRepository, times(1)).getByID(1);
    }

    @Test
    void getByID_idInexistente_lanzaExcepcion() {
        when(venueRepository.getByID(999)).thenThrow(new RuntimeException("Lugar no encontrado"));

        RuntimeException ex = assertThrows(RuntimeException.class,
            () -> venueService.getByID(999));

        assertEquals("Lugar no encontrado", ex.getMessage());
        verify(venueRepository, times(1)).getByID(999);
    }

    // ─────────────────────────────────────────────
    // createVenue
    // ─────────────────────────────────────────────

    @Test
    void createVenue_datosValidos_asignaIdYGuarda() {
        Venue venue = new Venue(null, "Teatro A", "Calle 10", 200);
        when(venueRepository.getAllVenues()).thenReturn(Collections.emptyList());

        venueService.createVenue(venue);

        assertEquals(1, venue.getId()); // lista vacía → newId = 0 + 1
        verify(venueRepository, times(1)).createVenue(venue);
    }

    @Test
    void createVenue_idIncrementalSegunListaExistente() {
        List<Venue> existentes = Arrays.asList(
            new Venue(1, "Lugar 1", "Calle A", 100),
            new Venue(2, "Lugar 2", "Calle B", 200)
        );
        when(venueRepository.getAllVenues()).thenReturn(existentes);

        Venue nuevo = new Venue(null, "Lugar Nuevo", "Calle C", 300);
        venueService.createVenue(nuevo);

        assertEquals(3, nuevo.getId()); // lista con 2 → newId = 3
        verify(venueRepository, times(1)).createVenue(nuevo);
    }

    @Test
    void createVenue_nombreNulo_lanzaExcepcion() {
        Venue venue = new Venue(null, null, "Calle 1", 100);

        RuntimeException ex = assertThrows(RuntimeException.class,
            () -> venueService.createVenue(venue));

        assertEquals("Nombre requerido", ex.getMessage());
        verify(venueRepository, never()).createVenue(any());
    }

    @Test
    void createVenue_nombreVacio_lanzaExcepcion() {
        Venue venue = new Venue(null, "", "Calle 1", 100);

        RuntimeException ex = assertThrows(RuntimeException.class,
            () -> venueService.createVenue(venue));

        assertEquals("Nombre requerido", ex.getMessage());
        verify(venueRepository, never()).createVenue(any());
    }

    // ─────────────────────────────────────────────
    // deleteById
    // ─────────────────────────────────────────────

    @Test
    void deleteById_idExistente_eliminaVenue() {
        when(venueRepository.existsByID(1)).thenReturn(true);

        venueService.deleteById(1);

        verify(venueRepository, times(1)).deleteByID(1);
    }

    @Test
    void deleteById_idInexistente_lanzaExcepcion() {
        when(venueRepository.existsByID(999)).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class,
            () -> venueService.deleteById(999));

        assertEquals("El lugar no existe", ex.getMessage());
        verify(venueRepository, never()).deleteByID(anyInt());
    }

    // ─────────────────────────────────────────────
    // updateVenue
    // ─────────────────────────────────────────────

    @Test
    void updateVenue_idExistente_actualizaVenue() {
        Venue existente = new Venue(1, "Teatro Viejo", "Calle 1", 100);
        Venue actualizado = new Venue(null, "Teatro Nuevo", "Calle 2", 300);
        when(venueRepository.getByID(1)).thenReturn(existente);

        venueService.updateVenue(1, actualizado);

        verify(venueRepository, times(1)).update(1, actualizado);
    }

    @Test
    void updateVenue_idInexistente_lanzaExcepcion() {
        when(venueRepository.getByID(999)).thenThrow(new RuntimeException("Lugar no encontrado"));

        RuntimeException ex = assertThrows(RuntimeException.class,
            () -> venueService.updateVenue(999, new Venue(null, "X", "Y", 10)));

        assertEquals("Lugar no encontrado", ex.getMessage());
        verify(venueRepository, never()).update(anyInt(), any());
    }
}