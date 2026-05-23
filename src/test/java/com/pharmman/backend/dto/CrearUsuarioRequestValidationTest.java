package com.pharmman.backend.dto;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;

import com.pharmman.backend.dto.request.CrearUsuarioRequest;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

@DisplayName("Validación de CrearUsuarioRequest")
@ExtendWith(CrearUsuarioRequestValidationTest.ResultLogger.class)
class CrearUsuarioRequestValidationTest {

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

    private static Validator validator;

    @BeforeAll
    static void configurarValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
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

    private Set<ConstraintViolation<CrearUsuarioRequest>> validar(CrearUsuarioRequest r) {
        return validator.validate(r);
    }

    private boolean tieneViolacionEn(Set<ConstraintViolation<CrearUsuarioRequest>> v, String campo) {
        return v.stream().anyMatch(c -> c.getPropertyPath().toString().equals(campo));
    }

    // =========================================================================
    // NOMBRE
    // =========================================================================
    @Nested
    @DisplayName("Nombre")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @ExtendWith(CrearUsuarioRequestValidationTest.ResultLogger.class)
    class NombreTests {

        @Test @Order(1) @DisplayName("CU-01 | Nombre válido 'Ana' → ✅ Válido")
        void nombreValido() {
            CrearUsuarioRequest r = requestValido();
            r.setNombre("Ana");
            assertThat(validar(r)).isEmpty();
        }

        @Test @Order(2) @DisplayName("CU-02 | Nombre con tildes y ñ 'María Ñoño' → ✅ Válido")
        void nombreConTildesYEnie() {
            CrearUsuarioRequest r = requestValido();
            r.setNombre("María Ñoño");
            assertThat(validar(r)).isEmpty();
        }

        @Test @Order(3) @DisplayName("CU-03 | Nombre con exactamente 3 caracteres 'Ana' → ✅ Límite inferior válido")
        void nombreLimiteInferiorValido() {
            CrearUsuarioRequest r = requestValido();
            r.setNombre("Ana");
            assertThat(validar(r)).isEmpty();
        }

        @Test @Order(4) @DisplayName("CU-04 | Nombre con exactamente 100 caracteres → ✅ Límite superior válido")
        void nombreLimiteSuperiorValido() {
            CrearUsuarioRequest r = requestValido();
            r.setNombre("A" + "n".repeat(98) + "a");
            assertThat(validar(r)).isEmpty();
        }

        @Test @Order(5) @DisplayName("CU-05 | Nombre con solo espacios → ❌ El nombre no debe ser espacios")
        void nombreSoloEspacios() {
            CrearUsuarioRequest r = requestValido();
            r.setNombre("   ");
            assertThat(tieneViolacionEn(validar(r), "nombre")).isTrue();
        }

        @Test @Order(6) @DisplayName("CU-06 | Nombre vacío → ❌ El nombre es obligatorio")
        void nombreVacio() {
            CrearUsuarioRequest r = requestValido();
            r.setNombre("");
            assertThat(tieneViolacionEn(validar(r), "nombre")).isTrue();
        }

        @Test @Order(7) @DisplayName("CU-07 | Nombre con exactamente 2 caracteres 'An' → ❌ Mínimo 3 caracteres")
        void nombreLimiteInferiorInvalido() {
            CrearUsuarioRequest r = requestValido();
            r.setNombre("An");
            assertThat(tieneViolacionEn(validar(r), "nombre")).isTrue();
        }

        @Test @Order(8) @DisplayName("CU-08 | Nombre con exactamente 101 caracteres (B+1) → ❌ Máximo 100 caracteres")
        void nombreLimiteSuperiorInvalido() {
            // Técnica de tres puntos: B-1=99 ✅, B=100 ✅, B+1=101 ❌
            String nombre101 = "A" + "n".repeat(99) + "a"; // longitud exacta: 101
            assertThat(nombre101.length()).isEqualTo(101);
            CrearUsuarioRequest r = requestValido();
            r.setNombre(nombre101);
            assertThat(tieneViolacionEn(validar(r), "nombre")).isTrue();
        }

        @Test @Order(9) @DisplayName("CU-09 | Nombre con números 'Ana123' → ❌ Solo letras permitidas")
        void nombreConNumeros() {
            CrearUsuarioRequest r = requestValido();
            r.setNombre("Ana123");
            assertThat(tieneViolacionEn(validar(r), "nombre")).isTrue();
        }

