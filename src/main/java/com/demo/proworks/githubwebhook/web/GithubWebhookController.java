package com.demo.proworks.githubwebhook.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.demo.proworks.githubwebhook.service.GithubWebhookService;
import com.demo.proworks.githubwebhook.vo.GithubWebhookVo;
import com.inswave.elfw.annotation.ElDescription;
import com.inswave.elfw.annotation.ElService;
import com.inswave.elfw.annotation.ElValidator;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @subject     : GitHub 웹훅 관련 처리를 담당하는 Controller
 * @description : GitHub 웹훅 관련 처리를 담당하는 Controller
 * @author      : 남기윤
 * @since       : 2025/07/07
 * @modification
 * ===========================================================
 * DATE              AUTHOR             DESC
 * ===========================================================
 * 2025/07/07              남기윤             최초 생성
 * 
 */
@Controller
public class GithubWebhookController {

    @Autowired
    private GithubWebhookService githubWebhookService;

    /**
     * GitHub 웹훅을 생성한다.
     * 
     * @param request HttpServletRequest
     * @param projectRepoId 프로젝트 레포지토리 ID
     * @param repoFullName 레포지토리 전체 이름
     * @param webhookUrl 웹훅 URL (선택사항)
     * @param userAccessToken 사용자 액세스 토큰
     * @return ResponseEntity<Map<String, Object>>
     */
    @ElService(key = "/github/webhooks/create")
    @RequestMapping(value = "/github/webhooks/create")
    @ElDescription(sub = "GitHub 웹훅 생성", desc = "GitHub API를 통해 웹훅을 생성합니다.")
    public @ResponseBody ResponseEntity<Map<String, Object>> createWebhook(HttpServletRequest request,
            @RequestParam("projectRepoId") String projectRepoId,
            @RequestParam("repoFullName") String repoFullName,
            @RequestParam(value = "webhookUrl", required = false) String webhookUrl,
            @RequestParam("userAccessToken") String userAccessToken) {
        
        Map<String, Object> result = new HashMap<>();
        
        try {
            GithubWebhookVo webhook = githubWebhookService.createWebhookViaAPI(
                projectRepoId, repoFullName, webhookUrl, userAccessToken);
            
            result.put("success", true);
            result.put("message", "웹훅이 성공적으로 생성되었습니다.");
            result.put("webhook", webhook);
            
            return new ResponseEntity<>(result, HttpStatus.OK);
            
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "웹훅 생성에 실패했습니다: " + e.getMessage());
            result.put("error", e.getMessage());
            
            return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * GitHub 웹훅 상태를 조회한다.
     * 
     * @param request HttpServletRequest
     * @param projectRepoId 프로젝트 레포지토리 ID
     * @param userAccessToken 사용자 액세스 토큰
     * @return ResponseEntity<Map<String, Object>>
     */
    @ElService(key = "/github/webhooks/status")
    @RequestMapping(value = "/github/webhooks/status")
    @ElDescription(sub = "GitHub 웹훅 상태 조회", desc = "GitHub 웹훅의 상태를 확인합니다.")
    public @ResponseBody ResponseEntity<Map<String, Object>> getWebhookStatus(HttpServletRequest request,
            @RequestParam("projectRepoId") String projectRepoId,
            @RequestParam("userAccessToken") String userAccessToken) {
        
        try {
            Map<String, Object> status = githubWebhookService.getWebhookStatus(projectRepoId, userAccessToken);
            return new ResponseEntity<>(status, HttpStatus.OK);
            
        } catch (Exception e) {
            Map<String, Object> result = new HashMap<>();
            result.put("success", false);
            result.put("message", "웹훅 상태 조회에 실패했습니다: " + e.getMessage());
            result.put("error", e.getMessage());
            
            return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * GitHub 웹훅을 삭제한다.
     * 
     * @param request HttpServletRequest
     * @param projectRepoId 프로젝트 레포지토리 ID
     * @param userAccessToken 사용자 액세스 토큰
     * @return ResponseEntity<Map<String, Object>>
     */
    @ElService(key = "/github/webhooks/delete")
    @RequestMapping(value = "/github/webhooks/delete")
    @ElDescription(sub = "GitHub 웹훅 삭제", desc = "GitHub API를 통해 웹훅을 삭제합니다.")
    public @ResponseBody ResponseEntity<Map<String, Object>> deleteWebhook(HttpServletRequest request,
            @RequestParam("projectRepoId") String projectRepoId,
            @RequestParam("userAccessToken") String userAccessToken) {
        
        Map<String, Object> result = new HashMap<>();
        
        try {
            boolean deleted = githubWebhookService.deleteWebhookViaAPI(projectRepoId, userAccessToken);
            
            result.put("success", deleted);
            result.put("message", deleted ? "웹훅이 성공적으로 삭제되었습니다." : "웹훅 삭제에 실패했습니다.");
            
            return new ResponseEntity<>(result, HttpStatus.OK);
            
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "웹훅 삭제에 실패했습니다: " + e.getMessage());
            result.put("error", e.getMessage());
            
            return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * GitHub 웹훅 이벤트를 수신하고 처리한다.
     * 
     * @param request HttpServletRequest
     * @param eventType GitHub 이벤트 타입
     * @param delivery GitHub 배송 ID
     * @param signature GitHub 서명
     * @param payload 웹훅 페이로드
     * @return ResponseEntity<Map<String, Object>>
     */
    @ElService(key = "/github/webhook")
	@RequestMapping(value = "/github/webhook")
    @PostMapping(value = "/github/webhook")
    @ElDescription(sub = "GitHub 웹훅 이벤트 처리", desc = "GitHub에서 전송된 웹훅 이벤트를 처리합니다.")
    public @ResponseBody ResponseEntity<Map<String, Object>> handleWebhookEvent(HttpServletRequest request,
            @RequestHeader(value = "X-GitHub-Event", required = false) String eventType,
            @RequestHeader(value = "X-GitHub-Delivery", required = false) String delivery,
            @RequestHeader(value = "X-Hub-Signature-256", required = false) String signature,
            @RequestBody String payload) {
        
        Map<String, Object> result = new HashMap<>();
        
        try {
            String processResult = githubWebhookService.processWebhookEvent(eventType, payload, signature);
            
            result.put("success", true);
            result.put("message", "웹훅 이벤트가 성공적으로 처리되었습니다.");
            result.put("event_type", eventType);
            result.put("delivery_id", delivery);
            result.put("process_result", processResult);
            
            return new ResponseEntity<>(result, HttpStatus.OK);
            
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "웹훅 이벤트 처리에 실패했습니다: " + e.getMessage());
            result.put("error", e.getMessage());
            result.put("event_type", eventType);
            result.put("delivery_id", delivery);
            
            return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 웹훅 서비스 헬스 체크
     * 
     * @param request HttpServletRequest
     * @return ResponseEntity<Map<String, Object>>
     */
    @ElService(key = "/github/webhooks/health")
    @RequestMapping(value = "/github/webhooks/health")
    @ElDescription(sub = "웹훅 서비스 헬스 체크", desc = "웹훅 서비스의 상태를 확인합니다.")
    public @ResponseBody ResponseEntity<Map<String, Object>> healthCheck(HttpServletRequest request) {
        
        Map<String, Object> result = new HashMap<>();
        
        try {
            result.put("status", "ok");
            result.put("timestamp", java.time.LocalDateTime.now().toString());
            result.put("service", "GitHub Webhook Service");
            result.put("version", "1.0.0");
            
            return new ResponseEntity<>(result, HttpStatus.OK);
            
        } catch (Exception e) {
            result.put("status", "error");
            result.put("message", e.getMessage());
            
            return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
