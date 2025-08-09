# Pod 단일 책임 원칙에 따라 하나의 Tomcat이 아닌 별도의 Tomcat으로 띄우기로함
# 이를 위해 각각의 모듈은 각자의 Tomcat에 빌드되어 하나의 이미지로 생성
# 자바 버전 이원화를 위하여 변수로 진행(portal&platform 1.8 / talk 17)
ARG MAVEN_IMAGE=maven:3.8.6-openjdk-8
ARG TOMCAT_IMAGE=tomcat:9.0-jdk8-openjdk
# ============ Builder Stage ============
FROM ${MAVEN_IMAGE} AS builder

ARG MODULE
WORKDIR /app

# 루트 POM과 필요한 모듈만 복사
COPY pom.xml ./
COPY core core/
COPY ${MODULE}/pom.xml ${MODULE}/pom.xml
COPY ${MODULE}/src ${MODULE}/src

# 1) core 먼저 로컬 리포지토리에 설치
# -ntp : 의존성 전송 로그 비활성화
# -B   : CI 배치 모드
RUN mvn -B -ntp -f core/pom.xml clean install -DskipTests

# 2) 대상 모듈 단독 패키징
RUN mvn -B -ntp -f ${MODULE}/pom.xml clean package -DskipTests

# ============ Runtime Stage ============
# 톰캣 버전 이원화를 위하여 변수로 진행(portal&platform 1.8 / talk 17)
FROM ${TOMCAT_IMAGE} AS runtime

# RUN apt-get update && apt-get install -y vim

ARG MODULE
COPY --from=builder /app/${MODULE}/target/*.war /usr/local/tomcat/webapps/ROOT.war

ARG ENVIRONMENT
ENV SPRING_PROFILES_ACTIVE=${ENVIRONMENT}

CMD ["catalina.sh", "run"]
