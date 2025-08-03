package com.demo.proworks.github.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.HashMap;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * GitHub API 호출 유틸리티
 */
@Component
public class GitHubApiUtil {
    
    
    private static final String GITHUB_API_BASE_URL = "https://api.github.com";
    private static final String USER_AGENT = "ProWorks-GitHub-Integration";
    private static final int TIMEOUT_MS = 30000; // 30초
    
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * GitHub API GET 요청
     * @param endpoint API 엔드포인트
     * @param accessToken 액세스 토큰
     * @return API 응답
     * @throws Exception
     */
    public Map<String, Object> get(String endpoint, String accessToken) throws Exception {
        return makeRequest("GET", endpoint, null, accessToken);
    }
    
    /**
     * GitHub API POST 요청
     * @param endpoint API 엔드포인트
     * @param payload 요청 데이터
     * @param accessToken 액세스 토큰
     * @return API 응답
     * @throws Exception
     */
    public Map<String, Object> post(String endpoint, Map<String, Object> payload, String accessToken) throws Exception {
        return makeRequest("POST", endpoint, payload, accessToken);
    }
    
    /**
     * GitHub API PUT 요청
     * @param endpoint API 엔드포인트
     * @param payload 요청 데이터
     * @param accessToken 액세스 토큰
     * @return API 응답
     * @throws Exception
     */
    public Map<String, Object> put(String endpoint, Map<String, Object> payload, String accessToken) throws Exception {
        return makeRequest("PUT", endpoint, payload, accessToken);
    }
    
    /**
     * GitHub API DELETE 요청
     * @param endpoint API 엔드포인트
     * @param accessToken 액세스 토큰
     * @return API 응답
     * @throws Exception
     */
    public Map<String, Object> delete(String endpoint, String accessToken) throws Exception {
        return makeRequest("DELETE", endpoint, null, accessToken);
    }
    
