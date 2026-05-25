package com.riwi.m2w1.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad JPA que representa un Evento en la base de datos.
 * @Entity  → Hibernate la mapea a la tabla "events"
 * @Table   → nombre explícito de la tabla
 */
@Entity
@Table(name = "events")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Event {

    /** Clave primaria con auto-incremento gestionado por la BD */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Nombre del evento: obligatorio, máx. 150 caracteres */
    @Column(nullable = false, length = 150)
    @NotBlank(message = "El nombre es obligatorio")
    private String name;

    /** Fecha en formato YYYY-MM-DD; almacenada como String de 10 chars */
    @Column(nullable = false, length = 10)
    @NotBlank(message = "La fecha es obligatoria")
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "La fecha debe tener el formato YYYY-MM-DD")
    private String date;

    /** Descripción libre del evento */
    @Column(nullable = false, length = 500)
    @NotBlank(message = "La descripción es obligatoria")
    private String description;
}
