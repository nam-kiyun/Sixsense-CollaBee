# Multi-stage build for InsWebApp
# Stage 1: Builder
FROM maven:3-jdk-8 AS builder

# 작업 디렉토리 설정
WORKDIR /usr/local/InsWebApp

# 의존성 캐시를 위해 pom.xml 파일을 먼저 복사
COPY pom.xml .

# --- 프로젝트의 lib 폴더를 컨테이너 안으로 복사 ---
COPY lib ./lib

# Maven 의존성을 다운로드
RUN mvn dependency:go-offline

# 전체 소스코드 복사
COPY src ./src

# Maven 빌드 실행
RUN mvn clean package -DskipTests

# --------------------------------------------------

# Stage 2: Runtime
FROM tomcat:9.0-jdk8-openjdk

# 필요한 패키지 설치
RUN apt-get update && \
    apt-get install -y \
    vim \
    curl \
    && apt-get clean \
    && rm -rf /var/lib/apt/lists/*

# Tomcat 기본 webapps 삭제
RUN rm -rf /usr/local/tomcat/webapps/*

# 로컬 설정 파일을 이미지에 복사
COPY config/server.xml /usr/local/tomcat/conf/
COPY config/catalina.properties /usr/local/tomcat/conf/
COPY config/context.xml /usr/local/tomcat/conf/
COPY config/configuration /usr/local/tomcat/configuration/

# setenv.sh 파일을 Tomcat bin 폴더로 복사하고 실행 권한 부여
COPY setenv.sh /usr/local/tomcat/bin/
RUN chmod +x /usr/local/tomcat/bin/setenv.sh

# 빌드된 WAR 파일 복사
COPY --from=builder /usr/local/InsWebApp/target/InsWebApp.war /usr/local/tomcat/webapps/InsWebApp.war

# 컨테이너 시작 시 실행할 스크립트 복사
COPY entrypoint.sh /usr/local/tomcat/
RUN chmod +x /usr/local/tomcat/entrypoint.sh


# --- [수정] 아래 websquare_home 관련 라인들 모두 삭제 ---
# RUN mkdir -p /usr/local/tomcat/volume/websquare_home/config
# RUN mkdir -p /usr/local/tomcat/volume/websquare_home/license
# COPY volume/websquare_home/config/websquare.xml /usr/local/tomcat/volume/websquare_home/config/
# COPY volume/websquare_home/license/license /usr/local/tomcat/volume/websquare_home/license/


# 기타 볼륨으로 사용될 디렉토리 생성
RUN mkdir -p /usr/local/tomcat/volume/matrix_mobile_home
RUN mkdir -p /usr/local/tomcat/volume/wmatrix_home


# 포트 노출
EXPOSE 8080

# 헬스체크 추가
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:8080/ || exit 1 # 참고: 톰캣 포트가 9093이라면 여기도 9093으로 수정해야 합니다.

# 컨테이너 시작 명령
CMD ["/usr/local/tomcat/entrypoint.sh"]