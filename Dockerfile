# Etapa de build
FROM maven:3.9.9-eclipse-temurin-21 AS build

# Defina o diretório de trabalho
WORKDIR /app

# Copie apenas o pom.xml primeiro para aproveitar o cache do Docker
COPY pom.xml .

# Baixe as dependências antes do código (melhor uso de cache)
RUN mvn dependency:go-offline

# Copie o código-fonte
COPY src ./src

# Compile e empacote a aplicação sem rodar testes
RUN mvn clean package -DskipTests

# Etapa de runtime
FROM eclipse-temurin:21-jdk-jammy

# Defina o diretório de trabalho
WORKDIR /app

# Copie o JAR gerado do estágio de build
COPY --from=build /app/target/*.jar app.jar

# Exponha a porta padrão da aplicação
EXPOSE 8081

# Comando de execução
ENTRYPOINT ["java", "-jar", "app.jar"]
