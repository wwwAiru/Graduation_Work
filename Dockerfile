FROM alpine:3.16

RUN apk add openjdk11
COPY build/libs/bank.jar /bank.jar

ENTRYPOINT ["java", "-jar", "/bank.jar"]