#!/bin/bash

# Bash 강제 실행
if [ -z "$BASH" ]; then
    exec bash "$0" "$@"
fi

set -e

echo "Starting InsWebApp with Docker..."
echo "Java Version: $(java -version 2>&1 | head -1)"
echo "Catalina Home: $CATALINA_HOME"
echo "Java Opts: $JAVA_OPTS"

# WAR 파일 자동 압축해제 대기 (필요시)
echo "Waiting for WAR file deployment..."
sleep 10

# 로그 디렉토리 확인 및 생성
if [ ! -d "$CATALINA_HOME/logs" ]; then
    mkdir -p "$CATALINA_HOME/logs"
fi

# Tomcat 시작
echo "Starting Tomcat..."
cd "$CATALINA_HOME/bin"
./startup.sh

echo "Tomcat started successfully"

# 로그 파일 대기 및 tail 실행
echo "Waiting for catalina.out..."
while [ ! -f "$CATALINA_HOME/logs/catalina.out" ]; do
    sleep 1
done

echo "Following catalina.out..."
tail -f "$CATALINA_HOME/logs/catalina.out"