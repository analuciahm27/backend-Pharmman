package com.pharmman.backend;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.pharmman.backend.entity.Permiso;
import com.pharmman.backend.entity.Rol;
import com.pharmman.backend.entity.Usuario;
import com.pharmman.backend.repository.IPermisoRepository;
import com.pharmman.backend.repository.IRolRepository;
import com.pharmman.backend.repository.IUsuarioRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final IRolRepository rolRepository;
    private final IPermisoRepository permisoRepository;
    private final IUsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {

        if (rolRepository.count() > 0) return;

        // ==========================================
        // 1. CREAR PERMISOS
        // ==========================================
        List<Permiso> permisos = permisoRepository.saveAll(List.of(
            new Permiso(null, "ver_usuarios",        "Ver lista de usuarios",           true),
            new Permiso(null, "gestionar_usuarios",  "Crear y editar usuarios",         true),
            new Permiso(null, "ver_productos",       "Ver catálogo de productos",       true),
            new Permiso(null, "gestionar_productos", "Crear y editar productos",        true),
            new Permiso(null, "registrar_venta",     "Registrar ventas",                true),
            new Permiso(null, "ver_ventas",          "Ver historial de ventas",         true),
            new Permiso(null, "registrar_ingreso",   "Registrar ingresos de productos", true),
            new Permiso(null, "ver_ingresos",        "Ver historial de ingresos",       true),
            new Permiso(null, "ver_sesiones",        "Ver historial de sesiones",       true)
        ));

        // ==========================================
        // 2. CREAR ROLES
        // ==========================================

        // ADMIN — todos los permisos
        Rol admin = new Rol();
        admin.setNombre("ADMIN");
        admin.setDescripcion("Administrador con acceso total");
        admin.setEstado(true);
        admin.setPermisos(permisos);
        rolRepository.save(admin);

        // VENDEDOR — permisos básicos
        // el admin puede cambiarlos desde la app
        Rol vendedor = new Rol();
        vendedor.setNombre("VENDEDOR");
        vendedor.setDescripcion("Vendedor con acceso limitado");
        vendedor.setEstado(true);
        vendedor.setPermisos(List.of(
            permisos.get(2), // ver_productos
            permisos.get(4)  // registrar_venta
        ));
        rolRepository.save(vendedor);

        // ==========================================
        // 3. SOLO EL ADMIN INICIAL
        // ==========================================
        Usuario usuarioAdmin = new Usuario();
        usuarioAdmin.setNombre("Admin");
        usuarioAdmin.setApellidoPaterno("PharmMan");
        usuarioAdmin.setEmail("admin@pharmman.com");
        usuarioAdmin.setPasswordHash(passwordEncoder.encode("123456"));
        usuarioAdmin.setRol(admin);
        usuarioAdmin.setEstado(true);
        usuarioRepository.save(usuarioAdmin);

        System.out.println("==============================");
        System.out.println("✅ Sistema iniciado");
        System.out.println("Admin: admin@pharmman.com / 123456");
        System.out.println("==============================");
    }
}