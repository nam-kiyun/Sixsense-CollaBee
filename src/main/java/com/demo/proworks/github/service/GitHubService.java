package com.demo.proworks.github.service;

import java.util.List;
import java.util.Map;

import com.demo.proworks.projectrepo.vo.ProjectRepositoryVo;
import com.demo.proworks.githubapptoken.vo.GithubAppTokenVo;
import com.demo.proworks.repobranch.vo.RepositoryBranchVo;
import com.demo.proworks.githubwebhook.vo.GithubWebhookVo;
import com.demo.proworks.githubapptoken.vo.GithubAppTokenVo;
import com.demo.proworks.github.vo.GitHubRepositoryVo;
import com.demo.proworks.github.vo.GitHubRepositoryListVo;
import com.demo.proworks.userpersonaltoken.vo.UserPersonalTokenVo;

/**
 * GitHub 통합 서비스 인터페이스
 * test 디렉터리의 서비스 기능을 기반으로 작성
 */
public interface GitHubService {

    // ==============================
    // GitHub OAuth 인증 관리
    // ==============================
    
    /**
     * GitHub OAuth 인증 URL 생성
     * @param param 인증 요청 정보 (redirect_uri, state)
     * @return GitHub OAuth URL
     * @throws Exception
     */
    Map<String, Object> generateOAuthUrl(Map<String, Object> param) throws Exception;
    
    /**
     * GitHub OAuth 콜백 처리
     * @param param OAuth 콜백 정보 (code, state)
     * @return 인증된 사용자 정보
     * @throws Exception
     */
    Map<String, Object> processOAuthCallback(Map<String, Object> param) throws Exception;
    
    /**
     * GitHub 사용자 정보 조회 및 로컬 계정 연동
     * @param accessToken GitHub 액세스 토큰
     * @param projectId 프로젝트 ID
     * @return 연동된 사용자 정보
     * @throws Exception
     */
    UserPersonalTokenVo linkGitHubUser(String accessToken, String projectId) throws Exception;
    
    /**
     * GitHub 사용자 정보 조회 및 특정 사용자 ID와 연동
     * @param accessToken GitHub 액세스 토큰
     * @param userId 로컬 사용자 ID
     * @param projectId 프로젝트 ID
     * @return 연동된 사용자 정보
     * @throws Exception
     */
    UserPersonalTokenVo linkGitHubUserWithUserId(String accessToken, String userId, String projectId) throws Exception;
    
    /**
     * GitHub 앱 설치 상태 확인
     * @param param 확인 정보 (access_token, username)
     * @return 설치 상태 정보
     * @throws Exception
     */
    Map<String, Object> checkAppInstallation(Map<String, Object> param) throws Exception;
    
    /**
     * GitHub 앱 설치 완료 처리
     * @param param 설치 정보 (installation_id, setup_action)
     * @return 처리 결과
     * @throws Exception
     */
    Map<String, Object> processAppInstallation(Map<String, Object> param) throws Exception;

    // ==============================
    // GitHub 레포지토리 관리
    // ==============================
    
    /**
     * 사용자의 GitHub 레포지토리 목록 조회
     * @param param 조회 조건 (access_token, type, sort, per_page)
     * @return 레포지토리 목록
     * @throws Exception
     */
    GitHubRepositoryListVo getRepositories(Map<String, Object> param) throws Exception;
    
    /**
     * 특정 레포지토리 상세 정보 조회
     * @param param 조회 조건 (access_token, owner, repo)
     * @return 레포지토리 상세 정보
     * @throws Exception
     */
    GitHubRepositoryVo getRepository(Map<String, Object> param) throws Exception;
    
    /**
     * 사용자의 레포지토리 선택 처리
     * @param param 선택 정보 (user_id, repository_data)
     * @return 선택 결과
     * @throws Exception
     */
    Map<String, Object> selectRepository(Map<String, Object> param) throws Exception;
    
