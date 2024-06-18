FROM openjdk:17-jdk-alpine
COPY target/auth-service.jar .
EXPOSE 8082
ENV EUREKA_HOST=service-registry
ENTRYPOINT ["java", "-jar", "auth-service.jar"]