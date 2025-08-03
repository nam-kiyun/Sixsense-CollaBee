package com.demo.proworks.github.util;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.HashMap;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * GitHub 웹훅 처리 유틸리티
 */
@Component
public class GitHubWebhookUtil {
    
    
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 웹훅 서명 검증
     * test의 verifySignature 함수 포팅
     * @param payload 웹훅 페이로드
     * @param signature GitHub에서 전송한 서명
     * @param secret 웹훅 시크릿
     * @return 검증 결과
     */
    public boolean verifySignature(String payload, String signature, String secret) {
        if (signature == null || signature.isEmpty()) {
            return false;
        }
        
        if (secret == null || secret.isEmpty()) {
            return false;
        }
        
        try {
            // HMAC-SHA256으로 예상 서명 생성
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKeySpec = new SecretKeySpec(secret.getBytes("UTF-8"), "HmacSHA256");
            mac.init(secretKeySpec);
            
            byte[] hashBytes = mac.doFinal(payload.getBytes("UTF-8"));
            
            // 16진수 문자열로 변환
            StringBuilder expectedSignature = new StringBuilder("sha256=");
            for (byte b : hashBytes) {
                expectedSignature.append(String.format("%02x", b));
            }
            
            // 타이밍 공격 방지를 위한 안전한 비교
            return timingSafeEquals(signature, expectedSignature.toString());
            
        } catch (NoSuchAlgorithmException | InvalidKeyException | java.io.UnsupportedEncodingException e) {
            return false;
        }
    }
    
    /**
     * 타이밍 공격 방지를 위한 안전한 문자열 비교
     * @param a 첫 번째 문자열
     * @param b 두 번째 문자열
     * @return 비교 결과
     */
    private boolean timingSafeEquals(String a, String b) {
        if (a.length() != b.length()) {
            return false;
        }
        
        int result = 0;
        for (int i = 0; i < a.length(); i++) {
            result |= a.charAt(i) ^ b.charAt(i);
        }
        
        return result == 0;
    }
    