    /**
     * 현재 선택된 레포지토리 정보 조회
     * @param userId 사용자 ID
     * @return 선택된 레포지토리 정보
     * @throws Exception
     */
    ProjectRepositoryVo getCurrentRepository(String userId) throws Exception;
    
    /**
     * 현재 선택된 레포지토리 정보 조회 (Map 형태)
     * @param param 조회 조건 (user_id)
     * @return 선택된 레포지토리 정보
     * @throws Exception
     */
    Map<String, Object> getCurrentRepository(Map<String, Object> param) throws Exception;
    
    /**
     * 프로젝트 ID로 연결된 레포지토리 정보 조회
     * @param projectId 프로젝트 ID
     * @return 연결된 레포지토리 정보
     * @throws Exception
     */
    ProjectRepositoryVo getCurrentRepositoryByProjectId(String projectId) throws Exception;

    // ==============================
    // GitHub 브랜치 관리 (간소화)
    // ==============================
    
    /**
     * 브랜치 삭제
     * @param param 삭제 정보 (access_token, owner, repo, branch_name)
     * @return 삭제 결과
     * @throws Exception
     */
    Map<String, Object> deleteBranch(Map<String, Object> param) throws Exception;
    
    /**
     * 브랜치 비교 (ahead/behind 정보)
     * @param param 비교 정보 (access_token, owner, repo, base, head)
     * @return 비교 결과
     * @throws Exception
     */
    Map<String, Object> compareBranches(Map<String, Object> param) throws Exception;

    // ==============================
    // GitHub 웹훅 관리
    // ==============================
    
    /**
     * GitHub 웹훅 생성
     * @param param 웹훅 생성 정보 (access_token, owner, repo, webhook_url, events, secret)
     * @return 생성된 웹훅 정보
     * @throws Exception
     */
    Map<String, Object> createWebhook(Map<String, Object> param) throws Exception;
    
    /**
     * GitHub 웹훅 상태 확인
     * @param param 확인 정보 (access_token, owner, repo, webhook_id)
     * @return 웹훅 상태 정보
     * @throws Exception
     */
    Map<String, Object> getWebhookStatus(Map<String, Object> param) throws Exception;
    
    /**
     * GitHub 웹훅 삭제
     * @param param 삭제 정보 (access_token, owner, repo, webhook_id)
     * @return 삭제 결과
     * @throws Exception
     */
    Map<String, Object> deleteWebhook(Map<String, Object> param) throws Exception;
    
    /**
     * 웹훅 이벤트 처리
     * @param param 이벤트 정보 (event_type, payload, signature, delivery_id)
     * @return 처리 결과
     * @throws Exception
     */
    Map<String, Object> processWebhookEvent(Map<String, Object> param) throws Exception;
    
    /**
     * 웹훅 이벤트 로그 조회
     * @param param 조회 조건 (project_repo_id, event_type, limit)
     * @return 이벤트 로그 목록
     * @throws Exception
     */
    List<GithubWebhookVo> getWebhookEventLogs(Map<String, Object> param) throws Exception;

    // ==============================
    // GitHub 이슈 및 PR 관리
    // ==============================
    
    /**
     * 레포지토리의 이슈 목록 조회
     * @param param 조회 조건 (access_token, owner, repo, state, labels)
     * @return 이슈 목록
     * @throws Exception
     */
    List<Map<String, Object>> getIssues(Map<String, Object> param) throws Exception;
    
    /**
     * 레포지토리의 Pull Request 목록 조회
     * @param param 조회 조건 (access_token, owner, repo, state, base, head)
     * @return PR 목록
     * @throws Exception
     */
    List<Map<String, Object>> getPullRequests(Map<String, Object> param) throws Exception;
    
    /**
     * 새로운 이슈 생성
     * @param param 이슈 생성 정보 (access_token, owner, repo, title, body, labels, assignees)
     * @return 생성된 이슈 정보
     * @throws Exception
     */
    Map<String, Object> createIssue(Map<String, Object> param) throws Exception;
    
    /**
     * Pull Request 생성
     * @param param PR 생성 정보 (access_token, owner, repo, title, body, head, base)
     * @return 생성된 PR 정보
     * @throws Exception
     */
    Map<String, Object> createPullRequest(Map<String, Object> param) throws Exception;

