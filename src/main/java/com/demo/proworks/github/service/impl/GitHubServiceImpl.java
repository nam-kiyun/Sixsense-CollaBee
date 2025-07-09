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
import com.demo.proworks.userpersonaltoken.vo.UserPersonalTokenVo;
import com.demo.proworks.repobranch.vo.RepositoryBranchVo;
import com.demo.proworks.githubwebhook.vo.GithubWebhookVo;
import com.demo.proworks.githubapptoken.vo.GithubAppTokenVo;
import com.demo.proworks.github.vo.GitHubRepositoryVo;
import com.demo.proworks.github.vo.GitHubRepositoryListVo;
import com.demo.proworks.github.vo.GitHubBranchVo;
import com.demo.proworks.github.vo.GitHubBranchListVo;

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
            
            // GitHub API 호출하여 액세스 토큰 획득
            String clientId = System.getProperty("github.client.id", "your-client-id");
            String clientSecret = System.getProperty("github.client.secret", "your-client-secret");
            
            Map<String, Object> tokenResponse = gitHubApiUtil.exchangeCodeForToken(code, clientId, clientSecret);
            
            if ((Boolean) tokenResponse.get("success")) {
                @SuppressWarnings("unchecked")
                Map<String, Object> tokenData = (Map<String, Object>) tokenResponse.get("data");
                String accessToken = (String) tokenData.get("access_token");
                String scope = (String) tokenData.get("scope");
                
                // GitHub 사용자 정보 조회
                Map<String, Object> userInfo = gitHubApiUtil.getUserInfo(accessToken);
                
                // GitHub 사용자를 로컬 사용자와 연동
                UserPersonalTokenVo userToken = linkGitHubUser(accessToken);
                
                result.put("success", true);
                result.put("message", "OAuth 콜백 처리 완료");
                result.put("user", userToken);
                result.put("access_token", accessToken);
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
    public UserPersonalTokenVo linkGitHubUser(String accessToken) throws Exception {
        logger.info("GitHub 사용자 연동 시작");
        
        UserPersonalTokenVo userToken = null;
        
        try {
            // GitHub API로 사용자 정보 조회
            Map<String, Object> userInfo = gitHubApiUtil.getUserInfo(accessToken);
            
            // user_personal_token 테이블에 저장할 데이터 준비
            Map<String, Object> param = new HashMap<>();
            param.put("user_personal_token_id", java.util.UUID.randomUUID().toString());
            param.put("user_id", "current_user_id"); // TODO: 세션에서 현재 사용자 ID 가져오기
            param.put("access_token", accessToken);
            param.put("scope", "repo read:user admin:repo_hook");
            param.put("expired_at", null); // GitHub 토큰은 만료되지 않음
            
            // DB에 GitHub 사용자 정보 저장/업데이트
            String tokenId = gitHubDAO.upsertGitHubUser(param);
            
            // 저장된 토큰 정보 조회
            userToken = gitHubDAO.selectGitHubUserByLocalId("current_user_id");
            
            logger.info("GitHub 사용자 연동 완료: {}", tokenId);
            
        } catch (Exception e) {
            logger.error("GitHub 사용자 연동 실패", e);
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
        logger.info("레포지토리 선택 처리 시작");
        
        Map<String, Object> result = new HashMap<>();
        
        try {
            // TODO: 사용자의 레포지토리 선택 정보 DB 저장
            // test 디렉터리의 auth.js의 select-repo 로직 참고
            
            String recordId = gitHubDAO.insertUserSelectedRepository(param);
            
            result.put("success", true);
            result.put("record_id", recordId);
            result.put("message", "레포지토리 선택 완료");
            
            logger.info("레포지토리 선택 처리 완료");
            
        } catch (Exception e) {
            logger.error("레포지토리 선택 처리 실패", e);
            result.put("success", false);
            result.put("error", e.getMessage());
        }
        
        return result;
    }
    
    @Override
    public ProjectRepositoryVo getCurrentRepository(String userId) throws Exception {
        logger.info("현재 선택된 레포지토리 조회 시작: {}", userId);
        
        ProjectRepositoryVo repository = null;
        
        try {
            repository = gitHubDAO.selectUserSelectedRepository(userId);
            logger.info("현재 선택된 레포지토리 조회 완료");
            
        } catch (Exception e) {
            logger.error("현재 선택된 레포지토리 조회 실패", e);
            throw e;
        }
        
        return repository;
    }

    // ==============================
    // GitHub 브랜치 관리
    // ==============================
    
    @Override
    public GitHubBranchListVo getBranches(Map<String, Object> param) throws Exception {
        logger.info("GitHub 브랜치 목록 조회 시작");
        
        GitHubBranchListVo result = new GitHubBranchListVo();
        
        try {
            // GitHub API로 브랜치 목록 조회 및 DB 동기화
            String accessToken = (String) param.get("access_token");
            String owner = (String) param.get("owner");
            String repo = (String) param.get("repo");
            
            // GitHub API에서 브랜치 목록 조회
            Map<String, Object> apiResponse = gitHubApiUtil.getBranches(accessToken, owner, repo);
            
            List<GitHubBranchVo> branches = new ArrayList<>();
            
            if ((Boolean) apiResponse.get("success")) {
                @SuppressWarnings("unchecked")
                List<Map<String, Object>> branchList = (List<Map<String, Object>>) apiResponse.get("data");
                
                String projectRepoId = (String) param.get("project_repo_id");
                String repositoryFullName = owner + "/" + repo;
                
                for (Map<String, Object> branchData : branchList) {
                    // GitHub 브랜치 정보를 ProWorks 형식으로 변환
                    Map<String, Object> localBranch = gitHubSyncUtil.convertGitHubBranchToLocal(
                        branchData, projectRepoId, repositoryFullName);
                    
                    GitHubBranchVo branch = new GitHubBranchVo();
                    branch.setProjectRepoId((String) localBranch.get("project_repo_id"));
                    branch.setRepositoryFullName((String) localBranch.get("repository_full_name"));
                    branch.setBranchName((String) localBranch.get("branch_name"));
                    branch.setCommitSha((String) localBranch.get("commit_sha"));
                    branch.setCommitMessage((String) localBranch.get("commit_message"));
                    branch.setCommitAuthor((String) localBranch.get("commit_author"));
                    branch.setCommitDate((String) localBranch.get("commit_date"));
                    branch.setIsProtected((String) localBranch.get("is_protected"));
                    branch.setIsDefault((String) localBranch.get("is_default"));
                    branch.setBranchUrl((String) localBranch.get("branch_url"));
                    
                    branches.add(branch);
                }
            }
            
            // GitHub API에서 가져온 브랜치 정보를 우선 사용
            result.setGitHubBranchVoList(branches);
            
            // DB 브랜치 정보는 필요시 별도로 조회하여 비교/동기화에 사용
            
            logger.info("GitHub 브랜치 목록 조회 완료: {} 개", branches.size());
            
        } catch (Exception e) {
            logger.error("GitHub 브랜치 목록 조회 실패", e);
            throw e;
        }
        
        return result;
    }
    
    @Override
    public GitHubBranchVo getBranch(Map<String, Object> param) throws Exception {
        logger.info("GitHub 브랜치 상세 조회 시작");
        
        GitHubBranchVo branch = new GitHubBranchVo();
        
        try {
            // TODO: GitHub API로 특정 브랜치 상세 정보 조회
            
            logger.info("GitHub 브랜치 상세 조회 완료");
            
        } catch (Exception e) {
            logger.error("GitHub 브랜치 상세 조회 실패", e);
            throw e;
        }
        
        return branch;
    }
    
    @Override
    @Transactional
    public GitHubBranchVo createBranch(Map<String, Object> param) throws Exception {
        logger.info("GitHub 브랜치 생성 시작");
        
        GitHubBranchVo branch = new GitHubBranchVo();
        
        try {
            // GitHub API로 브랜치 생성
            String accessToken = (String) param.get("access_token");
            String owner = (String) param.get("owner");
            String repo = (String) param.get("repo");
            String branchName = (String) param.get("branch_name");
            String sourceBranch = (String) param.get("source_branch");
            
            // 소스 브랜치의 SHA 가져오기
            String sourceSha = null;
            if (sourceBranch != null) {
                String endpoint = "/repos/" + owner + "/" + repo + "/git/refs/heads/" + sourceBranch;
                Map<String, Object> refResponse = gitHubApiUtil.get(endpoint, accessToken);
                if ((Boolean) refResponse.get("success")) {
                    @SuppressWarnings("unchecked")
                    Map<String, Object> refData = (Map<String, Object>) refResponse.get("data");
                    @SuppressWarnings("unchecked")
                    Map<String, Object> object = (Map<String, Object>) refData.get("object");
                    sourceSha = (String) object.get("sha");
                }
            }
            
            if (sourceSha != null) {
                // GitHub API로 브랜치 생성
                Map<String, Object> apiResponse = gitHubApiUtil.createBranch(accessToken, owner, repo, branchName, sourceSha);
                
                if ((Boolean) apiResponse.get("success")) {
                    // 브랜치 정보 생성
                    branch.setProjectRepoId((String) param.get("project_repo_id"));
                    branch.setRepositoryFullName(owner + "/" + repo);
                    branch.setBranchName(branchName);
                    branch.setCommitSha(sourceSha);
                    branch.setIsProtected("N");
                    branch.setIsDefault("N");
                    branch.setBranchUrl("https://github.com/" + owner + "/" + repo + "/tree/" + branchName);
                    
                    // DB에 브랜치 정보 저장 (RepositoryBranchVo로 변환)
                    RepositoryBranchVo repositoryBranch = new RepositoryBranchVo();
                    repositoryBranch.setProjectRepoId(branch.getProjectRepoId());
                    repositoryBranch.setBranchName(branch.getBranchName());
                    repositoryBranch.setBaseSha(branch.getCommitSha());
                    
                    String branchId = gitHubDAO.insertGitHubBranch(repositoryBranch);
                    
                    // 브랜치 생성 이력은 웹훅으로 처리되므로 별도 저장하지 않음
                    
                    logger.info("GitHub 브랜치 생성 완료: {}", branchId);
                } else {
                    throw new Exception("GitHub API 브랜치 생성 실패");
                }
            } else {
                throw new Exception("소스 브랜치 SHA를 찾을 수 없습니다");
            }
            
        } catch (Exception e) {
            logger.error("GitHub 브랜치 생성 실패", e);
            throw e;
        }
        
        return branch;
    }
    
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
        // Push 이벤트 처리 로직 구현
        @SuppressWarnings("unchecked")
        Map<String, Object> eventInfo = (Map<String, Object>) param.get("event_info");
        
        String branchName = (String) eventInfo.get("branch_name");
        String repositoryName = (String) eventInfo.get("repository_name");
        String senderLogin = (String) eventInfo.get("sender_login");
        
        // 브랜치 활동 이력은 웹훅 자체에 기록되므로 별도 저장하지 않음
        
        // ProWorks 로그에 GitHub 활동 동기화
        Map<String, Object> logEntry = gitHubSyncUtil.convertWebhookEventToLog(
            "push", eventInfo, (String) param.get("project_id"), (String) param.get("user_id"));
        
        // TODO: project_log 테이블에 저장하는 로직 필요시 추가
        
        logger.info("Push 이벤트 처리 완료: {} -> {}", senderLogin, branchName);
    }
    
    @Transactional
    private void processPullRequestEvent(Map<String, Object> param) throws Exception {
        logger.info("Pull Request 이벤트 처리");
        // PR 이벤트 처리 로직 구현
        @SuppressWarnings("unchecked")
        Map<String, Object> eventInfo = (Map<String, Object>) param.get("event_info");
        
        String action = (String) eventInfo.get("action");
        String prNumber = eventInfo.get("pr_number") != null ? eventInfo.get("pr_number").toString() : null;
        String senderLogin = (String) eventInfo.get("sender_login");
        
        // PR 정보를 Task로 동기화 (필요시)
        if ("opened".equals(action)) {
            Map<String, Object> prTask = gitHubSyncUtil.convertGitHubPRToTask(
                eventInfo, (String) param.get("project_id"), (String) param.get("user_id"));
            
            // TODO: task 테이블에 저장하는 로직 필요시 추가
        }
        
        logger.info("Pull Request 이벤트 처리 완료: {} #{} by {}", action, prNumber, senderLogin);
    }
    
    @Transactional
    private void processCreateEvent(Map<String, Object> param) throws Exception {
        logger.info("Create 이벤트 처리");
        // 브랜치/태그 생성 이벤트 처리 로직 구현
        @SuppressWarnings("unchecked")
        Map<String, Object> eventInfo = (Map<String, Object>) param.get("event_info");
        
        String refType = (String) eventInfo.get("ref_type");
        String refName = (String) eventInfo.get("ref");
        String senderLogin = (String) eventInfo.get("sender_login");
        
        if ("branch".equals(refType)) {
            // 브랜치 생성 이력은 웹훅 자체에 기록되므로 별도 저장하지 않음
        }
        
        logger.info("Create 이벤트 처리 완료: {} {} by {}", refType, refName, senderLogin);
    }
    
    @Transactional
    private void processDeleteEvent(Map<String, Object> param) throws Exception {
        logger.info("Delete 이벤트 처리");
        // 브랜치/태그 삭제 이벤트 처리 로직 구현
        @SuppressWarnings("unchecked")
        Map<String, Object> eventInfo = (Map<String, Object>) param.get("event_info");
        
        String refType = (String) eventInfo.get("ref_type");
        String refName = (String) eventInfo.get("ref");
        String senderLogin = (String) eventInfo.get("sender_login");
        
        if ("branch".equals(refType)) {
            // 브랜치 삭제 이력은 웹훅 자체에 기록되므로 별도 저장하지 않음
        }
        
        logger.info("Delete 이벤트 처리 완료: {} {} by {}", refType, refName, senderLogin);
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
        return gitHubDAO.selectGitHubActivityStats(param);
    }
    
    @Override
    public Map<String, Object> getUserGitHubStats(Map<String, Object> param) throws Exception {
        return gitHubDAO.selectUserGitHubActivityStats(param);
    }
    
    @Override
    public Map<String, Object> getServiceStatus() throws Exception {
        return gitHubDAO.selectServiceStatus();
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
}