    /**
     * 웹훅 페이로드에서 기본 정보 추출
     * @param payloadJson 웹훅 페이로드 JSON
     * @return 추출된 정보
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> extractBasicInfo(String payloadJson) throws Exception {
        Map<String, Object> payload = objectMapper.readValue(payloadJson, Map.class);
        Map<String, Object> info = new HashMap<>();
        
        // 레포지토리 정보
        Map<String, Object> repository = (Map<String, Object>) payload.get("repository");
        if (repository != null) {
            info.put("repository_id", repository.get("id"));
            info.put("repository_name", repository.get("name"));
            info.put("repository_full_name", repository.get("full_name"));
            info.put("repository_owner", ((Map<String, Object>) repository.get("owner")).get("login"));
        }
        
        // 발신자 정보
        Map<String, Object> sender = (Map<String, Object>) payload.get("sender");
        if (sender != null) {
            info.put("sender_id", sender.get("id"));
            info.put("sender_login", sender.get("login"));
        }
        
        // 공통 필드
        info.put("action", payload.get("action"));
        info.put("ref", payload.get("ref"));
        info.put("before", payload.get("before"));
        info.put("after", payload.get("after"));
        
        return info;
    }
    
    /**
     * Push 이벤트 정보 추출
     * test의 handlePushEvent 함수 참고
     * @param payloadJson 웹훅 페이로드 JSON
     * @return Push 이벤트 정보
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> extractPushEventInfo(String payloadJson) throws Exception {
        Map<String, Object> payload = objectMapper.readValue(payloadJson, Map.class);
        Map<String, Object> pushInfo = new HashMap<>();
        
        // 기본 정보
        Map<String, Object> basicInfo = extractBasicInfo(payloadJson);
        pushInfo.putAll(basicInfo);
        
        // Push 특화 정보
        pushInfo.put("ref", payload.get("ref"));
        pushInfo.put("before_sha", payload.get("before"));
        pushInfo.put("after_sha", payload.get("after"));
        
        // 커밋 정보
        Object commitsObj = payload.get("commits");
        if (commitsObj instanceof java.util.List) {
            java.util.List<Map<String, Object>> commits = (java.util.List<Map<String, Object>>) commitsObj;
            pushInfo.put("commits_count", commits.size());
            
            if (!commits.isEmpty()) {
                Map<String, Object> lastCommit = commits.get(commits.size() - 1);
                pushInfo.put("last_commit_message", lastCommit.get("message"));
                pushInfo.put("last_commit_author", ((Map<String, Object>) lastCommit.get("author")).get("name"));
            }
        }
        
        // 브랜치 이름 추출 (refs/heads/branch-name에서 branch-name만 추출)
        String ref = (String) payload.get("ref");
        if (ref != null && ref.startsWith("refs/heads/")) {
            pushInfo.put("branch_name", ref.substring("refs/heads/".length()));
        }
        
        return pushInfo;
    }
    
    /**
     * Pull Request 이벤트 정보 추출
     * test의 handlePullRequestEvent 함수 참고
     * @param payloadJson 웹훅 페이로드 JSON
     * @return PR 이벤트 정보
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> extractPullRequestEventInfo(String payloadJson) throws Exception {
        Map<String, Object> payload = objectMapper.readValue(payloadJson, Map.class);
        Map<String, Object> prInfo = new HashMap<>();
        
        // 기본 정보
        Map<String, Object> basicInfo = extractBasicInfo(payloadJson);
        prInfo.putAll(basicInfo);
        
        // PR 정보
        Map<String, Object> pullRequest = (Map<String, Object>) payload.get("pull_request");
        if (pullRequest != null) {
            prInfo.put("pr_number", pullRequest.get("number"));
            prInfo.put("pr_title", pullRequest.get("title"));
            prInfo.put("pr_body", pullRequest.get("body"));
            prInfo.put("pr_state", pullRequest.get("state"));
            prInfo.put("pr_url", pullRequest.get("html_url"));
            prInfo.put("is_merged", pullRequest.get("merged"));
            
            // 브랜치 정보
            Map<String, Object> head = (Map<String, Object>) pullRequest.get("head");
            Map<String, Object> base = (Map<String, Object>) pullRequest.get("base");
            if (head != null) {
                prInfo.put("head_branch", head.get("ref"));
            }
            if (base != null) {
                prInfo.put("base_branch", base.get("ref"));
            }
        }
        
        return prInfo;
    }
    
    /**
     * Create/Delete 이벤트 정보 추출
     * test의 handleCreateEvent, handleDeleteEvent 함수 참고
     * @param payloadJson 웹훅 페이로드 JSON
     * @return Create/Delete 이벤트 정보
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> extractCreateDeleteEventInfo(String payloadJson) throws Exception {
        Map<String, Object> payload = objectMapper.readValue(payloadJson, Map.class);
        Map<String, Object> eventInfo = new HashMap<>();
        
        // 기본 정보
        Map<String, Object> basicInfo = extractBasicInfo(payloadJson);
        eventInfo.putAll(basicInfo);
        
        // Create/Delete 특화 정보
        eventInfo.put("ref_type", payload.get("ref_type")); // branch, tag
        eventInfo.put("ref", payload.get("ref")); // 브랜치/태그 이름
        eventInfo.put("description", payload.get("description"));
        
        return eventInfo;
    }
    
    /**
     * 이슈 이벤트 정보 추출
     * @param payloadJson 웹훅 페이로드 JSON
     * @return 이슈 이벤트 정보
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> extractIssueEventInfo(String payloadJson) throws Exception {
        Map<String, Object> payload = objectMapper.readValue(payloadJson, Map.class);
        Map<String, Object> issueInfo = new HashMap<>();
        
        // 기본 정보
        Map<String, Object> basicInfo = extractBasicInfo(payloadJson);
        issueInfo.putAll(basicInfo);
        
        // 이슈 정보
        Map<String, Object> issue = (Map<String, Object>) payload.get("issue");
        if (issue != null) {
            issueInfo.put("issue_number", issue.get("number"));
            issueInfo.put("issue_title", issue.get("title"));
            issueInfo.put("issue_body", issue.get("body"));
            issueInfo.put("issue_state", issue.get("state"));
            issueInfo.put("issue_url", issue.get("html_url"));
            
            // 라벨 정보
            Object labelsObj = issue.get("labels");
            if (labelsObj instanceof java.util.List) {
                java.util.List<Map<String, Object>> labels = (java.util.List<Map<String, Object>>) labelsObj;
                java.util.List<String> labelNames = new java.util.ArrayList<>();
                for (Map<String, Object> label : labels) {
                    labelNames.add((String) label.get("name"));
                }
                issueInfo.put("labels", String.join(",", labelNames));
            }
            
            // 담당자 정보
            Object assigneesObj = issue.get("assignees");
            if (assigneesObj instanceof java.util.List) {
                java.util.List<Map<String, Object>> assignees = (java.util.List<Map<String, Object>>) assigneesObj;
                java.util.List<String> assigneeNames = new java.util.ArrayList<>();
                for (Map<String, Object> assignee : assignees) {
                    assigneeNames.add((String) assignee.get("login"));
                }
                issueInfo.put("assignees", String.join(",", assigneeNames));
            }
        }
        
        return issueInfo;
    }
    
    /**
     * 웹훅 이벤트 타입에 따른 정보 추출
     * @param eventType 이벤트 타입
     * @param payloadJson 웹훅 페이로드 JSON
     * @return 추출된 이벤트 정보
     * @throws Exception
     */
    public Map<String, Object> extractEventInfo(String eventType, String payloadJson) throws Exception {
        
        switch (eventType) {
            case "push":
                return extractPushEventInfo(payloadJson);
            case "pull_request":
                return extractPullRequestEventInfo(payloadJson);
            case "create":
            case "delete":
                return extractCreateDeleteEventInfo(payloadJson);
            case "issues":
                return extractIssueEventInfo(payloadJson);
            default:
                return extractBasicInfo(payloadJson);
        }
    }
    
