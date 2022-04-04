# 피사트(PieceART)

## 👓 소개

<br>

> 미술품 경매 및 조각 투자 웹사이트
<br>

미술품 옥션 사이트에서 제공하는 경매 서비스와 (주)서울옥션블루에서 운영하는 미술품 공동구매 플랫폼 SOTWO의 아이디어를 차용하여 기본 기능을 직접 구현한 미니 프로젝트입니다

##### * 프로젝트 기간: 2022.03.11 - 2022.04.01 (약 3주)
##### * 팀원: 신채연, 공인배, 이종훈
##### * 백엔드 저장소 : <a href="https://github.com/botonger/pieceart_back">click</a>
##### * 프론트엔드 저장소 : <a href="https://github.com/botonger/pieceart_front">click</a>

![Untitled_Artwork 3](https://user-images.githubusercontent.com/99788738/161419579-4c364267-f288-4938-bdab-bb00472598be.jpg)

![Untitled_Artwork 4](https://user-images.githubusercontent.com/99788738/161419585-5f9f80ad-5bfd-4c16-95d1-3180875d00f8.jpg)


<br>

## 기술 스택

🔙 프론트엔드: React, JavaScript, HTML/CSS

📲 백엔드: Java, Springboot, Gradle, MariaDB, RDS(mariaDB), Python, Swagger, JWT

📃 배포 등: AWS(EC2-linux, S3, CloudFront), SSL, HTTPS

👨‍👨‍👧  협업툴: Notion

<br>

## 🗒 설계

DB 구조(ERD)
![ERD](https://user-images.githubusercontent.com/99788738/161421512-bfe5b98e-1db3-4636-b77f-9be1e2d37f4e.png)

<a href="https://github.com/botonger/pieceart_back/blob/main/doc/api.md">API 설계</a>

<a href="https://github.com/botonger/pieceart_back/blob/main/doc/frame.md">화면 설계</a>

<a href="https://github.com/botonger/pieceart_back/blob/main/doc/crawl.md">데이터 크롤링 및 저장</a>

## 메인 서비스 / 구현 기능

- 메인페이지(스크롤, 메인 이미지 무한 변경)
- 검색페이지 : 검색어 필터(작품, 작가, 가격, 인기도, 마감일, 수익률)
- 상세페이지 : 위시리스트 추가/삭제, 응찰(현재가 미만 응찰 불가), 조각 구매(최대 남은 조각수만큼 선택 가능)
- 회원가입 / 로그인(자체 로그인-jwt 토큰, 스프링 시큐리티, 구글 로그인) / 로그아웃
- 입찰 현황(취소)
- 조각 보유 현황(취소)
- 위시리스트 현황(추가/삭제)
- 회원정보수정 (비밀번호 변경)
- 공지사항(페이지네이션, 검색어, 권리자 로그인 시 글쓰기, 수정, 삭제 버튼 나타남, 파일 첨부 포함)

<br>

## 시연영상

#### 회원가입, 로그인

https://user-images.githubusercontent.com/99788738/161420181-282d8fcf-fae2-4127-87a1-fc8dbf54b147.mov

#### 응찰, 조각구매, 마이페이지

https://user-images.githubusercontent.com/99788738/161420399-e5ffe07b-092a-469e-bb34-3ca800aca6b5.mov

#### 검색

https://user-images.githubusercontent.com/99788738/161420229-83f6d2ca-f48d-4ee9-83c5-14c72b4a5c90.mov



<br>

## 👨‍👨‍👧‍👧 협업 규칙

<a href="https://github.com/botonger/pieceart_back/blob/main/doc/rules.md">협업 규칙</a>

