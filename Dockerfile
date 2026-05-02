# ---------- STAGE 1: build ----------
FROM maven:3.9.9-eclipse-temurin-21 AS build

WORKDIR /app

# Copia o pom primeiro (melhora cache)
COPY pom.xml .
RUN mvn dependency:go-offline

# Copia o restante do projeto
COPY src ./src

# Gera o jar
RUN mvn clean package -DskipTests


# ---------- STAGE 2: runtime ----------
FROM eclipse-temurin:21-jdk

WORKDIR /app

# Copia o jar gerado
COPY --from=build /app/target/*.jar app.jar

# Porta do Spring
EXPOSE 8080

# Executa a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]