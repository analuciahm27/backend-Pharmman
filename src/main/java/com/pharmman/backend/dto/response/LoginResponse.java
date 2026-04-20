package com.pharmman.backend.dto.response;

import lombok.Data;
import lombok.AllArgsConstructor;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Data
@AllArgsConstructor
public class LoginResponse {
    @JsonIgnore
    private String token;
    private String nombre;
    private String apellidoPaterno;
    private String email;
    private String rol;
    private List<RolPermisoResponse> permisos;
}