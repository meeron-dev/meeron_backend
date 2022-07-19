# Meeron

> 회의하세요? 미론하세요!

[앱 스토어](https://apps.apple.com/kr/app/%EB%AF%B8%EB%A1%A0/id1615525092), [구글 플레이 스토어](https://play.google.com/store/apps/details?id=fourtune.meeron), [홈페이지](http://www.meeron.net/)

본 프로젝트는 수익형 앱 런칭 IT 동아리 [CMC](https://makeus.in/)(9기, 2022.01 ~ 2022.04)에서 진행한 프로젝트입니다.

<p align="center"><img src="https://user-images.githubusercontent.com/37062337/166639190-8648c314-2bbc-4dbc-bce0-3459d01ac497.png" width="180" height="400"><img src="https://user-images.githubusercontent.com/37062337/166639237-356eda88-7020-4395-a6b3-a6ad82d32068.png" width="180" height="400"><img src="https://user-images.githubusercontent.com/37062337/166639268-a552504b-88b1-48d5-aab1-bd9c728301a8.png" width="180" height="400"><img src="https://user-images.githubusercontent.com/37062337/166639275-84050bd6-b4ee-4603-88f9-cf4942a2af21.png" width="180" height="400"></p>

<p align="center"><img src="https://user-images.githubusercontent.com/37062337/166639277-6b5b2cd3-2f65-4dc6-9cda-e8a645c1a357.png" width="180" height="400"><img src="https://user-images.githubusercontent.com/37062337/166639281-83b53f75-c10e-4dfa-9f5d-dee979625d2d.png" width="180" height="400"><img src="https://user-images.githubusercontent.com/37062337/166639284-e04d4319-17f3-4ddc-82a5-f49e4982460b.png" width="180" height="400"><img src="https://user-images.githubusercontent.com/37062337/166639288-9109b8ea-25bc-4791-a9d2-449938202c4a.png" width="180" height="400"></p>

### 목차

1. [개요](#개요)
2. [팀 구성](#팀-구성)
3. [기술 스택](#기술-스택)
4. [주요 담당 기능](#주요-담당-기능)
5. [API 문서](#api-문서)
6. [아키텍처](#아키텍처)
7. [ERD](#erd)
8. [개선 사항](#개선-사항)
   1. [Elastic Beanstalk](#elastic-beanstalk)
   2. [API의 가독성과 응답 구조 개선](#api의-가독성과-응답-구조-개선)
   3. [ECS](#ecs)
9. [소감 및 느낀점](#소감-및-느낀점)

### 개요

- 기존의 캘린더(일정) 앱과 프로젝트 협업 툴을 함께 사용하면서 **회의를 관리**하는데 한계가 있었음.
- 또한, **회의와 관련된 자료들이 흩어져 있어 불편**하다는 의견이 많았음. 
- 본인의 회의 일정을 쉽게 찾을 수 있도록 캘린더를 통해 보여줌.
- 회의를 원활하게 진행할 수 있도록 회의에 맞는 아젠다와 프리리딩 자료를 한 눈에 보여줌으로써 생산성 향상 기대.

### 팀 구성

- AOS 1명
- iOS 1명
- Server 1명
- Planner 1명
- Designer 1명

### 기술 스택

- Java, Spring Boot, Spring Data JPA, Querydsl
- MySQL, Redis
- AWS, Docker, Github Action

### 주요 담당 기능

- 소셜 로그인 (카카오, 애플)
- 협업을 위한 워크스페이스, 팀 생성 기능
- 회의, 아젠다, 이슈 생성 / 삭제, 회의와 관련된 자료(파일) 업로드
- 워크스페이스 참가, 팀 참가, 회의 참가 기능

### API 문서

- https://dev.meeron.click/docs/index.html

### 아키텍처

![image](https://user-images.githubusercontent.com/37062337/179660161-0166b416-410c-4f1f-846b-a19afa7ea3ae.png)


### ERD

<img width="973" alt="image" src="https://user-images.githubusercontent.com/37062337/164975203-52471546-0070-49b6-b34c-0dc707edd100.png">

### 개선 사항

### Elastic Beanstalk

- 기존에는 하나의 EC2에 NGINX, Blue Green 배포를 위한 애플리케이션 2개가 Docker Container 위에 위치했습니다.

  <p align="center">
    <img src="https://user-images.githubusercontent.com/37062337/165519870-b94f7172-94dc-41ae-89a3-229e9e4a095a.png">
  </p>
  <p align="center">
    개선 전 프로젝트 아키텍처
  </p>

- 한정적인 EC2 자원을 효율적으로 사용하기 위해 NGINX가 담당하던 로드 밸런싱, HTTPS 리다이렉트 기능을 **AWS Elastic Load Balancer**로 대체했고, 배포 시 **Elastic Beanstalk**으로 새 EC2를 먼저 띄운 후, 정상적으로 작동되면 기존의 애플리케이션을 종료하도록 구현했습니다.

### API의 가독성과 응답 구조 개선

- 촉박한 기간과 저의 실력 부족으로 인해 하나의 자원에 대한 필드들이 뒤섞인 응답 구조로 반환했었습니다.

  <p align="center">
    <img src="https://user-images.githubusercontent.com/37062337/165523665-c7d8fb6a-a78a-4cee-a1a4-6cc16aa0e60e.png">
  </p>
  <p align="center">
    개선 전 필드들이 그룹화 되지 않은 API 응답 구조 중 하나
  </p>

- 클라이언트가 DTO를 재사용할 수 있도록 변경했으며, API 애매한 URI도 보다 직관적으로 변경했습니다. 자세한 사항은 [문서](https://dev.meeron.click/docs/index.html)에서 확인할 수 있습니다.

  <p align="center">
    <img src="https://user-images.githubusercontent.com/37062337/165524314-fcdd060c-e0c3-4247-96c8-209321e3c6a6.png">
  </p>
  <p align="center">
    개선 후 API 응답 구조
  </p>

### ECS
<p align="center">
   <img width="1227" alt="image" src="https://user-images.githubusercontent.com/37062337/164976852-ce1a61f1-4523-455f-be6f-25e5636481da.png">
</p>
<p align="center">
   이전 Elastic Beanstalk 아키텍처
</p>

- 기존에는 단일 컨테이너였지만 추후 여러 컨테이너를 효율적으로 관리하고 싶어 Elastic Beanstalk 환경에서 ECS EC2 환경으로 이전했습니다.
- ECS Fargate보다 상대적으로 요금이 저렴한 [ECS EC2 환경](#아키텍처)을 선택했습니다.
- 트래픽이 많지 않았고 프리 티어 및 요금을 아끼기 위해 개발 환경은 `t2.micro`, 운영 환경은 `t4g.small`(2022년 말 까지 무료) 스펙으로 설정했습니다.

### 소감 및 느낀점 

- 처음으로 비개발자분들과 협업 경험이었습니다. 처음에는 '어느 정도의 깊이까지 설명드려야 되지?' 라는 고민과 소통에 대한 어려움이 많았는데, [책](http://www.kyobobook.co.kr/product/detailViewKor.laf?mallGb=KOR&ejkGb=KOR&barcode=9791196918033)을 통해 저자처럼 비유적인 표현을 통해 개념을 설명하는 방식으로 소통을 진행했습니다. 
- API에 대해 가독성과 하나의 자원 조회 시 일관성있게 반환해야 클라이언트가 더 편할 수 있다는 것을 알게 되었습니다. 이후에는 화면 기획서에 의존하지 않고 '자원 중심적인 사고를 해야겠다 '라는 점을 깨달았습니다.

