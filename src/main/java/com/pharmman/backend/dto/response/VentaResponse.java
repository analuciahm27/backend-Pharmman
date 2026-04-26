package com.pharmman.backend.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VentaResponse {
    private Integer id;
    private String metodoPago;
    private Double total;
    private LocalDateTime fecha;
    private String usuarioNombre;
    private List<DetalleVentaResponse> detalles;

    @Data
    @AllArgsConstructor
    public static class DetalleVentaResponse {
        private String productoNombre;
        private Integer cantidad;
        private Double precioUnitario;
        private Double subtotal;
    }
}
