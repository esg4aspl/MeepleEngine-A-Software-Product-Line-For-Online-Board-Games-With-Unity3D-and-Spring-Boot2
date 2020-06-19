FROM java:8-jdk-alpine

COPY ./target/*.jar /usr/app/server.jar

WORKDIR /usr/app

EXPOSE 8080

ENTRYPOINT java -Djava.security.edg=file:/dev/./urandom -jar -Dspring.profiles.active=$SPRING_PROFILE /usr/app/server.jar