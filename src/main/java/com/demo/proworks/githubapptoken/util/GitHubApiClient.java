package com.demo.proworks.githubapptoken.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @subject     : GitHub API 호출을 위한 클라이언트 유틸리티
 * @description : GitHub API와의 통신을 담당하는 유틸리티 클래스
 * @author      : 남기윤
 * @since       : 2025/07/07
 */
@Component
public class GitHubApiClient {
    
    private static final String GITHUB_API_BASE_URL = "https://api.github.com";
    
    @Autowired
    private GitHubAppAuthUtil githubAppAuthUtil;
    
    private RestTemplate restTemplate = new RestTemplate();
    private ObjectMapper objectMapper = new ObjectMapper();
    
    /**
     * Installation Token을 생성한다.
     * 
     * @param installationId Installation ID
     * @return Installation Token
     */
    public String getInstallationToken(String installationId) throws Exception {
        String jwtToken = githubAppAuthUtil.generateJWT();
        
        String url = GITHUB_API_BASE_URL + "/app/installations/" + installationId + "/access_tokens";
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + jwtToken);
        headers.set("Accept", "application/vnd.github.v3+json");
        headers.set("User-Agent", "ProWorks5-GitHub-Manager");
        
        HttpEntity<String> entity = new HttpEntity<>("{}", headers);
        
        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, entity, Map.class);
        
        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            return (String) response.getBody().get("token");
        }
        
        throw new RuntimeException("Installation Token 생성에 실패했습니다.");
    }
    
    /**
     * 사용자의 설치된 앱 목록을 조회한다.
     * 
     * @param userAccessToken 사용자 액세스 토큰
     * @return 설치된 앱 목록
     */
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getUserInstallations(String userAccessToken) throws Exception {
        String url = GITHUB_API_BASE_URL + "/user/installations";
        
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "token " + userAccessToken);
        headers.set("Accept", "application/vnd.github.v3+json");
        headers.set("User-Agent", "ProWorks5-GitHub-Manager");
        
        HttpEntity<String> entity = new HttpEntity<>(headers);
        
        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);
        
        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            Map<String, Object> responseBody = response.getBody();
            return (List<Map<String, Object>>) responseBody.get("installations");
        }
        
        throw new RuntimeException("사용자 설치 앱 목록 조회에 실패했습니다.");
    }
    
    /**
     * Installation의 레포지토리 목록을 조회한다.
     * 
     * @param userAccessToken 사용자 액세스 토큰
     * @param installationId Installation ID
     * @return 레포지토리 목록
     */
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getInstallationRepositories(String userAccessToken, String installationId) throws Exception {
        String url = GITHUB_API_BASE_URL + "/user/installations/" + installationId + "/repositories";
        
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "token " + userAccessToken);
        headers.set("Accept", "application/vnd.github.v3+json");
        headers.set("User-Agent", "ProWorks5-GitHub-Manager");
        
        HttpEntity<String> entity = new HttpEntity<>(headers);
        
        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);
        
        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            Map<String, Object> responseBody = response.getBody();
            return (List<Map<String, Object>>) responseBody.get("repositories");
        }
        
        throw new RuntimeException("Installation 레포지토리 목록 조회에 실패했습니다.");
    }
    
    /**
     * Installation ID를 찾는다.
     * 
     * @param userAccessToken 사용자 액세스 토큰
     * @param repoOwner 레포지토리 소유자
     * @return Installation ID
     */
    public String findInstallationId(String userAccessToken, String repoOwner) throws Exception {
        List<Map<String, Object>> installations = getUserInstallations(userAccessToken);
        
        String targetAppId = githubAppAuthUtil.getAppId();
        
        for (Map<String, Object> installation : installations) {
            Object appIdObj = installation.get("app_id");
            String appId = appIdObj != null ? appIdObj.toString() : "";
            
            if (targetAppId.equals(appId)) {
                String installationId = installation.get("id").toString();
                
                // 해당 Installation에서 레포지토리 확인
                List<Map<String, Object>> repositories = getInstallationRepositories(userAccessToken, installationId);
                
                for (Map<String, Object> repo : repositories) {
                    Map<String, Object> owner = (Map<String, Object>) repo.get("owner");
                    if (owner != null && repoOwner.equals(owner.get("login"))) {
                        return installationId;
                    }
                }
            }
        }
        
        throw new RuntimeException("GitHub App이 해당 레포지토리 소유자에게 설치되지 않았습니다: " + repoOwner);
    }
    
    /**
     * 레포지토리의 Installation Token을 가져온다.
     * 
     * @param userAccessToken 사용자 액세스 토큰
     * @param repoOwner 레포지토리 소유자
     * @return Installation Token
     */
    public String getRepoInstallationToken(String userAccessToken, String repoOwner) throws Exception {
        String installationId = findInstallationId(userAccessToken, repoOwner);
        return getInstallationToken(installationId);
    }
    
    /**
     * GitHub API로 웹훅을 생성한다.
     * 
     * @param installationToken Installation Token
     * @param repoFullName 레포지토리 전체 이름 (owner/repo)
     * @param webhookConfig 웹훅 설정
     * @return 생성된 웹훅 정보
     */
    public Map<String, Object> createWebhook(String installationToken, String repoFullName, Map<String, Object> webhookConfig) throws Exception {
        String url = GITHUB_API_BASE_URL + "/repos/" + repoFullName + "/hooks";
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "token " + installationToken);
        headers.set("Accept", "application/vnd.github.v3+json");
        headers.set("User-Agent", "ProWorks5-GitHub-Manager");
        
        String requestBody = objectMapper.writeValueAsString(webhookConfig);
        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
        
        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, entity, Map.class);
        
        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            return response.getBody();
        }
        
        throw new RuntimeException("웹훅 생성에 실패했습니다.");
    }
    
    /**
     * 레포지토리의 브랜치 목록을 조회한다.
     * 
     * @param accessToken 액세스 토큰
     * @param repoFullName 레포지토리 전체 이름
     * @return 브랜치 목록
     */
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getRepositoryBranches(String accessToken, String repoFullName) throws Exception {
        String url = GITHUB_API_BASE_URL + "/repos/" + repoFullName + "/branches";
        
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "token " + accessToken);
        headers.set("Accept", "application/vnd.github.v3+json");
        headers.set("User-Agent", "ProWorks5-GitHub-Manager");
        
        HttpEntity<String> entity = new HttpEntity<>(headers);
        
        ResponseEntity<List> response = restTemplate.exchange(url, HttpMethod.GET, entity, List.class);
        
        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            return response.getBody();
        }
        
        throw new RuntimeException("브랜치 목록 조회에 실패했습니다.");
    }
    
    /**
     * 새로운 브랜치를 생성한다.
     * 
     * @param accessToken 액세스 토큰
     * @param repoFullName 레포지토리 전체 이름
     * @param branchName 새 브랜치 이름
     * @param sourceSha 소스 브랜치의 SHA
     * @return 생성된 브랜치 정보
     */
    public Map<String, Object> createBranch(String accessToken, String repoFullName, String branchName, String sourceSha) throws Exception {
        String url = GITHUB_API_BASE_URL + "/repos/" + repoFullName + "/git/refs";
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "token " + accessToken);
        headers.set("Accept", "application/vnd.github.v3+json");
        headers.set("User-Agent", "ProWorks5-GitHub-Manager");
        
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("ref", "refs/heads/" + branchName);
        requestBody.put("sha", sourceSha);
        
        String requestBodyJson = objectMapper.writeValueAsString(requestBody);
        HttpEntity<String> entity = new HttpEntity<>(requestBodyJson, headers);
        
        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, entity, Map.class);
        
        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            return response.getBody();
        }
        
        throw new RuntimeException("브랜치 생성에 실패했습니다.");
    }
    
    /**
     * 사용자가 레포지토리의 collaborator인지 확인한다.
     * 
     * @param accessToken 액세스 토큰
     * @param repoFullName 레포지토리 전체 이름
     * @param username GitHub 사용자명
     * @return collaborator 여부
     */
    public boolean isCollaborator(String accessToken, String repoFullName, String username) throws Exception {
        String url = GITHUB_API_BASE_URL + "/repos/" + repoFullName + "/collaborators/" + username;
        
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "token " + accessToken);
        headers.set("Accept", "application/vnd.github.v3+json");
        headers.set("User-Agent", "ProWorks5-GitHub-Manager");
        
        HttpEntity<String> entity = new HttpEntity<>(headers);
        
        try {
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);
            return response.getStatusCode().is2xxSuccessful();
        } catch (Exception e) {
            // 404 또는 403 등의 경우 collaborator가 아님
            return false;
        }
    }
    
    /**
     * 레포지토리에 collaborator를 초대한다.
     * 
     * @param accessToken 액세스 토큰
     * @param repoFullName 레포지토리 전체 이름
     * @param username GitHub 사용자명
     * @param permission 권한 레벨 (pull, push, maintain, admin)
     * @return 초대 결과
     */
    public Map<String, Object> inviteCollaborator(String accessToken, String repoFullName, String username, String permission) throws Exception {
        String url = GITHUB_API_BASE_URL + "/repos/" + repoFullName + "/collaborators/" + username;
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "token " + accessToken);
        headers.set("Accept", "application/vnd.github.v3+json");
        headers.set("User-Agent", "ProWorks5-GitHub-Manager");
        
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("permission", permission);
        
        String requestBodyJson = objectMapper.writeValueAsString(requestBody);
        HttpEntity<String> entity = new HttpEntity<>(requestBodyJson, headers);
        
        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.PUT, entity, Map.class);
        
        if (response.getStatusCode().is2xxSuccessful()) {
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("message", "사용자 " + username + "을(를) " + repoFullName + " 레포지토리에 초대했습니다.");
            result.put("username", username);
            result.put("repository", repoFullName);
            result.put("permission", permission);
            return result;
        }
        
        throw new RuntimeException("Collaborator 초대에 실패했습니다.");
    }
    
    /**
     * GitHub 사용자 정보를 조회한다.
     * 
     * @param accessToken 액세스 토큰
     * @return 사용자 정보
     */
    public Map<String, Object> getCurrentUser(String accessToken) throws Exception {
        String url = GITHUB_API_BASE_URL + "/user";
        
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "token " + accessToken);
        headers.set("Accept", "application/vnd.github.v3+json");
        headers.set("User-Agent", "ProWorks5-GitHub-Manager");
        
        HttpEntity<String> entity = new HttpEntity<>(headers);
        
        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);
        
        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            return response.getBody();
        }
        
        throw new RuntimeException("사용자 정보 조회에 실패했습니다.");
    }
}
