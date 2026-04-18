package com.pharmman.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.pharmman.backend.dto.request.AsignarPermisosRequest;
import com.pharmman.backend.dto.request.CrearRolRequest;
import com.pharmman.backend.entity.Permiso;
import com.pharmman.backend.entity.Rol;
import com.pharmman.backend.repository.IPermisoRepository;
import com.pharmman.backend.repository.IRolRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RolService {

    private final IRolRepository rolRepository;
    private final IPermisoRepository permisoRepository;

    public List<Rol> listarRoles() {
        return rolRepository.findAll();
    }

    public Rol crearRol(CrearRolRequest request) {
        Rol rol = new Rol();
        rol.setNombre(request.getNombre());
        rol.setDescripcion(request.getDescripcion());
        return rolRepository.save(rol);
    }

    public Rol asignarPermisos(AsignarPermisosRequest request) {
        Rol rol = rolRepository.findById(request.getRolId())
            .orElseThrow(() -> new RuntimeException("Rol no encontrado"));

        List<Permiso> permisos = permisoRepository
            .findAllById(request.getPermisoIds());

        rol.setPermisos(permisos);
        return rolRepository.save(rol);
    }

    public List<Permiso> listarPermisos() {
        return permisoRepository.findAll();
    }
}