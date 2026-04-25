package com.pharmman.backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Este es el que capturará los fallos del @PreAuthorize
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> handleAccessDeniedException(AccessDeniedException ex) {
        Map<String, String> respuesta = new HashMap<>();
        respuesta.put("mensaje", "No tienes permisos suficientes para realizar esta acción.");
        respuesta.put("error", "403 Forbidden");
        
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(respuesta);
    }

    // Opcional: Captura errores de "No encontrado" (como cuando buscas un usuario que no existe)
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleRuntimeException(RuntimeException ex) {
        Map<String, String> respuesta = new HashMap<>();
        respuesta.put("mensaje", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(respuesta);
    }

    @ExceptionHandler(org.springframework.http.converter.HttpMessageNotReadableException.class)
    public ResponseEntity<?> handleEmptyBody(org.springframework.http.converter.HttpMessageNotReadableException ex) {
        Map<String, String> respuesta = new HashMap<>();
        respuesta.put("mensaje", "El cuerpo de la petición (JSON) no puede estar vacío.");
        respuesta.put("error", "Bad Request");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(respuesta);
    }
}