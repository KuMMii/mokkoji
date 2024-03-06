# 공통 프로젝트(웹 기술) - 모꼬지(Mokkoji)

### 모꼬지 링크 : https://mokkoji.online/

<img src="https://github.com/KuMMii/mokkoji/assets/128502524/fb2d2bac-5057-4cc1-bdf2-06b1cdcc8427" width="300"/><br>

#### 💻SSAFY 10기 2학기 공통 프로젝트💻<br>
##### 우수 프로젝트 수상🎖
2024.01.03 - 2024.02.16(45일)


## 목차

- [공통 프로젝트(웹 기술) - 모꼬지(Mokkoji)](#공통-프로젝트웹-기술---모꼬지mokkoji)
    - [모꼬지 링크 : https://mokkoji.online/](#모꼬지-링크--httpsmokkojionline)
  - [목차](#목차)
  - [🐳포팅 메뉴얼](#포팅-메뉴얼)
  - [💡 프로젝트 콘셉트](#-프로젝트-콘셉트)
    - [온라인으로 만나서 오래도록 간직할 수 있는 추억 쌓기](#온라인으로-만나서-오래도록-간직할-수-있는-추억-쌓기)
    - [핵심 기능](#핵심-기능)
    - [추가 기능](#추가-기능)
  - [⚙️ 서비스 기능](#️-서비스-기능)
    - [회원](#회원)
    - [행사](#행사)
    - [결과물](#결과물)
  - [🏠 서비스 레이아웃](#-서비스-레이아웃)
    - [메인 페이지](#메인-페이지)
    - [소셜 로그인](#소셜-로그인)
    - [마이페이지](#마이페이지)
    - [행사 생성](#행사-생성)
    - [소그룹 형성](#소그룹-형성)
    - [축하 편지 쓰기](#축하-편지-쓰기)
    - [사진 찍기](#사진-찍기)
    - [기억 페이지](#기억-페이지)
    - [롤링페이퍼 생성](#롤링페이퍼-생성)
    - [포토 모자이크 생성](#포토-모자이크-생성)
    - [추억 페이지](#추억-페이지)
    - [롤링페이퍼 확인](#롤링페이퍼-확인)
    - [포토모자이크 확인](#포토모자이크-확인)
  - [🛠️ 기술 스택](#️-기술-스택)
    - [Management Tool](#management-tool)
    - [IDE](#ide)
    - [Infra](#infra)
    - [Frontend](#frontend)
    - [Backend](#backend)
  - [🛠️서비스 아키텍처](#️서비스-아키텍처)
  - [🗂설계 문서](#설계-문서)
    - [Flow Chart](#flow-chart)
    - [Mockup](#mockup)
  - [💾 ERD](#-erd)
  - [📁프로젝트 구조](#프로젝트-구조)
    - [Frontend](#frontend-1)
    - [Backend](#backend-1)
  - [📃발표자료](#발표자료)
  - [👥팀 구성원](#팀-구성원)

---

## 🐳[포팅 메뉴얼](https://lab.ssafy.com/s10-webmobile1-sub2/S10P12A401/-/blob/master/exec/mokkoji_%ED%8F%AC%ED%8C%85%EB%A7%A4%EB%89%B4%EC%96%BC.pdf?ref_type=heads)

## 💡 프로젝트 콘셉트

👨🏻‍👩🏻‍👦🏻‍👦🏻 **온라인 화상 모임 플랫폼** 👨🏻‍👩🏻‍👦🏻‍👦🏻

### 온라인으로 만나서 오래도록 간직할 수 있는 추억 쌓기

- 단순한 화상 회의 서비스에 그치지 않고, 결과물을 만들어줌으로서 하나의 추억으로 간직할 수 있는 화상 모임 플랫폼

### 핵심 기능

- 회의 주최자에게 축하 메세지(영상, 음성) 전송
- 참여자들의 축하 메세지를 기반으로 한 “롤링페이퍼” 생성
- 회의의 결과물로 “포토 모자이크” 생성

### 추가 기능

- 원하는 사람끼리 채팅 가능한 소그룹 형성 기능 제공
- 사진 찍기 기능 제공

## ⚙️ 서비스 기능

### 회원

💡 회원 관리는 기본적으로 `Spring Security`, `JWT`와 `OAUTH2`를 활용

- 회원 가입
- 로그인 / 로그아웃
- 마이페이지
- 회원 정보 수정

### 행사

💡 화상 회의는 `OpenVidu`를 활용<br>
💡 행사 별 소그룹 리스트는 `Redis`를 이용해 관리

- 호스트 메인 행사 생성 및 참여자들 입장
- 참여자들 소그룹 생성
- 회의 정보 조회
- 개인 & 소그룹 사진 촬영
- 롤링페이퍼 메시지 입력 후 저장

### 결과물

💡 사진, 음성, 영상 파일은 `S3` 에 업로드<br>
💡 사진 리스트는 `Redis Cache`를 이용해 조회 속도 개선<br>
💡 포토모자이크는 `OpenCv` 라이브러리를 활용

- **기억**
  - 롤링페이퍼, 포토모자이크 편집 완료 여부 조회
  - 롤링페이퍼 배경지, 포스트잇 템플릿 설정
  - 포토모자이크/추억 썸네일용 대표이미지 설정
  - 참여자들에게 받은 사진과 로컬 사진을 추가해 포토모자이크 생성
- **추억**
  - 각 추억의 종합 정보 조회
  - 롤링페이퍼 조회
  - 포토모자이크 조회

## 🏠 서비스 레이아웃

### 메인 페이지

![mainpage](https://github.com/KuMMii/mokkoji/assets/128502524/75997cf6-1e61-43ec-8389-d74868db2d23)

### 소셜 로그인

![login](https://github.com/KuMMii/mokkoji/assets/128502524/7a71029e-03f7-48e9-8dc0-7dca548260cc)
![login-loading](https://github.com/KuMMii/mokkoji/assets/128502524/906d9166-1e3c-4750-9176-30c8d230104d)
![login-fin](https://github.com/KuMMii/mokkoji/assets/128502524/21cee82b-b959-47f2-b18c-c450b23b5b5d)

### 마이페이지

![mypage](https://github.com/KuMMii/mokkoji/assets/128502524/07732e44-0a30-4577-b566-7b6671bb852f)

### 행사 생성

![event](https://github.com/KuMMii/mokkoji/assets/128502524/bda76a40-51b9-4b64-bc28-90b15c8cb72f)

### 소그룹 형성

![subsession](https://github.com/KuMMii/mokkoji/assets/128502524/d39916cf-6a8a-4931-b96e-f5cbf3f8a20d)

### 축하 편지 쓰기

![message](https://github.com/KuMMii/mokkoji/assets/128502524/408ea222-d450-41dc-8ad1-339a98b4cd20)

### 사진 찍기

![snapshot](https://github.com/KuMMii/mokkoji/assets/128502524/e562ee94-d452-44c2-837d-66dc07e34eb4)

### 기억 페이지

![result-memory](https://github.com/KuMMii/mokkoji/assets/128502524/8ec9dd0a-36d6-4a91-8925-ec1b59d26170)

### 롤링페이퍼 생성

![rollingpaper-edit](https://github.com/KuMMii/mokkoji/assets/128502524/6af2aa45-5437-42d6-ad2a-c25e3bef71a5)

### 포토 모자이크 생성

![photomosaic-edit](https://github.com/KuMMii/mokkoji/assets/128502524/fece5d56-c20d-481b-ba01-4365c6d91678)

### 추억 페이지

![result-recollection](https://github.com/KuMMii/mokkoji/assets/128502524/bc8d824e-74d4-4190-ad12-7cf00e961933)

### 롤링페이퍼 확인

![rollingpaper-result](https://github.com/KuMMii/mokkoji/assets/128502524/bcc6aefc-c990-42a1-b50c-e40fbf59cc48)

### 포토모자이크 확인

![photomosaic-result](https://github.com/KuMMii/mokkoji/assets/128502524/a53f646c-9263-44ba-bf62-399dd9d58017)

## 🛠️ 기술 스택

### Management Tool

![gitlab](https://img.shields.io/badge/gitlab-FC6D26?style=for-the-badge&logo=gitlab&logoColor=white)
![Git](https://img.shields.io/badge/Git-F05032?style=for-the-badge&logo=git&logoColor=white)
![jira](https://img.shields.io/badge/jira-0052CC?style=for-the-badge&logo=jira&logoColor=white)
![slack](https://img.shields.io/badge/slack-4A154B?style=for-the-badge&logo=slack&logoColor=white)
![notion](https://img.shields.io/badge/notion-000000?style=for-the-badge&logo=notion&logoColor=white)
![figma](https://img.shields.io/badge/figma-F24E1E?style=for-the-badge&logo=figma&logoColor=white)

### IDE

![intellij](https://img.shields.io/badge/intellij_idea-000000?style=for-the-badge&logo=intellijidea&logoColor=white)
![vscode](https://img.shields.io/badge/vscode-0078d7?style=for-the-badge&logo=visual%20studio&logoColor=white)

### Infra

![amazonec2](https://img.shields.io/badge/amazon%20ec2-FF9900?style=for-the-badge&logo=amazonec2&logoColor=white)
![amazons3](https://img.shields.io/badge/amazon%20s3-569A31?style=for-the-badge&logo=amazons3&logoColor=white)
![nginx](https://img.shields.io/badge/nginx-009639?style=for-the-badge&logo=nginx&logoColor=white)
![docker](https://img.shields.io/badge/docker-2496ED?style=for-the-badge&logo=docker&logoColor=white)
![ubuntu](https://img.shields.io/badge/ubuntu-E95420?style=for-the-badge&logo=ubuntu&logoColor=white)

### Frontend

![vue.js](https://img.shields.io/badge/vue.js-4FC08D?style=for-the-badge&logo=Vue.js&logoColor=white)
![javascript](https://img.shields.io/badge/javascript-F0DB4F?style=for-the-badge&logo=javascript&logoColor=white)
![nodejs](https://img.shields.io/badge/nodejs-3C873A?style=for-the-badge&logo=node.js&logoColor=white)
![tailwind](https://img.shields.io/badge/tailwind-06B6D4?style=for-the-badge&logo=tailwindcss&logoColor=white)
![html](https://img.shields.io/badge/html5-E34F26?style=for-the-badge&logo=html5&logoColor=white)
![css3](https://img.shields.io/badge/css3-1572B6?style=for-the-badge&logo=css3&logoColor=white)

### Backend

![java](https://img.shields.io/badge/Java-007396?style=for-the-badge)
![springboot](https://img.shields.io/badge/spring%20boot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![springjpa](https://img.shields.io/badge/spring%20jpa-6DB33F?style=for-the-badge&logo=Spring&logoColor=white)
![springsecurity](https://img.shields.io/badge/spring%20security-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white)
![jwt](https://img.shields.io/badge/jwt-000000?style=for-the-badge&logo=jwt&logoColor=white)
![oauth](https://img.shields.io/badge/oauth-000000?style=for-the-badge&logo=oauth&logoColor=white)
![querydsl](https://img.shields.io/badge/querydsl-4285F4?style=for-the-badge&logo=querydsl&logoColor=white)
![redis](https://img.shields.io/badge/redis-DC382D?style=for-the-badge&logo=redis&logoColor=white)
![mysql](https://img.shields.io/badge/mysql-4479A1?style=for-the-badge&logo=mysql&logoColor=white)
![junit5](https://img.shields.io/badge/junit5-25A162?style=for-the-badge&logo=junit5&logoColor=white)
![postman](https://img.shields.io/badge/postman-FF6C37?style=for-the-badge&logo=postman&logoColor=white)

## 🛠️서비스 아키텍처

![service architecture](https://github.com/KuMMii/mokkoji/assets/128502524/8806caa1-058e-41cb-9d46-5f2766834935)

## 🗂설계 문서

### Flow Chart

![flow chart](https://github.com/KuMMii/mokkoji/assets/128502524/68b05ab7-c586-47d1-8ffa-79b4f0b47da5)

### Mockup

![mockup](https://github.com/KuMMii/mokkoji/assets/128502524/c8b1fe7f-02e5-4c64-a379-da8e265b3aef)

## 💾 ERD

![erd](https://github.com/KuMMii/mokkoji/assets/128502524/79f4a73f-c12a-401b-8415-643d18f06230)

## 📁프로젝트 구조

### Frontend

```
frontend
  ├── node_modules
  ├── public
  └── src
      ├── api
      ├── assets
      ├── components
      │   ├── common
      │   ├── meeting
      │   ├── modal
      │   │   ├── home
      │   │   └── meeting
      │   ├── myedit
      │   ├── myevent
      │   └── mypage
      ├── icons
      ├── router
      ├── services
      ├── store
      ├── util
      └── views
```

### Backend

```
mokkoji
├── common
│   ├── auth
│   │   ├── jwt
│   │   └── oauth2
│   ├── domain
│   └── exception
│       ├── dto
│       ├── errorcode
│       └── handler
├── config
├── event
│   ├── controller
│   ├── domain
│   ├── dto
│   │   ├── request
│   │   └── response
│   ├── repository
│   └── service
├── openvidu
│   ├── controller
│   └── dto
│       ├── request
│       └── response
├── result
│   ├── controller
│   ├── domain
│   ├── dto
│   │   ├── request
│   │   └── response
│   ├── repository
│   └── service
├── s3
└── user
    ├── controller
    ├── domain
    ├── dto
    │   ├── request
    │   └── response
    ├── repository
    └── service
```

## 📃발표자료

## 👥팀 구성원

<table align="center">
  <tr>
    <tr align="center">
        <td style="min-width: 250px;">
            <a href="https://github.com/KuMMii">
              <b>김지은</b>
            </a> 
        </td>
        <td style="min-width: 250px;">
            <a href="https://github.com/cheoljunpark">
              <b>박철준</b>
            </a>
        </td>
        <td style="min-width: 250px;">
            <a href="">
              <b>이경배</b>
            </a>
        </td>
    </tr>
    <tr align="center">
        <td style="min-width: 250px;">
              <img src="https://avatars.githubusercontent.com/u/128502524?v=4" width="100">
        </td>
        <td style="min-width: 250px;">
              <img src="https://avatars.githubusercontent.com/u/82487512?v=4" width="100">
        </td>
        <td style="min-width: 250px;">
              <img src="https://secure.gravatar.com/avatar/2c1b4340df6c333d6e31f0331798b5a8?s=800&d=identicon" width="100">
        </td>
    </tr>
    <tr align="center">
        <td>
        <b>Backend</b><br> OpenVidu, 롤링페이퍼 api 담당
        <br/>
        </td>
        <td>
        <b>Infra</b><br>UCC 담당
        <br/>
        </td>
        <td>
        <b>Backend</b><br> 소셜 로그인, 포토모자이크 api 담당
        <br/>
        </td>
    </tr>
  </tr>
  <tr>
    <tr align="center">
        <td style="min-width: 250px;">
            <a href="">
              <b>이정민</b>
            </a>
        </td>
        <td style="min-width: 250px;">
            <a href="https://github.com/chomchom96">
              <b>조용운</b>
            </a>
        </td>
        <td style="min-width: 250px;">
            <a href="https://github.com/hunteac">
              <b>한재훈</b>
            </a>
        </td>
    </tr>
    <tr align="center">
        <td style="min-width: 250px;">
              <img src="https://secure.gravatar.com/avatar/11aa9e89b7b70c7db93e39b018993c08?s=800&d=identicon" width="100">
        </td>
        <td style="min-width: 250px;">
              <img src="https://avatars.githubusercontent.com/u/112466460?v=4" width="100">
        </td>
        <td style="min-width: 250px;">
              <img src="https://avatars.githubusercontent.com/u/135293451?v=4" width="100">
        </td>
    </tr>
    <tr align="center">
        <td>
        <b>Frontend</b><br> 롤링페이퍼, 포토모자이크 담당
        <br/>
        </td>
        <td>
        <b>Frontend</b><br> 소셜 로그인, 사용자 기능 담당
        <br/>
        </td>
        <td>
        <b>Frontend</b><br> OpenVidu 담당<br>발표 담당
        <br/>
        </td>
    </tr>
  </tr>

</table>
