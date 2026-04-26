package com.pharmman.backend.dto.request;

import lombok.Data;

@Data
public class EditarProductoRequest {
    private String codigo;
    private String nombre;
    private String descripcion;
    private Double precio;
    private Integer categoriaId;
}
