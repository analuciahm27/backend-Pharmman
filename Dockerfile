# ── Etapa 1: Build ──────────────────────────────────────────────
FROM eclipse-temurin:17-jdk AS builder

WORKDIR /app

# Copiar wrapper y pom primero (aprovechar caché de capas)
COPY mvnw pom.xml ./
COPY .mvn .mvn

# Descargar dependencias (se cachean si pom.xml no cambia)
RUN ./mvnw dependency:go-offline -B

# Copiar fuentes y compilar (omitiendo tests)
COPY src ./src
RUN ./mvnw package -DskipTests -B

# ── Etapa 2: Runtime ─────────────────────────────────────────────
FROM eclipse-temurin:17-jre

WORKDIR /app

# Copiar solo el JAR generado
COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
