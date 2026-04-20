package com.pharmman.backend.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.pharmman.backend.dto.request.CrearUsuarioRequest;
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
            .map(u -> new UsuarioResponse(
                u.getId(),
                u.getNombre(),
                u.getApellidoPaterno(),
                u.getApellidoMaterno(),
                u.getEmail(),
                u.getRol().getNombre(),
                u.isEstado()
            ))
            .toList();
    }

    public UsuarioResponse crearUsuario(CrearUsuarioRequest request) {
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("El email ya está registrado");
        }

        Rol rol = rolRepository.findById(request.getRolId())
            .orElseThrow(() -> new RuntimeException("Rol no encontrado"));

        Usuario usuario = new Usuario();
        usuario.setNombre(request.getNombre());
        usuario.setApellidoPaterno(request.getApellidoPaterno());
        usuario.setApellidoMaterno(request.getApellidoMaterno());
        usuario.setEmail(request.getEmail());
        usuario.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        usuario.setRol(rol);

        Usuario guardado = usuarioRepository.save(usuario);

        return new UsuarioResponse(
            guardado.getId(),
            guardado.getNombre(),
            guardado.getApellidoPaterno(),
            guardado.getApellidoMaterno(),
            guardado.getEmail(),
            guardado.getRol().getNombre(),
            guardado.isEstado()
        );
    }

    public UsuarioResponse cambiarEstado(Integer id) {
        Usuario usuario = usuarioRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        usuario.setEstado(!usuario.isEstado());
        usuarioRepository.save(usuario);
        return new UsuarioResponse(
            usuario.getId(),
            usuario.getNombre(),
            usuario.getApellidoPaterno(),
            usuario.getApellidoMaterno(),
            usuario.getEmail(),
            usuario.getRol().getNombre(),
            usuario.isEstado()
        );
    }
}
