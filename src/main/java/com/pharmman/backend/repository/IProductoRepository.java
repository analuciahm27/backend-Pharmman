package com.pharmman.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pharmman.backend.entity.Producto;

@Repository
public interface IProductoRepository extends JpaRepository<Producto, Integer> {
    boolean existsByCodigo(String codigo);
    List<Producto> findByNombreContainingIgnoreCaseOrCodigoContainingIgnoreCase(String nombre, String codigo);
    List<Producto> findByActivoTrue();
}