        @Test @Order(10) @DisplayName("CU-10 | Nombre con caracteres especiales 'Ana@#' → ❌ Solo letras permitidas")
        void nombreConCaracteresEspeciales() {
            CrearUsuarioRequest r = requestValido();
            r.setNombre("Ana@#");
            assertThat(tieneViolacionEn(validar(r), "nombre")).isTrue();
        }

        @Test @Order(11) @DisplayName("CU-11 | Nombre con solo números '123456' → ❌ Solo letras permitidas")
        void nombreSoloNumeros() {
            CrearUsuarioRequest r = requestValido();
            r.setNombre("123456");
            assertThat(tieneViolacionEn(validar(r), "nombre")).isTrue();
        }
    }

    // =========================================================================
    // APELLIDO PATERNO
    // =========================================================================
    @Nested
    @DisplayName("Apellido Paterno")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @ExtendWith(CrearUsuarioRequestValidationTest.ResultLogger.class)
    class ApellidoPaternoTests {

        @Test @Order(1) @DisplayName("CU-12 | Apellido paterno válido 'García' → ✅ Válido")
        void apellidoPaternoValido() {
            CrearUsuarioRequest r = requestValido();
            r.setApellidoPaterno("García");
            assertThat(validar(r)).isEmpty();
        }

        @Test @Order(2) @DisplayName("CU-13 | Apellido paterno con exactamente 100 caracteres → ✅ Límite superior válido")
        void apellidoPaternoLimiteSuperiorValido() {
            CrearUsuarioRequest r = requestValido();
            r.setApellidoPaterno("G" + "a".repeat(98) + "r");
            assertThat(validar(r)).isEmpty();
        }

        @Test @Order(3) @DisplayName("CU-14 | Apellido paterno con exactamente 3 caracteres 'Gar' → ✅ Límite inferior válido")
        void apellidoPaternoLimiteInferiorValido() {
            CrearUsuarioRequest r = requestValido();
            r.setApellidoPaterno("Gar");
            assertThat(validar(r)).isEmpty();
        }

        @Test @Order(4) @DisplayName("CU-15 | Apellido paterno compuesto 'De la Cruz' → ✅ Válido")
        void apellidoPaternoCompuesto() {
            CrearUsuarioRequest r = requestValido();
            r.setApellidoPaterno("De la Cruz");
            assertThat(validar(r)).isEmpty();
        }

        @Test @Order(5) @DisplayName("CU-16 | Apellido paterno vacío → ❌ El apellido paterno es obligatorio")
        void apellidoPaternoVacio() {
            CrearUsuarioRequest r = requestValido();
            r.setApellidoPaterno("");
            assertThat(tieneViolacionEn(validar(r), "apellidoPaterno")).isTrue();
        }

        @Test @Order(6) @DisplayName("CU-17 | Apellido paterno con solo espacios → ❌ El apellido paterno no debe ser espacios")
        void apellidoPaternoSoloEspacios() {
            CrearUsuarioRequest r = requestValido();
            r.setApellidoPaterno("   ");
            assertThat(tieneViolacionEn(validar(r), "apellidoPaterno")).isTrue();
        }

        @Test @Order(7) @DisplayName("CU-18 | Apellido paterno con exactamente 2 caracteres 'Ga' → ❌ Mínimo 3 caracteres")
        void apellidoPaternoLimiteInferiorInvalido() {
            CrearUsuarioRequest r = requestValido();
            r.setApellidoPaterno("Ga");
            assertThat(tieneViolacionEn(validar(r), "apellidoPaterno")).isTrue();
        }

        @Test @Order(8) @DisplayName("CU-19 | Apellido paterno con números 'García123' → ❌ Solo letras permitidas")
        void apellidoPaternoConNumeros() {
            CrearUsuarioRequest r = requestValido();
            r.setApellidoPaterno("García123");
            assertThat(tieneViolacionEn(validar(r), "apellidoPaterno")).isTrue();
        }

        @Test @Order(9) @DisplayName("CU-20 | Apellido paterno con caracteres especiales 'García@#' → ❌ Solo letras permitidas")
        void apellidoPaternoConCaracteresEspeciales() {
            CrearUsuarioRequest r = requestValido();
            r.setApellidoPaterno("García@#");
            assertThat(tieneViolacionEn(validar(r), "apellidoPaterno")).isTrue();
        }

