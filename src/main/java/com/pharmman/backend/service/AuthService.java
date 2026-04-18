package com.pharmman.backend.service;

import java.util.List;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.pharmman.backend.dto.request.LoginRequest;
import com.pharmman.backend.dto.response.LoginResponse;
import com.pharmman.backend.entity.Permiso;
import com.pharmman.backend.entity.Usuario;
import com.pharmman.backend.repository.IUsuarioRepository;
import com.pharmman.backend.security.JwtUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final IUsuarioRepository usuarioRepository;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public LoginResponse login(LoginRequest request) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.getEmail(), request.getPassword()
            )
        );

        Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
            .orElseThrow();

        UserDetails userDetails =
            new org.springframework.security.core.userdetails.User(
                usuario.getEmail(), usuario.getPasswordHash(), List.of()
            );

        String token = jwtUtil.generateAccessToken(userDetails);

        List<String> permisos = usuario.getRol().getPermisos()
            .stream()
            .map(Permiso::getNombre)
            .toList();

        return new LoginResponse(
            token,
            usuario.getNombre(),
            usuario.getApellidoPaterno(),
            usuario.getEmail(),
            usuario.getRol().getNombre(),
            permisos
        );
    }
    public LoginResponse getUsuarioActual(String email) {
        // 1. Buscamos al usuario por el email (que viene del token validado)
        Usuario usuario = usuarioRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // 2. Mapeamos sus permisos (puedes refactorizar esto a un método privado si quieres)
        List<String> permisos = usuario.getRol().getPermisos()
            .stream()
            .map(Permiso::getNombre)
            .toList();

        // 3. Devolvemos el LoginResponse pero con el token en null 
        // (porque el token ya vive en la cookie del navegador)
        return new LoginResponse(
            null, 
            usuario.getNombre(),
            usuario.getApellidoPaterno(),
            usuario.getEmail(),
            usuario.getRol().getNombre(),
            permisos
        );
    }
    
}
