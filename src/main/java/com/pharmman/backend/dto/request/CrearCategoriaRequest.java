package com.pharmman.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CrearCategoriaRequest {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 3, message = "El nombre debe tener mínimo 3 caracteres")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ\\s]+$",
             message = "El nombre no puede contener caracteres especiales ni números")
    private String nombre;

    @NotBlank(message = "El prefijo es obligatorio")
    @Size(min = 3, max = 3, message = "El prefijo debe tener exactamente 3 letras")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ]+$",
             message = "El prefijo solo puede contener letras, sin números ni caracteres especiales")
    private String prefijo;
}
