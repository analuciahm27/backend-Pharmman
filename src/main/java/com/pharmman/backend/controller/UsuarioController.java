package com.pharmman.backend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pharmman.backend.dto.request.CrearUsuarioRequest;
import com.pharmman.backend.dto.request.EditarUsuarioRequest;
import com.pharmman.backend.dto.response.UsuarioResponse;
import com.pharmman.backend.service.UsuarioService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @GetMapping
    @PreAuthorize("@ss.tienePermiso('USUARIOS', 'lectura')")
    public ResponseEntity<List<UsuarioResponse>> listar() {
        return ResponseEntity.ok(usuarioService.listarUsuarios());
    }

    @PostMapping
    @PreAuthorize("@ss.tienePermiso('USUARIOS', 'escritura')")
    public ResponseEntity<UsuarioResponse> crear(
            @RequestBody CrearUsuarioRequest request) {
        return ResponseEntity.ok(usuarioService.crearUsuario(request));
    }

    @PatchMapping("/{id}/estado")
    @PreAuthorize("@ss.tienePermiso('USUARIOS', 'escritura')")
    public ResponseEntity<UsuarioResponse> cambiarEstado(@PathVariable Integer id) {
        return ResponseEntity.ok(usuarioService.cambiarEstado(id));
    }
    @PutMapping("/{id}")
    @PreAuthorize("@ss.tienePermiso('USUARIOS', 'escritura')")
    public ResponseEntity<UsuarioResponse> editar(
            @PathVariable Integer id,
            @RequestBody EditarUsuarioRequest request) {
        return ResponseEntity.ok(usuarioService.editarUsuario(id, request));
    }
}