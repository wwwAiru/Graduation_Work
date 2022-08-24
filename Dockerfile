FROM openjdk:18.0.2-jdk-oraclelinux7

EXPOSE 8080

COPY target/bank-1.0.jar /bank-1.0.jar

ENTRYPOINT ["java", "-jar", "/bank-1.0.jar"]