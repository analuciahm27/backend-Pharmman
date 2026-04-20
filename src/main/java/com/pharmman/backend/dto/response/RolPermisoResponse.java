package com.pharmman.backend.dto.response;

import lombok.Data;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
public class RolPermisoResponse {
    private Integer id;
    private String modulo;
    private boolean lectura;
    private boolean escritura;
}