        @Test @Order(10) @DisplayName("CU-21 | Apellido paterno con exactamente 101 caracteres (B+1) → ❌ Máximo 100 caracteres")
        void apellidoPaternoLimiteSuperiorInvalido() {
            // Técnica de tres puntos: B-1=99 ✅, B=100 ✅, B+1=101 ❌
            String apellido101 = "G" + "a".repeat(99) + "r"; // longitud exacta: 101
            assertThat(apellido101.length()).isEqualTo(101);
            CrearUsuarioRequest r = requestValido();
            r.setApellidoPaterno(apellido101);
            assertThat(tieneViolacionEn(validar(r), "apellidoPaterno")).isTrue();
        }
    }

    // =========================================================================
    // APELLIDO MATERNO
    // =========================================================================
    @Nested
    @DisplayName("Apellido Materno")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @ExtendWith(CrearUsuarioRequestValidationTest.ResultLogger.class)
    class ApellidoMaternoTests {

        @Test @Order(1) @DisplayName("CU-22 | Apellido materno válido 'López' → ✅ Válido")
        void apellidoMaternoValido() {
            CrearUsuarioRequest r = requestValido();
            r.setApellidoMaterno("López");
            assertThat(validar(r)).isEmpty();
        }

        @Test @Order(2) @DisplayName("CU-23 | Apellido materno con exactamente 3 caracteres 'Lop' → ✅ Límite inferior válido")
        void apellidoMaternoLimiteInferiorValido() {
            CrearUsuarioRequest r = requestValido();
            r.setApellidoMaterno("Lop");
            assertThat(validar(r)).isEmpty();
        }

        @Test @Order(3) @DisplayName("CU-24 | Apellido materno con exactamente 100 caracteres → ✅ Límite superior válido")
        void apellidoMaternoLimiteSuperiorValido() {
            CrearUsuarioRequest r = requestValido();
            r.setApellidoMaterno("L" + "o".repeat(98) + "z");
            assertThat(validar(r)).isEmpty();
        }

        @Test @Order(4) @DisplayName("CU-25 | Apellido materno compuesto 'De la Cruz' → ✅ Válido")
        void apellidoMaternoCompuesto() {
            CrearUsuarioRequest r = requestValido();
            r.setApellidoMaterno("De la Cruz");
            assertThat(validar(r)).isEmpty();
        }

        @Test @Order(5) @DisplayName("CU-26 | Apellido materno vacío → ❌ El apellido materno es obligatorio")
        void apellidoMaternoVacio() {
            CrearUsuarioRequest r = requestValido();
            r.setApellidoMaterno("");
            assertThat(tieneViolacionEn(validar(r), "apellidoMaterno")).isTrue();
        }

        @Test @Order(6) @DisplayName("CU-27 | Apellido materno con exactamente 2 caracteres 'Lo' → ❌ Mínimo 3 caracteres")
        void apellidoMaternoLimiteInferiorInvalido() {
            CrearUsuarioRequest r = requestValido();
            r.setApellidoMaterno("Lo");
            assertThat(tieneViolacionEn(validar(r), "apellidoMaterno")).isTrue();
        }

        @Test @Order(7) @DisplayName("CU-28 | Apellido materno con solo espacios → ❌ El apellido materno no debe ser espacios")
        void apellidoMaternoSoloEspacios() {
            CrearUsuarioRequest r = requestValido();
            r.setApellidoMaterno("   ");
            assertThat(tieneViolacionEn(validar(r), "apellidoMaterno")).isTrue();
        }

        @Test @Order(8) @DisplayName("CU-29 | Apellido materno con números 'López123' → ❌ Solo letras permitidas")
        void apellidoMaternoConNumeros() {
            CrearUsuarioRequest r = requestValido();
            r.setApellidoMaterno("López123");
            assertThat(tieneViolacionEn(validar(r), "apellidoMaterno")).isTrue();
        }

        @Test @Order(9) @DisplayName("CU-30 | Apellido materno con caracteres especiales 'López@#' → ❌ Solo letras permitidas")
        void apellidoMaternoConCaracteresEspeciales() {
            CrearUsuarioRequest r = requestValido();
            r.setApellidoMaterno("López@#");
            assertThat(tieneViolacionEn(validar(r), "apellidoMaterno")).isTrue();
        }

