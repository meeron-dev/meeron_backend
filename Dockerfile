FROM adoptopenjdk/openjdk11

ARG JAR_FILE=meeron-0.0.1-SNAPSHOT.jar
ARG JASPYT_PASSWORD=""

RUN echo $JASPYT_PASSWORD

COPY ${JAR_FILE} app.jar

ENV PROFILE=""
ENV JASPYT_PASSWORD=$JASPYT_PASSWORD

ENTRYPOINT ["java", "-Djasypt.encryptor.password=${JASPYT_PASSWORD}", "-jar", "-Dspring.profiles.active=${PROFILE}", "/app.jar"]
