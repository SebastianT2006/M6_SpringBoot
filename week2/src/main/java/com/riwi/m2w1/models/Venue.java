package com.riwi.m2w1.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad JPA que representa un Lugar (venue) en la base de datos.
 */
@Entity
@Table(name = "venues")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Venue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Nombre del lugar: obligatorio, máx. 150 caracteres */
    @Column(nullable = false, length = 150)
    @NotBlank(message = "El nombre es obligatorio")
    private String name;

    /** Dirección física del lugar */
    @Column(nullable = false, length = 300)
    @NotBlank(message = "La dirección es obligatoria")
    private String address;

    /** Capacidad de personas; debe ser al menos 1 */
    @Column(nullable = false)
    @NotNull(message = "La capacidad es obligatoria")
    @Min(value = 1, message = "La capacidad debe ser al menos 1")
    private Integer capacity;
}
