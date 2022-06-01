FROM openjdk:11
ADD gateway/target/gateway-docker.jar gateway-docker.jar
ENTRYPOINT ["java", "-jar","gateway-docker.jar"]
EXPOSE 8080
