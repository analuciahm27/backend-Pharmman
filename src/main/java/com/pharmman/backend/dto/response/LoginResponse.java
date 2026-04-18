package com.pharmman.backend.dto.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {
    private String token;
    private String nombre;
    private String apellidoPaterno;
    private String email;
    private String rol;
    private List<String> permisos;
}