package com.demo.proworks.github.service.impl;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.demo.proworks.github.dao.GitHubDAO;
import com.demo.proworks.github.service.GitHubService;
import com.demo.proworks.github.util.GitHubApiUtil;
import com.demo.proworks.github.util.GitHubSyncUtil;
import com.demo.proworks.github.util.GitHubWebhookUtil;
import com.demo.proworks.projectrepo.vo.ProjectRepositoryVo;
import com.demo.proworks.githubapptoken.vo.GithubAppTokenVo;
import com.demo.proworks.repobranch.vo.RepositoryBranchVo;
import com.demo.proworks.githubwebhook.vo.GithubWebhookVo;
import com.demo.proworks.githubapptoken.vo.GithubAppTokenVo;
import com.demo.proworks.github.vo.GitHubRepositoryVo;
import com.demo.proworks.github.vo.GitHubRepositoryListVo;
import com.demo.proworks.userpersonaltoken.service.UserPersonalTokenService;
import com.demo.proworks.userpersonaltoken.vo.UserPersonalTokenVo;

/**
 * GitHub 통합 서비스 구현체
 * test 디렉터리의 Node.js 로직을 Java로 포팅
 */
@Service("gitHubService")
public class GitHubServiceImpl implements GitHubService {
    
    private static final Logger logger = LoggerFactory.getLogger(GitHubServiceImpl.class);
    
    @Resource(name = "gitHubDAO")
    private GitHubDAO gitHubDAO;
    
    @Resource
    private GitHubApiUtil gitHubApiUtil;
    
    @Resource
    private GitHubSyncUtil gitHubSyncUtil;
    
    @Resource
    private GitHubWebhookUtil gitHubWebhookUtil;
    
    @Resource(name = "userPersonalTokenServiceImpl")
    private UserPersonalTokenService userPersonalTokenService;
    
    @Resource(name = "repositoryBranchServiceImpl")
    private com.demo.proworks.repobranch.service.RepositoryBranchService repositoryBranchService;

    // ==============================
    // GitHub OAuth 인증 관리
    // ==============================
    
    @Override
    public Map<String, Object> generateOAuthUrl(Map<String, Object> param) throws Exception {
        logger.info("GitHub OAuth URL 생성 시작");
        
        Map<String, Object> result = new HashMap<>();
        
        try {
            // GitHub OAuth URL 구성 (test 디렉터리의 auth.js 참고)
            String clientId = System.getProperty("github.client.id");
            String redirectUri = (String) param.get("redirect_uri");
            String scope = "repo read:user admin:repo_hook"; // 웹훅 권한 포함
            String state = (String) param.get("state");
            
            String authUrl = String.format(
                "https://github.com/login/oauth/authorize?client_id=%s&redirect_uri=%s&scope=%s&state=%s",
                clientId, redirectUri, scope, state
            );
            
            result.put("auth_url", authUrl);
            result.put("success", true);
            
            logger.info("GitHub OAuth URL 생성 완료: {}", authUrl);
            
        } catch (Exception e) {
            logger.error("GitHub OAuth URL 생성 실패", e);
            result.put("success", false);
            result.put("error", e.getMessage());
        }
        
        return result;
    }
    
    @Override
    @Transactional
    public Map<String, Object> processOAuthCallback(Map<String, Object> param) throws Exception {
        logger.info("GitHub OAuth 콜백 처리 시작");
        
        Map<String, Object> result = new HashMap<>();
        
        try {
            String code = (String) param.get("code");
            String state = (String) param.get("state");
            String userId = (String) param.get("user_id"); // 세션에서 받은 사용자 ID
            
            // 디버깅 로그 추가
            logger.info("서비스 processOAuthCallback - 받은 파라미터: {}", param);
            logger.info("서비스 processOAuthCallback - userId: '{}' (length: {})", userId, userId != null ? userId.length() : "null");
            logger.info("서비스 processOAuthCallback - userId is null? {}", userId == null);
            logger.info("서비스 processOAuthCallback - userId is empty? {}", userId != null && userId.trim().isEmpty());
            
            // GitHub API 호출하여 액세스 토큰 획득 (elfw.properties에서 설정값 읽기)
            String clientId = System.getProperty("GITHUB_CLIENT_ID", "Iv23liShQFpINkvH7lCV");
            String clientSecret = System.getProperty("GITHUB_CLIENT_SECRET", "ac534ffba44e6eae3ee74832e406724bfea12620");
            
            logger.info("GitHub OAuth 설정 - ClientID: {}, ClientSecret: {}****", clientId, clientSecret.substring(0, 8));
            
            Map<String, Object> tokenResponse = gitHubApiUtil.exchangeCodeForToken(code, clientId, clientSecret);
            logger.info("GitHub 토큰 교환 결과: {}", tokenResponse.get("success"));
            
            if ((Boolean) tokenResponse.get("success")) {
                @SuppressWarnings("unchecked")
                Map<String, Object> tokenData = (Map<String, Object>) tokenResponse.get("data");
                String accessToken = (String) tokenData.get("access_token");
                String scope = (String) tokenData.get("scope");
                
                // GitHub 사용자 정보 조회
                Map<String, Object> userInfo = gitHubApiUtil.getUserInfo(accessToken);
                
                // GitHub 사용자를 로컬 사용자와 연동
                String projectId = (String) param.get("project_id");
                UserPersonalTokenVo userToken = null;
                if (userId != null && !userId.trim().isEmpty()) {
                    logger.info("userId가 있으므로 linkGitHubUserWithUserId 호출: userId='{}'", userId);
                    userToken = linkGitHubUserWithUserId(accessToken, userId, projectId);
                } else {
                    logger.warn("userId가 null이거나 빈 문자열이므로 기본 linkGitHubUser 호출: userId='{}'", userId);
                    userToken = linkGitHubUser(accessToken, projectId); // 기본 방식
                }
                
                result.put("success", true);
                result.put("message", "OAuth 콜백 처리 완료");
                result.put("user", userToken);
                result.put("access_token", accessToken);
                result.put("username", userInfo.get("login"));
                result.put("avatar_url", userInfo.get("avatar_url"));
            } else {
                result.put("success", false);
                result.put("error", "액세스 토큰 획득 실패");
            }
            
        } catch (Exception e) {
            logger.error("GitHub OAuth 콜백 처리 실패", e);
            result.put("success", false);
            result.put("error", e.getMessage());
        }
        
        return result;
    }
    
