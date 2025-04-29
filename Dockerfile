FROM openjdk:21-jdk-slim

WORKDIR /programandoconjava

COPY ./target/*.jar programandoconjava.jar

EXPOSE 3500

CMD ["java", "-jar", "programandoconjava.jar", "--spring.profiles.active=${SPRING_PROFILE}"]
