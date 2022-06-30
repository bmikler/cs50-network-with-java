FROM openjdk:17
ADD target/restapi-0.0.1-SNAPSHOT.jar .
EXPOSE 8080
CMD java -jar restapi-0.0.1-SNAPSHOT.jar