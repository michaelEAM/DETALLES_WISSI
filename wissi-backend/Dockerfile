FROM openjdk:17-jdk-slim

WORKDIR /app

COPY . .

RUN ./gradlew build

COPY build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","app.jar"]