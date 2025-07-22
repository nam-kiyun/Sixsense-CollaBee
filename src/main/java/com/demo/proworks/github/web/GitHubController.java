package com.demo.proworks.github.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import com.demo.proworks.github.service.GitHubService;
import com.demo.proworks.github.vo.BranchParameterVo;
import com.demo.proworks.projectrepo.vo.ProjectRepositoryVo;
import com.inswave.elfw.annotation.ElDescription;
import com.inswave.elfw.annotation.ElService;

/**
 * GitHub 통합 컨트롤러
 * test 디렉터리의 Node.js 라우터들을 Java로 포팅
 */
@Controller
public class GitHubController {
    
    private static final Logger logger = LoggerFactory.getLogger(GitHubController.class);
    
    @Resource(name = "gitHubService")
    private GitHubService gitHubService;
    
    @Autowired
    private RequestMappingHandlerMapping requestMappingHandlerMapping;
    
    @Autowired
    private com.demo.proworks.github.util.GitHubApiUtil gitHubApiUtil;
    
    @Resource(name = "userPersonalTokenServiceImpl")
    private com.demo.proworks.userpersonaltoken.service.UserPersonalTokenService userPersonalTokenService;
    
    // 중복 웹훅 방지를 위한 배송 ID 캐시 (간단한 메모리 기반)
    private static final java.util.Set<String> processedDeliveryIds = 
        java.util.Collections.synchronizedSet(new java.util.LinkedHashSet<String>());
    
    /**
     * 생성자 - 빈 등록 확인용
     */
    public GitHubController() {
        logger.info("GitHubController 빈이 생성되었습니다.");
        System.out.println("=== GitHubController 생성됨 ===");
        
        // 매핑 확인 코드 주석 처리 (의존성 문제 방지)
        new Thread(() -> {
            try {
                Thread.sleep(5000);
                if (requestMappingHandlerMapping != null) {
                    System.out.println("=== GitHub 컨트롤러 매핑 정보 ===");
                    requestMappingHandlerMapping.getHandlerMethods().forEach((requestMappingInfo, handlerMethod) -> {
                        if (handlerMethod.getBeanType().equals(GitHubController.class)) {
                            System.out.println("GitHub 매핑: " + requestMappingInfo + " -> " + handlerMethod.getMethod().getName());
                        }
                    });
                    System.out.println("=== GitHub 매핑 정보 끝 ===");
                } else {
                    System.out.println("RequestMappingHandlerMapping이 null입니다.");
                }
            } catch (Exception e) {
                System.out.println("매핑 확인 중 오류: " + e.getMessage());
            }
        }).start();
    }

    // ==============================
    // GitHub OAuth 인증 관리
    // ==============================
    
    /**
     * GitHub 컨트롤러 테스트용 엔드포인트
     */
    @ElService(key = "test")
    @ElDescription(sub = "GitHub 컨트롤러 테스트", desc = "GitHub 컨트롤러 연결 상태를 테스트합니다.")
    @RequestMapping(value = "test", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> testGitHubController() {
        logger.info("GitHub 컨트롤러 테스트 호출");
        System.out.println("=== GitHub 컨트롤러 테스트 메소드 진입 ===");
        
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "GitHub 컨트롤러가 정상적으로 동작합니다.");
        result.put("controller", "GitHubController");
        result.put("timestamp", new java.util.Date().toString());
        
        return result;
    }
    
    /**
     * 매핑 정보 확인용 엔드포인트 (의존성 문제로 주석 처리)
     */
    /*
    @ElService(key = "mappings")
    @ElDescription(sub = "GitHub 매핑 확인", desc = "현재 등록된 URL 매핑 정보를 확인합니다.")
    @RequestMapping(value = "mappings")
    @ResponseBody
    public Map<String, Object> checkMappings() {
        logger.info("GitHub 매핑 확인");
        System.out.println("=== GitHub 매핑 확인 메소드 진입 ===");
        
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "매핑 확인 완료");
        
        try {
            // 등록된 모든 매핑 정보 출력
            requestMappingHandlerMapping.getHandlerMethods().forEach((requestMappingInfo, handlerMethod) -> {
                System.out.println("매핑: " + requestMappingInfo + " -> " + handlerMethod);
            });
            result.put("mapping_logged", true);
        } catch (Exception e) {
            System.out.println("매핑 확인 중 오류: " + e.getMessage());
            result.put("mapping_error", e.getMessage());
        }
        
        return result;
    }
    */
    
    /**
     * 간단한 상태 확인 테스트 엔드포인트
     */
    @ElService(key = "health")
    @ElDescription(sub = "GitHub 헬스체크", desc = "GitHub 컨트롤러의 상태를 간단히 확인합니다.")
    @RequestMapping(value = "health")
    @ResponseBody
    public Map<String, Object> healthCheck() {
        logger.info("GitHub 컨트롤러 헬스체크 호출");
        
        Map<String, Object> result = new HashMap<>();
        result.put("status", "ok");
        result.put("controller", "GitHubController");
        result.put("timestamp", System.currentTimeMillis());
        
        return result;
    }
    
    /**
     * GitHub 컨트롤러 간단 테스트용 엔드포인트 (서비스 의존성 없음)
     */
    @ElService(key = "simple-test")
    @ElDescription(sub = "GitHub 간단 테스트", desc = "GitHub 컨트롤러의 간단한 테스트를 수행합니다.")
    @RequestMapping(value = "simple-test")
    @ResponseBody
    public Map<String, Object> simpleTestGitHubController() {
        logger.info("GitHub 컨트롤러 간단 테스트 호출");
        
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "GitHub 컨트롤러 간단 테스트 성공");
        result.put("controller", "GitHubController");
        result.put("timestamp", new java.util.Date().toString());
        
