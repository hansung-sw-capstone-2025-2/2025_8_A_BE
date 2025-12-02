# server base image - java 21
FROM eclipse-temurin:21-jdk

# copy .jar file to docker
COPY ./build/libs/back-end-0.0.1-SNAPSHOT.jar app.jar

# always do command
ENTRYPOINT ["java", "-jar", "app.jar"]