        @Test @Order(10) @DisplayName("CU-31 | Apellido materno con exactamente 101 caracteres (B+1) → ❌ Máximo 100 caracteres")
        void apellidoMaternoLimiteSuperiorInvalido() {
            // Técnica de tres puntos: B-1=99 ✅, B=100 ✅, B+1=101 ❌
            String apellido101 = "L" + "o".repeat(99) + "z"; // longitud exacta: 101
            assertThat(apellido101.length()).isEqualTo(101);
            CrearUsuarioRequest r = requestValido();
            r.setApellidoMaterno(apellido101);
            assertThat(tieneViolacionEn(validar(r), "apellidoMaterno")).isTrue();
        }
    }

    // =========================================================================
    // EMAIL (emailPrefijo)
    // =========================================================================
    @Nested
    @DisplayName("Email (emailPrefijo)")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @ExtendWith(CrearUsuarioRequestValidationTest.ResultLogger.class)
    class EmailPrefijoTests {

        @Test @Order(1) @DisplayName("CU-32 | Prefijo válido 'ana' → ✅ Válido")
        void prefijoSimpleValido() {
            CrearUsuarioRequest r = requestValido();
            r.setEmailPrefijo("ana");
            assertThat(validar(r)).isEmpty();
        }

        @Test @Order(2) @DisplayName("CU-33 | Prefijo con 64 caracteres → ✅ Límite superior válido")
        void prefijoLimiteSuperiorValido() {
            CrearUsuarioRequest r = requestValido();
            r.setEmailPrefijo("a".repeat(64));
            assertThat(validar(r)).isEmpty();
        }

        @Test @Order(3) @DisplayName("CU-34 | Prefijo con punto 'ana.hernandez' → ✅ Válido")
        void prefijoConPunto() {
            CrearUsuarioRequest r = requestValido();
            r.setEmailPrefijo("ana.hernandez");
            assertThat(validar(r)).isEmpty();
        }

        @Test @Order(4) @DisplayName("CU-35 | Prefijo con guión 'ana-hernandez' → ✅ Válido")
        void prefijoConGuion() {
            CrearUsuarioRequest r = requestValido();
            r.setEmailPrefijo("ana-hernandez");
            assertThat(validar(r)).isEmpty();
        }

        @Test @Order(5) @DisplayName("CU-36 | Prefijo con 65 caracteres → ❌ Máximo 64 caracteres")
        void prefijoLimiteSuperiorInvalido() {
            CrearUsuarioRequest r = requestValido();
            r.setEmailPrefijo("a".repeat(65));
            assertThat(tieneViolacionEn(validar(r), "emailPrefijo")).isTrue();
        }

        @Test @Order(6) @DisplayName("CU-37 | Prefijo vacío → ❌ El prefijo es obligatorio")
        void prefijoVacio() {
            CrearUsuarioRequest r = requestValido();
            r.setEmailPrefijo("");
            assertThat(tieneViolacionEn(validar(r), "emailPrefijo")).isTrue();
        }

        @Test @Order(7) @DisplayName("CU-38 | Prefijo con '@' → ❌ No incluya el dominio ni el carácter '@'")
        void prefijoConArroba() {
            CrearUsuarioRequest r = requestValido();
            r.setEmailPrefijo("ana@");
            assertThat(tieneViolacionEn(validar(r), "emailPrefijo")).isTrue();
        }

        @Test @Order(8) @DisplayName("CU-39 | Prefijo con caracteres inválidos 'ana#$!' → ❌ Solo alfanuméricos, puntos y guiones")
        void prefijoConCaracteresInvalidos() {
            CrearUsuarioRequest r = requestValido();
            r.setEmailPrefijo("ana#$!");
            assertThat(tieneViolacionEn(validar(r), "emailPrefijo")).isTrue();
        }

        @Test @Order(9) @DisplayName("CU-41 | Prefijo con espacios 'ana maria' → ❌ No se permiten espacios")
        void prefijoConEspacios() {
            CrearUsuarioRequest r = requestValido();
            r.setEmailPrefijo("ana maria");
            assertThat(tieneViolacionEn(validar(r), "emailPrefijo")).isTrue();
        }

