# Usa Java 21 JDK oficial de Eclipse Temurin
FROM eclipse-temurin:21-jdk

# Carpeta de trabajo dentro del contenedor
WORKDIR /app

# Copia todos los archivos del proyecto al contenedor
COPY . .

# Dar permisos de ejecución a gradlew
RUN chmod +x ./gradlew

# Construir el proyecto sin tests
RUN ./gradlew build -x test

# Puerto que expondrá la app
EXPOSE 8080

# Comando para ejecutar la aplicación
CMD ["java", "-jar", "build/libs/app.jar"]