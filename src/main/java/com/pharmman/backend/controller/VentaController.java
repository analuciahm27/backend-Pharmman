package com.pharmman.backend.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pharmman.backend.dto.request.RegistrarVentaRequest;
import com.pharmman.backend.dto.response.VentaResponse;
import com.pharmman.backend.service.VentaService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/ventas")
@RequiredArgsConstructor
public class VentaController {

    private final VentaService ventaService;

    @PostMapping
    @PreAuthorize("@ss.tienePermiso('Ventas', 'escritura')")
    public ResponseEntity<VentaResponse> registrar(@RequestBody RegistrarVentaRequest request,
                                                    Authentication auth) {
        return ResponseEntity.ok(ventaService.registrar(request, auth.getName()));
    }

    @GetMapping
    @PreAuthorize("@ss.tienePermiso('Ventas', 'lectura')")
    public ResponseEntity<List<VentaResponse>> listar() {
        return ResponseEntity.ok(ventaService.listar());
    }

    @GetMapping("/filtrar")
    @PreAuthorize("@ss.tienePermiso('Ventas', 'lectura')")
    public ResponseEntity<List<VentaResponse>> filtrarPorFecha(
            @RequestParam String desde,
            @RequestParam String hasta) {
        return ResponseEntity.ok(ventaService.listarPorFecha(
            LocalDateTime.parse(desde), LocalDateTime.parse(hasta)));
    }
}
