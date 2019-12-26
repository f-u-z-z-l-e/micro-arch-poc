FROM openjdk:8
ARG ARTIFACT_FILE
COPY "${ARTIFACT_FILE}" app.jar
ENV JAVA_OPTS="-Xmx1g"
ENV SPRING_PROFILE="docker"
ENV SPRING_LABEL="24aac9733b15691b63d58646521bc027a34e"
ENTRYPOINT ["sh","-c","java ${JAVA_OPTS} -Djava.security.egd=file:/dev/./urandom -jar -Dspring.cloud.config.label=${SPRING_LABEL} -Dspring.profiles.active=${SPRING_PROFILE} /app.jar"]