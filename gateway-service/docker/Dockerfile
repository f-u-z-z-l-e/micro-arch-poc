FROM openjdk:8-jdk-alpine

COPY gateway-service-0.0.1.jar app.jar
ENV JAVA_OPTS="-Xmx1g"
ENV SPRING_PROFILE="docker"
ENV SPRING_LABEL="0.0.3"
ENTRYPOINT ["sh","-c","java ${JAVA_OPTS} -Djava.security.egd=file:/dev/./urandom -jar -Dspring.cloud.config.label=${SPRING_LABEL} -Dspring.profiles.active=${SPRING_PROFILE} /app.jar"]