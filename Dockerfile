FROM openjdk:17-jdk-slim
LABEL authors="Tobias Poll"
COPY build/libs/*SNAPSHOT.jar app.jar
ENTRYPOINT exec java $(eval echo $JAVA_OPTS) -jar app.jar