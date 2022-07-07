FROM openjdk:11.0.15-slim-buster

WORKDIR /app

COPY ./target/api-rest-credit-0.0.1-SNAPSHOT.jar .

EXPOSE 8084

ENTRYPOINT ["java","-jar","api-rest-credit-0.0.1-SNAPSHOT.jar"]

