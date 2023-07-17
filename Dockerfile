FROM openjdk:11

EXPOSE 8089

COPY target/*.jar /

ENTRYPOINT ["java","-jar","gateway-service-0.0.1-SNAPSHOT.jar"]