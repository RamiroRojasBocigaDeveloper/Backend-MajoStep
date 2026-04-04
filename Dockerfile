# Build stage
FROM maven:3.9-eclipse-temurin-17 AS builder

WORKDIR /app

# Copia pom.xml primero para aprovechar el cache de Docker
COPY pom.xml .

# Descarga dependencias al repositorio local (.m2) - se cachea si pom.xml no cambia
RUN mvn dependency:go-offline -B

# Copia el código fuente
COPY src ./src

# Compila reutilizando el .m2 ya descargado, sin limpiar el cache
# Nota: se usa 'package' en vez de 'clean package' para no borrar el .m2 cacheado
RUN mvn package -DskipTests -B -o

# Runtime stage - alpine es más liviano (~200MB menos)
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Copia el JAR compilado del stage anterior
COPY --from=builder /app/target/*.jar app.jar

# Expone el puerto que usa Render por defecto
EXPOSE 10000

# Flags de JVM optimizados para contenedores con poca RAM (Render free: 512MB)
# UseContainerSupport: detecta correctamente los límites de memoria del contenedor
# MaxRAMPercentage: usa hasta el 75% de la RAM disponible para el heap
# TieredStopAtLevel=1: reduce el tiempo de compilación JIT en el arranque
ENTRYPOINT ["java", \
    "-XX:+UseContainerSupport", \
    "-XX:MaxRAMPercentage=75.0", \
    "-XX:TieredStopAtLevel=1", \
    "-Dspring.profiles.active=prod", \
    "-jar", "app.jar"]