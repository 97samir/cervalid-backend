
# Etapa 1: compilar el backend
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /workspace
# Copiar el proyecto Maven
COPY backend/pom.xml backend/pom.xml
# Web3j necesita el artefacto del contrato durante la compilación
COPY blockchain/artifacts/ blockchain/artifacts/
WORKDIR /workspace/backend
# Copiar el código fuente
COPY backend/src/ src/
# Compilar y generar el JAR
RUN mvn -B -DskipTests package

# Etapa 2: ejecutar la aplicación
FROM eclipse-temurin:17-jre
WORKDIR /app
# Copiar el JAR generado
COPY --from=build /workspace/backend/target/*.jar app.jar
# Ejecutar como usuario sin privilegios de administrador
RUN useradd --system --uid 10001 appuser \
    && chown -R appuser:appuser /app
USER appuser
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]
