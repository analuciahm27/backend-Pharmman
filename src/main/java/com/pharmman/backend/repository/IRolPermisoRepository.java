package com.pharmman.backend.repository;

import com.pharmman.backend.entity.RolPermiso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface IRolPermisoRepository extends JpaRepository<RolPermiso, Integer> {
    List<RolPermiso> findByRolId(Integer rolId);
    Optional<RolPermiso> findByRolIdAndModuloId(Integer rolId, Integer moduloId);
}