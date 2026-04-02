# Build stage
FROM maven:3.9-eclipse-temurin-17 AS builder

WORKDIR /app

# Copia pom.xml
COPY pom.xml .

# Descarga dependencias
RUN mvn dependency:go-offline

# Copia el código fuente
COPY src ./src

# Compila el proyecto
RUN mvn clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Copia el JAR compilado del stage anterior
COPY --from=builder /app/target/*.jar app.jar

# Expone el puerto 8080
EXPOSE 8080

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-jar", "app.jar"]
