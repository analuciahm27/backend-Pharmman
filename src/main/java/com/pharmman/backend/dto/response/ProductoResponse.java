package com.pharmman.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductoResponse {
    private Integer id;
    private String codigo;
    private String nombre;
    private String descripcion;
    private Double precio;
    private Integer stock;
    private boolean activo;
    private Integer categoriaId;
    private String categoriaNombre;
}
