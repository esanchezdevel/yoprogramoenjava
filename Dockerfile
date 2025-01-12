FROM openjdk:17-jdk-slim

WORKDIR /yoprogramoenjava

COPY ./target/*.jar yoprogramoenjava.jar

EXPOSE 3500

CMD ["java", "-jar", "yoprogramoenjava.jar", "--spring.profiles.active=dev"]
