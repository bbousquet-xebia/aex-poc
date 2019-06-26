FROM maven:3.6.1-jdk-13 as build

ADD pom.xml ./pom.xml
ADD src ./src
ADD /Users/bbousquet/.m2/settings.xml /root/.m2/settings.xml
RUN mvn package -DskipTests

FROM openjdk:13-alpine
COPY --from=build ./target/aex-messaging-vertx-*-fat.jar aex-messaging-vertx.jar
VOLUME /tmp
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom -Dlog4j2.contextSelector=org.apache.logging.log4j.core.async.AsyncLoggerContextSelector","-jar","/aex-messaging-vertx.jar"]
