FROM adoptopenjdk/openjdk11

ARG JAR_FILE=meeron-0.0.1-SNAPSHOT.jar
ARG JASPYT_PASSWORD=""

COPY ${JAR_FILE} app.jar

ENV PROFILE=""
ENV JASPYT_PASSWORD=$JASPYT_PASSWORD
ENV TZ=Asia/Seoul

ENTRYPOINT [ \
            "java", \
            "-Duser.timezone=${TZ}", \
            "-Djasypt.encryptor.password=${JASPYT_PASSWORD}", \
            "-Dspring.profiles.active=${PROFILE}", \
            "-jar", \
            "/app.jar" \
]
