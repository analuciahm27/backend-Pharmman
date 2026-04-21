package com.pharmman.backend.dto.request;

import lombok.Data;

@Data
public class EditarUsuarioRequest {
    private String nombre;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String email;
    private Integer rolId;
}