FROM openjdk:8
ARG ARTIFACT_FILE
COPY "${ARTIFACT_FILE}" app.jar
ENV JAVA_OPTS="-Xmx1g"
ENV SPRING_PROFILE="docker"
ENTRYPOINT ["sh","-c","java ${JAVA_OPTS} -Djava.security.egd=file:/dev/./urandom -jar -Dspring.profiles.active=${SPRING_PROFILE} /app.jar"]