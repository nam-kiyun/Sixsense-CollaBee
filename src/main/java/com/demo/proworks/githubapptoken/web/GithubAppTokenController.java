package com.demo.proworks.githubapptoken.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.demo.proworks.githubapptoken.service.GithubAppTokenService;
import com.inswave.elfw.annotation.ElDescription;
import com.inswave.elfw.annotation.ElService;
import com.inswave.elfw.annotation.ElValidator;

/**
 * @subject     : GitHub App 토큰 관련 처리를 담당하는 Controller
 * @description : GitHub App 토큰 관련 처리를 담당하는 Controller
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
public class GithubAppTokenController {

    @Autowired
    private GithubAppTokenService githubAppTokenService;

    /**
     * GitHub App Installation Token을 생성한다.
     * 
     * @param request HttpServletRequest
     * @param userAccessToken 사용자 액세스 토큰
     * @param repoOwner 레포지토리 소유자
     * @return ResponseEntity<Map<String, Object>>
     */
    @ElService(key = "SvcGHAPPTOKENGEN01")
    @RequestMapping(value = "/github/app-token/generate")
    @ElDescription(sub = "GitHub App Token 생성", desc = "GitHub App Installation Token을 생성합니다.")
    public @ResponseBody ResponseEntity<Map<String, Object>> generateInstallationToken(HttpServletRequest request,
            @RequestParam("userAccessToken") String userAccessToken,
            @RequestParam("repoOwner") String repoOwner) {
        
        Map<String, Object> result = new HashMap<>();
        
        try {
            String installationToken = githubAppTokenService.generateInstallationToken(userAccessToken, repoOwner);
            
            result.put("success", true);
            result.put("message", "Installation Token이 성공적으로 생성되었습니다.");
            result.put("token", installationToken);
            result.put("repo_owner", repoOwner);
            
            return new ResponseEntity<>(result, HttpStatus.OK);
            
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "Installation Token 생성에 실패했습니다: " + e.getMessage());
            result.put("error", e.getMessage());
            result.put("repo_owner", repoOwner);
            
            return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 프로젝트 레포지토리의 유효한 App Token을 조회한다.
     * 
     * @param request HttpServletRequest
     * @param projectRepoId 프로젝트 레포지토리 ID
     * @return ResponseEntity<Map<String, Object>>
     */
    @ElService(key = "SvcGHAPPTOKENGET01")
    @RequestMapping(value = "/github/app-token/get")
    @ElDescription(sub = "유효한 App Token 조회", desc = "프로젝트 레포지토리의 유효한 App Token을 조회합니다.")
    public @ResponseBody ResponseEntity<Map<String, Object>> getValidAppToken(HttpServletRequest request,
            @RequestParam("projectRepoId") String projectRepoId) {
        
        Map<String, Object> result = new HashMap<>();
        
        try {
            String appToken = githubAppTokenService.getValidAppToken(projectRepoId);
            
            if (appToken != null) {
                result.put("success", true);
                result.put("message", "유효한 App Token을 찾았습니다.");
                result.put("token", appToken);
                result.put("has_valid_token", true);
            } else {
                result.put("success", true);
                result.put("message", "유효한 App Token이 없습니다.");
                result.put("has_valid_token", false);
            }
            
            result.put("project_repo_id", projectRepoId);
            
            return new ResponseEntity<>(result, HttpStatus.OK);
            
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "App Token 조회에 실패했습니다: " + e.getMessage());
            result.put("error", e.getMessage());
            result.put("project_repo_id", projectRepoId);
            
            return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * App Token을 갱신한다.
     * 
     * @param request HttpServletRequest
     * @param projectRepoId 프로젝트 레포지토리 ID
     * @param userAccessToken 사용자 액세스 토큰
     * @param repoOwner 레포지토리 소유자
     * @return ResponseEntity<Map<String, Object>>
     */
    @ElService(key = "SvcGHAPPTOKENREFRESH01")
    @RequestMapping(value = "/github/app-token/refresh")
    @ElDescription(sub = "App Token 갱신", desc = "App Token을 갱신하거나 새로 생성합니다.")
    public @ResponseBody ResponseEntity<Map<String, Object>> refreshAppToken(HttpServletRequest request,
            @RequestParam("projectRepoId") String projectRepoId,
            @RequestParam("userAccessToken") String userAccessToken,
            @RequestParam("repoOwner") String repoOwner) {
        
        Map<String, Object> result = new HashMap<>();
        
        try {
            String refreshedToken = githubAppTokenService.refreshAppToken(projectRepoId, userAccessToken, repoOwner);
            
            result.put("success", true);
            result.put("message", "App Token이 성공적으로 갱신되었습니다.");
            result.put("token", refreshedToken);
            result.put("project_repo_id", projectRepoId);
            result.put("repo_owner", repoOwner);
            
            return new ResponseEntity<>(result, HttpStatus.OK);
            
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "App Token 갱신에 실패했습니다: " + e.getMessage());
            result.put("error", e.getMessage());
            result.put("project_repo_id", projectRepoId);
            result.put("repo_owner", repoOwner);
            
            return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * GitHub App Token 서비스 상태를 확인한다.
     * 
     * @param request HttpServletRequest
     * @return ResponseEntity<Map<String, Object>>
     */
    @ElService(key = "SvcGHAPPTOKENSTATUS01")
    @RequestMapping(value = "/github/app-token/status")
    @ElDescription(sub = "App Token 서비스 상태", desc = "GitHub App Token 서비스의 상태를 확인합니다.")
    public @ResponseBody ResponseEntity<Map<String, Object>> getServiceStatus(HttpServletRequest request) {
        
        Map<String, Object> result = new HashMap<>();
        
        try {
            result.put("status", "ok");
            result.put("timestamp", java.time.LocalDateTime.now().toString());
            result.put("service", "GitHub App Token Service");
            result.put("version", "1.0.0");
            
            // 간단한 서비스 연결 테스트
            try {
                githubAppTokenService.selectListCountGithubAppToken(new com.demo.proworks.githubapptoken.vo.GithubAppTokenVo());
                result.put("database", "connected");
            } catch (Exception e) {
                result.put("database", "error: " + e.getMessage());
            }
            
            return new ResponseEntity<>(result, HttpStatus.OK);
            
        } catch (Exception e) {
            result.put("status", "error");
            result.put("message", e.getMessage());
            
            return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
