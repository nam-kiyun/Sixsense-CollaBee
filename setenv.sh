# Docker 컨테이너 내부 경로로 수정한 JAVA_OPTS
JAVA_OPTS="$JAVA_OPTS -DWMATRIX_HOME=/usr/local/tomcat/volume/wmatrix_home"
JAVA_OPTS="$JAVA_OPTS -DWEBSQUARE_HOME=/usr/local/tomcat/volume/websquare_home"

# WHYBRID_HOME 경로도 컨테이너 내부의 일관된 경로로 수정합니다.
# (docker-compose.yml의 volume 설정과 맞춰주어야 합니다.)
JAVA_OPTS="$JAVA_OPTS -DWHYBRID_HOME=/usr/local/tomcat/volume/whybrid_home"