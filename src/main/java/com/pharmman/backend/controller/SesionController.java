package com.pharmman.backend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pharmman.backend.dto.response.SesionResponse;
import com.pharmman.backend.service.SesionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/sesiones")
@RequiredArgsConstructor
public class SesionController {

    private final SesionService sesionService;

    @GetMapping
    @PreAuthorize("@ss.tienePermiso('Sesiones', 'lectura')")
    public ResponseEntity<List<SesionResponse>> listar() {
        return ResponseEntity.ok(sesionService.listar());
    }
}
