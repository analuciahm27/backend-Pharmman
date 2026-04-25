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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RolController {

    private final RolService rolService;

    @GetMapping
    @PreAuthorize("@ss.tienePermiso('USUARIOS', 'lectura')")
    public ResponseEntity<List<Rol>> listar() {
        return ResponseEntity.ok(rolService.listarRoles());
    }

    @PostMapping
    @PreAuthorize("@ss.tienePermiso('USUARIOS', 'escritura')")
    public ResponseEntity<Rol> crear(@RequestBody CrearRolRequest request) {
        return ResponseEntity.ok(rolService.crearRol(request));
    }

    @GetMapping("/modulos")
    @PreAuthorize("@ss.tienePermiso('USUARIOS', 'lectura')")
    public ResponseEntity<List<Modulo>> listarModulos() {
        return ResponseEntity.ok(rolService.listarModulos());
    }

    @GetMapping("/{rolId}/permisos")
    @PreAuthorize("@ss.tienePermiso('USUARIOS', 'lectura')")
    public ResponseEntity<List<RolPermisoResponse>> getPermisos(
            @PathVariable Integer rolId) {
        return ResponseEntity.ok(rolService.getPermisosPorRol(rolId));
    }

    @PutMapping("/permisos")
    @PreAuthorize("@ss.tienePermiso('USUARIOS', 'escritura')")
    public ResponseEntity<RolPermiso> actualizarPermiso(
            @RequestBody RolPermisoRequest request) {
        return ResponseEntity.ok(rolService.actualizarPermiso(request));
    }
}