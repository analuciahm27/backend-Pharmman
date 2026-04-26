package com.pharmman.backend.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.pharmman.backend.dto.response.SesionResponse;
import com.pharmman.backend.entity.Sesion;
import com.pharmman.backend.entity.Usuario;
import com.pharmman.backend.repository.ISesionRepository;
import com.pharmman.backend.repository.IUsuarioRepository;

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

    public SesionResponse registrarSalida(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        Sesion sesion = sesionRepository
            .findTopByUsuarioIdAndSalidaIsNullOrderByEntradaDesc(usuario.getId())
            .orElseThrow(() -> new RuntimeException("No hay sesión activa"));
        sesion.setSalida(LocalDateTime.now());
        return toResponse(sesionRepository.save(sesion));
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
