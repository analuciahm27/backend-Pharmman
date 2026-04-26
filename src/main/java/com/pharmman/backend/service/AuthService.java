package com.pharmman.backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.pharmman.backend.dto.request.LoginRequest;
import com.pharmman.backend.dto.response.LoginResponse;
import com.pharmman.backend.dto.response.RolPermisoResponse;
import com.pharmman.backend.entity.Usuario;
import com.pharmman.backend.repository.IRolPermisoRepository;
import com.pharmman.backend.repository.IUsuarioRepository;
import com.pharmman.backend.security.JwtUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final IUsuarioRepository usuarioRepository;
    private final IRolPermisoRepository rolPermisoRepository;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    @Autowired
    private ApplicationContext applicationContext;

    private SesionService getSesionService() {
        return applicationContext.getBean(SesionService.class);
    }

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

        // Registrar entrada de sesión automáticamente (RF01, RF10)
        getSesionService().registrarEntrada(request.getEmail());

        // obtiene permisos por módulo del rol del usuario
        List<RolPermisoResponse> permisos = rolPermisoRepository
            .findByRolId(usuario.getRol().getId())
            .stream()
            .map(rp -> new RolPermisoResponse(
                rp.getId(),
                rp.getModulo().getNombre(),
                rp.isLectura(),
                rp.isEscritura()
            ))
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
        Usuario usuario = usuarioRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        List<RolPermisoResponse> permisos = rolPermisoRepository
            .findByRolId(usuario.getRol().getId())
            .stream()
            .map(rp -> new RolPermisoResponse(
                rp.getId(),
                rp.getModulo().getNombre(),
                rp.isLectura(),
                rp.isEscritura()
            ))
            .toList();

        return new LoginResponse(
            null,  // no devuelve token
            usuario.getNombre(),
            usuario.getApellidoPaterno(),
            usuario.getEmail(),
            usuario.getRol().getNombre(),
            permisos
        );
    }
}