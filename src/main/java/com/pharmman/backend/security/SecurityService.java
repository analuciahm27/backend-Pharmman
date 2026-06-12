package com.pharmman.backend.security;

import org.springframework.stereotype.Component;

import com.pharmman.backend.repository.IRolPermisoRepository;

import lombok.RequiredArgsConstructor;

@Component("ss")
@RequiredArgsConstructor
public class SecurityService {

    private final IRolPermisoRepository rolPermisoRepository;

    public boolean tienePermiso(String modulo, String tipo) {
        String email = org.springframework.security.core.context.SecurityContextHolder
                        .getContext().getAuthentication().getName();

        if (email == null) return false;

        return rolPermisoRepository.tienePermisoEspecifico(email, modulo, tipo);
    }

    /**
     * Devuelve true si el usuario tiene AL MENOS UNO de los permisos indicados.
     * Uso en @PreAuthorize: @ss.tieneAlgunPermiso('Mod1','lectura','Mod2','escritura')
     * Los parámetros van en pares: modulo1, tipo1, modulo2, tipo2, ...
     */
    public boolean tieneAlgunPermiso(String... pares) {
        String email = org.springframework.security.core.context.SecurityContextHolder
                        .getContext().getAuthentication().getName();

        if (email == null || pares.length % 2 != 0) return false;

        for (int i = 0; i < pares.length; i += 2) {
            if (rolPermisoRepository.tienePermisoEspecifico(email, pares[i], pares[i + 1])) {
                return true;
            }
        }
        return false;
    }
}