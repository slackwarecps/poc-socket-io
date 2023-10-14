FROM openjdk:17
VOLUME /tmp
EXPOSE 8080
EXPOSE 8878
ARG JAR_FILE=target/socketIO-0.0.1-SNAPSHOT.jar
ADD ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]