    /**
     * GitHub API 요청 실행
     * @param method HTTP 메서드
     * @param endpoint API 엔드포인트
     * @param payload 요청 데이터
     * @param accessToken 액세스 토큰
     * @return API 응답
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    private Map<String, Object> makeRequest(String method, String endpoint, Map<String, Object> payload, String accessToken) throws Exception {
        String fullUrl = GITHUB_API_BASE_URL + endpoint;
        
        
        URL url = new URL(fullUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        
        try {
            // 요청 설정
            connection.setRequestMethod(method);
            connection.setRequestProperty("Authorization", "token " + accessToken);
            connection.setRequestProperty("Accept", "application/vnd.github.v3+json");
            connection.setRequestProperty("User-Agent", USER_AGENT);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setConnectTimeout(TIMEOUT_MS);
            connection.setReadTimeout(TIMEOUT_MS);
            
            // POST/PUT 요청인 경우 페이로드 전송
            if (("POST".equals(method) || "PUT".equals(method)) && payload != null) {
                connection.setDoOutput(true);
                try (OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream(), "UTF-8")) {
                    String jsonPayload = objectMapper.writeValueAsString(payload);
                    writer.write(jsonPayload);
                    writer.flush();
                }
            }
            
            // 응답 처리
            int responseCode = connection.getResponseCode();
            
            BufferedReader reader;
            if (responseCode >= 200 && responseCode < 300) {
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
            } else {
                reader = new BufferedReader(new InputStreamReader(connection.getErrorStream(), "UTF-8"));
            }
            
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            
            // JSON 응답 파싱
            Map<String, Object> result = new HashMap<>();
            result.put("statusCode", responseCode);  // statusCode로 통일
            result.put("success", responseCode >= 200 && responseCode < 300);
            
            // 401 에러 감지 및 표시
            if (responseCode == 401) {
                result.put("is_auth_error", true);
            }
            
            if (response.length() > 0) {
                try {
                    // JSON 응답이 배열인지 객체인지 확인하고 적절하게 파싱
                    String responseString = response.toString().trim();
                    if (responseString.startsWith("[")) {
                        // JSON 배열인 경우 (예: 브랜치 목록)
                        Object jsonResponse = objectMapper.readValue(responseString, Object.class);
                        result.put("data", jsonResponse);
                    } else {
                        // JSON 객체인 경우
                        Map<String, Object> jsonResponse = objectMapper.readValue(responseString, Map.class);
                        result.put("data", jsonResponse);
                        
                        // 401 에러의 경우 추가 정보 확인
                        if (responseCode == 401 && jsonResponse.containsKey("message")) {
                            String errorMessage = (String) jsonResponse.get("message");
                            result.put("auth_error_message", errorMessage);
                        }
                    }
                } catch (Exception e) {
                    result.put("data", response.toString());
                }
            } else {
                result.put("data", null);
            }
            
            // API 레이트 리미트 정보 추가
            String rateLimitRemaining = connection.getHeaderField("X-RateLimit-Remaining");
            String rateLimitReset = connection.getHeaderField("X-RateLimit-Reset");
            if (rateLimitRemaining != null) {
                result.put("rate_limit_remaining", Integer.parseInt(rateLimitRemaining));
            }
            if (rateLimitReset != null) {
                result.put("rate_limit_reset", Long.parseLong(rateLimitReset));
            }
            
            
            return result;
            
        } finally {
            connection.disconnect();
        }
    }
    
    /**
     * OAuth 액세스 토큰 교환
     * test 디렉터리의 auth.js 콜백 처리 로직 참고
     * @param code OAuth 인증 코드
     * @param clientId GitHub 앱 클라이언트 ID
     * @param clientSecret GitHub 앱 클라이언트 시크릿
     * @return 액세스 토큰 정보
     * @throws Exception
     */
    public Map<String, Object> exchangeCodeForToken(String code, String clientId, String clientSecret) throws Exception {
        
        URL url = new URL("https://github.com/login/oauth/access_token");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        
        try {
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("User-Agent", USER_AGENT);
            connection.setDoOutput(true);
            
            // 요청 페이로드 구성
            Map<String, Object> payload = new HashMap<>();
            payload.put("client_id", clientId);
            payload.put("client_secret", clientSecret);
            payload.put("code", code);
            
            // 요청 전송
            try (OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream(), "UTF-8")) {
                String jsonPayload = objectMapper.writeValueAsString(payload);
                writer.write(jsonPayload);
                writer.flush();
            }
            
            // 응답 처리
            int responseCode = connection.getResponseCode();
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                responseCode >= 200 && responseCode < 300 ? 
                connection.getInputStream() : connection.getErrorStream(), "UTF-8"));
            
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            
            Map<String, Object> result = new HashMap<>();
            result.put("statusCode", responseCode);  // statusCode로 통일
            result.put("success", responseCode >= 200 && responseCode < 300);
            
            if (response.length() > 0) {
                @SuppressWarnings("unchecked")
                Map<String, Object> tokenResponse = objectMapper.readValue(response.toString(), Map.class);
                result.put("data", tokenResponse);
            }
            
            
            return result;
            
        } finally {
            connection.disconnect();
        }
    }
    
    /**
     * GitHub 사용자 정보 조회
     * @param accessToken 액세스 토큰
     * @return 사용자 정보
     * @throws Exception
     */
    public Map<String, Object> getUserInfo(String accessToken) throws Exception {
        
        Map<String, Object> response = get("/user", accessToken);
        
        if ((Boolean) response.get("success")) {
            @SuppressWarnings("unchecked")
            Map<String, Object> userData = (Map<String, Object>) response.get("data");
            return userData;
        } else {
            throw new Exception("GitHub 사용자 정보 조회 실패: " + response.get("data"));
        }
    }
    
    /**
     * 사용자의 레포지토리 목록 조회
     * @param accessToken 액세스 토큰
     * @param type 레포지토리 타입 (all, owner, member)
     * @param sort 정렬 (created, updated, pushed, full_name)
     * @param perPage 페이지당 개수
     * @return 레포지토리 목록
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> getRepositories(String accessToken, String type, String sort, int perPage) throws Exception {
        
        String endpoint = String.format("/user/repos?type=%s&sort=%s&per_page=%d", type, sort, perPage);
        Map<String, Object> response = get(endpoint, accessToken);
        
        if ((Boolean) response.get("success")) {
            return response;
        } else {
            throw new Exception("GitHub 레포지토리 목록 조회 실패: " + response.get("data"));
        }
    }
    
    /**
     * 레포지토리의 브랜치 목록 조회
     * @param accessToken 액세스 토큰
     * @param owner 레포지토리 소유자
     * @param repo 레포지토리 이름
     * @return 브랜치 목록
     * @throws Exception
     */
    public Map<String, Object> getBranches(String accessToken, String owner, String repo) throws Exception {
        
        String endpoint = String.format("/repos/%s/%s/branches", owner, repo);
        Map<String, Object> response = get(endpoint, accessToken);
        
        if ((Boolean) response.get("success")) {
            return response;
        } else {
            throw new Exception("GitHub 브랜치 목록 조회 실패: " + response.get("data"));
        }
    }
    
    /**
     * 브랜치 생성
     * @param accessToken 액세스 토큰
     * @param owner 레포지토리 소유자
     * @param repo 레포지토리 이름
     * @param branchName 새 브랜치 이름
     * @param sourceSha 소스 커밋 SHA
     * @return 생성된 브랜치 정보
     * @throws Exception
     */
    public Map<String, Object> createBranch(String accessToken, String owner, String repo, String branchName, String sourceSha) throws Exception {
        
        String endpoint = String.format("/repos/%s/%s/git/refs", owner, repo);
        
        Map<String, Object> payload = new HashMap<>();
        payload.put("ref", "refs/heads/" + branchName);
        payload.put("sha", sourceSha);
        
        Map<String, Object> response = post(endpoint, payload, accessToken);
        
        if ((Boolean) response.get("success")) {
            return response;
        } else {
            // 403 권한 오류인 경우 상태 코드 정보를 포함하여 예외 발생
            String errorMessage = "GitHub 브랜치 생성 실패: " + response.get("data");
            if (response.get("status_code") != null && response.get("status_code").equals(403)) {
                errorMessage = "403: " + errorMessage;
            }
            throw new Exception(errorMessage);
        }
    }
    
    /**
     * 브랜치 삭제
     * @param accessToken 액세스 토큰
     * @param owner 레포지토리 소유자
     * @param repo 레포지토리 이름
     * @param branchName 삭제할 브랜치 이름
     * @return 삭제 결과
     * @throws Exception
     */
    public Map<String, Object> deleteBranch(String accessToken, String owner, String repo, String branchName) throws Exception {
        
        String endpoint = String.format("/repos/%s/%s/git/refs/heads/%s", owner, repo, branchName);
        Map<String, Object> response = delete(endpoint, accessToken);
        
        if ((Boolean) response.get("success")) {
            return response;
        } else {
            throw new Exception("GitHub 브랜치 삭제 실패: " + response.get("data"));
        }
    }
    
    /**
     * 웹훅 생성
     * @param accessToken 액세스 토큰
     * @param owner 레포지토리 소유자
     * @param repo 레포지토리 이름
     * @param webhookUrl 웹훅 URL
     * @param events 감지할 이벤트 목록
     * @param secret 웹훅 시크릿
     * @return 생성된 웹훅 정보
     * @throws Exception
     */
    public Map<String, Object> createWebhook(String accessToken, String owner, String repo, String webhookUrl, String[] events, String secret) throws Exception {
        
        String endpoint = String.format("/repos/%s/%s/hooks", owner, repo);
        
        Map<String, Object> config = new HashMap<>();
        config.put("url", webhookUrl);
        config.put("content_type", "json");
        config.put("insecure_ssl", "0");
        if (secret != null && !secret.isEmpty()) {
            config.put("secret", secret);
        }
        
        Map<String, Object> payload = new HashMap<>();
        payload.put("name", "web");
        payload.put("active", true);
        payload.put("events", events);
        payload.put("config", config);
        
        Map<String, Object> response = post(endpoint, payload, accessToken);
        
        if ((Boolean) response.get("success")) {
            return response;
        } else {
            throw new Exception("GitHub 웹훅 생성 실패: " + response.get("data"));
        }
    }
    
    /**
     * API 사용량 확인
     * @param accessToken 액세스 토큰
     * @return API 사용량 정보
     * @throws Exception
     */
    public Map<String, Object> getRateLimit(String accessToken) throws Exception {
        
        Map<String, Object> response = get("/rate_limit", accessToken);
        
        if ((Boolean) response.get("success")) {
            return response;
        } else {
            throw new Exception("GitHub API 사용량 확인 실패: " + response.get("data"));
        }
    }
    
    /**
     * 현재 사용자 정보 조회 (getUserInfo와 동일)
     * @param accessToken 액세스 토큰
     * @return 사용자 정보
     * @throws Exception
     */
    public Map<String, Object> getCurrentUser(String accessToken) throws Exception {
        return getUserInfo(accessToken);
    }
    
    /**
     * Installation Token 생성
     * @param installationId Installation ID
     * @return Installation Token
     * @throws Exception
     */
    public String generateInstallationToken(String installationId) throws Exception {
        
        // JWT 토큰 생성이 필요하지만 현재는 GitHubApiClient의 로직을 사용
        // TODO: JWT 생성 로직 추가 또는 GitHubApiClient 사용
        throw new Exception("Installation Token 생성 기능이 구현되지 않았습니다. GitHubApiClient를 사용하세요.");
    }
    
    /**
     * 사용자가 레포지토리의 collaborator인지 확인
     * @param accessToken 액세스 토큰
     * @param repoFullName 레포지토리 전체 이름 (owner/repo)
     * @param username GitHub 사용자명
     * @return collaborator 여부
     * @throws Exception
     */
    public boolean isCollaborator(String accessToken, String repoFullName, String username) throws Exception {
        
        String endpoint = String.format("/repos/%s/collaborators/%s", repoFullName, username);
        
        try {
            Map<String, Object> response = get(endpoint, accessToken);
            return (Boolean) response.get("success");
        } catch (Exception e) {
            // 404 또는 403 등의 경우 collaborator가 아님
            return false;
        }
    }
    
    /**
     * 레포지토리에 collaborator를 초대
     * @param accessToken 액세스 토큰
     * @param repoFullName 레포지토리 전체 이름 (owner/repo)
     * @param username GitHub 사용자명
     * @param permission 권한 레벨 (pull, push, maintain, admin)
     * @return 초대 결과
     * @throws Exception
     */
    public Map<String, Object> inviteCollaborator(String accessToken, String repoFullName, String username, String permission) throws Exception {
        
        if (accessToken != null && accessToken.length() > 8) {
        }
        
        String endpoint = String.format("/repos/%s/collaborators/%s", repoFullName, username);
        
        Map<String, Object> payload = new HashMap<>();
        payload.put("permission", permission);
        
        Map<String, Object> response = put(endpoint, payload, accessToken);
        
        
        if ((Boolean) response.get("success")) {
            
            // 상태코드 별 상세 분석
            Object statusCode = response.get("statusCode");
            if (statusCode == null) {
                // status_code로 다시 시도 (호환성)
                statusCode = response.get("status_code");
            }
            
            if (statusCode != null) {
                int code = (Integer) statusCode;
                if (code == 201) {
                } else if (code == 204) {
                } else {
                }
            } else {
            }
            
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("message", "사용자 " + username + "을(를) " + repoFullName + " 레포지토리에 초대했습니다.");
            result.put("username", username);
            result.put("repository", repoFullName);
            result.put("permission", permission);
            return result;
        } else {
            
            Object statusCode = response.get("statusCode");
            Object errorData = response.get("data");
            
            
            if (statusCode != null) {
                int code = (Integer) statusCode;
                switch (code) {
                    case 401:
                        break;
                    case 403:
                        break;
                    case 404:
                        break;
                    case 422:
                        break;
                    default:
                }
            }
            
            throw new Exception("Collaborator 초대 실패: " + response.get("data"));
        }
    }
}