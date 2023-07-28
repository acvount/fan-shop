docker login --username=t-3369458326-1 --password cc1234567. registry.cn-beijing.aliyuncs.com
docker buildx build -t registry.cn-beijing.aliyuncs.com/acvount/game-server-log-handle-service:1.0 --platform=linux/amd64 . --load
docker push registry.cn-beijing.aliyuncs.com/acvount/game-server-log-handle-service:1.0
docker rmi registry.cn-beijing.aliyuncs.com/acvount/game-server-log-handle-service:1.0