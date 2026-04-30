package com.pharmman.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class EditarUsuarioRequest {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 3, message = "El nombre debe tener mínimo 3 caracteres")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ\\s]+$", message = "El nombre no puede contener caracteres especiales")
    private String nombre;

    @NotBlank(message = "El apellido paterno es obligatorio")
    @Size(min = 3, message = "El apellido paterno debe tener mínimo 3 caracteres")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ\\s]+$", message = "El apellido paterno no puede contener caracteres especiales")
    private String apellidoPaterno;

    @Size(min = 3, message = "El apellido materno debe tener mínimo 3 caracteres")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ\\s]*$", message = "El apellido materno no puede contener caracteres especiales")
    private String apellidoMaterno;

    private String email;
    private Integer rolId;
}
