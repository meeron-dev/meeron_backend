#!/usr/bin/env bash

echo "[dev-meeron] 작업 디렉토리 위치 변경"
cd /home/ec2-user/app

echo "[dev-meeron] 새 배포 dev 환경 애플리케이션 build"
docker build -t dev-meeron:latest .

echo "[dev-meeron] 새 배포 dev 환경 애플리케이션 build"
docker run -dit -p 8080:8080 -e PROFILE=dev --name dev-meeron dev-meeron
