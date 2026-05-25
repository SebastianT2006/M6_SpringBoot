package com.riwi.m2w1.controllers;

import com.riwi.m2w1.models.Event;
import com.riwi.m2w1.service.EventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST para Eventos.
 *
 * Endpoints:
 *   POST   /events            → crear evento            (201)
 *   GET    /events            → listar paginado         (200)
 *   GET    /events/search     → buscar por nombre       (200)
 *   GET    /events/{id}       → obtener por ID          (200 / 404)
 *   PUT    /events/{id}       → actualizar              (200 / 404)
 *   DELETE /events/{id}       → eliminar                (204 / 404)
 */
@RestController
@RequestMapping("/api/events")
@Tag(name = "Eventos", description = "CRUD completo de eventos con paginación y ordenamiento.")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    // ── POST /events ──────────────────────────────────────────────────────────
    @Operation(summary = "Crear un evento",
               description = "Persiste un nuevo evento en la BD. El ID es generado automáticamente.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Evento creado."),
        @ApiResponse(responseCode = "400", description = "Datos inválidos.",
                     content = @Content(examples = @ExampleObject(
                         value = "{\"message\":\"Datos inválidos.\",\"errors\":{\"name\":\"El nombre es obligatorio\"}}"
                     )))
    })
    @PostMapping
    public ResponseEntity<Event> createEvent(@Valid @RequestBody Event event) {
        Event saved = eventService.createEvent(event);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // ── GET /events ───────────────────────────────────────────────────────────
    @Operation(summary = "Listar eventos (paginado)",
               description = "Devuelve una página de eventos. Parámetros: ?page=0&size=10&sort=name,asc")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Página de eventos.")
    })
    @GetMapping
    public ResponseEntity<Page<Event>> getAllEvents(
            // @PageableDefault: página 0, tamaño 10, ordenado por id ascendente por defecto
            @ParameterObject
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC)
            Pageable pageable) {
        return ResponseEntity.ok(eventService.findAll(pageable));
    }

    // ── GET /events/search?name=rock ──────────────────────────────────────────
    @Operation(summary = "Buscar eventos por nombre",
               description = "Búsqueda parcial e insensible a mayúsculas. Ej: /events/search?name=rock&page=0&size=5")
    @GetMapping("/search")
    public ResponseEntity<Page<Event>> searchByName(
            @Parameter(description = "Fragmento del nombre a buscar")
            @RequestParam String name,
            @ParameterObject
            @PageableDefault(size = 10, sort = "name", direction = Sort.Direction.ASC)
            Pageable pageable) {
        return ResponseEntity.ok(eventService.findByName(name, pageable));
    }

    // ── GET /events/{id} ──────────────────────────────────────────────────────
    @Operation(summary = "Obtener evento por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Evento encontrado."),
        @ApiResponse(responseCode = "404", description = "Evento no encontrado.",
                     content = @Content(examples = @ExampleObject(
                         value = "{\"message\":\"Evento con ID 9999 no encontrado\"}"
                     )))
    })
    @GetMapping("/{id}")
    public ResponseEntity<Event> getById(@PathVariable Long id) {
        return ResponseEntity.ok(eventService.getById(id));
    }

    // ── PUT /events/{id} ──────────────────────────────────────────────────────
    @Operation(summary = "Actualizar evento por ID",
               description = "Actualiza todos los campos. Valida existencia (404 si no existe).")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Evento actualizado."),
        @ApiResponse(responseCode = "400", description = "Datos inválidos."),
        @ApiResponse(responseCode = "404", description = "Evento no encontrado.")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Event> updateEvent(
            @PathVariable Long id,
            @Valid @RequestBody Event updatedEvent) {
        return ResponseEntity.ok(eventService.updateEvent(id, updatedEvent));
    }

    // ── DELETE /events/{id} ───────────────────────────────────────────────────
    @Operation(summary = "Eliminar evento por ID",
               description = "Borrado físico. Devuelve 204 No Content si fue exitoso.")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Evento eliminado correctamente."),
        @ApiResponse(responseCode = "404", description = "Evento no encontrado.")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        eventService.deleteById(id);
        return ResponseEntity.noContent().build(); // 204
    }
}
