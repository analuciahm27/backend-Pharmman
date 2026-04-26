package com.pharmman.backend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pharmman.backend.dto.request.RegistrarIngresoRequest;
import com.pharmman.backend.dto.response.IngresoResponse;
import com.pharmman.backend.service.IngresoService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/ingresos")
@RequiredArgsConstructor
public class IngresoController {

    private final IngresoService ingresoService;

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<IngresoResponse> registrar(@RequestBody RegistrarIngresoRequest request,
                                                      Authentication auth) {
        return ResponseEntity.ok(ingresoService.registrar(request, auth.getName()));
    }

    @GetMapping
    @PreAuthorize("@ss.tienePermiso('Ingresos', 'lectura')")
    public ResponseEntity<List<IngresoResponse>> listar() {
        return ResponseEntity.ok(ingresoService.listar());
    }
}
