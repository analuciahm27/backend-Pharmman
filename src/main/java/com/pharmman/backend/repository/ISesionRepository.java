package com.pharmman.backend.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pharmman.backend.entity.Sesion;

@Repository
public interface ISesionRepository extends JpaRepository<Sesion, Integer> {
    List<Sesion> findByUsuarioIdAndSalidaIsNull(Integer usuarioId);

    List<Sesion> findAllByOrderByEntradaDesc();
    
    Optional<Sesion> findTopByUsuarioIdAndSalidaIsNullOrderByEntradaDesc(Integer usuarioId);
    
    List<Sesion> findByUsuarioIdAndEntradaBetweenOrderByEntradaDesc(Integer usuarioId, LocalDateTime desde, LocalDateTime hasta);

    List<Sesion> findByEntradaBeforeAndSalidaIsNull(LocalDateTime limite);
}