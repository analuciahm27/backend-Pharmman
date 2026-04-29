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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pharmman.backend.dto.request.CrearProductoRequest;
import com.pharmman.backend.dto.request.EditarProductoRequest;
import com.pharmman.backend.dto.response.ProductoResponse;
import com.pharmman.backend.service.ProductoService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor
public class ProductoController {

    private final ProductoService productoService;

    @GetMapping
    @PreAuthorize("@ss.tienePermiso('Productos', 'lectura')")
    public ResponseEntity<List<ProductoResponse>> listar() {
        return ResponseEntity.ok(productoService.listar());
    }

    @GetMapping("/siguiente-codigo")
    @PreAuthorize("@ss.tienePermiso('Productos', 'lectura')")
    public ResponseEntity<java.util.Map<String, String>> siguienteCodigo(
            @org.springframework.web.bind.annotation.RequestParam Integer categoriaId) {
        return ResponseEntity.ok(java.util.Map.of("codigo", productoService.siguienteCodigo(categoriaId)));
    }

    @GetMapping("/buscar")
    @PreAuthorize("@ss.tienePermiso('Productos', 'lectura')")
    public ResponseEntity<List<ProductoResponse>> buscar(@RequestParam String termino) {
        return ResponseEntity.ok(productoService.buscar(termino));
    }

    @PostMapping
    @PreAuthorize("@ss.tienePermiso('Productos', 'escritura')")
    public ResponseEntity<ProductoResponse> crear(@RequestBody CrearProductoRequest request) {
        return ResponseEntity.ok(productoService.crear(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("@ss.tienePermiso('Productos', 'escritura')")
    public ResponseEntity<ProductoResponse> editar(@PathVariable Integer id,
                                                    @RequestBody EditarProductoRequest request) {
        return ResponseEntity.ok(productoService.editar(id, request));
    }

    @PatchMapping("/{id}/estado")
    @PreAuthorize("@ss.tienePermiso('Productos', 'escritura')")
    public ResponseEntity<ProductoResponse> cambiarEstado(@PathVariable Integer id) {
        return ResponseEntity.ok(productoService.cambiarEstado(id));
    }
}
