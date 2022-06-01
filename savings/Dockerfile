FROM openjdk:11
ADD savings/target/savings-docker.jar savings-docker.jar
ENTRYPOINT ["java", "-jar","savings-docker.jar"]
EXPOSE 8081
