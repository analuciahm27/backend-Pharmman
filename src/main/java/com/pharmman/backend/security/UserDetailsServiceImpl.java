package com.pharmman.backend.security;

import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.pharmman.backend.entity.Usuario;
import com.pharmman.backend.repository.IUsuarioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final IUsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + email));

        SimpleGrantedAuthority authority =
                new SimpleGrantedAuthority("ROLE_" + usuario.getRol().getNombre());

        return new org.springframework.security.core.userdetails.User(
                usuario.getEmail(),
                usuario.getPasswordHash(),
                usuario.isEstado(),   // enabled — false bloquea el login si está inactivo
                true,                 // accountNonExpired
                true,                 // credentialsNonExpired
                true,                 // accountNonLocked
                List.of(authority)
        );
    }
}