    @Override
    @Transactional
    public UserPersonalTokenVo linkGitHubUser(String accessToken, String projectId) throws Exception {
        logger.error("linkGitHubUser 호출됨 - userId 파라미터가 없어서 GitHub 연동을 진행할 수 없습니다.");
        throw new RuntimeException("사용자 ID가 제공되지 않아 GitHub 연동을 진행할 수 없습니다. 다시 로그인해주세요.");
    }

    @Override
    @Transactional
    public UserPersonalTokenVo linkGitHubUserWithUserId(String accessToken, String userId, String projectId) throws Exception {
        logger.info("GitHub 사용자 연동 시작 (userId: {}, projectId: {})", userId, projectId);
        
        UserPersonalTokenVo userToken = null;
        
        try {
            // GitHub API로 사용자 정보 조회
            Map<String, Object> userInfo = gitHubApiUtil.getUserInfo(accessToken);
            String githubUserId = userInfo.get("login").toString();
            logger.info("GitHub 사용자 정보 조회 완료: {}", githubUserId);
            
            // USER_PERSONAL_TOKENS 테이블에 저장할 데이터 준비
            UserPersonalTokenVo tokenVo = new UserPersonalTokenVo();
            // userPersonalTokenId는 AUTO_INCREMENT이므로 설정하지 않음
            tokenVo.setUserId(userId);
            tokenVo.setAccessToken(accessToken);
            tokenVo.setScope("repo read:user admin:repo_hook");
            // createdAt은 NOW()로 처리하므로 설정하지 않음
            
            // 기존 토큰 정보 확인
            UserPersonalTokenVo queryVo = new UserPersonalTokenVo();
            queryVo.setUserId(userId);
            UserPersonalTokenVo existingToken = userPersonalTokenService.selectUserPersonalTokenByUserId(queryVo);
            
            // DB에 GitHub 사용자 정보 저장/업데이트
            if (existingToken == null) {
                // 새 사용자 - INSERT
                userPersonalTokenService.insertUserPersonalToken(tokenVo);
                logger.info("새 OAuth 토큰 저장 완료: userId={}, projectId={}", userId, projectId);
            } else {
                // 기존 사용자 - UPDATE
                existingToken.setAccessToken(accessToken);
                existingToken.setScope("repo read:user admin:repo_hook");
                userPersonalTokenService.updateUserPersonalToken(existingToken);
                tokenVo = existingToken;
                logger.info("기존 OAuth 토큰 업데이트 완료: userId={}, projectId={}", userId, projectId);
            }
            
            userToken = tokenVo;
            
        } catch (Exception e) {
            logger.error("GitHub 사용자 연동 실패 (userId: {}, projectId: {})", userId, projectId, e);
            throw e;
        }
        
        return userToken;
    }
    
    @Override
    public Map<String, Object> checkAppInstallation(Map<String, Object> param) throws Exception {
        logger.info("GitHub App 설치 상태 확인 시작");
        
        Map<String, Object> result = new HashMap<>();
        
        try {
            // TODO: GitHub API로 App 설치 상태 확인
            // test 디렉터리의 auth.js의 checkAppInstallation 함수 참고
            
            result.put("installed", true);
            result.put("success", true);
            
        } catch (Exception e) {
            logger.error("GitHub App 설치 상태 확인 실패", e);
            result.put("success", false);
            result.put("error", e.getMessage());
        }
        
        return result;
    }
    
    @Override
    public Map<String, Object> processAppInstallation(Map<String, Object> param) throws Exception {
        logger.info("GitHub App 설치 완료 처리 시작");
        
        Map<String, Object> result = new HashMap<>();
        
        try {
            String installationId = (String) param.get("installation_id");
            String setupAction = (String) param.get("setup_action");
            
            logger.info("GitHub App 설치 완료: installation_id={}, setup_action={}", installationId, setupAction);
            
            // TODO: installation_id를 데이터베이스에 저장
            // TODO: GitHub API로 설치된 앱 정보 조회
            // TODO: 설치된 레포지토리 목록 조회 및 저장
            
            if (installationId != null && !installationId.isEmpty()) {
                // 설치 완료 로직
                result.put("success", true);
                result.put("installation_id", installationId);
                result.put("setup_action", setupAction);
                result.put("message", "GitHub App 설치가 완료되었습니다.");
                
                // 설치 로그 생성
                Map<String, Object> installLog = new HashMap<>();
                installLog.put("installation_id", installationId);
                installLog.put("setup_action", setupAction);
                installLog.put("installed_at", new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date()));
                
                // TODO: 설치 로그를 데이터베이스에 저장
                
            } else {
                result.put("success", false);
                result.put("error", "설치 ID가 제공되지 않았습니다.");
            }
            
        } catch (Exception e) {
            logger.error("GitHub App 설치 완료 처리 실패", e);
            result.put("success", false);
            result.put("error", e.getMessage());
        }
        
