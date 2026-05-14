package com.pharmman.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class EditarUsuarioRequest {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ\\s]+$",
             message = "El nombre solo puede contener letras y espacios")
    private String nombre;

    @NotBlank(message = "El apellido paterno es obligatorio")
    @Size(min = 3, max = 100, message = "El apellido paterno debe tener entre 3 y 100 caracteres")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ\\s]+$",
             message = "El apellido paterno solo puede contener letras y espacios")
    private String apellidoPaterno;

    @NotBlank(message = "El apellido materno es obligatorio")
    @Size(min = 3, max = 100, message = "El apellido materno debe tener entre 3 y 100 caracteres")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ\\s]+$",
             message = "El apellido materno solo puede contener letras y espacios")
    private String apellidoMaterno;

    @NotBlank(message = "El email es obligatorio")
    @Size(max = 150, message = "El email no puede superar los 150 caracteres")
    @Pattern(regexp = "^[a-zA-Z0-9._%+\\-]+@pharmman\\.com$",
             message = "El email debe pertenecer al dominio @pharmman.com")
    private String email;

    private Integer rolId;
}
