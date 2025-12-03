# DiffLens Main Server

https://github.com/user-attachments/assets/8916618c-9827-4a0d-8cb9-5339a6f3f6e3

DiffLens Main Server는 패널 데이터 분석 플랫폼의 핵심 백엔드 서비스입니다.

## Preview

<img width="4760" height="6736" alt="image" src="https://github.com/user-attachments/assets/7cd8899e-e107-4f6d-83f8-244018cc2da2" />

### Members

<table width="50%" align="center">
    <tr>
        <td align="center"><b>LEAD/BE</b></td>
        <td align="center"><b>FE</b></td>
        <td align="center"><b>FE/DE</b></td>
        <td align="center"><b>BE</b></td>
        <td align="center"><b>AI/DATA</b></td>
    </tr>
    <tr>
        <td align="center"><img src="https://github.com/user-attachments/assets/561672fc-71f6-49d3-b826-da55d6ace0c4" /></td>
        <td align="center"><img src="https://github.com/user-attachments/assets/b95eea07-c69a-4bbf-9a8f-eccda41c410e" /></td>
        <td align="center"><img src="https://github.com/user-attachments/assets/15ac4334-9325-48f1-9cf6-0485f9cf130f"></td>
        <td align="center"><img src="https://github.com/user-attachments/assets/2572fa94-b981-46c6-9731-10c977267e16" /></td>
        <td align="center"><img src="https://github.com/user-attachments/assets/197a24c6-853c-4d63-b026-44032b27a5f1" /></td>
    </tr>
    <tr>
        <td align="center"><b><a href="https://github.com/hardwoong">박세웅</a></b></td>
        <td align="center"><b><a href="https://github.com/nyun-nye">윤예진</a></b></td>
        <td align="center"><b><a href="https://github.com/hyesngy">윤혜성</a></b></td>
        <td align="center"><b><a href="https://github.com/ggamnunq">김준용</a></b></td> 
        <td align="center"><b><a href="https://github.com/hoya04">신정호</a></b></td> 
    </tr>
</table>

## Tech Stack

- **Java 21** - 런타임
- **Spring Boot 3.5.6** - 웹 프레임워크
- **Spring Data JPA** - ORM
- **Spring Security + JWT** - 인증/인가
- **Spring WebFlux** - AI 서버 연동
- **PostgreSQL** - 데이터베이스
- **Redis** - 캐시
- **Docker** - 컨테이너

## Getting Started

### Installation

```bash
# 저장소 클론
git clone https://github.com/hansung-sw-capstone-2025-2/2025_8_A_BE.git
cd 2025_8_A_BE

# 빌드
./gradlew clean build
```

### Environment Variables

```env
# JWT
JWT_SECRET=your-secret-key-at-least-256-bits

# Admin
ADMIN_EMAIL=admin@difflens.com

# AI Server
FAST_API_URL=http://localhost:8000

# Database
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/difflens
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=your-password

# Redis
SPRING_REDIS_HOST=localhost
SPRING_REDIS_PORT=6379
```

### Run

```bash
./gradlew bootRun

# 또는
java -jar build/libs/back-end-0.0.1-SNAPSHOT.jar
```

### Docker

```bash
# 이미지 빌드
./gradlew clean build
docker build -t difflens-main .

# 컨테이너 실행
docker run -p 8080:8080 --env-file .env difflens-main
```

## Project Structure

```
2025_8_A_BE/
├── src/main/java/DiffLens/back_end/
│   ├── Application.java         # 엔트리포인트
│   ├── domain/
│   │   ├── library/             # 라이브러리 도메인
│   │   ├── members/             # 회원/인증 도메인
│   │   ├── panel/               # 패널 도메인
│   │   ├── rawData/             # 원본 데이터 도메인
│   │   └── search/              # 검색 도메인
│   └── global/
│       ├── fastapi/             # AI 서버 클라이언트
│       ├── redis/               # 캐시 설정
│       ├── security/            # Spring Security
│       └── swagger/             # API 문서
├── src/main/resources/
│   └── application.yml
├── build.gradle
└── Dockerfile
```

## Key Features

- **자연어 검색**: AI 서버 연동을 통한 자연어 쿼리 기반 패널 검색
- **지능형 차트 추천**: 데이터 특성에 맞는 최적의 시각화 차트 자동 선택
- **라이브러리 관리**: 검색 결과 저장, 분류, 태그 관리
- **집단 비교 분석**: 두 그룹 간 통계적 차이 분석 및 인사이트 도출
- **사용자 인증**: JWT 기반 인증, 로컬/OAuth2.0(Google) 로그인 지원
- **개인화 추천**: 업종 및 검색 이력 기반 맞춤형 패널 추천
- **대시보드**: 라이브러리 기반 통계 및 차트 대시보드 제공

## API Endpoints

### Search API (`/search`)

- `POST /search` - 자연어 기반 패널 검색 (AI 연동, 차트 포함)
- `POST /search/recommended/{recommendedId}` - AI 추천 검색어로 검색
- `GET /search/{searchId}/each-responses` - 개별 응답 데이터 조회 (페이징)
- `GET /search/recommended` - 맞춤 검색 추천 (AI 연동)

### Library API (`/libraries`)

- `GET /libraries` - 라이브러리 목록 조회
- `POST /libraries` - 라이브러리 생성
- `GET /libraries/{libraryId}` - 라이브러리 상세 조회
- `POST /libraries/compare` - 라이브러리 비교 분석
- `GET /libraries/{libraryId}/dashboard` - 라이브러리 대시보드

### Panel API (`/panels`)

- `GET /panels/{panelId}` - 패널 상세 조회

### Auth API (`/auth`)

- `POST /auth/signup/local` - 일반 회원가입
- `POST /auth/login/local` - 일반 로그인
- `POST /auth/login/google` - 구글 OAuth 로그인
- `POST /auth/reissue` - 토큰 재발급

## API Documentation

서버 실행 후 아래 URL에서 확인:

- **Swagger UI**: http://localhost:8080/swagger-ui/index.html

## License

이 프로젝트는 한성대학교 기업연계 SW캡스톤디자인 수업에서 진행되었습니다.
