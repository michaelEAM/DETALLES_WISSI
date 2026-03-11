FROM openjdk:17.0.9-jdk-slim-bullseye
WORKDIR /app
COPY . .
RUN ./gradlew build -x test
EXPOSE 8080
CMD ["java","-jar","build/libs/app.jar"]
