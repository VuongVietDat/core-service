FROM openjdk:17
COPY target/core-service-1.0.0.jar /app/core.jar
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=k8s", "/app/core.jar"]
