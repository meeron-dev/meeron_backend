#!/usr/bin/env bash
source /home/ec2-user/.bashrc

echo "[dev-meeron] 작업 디렉토리 위치 변경"
cd /home/ec2-user/app

echo "[dev-meeron] JASPYT_PASSWORD 변수 설정"
JASYPT_MEERON_PASSWORD=$JASYPT_MEERON_PASSWORD

echo "[dev-meeron] 새 배포 dev 환경 애플리케이션 build"
echo "[dev-meeron] jasypt password = $JASYPT_MEERON_PASSWORD"
docker build --build-arg JASPYT_PASSWORD=$JASYPT_MEERON_PASSWORD -t dev-meeron:latest .

echo "[dev-meeron] 새 배포 dev 환경 애플리케이션 run"
docker run -dit -p 8080:8080 -e PROFILE=dev --name dev-meeron dev-meeron
