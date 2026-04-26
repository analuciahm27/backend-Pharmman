package com.pharmman.backend.dto.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SesionResponse {
    private Integer id;
    private LocalDateTime entrada;
    private LocalDateTime salida;
    private Integer usuarioId;
    private String usuarioNombre;
    private String usuarioEmail;
    private String rolNombre;
}
