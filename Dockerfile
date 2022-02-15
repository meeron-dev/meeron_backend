FROM adoptopenjdk/openjdk11

ARG JAR_FILE=meeron-0.0.1-SNAPSHOT.jar
ARG JASYPT_MEERON_PASSWORD

COPY ${JAR_FILE} app.jar

ENV PROFILE=""
ENV JASYPT_PASSWORD=$JASYPT_MEERON_PASSWORD

ENTRYPOINT ["java", "-Djasypt.encryptor.password=${JASYPT_PASSWORD}", "-jar", "-Dspring.profiles.active=${PROFILE}", "/app.jar"]
