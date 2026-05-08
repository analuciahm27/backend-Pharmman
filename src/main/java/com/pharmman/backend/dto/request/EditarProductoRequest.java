package com.pharmman.backend.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class EditarProductoRequest {

    private String codigo;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 3, message = "El nombre debe tener mínimo 3 caracteres")
    @Pattern(regexp = "^[a-zA-Z0-9áéíóúÁÉÍÓÚñÑüÜ\\s]+$",
             message = "El nombre no puede contener caracteres especiales")
    private String nombre;

    @NotBlank(message = "La descripción es obligatoria")
    @Size(min = 3, message = "La descripción debe tener mínimo 3 caracteres")
    @Pattern(regexp = "^[a-zA-Z0-9áéíóúÁÉÍÓÚñÑüÜ\\s.,;:\\-()]+$",
             message = "La descripción no puede contener caracteres especiales")
    private String descripcion;

    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0.01", message = "El precio debe ser mayor a 0")
    private Double precio;

    @NotNull(message = "La categoría es obligatoria")
    private Integer categoriaId;
}
