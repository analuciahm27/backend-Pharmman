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
        // Validaciones
        if (request.getNombre() == null || request.getNombre().trim().isEmpty()) {
            throw new RuntimeException("El nombre es obligatorio");
        }
        if (request.getApellidoPaterno() == null || request.getApellidoPaterno().trim().isEmpty()) {
            throw new RuntimeException("El apellido paterno es obligatorio");
        }
        if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
            throw new RuntimeException("El email es obligatorio");
        }
        if (request.getDni() == null || !request.getDni().matches("^\\d{8}$")) {
            throw new RuntimeException("El DNI debe tener exactamente 8 dígitos numéricos");
        }

        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("El email ya está registrado");
        }

        Rol rol = rolRepository.findById(request.getRolId())
            .orElseThrow(() -> new RuntimeException("Rol no encontrado"));

        Usuario usuario = new Usuario();
        usuario.setNombre(request.getNombre().trim());
        usuario.setApellidoPaterno(request.getApellidoPaterno().trim());
        usuario.setApellidoMaterno(request.getApellidoMaterno() != null ? request.getApellidoMaterno().trim() : null);
        usuario.setEmail(request.getEmail().trim().toLowerCase());
        usuario.setDni(request.getDni());
        // Contraseña inicial = DNI (RF: mustChangePassword)
        usuario.setPasswordHash(passwordEncoder.encode(request.getDni()));
        usuario.setMustChangePassword(true);
        usuario.setRol(rol);

        Usuario guardado = usuarioRepository.save(usuario);
        return toResponse(guardado);
    }

    public UsuarioResponse cambiarEstado(Integer id) {
        Usuario usuario = usuarioRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        usuario.setEstado(!usuario.isEstado());
        usuarioRepository.save(usuario);
        return toResponse(usuario);
    }

    public UsuarioResponse editarUsuario(Integer id, EditarUsuarioRequest request) {
        // Validaciones
        if (request.getNombre() == null || request.getNombre().trim().isEmpty()) {
            throw new RuntimeException("El nombre es obligatorio");
        }
        if (request.getApellidoPaterno() == null || request.getApellidoPaterno().trim().isEmpty()) {
            throw new RuntimeException("El apellido paterno es obligatorio");
        }
        if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
            throw new RuntimeException("El email es obligatorio");
        }

        Usuario usuario = usuarioRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!usuario.getEmail().equals(request.getEmail()) &&
            usuarioRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("El email ya está registrado");
        }

        Rol rol = rolRepository.findById(request.getRolId())
            .orElseThrow(() -> new RuntimeException("Rol no encontrado"));

        usuario.setNombre(request.getNombre().trim());
        usuario.setApellidoPaterno(request.getApellidoPaterno().trim());
        usuario.setApellidoMaterno(request.getApellidoMaterno() != null ? request.getApellidoMaterno().trim() : null);
        usuario.setEmail(request.getEmail().trim().toLowerCase());
        usuario.setRol(rol);

        return toResponse(usuarioRepository.save(usuario));
    }

    public void cambiarPassword(String email, CambiarPasswordRequest request) {
        Usuario usuario = usuarioRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!passwordEncoder.matches(request.getPasswordActual(), usuario.getPasswordHash()))
            throw new RuntimeException("La contraseña actual es incorrecta");

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
