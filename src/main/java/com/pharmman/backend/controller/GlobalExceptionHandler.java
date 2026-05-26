package com.pharmman.backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Captura errores de validación (@Valid)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationException(MethodArgumentNotValidException ex) {
        String mensaje = ex.getBindingResult().getFieldErrors().stream()
            .findFirst()
            .map(FieldError::getDefaultMessage)
            .orElse("Error de validación");
        Map<String, String> respuesta = new HashMap<>();
        respuesta.put("mensaje", mensaje);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(respuesta);
    }

    // Este es el que capturará los fallos del @PreAuthorize
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> handleAccessDeniedException(AccessDeniedException ex) {
        Map<String, String> respuesta = new HashMap<>();
        respuesta.put("mensaje", "No tienes permisos suficientes para realizar esta acción.");
        respuesta.put("error", "403 Forbidden");
        
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(respuesta);
    }

    // Opcional: Captura errores de "No encontrado" (como cuando buscas un usuario que no existe)
    // NOTA: AccessDeniedException extiende RuntimeException, por eso su handler específico
    // debe declararse ANTES de este, o excluirla explícitamente aquí.
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleRuntimeException(RuntimeException ex) {
        // Dejar que AccessDeniedException la maneje su propio handler
        if (ex instanceof org.springframework.security.access.AccessDeniedException) {
            throw (org.springframework.security.access.AccessDeniedException) ex;
        }
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