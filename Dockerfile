FROM maven:3.5.2-jdk-8-alpine

WORKDIR /code

# Adding pom, git directory, source and assets
ADD pom.xml /code/pom.xml
ADD src /code/src
ADD assets /code/assets

# Prepare by downloading dependencies
RUN ["mvn", "dependency:resolve"]
RUN ["mvn", "verify"]

# Compile and package into a fat jar
RUN ["mvn", "package"]

EXPOSE 4567
CMD ["java", "-jar", "target/dist/Bibliothek.jar"]
