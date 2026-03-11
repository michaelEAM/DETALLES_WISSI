FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY . .

# Dar permisos de ejecución a gradlew
RUN chmod +x ./gradlew

# Construir el proyecto sin tests
RUN ./gradlew build -x test

EXPOSE 8080
CMD ["java","-jar","build/libs/app.jar"]
