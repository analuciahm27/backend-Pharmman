package com.pharmman.backend.security;

import com.pharmman.backend.repository.IRolPermisoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component("ss")
@RequiredArgsConstructor
public class SecurityService {

    private final IRolPermisoRepository rolPermisoRepository;

    public boolean tienePermiso(String modulo, String tipo) {
        // Obtenemos el email del token actual
        String email = org.springframework.security.core.context.SecurityContextHolder
                        .getContext().getAuthentication().getName();

        if (email == null) return false;

        return rolPermisoRepository.tienePermisoEspecifico(email, modulo, tipo);
    }
}