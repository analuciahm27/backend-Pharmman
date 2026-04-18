package com.pharmman.backend.dto.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RolResponse {
    private Long id;
    private String nombre;
    private String descripcion;
    private boolean estado;
    private List<String> permisos;
}
