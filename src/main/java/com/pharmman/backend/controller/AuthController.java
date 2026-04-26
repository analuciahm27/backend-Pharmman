package com.pharmman.backend.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pharmman.backend.dto.request.LoginRequest;
import com.pharmman.backend.dto.response.LoginResponse;
import com.pharmman.backend.security.JwtUtil;
import com.pharmman.backend.service.AuthService;
import com.pharmman.backend.service.SesionService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final SesionService sesionService;
    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @RequestBody LoginRequest request,
            HttpServletResponse response) {

        LoginResponse loginResponse = authService.login(request);

        // setea la cookie HttpOnly con el token
        Cookie cookie = new Cookie("jwt", loginResponse.getToken());
        cookie.setHttpOnly(true);
        cookie.setSecure(false);    // true en producción con HTTPS
        cookie.setPath("/");
        cookie.setMaxAge(60 * 30); // 30 minutos (tiempo de inactividad)
        response.addCookie(cookie);

        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        String token = null;
        
        // 1. Buscamos la cookie "jwt" manualmente
        if (request.getCookies() != null) {
            for (Cookie c : request.getCookies()) {
                if ("jwt".equals(c.getName())) {
                    token = c.getValue();
                    break; // Encontrado, dejamos de buscar
                }
            }
        }

        // 2. Si encontramos el token, extraemos el email y cerramos sesión
        if (token != null) {
            // CORRECCIÓN: Se agregó el ; al final
            String email = jwtUtil.extractEmail(token); 
            System.out.println(">>> CERRANDO SESIÓN PARA: " + email);
            sesionService.registrarSalida(email);
        } else {
            System.out.println(">>> LOGOUT FALLIDO: No se encontró cookie jwt en la petición");
        }

        // 3. Destruimos la cookie en el navegador
        Cookie cookie = new Cookie("jwt", null);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        // Cambiamos el retorno para que acepte el Map del mensaje
        return ResponseEntity.ok(Map.of("message", "Adiós"));
    }
    @GetMapping("/me")
    public ResponseEntity<LoginResponse> getMe(Authentication authentication) {
        System.out.println("=== CONTROLLER /me === auth = " + authentication);

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String email = authentication.getName();
        LoginResponse userDetails = authService.getUsuarioActual(email);
        userDetails.setToken(null);
        return ResponseEntity.ok(userDetails);
    }
    
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Map<String, String>> handleBadCredentials() {
        return ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .body(Map.of("error", "Credenciales incorrectas"));
    }
    
}