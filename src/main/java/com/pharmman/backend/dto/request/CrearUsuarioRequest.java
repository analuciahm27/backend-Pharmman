package com.pharmman.backend.dto.request;

import lombok.Data;

@Data
public class CrearUsuarioRequest {
    private String nombre;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String email;
    private String password;
    private Integer rolId;
}