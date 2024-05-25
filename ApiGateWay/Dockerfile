FROM openjdk:17-jdk-alpine
EXPOSE 5000
ARG JAR_FILE=target/ApiGateWay-0.0.1-SNAPSHOT.jar
ADD ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]