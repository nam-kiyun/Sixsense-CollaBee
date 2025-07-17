# GitHub App Manager - Java Implementation

이 프로젝트는 Node.js로 작성된 GitHub App Manager를 ProWorks5 프레임워크 기반의 Java로 포팅한 구현체입니다.

## 구현된 기능

### 1. GitHub App 토큰 관리 (`githubapptoken`)

**주요 클래스:**
- `GitHubAppAuthUtil.java` - JWT 토큰 생성 및 private key 관리
- `GitHubApiClient.java` - GitHub API 호출 클라이언트 
- `GithubAppTokenService.java` - App Token 관리 서비스
- `GitHubAppTokenController.java` - REST API 컨트롤러

**주요 기능:**
- GitHub App JWT 토큰 생성
- Installation Token 생성 및 관리
- 프로젝트별 토큰 저장 및 갱신
- 토큰 만료 시간 관리

**API 엔드포인트:**
- `POST /github/app-token/generate` - Installation Token 생성
- `GET /github/app-token/get` - 유효한 App Token 조회
- `POST /github/app-token/refresh` - App Token 갱신
- `GET /github/app-token/status` - 서비스 상태 확인

### 2. GitHub 웹훅 관리 (`githubwebhook`)

**주요 클래스:**
- `GithubWebhookService.java` - 웹훅 관리 서비스
- `GitHubWebhookController.java` - 웹훅 REST API 컨트롤러

**주요 기능:**
- GitHub API를 통한 웹훅 생성/삭제
- 웹훅 이벤트 수신 및 처리
- 웹훅 상태 조회 및 관리
- 다양한 GitHub 이벤트 처리 (push, pull_request, create, delete, repository, ping)

**API 엔드포인트:**
- `POST /github/webhook/create` - 웹훅 생성
- `GET /github/webhook/status` - 웹훅 상태 조회
- `DELETE /github/webhook/delete` - 웹훅 삭제
- `POST /github/webhook` - 웹훅 이벤트 수신
- `GET /github/webhook/health` - 헬스 체크

### 3. 브랜치 관리 (`repobranch`)

**주요 클래스:**
- `GitHubBranchManager.java` - 브랜치 관리 유틸리티
- `GitHubBranchController.java` - 브랜치 관리 REST API 컨트롤러

**주요 기능:**
- 레포지토리 브랜치 목록 조회
- 브랜치 상세 정보 조회
- 새로운 브랜치 생성
- 기존 브랜치 삭제
- Installation Token 및 OAuth Token 지원

**API 엔드포인트:**
- `GET /github/branches/list` - 브랜치 목록 조회
- `GET /github/branches/detail` - 브랜치 상세 조회
- `POST /github/branches/create` - 브랜치 생성
- `DELETE /github/branches/delete` - 브랜치 삭제
- `GET /github/branches/status` - 서비스 상태

### 4. 기존 모듈 연동

**연동 모듈:**
- `projectrepo` - 프로젝트 레포지토리 정보 관리
- `githubapptoken` - GitHub App 토큰 저장
- `githubwebhook` - 웹훅 정보 저장
- `repobranch` - 브랜치 정보 저장

## 주요 특징

1. **Token 관리 최적화**
   - Installation Token 자동 갱신
   - OAuth Token 폴백 지원
   - 토큰 만료 시간 추적
   - DB 기반 토큰 캐싱

2. **웹훅 이벤트 처리**
   - 다양한 GitHub 이벤트 지원
   - 이벤트별 맞춤 처리 로직
   - 실패 시 로깅 및 에러 핸들링
   - 서명 검증 지원

3. **브랜치 관리**
   - 안전한 브랜치 생성/삭제
   - 기본 브랜치 보호
   - Installation Token 우선 사용
   - 상세한 브랜치 정보 제공

4. **에러 핸들링**
   - 단계별 폴백 메커니즘
   - 상세한 에러 로깅
   - 사용자 친화적 에러 메시지
   - API 응답 표준화

## 설정 방법

### 1. GitHub App 설정

