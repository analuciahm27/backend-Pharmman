package com.pharmman.backend.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.pharmman.backend.dto.request.CambiarPasswordRequest;
import com.pharmman.backend.dto.request.CrearUsuarioRequest;
import com.pharmman.backend.dto.request.EditarUsuarioRequest;
import com.pharmman.backend.dto.response.UsuarioResponse;
import com.pharmman.backend.entity.Rol;
import com.pharmman.backend.entity.Usuario;
import com.pharmman.backend.repository.IRolRepository;
import com.pharmman.backend.repository.IUsuarioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private static final String DOMINIO = "@pharmman.com";

    private final IUsuarioRepository usuarioRepository;
    private final IRolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;

    public List<UsuarioResponse> listarUsuarios() {
        return usuarioRepository.findAll()
            .stream()
            .map(this::toResponse)
            .toList();
    }

    public UsuarioResponse crearUsuario(CrearUsuarioRequest request) {
        // Bean Validation ya verificó formato; aquí solo validamos reglas de negocio

        // Validar que el nombre no sea solo espacios (doble seguridad)
        if (request.getNombre().trim().isEmpty()) {
            throw new RuntimeException("El nombre no debe ser espacios");
        }
        if (request.getApellidoPaterno().trim().isEmpty()) {
            throw new RuntimeException("El apellido paterno no debe ser espacios");
        }
        if (request.getApellidoMaterno().trim().isEmpty()) {
            throw new RuntimeException("El apellido materno no debe ser espacios");
        }

        // Construir email completo a partir del prefijo
        String emailCompleto = request.getEmailPrefijo().toLowerCase() + DOMINIO;

        // Validar duplicado de prefijo
        if (usuarioRepository.existsByEmail(emailCompleto)) {
            throw new RuntimeException("El prefijo ya está registrado");
        }

        // Validar rol
        Rol rol = rolRepository.findById(request.getRolId())
            .orElseThrow(() -> new RuntimeException("Rol no encontrado"));

        Usuario usuario = new Usuario();
        usuario.setNombre(request.getNombre().trim());
        usuario.setApellidoPaterno(request.getApellidoPaterno().trim());
        usuario.setApellidoMaterno(request.getApellidoMaterno().trim());
        usuario.setEmail(emailCompleto);
        usuario.setDni(request.getDni());
        // Contraseña inicial = DNI (el usuario debe cambiarla en el primer login)
        usuario.setPasswordHash(passwordEncoder.encode(request.getDni()));
        usuario.setMustChangePassword(true);
        usuario.setRol(rol);

        return toResponse(usuarioRepository.save(usuario));
    }

    public UsuarioResponse cambiarEstado(Integer id) {
        Usuario usuario = usuarioRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        usuario.setEstado(!usuario.isEstado());
        usuarioRepository.save(usuario);
        return toResponse(usuario);
    }

    public UsuarioResponse editarUsuario(Integer id, EditarUsuarioRequest request) {
        // Validar que los campos no sean solo espacios (doble seguridad)
        if (request.getNombre().trim().isEmpty()) {
            throw new RuntimeException("El nombre no debe ser espacios");
        }
        if (request.getApellidoPaterno().trim().isEmpty()) {
            throw new RuntimeException("El apellido paterno no debe ser espacios");
        }
        if (request.getApellidoMaterno().trim().isEmpty()) {
            throw new RuntimeException("El apellido materno no debe ser espacios");
        }

        Usuario usuario = usuarioRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Construir email completo a partir del prefijo
        String emailCompleto = request.getEmailPrefijo().toLowerCase() + DOMINIO;

        // Validar duplicado solo si el prefijo cambió
        if (!usuario.getEmail().equals(emailCompleto) &&
            usuarioRepository.existsByEmail(emailCompleto)) {
            throw new RuntimeException("El prefijo ya está registrado");
        }

        // Validar rol
        Rol rol = rolRepository.findById(request.getRolId())
            .orElseThrow(() -> new RuntimeException("Rol no encontrado"));

        usuario.setNombre(request.getNombre().trim());
        usuario.setApellidoPaterno(request.getApellidoPaterno().trim());
        usuario.setApellidoMaterno(request.getApellidoMaterno().trim());
        usuario.setEmail(emailCompleto);
        usuario.setRol(rol);

        return toResponse(usuarioRepository.save(usuario));
    }

    public void cambiarPassword(String email, CambiarPasswordRequest request) {
        Usuario usuario = usuarioRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!passwordEncoder.matches(request.getPasswordActual(), usuario.getPasswordHash())) {
            throw new RuntimeException("La contraseña actual es incorrecta");
        }

        usuario.setPasswordHash(passwordEncoder.encode(request.getPasswordNueva()));
        usuario.setMustChangePassword(false);
        usuarioRepository.save(usuario);
    }

    private UsuarioResponse toResponse(Usuario u) {
        return new UsuarioResponse(
            u.getId(), u.getNombre(), u.getApellidoPaterno(), u.getApellidoMaterno(),
            u.getEmail(), u.getDni(), u.getRol().getNombre(), u.isEstado(), u.isMustChangePassword()
        );
    }
}
