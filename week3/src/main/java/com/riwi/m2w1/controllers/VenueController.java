package com.riwi.m2w1.controllers;

import com.riwi.m2w1.models.Venue;
import com.riwi.m2w1.service.VenueService;
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
 * Controlador REST para Lugares (Venues).
 */
@RestController
@RequestMapping("/api/venues")
@Tag(name = "Lugares", description = "CRUD completo de lugares con paginación y ordenamiento.")
public class VenueController {

    private final VenueService venueService;

    public VenueController(VenueService venueService) {
        this.venueService = venueService;
    }

    // ── POST /venues ──────────────────────────────────────────────────────────
    @Operation(summary = "Crear un lugar")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Lugar creado."),
        @ApiResponse(responseCode = "400", description = "Datos inválidos.")
    })
    @PostMapping
    public ResponseEntity<Venue> createVenue(@Valid @RequestBody Venue venue) {
        return ResponseEntity.status(HttpStatus.CREATED).body(venueService.createVenue(venue));
    }

    // ── GET /venues ───────────────────────────────────────────────────────────
    @Operation(summary = "Listar lugares (paginado)",
               description = "Soporta ?page=0&size=10&sort=name,asc")
    @GetMapping
    public ResponseEntity<Page<Venue>> getAllVenues(
            @ParameterObject
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC)
            Pageable pageable) {
        return ResponseEntity.ok(venueService.findAll(pageable));
    }

    // ── GET /venues/search?name=teatro ────────────────────────────────────────
    @Operation(summary = "Buscar lugares por nombre (parcial)")
    @GetMapping("/search")
    public ResponseEntity<Page<Venue>> searchByName(
            @Parameter(description = "Fragmento del nombre")
            @RequestParam String name,
            @ParameterObject
            @PageableDefault(size = 10, sort = "name", direction = Sort.Direction.ASC)
            Pageable pageable) {
        return ResponseEntity.ok(venueService.findByName(name, pageable));
    }

    // ── GET /venues/{id} ──────────────────────────────────────────────────────
    @Operation(summary = "Obtener lugar por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lugar encontrado."),
        @ApiResponse(responseCode = "404", description = "Lugar no encontrado.",
                     content = @Content(examples = @ExampleObject(
                         value = "{\"message\":\"Lugar con ID 9999 no encontrado\"}"
                     )))
    })
    @GetMapping("/{id}")
    public ResponseEntity<Venue> getById(@PathVariable Long id) {
        return ResponseEntity.ok(venueService.getById(id));
    }

    // ── PUT /venues/{id} ──────────────────────────────────────────────────────
    @Operation(summary = "Actualizar lugar por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lugar actualizado."),
        @ApiResponse(responseCode = "400", description = "Datos inválidos."),
        @ApiResponse(responseCode = "404", description = "Lugar no encontrado.")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Venue> updateVenue(
            @PathVariable Long id,
            @Valid @RequestBody Venue updatedVenue) {
        return ResponseEntity.ok(venueService.updateVenue(id, updatedVenue));
    }

    // ── DELETE /venues/{id} ───────────────────────────────────────────────────
    @Operation(summary = "Eliminar lugar por ID",
               description = "Borrado físico. Devuelve 204 No Content si fue exitoso.")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Lugar eliminado."),
        @ApiResponse(responseCode = "404", description = "Lugar no encontrado.")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVenue(@PathVariable Long id) {
        venueService.deleteById(id);
        return ResponseEntity.noContent().build(); // 204
    }
}
