FROM eclipse-temurin:23-jdk-alpine
WORKDIR /app
COPY target/authentication-service.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]