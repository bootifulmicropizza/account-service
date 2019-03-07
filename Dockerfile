FROM openjdk:8u191-jre-alpine3.8

ADD target/account-service.jar /app/dist/account-service.jar

EXPOSE 9005

VOLUME /tmp

ENTRYPOINT java -Dspring.profiles.active=$BOOTIFUL_MICRO_PIZZA_ENV -Djava.security.egd=file:/dev/./urandom -jar /app/dist/account-service.jar
