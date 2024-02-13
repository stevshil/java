# Simple Java Spring Boot REST API with JPA H2 DB

This is a simple example of using Java Spring Boot with JPA to create and run an in memory H2 database.

The application can be modified by changing H2 to another database.

When launched the application creates the database and populates it with 4 records.

# Exercises with this lab

## Gradle

Create a build.gradle file using the pom.xml as the reference, or use Spring Initializr to create the Gradle file.

## Docker

1. Use a Maven container to build the JAR artefact.

   * Look at the documentation on Docker Hub to see how to perform an mvn package and create the target directory on your host system.
   * Use an openjdk with the appropriate Java version matching your Maven build to run the application instead of installing Java locally.
   * Create a 2 stage Dockerfile to compile the JAR and copy the JAR to a final Docker image that will run the application.

2. Use a Gradle container to do the same as the above Maven lab, provided you have created the build.gradle file.

3. Create a docker-compose.yml file to build and launch your container.

4. Extend 3 so that you launch a MySQL/MariaDB database.

5. Create a new application.properties file at the root of this project that will be copied into the container at the same location as the JAR file.  Try to get the app to work with MySQL. 