    // ==============================
    // GitHub 커밋 및 히스토리
    // ==============================
    
    /**
     * 브랜치의 커밋 히스토리 조회
     * @param param 조회 조건 (access_token, owner, repo, branch, since, until)
     * @return 커밋 히스토리
     * @throws Exception
     */
    List<Map<String, Object>> getCommitHistory(Map<String, Object> param) throws Exception;
    
    /**
     * 특정 커밋 상세 정보 조회
     * @param param 조회 조건 (access_token, owner, repo, commit_sha)
     * @return 커밋 상세 정보
     * @throws Exception
     */
    Map<String, Object> getCommitDetails(Map<String, Object> param) throws Exception;
    
    /**
     * 브랜치 활동 히스토리 로그 저장
     * @param param 활동 정보 (user_id, repo_id, branch_name, action, source_branch)
     * @return 저장 결과
     * @throws Exception
     */
    Map<String, Object> logBranchActivity(Map<String, Object> param) throws Exception;

    // ==============================
    // GitHub 동기화 및 통합
    // ==============================
    
    /**
     * 프로젝트와 GitHub 레포지토리 동기화
     * @param param 동기화 정보 (project_id, repository_info)
     * @return 동기화 결과
     * @throws Exception
     */
    Map<String, Object> syncProjectWithRepository(Map<String, Object> param) throws Exception;
    
    /**
     * Task와 GitHub 이슈 연동
     * @param param 연동 정보 (task_id, issue_number, sync_type)
     * @return 연동 결과
     * @throws Exception
     */
    Map<String, Object> linkTaskWithIssue(Map<String, Object> param) throws Exception;
    
    /**
     * GitHub 활동을 ProWorks 로그에 동기화
     * @param param 동기화 정보 (activity_data, sync_options)
     * @return 동기화 결과
     * @throws Exception
     */
    Map<String, Object> syncGitHubActivityToProWorks(Map<String, Object> param) throws Exception;

    // ==============================
    // 통계 및 모니터링
    // ==============================
    
    /**
     * 프로젝트별 GitHub 활동 통계 조회
     * @param param 조회 조건 (project_id, date_from, date_to, group_by)
     * @return 활동 통계
     * @throws Exception
     */
    Map<String, Object> getProjectGitHubStats(Map<String, Object> param) throws Exception;
    
    /**
     * 사용자별 GitHub 활동 통계 조회
     * @param param 조회 조건 (user_id, date_from, date_to)
     * @return 사용자 활동 통계
     * @throws Exception
     */
    Map<String, Object> getUserGitHubStats(Map<String, Object> param) throws Exception;
    
    /**
     * GitHub 통합 서비스 상태 확인
     * @return 서비스 상태 정보
     * @throws Exception
     */
    Map<String, Object> getServiceStatus() throws Exception;
    
    /**
     * GitHub API 사용량 확인
     * @param accessToken GitHub 액세스 토큰
     * @return API 사용량 정보
     * @throws Exception
     */
    Map<String, Object> getApiUsage(String accessToken) throws Exception;

    // ==============================
    // 설정 및 관리
    // ==============================
    
    /**
     * GitHub 통합 설정 조회
     * @param param 조회 조건 (user_id, project_id)
     * @return 통합 설정 정보
     * @throws Exception
     */
    Map<String, Object> getGitHubSettings(Map<String, Object> param) throws Exception;
    
    /**
     * GitHub 통합 설정 업데이트
     * @param param 설정 정보 (user_id, project_id, settings)
     * @return 업데이트 결과
     * @throws Exception
     */
    Map<String, Object> updateGitHubSettings(Map<String, Object> param) throws Exception;
    
    /**
     * GitHub 연결 해제
     * @param param 해제 정보 (user_id, keep_history)
     * @return 해제 결과
     * @throws Exception
     */
    Map<String, Object> disconnectGitHub(Map<String, Object> param) throws Exception;
}