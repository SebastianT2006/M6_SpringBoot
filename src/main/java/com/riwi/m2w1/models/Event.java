package com.riwi.m2w1.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Event {

    private Integer id;

    @NotBlank(message = "El nombre es obligatorio")
    private String name;

    @NotBlank(message = "La fecha es obligatoria")
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "La fecha debe tener el formato YYYY-MM-DD")
    private String date;

    @NotBlank(message = "La descripción es obligatoria")
    private String description;
}
