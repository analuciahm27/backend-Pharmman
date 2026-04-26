package com.pharmman.backend.dto.request;

import java.util.List;

import lombok.Data;

@Data
public class RegistrarIngresoRequest {
    private List<DetalleIngresoRequest> detalles;

    @Data
    public static class DetalleIngresoRequest {
        private Integer productoId;
        private Integer cantidad;
    }
}
