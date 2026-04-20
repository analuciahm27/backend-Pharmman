package com.pharmman.backend.controller;

import com.pharmman.backend.dto.request.CrearRolRequest;
import com.pharmman.backend.dto.request.RolPermisoRequest;
import com.pharmman.backend.dto.response.RolPermisoResponse;
import com.pharmman.backend.entity.Modulo;
import com.pharmman.backend.entity.Rol;
import com.pharmman.backend.entity.RolPermiso;
import com.pharmman.backend.service.RolService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

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
    public ResponseEntity<Rol> crear(@RequestBody CrearRolRequest request) {
        return ResponseEntity.ok(rolService.crearRol(request));
    }

    @GetMapping("/modulos")
    public ResponseEntity<List<Modulo>> listarModulos() {
        return ResponseEntity.ok(rolService.listarModulos());
    }

    @GetMapping("/{rolId}/permisos")
    public ResponseEntity<List<RolPermisoResponse>> getPermisos(
            @PathVariable Integer rolId) {
        return ResponseEntity.ok(rolService.getPermisosPorRol(rolId));
    }

    @PutMapping("/permisos")
    public ResponseEntity<RolPermiso> actualizarPermiso(
            @RequestBody RolPermisoRequest request) {
        return ResponseEntity.ok(rolService.actualizarPermiso(request));
    }
}