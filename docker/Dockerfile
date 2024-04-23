
FROM  node:lts-alpine3.19 as builderFront

WORKDIR /frontend
#Can put another name
COPY ../frontend .
#Not sure if theya re correclty named
RUN npm install --ng build --configuration=production --base-href="/spa/"

# Test it if the firts doent's work  npx ng build 
##Prduction mode to ensure css
#

#################################################
# Base image for the build container
#################################################
FROM  maven:3.9.6-eclipse-temurin-17 as builderBack

# Sets the working directory for commands to run
WORKDIR /gymguru

# Copies the project's dependencies
COPY ../backend/pom.xml ./pom.xml

# Downloads the project's dependencies
#RUN mvn clean verify 

# Copies the project's code
COPY ../backend/src ./src
COPY --from=builderFront ./frontend/dist ./src/main/resources/public/new
# Compiles the project
RUN mvn clean package -DskipTests=true


#################################################
# Base image for the application container
#################################################
FROM openjdk:17.0.2 AS runtime

# Sets the working directory where the JAR is located
WORKDIR /gymguru

# Copies the JAR from the build container
COPY --from=builderBack ../gymguru/target/backend-0.0.1-SNAPSHOT.jar .

# Indicates the port that the container exposes
EXPOSE 8443

# Command to run on docker run
CMD ["java", "-jar", "backend-0.0.1-SNAPSHOT.jar"]

