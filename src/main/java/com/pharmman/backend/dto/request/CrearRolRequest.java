package com.pharmman.backend.dto.request;

import lombok.Data;

@Data
public class CrearRolRequest {
    private String nombre;
    private String descripcion;
}