        return result;
    }
    
    /**
     * GitHub OAuth 인증 시작 (서비스 의존성 문제로 주석 처리)
     * test의 /auth/github 에 해당
     */
    
    @ElService(key = "auth/start")
    @ElDescription(sub = "GitHub OAuth 인증 시작", desc = "GitHub OAuth 인증 프로세스를 시작합니다.")
    @RequestMapping(value = "auth/start", method = RequestMethod.GET)
    public ModelAndView startGitHubAuth(
            @RequestParam(required = false) String projectId,
            @RequestParam(required = false) String userId,
            HttpServletRequest request, 
            HttpServletResponse response) {
        logger.info("GitHub OAuth 인증 시작 (projectId: {}, 요청된 userId: {})", projectId, userId);
        
        try {
            HttpSession session = request.getSession();
            
            // 현재 세션의 실제 사용자 ID 가져오기 (프론트엔드 파라미터 무시)
            String actualUserId = getUserId(request, null);
            logger.info("OAuth 시작 - 프론트엔드 요청 userId: {}, 실제 세션 userId: {}", userId, actualUserId);
            
            // state에 실제 사용자 ID와 projectId 정보를 포함 (JSON 형태)
            Map<String, String> stateData = new HashMap<>();
            stateData.put("uuid", java.util.UUID.randomUUID().toString());
            if (actualUserId != null && !actualUserId.trim().isEmpty()) {
                stateData.put("userId", actualUserId);
                logger.info("state에 실제 사용자 ID 포함: {}", actualUserId);
            }
            if (projectId != null && !projectId.trim().isEmpty()) {
                stateData.put("projectId", projectId);
                logger.info("state에 프로젝트 ID 포함: {}", projectId);
            }
            
            // 간단한 JSON 형태로 state 생성 (Jackson 대신 수동 생성)
            StringBuilder stateJson = new StringBuilder("{");
            stateJson.append("\"uuid\":\"").append(stateData.get("uuid")).append("\"");
            if (stateData.get("userId") != null) {
                stateJson.append(",\"userId\":\"").append(stateData.get("userId")).append("\"");
            }
            if (stateData.get("projectId") != null) {
                stateJson.append(",\"projectId\":\"").append(stateData.get("projectId")).append("\"");
            }
            stateJson.append("}");
            
            String state = java.util.Base64.getEncoder().encodeToString(stateJson.toString().getBytes("UTF-8"));
            
            session.setAttribute("githubOAuthState", state);
            
            // 백업용으로 세션에도 저장 (실제 사용자 ID 사용)
            if (projectId != null && !projectId.trim().isEmpty()) {
                session.setAttribute("githubOAuthProjectId", projectId);
            }
            if (actualUserId != null && !actualUserId.trim().isEmpty()) {
                session.setAttribute("githubOAuthUserId", actualUserId);
                logger.info("세션에 실제 사용자 ID 백업 저장: {}", actualUserId);
            }
            
            // GitHub OAuth URL 생성
            String redirectUri = request.getScheme() + "://" + request.getServerName() + 
                    ":" + request.getServerPort() + "/InsWebApp/github/auth/callback";
            
            String clientId = System.getProperty("GITHUB_CLIENT_ID", "Iv23liShQFpINkvH7lCV");
            String authUrl = "https://github.com/login/oauth/authorize?" +
                    "client_id=" + clientId + "&" +
                    "redirect_uri=" + java.net.URLEncoder.encode(redirectUri, "UTF-8") + "&" +
                    "scope=repo read:user admin:repo_hook&" +
                    "state=" + state;
            
            logger.info("=== GitHub OAuth 정보 ===");
            logger.info("Client ID: {}", clientId);
            logger.info("Redirect URI: {}", redirectUri);
            logger.info("State: {}", state);
            logger.info("Full OAuth URL: {}", authUrl);
            logger.info("========================");
            
            // Node.js 방식처럼 직접 리다이렉트
            return new ModelAndView("redirect:" + authUrl);
            
        } catch (Exception e) {
            logger.error("GitHub OAuth 인증 시작 실패", e);
            ModelAndView startErrorMv = new ModelAndView("/error");
            startErrorMv.addObject("error", "GitHub 인증을 시작할 수 없습니다: " + e.getMessage());
            return startErrorMv;
        }
    }
    
    
    /**
     * 서비스 의존성 문제로 모든 메서드 주석 처리
     * 필요한 것만 남겨두고 테스트
     */
    
    @ElService(key = "auth/callback")
    @ElDescription(sub = "GitHub OAuth 콜백 처리", desc = "GitHub OAuth 인증 콜백을 처리합니다.")
    @RequestMapping(value = "auth/callback", method = RequestMethod.GET)
    public ModelAndView handleGitHubCallback(
            @RequestParam(required = false) String code,
            @RequestParam(required = false) String state,
            @RequestParam(required = false) String error,
            HttpServletRequest request) {
        
        logger.info("GitHub OAuth 콜백 처리 시작");
        
        try {
            if (error != null) {
                logger.error("GitHub OAuth 인증 실패: {}", error);
                ModelAndView errorMv = new ModelAndView();
                errorMv.setViewName("redirect:/InsWebApp/websquare/websquare.html?w2xPath=/ui/github_callback.xml&status=error&message=" + 
                    java.net.URLEncoder.encode("GitHub 인증이 거부되었습니다.", "UTF-8"));
                return errorMv;
            }
            
            if (code == null) {
                logger.error("GitHub OAuth 코드가 없습니다");
                ModelAndView noCodeMv = new ModelAndView();
                noCodeMv.setViewName("redirect:/InsWebApp/websquare/websquare.html?w2xPath=/ui/github_callback.xml&status=error&message=" + 
                    java.net.URLEncoder.encode("인증 코드가 제공되지 않았습니다.", "UTF-8"));
                return noCodeMv;
            }
            
            HttpSession session = request.getSession();
            String sessionState = (String) session.getAttribute("githubOAuthState");
            
            if (!state.equals(sessionState)) {
                logger.error("GitHub OAuth state 불일치");
                ModelAndView stateMv = new ModelAndView();
                stateMv.setViewName("redirect:/InsWebApp/websquare/websquare.html?w2xPath=/ui/github_callback.xml&status=error&message=" + 
                    java.net.URLEncoder.encode("인증 상태가 유효하지 않습니다.", "UTF-8"));
                return stateMv;
            }
            
            // state에서 userId와 projectId 추출
            String stateUserId = null;
            String stateProjectId = null;
            try {
                // Base64 디코딩 후 간단한 JSON 파싱
                String stateJson = new String(java.util.Base64.getDecoder().decode(state), "UTF-8");
                logger.info("디코딩된 state JSON: {}", stateJson);
                
                // 간단한 JSON 파싱 (userId와 projectId 추출)
                if (stateJson.contains("\"userId\":\"")) {
                    int start = stateJson.indexOf("\"userId\":\"") + 10;
                    int end = stateJson.indexOf("\"", start);
                    if (end > start) {
                        stateUserId = stateJson.substring(start, end);
                    }
                }
                
                if (stateJson.contains("\"projectId\":\"")) {
                    int start = stateJson.indexOf("\"projectId\":\"") + 13;
                    int end = stateJson.indexOf("\"", start);
                    if (end > start) {
                        stateProjectId = stateJson.substring(start, end);
                    }
                }
                
                logger.info("state에서 추출된 userId: {}, projectId: {}", stateUserId, stateProjectId);
            } catch (Exception e) {
                logger.warn("state 파싱 실패, 세션에서 정보 확인: {}", e.getMessage());
            }
            
            // 서비스를 통해 OAuth 콜백 처리
            Map<String, Object> param = new HashMap<>();
            param.put("code", code);
            param.put("state", state);
            
            // 공통 getUserId 메소드 사용 (auth/status와 동일한 로직)
            String finalUserId = getUserId(request, stateUserId);
            
            if (finalUserId == null) {
                logger.error("사용자 ID를 찾을 수 없습니다. OAuth 연동을 진행할 수 없습니다.");
                ModelAndView userNotFoundMv = new ModelAndView();
                userNotFoundMv.setViewName("redirect:/InsWebApp/websquare/websquare.html?w2xPath=/ui/github_callback.xml&status=error&message=" + 
                    java.net.URLEncoder.encode("로그인된 사용자 정보를 찾을 수 없습니다. 다시 로그인해주세요.", "UTF-8"));
                return userNotFoundMv;
            }
            
            logger.info("디버깅 - 최종 사용할 userId: {}", finalUserId);
            param.put("user_id", finalUserId);
            
            // 프로젝트 ID 결정 (우선순위: state > 세션)
            String finalProjectId = null;
            if (stateProjectId != null && !stateProjectId.trim().isEmpty()) {
                finalProjectId = stateProjectId;
                logger.info("state에서 추출한 프로젝트 ID 사용: {}", finalProjectId);
            } else {
                finalProjectId = (String) session.getAttribute("githubOAuthProjectId");
                if (finalProjectId != null && !finalProjectId.trim().isEmpty()) {
                    logger.info("세션에서 프로젝트 ID 사용: {}", finalProjectId);
                } else {
                    logger.warn("프로젝트 ID를 찾을 수 없습니다.");
                }
            }
            
            if (finalProjectId != null && !finalProjectId.trim().isEmpty()) {
                param.put("project_id", finalProjectId);
            }
            
            Map<String, Object> authResult = gitHubService.processOAuthCallback(param);
            
            session.setAttribute("githubConnected", true);
            session.setAttribute("githubUsername", authResult.get("username"));
            session.setAttribute("githubAvatarUrl", authResult.get("avatar_url"));
            session.setAttribute("githubAccessToken", authResult.get("access_token"));
            
            logger.info("GitHub OAuth 콜백 처리 성공 - GitHub App 설치로 자동 진행");
            
            // OAuth 성공 후 바로 GitHub App 설치로 진행
            // 필요한 정보를 세션에 저장
            session.setAttribute("githubAppProjectRepoId", finalProjectId);
            session.setAttribute("githubAppUserId", finalUserId);
            
            // GitHub App 설치 URL로 바로 리다이렉트 (절대 경로 사용)
            String appInstallUrl = "/github/app/install";
            if (finalProjectId != null && !finalProjectId.trim().isEmpty()) {
                appInstallUrl += "?projectRepoId=" + java.net.URLEncoder.encode(finalProjectId, "UTF-8");
            }
            
            logger.info("OAuth 성공 후 GitHub App 설치로 자동 리다이렉트: {}", appInstallUrl);
            ModelAndView redirectMv = new ModelAndView();
            redirectMv.setViewName("redirect:" + appInstallUrl);
            return redirectMv;
            
        } catch (Exception e) {
            logger.error("GitHub OAuth 콜백 처리 실패", e);
            ModelAndView exceptionMv = new ModelAndView();
//            exceptionMv.setViewName("redirect:/InsWebApp/websquare/websquare.html?w2xPath=/ui/github_callback.xml&status=error&message=" + 
//                java.net.URLEncoder.encode("GitHub OAuth 처리 중 오류가 발생했습니다.", "UTF-8"));
            return exceptionMv;
        }
    }
    
    /**
     * GitHub App 설치 URL 생성
     */
    @ElService(key = "app/install")
    @ElDescription(sub = "GitHub App 설치", desc = "GitHub App 설치 URL을 생성하여 리다이렉트합니다.")
    @RequestMapping(value = "app/install", method = RequestMethod.GET)
    public ModelAndView startGitHubAppInstall(
            @RequestParam(required = false) String repoOwner,
            @RequestParam(required = false) String repoName,
            HttpServletRequest request) {
        
        logger.info("GitHub App 설치 시작: repoOwner={}, repoName={}", 
                   repoOwner, repoName);
        
        try {
            HttpSession session = request.getSession();
            
            // 현재 사용자 ID 가져오기
            String userId = getUserId(request, null);
            if (userId == null) {
                ModelAndView installErrorMv = new ModelAndView("/error");
                installErrorMv.addObject("error", "사용자 정보를 찾을 수 없습니다.");
                return installErrorMv;
            }
            
            // 설치 정보를 세션에 저장 (콜백에서 사용)
            if (repoOwner != null && !repoOwner.trim().isEmpty()) {
                session.setAttribute("githubAppRepoOwner", repoOwner);
            }
            if (repoName != null && !repoName.trim().isEmpty()) {
                session.setAttribute("githubAppRepoName", repoName);
            }
            session.setAttribute("githubAppUserId", userId);
            
            // GitHub App 설치 URL 생성
            String appName = System.getProperty("GITHUB_APP_NAME", "sixsense-collabee");
            String installUrl = "https://github.com/apps/" + appName + "/installations/new";
            
            // 특정 레포지토리 설치 시 추가 파라미터
            if (repoOwner != null && repoName != null) {
                try {
                    // 레포지토리 ID 조회가 필요한 경우 (선택사항)
                    installUrl += "?suggested_target_id=" + java.net.URLEncoder.encode(repoOwner, "UTF-8");
                } catch (Exception e) {
                    logger.warn("레포지토리 정보 URL 인코딩 실패", e);
                }
            }
            
            logger.info("GitHub App 설치 URL로 리다이렉트: {}", installUrl);
            
            return new ModelAndView("redirect:" + installUrl);
            
        } catch (Exception e) {
            logger.error("GitHub App 설치 시작 실패", e);
            ModelAndView appInstallErrorMv = new ModelAndView("/error");
            appInstallErrorMv.addObject("error", "GitHub App 설치를 시작할 수 없습니다: " + e.getMessage());
            return appInstallErrorMv;
        }
    }

    /**
     * GitHub App 설치 후 설정 완료 처리 (개선됨)
     * GitHub App Setup URL에 해당
     */
    @ElService(key = "app/setup")
    @ElDescription(sub = "GitHub App 설정 완료", desc = "GitHub App 설치 완료 후 설정을 처리합니다.")
    @RequestMapping(value = "app/setup")
    public ModelAndView handleGitHubAppSetup(
            @RequestParam(required = false) String installation_id,
            @RequestParam(required = false) String setup_action,
            HttpServletRequest request) {
        
        logger.info("GitHub App 설정 완료 처리: installation_id={}, setup_action={}", installation_id, setup_action);
        
        try {
            HttpSession session = request.getSession();
            
            if (installation_id == null || installation_id.trim().isEmpty()) {
                logger.error("Installation ID가 제공되지 않았습니다");
                ModelAndView noInstallationMv = new ModelAndView("/error");
                noInstallationMv.addObject("error", "GitHub App 설치 정보가 제공되지 않았습니다.");
                return noInstallationMv;
            }
            
            // 세션에서 설치 정보 가져오기
            String repoOwner = (String) session.getAttribute("githubAppRepoOwner");
            String repoName = (String) session.getAttribute("githubAppRepoName");
            String userId = (String) session.getAttribute("githubAppUserId");
            
            logger.info("세션에서 가져온 설치 정보 - repoOwner: {}, repoName: {}, userId: {}", 
                       repoOwner, repoName, userId);
            
            // Installation ID를 DB에 저장 (사용자 기준)
            if (userId != null && !userId.trim().isEmpty()) {
                try {
                    // 서비스를 통해 Installation ID 저장
                    Map<String, Object> param = new HashMap<>();
                    param.put("installation_id", installation_id);
                    param.put("setup_action", setup_action);
                    param.put("repo_owner", repoOwner);
                    param.put("repo_name", repoName);
                    param.put("user_id", userId);
                    
                    Map<String, Object> setupResult = gitHubService.processAppInstallation(param);
                    
                    if ((Boolean) setupResult.get("success")) {
                        logger.info("GitHub App Installation ID 저장 성공: {}", installation_id);
                        
                        // 세션에 성공 정보 저장
                        session.setAttribute("githubAppInstalled", true);
                        session.setAttribute("githubInstallationId", installation_id);
                        
                        // 설치 완료 후 세션 정리
                        session.removeAttribute("githubAppRepoOwner");
                        session.removeAttribute("githubAppRepoName");
                        session.removeAttribute("githubAppUserId");
                        
                        // GitHub 연동 성공 페이지로 리다이렉트 (OAuth + App 설치 모두 완료)
                        String username = (String) session.getAttribute("githubUsername");
                        String redirectUrl = String.format(
                            "/websquare/websquare.html?w2xPath=/InsWebApp/ui/github_callback.xml&status=success&username=%s&hasSelectedRepo=false&appInstalled=true&installation_id=%s", 
                            java.net.URLEncoder.encode(username != null ? username : "", "UTF-8"),
                            installation_id
                        );
                        return new ModelAndView("redirect:" + redirectUrl);
                    } else {
                        logger.error("GitHub App Installation ID 저장 실패: {}", setupResult.get("error"));
                        ModelAndView saveErrorMv = new ModelAndView("/error");
                        saveErrorMv.addObject("error", "GitHub App 설정 저장 실패: " + setupResult.get("error"));
                        return saveErrorMv;
                    }
                    
                } catch (Exception e) {
                    logger.error("Installation ID 저장 중 오류", e);
                    ModelAndView storageErrorMv = new ModelAndView("/error");
                    storageErrorMv.addObject("error", "GitHub App 설정 저장 중 오류가 발생했습니다: " + e.getMessage());
                    return storageErrorMv;
                }
            } else {
                logger.warn("ProjectRepoId가 없어서 Installation ID를 저장할 수 없습니다. 일반 설정 완료로 처리합니다.");
                
                // 일반적인 설치 완료 처리
                session.setAttribute("githubAppInstalled", true);
                session.setAttribute("githubInstallationId", installation_id);
                
                // 일반적인 GitHub 연동 성공 페이지로 리다이렉트
                String username = (String) session.getAttribute("githubUsername");
                String redirectUrl = String.format(
                    "/websquare/websquare.html?w2xPath=/InsWebApp/ui/github_callback.xml&status=success&username=%s&hasSelectedRepo=false&appInstalled=true&installation_id=%s", 
                    java.net.URLEncoder.encode(username != null ? username : "", "UTF-8"),
                    installation_id
                );
                return new ModelAndView("redirect:" + redirectUrl);
            }
            
        } catch (Exception e) {
            logger.error("GitHub App 설정 완료 처리 실패", e);
            ModelAndView setupErrorMv = new ModelAndView("/error");
            setupErrorMv.addObject("error", "GitHub App 설정 처리 중 오류가 발생했습니다: " + e.getMessage());
            return setupErrorMv;
        }
    }
    
    /**
     * 사용자 ID를 다양한 소스에서 일관된 우선순위로 가져오는 공통 메소드
     */
    private String getUserId(HttpServletRequest request, String stateUserId) {
        String userId = null;
        HttpSession session = request.getSession();
        
        // 1. state에서 추출한 userId (OAuth callback에서 전달된 최우선 정보)
        if (stateUserId != null && !stateUserId.trim().isEmpty() && !"user01".equals(stateUserId)) {
            userId = stateUserId;
            System.out.println("state에서 추출한 사용자 ID 사용: " + userId);
            return userId;
        }
        
        // 2. OAuth 시작 시 전달받은 userId
        String oauthUserId = (String) session.getAttribute("githubOAuthUserId");
        if (oauthUserId != null && !oauthUserId.trim().isEmpty() && !"user01".equals(oauthUserId)) {
            userId = oauthUserId;
            System.out.println("OAuth에서 전달받은 사용자 ID 사용: " + userId);
            return userId;
        }
        
        // 3. UserHeader에서 userId 가져오기
        try {
            Object userheader = session.getAttribute("userheader");
            if (userheader != null) {
                System.out.println("UserHeader 타입: " + userheader.getClass().getName());
                
                // ProworksUserHeader로 캐스팅하여 직접 접근
                if (userheader instanceof com.demo.proworks.cmmn.ProworksUserHeader) {
                    com.demo.proworks.cmmn.ProworksUserHeader proworksUserHeader = 
                        (com.demo.proworks.cmmn.ProworksUserHeader) userheader;
                    
                    String testId = proworksUserHeader.getTestId();
                    System.out.println("testId: " + testId);
                    
                    if (testId != null && !testId.trim().isEmpty() && !"user01".equals(testId)) {
                        userId = testId;
                        System.out.println("UserHeader에서 testId로 userId 가져옴: " + userId);
                        return userId;
                    }
                }
                
                // Reflection을 통해 getUserId() 메서드 호출 시도
                try {
                    java.lang.reflect.Method getUserIdMethod = userheader.getClass().getMethod("getUserId");
                    String reflectionUserId = (String) getUserIdMethod.invoke(userheader);
                    if (reflectionUserId != null && !reflectionUserId.trim().isEmpty() && !"user01".equals(reflectionUserId)) {
                        userId = reflectionUserId;
                        System.out.println("UserHeader에서 Reflection으로 userId 가져옴: " + userId);
                        return userId;
                    }
                } catch (Exception e) {
                    System.out.println("Reflection으로 getUserId 가져오기 실패: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            System.out.println("UserHeader에서 userId 가져오기 실패: " + e.getMessage());
        }
        
        // 4. 세션에서 다양한 키로 사용자 ID 찾기
        String[] userIdKeys = {"userId", "user_id", "userEmail", "user_email", "loginUserId", "login_user_id"};
        for (String key : userIdKeys) {
            String sessionUserId = (String) session.getAttribute(key);
            if (sessionUserId != null && !sessionUserId.trim().isEmpty() && !"user01".equals(sessionUserId)) {
                userId = sessionUserId;
                System.out.println("세션에서 사용자 ID 찾음 (키: {}): {}" + key + ", " + userId);
                return userId;
            }
        }
        
        // 5. ElHeader에서 사용자 정보 가져오기 시도
        Object elHeader = request.getAttribute("ElHeader");
        if (elHeader != null) {
            try {
                java.lang.reflect.Method getUserIdMethod = elHeader.getClass().getMethod("getUserId");
                String elHeaderUserId = (String) getUserIdMethod.invoke(elHeader);
                if (elHeaderUserId != null && !elHeaderUserId.trim().isEmpty() && !"user01".equals(elHeaderUserId)) {
                    userId = elHeaderUserId;
                    System.out.println("ElHeader에서 사용자 ID 가져옴: " + userId);
                    return userId;
                }
            } catch (Exception e) {
                System.out.println("ElHeader에서 사용자 ID 가져오기 실패: " + e.getMessage());
            }
        }
        
        // 6. request parameter에서 확인 (프론트엔드에서 전달)
        String paramUserId = request.getParameter("userId");
        if (paramUserId != null && !paramUserId.trim().isEmpty() && !"user01".equals(paramUserId)) {
            userId = paramUserId;
            System.out.println("request parameter에서 사용자 ID 찾음: " + userId);
            return userId;
        }
        
        System.out.println("모든 소스에서 사용자 ID를 찾을 수 없습니다.");
        return null;
    }

    /**
     * GitHub 연결 상태 확인 (의존성 문제로 주석 처리)
     */
    
    @ElService(key = "auth/status")
    @ElDescription(sub = "GitHub 연결 상태 확인", desc = "현재 GitHub 연결 상태를 확인합니다.")
    @RequestMapping(value = "auth/status")
    @ResponseBody
    public Map<String, Object> getGitHubAuthStatus(HttpServletRequest request) {
        logger.info("GitHub 연결 상태 확인");
        System.out.println("=== GitHub 인증 상태 확인 메소드 진입 ===");
        
        Map<String, Object> result = new HashMap<>();
        
        try {
            HttpSession session = request.getSession();
            
            // 공통 getUserId 메소드 사용 (OAuth callback과 동일한 로직)
            String userId = getUserId(request, null);
            System.out.println("최종 사용할 userId: " + userId);
            
            // 먼저 세션에서 GitHub 인증 상태 확인
            Boolean githubConnected = (Boolean) session.getAttribute("githubConnected");
            String sessionAccessToken = (String) session.getAttribute("githubAccessToken");
            
            // DB에서 저장된 access token 확인
            String dbAccessToken = null;
            Map<String, Object> dbUserInfo = null;
            
            if (userId != null) {
                try {
                    // 서비스를 통해 DB에서 GitHub 토큰 정보 조회
                    com.demo.proworks.userpersonaltoken.vo.UserPersonalTokenVo tokenParam = 
                        new com.demo.proworks.userpersonaltoken.vo.UserPersonalTokenVo();
                    tokenParam.setUserId(userId);
                    
                    com.demo.proworks.userpersonaltoken.vo.UserPersonalTokenVo tokenInfo = 
                        userPersonalTokenService.selectUserPersonalTokenByUserId(tokenParam);
                    
                    if (tokenInfo != null) {
                        dbAccessToken = tokenInfo.getAccessToken();
                        System.out.println("DB에서 GitHub 토큰 발견 (userId: " + userId + ")");
                        
                        // DB에서 찾은 토큰으로 GitHub 사용자 정보 조회
                        try {
                            Map<String, Object> userInfo = gitHubApiUtil.getUserInfo(dbAccessToken);
                            dbUserInfo = userInfo;
                            System.out.println("DB 토큰으로 GitHub 사용자 정보 조회 성공");
                        } catch (Exception e) {
                            System.out.println("DB 토큰으로 GitHub 사용자 정보 조회 실패: " + e.getMessage());
                            // 토큰이 만료된 경우 null로 설정
                            dbAccessToken = null;
                        }
                    } else {
                        System.out.println("DB에서 GitHub 토큰을 찾을 수 없습니다 (userId: " + userId + ")");
                    }
                    
                } catch (Exception e) {
                    System.out.println("DB에서 GitHub 토큰 조회 실패: " + e.getMessage());
                }
            }
            
            // 세션에 토큰이 있거나 DB에서 토큰을 찾은 경우
            boolean isAuthenticated = false;
            if (sessionAccessToken != null) {
                System.out.println("세션에서 access token 발견");
                isAuthenticated = true;
            } else if (dbAccessToken != null) {
                System.out.println("DB에서 access token 발견, 세션에 저장");
                // DB에서 찾은 토큰을 세션에 저장
                session.setAttribute("githubAccessToken", dbAccessToken);
                session.setAttribute("githubConnected", true);
                
                // DB에서 조회한 GitHub 사용자 정보도 세션에 저장
                if (dbUserInfo != null) {
                    session.setAttribute("githubUsername", dbUserInfo.get("login"));
                    session.setAttribute("githubAvatarUrl", dbUserInfo.get("avatar_url"));
                    System.out.println("DB 토큰으로 GitHub 사용자 정보 세션에 저장: " + dbUserInfo.get("login"));
                }
                
                isAuthenticated = true;
            }
            
            result.put("authenticated", isAuthenticated);
            result.put("success", true);
            
            // 인증된 경우 사용자 정보 추가
            if (isAuthenticated) {
                Map<String, Object> user = new HashMap<>();
                user.put("login", session.getAttribute("githubUsername"));
                user.put("avatar_url", session.getAttribute("githubAvatarUrl"));
                result.put("user", user);
                
                // GitHub App 설치 여부 확인
                Boolean hasSelectedRepo = (Boolean) session.getAttribute("selectedRepository");
                result.put("hasSelectedRepo", hasSelectedRepo != null ? hasSelectedRepo : false);
            }
            
        } catch (Exception e) {
            logger.error("GitHub 연결 상태 확인 실패", e);
            result.put("success", false);
            result.put("error", e.getMessage());
        }
        
        return result;
    }
    

    // ==============================
    // GitHub 레포지토리 관리
    // ==============================
    
    /**
     * GitHub API 프록시 - 레포지토리 목록 조회
     * 프론트엔드에서 CORS 문제 없이 GitHub API 호출할 수 있도록 프록시 역할
     */
    @ElService(key = "api/repositories")
    @ElDescription(sub = "GitHub API 프록시 - 레포지토리 목록", desc = "GitHub API를 프록시하여 레포지토리 목록을 조회합니다.")
    @RequestMapping(value = "api/repositories")
    @ResponseBody
    public String getRepositoriesProxy(
            @RequestParam(defaultValue = "updated") String sort,
            @RequestParam(defaultValue = "100") int per_page,
            @RequestParam(defaultValue = "owner,collaborator") String affiliation,
            HttpServletRequest request) {
        
        logger.info("GitHub API 프록시 - 레포지토리 목록 조회 시작");
        
        try {
            HttpSession session = request.getSession();
            String accessToken = (String) session.getAttribute("githubAccessToken");
            
            if (accessToken == null) {
                throw new RuntimeException("GitHub 인증이 필요합니다.");
            }
            
            // GitHub API 직접 호출
            String url = "https://api.github.com/user/repos?sort=" + sort + 
                        "&per_page=" + per_page + "&affiliation=" + affiliation;
            
            java.net.URL apiUrl = new java.net.URL(url);
            java.net.HttpURLConnection conn = (java.net.HttpURLConnection) apiUrl.openConnection();
            
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", "token " + accessToken);
            conn.setRequestProperty("Accept", "application/vnd.github.v3+json");
            conn.setRequestProperty("User-Agent", "InsWebApp/1.0");
            
            int responseCode = conn.getResponseCode();
            logger.info("GitHub API 응답 코드: {}", responseCode);
            
            java.io.BufferedReader reader;
            if (responseCode >= 200 && responseCode < 300) {
                reader = new java.io.BufferedReader(new java.io.InputStreamReader(conn.getInputStream()));
            } else {
                reader = new java.io.BufferedReader(new java.io.InputStreamReader(conn.getErrorStream()));
            }
            
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            
            if (responseCode >= 200 && responseCode < 300) {
                logger.info("GitHub API 레포지토리 목록 조회 성공");
                return response.toString();
            } else {
                logger.error("GitHub API 오류: {} - {}", responseCode, response.toString());
                throw new RuntimeException("GitHub API 호출 실패: " + responseCode);
            }
            
        } catch (Exception e) {
            logger.error("GitHub API 프록시 실패", e);
            return "{\"error\": \"" + e.getMessage() + "\"}";
        }
    }
    
    /**
     * 레포지토리 선택 처리
     * test의 /auth/select-repo 에 해당
     */
    @ElService(key = "repositories/select")
    @ElDescription(sub = "레포지토리 선택", desc = "사용자가 선택한 레포지토리를 등록합니다.")
    @RequestMapping(value = "repositories/select")
    @ResponseBody
    public Map<String, Object> selectRepository(ProjectRepositoryVo projectRepositoryVo,
            HttpServletRequest request) {
        
        System.out.println("=== 컨트롤러 selectRepository 메소드 시작 ===");
        System.out.println("받은 ProjectRepositoryVo: " + projectRepositoryVo);
        
        Map<String, Object> result = new HashMap<>();
        
        try {
            HttpSession session = request.getSession();
            String userId = null;
            
            // ProWorks UserHeader에서 userId 가져오기
            try {
                Object userHeader = session.getAttribute("userHeader");
                System.out.println("세션의 userHeader: " + userHeader);
                
                if (userHeader != null) {
                    // userHeader가 ProworksUserHeader 타입이라면
                    if (userHeader instanceof com.demo.proworks.cmmn.ProworksUserHeader) {
                        com.demo.proworks.cmmn.ProworksUserHeader proworksUserHeader = (com.demo.proworks.cmmn.ProworksUserHeader) userHeader;
                        userId = proworksUserHeader.getUserId();
                        System.out.println("ProworksUserHeader에서 가져온 userId: " + userId);
                    } else {
                        // 다른 타입이면 reflection으로 userId 필드 접근
                        try {
                            java.lang.reflect.Field userIdField = userHeader.getClass().getDeclaredField("userId");
                            userIdField.setAccessible(true);
                            userId = (String) userIdField.get(userHeader);
                            System.out.println("Reflection으로 가져온 userId: " + userId);
                        } catch (Exception e) {
                            System.out.println("Reflection으로 userId 가져오기 실패: " + e.getMessage());
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println("UserHeader에서 userId 가져오기 실패: " + e.getMessage());
            }
            
            // 세션에서 직접 userId 가져오기 시도
            if (userId == null) {
                userId = (String) session.getAttribute("userId");
                System.out.println("세션에서 직접 가져온 userId: " + userId);
            }
            
            System.out.println("최종 사용할 userId: " + userId);
            
            if (userId == null) {
                System.out.println("userId가 null입니다. 로그인 필요");
                result.put("success", false);
                result.put("error", "로그인이 필요합니다.");
                return result;
            }
            
            // DB 저장을 위한 파라미터 구성
            Map<String, Object> param = new HashMap<>();
            param.put("project_id", projectRepositoryVo.getProjectId());
            param.put("github_repository_id", projectRepositoryVo.getGithubRepositoryId());
            param.put("repo_owner", projectRepositoryVo.getRepoOwner());
            param.put("repo_name", projectRepositoryVo.getRepoName());
            param.put("default_branch", projectRepositoryVo.getDefaultBranch());
            param.put("github_app_installation_id", projectRepositoryVo.getGithubAppInstallationId());
            
            System.out.println("컨트롤러 - DB 저장 파라미터: " + param);
            
            // 서비스를 통해 레포지토리 선택 처리
            System.out.println("서비스 호출 시작");
            Map<String, Object> selectResult = gitHubService.selectRepository(param);
            System.out.println("서비스 호출 완료, 결과: " + selectResult);
            
            result.putAll(selectResult);
            
            if ((Boolean) selectResult.get("success")) {
                session.setAttribute("selectedRepository", true);
                System.out.println("레포지토리 선택 성공");
                
                // 신규 연결이거나 저장소 변경된 경우에만 초기 브랜치 동기화 수행
                boolean shouldSyncBranches = "created".equals(selectResult.get("action")) || 
                                           (Boolean.TRUE.equals(selectResult.get("repository_changed")));
                
                if (shouldSyncBranches) {
                    System.out.println("초기 브랜치 동기화 필요 - 이유: " + 
                        ("created".equals(selectResult.get("action")) ? "신규 저장소 연결" : "저장소 변경"));
                } else {
                    System.out.println("기존 저장소 정보만 업데이트 - 브랜치 동기화 스킵");
                }
                
                // 저장소 연결/변경 시 초기 브랜치 동기화 수행
                if (shouldSyncBranches) {
                    try {
                    System.out.println("=== 초기 브랜치 동기화 시작 ===");
                    
                    // GitHub 토큰 조회
                    String accessToken = null;
                    com.demo.proworks.userpersonaltoken.vo.UserPersonalTokenVo tokenParam = 
                        new com.demo.proworks.userpersonaltoken.vo.UserPersonalTokenVo();
                    tokenParam.setUserId(userId);
                    
                    com.demo.proworks.userpersonaltoken.vo.UserPersonalTokenVo tokenInfo = 
                        userPersonalTokenService.selectUserPersonalTokenByUserId(tokenParam);
                    
                    if (tokenInfo != null) {
                        accessToken = tokenInfo.getAccessToken();
                    }
                    
                    if (accessToken != null) {
                        // 브랜치 동기화 파라미터 구성
                        Map<String, Object> syncParam = new HashMap<>();
                        syncParam.put("access_token", accessToken);
                        syncParam.put("owner", projectRepositoryVo.getRepoOwner());
                        syncParam.put("repo", projectRepositoryVo.getRepoName());
                        syncParam.put("project_repo_id", selectResult.get("record_id"));
                        
                        System.out.println("브랜치 동기화 파라미터: " + syncParam);
                        
                        // 초기 브랜치 동기화 실행
                        Map<String, Object> syncResult = gitHubService.syncInitialBranches(syncParam);
                        System.out.println("브랜치 동기화 결과: " + syncResult);
                        
                        // 동기화 결과를 메인 결과에 추가
                        result.put("branch_sync", syncResult);
                        
                        if ((Boolean) syncResult.get("success")) {
                            System.out.println("초기 브랜치 동기화 성공 - 저장된 브랜치: " + syncResult.get("saved_count") + "개");
                        } else {
                            System.out.println("초기 브랜치 동기화 실패: " + syncResult.get("error"));
                        }
                        
                    } else {
                        System.out.println("GitHub 토큰이 없어 브랜치 동기화 스킵");
                        Map<String, Object> branchSyncError = new HashMap<>();
                        branchSyncError.put("success", false);
                        branchSyncError.put("error", "GitHub 토큰 없음");
                        result.put("branch_sync", branchSyncError);
                    }
                    
                } catch (Exception e) {
                    System.out.println("초기 브랜치 동기화 중 오류 발생: " + e.getMessage());
                    e.printStackTrace();
                    // 브랜치 동기화 실패는 저장소 연결 성공에 영향을 주지 않음
                    Map<String, Object> branchSyncException = new HashMap<>();
                    branchSyncException.put("success", false);
                    branchSyncException.put("error", e.getMessage());
                    result.put("branch_sync", branchSyncException);
                }
                
                System.out.println("=== 초기 브랜치 동기화 완료 ===");
                
                } else {
                    // 브랜치 동기화가 필요 없는 경우
                    Map<String, Object> branchSyncSkip = new HashMap<>();
                    branchSyncSkip.put("success", true);
                    branchSyncSkip.put("skipped", true);
                    branchSyncSkip.put("reason", "기존 저장소 정보만 업데이트");
                    result.put("branch_sync", branchSyncSkip);
                }
            }
            
        } catch (Exception e) {
            System.out.println("컨트롤러 - 레포지토리 선택 처리 실패: " + e.getMessage());
            e.printStackTrace();
            result.put("success", false);
            result.put("error", e.getMessage());
        }
        
        System.out.println("컨트롤러 - 최종 결과: " + result);
        return result;
    }
    
    /**
     * 현재 프로젝트의 연결된 저장소 조회
     */
    @ElService(key = "project/currentRepository")
    @ElDescription(sub = "현재 프로젝트 연결된 저장소 조회", desc = "현재 프로젝트에 연결된 저장소 정보를 조회합니다.")
    @RequestMapping(value = "project/currentRepository")
    @ResponseBody
    public Map<String, Object> getCurrentRepository(
            @RequestParam(value = "projectId", required = true) String projectId,
            HttpServletRequest request) {
        
        System.out.println("=== 현재 프로젝트 연결된 저장소 조회 API 호출 ===");
        System.out.println("projectId: " + projectId);
        
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 프로젝트 ID로 연결된 저장소 조회
            com.demo.proworks.projectrepo.vo.ProjectRepositoryVo repository = 
                gitHubService.getCurrentRepositoryByProjectId(projectId);
            
            if (repository != null) {
                System.out.println("연결된 저장소 발견: " + repository.getRepoOwner() + "/" + repository.getRepoName());
                
                // 저장소 정보를 Map으로 변환
                Map<String, Object> repoInfo = new HashMap<>();
                repoInfo.put("projectRepoId", repository.getProjectRepoId());
                repoInfo.put("projectId", repository.getProjectId());
                repoInfo.put("githubRepositoryId", repository.getGithubRepositoryId());
                repoInfo.put("repoOwner", repository.getRepoOwner());
                repoInfo.put("repoName", repository.getRepoName());
                repoInfo.put("defaultBranch", repository.getDefaultBranch());
                repoInfo.put("githubAppInstallationId", repository.getGithubAppInstallationId());
                
                result.put("success", true);
                result.put("hasRepository", true);
                result.put("repository", repoInfo);
                result.put("message", "연결된 저장소 정보 조회 완료");
            } else {
                System.out.println("연결된 저장소 없음");
                result.put("success", true);
                result.put("hasRepository", false);
                result.put("message", "연결된 저장소 없음");
            }
            
        } catch (Exception e) {
            System.out.println("현재 프로젝트 연결된 저장소 조회 실패: " + e.getMessage());
            e.printStackTrace();
            result.put("success", false);
            result.put("error", e.getMessage());
        }
        
        System.out.println("현재 프로젝트 연결된 저장소 조회 결과: " + result);
        return result;
    }
    
    // ==============================
    // GitHub 연결 상태 체크 API
    // ==============================
    
    /**
     * GitHub 연결 상태를 확인하고 필요한 단계를 안내한다.
     */
    @ElService(key = "connection/status")
    @ElDescription(sub = "GitHub 연결 상태 확인", desc = "OAuth와 GitHub App 설치 상태를 확인하여 필요한 단계를 안내합니다.")
    @RequestMapping(value = "connection/status")
    @ResponseBody
    public Map<String, Object> checkGitHubConnectionStatus(HttpServletRequest request) {
        
        logger.info("=== GitHub 연결 상태 확인 API 호출 ===");
        
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 현재 사용자 ID 가져오기
            String userId = getUserId(request, null);
            if (userId == null) {
                result.put("success", false);
                result.put("error", "사용자 정보를 찾을 수 없습니다.");
                return result;
            }
            
            logger.info("사용자 ID: {}", userId);
            
            // 1단계: OAuth Token 확인
            try {
                com.demo.proworks.userpersonaltoken.service.UserPersonalTokenService userPersonalTokenService = 
                    (com.demo.proworks.userpersonaltoken.service.UserPersonalTokenService) 
                    org.springframework.web.context.support.WebApplicationContextUtils
                        .getWebApplicationContext(request.getServletContext())
                        .getBean("userPersonalTokenServiceImpl");
                
                String personalToken = userPersonalTokenService.getToken(userId);
                
                if (personalToken == null || personalToken.trim().isEmpty()) {
                    logger.info("OAuth 토큰 없음 - OAuth 인증 필요");
                    result.put("success", true);
                    result.put("status", "oauth_required");
                    result.put("message", "GitHub OAuth 인증이 필요합니다.");
                    result.put("next_step", "oauth_auth");
                    result.put("redirect_url", "/InsWebApp/github/auth");
                    return result;
                }
                
                logger.info("OAuth 토큰 확인됨");
                
            } catch (Exception e) {
                logger.warn("OAuth 토큰 조회 실패: {}", e.getMessage());
                result.put("success", true);
                result.put("status", "oauth_required");
                result.put("message", "GitHub OAuth 인증이 필요합니다.");
                result.put("next_step", "oauth_auth");
                result.put("redirect_url", "/InsWebApp/github/auth");
                return result;
            }
            
            // 2단계: Installation ID 확인
            try {
                com.demo.proworks.githubapptoken.service.GithubAppTokenService githubAppTokenService = 
                    (com.demo.proworks.githubapptoken.service.GithubAppTokenService) 
                    org.springframework.web.context.support.WebApplicationContextUtils
                        .getWebApplicationContext(request.getServletContext())
                        .getBean("githubAppTokenService");
                
                com.demo.proworks.githubapptoken.vo.GithubAppTokenVo appToken = 
                    githubAppTokenService.selectGithubAppTokenByUserId(userId);
                
                if (appToken == null || appToken.getGithubAppInstallationId() == null || appToken.getGithubAppInstallationId().trim().isEmpty()) {
                    logger.info("Installation ID 없음 - GitHub App 설치 필요");
                    result.put("success", true);
                    result.put("status", "app_install_required");
                    result.put("message", "GitHub App 설치가 필요합니다.");
                    result.put("next_step", "app_install");
                    result.put("redirect_url", "/InsWebApp/github/app/install");
                    return result;
                }
                
                logger.info("Installation ID 확인됨: {}", appToken.getGithubAppInstallationId());
                
                // 3단계: 완전 연동 완료
                result.put("success", true);
                result.put("status", "fully_connected");
                result.put("message", "GitHub 연동이 완료되었습니다.");
                result.put("oauth_available", true);
                result.put("app_installed", true);
                result.put("installation_id", appToken.getGithubAppInstallationId());
                
                return result;
                
            } catch (Exception e) {
                logger.warn("Installation ID 조회 실패: {}", e.getMessage());
                result.put("success", true);
                result.put("status", "app_install_required");
                result.put("message", "GitHub App 설치가 필요합니다.");
                result.put("next_step", "app_install");
                result.put("redirect_url", "/InsWebApp/github/app/install");
                return result;
            }
            
        } catch (Exception e) {
            logger.error("GitHub 연결 상태 확인 실패", e);
            result.put("success", false);
            result.put("error", "연결 상태 확인 중 오류가 발생했습니다: " + e.getMessage());
            return result;
        }
    }
    
    /**
     * GitHub 연결 상태를 확인하고 필요시 자동 리다이렉트한다.
     */
    @ElService(key = "connection/check-and-redirect")
    @ElDescription(sub = "GitHub 연결 체크 및 리다이렉트", desc = "GitHub 연결 상태를 확인하고 필요한 단계로 자동 리다이렉트합니다.")
    @RequestMapping(value = "connection/check-and-redirect", method = RequestMethod.GET)
    public ModelAndView checkAndRedirectGitHubConnection(HttpServletRequest request) {
        
        logger.info("=== GitHub 연결 체크 및 리다이렉트 ===");
        
        try {
            Map<String, Object> statusResult = checkGitHubConnectionStatus(request);
            
            if (!(Boolean) statusResult.get("success")) {
                ModelAndView errorMv = new ModelAndView("/error");
                errorMv.addObject("error", statusResult.get("error"));
                return errorMv;
            }
            
            String status = (String) statusResult.get("status");
            String redirectUrl = (String) statusResult.get("redirect_url");
            
            switch (status) {
                case "oauth_required":
                    logger.info("OAuth 인증 필요 - 리다이렉트: {}", redirectUrl);
                    return new ModelAndView("redirect:" + redirectUrl);
                    
                case "app_install_required":
                    logger.info("GitHub App 설치 필요 - 리다이렉트: {}", redirectUrl);
                    return new ModelAndView("redirect:" + redirectUrl);
                    
                case "fully_connected":
                    logger.info("GitHub 연동 완료 - 메인 페이지로 이동");
                    ModelAndView successMv = new ModelAndView();
                    successMv.setViewName("redirect:/InsWebApp/websquare/websquare.html?w2xPath=/ui/github_main.xml");
                    successMv.addObject("status", "connected");
                    successMv.addObject("message", "GitHub 연동이 완료되었습니다.");
                    return successMv;
                    
                default:
                    ModelAndView unknownMv = new ModelAndView("/error");
                    unknownMv.addObject("error", "알 수 없는 연결 상태입니다.");
                    return unknownMv;
            }
            
        } catch (Exception e) {
            logger.error("GitHub 연결 체크 및 리다이렉트 실패", e);
            ModelAndView exceptionMv = new ModelAndView("/error");
            exceptionMv.addObject("error", "연결 상태 확인 중 오류가 발생했습니다: " + e.getMessage());
            return exceptionMv;
        }
    }

    // ==============================
    // 브랜치 관리 API
    // ==============================
    
    /**
     * 저장소 브랜치 목록 조회
     */
    @ElService(key = "branches")
    @ElDescription(sub = "브랜치 목록 조회", desc = "선택된 저장소의 브랜치 목록을 조회합니다.")
    @RequestMapping(value = "branches")
    @ResponseBody
    public Map<String, Object> getBranches(
        BranchParameterVo branchParameterVo,
            HttpServletRequest request) {
        
        String owner = branchParameterVo.getOwner();
        String repo = branchParameterVo.getRepo();
        String projectId = branchParameterVo.getProjectId();
        System.out.println("=== 브랜치 목록 조회 API 호출 ===");
        System.out.println("owner: " + owner);
        System.out.println("repo: " + repo);
        System.out.println("projectId: " + projectId);
        
        Map<String, Object> result = new HashMap<>();
        
        // 성능 측정 시작
        long startTime = System.currentTimeMillis();
        
        try {
            HttpSession session = request.getSession();
            String userId = null;
            
            // UserHeader에서 userId 가져오기
            try {
                Object userHeader = session.getAttribute("userHeader");
                if (userHeader != null && userHeader instanceof com.demo.proworks.cmmn.ProworksUserHeader) {
                    com.demo.proworks.cmmn.ProworksUserHeader proworksUserHeader = (com.demo.proworks.cmmn.ProworksUserHeader) userHeader;
                    userId = proworksUserHeader.getUserId();
                }
            } catch (Exception e) {
                System.out.println("UserHeader에서 userId 가져오기 실패: " + e.getMessage());
            }
            
            if (userId == null) {
                userId = (String) session.getAttribute("userId");
            }
            
            System.out.println("userId: " + userId);
            
            if (userId == null) {
                result.put("success", false);
                result.put("error", "로그인이 필요합니다.");
                return result;
            }
            
            // 세션에서 userId를 가져와서 DB에서 GitHub 토큰 조회
            String accessToken = null;
            
            if (userId != null) {
                try {
                    System.out.println("DB에서 GitHub 토큰 조회 시도 (userId: " + userId + ")");
                    
                    com.demo.proworks.userpersonaltoken.vo.UserPersonalTokenVo tokenParam = 
                        new com.demo.proworks.userpersonaltoken.vo.UserPersonalTokenVo();
                    tokenParam.setUserId(userId);
                    
                    com.demo.proworks.userpersonaltoken.vo.UserPersonalTokenVo tokenInfo = 
                        userPersonalTokenService.selectUserPersonalTokenByUserId(tokenParam);
                    
                    if (tokenInfo != null) {
                        accessToken = tokenInfo.getAccessToken();
                        System.out.println("DB에서 GitHub 토큰 발견");
                    } else {
                        System.out.println("DB에서 GitHub 토큰을 찾을 수 없습니다 (userId: " + userId + ")");
                    }
                    
                } catch (Exception e) {
                    System.out.println("DB에서 GitHub 토큰 조회 실패: " + e.getMessage());
                    e.printStackTrace();
                }
            }
            
            System.out.println("최종 accessToken 존재 여부: " + (accessToken != null));
            
            if (accessToken == null) {
                result.put("success", false);
                result.put("error", "GitHub 인증이 필요합니다.");
                return result;
            }
            
            // 프로젝트 ID가 파라미터로 전달되지 않은 경우 세션에서 확인
            if (projectId == null) {
                projectId = (String) session.getAttribute("currentProjectId");
                if (projectId == null) {
                    // 파라미터에서 프로젝트 ID 추출 시도
                    Object projectIdObj = request.getParameter("projectId");
                    if (projectIdObj != null) {
                        projectId = projectIdObj.toString();
                    }
                }
            }
            
            System.out.println("프로젝트 ID: " + projectId);
            
            if (projectId == null) {
                result.put("success", false);
                result.put("error", "프로젝트 정보가 필요합니다.");
                return result;
            }
            
            // DB에서 브랜치 목록 조회 (성능 최적화)
            System.out.println("DB에서 브랜치 목록 조회 시작");
            
            try {
                // 프로젝트에 연결된 저장소 정보 조회
                ProjectRepositoryVo repoInfo = gitHubService.getCurrentRepositoryByProjectId(projectId);
                
                if (repoInfo == null) {
                    result.put("success", false);
                    result.put("error", "프로젝트에 연결된 저장소가 없습니다.");
                    return result;
                }
                
                // owner/repo와 DB 정보 일치 확인
                if (!owner.equals(repoInfo.getRepoOwner()) || !repo.equals(repoInfo.getRepoName())) {
                    logger.warn("요청한 저장소와 DB 저장소 불일치 - 요청: {}/{}, DB: {}/{}", 
                              owner, repo, repoInfo.getRepoOwner(), repoInfo.getRepoName());
                    result.put("success", false);
                    result.put("error", "저장소 정보가 일치하지 않습니다.");
                    return result;
                }
                
                // DB에서 브랜치 목록 조회 - Spring ApplicationContext에서 DAO 빈 가져오기
                com.demo.proworks.github.dao.GitHubDAO gitHubDAO = (com.demo.proworks.github.dao.GitHubDAO) 
                    org.springframework.web.context.support.WebApplicationContextUtils
                        .getWebApplicationContext(request.getServletContext())
                        .getBean("gitHubDAO");
                
                Map<String, Object> branchParam = new HashMap<>();
                branchParam.put("project_repo_id", repoInfo.getProjectRepoId());
                
                List<com.demo.proworks.repobranch.vo.RepositoryBranchVo> branches = 
                    gitHubDAO.selectRepositoryBranches(branchParam);
                
                System.out.println("DB에서 조회된 브랜치 수: " + (branches != null ? branches.size() : 0));
                
                // GitHub API 형태로 데이터 변환
                List<Map<String, Object>> formattedBranches = new java.util.ArrayList<>();
                
                if (branches != null && !branches.isEmpty()) {
                    for (com.demo.proworks.repobranch.vo.RepositoryBranchVo branch : branches) {
                        Map<String, Object> branchData = new HashMap<>();
                        branchData.put("name", branch.getBranchName());
                        branchData.put("commit", new HashMap<String, Object>() {{
                            put("sha", branch.getBaseSha());
                            put("url", ""); // DB에는 저장하지 않는 정보
                        }});
                        branchData.put("protected", false); // DB에는 저장하지 않는 정보
                        formattedBranches.add(branchData);
                    }
                    
                    result.put("success", true);
                    result.put("message", "브랜치 목록 조회 성공 (DB 기반 - 최적화됨)");
                    result.put("data", formattedBranches);
                    result.put("source", "database"); // DB에서 조회했음을 표시
                    
                } else {
                    // DB에 브랜치가 없을 때 폴백: GitHub API 직접 조회 + 동기화
                    System.out.println("⚠️ DB에 브랜치가 없음 - GitHub API 폴백 실행");
                    
                    try {
                        // GitHub API로 브랜치 조회
                        Map<String, Object> apiResponse = gitHubApiUtil.getBranches(accessToken, owner, repo);
                        
                        if ((Boolean) apiResponse.get("success")) {
                            // 폴백으로 조회한 브랜치들을 DB에 저장
                            Map<String, Object> syncParam = new HashMap<>();
                            syncParam.put("access_token", accessToken);
                            syncParam.put("owner", owner);
                            syncParam.put("repo", repo);
                            syncParam.put("project_repo_id", repoInfo.getProjectRepoId());
                            
                            // 비동기로 브랜치 동기화 (응답 지연 방지)
                            try {
                                gitHubService.syncInitialBranches(syncParam);
                                System.out.println("폴백 브랜치 동기화 완료");
                            } catch (Exception e) {
                                System.out.println("폴백 브랜치 동기화 실패: " + e.getMessage());
                            }
                            
                            result.put("success", true);
                            result.put("message", "브랜치 목록 조회 성공 (GitHub API 폴백)");
                            result.put("data", apiResponse.get("data"));
                            result.put("source", "github_api_fallback"); // 폴백으로 조회했음을 표시
                            
                        } else {
                            result.put("success", false);
                            result.put("error", "브랜치 조회 실패: " + apiResponse.get("data"));
                        }
                        
                    } catch (Exception e) {
                        System.out.println("GitHub API 폴백 실패: " + e.getMessage());
                        result.put("success", false);
                        result.put("error", "브랜치 조회 실패 (DB 없음, API 오류): " + e.getMessage());
                    }
                }
                
            } catch (Exception e) {
                System.out.println("DB 브랜치 조회 오류: " + e.getMessage());
                e.printStackTrace();
                result.put("success", false);
                result.put("error", "브랜치 목록 조회 오류: " + e.getMessage());
            }
            
        } catch (Exception e) {
            System.out.println("브랜치 목록 조회 실패: " + e.getMessage());
            e.printStackTrace();
            result.put("success", false);
            result.put("error", e.getMessage());
        }
        
        // 성능 측정 종료 및 결과 로그
        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;
        
        System.out.println("브랜치 목록 조회 결과: " + result);
        System.out.println("=== 성능 측정 결과 ===");
        System.out.println("실행 시간: " + executionTime + "ms");
        System.out.println("조회 방식: DB 기반 (최적화됨)");
        System.out.println("목표 달성 여부: " + (executionTime <= 50 ? "✅ 성공 (10-50ms 목표)" : "⚠️ 개선 필요"));
        System.out.println("===================");
        
        // 성능 정보를 결과에 추가
        result.put("execution_time_ms", executionTime);
        result.put("performance_optimized", true);
        result.put("query_method", "database");
        
        return result;
    }
    
    /**
     * 브랜치 생성
     */
    @ElService(key = "branches/create")
    @ElDescription(sub = "브랜치 생성", desc = "새로운 브랜치를 생성합니다.")
    @RequestMapping(value = "branches/create", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> createBranch(
            BranchParameterVo branchParameterVo,
            HttpServletRequest request) {
        
        String owner = branchParameterVo.getOwner();
        String repo = branchParameterVo.getRepo();
        String branchName = branchParameterVo.getBranchName();
        String fromBranch = branchParameterVo.getFromBranch();
        
        System.out.println("=== 브랜치 생성 API 호출 ===");
        System.out.println("owner: " + owner);
        System.out.println("repo: " + repo);
        System.out.println("branchName: " + branchName);
        System.out.println("fromBranch: " + fromBranch);
        
        Map<String, Object> result = new HashMap<>();
        
        try {
            HttpSession session = request.getSession();
            String userId = null;
            
            // UserHeader에서 userId 가져오기 (getBranches와 동일한 로직)
            try {
                Object userHeader = session.getAttribute("userHeader");
                if (userHeader != null && userHeader instanceof com.demo.proworks.cmmn.ProworksUserHeader) {
                    com.demo.proworks.cmmn.ProworksUserHeader proworksUserHeader = (com.demo.proworks.cmmn.ProworksUserHeader) userHeader;
                    userId = proworksUserHeader.getUserId();
                }
            } catch (Exception e) {
                System.out.println("UserHeader에서 userId 가져오기 실패: " + e.getMessage());
            }
            
            if (userId == null) {
                userId = (String) session.getAttribute("userId");
            }
            
            System.out.println("userId: " + userId);
            
            if (userId == null) {
                result.put("success", false);
                result.put("error", "로그인이 필요합니다.");
                return result;
            }
            
            // 세션에서 userId를 가져와서 DB에서 GitHub 토큰 조회
            String accessToken = null;
            
            if (userId != null) {
                try {
                    System.out.println("DB에서 GitHub 토큰 조회 시도 (userId: " + userId + ")");
                    
                    com.demo.proworks.userpersonaltoken.vo.UserPersonalTokenVo tokenParam = 
                        new com.demo.proworks.userpersonaltoken.vo.UserPersonalTokenVo();
                    tokenParam.setUserId(userId);
                    
                    com.demo.proworks.userpersonaltoken.vo.UserPersonalTokenVo tokenInfo = 
                        userPersonalTokenService.selectUserPersonalTokenByUserId(tokenParam);
                    
                    if (tokenInfo != null) {
                        accessToken = tokenInfo.getAccessToken();
                        System.out.println("DB에서 GitHub 토큰 발견");
                    } else {
                        System.out.println("DB에서 GitHub 토큰을 찾을 수 없습니다 (userId: " + userId + ")");
                    }
                    
                } catch (Exception e) {
                    System.out.println("DB에서 GitHub 토큰 조회 실패: " + e.getMessage());
                    e.printStackTrace();
                }
            }
            
            System.out.println("최종 accessToken 존재 여부: " + (accessToken != null));
            
            if (accessToken == null) {
                result.put("success", false);
                result.put("error", "GitHub 인증이 필요합니다.");
                return result;
            }
            
            // 1. 기준 브랜치의 최신 커밋 SHA 가져오기
            System.out.println("기준 브랜치 커밋 SHA 조회: " + fromBranch);
            
            try {
                // 기준 브랜치 정보 조회
                Map<String, Object> branchInfo = gitHubApiUtil.get("/repos/" + owner + "/" + repo + "/branches/" + fromBranch, accessToken);
                System.out.println("기준 브랜치 정보: " + branchInfo);
                
                // 401 에러 검사 및 처리 - 기준 브랜치 조회
                if (branchInfo.containsKey("is_auth_error") && (Boolean) branchInfo.get("is_auth_error")) {
                    logger.warn("GitHub API 401 인증 오류 감지 (기준 브랜치 조회) - 토큰 무효화 처리 (userId: {})", userId);
                    
                    // 세션에서 GitHub 관련 정보 제거
                    session.removeAttribute("githubAccessToken");
                    session.removeAttribute("githubConnected");
                    session.removeAttribute("githubUsername");
                    session.removeAttribute("githubAvatarUrl");
                    
                    // DB에서 만료된 토큰 제거
                    try {
                        userPersonalTokenService.invalidateUserPersonalTokenByUserId(userId);
                        logger.info("만료된 GitHub 토큰 DB에서 제거 완료 (userId: {})", userId);
                    } catch (Exception e) {
                        logger.error("만료된 GitHub 토큰 DB 제거 실패 (userId: {}): {}", userId, e.getMessage());
                    }
                    
                    result.put("success", false);
                    result.put("error", "GitHub 토큰이 만료되었습니다. 다시 로그인해 주세요.");
                    result.put("error_code", "AUTH_EXPIRED");
                    result.put("auth_error_message", branchInfo.get("auth_error_message"));
                    return result;
                }
                
                if ((Boolean) branchInfo.get("success")) {
                    @SuppressWarnings("unchecked")
                    Map<String, Object> branchData = (Map<String, Object>) branchInfo.get("data");
                    @SuppressWarnings("unchecked")
                    Map<String, Object> commit = (Map<String, Object>) branchData.get("commit");
                    String sourceSha = (String) commit.get("sha");
                    
                    System.out.println("기준 브랜치 SHA: " + sourceSha);
                    
                    // 2. 새 브랜치 생성
                    Map<String, Object> createResponse = gitHubApiUtil.createBranch(accessToken, owner, repo, branchName, sourceSha);
                    System.out.println("브랜치 생성 응답: " + createResponse);
                    
                    // 401 에러 검사 및 처리 - 브랜치 생성
                    if (createResponse.containsKey("is_auth_error") && (Boolean) createResponse.get("is_auth_error")) {
                        logger.warn("GitHub API 401 인증 오류 감지 (브랜치 생성) - 토큰 무효화 처리 (userId: {})", userId);
                        
                        // 세션에서 GitHub 관련 정보 제거
                        session.removeAttribute("githubAccessToken");
                        session.removeAttribute("githubConnected");
                        session.removeAttribute("githubUsername");
                        session.removeAttribute("githubAvatarUrl");
                        
                        // DB에서 만료된 토큰 제거
                        try {
                            userPersonalTokenService.invalidateUserPersonalTokenByUserId(userId);
                            logger.info("만료된 GitHub 토큰 DB에서 제거 완료 (userId: {})", userId);
                        } catch (Exception e) {
                            logger.error("만료된 GitHub 토큰 DB 제거 실패 (userId: {}): {}", userId, e.getMessage());
                        }
                        
                        result.put("success", false);
                        result.put("error", "GitHub 토큰이 만료되었습니다. 다시 로그인해 주세요.");
                        result.put("error_code", "AUTH_EXPIRED");
                        result.put("auth_error_message", createResponse.get("auth_error_message"));
                        return result;
                    }
                    
                    if ((Boolean) createResponse.get("success")) {
                        result.put("success", true);
                        result.put("message", "브랜치 생성 성공: " + branchName);
                        result.put("data", createResponse.get("data"));
                    } else {
                        result.put("success", false);
                        result.put("error", "브랜치 생성 실패: " + createResponse.get("data"));
                    }
                } else {
                    result.put("success", false);
                    result.put("error", "기준 브랜치 정보 조회 실패: " + branchInfo.get("data"));
                }
            } catch (Exception e) {
                System.out.println("브랜치 생성 API 호출 오류: " + e.getMessage());
                result.put("success", false);
                result.put("error", "브랜치 생성 오류: " + e.getMessage());
            }
            
        } catch (Exception e) {
            System.out.println("브랜치 생성 실패: " + e.getMessage());
            e.printStackTrace();
            result.put("success", false);
            result.put("error", e.getMessage());
        }
        
        System.out.println("브랜치 생성 결과: " + result);
        return result;
    }
    
    /**
     * 브랜치 삭제
     */
    @ElService(key = "branches/delete")
    @ElDescription(sub = "브랜치 삭제", desc = "브랜치를 삭제합니다.")
    @RequestMapping(value = "branches/delete", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> deleteBranch(
            BranchParameterVo branchParameterVo,
            HttpServletRequest request) {
        
        String owner = branchParameterVo.getOwner();
        String repo = branchParameterVo.getRepo();
        String branchName = branchParameterVo.getBranchName();
        
        System.out.println("=== 브랜치 삭제 API 호출 ===");
        System.out.println("owner: " + owner);
        System.out.println("repo: " + repo);
        System.out.println("branchName: " + branchName);
        
        Map<String, Object> result = new HashMap<>();
        
        try {
            HttpSession session = request.getSession();
            String userId = null;
            
            // UserHeader에서 userId 가져오기 (getBranches와 동일한 로직)
            try {
                Object userHeader = session.getAttribute("userHeader");
                if (userHeader != null && userHeader instanceof com.demo.proworks.cmmn.ProworksUserHeader) {
                    com.demo.proworks.cmmn.ProworksUserHeader proworksUserHeader = (com.demo.proworks.cmmn.ProworksUserHeader) userHeader;
                    userId = proworksUserHeader.getUserId();
                }
            } catch (Exception e) {
                System.out.println("UserHeader에서 userId 가져오기 실패: " + e.getMessage());
            }
            
            if (userId == null) {
                userId = (String) session.getAttribute("userId");
            }
            
            System.out.println("userId: " + userId);
            
            if (userId == null) {
                result.put("success", false);
                result.put("error", "로그인이 필요합니다.");
                return result;
            }
            
            // 세션에서 userId를 가져와서 DB에서 GitHub 토큰 조회
            String accessToken = null;
            
            if (userId != null) {
                try {
                    System.out.println("DB에서 GitHub 토큰 조회 시도 (userId: " + userId + ")");
                    
                    com.demo.proworks.userpersonaltoken.vo.UserPersonalTokenVo tokenParam = 
                        new com.demo.proworks.userpersonaltoken.vo.UserPersonalTokenVo();
                    tokenParam.setUserId(userId);
                    
                    com.demo.proworks.userpersonaltoken.vo.UserPersonalTokenVo tokenInfo = 
                        userPersonalTokenService.selectUserPersonalTokenByUserId(tokenParam);
                    
                    if (tokenInfo != null) {
                        accessToken = tokenInfo.getAccessToken();
                        System.out.println("DB에서 GitHub 토큰 발견");
                    } else {
                        System.out.println("DB에서 GitHub 토큰을 찾을 수 없습니다 (userId: " + userId + ")");
                    }
                    
                } catch (Exception e) {
                    System.out.println("DB에서 GitHub 토큰 조회 실패: " + e.getMessage());
                    e.printStackTrace();
                }
            }
            
            System.out.println("최종 accessToken 존재 여부: " + (accessToken != null));
            
            if (accessToken == null) {
                result.put("success", false);
                result.put("error", "GitHub 인증이 필요합니다.");
                return result;
            }
            
            // 기본 브랜치 삭제 방지
            if ("main".equals(branchName) || "master".equals(branchName)) {
                result.put("success", false);
                result.put("error", "기본 브랜치는 삭제할 수 없습니다.");
                return result;
            }
            
            // GitHub API 호출하여 브랜치 삭제
            System.out.println("GitHub API 호출하여 브랜치 삭제 시작");
            
            try {
                Map<String, Object> deleteResponse = gitHubApiUtil.deleteBranch(accessToken, owner, repo, branchName);
                System.out.println("브랜치 삭제 응답: " + deleteResponse);
                
                // 401 에러 검사 및 처리
                if (deleteResponse.containsKey("is_auth_error") && (Boolean) deleteResponse.get("is_auth_error")) {
                    logger.warn("GitHub API 401 인증 오류 감지 (브랜치 삭제) - 토큰 무효화 처리 (userId: {})", userId);
                    
                    // 세션에서 GitHub 관련 정보 제거
                    session.removeAttribute("githubAccessToken");
                    session.removeAttribute("githubConnected");
                    session.removeAttribute("githubUsername");
                    session.removeAttribute("githubAvatarUrl");
                    
                    // DB에서 만료된 토큰 제거
                    try {
                        userPersonalTokenService.invalidateUserPersonalTokenByUserId(userId);
                        logger.info("만료된 GitHub 토큰 DB에서 제거 완료 (userId: {})", userId);
                    } catch (Exception e) {
                        logger.error("만료된 GitHub 토큰 DB 제거 실패 (userId: {}): {}", userId, e.getMessage());
                    }
                    
                    result.put("success", false);
                    result.put("error", "GitHub 토큰이 만료되었습니다. 다시 로그인해 주세요.");
                    result.put("error_code", "AUTH_EXPIRED");
                    result.put("auth_error_message", deleteResponse.get("auth_error_message"));
                    return result;
                }
                
                if ((Boolean) deleteResponse.get("success")) {
                    result.put("success", true);
                    result.put("message", "브랜치 삭제 성공: " + branchName);
                    result.put("data", deleteResponse.get("data"));
                } else {
                    result.put("success", false);
                    result.put("error", "브랜치 삭제 실패: " + deleteResponse.get("data"));
                }
            } catch (Exception e) {
                System.out.println("브랜치 삭제 API 호출 오류: " + e.getMessage());
                result.put("success", false);
                result.put("error", "브랜치 삭제 오류: " + e.getMessage());
            }
            
        } catch (Exception e) {
            System.out.println("브랜치 삭제 실패: " + e.getMessage());
            e.printStackTrace();
            result.put("success", false);
            result.put("error", e.getMessage());
        }
        
        System.out.println("브랜치 삭제 결과: " + result);
        return result;
    }
    

    // ==============================
    // GitHub 브랜치 관리
    // ==============================
    
    
    

    // ==============================
    // GitHub 웹훅 관리
    // ==============================
    
    
    /**
     * 웹훅 이벤트 수신 처리 (중복 감지 로깅 포함)
     * test의 POST /webhook 에 해당
     */
    @ElService(key = "webhook/legacy")
    @ElDescription(sub = "레거시 웹훅 이벤트 처리", desc = "GitHub 웹훅 이벤트를 처리합니다 (레거시).")
    @RequestMapping(value = "webhook/legacy")
    @ResponseBody
    public Map<String, Object> handleWebhookEvent(
            HttpServletRequest request,
            HttpServletResponse response) {
        
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 헤더에서 이벤트 정보 추출
            String eventType = request.getHeader("X-GitHub-Event");
            String deliveryId = request.getHeader("X-GitHub-Delivery");
            String signature = request.getHeader("X-Hub-Signature-256");
            String userAgent = request.getHeader("User-Agent");
            String forwardedFor = request.getHeader("X-Forwarded-For");
            String realIp = request.getHeader("X-Real-IP");
            
            // 페이로드(request body) 읽기
            StringBuilder payloadBuilder = new StringBuilder();
            java.io.BufferedReader reader = request.getReader();
            String line;
            while ((line = reader.readLine()) != null) {
                payloadBuilder.append(line);
            }
            String payload = payloadBuilder.toString();
            
            // 상세 로깅으로 중복 원인 분석
            logger.info("🔔 GitHub 웹훅 수신 - 이벤트: {} | 배송ID: {} | 페이로드 크기: {} bytes", 
                eventType, deliveryId, payload.length());
            logger.info("📋 헤더 정보 - UserAgent: {} | X-Forwarded-For: {} | X-Real-IP: {}", 
                userAgent, forwardedFor, realIp);
            logger.info("🔐 서명: {} | 요청 IP: {}", 
                signature != null ? signature.substring(0, Math.min(signature.length(), 20)) + "..." : "없음",
                request.getRemoteAddr());
            
            // Delivery ID 기반 중복 검사 (GitHub의 고유 배송 ID)
            if (deliveryId != null) {
                // 중복 검사 로직 (실제로는 캐시나 DB에 저장해서 확인해야 함)
                logger.info("🔍 배송 ID로 중복 검사: {}", deliveryId);
                
                // 간단한 메모리 기반 중복 검사 (실제 운영에서는 Redis나 DB 사용 권장)
                if (isDuplicateDelivery(deliveryId)) {
                    logger.warn("⚠️  중복 웹훅 감지! 배송 ID: {} | 이벤트: {}", deliveryId, eventType);
                    result.put("success", true);
                    result.put("message", "중복 웹훅 - 처리 스킵");
                    result.put("duplicate", true);
                    result.put("delivery_id", deliveryId);
                    return result;
                }
                
                // 배송 ID 기록
                recordDeliveryId(deliveryId);
            }
            
            // 서비스를 통해 웹훅 이벤트 처리
            Map<String, Object> param = new HashMap<>();
            param.put("event_type", eventType);
            param.put("delivery_id", deliveryId);
            param.put("signature", signature);
            param.put("payload", payload);
            
            Map<String, Object> webhookResult = gitHubService.processWebhookEvent(param);
            result.putAll(webhookResult);
            
            logger.info("✅ 웹훅 처리 완료 - 배송ID: {} | 결과: {}", deliveryId, webhookResult.get("success"));
            
        } catch (Exception e) {
            logger.error("❌ GitHub 웹훅 이벤트 처리 실패", e);
            result.put("success", false);
            result.put("error", e.getMessage());
        }
        
        return result;
    }
    
    /**
     * 중복 배송 ID 검사
     */
    private boolean isDuplicateDelivery(String deliveryId) {
        return processedDeliveryIds.contains(deliveryId);
    }
    
    /**
     * 배송 ID 기록 (중복 방지용)
     */
    private void recordDeliveryId(String deliveryId) {
        processedDeliveryIds.add(deliveryId);
        
        // 캐시 크기 제한 (메모리 누수 방지)
        if (processedDeliveryIds.size() > 1000) {
            // LinkedHashSet의 첫 번째 요소(가장 오래된 것) 제거
            java.util.Iterator<String> iterator = processedDeliveryIds.iterator();
            if (iterator.hasNext()) {
                iterator.next();
                iterator.remove();
            }
        }
        
        logger.debug("배송 ID 기록됨: {} (총 {}개)", deliveryId, processedDeliveryIds.size());
    }

    // ==============================
    // 서비스 상태 및 헬스 체크
    // ==============================
    
    /**
     * GitHub 통합 서비스 상태 확인
     * test의 /github/branches/status 에 해당
     */
    @ElService(key = "status")
    @ElDescription(sub = "서비스 상태 확인", desc = "GitHub 통합 서비스의 상태를 확인합니다.")
    @RequestMapping(value = "status")
    @ResponseBody
    public Map<String, Object> getServiceStatus() {
        logger.info("GitHub 서비스 상태 확인");
        
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 서비스를 통해 상태 확인
            Map<String, Object> serviceStatus = gitHubService.getServiceStatus();
            result.putAll(serviceStatus);
            
        } catch (Exception e) {
            logger.error("GitHub 서비스 상태 확인 실패", e);
            result.put("status", "error");
            result.put("error", e.getMessage());
        }
        
        return result;
    }
    
    /**
     * GitHub 통합 통계 조회
     */
    @ElService(key = "stats")
    @ElDescription(sub = "GitHub 통계 조회", desc = "GitHub 통합 통계를 조회합니다.")
    @RequestMapping(value = "stats")
    @ResponseBody
    public Map<String, Object> getGitHubStats(
            @RequestParam(required = false) String projectId,
            @RequestParam(required = false) String userId,
            @RequestParam(required = false) String dateFrom,
            @RequestParam(required = false) String dateTo,
            HttpServletRequest request) {
        
        logger.info("GitHub 통합 통계 조회");
        
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 서비스를 통해 GitHub 통계 조회
            Map<String, Object> param = new HashMap<>();
            param.put("project_id", projectId);
            param.put("user_id", userId);
            param.put("date_from", dateFrom);
            param.put("date_to", dateTo);
            
            Map<String, Object> statsResult = gitHubService.getProjectGitHubStats(param);
            result.putAll(statsResult);
            
        } catch (Exception e) {
            logger.error("GitHub 통합 통계 조회 실패", e);
            result.put("success", false);
            result.put("error", e.getMessage());
        }
        
        return result;
    }

    /**
     * 현재 프로젝트에 연결된 저장소 조회
     */
    @ElService(key = "repositories/current")
    @ElDescription(sub = "현재 연결된 저장소 조회", desc = "현재 프로젝트에 연결된 저장소를 조회합니다.")
    @RequestMapping(value = "repositories/current")
    @ResponseBody
    public Map<String, Object> getCurrentRepository(HttpServletRequest request) {
        System.out.println("=== 현재 연결된 저장소 조회 API 호출 ===");
        
        Map<String, Object> result = new HashMap<>();
        
        try {
            HttpSession session = request.getSession();
            String userId = null;
            
            // UserHeader에서 userId 가져오기
            try {
                Object userheader = session.getAttribute("userheader");
                if (userheader != null) {
                    System.out.println("UserHeader 타입: " + userheader.getClass().getName());
                    
                    // Reflection을 통해 getUserId() 메서드 호출
                    try {
                        java.lang.reflect.Method getUserIdMethod = userheader.getClass().getMethod("getUserId");
                        userId = (String) getUserIdMethod.invoke(userheader);
                        System.out.println("UserHeader에서 가져온 userId: " + userId);
                    } catch (Exception e) {
                        System.out.println("Reflection으로 userId 가져오기 실패: " + e.getMessage());
                    }
                }
            } catch (Exception e) {
                System.out.println("UserHeader에서 userId 가져오기 실패: " + e.getMessage());
            }
            
            // 세션에서 직접 userId 가져오기 시도
            if (userId == null) {
                userId = (String) session.getAttribute("userId");
                System.out.println("세션에서 직접 가져온 userId: " + userId);
            }
            
            System.out.println("최종 사용할 userId: " + userId);
            
            if (userId == null) {
                System.out.println("userId가 null입니다. 로그인 필요");
                result.put("success", false);
                result.put("error", "로그인이 필요합니다.");
                return result;
            }
            
            // 현재 연결된 저장소 조회
            Map<String, Object> param = new HashMap<>();
            param.put("user_id", userId);
            
            Map<String, Object> currentRepo = gitHubService.getCurrentRepository(param);
            result.putAll(currentRepo);
            
        } catch (Exception e) {
            System.out.println("현재 저장소 조회 실패: " + e.getMessage());
            e.printStackTrace();
            result.put("success", false);
            result.put("error", e.getMessage());
        }
        
        return result;
    }

}