package com.pharmman.backend.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.pharmman.backend.entity.Sesion;
import com.pharmman.backend.repository.ISesionRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class SesionCleanupService {

    private final ISesionRepository sesionRepository;

    /**
     * Cada 5 minutos, cierra las sesiones que llevan más de 485 minutos (8 horas + 5 min de margen)
     * sin registrar salida (token expirado o pestaña cerrada sin logout).
     * 485 min = tiempo de expiración del token (480 min = 8 horas) + margen de 5 min.
     */
    @Scheduled(fixedDelay = 5 * 60 * 1000) // cada 5 minutos
    @Transactional
    public void cerrarSesionesExpiradas() {
        LocalDateTime limite = LocalDateTime.now().minusMinutes(485);
        List<Sesion> expiradas = sesionRepository.findByEntradaBeforeAndSalidaIsNull(limite);
        if (!expiradas.isEmpty()) {
            log.info("Cerrando {} sesión(es) expirada(s) automáticamente", expiradas.size());
            expiradas.forEach(s -> s.setSalida(s.getEntrada().plusMinutes(480)));
            sesionRepository.saveAll(expiradas);
        }
    }
}
