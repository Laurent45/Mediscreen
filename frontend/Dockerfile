
FROM amazoncorretto:8
ARG JAR_FILE=frontend/target/frontend-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} frontend.jar
ENTRYPOINT ["java", "-jar", "/frontend.jar"]


