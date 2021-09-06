FROM openjdk:8-jre-alpine
ADD .mvn/wrapper/maven-wrapper.jar app.jar
EXPOSE 8761
EXPOSE 8001
EXPOSE 8002