```properties
# application.properties 또는 별도 설정 파일에 추가
github.app.id=your_github_app_id
github.app.private.key.path=/path/to/private-key.pem
webhook.url=https://your-domain.com/webhook
github.webhook.secret=your_webhook_secret
```

### 2. 데이터베이스 설정

기존 ProWorks5 DB 스키마를 사용하며, 다음 테이블들이 이미 생성되어 있어야 합니다:
- `GITHUB_APP_TOKENS`
- `GITHUB_WEBHOOKS` 
- `PROJECT_REPOSITORIES`
- `REPOSITORY_BRANCHES`

### 3. Private Key 설정

1. GitHub App에서 private key 다운로드
2. 서버의 안전한 위치에 저장
3. 애플리케이션에서 해당 경로 설정

## 사용 예시

### 웹훅 생성
```bash
curl -X POST "http://localhost:8080/github/webhook/create" \
  -d "projectRepoId=PROJ_REPO_001" \
  -d "repoFullName=owner/repository" \
  -d "userAccessToken=ghp_xxxxxxxxxxxx"
```

### 브랜치 생성
```bash
curl -X POST "http://localhost:8080/github/branches/create" \
  -d "projectRepoId=PROJ_REPO_001" \
  -d "branchName=feature/new-feature" \
  -d "sourceBranch=main" \
  -d "userAccessToken=ghp_xxxxxxxxxxxx"
```

### 브랜치 목록 조회
```bash
curl "http://localhost:8080/github/branches/list?projectRepoId=PROJ_REPO_001&userAccessToken=ghp_xxxxxxxxxxxx"
```

## Node.js에서 Java로의 주요 변경사항

| 기능 | Node.js 원본 | Java 포팅 |
|------|-------------|-----------|
| 데이터베이스 | Supabase | MariaDB (기존 ProWorks5) |
| 웹 프레임워크 | Express.js | ProWorks5 Framework |
| 인증 방식 | 세션 기반 | JWT + REST API |
| 데이터 모델 | JavaScript 객체 | VO 클래스 |
| 에러 처리 | try-catch | Exception + 표준화된 응답 |

## 문제 해결

### 일반적인 문제들

1. **Installation Token 생성 실패**
   - GitHub App이 해당 레포지토리에 설치되어 있는지 확인
   - App ID와 private key 경로가 올바른지 확인
   - 레포지토리 소유자 정보가 정확한지 확인

2. **웹훅 생성 실패**
   - 레포지토리에 대한 admin 권한이 있는지 확인
   - 웹훅 URL이 접근 가능한지 확인
   - GitHub App 권한 설정 확인

3. **브랜치 생성/삭제 실패**
   - 브랜치 이름이 유효한지 확인
   - 소스 브랜치가 존재하는지 확인
   - 기본 브랜치 삭제 시도가 아닌지 확인

### 로그 확인

```properties
# 디버그 로깅 활성화
logging.level.com.demo.proworks.githubapptoken=DEBUG
logging.level.com.demo.proworks.githubwebhook=DEBUG
logging.level.com.demo.proworks.repobranch=DEBUG
```

## 참고 자료

- [GitHub Apps Documentation](https://docs.github.com/en/developers/apps)
- [GitHub Webhooks Guide](https://docs.github.com/en/developers/webhooks-and-events/webhooks)
- [ProWorks5 Framework Guide](internal-documentation)
- [JWT 토큰 관리 Best Practices](https://jwt.io/introduction/)

## 구현 완료 현황

✅ **완료된 기능:**
- GitHub App JWT 토큰 생성 및 관리
- Installation Token 자동 생성 및 갱신
- 웹훅 생성, 삭제, 상태 조회
- 웹훅 이벤트 수신 및 처리
- 브랜치 목록 조회, 생성, 삭제
- REST API 컨트롤러 구현
- 에러 핸들링 및 폴백 메커니즘
- 기존 ProWorks5 모듈과의 연동

🔄 **향후 확장 가능:**
- Pull Request 관리
- Issue 관리  
- Repository 설정 관리
- 통계 및 분석 기능
- 사용자 인터페이스 (웹 UI)

## 라이선스

ProWorks5 프레임워크 라이선스를 따릅니다.
