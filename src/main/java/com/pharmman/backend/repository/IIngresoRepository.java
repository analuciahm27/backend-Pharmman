package com.pharmman.backend.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pharmman.backend.entity.Ingreso;

@Repository
public interface IIngresoRepository extends JpaRepository<Ingreso, Integer> {
    List<Ingreso> findAllByOrderByFechaDesc();
    List<Ingreso> findByFechaBetweenOrderByFechaDesc(LocalDateTime desde, LocalDateTime hasta);
}
