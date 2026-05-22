package com.pharmman.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CrearUsuarioRequest {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
    @Pattern(
        regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ]+(\\s[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ]+)*$",
        message = "El nombre solo puede contener letras y no debe ser solo espacios"
    )
    private String nombre;

    @NotBlank(message = "El apellido paterno es obligatorio")
    @Size(min = 3, max = 100, message = "El apellido paterno debe tener entre 3 y 100 caracteres")
    @Pattern(
        regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ]+(\\s[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ]+)*$",
        message = "El apellido paterno solo puede contener letras y no debe ser solo espacios"
    )
    private String apellidoPaterno;

    @NotBlank(message = "El apellido materno es obligatorio")
    @Size(min = 3, max = 100, message = "El apellido materno debe tener entre 3 y 100 caracteres")
    @Pattern(
        regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ]+(\\s[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ]+)*$",
        message = "El apellido materno solo puede contener letras y no debe ser solo espacios"
    )
    private String apellidoMaterno;

    /**
     * Solo el prefijo del email (sin @pharmman.com).
     * Reglas:
     *  - Obligatorio
     *  - Máximo 64 caracteres
     *  - Solo alfanuméricos, puntos y guiones
     *  - No puede empezar con punto
     *  - No puede contener '@' ni espacios
     */
    @NotBlank(message = "El prefijo es obligatorio")
    @Size(max = 64, message = "El prefijo no puede superar los 64 caracteres")
    @Pattern(
        regexp = "^[a-zA-Z0-9][a-zA-Z0-9.\\-]*$",
        message = "El prefijo solo puede contener letras, números, puntos y guiones, y no puede empezar con punto"
    )
    private String emailPrefijo;

    @NotBlank(message = "El DNI es obligatorio")
    @Pattern(
        regexp = "^\\d{8}$",
        message = "El DNI debe tener exactamente 8 dígitos numéricos"
    )
    private String dni;

    @NotNull(message = "El rol es obligatorio")
    private Integer rolId;
}