        return result;
    }

    // ==============================
    // GitHub 레포지토리 관리
    // ==============================
    
    @Override
    public GitHubRepositoryListVo getRepositories(Map<String, Object> param) throws Exception {
        logger.info("GitHub 레포지토리 목록 조회 시작");
        
        GitHubRepositoryListVo result = new GitHubRepositoryListVo();
        
        try {
            // GitHub API로 레포지토리 목록 조회
            String accessToken = (String) param.get("access_token");
            String type = (String) param.get("type");
            String sort = (String) param.get("sort");
            Integer perPage = (Integer) param.get("per_page");
            
            Map<String, Object> apiResponse = gitHubApiUtil.getRepositories(accessToken, type, sort, perPage);
            
            List<GitHubRepositoryVo> repositories = new ArrayList<>();
            
            if ((Boolean) apiResponse.get("success")) {
                @SuppressWarnings("unchecked")
                List<Map<String, Object>> repoList = (List<Map<String, Object>>) apiResponse.get("data");
                
                for (Map<String, Object> repoData : repoList) {
                    GitHubRepositoryVo repo = new GitHubRepositoryVo();
                    repo.setGithubRepoId(repoData.get("id").toString());
                    repo.setRepoName((String) repoData.get("name"));
                    repo.setRepoFullName((String) repoData.get("full_name"));
                    repo.setRepoOwner(((Map<String, Object>) repoData.get("owner")).get("login").toString());
                    repo.setRepoDescription((String) repoData.get("description"));
                    repo.setRepoUrl((String) repoData.get("html_url"));
                    repo.setCloneUrl((String) repoData.get("clone_url"));
                    repo.setSshUrl((String) repoData.get("ssh_url"));
                    repo.setDefaultBranch((String) repoData.get("default_branch"));
                    repo.setIsPrivate(repoData.get("private").toString());
                    repo.setIsFork(repoData.get("fork").toString());
                    repo.setLanguage((String) repoData.get("language"));
                    repo.setStarsCount(repoData.get("stargazers_count") != null ? 
                        Integer.parseInt(repoData.get("stargazers_count").toString()) : 0);
                    repo.setForksCount(repoData.get("forks_count") != null ? 
                        Integer.parseInt(repoData.get("forks_count").toString()) : 0);
                    repo.setCreatedAt((String) repoData.get("created_at"));
                    repo.setUpdatedAt((String) repoData.get("updated_at"));
                    repo.setPushedAt((String) repoData.get("pushed_at"));
                    
                    repositories.add(repo);
                }
            }
            
            result.setGitHubRepositoryVoList(repositories);
            
            logger.info("GitHub 레포지토리 목록 조회 완료: {} 개", repositories.size());
            
        } catch (Exception e) {
            logger.error("GitHub 레포지토리 목록 조회 실패", e);
            throw e;
        }
        
        return result;
    }
    
    @Override
    public GitHubRepositoryVo getRepository(Map<String, Object> param) throws Exception {
        logger.info("GitHub 레포지토리 상세 조회 시작");
        
        GitHubRepositoryVo repository = new GitHubRepositoryVo();
        
        try {
            // GitHub API로 특정 레포지토리 상세 정보 조회
            String accessToken = (String) param.get("access_token");
            String owner = (String) param.get("owner");
            String repo = (String) param.get("repo");
            
            String endpoint = "/repos/" + owner + "/" + repo;
            Map<String, Object> apiResponse = gitHubApiUtil.get(endpoint, accessToken);
            
            if ((Boolean) apiResponse.get("success")) {
                @SuppressWarnings("unchecked")
                Map<String, Object> repoData = (Map<String, Object>) apiResponse.get("data");
                
                repository.setGithubRepoId(repoData.get("id").toString());
                repository.setRepoName((String) repoData.get("name"));
                repository.setRepoFullName((String) repoData.get("full_name"));
                repository.setRepoOwner(((Map<String, Object>) repoData.get("owner")).get("login").toString());
                repository.setRepoDescription((String) repoData.get("description"));
                repository.setRepoUrl((String) repoData.get("html_url"));
                repository.setCloneUrl((String) repoData.get("clone_url"));
                repository.setSshUrl((String) repoData.get("ssh_url"));
                repository.setDefaultBranch((String) repoData.get("default_branch"));
                repository.setIsPrivate(repoData.get("private").toString());
                repository.setIsFork(repoData.get("fork").toString());
                repository.setLanguage((String) repoData.get("language"));
                repository.setStarsCount(repoData.get("stargazers_count") != null ? 
                    Integer.parseInt(repoData.get("stargazers_count").toString()) : 0);
                repository.setForksCount(repoData.get("forks_count") != null ? 
                    Integer.parseInt(repoData.get("forks_count").toString()) : 0);
                repository.setCreatedAt((String) repoData.get("created_at"));
                repository.setUpdatedAt((String) repoData.get("updated_at"));
                repository.setPushedAt((String) repoData.get("pushed_at"));
            }
            
            logger.info("GitHub 레포지토리 상세 조회 완료");
            
        } catch (Exception e) {
            logger.error("GitHub 레포지토리 상세 조회 실패", e);
            throw e;
        }
        
        return repository;
    }
    
    @Override
    @Transactional
    public Map<String, Object> selectRepository(Map<String, Object> param) throws Exception {
        System.out.println("=== 레포지토리 선택 처리 시작 ===");
        System.out.println("서비스 - 받은 파라미터: " + param);
        
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 파라미터 상세 로그
            String projectId = (String) param.get("project_id");
            System.out.println("project_id: " + projectId);
            System.out.println("github_repository_id: " + param.get("github_repository_id"));
            System.out.println("repo_owner: " + param.get("repo_owner"));
            System.out.println("repo_name: " + param.get("repo_name"));
            System.out.println("default_branch: " + param.get("default_branch"));
            System.out.println("github_app_installation_id: " + param.get("github_app_installation_id"));
            
            // 기존 연결된 저장소 확인 (프로젝트 ID 기반)
            System.out.println("기존 연결된 저장소 확인 (프로젝트 ID: " + projectId + ")");
            ProjectRepositoryVo existingRepo = gitHubDAO.selectProjectRepositoryByProjectId(projectId);
            
            if (existingRepo != null) {
                System.out.println("기존 연결된 저장소 발견: " + existingRepo.getRepoOwner() + "/" + existingRepo.getRepoName());
                System.out.println("기존 저장소 정보 업데이트");
                
                // 기존 저장소 정보 업데이트
                Map<String, Object> updateParam = new HashMap<>();
                updateParam.put("project_repo_id", existingRepo.getProjectRepoId());
                updateParam.put("repo_owner", param.get("repo_owner"));
                updateParam.put("repo_name", param.get("repo_name"));
                updateParam.put("default_branch", param.get("default_branch"));
                updateParam.put("github_app_installation_id", param.get("github_app_installation_id"));
                
                gitHubDAO.updateProjectRepository(updateParam);
                
                result.put("success", true);
                result.put("record_id", existingRepo.getProjectRepoId());
                result.put("message", "레포지토리 연결 정보 업데이트 완료");
                result.put("action", "updated");
                
            } else {
                System.out.println("새로운 저장소 연결");
                // 새로운 저장소 연결
                String recordId = gitHubDAO.insertProjectRepository(param);
                System.out.println("DB 저장 완료, recordId: " + recordId);
                
                result.put("success", true);
                result.put("record_id", recordId);
                result.put("message", "레포지토리 선택 완료");
                result.put("action", "created");
            }
            
            System.out.println("레포지토리 선택 처리 완료");
            
        } catch (Exception e) {
            System.out.println("레포지토리 선택 처리 실패: " + e.getMessage());
            e.printStackTrace();
            result.put("success", false);
            result.put("error", e.getMessage());
        }
        
        return result;
    }
    
    @Override
    public ProjectRepositoryVo getCurrentRepositoryByProjectId(String projectId) throws Exception {
        System.out.println("=== 프로젝트 ID로 연결된 레포지토리 조회 시작 ===");
        System.out.println("프로젝트 ID: " + projectId);
        
        ProjectRepositoryVo repository = null;
        
        try {
            repository = gitHubDAO.selectProjectRepositoryByProjectId(projectId);
            
            if (repository != null) {
                System.out.println("연결된 저장소 발견: " + repository.getRepoOwner() + "/" + repository.getRepoName());
            } else {
                System.out.println("연결된 저장소 없음");
            }
            
        } catch (Exception e) {
            System.out.println("프로젝트 ID로 레포지토리 조회 실패: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
        
        return repository;
    }
    
    // 현재 사용자 ID 가져오기 헬퍼 메소드
    private String getCurrentUserId() {
        try {
            // 현재 스레드에서 사용자 ID 가져오기
            // 실제 구현에서는 SecurityContext나 ThreadLocal에서 가져와야 함
            return "fordnam1@gmail.com"; // 임시로 하드코딩
        } catch (Exception e) {
            System.out.println("현재 사용자 ID 가져오기 실패: " + e.getMessage());
            return null;
        }
    }
    
    @Override
    public ProjectRepositoryVo getCurrentRepository(String userId) throws Exception {
        logger.info("현재 선택된 레포지토리 조회 시작: {}", userId);
        
        ProjectRepositoryVo repository = null;
        
        try {
            repository = gitHubDAO.selectProjectRepositoryByUserId(userId);
            logger.info("현재 선택된 레포지토리 조회 완료");
            
        } catch (Exception e) {
            logger.error("현재 선택된 레포지토리 조회 실패", e);
            throw e;
        }
        
        return repository;
    }
    
    @Override
    public Map<String, Object> getCurrentRepository(Map<String, Object> param) throws Exception {
        System.out.println("=== 현재 선택된 레포지토리 조회 (Map 형태) ===");
        
        Map<String, Object> result = new HashMap<>();
        
        try {
            String userId = (String) param.get("user_id");
            System.out.println("조회 대상 userId: " + userId);
            
            if (userId == null || userId.trim().isEmpty()) {
                result.put("success", false);
                result.put("error", "사용자 ID가 필요합니다.");
                return result;
            }
            
            // 기존 메서드 호출
            ProjectRepositoryVo repository = getCurrentRepository(userId);
            
            if (repository != null) {
                System.out.println("연결된 저장소 발견: " + repository.getRepoOwner() + "/" + repository.getRepoName());
                
                // VO를 Map으로 변환 (ProjectRepositoryVo에 실제 존재하는 필드만 사용)
                Map<String, Object> repositoryMap = new HashMap<>();
                repositoryMap.put("projectRepoId", repository.getProjectRepoId());
                repositoryMap.put("projectId", repository.getProjectId());
                repositoryMap.put("githubRepositoryId", repository.getGithubRepositoryId());
                repositoryMap.put("repoOwner", repository.getRepoOwner());
                repositoryMap.put("repoName", repository.getRepoName());
                repositoryMap.put("defaultBranch", repository.getDefaultBranch());
                repositoryMap.put("githubAppInstallationId", repository.getGithubAppInstallationId());
                
                result.put("success", true);
                result.put("repository", repositoryMap);
                
            } else {
                System.out.println("연결된 저장소가 없습니다.");
                result.put("success", false);
                result.put("error", "연결된 저장소가 없습니다.");
            }
            
        } catch (Exception e) {
            System.out.println("현재 저장소 조회 실패: " + e.getMessage());
            e.printStackTrace();
            result.put("success", false);
            result.put("error", e.getMessage());
        }
        
        return result;
    }

    // ==============================
    // GitHub 브랜치 관리 (간소화)
    // ==============================
    
    @Override
    @Transactional
    public Map<String, Object> deleteBranch(Map<String, Object> param) throws Exception {
        logger.info("GitHub 브랜치 삭제 시작");
        
        Map<String, Object> result = new HashMap<>();
        
        try {
            // GitHub API로 브랜치 삭제
            String accessToken = (String) param.get("access_token");
            String owner = (String) param.get("owner");
            String repo = (String) param.get("repo");
            String branchName = (String) param.get("branch_name");
            
            Map<String, Object> apiResponse = gitHubApiUtil.deleteBranch(accessToken, owner, repo, branchName);
            
            if ((Boolean) apiResponse.get("success")) {
                // 브랜치 삭제 이력은 웹훅으로 처리되므로 별도 저장하지 않음
            } else {
                throw new Exception("GitHub API 브랜치 삭제 실패");
            }
            
            result.put("success", true);
            result.put("message", "브랜치 삭제 완료");
            
            logger.info("GitHub 브랜치 삭제 완료");
            
        } catch (Exception e) {
            logger.error("GitHub 브랜치 삭제 실패", e);
            result.put("success", false);
            result.put("error", e.getMessage());
        }
        
        return result;
    }
    
    @Override
    public Map<String, Object> compareBranches(Map<String, Object> param) throws Exception {
        logger.info("GitHub 브랜치 비교 시작");
        
        Map<String, Object> result = new HashMap<>();
        
        try {
            // TODO: GitHub API로 브랜치 비교 (ahead/behind)
            
            result.put("success", true);
            
        } catch (Exception e) {
            logger.error("GitHub 브랜치 비교 실패", e);
            result.put("success", false);
            result.put("error", e.getMessage());
        }
        
        return result;
    }

    // ==============================
    // GitHub 웹훅 관리
    // ==============================
    
    @Override
    @Transactional
    public Map<String, Object> createWebhook(Map<String, Object> param) throws Exception {
        logger.info("GitHub 웹훅 생성 시작");
        
        Map<String, Object> result = new HashMap<>();
        
        try {
            // GitHub API로 웹훅 생성
            String accessToken = (String) param.get("access_token");
            String owner = (String) param.get("owner");
            String repo = (String) param.get("repo");
            String webhookUrl = (String) param.get("webhook_url");
            String[] events = (String[]) param.get("events");
            String secret = (String) param.get("secret");
            
            Map<String, Object> apiResponse = gitHubApiUtil.createWebhook(accessToken, owner, repo, webhookUrl, events, secret);
            
            if ((Boolean) apiResponse.get("success")) {
                @SuppressWarnings("unchecked")
                Map<String, Object> webhookData = (Map<String, Object>) apiResponse.get("data");
                
                result.put("success", true);
                result.put("message", "웹훅 생성 완료");
                result.put("webhook", webhookData);
                
                // DB에 웹훅 정보 저장 (선택사항)
                // TODO: 필요시 github_webhook 테이블에 저장
                
            } else {
                result.put("success", false);
                result.put("error", "GitHub API 웹훅 생성 실패");
            }
            
            logger.info("GitHub 웹훅 생성 완료");
            
        } catch (Exception e) {
            logger.error("GitHub 웹훅 생성 실패", e);
            result.put("success", false);
            result.put("error", e.getMessage());
        }
        
        return result;
    }
    
    @Override
    public Map<String, Object> getWebhookStatus(Map<String, Object> param) throws Exception {
        logger.info("GitHub 웹훅 상태 확인 시작");
        
        Map<String, Object> result = new HashMap<>();
        
        try {
            // TODO: GitHub API로 웹훅 상태 확인
            // test 디렉터리의 webhooks.js의 상태 확인 로직 참고
            
            result.put("exists", true);
            result.put("active", true);
            
        } catch (Exception e) {
            logger.error("GitHub 웹훅 상태 확인 실패", e);
            result.put("success", false);
            result.put("error", e.getMessage());
        }
        
        return result;
    }
    
    @Override
    @Transactional
    public Map<String, Object> deleteWebhook(Map<String, Object> param) throws Exception {
        logger.info("GitHub 웹훅 삭제 시작");
        
        Map<String, Object> result = new HashMap<>();
        
        try {
            // TODO: GitHub API로 웹훅 삭제
            
            result.put("success", true);
            result.put("message", "웹훅 삭제 완료");
            
        } catch (Exception e) {
            logger.error("GitHub 웹훅 삭제 실패", e);
            result.put("success", false);
            result.put("error", e.getMessage());
        }
        
        return result;
    }
    
    @Override
    @Transactional
    public Map<String, Object> processWebhookEvent(Map<String, Object> param) throws Exception {
        logger.info("GitHub 웹훅 이벤트 처리 시작");
        
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 웹훅 이벤트 처리
            String eventType = (String) param.get("event_type");
            String deliveryId = (String) param.get("delivery_id");
            String signature = (String) param.get("signature");
            String payloadJson = (String) param.get("payload");
            
            // 웹훅 이벤트 검증 및 정보 추출
            String secret = System.getProperty("github.webhook.secret", "");
            Map<String, Object> validationResult = gitHubWebhookUtil.validateAndPrepareEvent(
                eventType, deliveryId, signature, payloadJson, secret);
            
            if (!(Boolean) validationResult.get("success")) {
                result.put("success", false);
                result.put("error", validationResult.get("error"));
                return result;
            }
            
            @SuppressWarnings("unchecked")
            Map<String, Object> eventInfo = (Map<String, Object>) validationResult.get("event_info");
            
            // 웹훅 이벤트는 github_webhook 테이블에 자동으로 기록되므로 별도 저장하지 않음
            
            // 이벤트 타입별 처리
            switch (eventType) {
                case "push":
                    processPushEvent(param);
                    break;
                case "pull_request":
                    processPullRequestEvent(param);
                    break;
                case "create":
                    processCreateEvent(param);
                    break;
                case "delete":
                    processDeleteEvent(param);
                    break;
                default:
                    logger.info("처리되지 않은 이벤트 타입: {}", eventType);
            }
            
            result.put("success", true);
            result.put("delivery_id", deliveryId);
            result.put("message", "웹훅 이벤트 처리 완료");
            
        } catch (Exception e) {
            logger.error("GitHub 웹훅 이벤트 처리 실패", e);
            result.put("success", false);
            result.put("error", e.getMessage());
        }
        
        return result;
    }
    
    @Override
    public List<GithubWebhookVo> getWebhookEventLogs(Map<String, Object> param) throws Exception {
        logger.info("웹훅 이벤트 로그 조회 시작");
        
        List<GithubWebhookVo> result = new ArrayList<>();
        
        try {
            // github_webhook 테이블에서 웹훅 정보 조회
            // 실제 웹훅 이벤트는 웹훅 테이블 자체에 기록되므로 별도 이벤트 로그 테이블은 사용하지 않음
            result = new ArrayList<>(); // 임시로 빈 리스트 반환
            
            logger.info("웹훅 이벤트 로그 조회 완료: {} 개", result.size());
            
        } catch (Exception e) {
            logger.error("웹훅 이벤트 로그 조회 실패", e);
            throw e;
        }
        
        return result;
    }

    // ==============================
    // 웹훅 이벤트 처리 헬퍼 메서드들
    // ==============================
    
    @Transactional
    private void processPushEvent(Map<String, Object> param) throws Exception {
        logger.info("Push 이벤트 처리");
        
        try {
            // 페이로드에서 이벤트 정보 파싱
            String payload = (String) param.get("payload");
            if (payload == null || payload.trim().isEmpty()) {
                logger.warn("Push 이벤트 페이로드가 비어있습니다.");
                return;
            }
            
            // JSON 파싱
            com.fasterxml.jackson.databind.ObjectMapper objectMapper = new com.fasterxml.jackson.databind.ObjectMapper();
            @SuppressWarnings("unchecked")
            Map<String, Object> payloadMap = objectMapper.readValue(payload, Map.class);
            
            // Push 이벤트에서 브랜치 정보 추출
            String ref = (String) payloadMap.get("ref");
            String branchName = null;
            if (ref != null && ref.startsWith("refs/heads/")) {
                branchName = ref.substring("refs/heads/".length());
            }
            
            @SuppressWarnings("unchecked")
            Map<String, Object> repository = (Map<String, Object>) payloadMap.get("repository");
            String repositoryName = null;
            if (repository != null) {
                repositoryName = (String) repository.get("name");
            }
            
            @SuppressWarnings("unchecked")
            Map<String, Object> pusher = (Map<String, Object>) payloadMap.get("pusher");
            String senderLogin = null;
            if (pusher != null) {
                senderLogin = (String) pusher.get("name");
            }
            
            logger.info("Push 이벤트 파싱: ref={}, branchName={}, repository={}, pusher={}", 
                ref, branchName, repositoryName, senderLogin);
            
            // Push 이벤트는 브랜치 생성/삭제가 아닌 커밋 푸시이므로 별도 DB 처리하지 않음
            logger.info("Push 이벤트 처리 완료: {} -> {}", senderLogin, branchName);
            
        } catch (Exception e) {
            logger.error("Push 이벤트 처리 실패", e);
            throw e;
        }
    }
    
    @Transactional
    private void processPullRequestEvent(Map<String, Object> param) throws Exception {
        logger.info("Pull Request 이벤트 처리");
        
        try {
            // 페이로드에서 이벤트 정보 파싱
            String payload = (String) param.get("payload");
            if (payload == null || payload.trim().isEmpty()) {
                logger.warn("Pull Request 이벤트 페이로드가 비어있습니다.");
                return;
            }
            
            // JSON 파싱
            com.fasterxml.jackson.databind.ObjectMapper objectMapper = new com.fasterxml.jackson.databind.ObjectMapper();
            @SuppressWarnings("unchecked")
            Map<String, Object> payloadMap = objectMapper.readValue(payload, Map.class);
            
            String action = (String) payloadMap.get("action");
            
            @SuppressWarnings("unchecked")
            Map<String, Object> pullRequest = (Map<String, Object>) payloadMap.get("pull_request");
            String prNumber = null;
            if (pullRequest != null) {
                Object number = pullRequest.get("number");
                prNumber = number != null ? number.toString() : null;
            }
            
            @SuppressWarnings("unchecked")
            Map<String, Object> sender = (Map<String, Object>) payloadMap.get("sender");
            String senderLogin = null;
            if (sender != null) {
                senderLogin = (String) sender.get("login");
            }
            
            logger.info("Pull Request 이벤트 파싱: action={}, number={}, sender={}", 
                action, prNumber, senderLogin);
            
            // PR 이벤트는 현재 별도 DB 처리하지 않음 (필요시 추가)
            logger.info("Pull Request 이벤트 처리 완료: {} #{} by {}", action, prNumber, senderLogin);
            
        } catch (Exception e) {
            logger.error("Pull Request 이벤트 처리 실패", e);
            throw e;
        }
    }
    
    @Transactional
    private void processCreateEvent(Map<String, Object> param) throws Exception {
        logger.info("Create 이벤트 처리");
        
        try {
            // 페이로드에서 이벤트 정보 파싱
            String payload = (String) param.get("payload");
            if (payload == null || payload.trim().isEmpty()) {
                logger.warn("Create 이벤트 페이로드가 비어있습니다.");
                return;
            }
            
            // JSON 파싱
            com.fasterxml.jackson.databind.ObjectMapper objectMapper = new com.fasterxml.jackson.databind.ObjectMapper();
            @SuppressWarnings("unchecked")
            Map<String, Object> payloadMap = objectMapper.readValue(payload, Map.class);
            
            String refType = (String) payloadMap.get("ref_type");
            String refName = (String) payloadMap.get("ref");
            
            @SuppressWarnings("unchecked")
            Map<String, Object> repository = (Map<String, Object>) payloadMap.get("repository");
            String repoOwner = null;
            String repoName = null;
            
            if (repository != null) {
                @SuppressWarnings("unchecked")
                Map<String, Object> owner = (Map<String, Object>) repository.get("owner");
                if (owner != null) {
                    repoOwner = (String) owner.get("login");
                }
                repoName = (String) repository.get("name");
            }
            
            logger.info("브랜치 생성 이벤트 파싱: refType={}, refName={}, repository={}/{}", 
                refType, refName, repoOwner, repoName);
            
            if ("branch".equals(refType) && refName != null && repoOwner != null && repoName != null) {
                // repository_branch 테이블에 브랜치 추가
                insertBranchToDatabase(repoOwner, repoName, refName, payloadMap);
            }
            
            logger.info("Create 이벤트 처리 완료: {} {}", refType, refName);
            
        } catch (Exception e) {
            logger.error("Create 이벤트 처리 실패", e);
            throw e;
        }
    }
    
    @Transactional
    private void processDeleteEvent(Map<String, Object> param) throws Exception {
        logger.info("Delete 이벤트 처리");
        
        try {
            // 페이로드에서 이벤트 정보 파싱
            String payload = (String) param.get("payload");
            if (payload == null || payload.trim().isEmpty()) {
                logger.warn("Delete 이벤트 페이로드가 비어있습니다.");
                return;
            }
            
            // JSON 파싱
            com.fasterxml.jackson.databind.ObjectMapper objectMapper = new com.fasterxml.jackson.databind.ObjectMapper();
            @SuppressWarnings("unchecked")
            Map<String, Object> payloadMap = objectMapper.readValue(payload, Map.class);
            
            String refType = (String) payloadMap.get("ref_type");
            String refName = (String) payloadMap.get("ref");
            
            @SuppressWarnings("unchecked")
            Map<String, Object> repository = (Map<String, Object>) payloadMap.get("repository");
            String repoOwner = null;
            String repoName = null;
            
            if (repository != null) {
                @SuppressWarnings("unchecked")
                Map<String, Object> owner = (Map<String, Object>) repository.get("owner");
                if (owner != null) {
                    repoOwner = (String) owner.get("login");
                }
                repoName = (String) repository.get("name");
            }
            
            logger.info("브랜치 삭제 이벤트 파싱: refType={}, refName={}, repository={}/{}", 
                refType, refName, repoOwner, repoName);
            
            if ("branch".equals(refType) && refName != null && repoOwner != null && repoName != null) {
                // repository_branch 테이블에서 브랜치 삭제
                deleteBranchFromDatabase(repoOwner, repoName, refName);
            }
            
            logger.info("Delete 이벤트 처리 완료: {} {}", refType, refName);
            
        } catch (Exception e) {
            logger.error("Delete 이벤트 처리 실패", e);
            throw e;
        }
    }
    
    // ==============================
    // 웹훅 이벤트 처리 헬퍼 메서드들
    // ==============================
    
    /**
     * 브랜치 생성 시 repository_branch 테이블에 데이터 삽입 (중복 방지)
     */
    @Transactional
    private void insertBranchToDatabase(String repoOwner, String repoName, String branchName, Map<String, Object> payloadMap) throws Exception {
        logger.info("데이터베이스에 브랜치 추가: {}/{} - {}", repoOwner, repoName, branchName);
        
        try {
            // 먼저 해당 저장소가 project_repository에 등록되어 있는지 확인
            String projectRepoId = findProjectRepoId(repoOwner, repoName);
            if (projectRepoId == null) {
                logger.warn("프로젝트에 연결되지 않은 저장소의 브랜치 생성 이벤트: {}/{}", repoOwner, repoName);
                return;
            }
            
            // 중복 브랜치 확인 - 이미 존재하는지 검사
            if (isBranchExists(projectRepoId, branchName)) {
                logger.warn("브랜치가 이미 존재합니다. 중복 생성 방지: {}/{} - {}", repoOwner, repoName, branchName);
                return;
            }
            
            // repository_branch 테이블에 브랜치 정보 삽입
            com.demo.proworks.repobranch.vo.RepositoryBranchVo branchVo = new com.demo.proworks.repobranch.vo.RepositoryBranchVo();
            branchVo.setProjectRepoId(projectRepoId);
            branchVo.setBranchName(branchName);
            branchVo.setCreatedAt(new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date()));
            
            // 페이로드에서 추가 정보 추출 (있는 경우)
            if (payloadMap != null) {
                // GitHub 웹훅에서 브랜치 생성 시 커밋 SHA는 master_branch에서 가져올 수 있음
                @SuppressWarnings("unchecked")
                Map<String, Object> repository = (Map<String, Object>) payloadMap.get("repository");
                if (repository != null) {
                    String defaultBranch = (String) repository.get("default_branch");
                    if (defaultBranch != null) {
                        branchVo.setBaseSha("created_from_" + defaultBranch);
                    }
                }
            }
            
            int insertResult = repositoryBranchService.insertRepositoryBranch(branchVo);
            logger.info("브랜치 데이터베이스 삽입 완료: {} (result: {})", branchName, insertResult);
            
        } catch (org.springframework.dao.DuplicateKeyException e) {
            // 유니크 제약 조건 위반 시 (데이터베이스 레벨에서의 중복 방지)
            logger.warn("브랜치 중복 생성 감지 (DB 제약): {}/{} - {}", repoOwner, repoName, branchName);
        } catch (Exception e) {
            logger.error("브랜치 데이터베이스 삽입 실패: {}/{} - {}", repoOwner, repoName, branchName, e);
            throw e;
        }
    }
    
    /**
     * 브랜치 삭제 시 repository_branch 테이블에서 데이터 삭제
     */
    @Transactional
    private void deleteBranchFromDatabase(String repoOwner, String repoName, String branchName) throws Exception {
        logger.info("데이터베이스에서 브랜치 삭제: {}/{} - {}", repoOwner, repoName, branchName);
        
        try {
            // 먼저 해당 저장소가 project_repository에 등록되어 있는지 확인
            String projectRepoId = findProjectRepoId(repoOwner, repoName);
            if (projectRepoId == null) {
                logger.warn("프로젝트에 연결되지 않은 저장소의 브랜치 삭제 이벤트: {}/{}", repoOwner, repoName);
                return;
            }
            
            // repository_branch 테이블에서 브랜치 정보 삭제
            com.demo.proworks.repobranch.vo.RepositoryBranchVo branchVo = new com.demo.proworks.repobranch.vo.RepositoryBranchVo();
            branchVo.setProjectRepoId(projectRepoId);
            branchVo.setBranchName(branchName);
            
            int deleteResult = repositoryBranchService.deleteRepositoryBranch(branchVo);
            logger.info("브랜치 데이터베이스 삭제 완료: {} (result: {})", branchName, deleteResult);
            
        } catch (Exception e) {
            logger.error("브랜치 데이터베이스 삭제 실패: {}/{} - {}", repoOwner, repoName, branchName, e);
            throw e;
        }
    }
    
    /**
     * 저장소 소유자와 이름으로 project_repository 테이블에서 project_repo_id 찾기
     */
    private String findProjectRepoId(String repoOwner, String repoName) throws Exception {
        try {
            // project_repository 테이블에서 해당 저장소 조회
            ProjectRepositoryVo repoVo = new ProjectRepositoryVo();
            repoVo.setRepoOwner(repoOwner);
            repoVo.setRepoName(repoName);
            
            // DAO를 통해 저장소 조회 (GitHubDAO 사용)
            ProjectRepositoryVo foundRepo = gitHubDAO.selectProjectRepositoryByOwnerAndName(repoVo);
            if (foundRepo != null) {
                return foundRepo.getProjectRepoId();
            }
            
            return null;
            
        } catch (Exception e) {
            logger.error("프로젝트 저장소 ID 조회 실패: {}/{}", repoOwner, repoName, e);
            throw e;
        }
    }
    
    /**
     * 브랜치가 이미 존재하는지 확인 (효율적인 COUNT 쿼리 사용)
     */
    private boolean isBranchExists(String projectRepoId, String branchName) throws Exception {
        try {
            // 효율적인 COUNT 쿼리로 존재 여부만 확인
            Map<String, Object> searchParam = new HashMap<>();
            searchParam.put("project_repo_id", projectRepoId);
            searchParam.put("branch_name", branchName);
            
            int count = gitHubDAO.selectBranchExists(searchParam);
            boolean exists = count > 0;
            
            logger.debug("브랜치 존재 여부 확인: {} (projectRepoId: {}) = {}", branchName, projectRepoId, exists);
            return exists;
            
        } catch (Exception e) {
            logger.error("브랜치 존재 여부 확인 실패: {} (projectRepoId: {})", branchName, projectRepoId, e);
            throw e;
        }
    }

    // ==============================
    // 기타 메서드들 (간략화)
    // ==============================
    
    @Override
    public List<Map<String, Object>> getIssues(Map<String, Object> param) throws Exception {
        // TODO: GitHub 이슈 목록 조회 구현
        return new ArrayList<>();
    }
    
    @Override
    public List<Map<String, Object>> getPullRequests(Map<String, Object> param) throws Exception {
        // TODO: GitHub PR 목록 조회 구현
        return new ArrayList<>();
    }
    
    @Override
    public Map<String, Object> createIssue(Map<String, Object> param) throws Exception {
        // TODO: GitHub 이슈 생성 구현
        return new HashMap<>();
    }
    
    @Override
    public Map<String, Object> createPullRequest(Map<String, Object> param) throws Exception {
        // TODO: GitHub PR 생성 구현
        return new HashMap<>();
    }
    
    @Override
    public List<Map<String, Object>> getCommitHistory(Map<String, Object> param) throws Exception {
        // TODO: 커밋 히스토리 조회 구현
        return new ArrayList<>();
    }
    
    @Override
    public Map<String, Object> getCommitDetails(Map<String, Object> param) throws Exception {
        // TODO: 커밋 상세 정보 조회 구현
        return new HashMap<>();
    }
    
    @Override
    @Transactional
    public Map<String, Object> logBranchActivity(Map<String, Object> param) throws Exception {
        Map<String, Object> result = new HashMap<>();
        // 브랜치 활동은 웹훅으로 자동 처리되므로 별도 로그 저장하지 않음
        result.put("success", true);
        result.put("message", "브랜치 활동은 웹훅으로 자동 기록됩니다");
        return result;
    }
    
    @Override
    public Map<String, Object> syncProjectWithRepository(Map<String, Object> param) throws Exception {
        // TODO: 프로젝트-레포지토리 동기화 구현
        return new HashMap<>();
    }
    
    @Override
    public Map<String, Object> linkTaskWithIssue(Map<String, Object> param) throws Exception {
        // TODO: Task-이슈 연동 구현
        return new HashMap<>();
    }
    
    @Override
    public Map<String, Object> syncGitHubActivityToProWorks(Map<String, Object> param) throws Exception {
        // TODO: GitHub 활동 동기화 구현
        return new HashMap<>();
    }
    
    @Override
    public Map<String, Object> getProjectGitHubStats(Map<String, Object> param) throws Exception {
        return gitHubDAO.selectProjectGitHubActivityStats(param);
    }
    
    @Override
    public Map<String, Object> getUserGitHubStats(Map<String, Object> param) throws Exception {
        return gitHubDAO.selectUserGitHubActivityStats(param);
    }
    
    @Override
    public Map<String, Object> getServiceStatus() throws Exception {
        return gitHubDAO.selectGitHubServiceStatus();
    }
    
    @Override
    public Map<String, Object> getApiUsage(String accessToken) throws Exception {
        // TODO: GitHub API 사용량 조회 구현
        return new HashMap<>();
    }
    
    @Override
    public Map<String, Object> getGitHubSettings(Map<String, Object> param) throws Exception {
        // TODO: GitHub 설정 조회 구현
        return new HashMap<>();
    }
    
    @Override
    @Transactional
    public Map<String, Object> updateGitHubSettings(Map<String, Object> param) throws Exception {
        // TODO: GitHub 설정 업데이트 구현
        return new HashMap<>();
    }
    
    @Override
    @Transactional
    public Map<String, Object> disconnectGitHub(Map<String, Object> param) throws Exception {
        // TODO: GitHub 연결 해제 구현
        return new HashMap<>();
    }
    
    /**
     * 현재 프로젝트 레포지토리 ID를 가져오는 헬퍼 메서드
     * @return 프로젝트 레포지토리 ID
     */
    private String getCurrentProjectRepoId() {
        // 현재 세션 또는 컨텍스트에서 프로젝트 레포지토리 ID를 가져오는 로직
        // 임시로 기본값 반환 (실제 구현 시 수정 필요)
        String projectRepoId = "1"; // 기본 프로젝트 레포지토리 ID
        
        // TODO: 실제 구현 시 다음 중 하나의 방식으로 구현
        // 1. HttpSession에서 현재 선택된 프로젝트 정보 가져오기
        // 2. SecurityContext에서 현재 사용자의 기본 프로젝트 가져오기
        // 3. 파라미터로 전달된 프로젝트 ID 사용
        // 4. 사용자별 최근 사용 프로젝트 조회
        
        logger.debug("현재 프로젝트 레포지토리 ID: {}", projectRepoId);
        return projectRepoId;
    }
}