    /**
     * 웹훅 이벤트 검증 및 처리 준비
     * @param eventType GitHub 이벤트 타입
     * @param deliveryId GitHub 전송 ID
     * @param signature GitHub 서명
     * @param payloadJson 페이로드 JSON
     * @param secret 웹훅 시크릿
     * @return 검증 및 처리 결과
     */
    public Map<String, Object> validateAndPrepareEvent(String eventType, String deliveryId, String signature, String payloadJson, String secret) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            
            // 서명 검증 (시크릿이 설정된 경우만)
            if (secret != null && !secret.isEmpty()) {
                if (!verifySignature(payloadJson, signature, secret)) {
                    result.put("success", false);
                    result.put("error", "Invalid webhook signature");
                    return result;
                }
            } else {
            }
            
            // 이벤트 정보 추출
            Map<String, Object> eventInfo = extractEventInfo(eventType, payloadJson);
            
            result.put("success", true);
            result.put("event_type", eventType);
            result.put("delivery_id", deliveryId);
            result.put("event_info", eventInfo);
            result.put("payload_json", payloadJson);
            result.put("signature", signature);
            
            
        } catch (Exception e) {
            result.put("success", false);
            result.put("error", "Webhook event validation failed: " + e.getMessage());
        }
        
        return result;
    }
    
    /**
     * 웹훅 설정 정보 생성
     * test의 createWebhook 함수 참고
     * @param webhookUrl 웹훅 수신 URL
     * @param events 감지할 이벤트 목록
     * @param secret 웹훅 시크릿 (선택사항)
     * @return 웹훅 설정 정보
     */
    public Map<String, Object> createWebhookConfig(String webhookUrl, String[] events, String secret) {
        Map<String, Object> config = new HashMap<>();
        config.put("url", webhookUrl);
        config.put("content_type", "json");
        config.put("insecure_ssl", "0");
        
        if (secret != null && !secret.isEmpty()) {
            config.put("secret", secret);
        }
        
        Map<String, Object> webhookConfig = new HashMap<>();
        webhookConfig.put("name", "web");
        webhookConfig.put("active", true);
        webhookConfig.put("events", events);
        webhookConfig.put("config", config);
        
        return webhookConfig;
    }
}