ARG JDK_VERSION=21
FROM maven:3.9-eclipse-temurin-${JDK_VERSION} AS builder

WORKDIR /app

COPY pom.xml .

RUN mvn dependency:go-offline

COPY src ./src

RUN mvn clean install spring-boot:repackage

FROM eclipse-temurin:${JDK_VERSION}-jre-jammy AS runtime

WORKDIR /app

RUN groupadd --system spring && useradd --system --gid spring spring

COPY --from=builder /app/target/*.jar app.jar

RUN chown spring:spring /app/app.jar

USER spring

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
