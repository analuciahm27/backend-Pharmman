package com.pharmman.backend.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class IngresoResponse {
    private Integer id;
    private LocalDateTime fecha;
    private String usuarioNombre;
    private List<DetalleIngresoResponse> detalles;

    @Data
    @AllArgsConstructor
    public static class DetalleIngresoResponse {
        private String productoNombre;
        private Integer cantidad;
    }
}
