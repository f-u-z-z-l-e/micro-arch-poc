FROM openjdk:8

COPY account-service-0.0.1.jar app.jar
ENV JAVA_OPTS="-Xmx1g"
ENTRYPOINT ["sh","-c","java ${JAVA_OPTS} -Djava.security.egd=file:/dev/./urandom -jar /app.jar"]