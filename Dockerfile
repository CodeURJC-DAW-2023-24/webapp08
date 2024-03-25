# Usa una imagen base de Java y Maven
FROM maven:3.8.4-openjdk-17-slim AS build

# Establece el directorio de trabajo en /app
WORKDIR /backend

# Copia el archivo pom.xml al directorio de trabajo
COPY /backend/pom.xml ./pom.xml

# Descarga las dependencias de Maven (esto se hace primero para que las dependencias se almacenen en caché)
RUN mvn clean verify

# Copia el resto de la aplicación al directorio de trabajo
COPY ./backend/src ./src

# Compila la aplicación usando Maven
RUN mvn package -o -DskipTests=true

# Usa una imagen base de Java para ejecutar la aplicación compilada
FROM openjdk:17-jdk-oracle AS runtime

# Establece el directorio de trabajo en /app
WORKDIR /backend

# Copia el archivo JAR compilado desde la etapa de construcción al directorio de trabajo
COPY --from=build /backend/target/backend-0.0.1-SNAPSHOT.jar .

# Expone el puerto en el que se ejecuta la aplicación
EXPOSE 8443

# Comando para ejecutar la aplicación al iniciar el contenedor
CMD ["java", "-jar", "backend-0.0.1-SNAPSHOT.jar"]

