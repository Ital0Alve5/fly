# back/client-service/Dockerfile

# 1) Build stage
FROM maven:3.9.7-eclipse-temurin-21-alpine AS builder
WORKDIR /app

# Copia TODO o back/ (inclui pom.xml raiz + todos os módulos)
COPY . .

# Compila e instala shared + auth-service no repositório local,
# mas ignora os demais módulos que não estão em -pl
RUN mvn clean package -pl client-service -am -DskipTests

# 2) Runtime stage
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copia só o JAR gerado
COPY --from=builder /app/client-service/target/client-service-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]
