package com.pharmman.backend.dto.request;

import lombok.Data;

@Data
public class RolPermisoRequest {
    private Integer rolId;
    private Integer moduloId;
    private boolean lectura;
    private boolean escritura;
}