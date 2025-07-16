FROM openjdk:21

#ENV DB_URL=jdbc:postgresql://localhost:5432/RestfulApi
#ENV DB_USERNAME=postgres
#ENV DB_PASSWORD=root

VOLUME /tmp
EXPOSE 8080
COPY target/demo-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]