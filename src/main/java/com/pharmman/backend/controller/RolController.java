package com.pharmman.backend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pharmman.backend.dto.request.AsignarPermisosRequest;
import com.pharmman.backend.dto.request.CrearRolRequest;
import com.pharmman.backend.entity.Permiso;
import com.pharmman.backend.entity.Rol;
import com.pharmman.backend.service.RolService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RolController {

    private final RolService rolService;

    @GetMapping
    public ResponseEntity<List<Rol>> listar() {
        return ResponseEntity.ok(rolService.listarRoles());
    }

    @PostMapping
    public ResponseEntity<Rol> crear(
            @RequestBody CrearRolRequest request) {
        return ResponseEntity.ok(rolService.crearRol(request));
    }

    @PutMapping("/permisos")
    public ResponseEntity<Rol> asignarPermisos(
            @RequestBody AsignarPermisosRequest request) {
        return ResponseEntity.ok(rolService.asignarPermisos(request));
    }

    @GetMapping("/permisos")
    public ResponseEntity<List<Permiso>> listarPermisos() {
        return ResponseEntity.ok(rolService.listarPermisos());
    }
}