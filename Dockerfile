# ─── Etapa 1: Build con Maven ───────────────────────────────────────────────
FROM eclipse-temurin:17-jdk-alpine AS builder

WORKDIR /app

# Copiar descriptores de dependencias primero (cache de Maven)
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./

# Descargar dependencias (cacheado si pom.xml no cambia)
RUN ./mvnw dependency:go-offline -B

# Copiar código fuente y construir
COPY src ./src
RUN ./mvnw package -DskipTests -B

# ─── Etapa 2: Runtime ligero ─────────────────────────────────────────────────
FROM eclipse-temurin:17-jre-alpine AS runtime

WORKDIR /app

# Usuario sin privilegios por seguridad
RUN addgroup -S chancla && adduser -S chancla -G chancla

# Copiar el JAR generado
COPY --from=builder /app/target/*.jar app.jar

RUN chown chancla:chancla app.jar
USER chancla

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
