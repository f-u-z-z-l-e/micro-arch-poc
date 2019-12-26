FROM openjdk:8-jdk-alpine
ARG ARTIFACT_FILE
COPY "${ARTIFACT_FILE}" app.jar
ENV JAVA_OPTS="-Xmx1g"

ENTRYPOINT ["sh","-c","java ${JAVA_OPTS} -Djava.security.egd=file:/dev/./urandom -jar /app.jar"]