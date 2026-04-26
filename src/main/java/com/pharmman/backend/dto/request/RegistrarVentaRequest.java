package com.pharmman.backend.dto.request;

import java.util.List;

import lombok.Data;

@Data
public class RegistrarVentaRequest {
    private String metodoPago;
    private List<DetalleVentaRequest> detalles;

    @Data
    public static class DetalleVentaRequest {
        private Integer productoId;
        private Integer cantidad;
    }
}
