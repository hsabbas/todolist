#Source:
#https://stackoverflow.com/questions/27767264/how-to-dockerize-a-maven-project-how-many-ways-to-accomplish-it
#
# Build stage
#
FROM eclipse-temurin:17-jdk-jammy AS build
ENV HOME=/app
ENV DATABASE_URL=jdbc:mysql://host.docker.internal:3306/todo
ENV DATABASE_USERNAME=localdev
ENV DATABASE_PASSWORD=password
RUN mkdir -p $HOME
WORKDIR $HOME
ADD . $HOME
RUN target=/root/.m2 ./mvnw -f $HOME/pom.xml clean package

#
# Package stage
#
FROM eclipse-temurin:17-jre-jammy 
ARG JAR_FILE=/app/target/*.jar
ENV JWT_SECRET=SuperSecretSecretWowfalkhalvjbvlkjhgqewroiuqew6r98756v876sdf5vsdufyhtvgjh4g5qk3hjg532k4j5hg23452k34jg5k2j34htgrfas8d76f5a8sd7f65
ENV DATABASE_URL=jdbc:mysql://host.docker.internal:3306/todo
ENV DATABASE_USERNAME=localdev
ENV DATABASE_PASSWORD=password
COPY --from=build $JAR_FILE /app/runner.jar
EXPOSE 8080
ENTRYPOINT java -jar /app/runner.jar