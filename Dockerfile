# Step 1: Use a Maven image with JDK 1.8
FROM maven:3.8.6-openjdk-8 AS builder

WORKDIR /app

COPY pom.xml .
COPY core core
COPY document document
COPY platform platform
COPY portal portal

RUN mvn clean package -DskipTests

FROM tomcat:9.0

RUN apt-get update && \
    apt-get install -y vim

COPY --from=builder /app/portal/target/*.war /usr/local/tomcat/webapps/portal.war
COPY --from=builder /app/platform/target/*.war /usr/local/tomcat/webapps/platform.war


ARG ENVIRONMENT
ENV SPRING_PROFILES_ACTIVE=${ENVIRONMENT}

CMD ["catalina.sh", "run"]


