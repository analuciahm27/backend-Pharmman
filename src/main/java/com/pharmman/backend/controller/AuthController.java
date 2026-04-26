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
import com.pharmman.backend.service.AuthService;
import com.pharmman.backend.service.SesionService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final SesionService sesionService;

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
        cookie.setMaxAge(60 * 15); // 15 minutos
        response.addCookie(cookie);

        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(HttpServletResponse response,
                                                       Authentication authentication) {
        // Registrar salida de sesión automáticamente (RF02, RF10)
        if (authentication != null && authentication.isAuthenticated()) {
            try { sesionService.registrarSalida(authentication.getName()); }
            catch (Exception ignored) {}
        }
        Cookie cookie = new Cookie("jwt", null);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        return ResponseEntity.ok(Map.of("message", "Sesión cerrada correctamente"));
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