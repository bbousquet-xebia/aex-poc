FROM maven:3.6.1-jdk-13 as build
ADD pom.xml ./pom.xml
ADD src ./src
RUN mvn package -DskipTests

FROM openjdk:13-alpine
COPY --from=build ./target/aex-playlist-*.jar aex-playlist.jar
VOLUME /tmp
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/aex-playlist.jar"]
