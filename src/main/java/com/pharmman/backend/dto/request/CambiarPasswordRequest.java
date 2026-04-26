package com.pharmman.backend.dto.request;

import lombok.Data;

@Data
public class CambiarPasswordRequest {
    private String passwordActual;
    private String passwordNueva;
}
