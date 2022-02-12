#!/usr/bin/env bash

echo "[dev-meeron] 현재 구동중인 dev 환경 애플리케이션 종료"
docker rm -f dev-meeron

echo "[dev-meeron] 현재 구동중인 dev 환경 애플리케이션 이미지 삭제"
docker rmi -f dev-meeron:latest
