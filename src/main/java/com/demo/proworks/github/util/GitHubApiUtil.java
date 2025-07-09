package com.demo.proworks.github.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * GitHub API 호출 유틸리티
 * test 디렉터리의 GitHubApiClient와 axios 호출을 Java로 포팅
 */
@Component
public class GitHubApiUtil {
    
    private static final Logger logger = LoggerFactory.getLogger(GitHubApiUtil.class);
    
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
        
        logger.debug("GitHub API 요청: {} {}", method, fullUrl);
        
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
            result.put("status_code", responseCode);
            result.put("success", responseCode >= 200 && responseCode < 300);
            
            if (response.length() > 0) {
                try {
                    Map<String, Object> jsonResponse = objectMapper.readValue(response.toString(), Map.class);
                    result.put("data", jsonResponse);
                } catch (Exception e) {
                    result.put("data", response.toString());
                }
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
            
            logger.debug("GitHub API 응답: {} - {}", responseCode, response.length() > 100 ? response.substring(0, 100) + "..." : response.toString());
            
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
        logger.info("OAuth 액세스 토큰 교환 시작");
        
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
            result.put("status_code", responseCode);
            result.put("success", responseCode >= 200 && responseCode < 300);
            
            if (response.length() > 0) {
                @SuppressWarnings("unchecked")
                Map<String, Object> tokenResponse = objectMapper.readValue(response.toString(), Map.class);
                result.put("data", tokenResponse);
            }
            
            logger.info("OAuth 액세스 토큰 교환 완료: {}", responseCode);
            
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
        logger.info("GitHub 사용자 정보 조회");
        
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
        logger.info("GitHub 레포지토리 목록 조회");
        
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
        logger.info("GitHub 브랜치 목록 조회: {}/{}", owner, repo);
        
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
        logger.info("GitHub 브랜치 생성: {}/{} - {}", owner, repo, branchName);
        
        String endpoint = String.format("/repos/%s/%s/git/refs", owner, repo);
        
        Map<String, Object> payload = new HashMap<>();
        payload.put("ref", "refs/heads/" + branchName);
        payload.put("sha", sourceSha);
        
        Map<String, Object> response = post(endpoint, payload, accessToken);
        
        if ((Boolean) response.get("success")) {
            return response;
        } else {
            throw new Exception("GitHub 브랜치 생성 실패: " + response.get("data"));
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
        logger.info("GitHub 브랜치 삭제: {}/{} - {}", owner, repo, branchName);
        
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
        logger.info("GitHub 웹훅 생성: {}/{}", owner, repo);
        
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
        logger.info("GitHub API 사용량 확인");
        
        Map<String, Object> response = get("/rate_limit", accessToken);
        
        if ((Boolean) response.get("success")) {
            return response;
        } else {
            throw new Exception("GitHub API 사용량 확인 실패: " + response.get("data"));
        }
    }
}