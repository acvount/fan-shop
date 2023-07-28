docker login --username=t-3369458326-1 --password cc1234567. registry.cn-beijing.aliyuncs.com
docker buildx build -t registry.cn-beijing.aliyuncs.com/acvount/game-server-log-listener-service:1.0 --platform=linux/amd64 . --push
