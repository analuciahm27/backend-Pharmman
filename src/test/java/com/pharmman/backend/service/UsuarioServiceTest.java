package com.pharmman.backend.service;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.pharmman.backend.dto.request.CrearUsuarioRequest;
import com.pharmman.backend.entity.Rol;
import com.pharmman.backend.entity.Usuario;
import com.pharmman.backend.repository.IRolRepository;
import com.pharmman.backend.repository.IUsuarioRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("UsuarioService - crearUsuario")
class UsuarioServiceTest {

    static class ResultLogger implements TestWatcher {
        @Override
        public void testSuccessful(ExtensionContext ctx) {
            System.out.printf("  ✅ PASSED  | %s%n", ctx.getDisplayName());
        }

        @Override
        public void testFailed(ExtensionContext ctx, Throwable cause) {
            System.out.printf("  ❌ FAILED  | %s%n  Causa: %s%n",
                    ctx.getDisplayName(), cause.getMessage());
        }
    }

    @Mock private IUsuarioRepository usuarioRepository;
    @Mock private IRolRepository rolRepository;
    @Mock private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioService usuarioService;

    private Rol rolVendedor;

    @BeforeEach
    void setUp() {
        rolVendedor = new Rol();
        rolVendedor.setId(1);
        rolVendedor.setNombre("VENDEDOR");
        rolVendedor.setEstado(true);
    }

    private CrearUsuarioRequest requestValido() {
        CrearUsuarioRequest r = new CrearUsuarioRequest();
        r.setNombre("Ana");
        r.setApellidoPaterno("García");
        r.setApellidoMaterno("López");
        r.setEmailPrefijo("ana");
        r.setDni("12345678");
        r.setRolId(1);
        return r;
    }

    // =========================================================================
    // ROL
    // =========================================================================
    @Nested
    @DisplayName("Rol")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @ExtendWith(UsuarioServiceTest.ResultLogger.class)
    class RolTests {

        @Test @Order(1) @DisplayName("CU-50 | Rol válido existente 'VENDEDOR' (id=1) → ✅ Usuario creado sin excepción")
        void rolValido() {
            when(usuarioRepository.existsByEmail("ana@pharmman.com")).thenReturn(false);
            when(rolRepository.findById(1)).thenReturn(Optional.of(rolVendedor));
            when(passwordEncoder.encode(anyString())).thenReturn("hash");
            when(usuarioRepository.save(any(Usuario.class))).thenAnswer(inv -> {
                Usuario u = inv.getArgument(0);
                u.setId(1);
                u.setRol(rolVendedor);
                return u;
            });

            assertThatCode(() -> usuarioService.crearUsuario(requestValido()))
                    .doesNotThrowAnyException();
        }

        @Test @Order(2) @DisplayName("CU-51 | Sin rol asignado (rolId=null) → ❌ Rol no encontrado")
        void rolNulo() {
            when(usuarioRepository.existsByEmail(anyString())).thenReturn(false);
            when(rolRepository.findById(null)).thenReturn(Optional.empty());

            CrearUsuarioRequest r = requestValido();
            r.setRolId(null);

            assertThatThrownBy(() -> usuarioService.crearUsuario(r))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("Rol no encontrado");
        }

        @Test @Order(3) @DisplayName("CU-52 | Rol con ID inexistente (id=999) → ❌ Rol no encontrado")
        void rolIdInexistente() {
            when(usuarioRepository.existsByEmail(anyString())).thenReturn(false);
            when(rolRepository.findById(999)).thenReturn(Optional.empty());

            CrearUsuarioRequest r = requestValido();
            r.setRolId(999);

            assertThatThrownBy(() -> usuarioService.crearUsuario(r))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("Rol no encontrado");
        }
    }

    // =========================================================================
    // EMAIL DUPLICADO
    // =========================================================================
    @Nested
    @DisplayName("Email - prefijo duplicado")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @ExtendWith(UsuarioServiceTest.ResultLogger.class)
    class EmailDuplicadoTests {

        @Test @Order(1) @DisplayName("CU-40 | Prefijo ya registrado 'ana' → ❌ El prefijo ya está registrado")
        void emailDuplicado() {
            when(usuarioRepository.existsByEmail("ana@pharmman.com")).thenReturn(true);

            CrearUsuarioRequest r = requestValido();
            r.setEmailPrefijo("ana");

            assertThatThrownBy(() -> usuarioService.crearUsuario(r))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("El prefijo ya está registrado");
        }
    }
}