        @Test @Order(10) @DisplayName("CU-42 | Prefijo que empieza con punto '.ana' → ❌ Formato de prefijo inválido")
        void prefijoEmpiezaConPunto() {
            CrearUsuarioRequest r = requestValido();
            r.setEmailPrefijo(".ana");
            assertThat(tieneViolacionEn(validar(r), "emailPrefijo")).isTrue();
        }
    }

    // =========================================================================
    // DNI
    // =========================================================================
    @Nested
    @DisplayName("DNI")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @ExtendWith(CrearUsuarioRequestValidationTest.ResultLogger.class)
    class DniValidationTests {

        @Test @Order(1) @DisplayName("CU-43 | DNI válido de 8 dígitos '12345678' → ✅ Válido")
        void dniValido() {
            CrearUsuarioRequest r = requestValido();
            r.setDni("12345678");
            assertThat(validar(r)).isEmpty();
        }

        @Test @Order(2) @DisplayName("CU-44 | DNI vacío → ❌ DNI debe tener exactamente 8 dígitos")
        void dniVacio() {
            CrearUsuarioRequest r = requestValido();
            r.setDni("");
            assertThat(tieneViolacionEn(validar(r), "dni")).isTrue();
        }

        @Test @Order(3) @DisplayName("CU-45 | DNI con 7 dígitos '1234567' → ❌ Límite inferior inválido")
        void dniSieteDigitos() {
            CrearUsuarioRequest r = requestValido();
            r.setDni("1234567");
            assertThat(tieneViolacionEn(validar(r), "dni")).isTrue();
        }

        @Test @Order(4) @DisplayName("CU-46 | DNI con 9 dígitos '123456789' → ❌ Límite superior inválido")
        void dniNueveDigitos() {
            CrearUsuarioRequest r = requestValido();
            r.setDni("123456789");
            assertThat(tieneViolacionEn(validar(r), "dni")).isTrue();
        }

        @Test @Order(5) @DisplayName("CU-47 | DNI con letras '1234ABCD' → ❌ Solo dígitos numéricos")
        void dniConLetras() {
            CrearUsuarioRequest r = requestValido();
            r.setDni("1234ABCD");
            assertThat(tieneViolacionEn(validar(r), "dni")).isTrue();
        }

        @Test @Order(6) @DisplayName("CU-48 | DNI con caracteres especiales '1234@678' → ❌ Solo dígitos numéricos")
        void dniConCaracteresEspeciales() {
            CrearUsuarioRequest r = requestValido();
            r.setDni("1234@678");
            assertThat(tieneViolacionEn(validar(r), "dni")).isTrue();
        }

        @Test @Order(7) @DisplayName("CU-49 | DNI con solo espacios → ❌ El DNI no debe ser espacios")
        void dniSoloEspacios() {
            CrearUsuarioRequest r = requestValido();
            r.setDni("        ");
            assertThat(tieneViolacionEn(validar(r), "dni")).isTrue();
        }
    }

    // =========================================================================
    // ROL
    // =========================================================================
    @Nested
    @DisplayName("Rol")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @ExtendWith(CrearUsuarioRequestValidationTest.ResultLogger.class)
    class RolTests {

        @Test @Order(1) @DisplayName("CU-50 | Rol válido existente (ID=1) → ✅ Válido")
        void rolValido() {
            CrearUsuarioRequest r = requestValido();
            r.setRolId(1);
            assertThat(validar(r)).isEmpty();
        }

        @Test @Order(2) @DisplayName("CU-51 | Sin rol asignado (null) → ❌ Rol no encontrado")
        void rolNulo() {
            CrearUsuarioRequest r = requestValido();
            r.setRolId(null);
            assertThat(tieneViolacionEn(validar(r), "rolId")).isTrue();
        }

        @Test @Order(3) @DisplayName("CU-52 | Rol con ID inexistente (999) → ✅ Pasa validación de bean (la existencia se verifica en servicio)")
        void rolIdInexistente() {
            // Bean Validation solo verifica que rolId no sea null.
            // La verificación de existencia en BD ocurre en la capa de servicio.
            CrearUsuarioRequest r = requestValido();
            r.setRolId(999);
            assertThat(tieneViolacionEn(validar(r), "rolId")).isFalse();
        }
    }
}
