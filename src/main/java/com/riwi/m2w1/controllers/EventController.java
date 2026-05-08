package com.riwi.m2w1.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.riwi.m2w1.models.Event;
import com.riwi.m2w1.service.EventService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/events")
@Tag(name = "Eventos", description = "Operaciones relacionadas con eventos.")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @Operation(
        summary = "Crear un evento",
        description = "Crea un nuevo evento con los detalles proporcionados en el cuerpo de la solicitud."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "201",
            description = "Evento creado correctamente.",
            content = @Content(
                examples = @ExampleObject(
                    name = "Evento válido",
                    value = """
                    {
                      "id": 1,
                      "name": "Concierto de Rock",
                      "date": "2026-05-10",
                      "description": "Evento musical"
                    }
                    """
                )
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Datos inválidos.",
            content = @Content(
                examples = {
                     @ExampleObject(
        name = "Falta nombre",
        value = """
        {
          "date": "2026-05-10",
          "description": "Evento musical"
        }
        """
    ),
                    @ExampleObject(
        name = "Campos vacíos",
        value = """
        {
          "name": "",
          "date": "",
          "description": ""
        }
        """
    ),
                    @ExampleObject(
        name = "Formato de fecha inválido",
        value = """
        {
          "name": "Evento",
          "date": "10-05-2026",
          "description": "Desc"
        }
        """
    )
                }
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Recurso no encontrado.",
            content = @Content(
               examples = @ExampleObject(
    name = "Evento no encontrado",
    value = """
    {
      "message": "Evento no encontrado"
    }
    """
)
            )
        ),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor.")
    })
    @PostMapping
    public ResponseEntity<String> createEvent(@Valid @RequestBody Event event) {
        eventService.createEvent(event);
        return ResponseEntity.status(HttpStatus.CREATED).body("Evento creado correctamente.");
    }

    @Operation(
        summary = "Obtener todos los eventos",
        description = "Devuelve una lista con todos los eventos disponibles."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Lista de eventos obtenida correctamente.",
            content = @Content(
                examples = @ExampleObject(
                    name = "Lista de eventos",
                    value = """
                    [{"id":1,"name":"Concierto de Rock","date":"2026-05-10","description":"Evento musical"}]
                    """
                )
            )
        )
    })
    @GetMapping("/all")
    public ResponseEntity<List<Event>> getAllEvents() {
        return ResponseEntity.ok(eventService.findAll());
    }

    @Operation(
        summary = "Buscar evento por ID",
        description = "Devuelve el evento que corresponde al ID proporcionado."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Evento encontrado.",
            content = @Content(
               examples = @ExampleObject(
    name = "Evento",
    value = """
    {
      "id": 1,
      "name": "Concierto",
      "date": "2026-05-10",
      "description": "Desc"
    }
    """
)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Evento no encontrado.",
            content = @Content(
               examples = @ExampleObject(
    name = "ID inexistente",
    value = """
    {
      "message": "Evento no encontrado"
    }
    """
)
            )
        )
    })
    @GetMapping("/{id}")
    public ResponseEntity<Event> getByID(@PathVariable Integer id) {
        return ResponseEntity.ok(eventService.getByID(id));
    }

    @Operation(
        summary = "Eliminar evento por ID",
        description = "Elimina un evento existente basado en su ID."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Evento eliminado correctamente."),
        @ApiResponse(responseCode = "404", description = "Evento no encontrado.")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteByID(@PathVariable Integer id) {
        eventService.deleteById(id);
        return ResponseEntity.ok("Evento eliminado correctamente.");
    }

    @Operation(
        summary = "Actualizar evento por ID",
        description = "Actualiza un evento existente basado en su ID y los detalles proporcionados en el cuerpo de la solicitud."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Evento actualizado correctamente."),
        @ApiResponse(responseCode = "400", description = "Datos inválidos."),
        @ApiResponse(responseCode = "404", description = "Evento no encontrado.")
    })
    @PutMapping("/{id}")
    public ResponseEntity<String> updateEvent(@PathVariable Integer id, @Valid @RequestBody Event updatedEvent) {
        eventService.updateEvent(id, updatedEvent);
        return ResponseEntity.ok("Evento actualizado correctamente.");
    }
}
