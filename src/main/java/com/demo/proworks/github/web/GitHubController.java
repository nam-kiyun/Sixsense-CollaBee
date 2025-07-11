package com.demo.proworks.github.web;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.demo.proworks.github.service.GitHubService;
import com.demo.proworks.github.vo.GitHubRepositoryListVo;
import com.demo.proworks.projectrepo.vo.ProjectRepositoryVo;
import com.inswave.elfw.annotation.ElDescription;
import com.inswave.elfw.annotation.ElService;
import com.inswave.elfw.annotation.ElValidator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

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
        logger.info("GitHub OAuth 인증 시작 (projectId: {}, userId: {})", projectId, userId);
        
        try {
            HttpSession session = request.getSession();
            
            // state에 userId와 projectId 정보를 포함 (JSON 형태)
            Map<String, String> stateData = new HashMap<>();
            stateData.put("uuid", java.util.UUID.randomUUID().toString());
            if (userId != null && !userId.trim().isEmpty()) {
                stateData.put("userId", userId);
                logger.info("state에 사용자 ID 포함: {}", userId);
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
            
            // 백업용으로 세션에도 저장
            if (projectId != null && !projectId.trim().isEmpty()) {
                session.setAttribute("githubOAuthProjectId", projectId);
            }
            if (userId != null && !userId.trim().isEmpty()) {
                session.setAttribute("githubOAuthUserId", userId);
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
            ModelAndView mv = new ModelAndView("/error");
            mv.addObject("error", "GitHub 인증을 시작할 수 없습니다: " + e.getMessage());
            return mv;
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
                ModelAndView mv = new ModelAndView();
                mv.setViewName("redirect:/InsWebApp/websquare/websquare.html?w2xPath=/ui/github_callback.xml&status=error&message=" + 
                    java.net.URLEncoder.encode("GitHub 인증이 거부되었습니다.", "UTF-8"));
                return mv;
            }
            
            if (code == null) {
                logger.error("GitHub OAuth 코드가 없습니다");
                ModelAndView mv = new ModelAndView();
                mv.setViewName("redirect:/InsWebApp/websquare/websquare.html?w2xPath=/ui/github_callback.xml&status=error&message=" + 
                    java.net.URLEncoder.encode("인증 코드가 제공되지 않았습니다.", "UTF-8"));
                return mv;
            }
            
            HttpSession session = request.getSession();
            String sessionState = (String) session.getAttribute("githubOAuthState");
            
            if (!state.equals(sessionState)) {
                logger.error("GitHub OAuth state 불일치");
                ModelAndView mv = new ModelAndView();
                mv.setViewName("redirect:/InsWebApp/websquare/websquare.html?w2xPath=/ui/github_callback.xml&status=error&message=" + 
                    java.net.URLEncoder.encode("인증 상태가 유효하지 않습니다.", "UTF-8"));
                return mv;
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
            
            // OAuth 시작 시 저장된 사용자 ID 또는 현재 로그인된 사용자 ID 가져오기
            String oauthUserId = (String) session.getAttribute("githubOAuthUserId");
            String currentUserId = (String) session.getAttribute("userId");
            
            logger.info("디버깅 - oauthUserId: {}", oauthUserId);
            logger.info("디버깅 - currentUserId (session): {}", currentUserId);
            
            // 세션에서 다양한 키로 사용자 ID 찾기
            if (currentUserId == null) {
                // 다양한 세션 키 시도
                String[] userIdKeys = {"userId", "user_id", "userEmail", "user_email", "loginUserId", "login_user_id"};
                for (String key : userIdKeys) {
                    currentUserId = (String) session.getAttribute(key);
                    if (currentUserId != null && !currentUserId.trim().isEmpty()) {
                        logger.info("세션에서 사용자 ID 찾음 (키: {}): {}", key, currentUserId);
                        break;
                    }
                }
            }
            
            // ElHeader에서 사용자 정보 가져오기 시도
            if (currentUserId == null) {
                Object elHeader = request.getAttribute("ElHeader");
                logger.info("디버깅 - ElHeader 객체: {}", elHeader != null ? elHeader.getClass().getName() : "null");
                if (elHeader != null) {
                    try {
                        java.lang.reflect.Method getUserIdMethod = elHeader.getClass().getMethod("getUserId");
                        currentUserId = (String) getUserIdMethod.invoke(elHeader);
                        logger.info("ElHeader에서 사용자 ID 가져옴: {}", currentUserId);
                    } catch (Exception e) {
                        logger.error("ElHeader에서 사용자 ID 가져오기 실패", e);
                    }
                }
            }
            
            // request parameter에서 확인 (프론트엔드에서 전달)
            if (currentUserId == null) {
                currentUserId = request.getParameter("userId");
                if (currentUserId != null && !currentUserId.trim().isEmpty()) {
                    logger.info("request parameter에서 사용자 ID 찾음: {}", currentUserId);
                }
            }
            
            // 최종 사용자 ID 결정 (우선순위: state > OAuth 시작 시 전달 > 세션 > 기타)
            String finalUserId = null;
            if (stateUserId != null && !stateUserId.trim().isEmpty() && !"user01".equals(stateUserId)) {
                finalUserId = stateUserId;
                logger.info("state에서 추출한 사용자 ID 사용: {}", finalUserId);
            } else if (oauthUserId != null && !oauthUserId.trim().isEmpty() && !"user01".equals(oauthUserId)) {
                finalUserId = oauthUserId;
                logger.info("OAuth에서 전달받은 사용자 ID 사용: {}", finalUserId);
            } else if (currentUserId != null && !currentUserId.trim().isEmpty() && !"user01".equals(currentUserId)) {
                finalUserId = currentUserId;
                logger.info("세션/기타에서 현재 사용자 ID 사용: {}", finalUserId);
            } else {
                logger.error("사용자 ID를 찾을 수 없습니다. OAuth 연동을 진행할 수 없습니다.");
                ModelAndView mv = new ModelAndView();
                mv.setViewName("redirect:/InsWebApp/websquare/websquare.html?w2xPath=/ui/github_callback.xml&status=error&message=" + 
                    java.net.URLEncoder.encode("로그인된 사용자 정보를 찾을 수 없습니다. 다시 로그인해주세요.", "UTF-8"));
                return mv;
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
            
            logger.info("GitHub OAuth 콜백 처리 성공");
            
            // WebSquare 방식으로 콜백 처리 - websquare.html을 통해 XML 파일 로드
            String username = (String) authResult.get("username");
            String hasSelectedRepo = "false"; // 현재는 기본값
            
            String redirectUrl = String.format(
                "/websquare/websquare.html?w2xPath=/InsWebApp/ui/github_callback.xml&status=success&username=%s&hasSelectedRepo=%s", 
                java.net.URLEncoder.encode(username != null ? username : "", "UTF-8"),
                hasSelectedRepo
            );
            
            logger.info("WebSquare 콜백 URL로 리다이렉트: {}", redirectUrl);
            ModelAndView mv = new ModelAndView();
            mv.setViewName("redirect:" + redirectUrl);
            return mv;
            
        } catch (Exception e) {
            logger.error("GitHub OAuth 콜백 처리 실패", e);
            ModelAndView mv = new ModelAndView();
//            mv.setViewName("redirect:/InsWebApp/websquare/websquare.html?w2xPath=/ui/github_callback.xml&status=error&message=" + 
//                java.net.URLEncoder.encode("GitHub OAuth 처리 중 오류가 발생했습니다.", "UTF-8"));
            return mv;
        }
    }
    
    /**
     * GitHub App 설치 후 설정 완료 처리
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
            
            if (installation_id != null) {
                // 서비스를 통해 GitHub App 설치 완료 처리
                Map<String, Object> param = new HashMap<>();
                param.put("installation_id", installation_id);
                param.put("setup_action", setup_action);
                
                Map<String, Object> setupResult = gitHubService.processAppInstallation(param);
                
                session.setAttribute("githubAppInstalled", true);
                session.setAttribute("githubInstallationId", installation_id);
                
                logger.info("GitHub App 설치 처리 성공");
                
                // 웹스퀘어 구조에 맞게 프로젝트 메인 페이지로 리다이렉트
                return new ModelAndView("redirect:/websquare/websquare.html?w2xPath=/InsWebApp/ui/project/projectMainPage.xml&setup=completed");
            } else {
                ModelAndView mv = new ModelAndView("/error");
                mv.addObject("error", "GitHub App 설치 정보가 제공되지 않았습니다.");
                return mv;
            }
            
        } catch (Exception e) {
            logger.error("GitHub App 설정 완료 처리 실패", e);
            ModelAndView mv = new ModelAndView("/error");
            mv.addObject("error", "GitHub App 설정 처리 중 오류가 발생했습니다.");
            return mv;
        }
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
            String userId = (String) session.getAttribute("userId");
            
            // 서비스를 통해 GitHub 인증 상태 확인
            Boolean githubConnected = (Boolean) session.getAttribute("githubConnected");
            
            result.put("authenticated", githubConnected != null ? githubConnected : false);
            result.put("success", true);
            
            // 인증된 경우 사용자 정보 추가
            if (githubConnected != null && githubConnected) {
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
            @RequestParam(value = "owner", required = true) String owner,
            @RequestParam(value = "repo", required = true) String repo,
            HttpServletRequest request) {
        
        System.out.println("=== 브랜치 목록 조회 API 호출 ===");
        System.out.println("owner: " + owner);
        System.out.println("repo: " + repo);
        
        Map<String, Object> result = new HashMap<>();
        
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
            
            // GitHub API 프록시 호출
            String accessToken = (String) session.getAttribute("github_access_token");
            System.out.println("accessToken 존재 여부: " + (accessToken != null));
            
            if (accessToken == null) {
                result.put("success", false);
                result.put("error", "GitHub 인증이 필요합니다.");
                return result;
            }
            
            // GitHub API 호출
            System.out.println("GitHub API 호출 시작");
            
            try {
                Map<String, Object> apiResponse = gitHubApiUtil.getBranches(accessToken, owner, repo);
                System.out.println("GitHub API 응답: " + apiResponse);
                
                if ((Boolean) apiResponse.get("success")) {
                    result.put("success", true);
                    result.put("message", "브랜치 목록 조회 성공");
                    result.put("data", apiResponse.get("data"));
                } else {
                    result.put("success", false);
                    result.put("error", "GitHub API 호출 실패: " + apiResponse.get("data"));
                }
            } catch (Exception e) {
                System.out.println("GitHub API 호출 오류: " + e.getMessage());
                result.put("success", false);
                result.put("error", "GitHub API 호출 오류: " + e.getMessage());
            }
            
        } catch (Exception e) {
            System.out.println("브랜치 목록 조회 실패: " + e.getMessage());
            e.printStackTrace();
            result.put("success", false);
            result.put("error", e.getMessage());
        }
        
        System.out.println("브랜치 목록 조회 결과: " + result);
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
            @RequestParam(value = "owner", required = true) String owner,
            @RequestParam(value = "repo", required = true) String repo,
            @RequestParam(value = "branchName", required = true) String branchName,
            @RequestParam(value = "fromBranch", required = true) String fromBranch,
            HttpServletRequest request) {
        
        System.out.println("=== 브랜치 생성 API 호출 ===");
        System.out.println("owner: " + owner);
        System.out.println("repo: " + repo);
        System.out.println("branchName: " + branchName);
        System.out.println("fromBranch: " + fromBranch);
        
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 인증 확인
            HttpSession session = request.getSession();
            String accessToken = (String) session.getAttribute("github_access_token");
            
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
            @RequestParam(value = "owner", required = true) String owner,
            @RequestParam(value = "repo", required = true) String repo,
            @RequestParam(value = "branchName", required = true) String branchName,
            HttpServletRequest request) {
        
        System.out.println("=== 브랜치 삭제 API 호출 ===");
        System.out.println("owner: " + owner);
        System.out.println("repo: " + repo);
        System.out.println("branchName: " + branchName);
        
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 인증 확인
            HttpSession session = request.getSession();
            String accessToken = (String) session.getAttribute("github_access_token");
            
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
    
    /**
     * 현재 선택된 레포지토리 정보 조회
     */
    @ElService(key = "repositories/current")
    @ElDescription(sub = "현재 레포지토리 조회", desc = "현재 선택된 레포지토리 정보를 조회합니다.")
    @RequestMapping(value = "repositories/current")
    @ResponseBody
    public Map<String, Object> getCurrentRepository(HttpServletRequest request) {
        logger.info("현재 선택된 레포지토리 조회");
        
        Map<String, Object> result = new HashMap<>();
        
        try {
            HttpSession session = request.getSession();
            String userId = (String) session.getAttribute("userId");
            
            if (userId == null) {
                result.put("success", false);
                result.put("error", "로그인이 필요합니다.");
                return result;
            }
            
            // 서비스를 통해 현재 선택된 레포지토리 조회
            ProjectRepositoryVo currentRepo = gitHubService.getCurrentRepository(userId);
            
            result.put("success", true);
            result.put("repository", currentRepo);
            result.put("message", currentRepo != null ? "현재 선택된 레포지토리가 있습니다." : "선택된 레포지토리가 없습니다.");
            
        } catch (Exception e) {
            logger.error("현재 선택된 레포지토리 조회 실패", e);
            result.put("success", false);
            result.put("error", e.getMessage());
        }
        
        return result;
    }

    // ==============================
    // GitHub 브랜치 관리
    // ==============================
    
    
    

    // ==============================
    // GitHub 웹훅 관리
    // ==============================
    
    
    /**
     * 웹훅 이벤트 수신 처리
     * test의 POST /webhook 에 해당
     */
    @ElService(key = "webhook/legacy")
    @ElDescription(sub = "레거시 웹훅 이벤트 처리", desc = "GitHub 웹훅 이벤트를 처리합니다 (레거시).")
    @RequestMapping(value = "webhook/legacy")
    @ResponseBody
    public Map<String, Object> handleWebhookEvent(
            HttpServletRequest request,
            HttpServletResponse response) {
        
        logger.info("GitHub 웹훅 이벤트 수신");
        
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 헤더에서 이벤트 정보 추출
            String eventType = request.getHeader("X-GitHub-Event");
            String deliveryId = request.getHeader("X-GitHub-Delivery");
            String signature = request.getHeader("X-Hub-Signature-256");
            
            logger.info("웹훅 이벤트: {} ({})", eventType, deliveryId);
            
            // 서비스를 통해 웹훅 이벤트 처리
            Map<String, Object> param = new HashMap<>();
            param.put("event_type", eventType);
            param.put("delivery_id", deliveryId);
            param.put("signature", signature);
            
            Map<String, Object> webhookResult = gitHubService.processWebhookEvent(param);
            result.putAll(webhookResult);
            
        } catch (Exception e) {
            logger.error("GitHub 웹훅 이벤트 처리 실패", e);
            result.put("success", false);
            result.put("error", e.getMessage());
        }
        
        return result;
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

}