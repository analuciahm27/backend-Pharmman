package com.pharmman.backend.service;

import com.pharmman.backend.dto.request.CrearRolRequest;
import com.pharmman.backend.dto.request.RolPermisoRequest;
import com.pharmman.backend.dto.response.RolPermisoResponse;
import com.pharmman.backend.entity.Modulo;
import com.pharmman.backend.entity.Rol;
import com.pharmman.backend.entity.RolPermiso;
import com.pharmman.backend.repository.IModuloRepository;
import com.pharmman.backend.repository.IRolPermisoRepository;
import com.pharmman.backend.repository.IRolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RolService {

    private final IRolRepository rolRepository;
    private final IModuloRepository moduloRepository;
    private final IRolPermisoRepository rolPermisoRepository;

    public List<Rol> listarRoles() {
        return rolRepository.findAll();
    }

    public Rol crearRol(CrearRolRequest request) {
        Rol rol = new Rol();
        rol.setNombre(request.getNombre());
        rol.setDescripcion(request.getDescripcion());
        return rolRepository.save(rol);
    }

    public List<Modulo> listarModulos() {
        return moduloRepository.findAll();
    }

    public List<RolPermisoResponse> getPermisosPorRol(Integer rolId) {
        return rolPermisoRepository.findByRolId(rolId)
            .stream()
            .map(rp -> new RolPermisoResponse(
                rp.getId(),
                rp.getModulo().getNombre(),
                rp.isLectura(),
                rp.isEscritura()
            ))
            .toList();
    }

    public RolPermiso actualizarPermiso(RolPermisoRequest request) {
        RolPermiso rp = rolPermisoRepository
            .findByRolIdAndModuloId(request.getRolId(), request.getModuloId())
            .orElse(new RolPermiso());

        Rol rol = rolRepository.findById(request.getRolId())
            .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
        Modulo modulo = moduloRepository.findById(request.getModuloId())
            .orElseThrow(() -> new RuntimeException("Módulo no encontrado"));

        rp.setRol(rol);
        rp.setModulo(modulo);
        rp.setLectura(request.isLectura());
        rp.setEscritura(request.isEscritura());

        return rolPermisoRepository.save(rp);
    }
}