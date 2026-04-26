package com.pharmman.backend.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.pharmman.backend.dto.response.SesionResponse;
import com.pharmman.backend.entity.Sesion;
import com.pharmman.backend.entity.Usuario;
import com.pharmman.backend.repository.ISesionRepository;
import com.pharmman.backend.repository.IUsuarioRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SesionService {

    private final ISesionRepository sesionRepository;
    private final IUsuarioRepository usuarioRepository;

    public SesionResponse registrarEntrada(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        Sesion sesion = new Sesion();
        sesion.setUsuario(usuario);
        sesion.setEntrada(LocalDateTime.now());
        return toResponse(sesionRepository.save(sesion));
    }

    @Transactional // IMPORTANTE: Sin esto, Hibernate no hace el commit a la BD
    public void registrarSalida(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email).orElseThrow();
        // Cerramos TODAS las sesiones abiertas de este usuario para limpiar el historial
        List<Sesion> activas = sesionRepository.findByUsuarioIdAndSalidaIsNull(usuario.getId());
        for (Sesion s : activas) {
            s.setSalida(LocalDateTime.now());
        }
        sesionRepository.saveAll(activas);
    }

    public List<SesionResponse> listar() {
        return sesionRepository.findAllByOrderByEntradaDesc()
            .stream().map(this::toResponse).toList();
    }

    private SesionResponse toResponse(Sesion s) {
        return new SesionResponse(
            s.getId(), s.getEntrada(), s.getSalida(),
            s.getUsuario().getId(),
            s.getUsuario().getNombre() + " " + s.getUsuario().getApellidoPaterno(),
            s.getUsuario().getEmail(),
            s.getUsuario().getRol().getNombre()
        );
    }
}
