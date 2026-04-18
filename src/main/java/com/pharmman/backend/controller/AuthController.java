package com.pharmman.backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pharmman.backend.dto.request.LoginRequest;
import com.pharmman.backend.dto.response.LoginResponse;
import com.pharmman.backend.entity.Usuario;
import com.pharmman.backend.service.AuthService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @RequestBody LoginRequest request,
            HttpServletResponse response) {

        LoginResponse loginResponse = authService.login(request);
        Cookie cookie = new Cookie("jwt", loginResponse.getToken());
        cookie.setHttpOnly(true);    // JavaScript no puede leerla
        cookie.setSecure(false);     // true en producción con HTTPS
        cookie.setPath("/");
        cookie.setMaxAge(60 * 15);   // 15 minutos
        response.addCookie(cookie);

        // ya no mandamos el token en el body
        loginResponse.setToken(null);
        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        // borra la cookie
        Cookie cookie = new Cookie("jwt", null);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0); // expira inmediatamente
        response.addCookie(cookie);
        return ResponseEntity.ok().build();
    }
    @GetMapping("/me")
    public ResponseEntity<LoginResponse> getMe(Authentication authentication) {
        // authentication es llenado automáticamente por Spring Security 
        // después de validar el JWT que viene en la cookie
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        String email = authentication.getName();
        LoginResponse userDetails = authService.getUsuarioActual(email);
        
        return ResponseEntity.ok(userDetails);
    }
    
}