package com.pharmman.backend.repository;

import com.pharmman.backend.entity.RolPermiso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface IRolPermisoRepository extends JpaRepository<RolPermiso, Integer> {
    List<RolPermiso> findByRolId(Integer rolId);
    Optional<RolPermiso> findByRolIdAndModuloId(Integer rolId, Integer moduloId);

    @Query("SELECT COUNT(rp) > 0 FROM RolPermiso rp " +
       "JOIN rp.rol r " +
       "JOIN Usuario u ON u.rol.id = r.id " +
       "JOIN rp.modulo m " +
       "WHERE u.email = :email " +
       "AND UPPER(m.nombre) = UPPER(:modulo) " + // <--- Esto ignora mayúsculas/minúsculas
       "AND (" +
       "  (:tipo = 'lectura' AND rp.lectura = true) OR " +
       "  (:tipo = 'escritura' AND rp.escritura = true)" +
       ")")
    boolean tienePermisoEspecifico(@Param("email") String email, 
                                   @Param("modulo") String modulo, 
                                   @Param("tipo") String tipo);
}