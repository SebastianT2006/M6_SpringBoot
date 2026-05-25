package com.riwi.m2w1;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.instanceOf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Tests de integración para las vistas Thymeleaf (rutas /admin/).
 *
 * Validan los 4 escenarios de aceptación de la HU:
 *  1. Camino feliz  → 200 OK + tabla de datos
 *  2. Catálogo vacío → atributo "events" es una List (puede estar vacía)
 *  3. PRG tras POST  → 302 redirect a /admin/events o /admin/venues
 *  4. Atributos del Model → "events" y "venues" presentes en el modelo
 */
@SpringBootTest
@AutoConfigureMockMvc
class AdminViewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // ── EVENTS ────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("GET /admin/events → 200, vista 'admin/events/list', model tiene 'events'")
    void listEvents_returns200AndCorrectViewAndModel() throws Exception {
        mockMvc.perform(get("/admin/events"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/events/list"))
                .andExpect(model().attributeExists("events"))
                .andExpect(model().attribute("events", instanceOf(List.class)));
    }

    @Test
    @DisplayName("GET /admin/events/new → 200, vista 'admin/events/form', model tiene 'event'")
    void newEventForm_returns200AndFormView() throws Exception {
        mockMvc.perform(get("/admin/events/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/events/form"))
                .andExpect(model().attributeExists("event"));
    }

    @Test
    @DisplayName("POST /admin/events/save → 302 redirect a /admin/events (PRG)")
    void saveEvent_redirectsToList() throws Exception {
        mockMvc.perform(post("/admin/events/save")
                        .param("name", "Evento Test MockMvc")
                        .param("date", "2024-12-01")
                        .param("description", "Creado desde test"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/events"));
    }

    // ── VENUES ────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("GET /admin/venues → 200, vista 'admin/venues/list', model tiene 'venues'")
    void listVenues_returns200AndCorrectViewAndModel() throws Exception {
        mockMvc.perform(get("/admin/venues"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/venues/list"))
                .andExpect(model().attributeExists("venues"))
                .andExpect(model().attribute("venues", instanceOf(List.class)));
    }

    @Test
    @DisplayName("GET /admin/venues/new → 200, vista 'admin/venues/form', model tiene 'venue'")
    void newVenueForm_returns200AndFormView() throws Exception {
        mockMvc.perform(get("/admin/venues/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/venues/form"))
                .andExpect(model().attributeExists("venue"));
    }

    @Test
    @DisplayName("POST /admin/venues/save → 302 redirect a /admin/venues (PRG)")
    void saveVenue_redirectsToList() throws Exception {
        mockMvc.perform(post("/admin/venues/save")
                        .param("name", "Sala Test")
                        .param("address", "Calle 123, Medellín")
                        .param("capacity", "300"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/venues"));
    }

    // ── SEPARACIÓN API / VISTAS ────────────────────────────────────────────────

    @Test
    @DisplayName("GET /api/events → 200 JSON (ruta REST separada de las vistas)")
    void apiEvents_returnsJson() throws Exception {
        mockMvc.perform(get("/api/events")
                        .accept(org.springframework.http.MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(
                        org.springframework.http.MediaType.APPLICATION_JSON));
    }
}
