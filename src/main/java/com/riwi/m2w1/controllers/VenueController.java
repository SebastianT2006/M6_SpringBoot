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

import com.riwi.m2w1.models.Venue;
import com.riwi.m2w1.service.VenueService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@Tag(name = "Lugares", description = "Operaciones relacionadas con lugares.")
@RequestMapping("/venues")
public class VenueController {

    private final VenueService venueService;

    public VenueController(VenueService venueService) {
        this.venueService = venueService;
    }

    @Operation(
        summary = "Crear un lugar",
        description = "Crea un nuevo lugar con los detalles proporcionados en el cuerpo de la solicitud."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "201",
            description = "Lugar creado correctamente.",
            content = @Content(
                examples = @ExampleObject(
                    name = "Lugar válido",
                    value = """
                    {
                      "id": 1,
                      "name": "Teatro Principal",
                      "address": "Cra. 65 #8B-91",
                      "capacity": 500
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
                          "address": "Cra. 65 #8B-91",
                          "capacity": 500
                        }
                        """
                    ),
                    @ExampleObject(
                        name = "Campos vacíos",
                        value = """
                        {
                          "name": "",
                          "address": "",
                          "capacity": null
                        }
                        """
                    ),
                    @ExampleObject(
                        name = "Capacidad negativa",
                        value = """
                        {
                          "name": "Teatro",
                          "address": "Calle 1",
                          "capacity": -5
                        }
                        """
                    ),
                    @ExampleObject(
                        name = "Capacidad cero",
                        value = """
                        {
                          "name": "Teatro",
                          "address": "Calle 1",
                          "capacity": 0
                        }
                        """
                    )
                }
            )
        ),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor.")
    })
    @PostMapping
    public ResponseEntity<String> createVenue(@Valid @RequestBody Venue venue) {
        venueService.createVenue(venue);
        return ResponseEntity.status(HttpStatus.CREATED).body("Lugar creado correctamente.");
    }

    @Operation(
        summary = "Obtener todos los lugares",
        description = "Devuelve una lista con todos los lugares disponibles."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Lista de lugares obtenida correctamente.",
            content = @Content(
                examples = @ExampleObject(
                    name = "Lista de lugares",
                    value = """
                    [
                      {"id":1,"name":"Teatro Principal","address":"Cra. 65 #8B-91","capacity":500},
                      {"id":2,"name":"Estadio El Campín","address":"Calle 63 #35-18","capacity":36000}
                    ]
                    """
                )
            )
        )
    })
    @GetMapping("/all")
    public ResponseEntity<List<Venue>> getAllVenues() {
        return ResponseEntity.ok(venueService.findAll());
    }

    @Operation(
        summary = "Buscar lugar por ID",
        description = "Devuelve el lugar que corresponde al ID proporcionado."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Lugar encontrado.",
            content = @Content(
                examples = @ExampleObject(
                    name = "Lugar",
                    value = """
                    {
                      "id": 1,
                      "name": "Teatro Principal",
                      "address": "Cra. 65 #8B-91",
                      "capacity": 500
                    }
                    """
                )
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Lugar no encontrado.",
            content = @Content(
                examples = @ExampleObject(
                    name = "ID inexistente",
                    value = """
                    {
                      "message": "Lugar no encontrado"
                    }
                    """
                )
            )
        )
    })
    @GetMapping("/{id}")
    public ResponseEntity<Venue> getByID(@PathVariable Integer id) {
        return ResponseEntity.ok(venueService.getByID(id));
    }

    @Operation(
        summary = "Eliminar lugar por ID",
        description = "Elimina un lugar existente basado en su ID."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lugar eliminado correctamente."),
        @ApiResponse(
            responseCode = "404",
            description = "Lugar no encontrado.",
            content = @Content(
                examples = @ExampleObject(
                    name = "ID inexistente",
                    value = """
                    {
                      "message": "El lugar no existe"
                    }
                    """
                )
            )
        )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteByID(@PathVariable Integer id) {
        venueService.deleteById(id);
        return ResponseEntity.ok("Lugar eliminado correctamente.");
    }

    @Operation(
        summary = "Actualizar lugar por ID",
        description = "Actualiza un lugar existente basado en su ID y los detalles proporcionados en el cuerpo de la solicitud."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Lugar actualizado correctamente.",
            content = @Content(
                examples = @ExampleObject(
                    name = "Lugar actualizado",
                    value = """
                    {
                      "name": "Teatro Nuevo",
                      "address": "Calle 50 #20-10",
                      "capacity": 800
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
                        name = "Campos vacíos",
                        value = """
                        {
                          "name": "",
                          "address": "",
                          "capacity": null
                        }
                        """
                    ),
                    @ExampleObject(
                        name = "Capacidad negativa",
                        value = """
                        {
                          "name": "Teatro",
                          "address": "Calle 1",
                          "capacity": -1
                        }
                        """
                    )
                }
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Lugar no encontrado.",
            content = @Content(
                examples = @ExampleObject(
                    name = "ID inexistente",
                    value = """
                    {
                      "message": "El lugar no existe"
                    }
                    """
                )
            )
        )
    })
    @PutMapping("/{id}")
    public ResponseEntity<String> updateVenue(@PathVariable Integer id, @Valid @RequestBody Venue updatedVenue) {
        venueService.updateVenue(id, updatedVenue);
        return ResponseEntity.ok("Lugar actualizado correctamente.");
    }
}