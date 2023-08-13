#!/bin/sh
IP=$(ip -4 addr | awk '/inet/ {print $2}' | cut -d/ -f1 | grep -m 1 '^10\.0\.')
export DUBBO_IP_TO_BIND=$IP
export DUBBO_IP_TO_REGISTRY=$IP
exec java -jar /app.jar -Dname=game-server-log-show-service --spring.profiles.active=prod -Duser.timezone=Asia/Shanghai -Xms512m -Xmx1024m -XX:MetaspaceSize=128m -XX:MaxMetaspaceSize=512m
