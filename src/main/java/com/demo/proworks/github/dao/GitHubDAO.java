package com.demo.proworks.github.dao;

import java.util.List;
import java.util.Map;

import com.demo.proworks.projectrepo.vo.ProjectRepositoryVo;
import com.demo.proworks.userpersonaltoken.vo.UserPersonalTokenVo;
import com.demo.proworks.repobranch.vo.RepositoryBranchVo;
import com.demo.proworks.githubwebhook.vo.GithubWebhookVo;
import com.demo.proworks.githubapptoken.vo.GithubAppTokenVo;

/**
 * GitHub 통합 DAO 인터페이스
 * test 디렉터리의 데이터베이스 기능을 기반으로 작성
 */
public interface GitHubDAO {

    // ==============================
    // GitHub 사용자 관리
    // ==============================
    
    /**
     * GitHub 사용자 Personal Token 저장/업데이트
     * @param param GitHub 사용자 정보 (Map)
     * @return 등록/업데이트된 토큰 ID
     * @throws Exception
     */
    String upsertGitHubUser(Map<String, Object> param) throws Exception;
    
    /**
     * 로컬 사용자 ID로 GitHub Personal Token 조회
     * @param userId 로컬 사용자 ID
     * @return GitHub Personal Token 정보
     * @throws Exception
     */
    UserPersonalTokenVo selectGitHubUserByLocalId(String userId) throws Exception;

    // ==============================
    // GitHub 레포지토리 관리
    // ==============================
    
    /**
     * 사용자의 선택된 레포지토리 정보 조회
     * @param userId 로컬 사용자 ID
     * @return 선택된 레포지토리 정보
     * @throws Exception
     */
    ProjectRepositoryVo selectUserSelectedRepository(String userId) throws Exception;
    
    /**
     * 사용자의 레포지토리 선택 정보 저장
     * @param param 레포지토리 선택 정보 (user_id, repo_data)
     * @return 저장된 레코드 ID
     * @throws Exception
     */
    String insertUserSelectedRepository(Map<String, Object> param) throws Exception;
    
    /**
     * 사용자의 레포지토리 선택 정보 업데이트
     * @param param 레포지토리 업데이트 정보
     * @throws Exception
     */
    void updateUserSelectedRepository(Map<String, Object> param) throws Exception;
    
    /**
     * GitHub 레포지토리 ID로 연결된 프로젝트 레포지토리 조회
     * @param githubRepoId GitHub 레포지토리 ID
     * @return 프로젝트 레포지토리 정보
     * @throws Exception
     */
    Map<String, Object> selectProjectRepositoryByGitHubId(String githubRepoId) throws Exception;

    // ==============================
    // GitHub 브랜치 관리
    // ==============================
    
    
    /**
     * GitHub 브랜치 정보 저장
     * @param repositoryBranchVo GitHub 브랜치 정보
     * @return 저장된 브랜치 ID
     * @throws Exception
     */
    String insertGitHubBranch(RepositoryBranchVo repositoryBranchVo) throws Exception;
    
    /**
     * GitHub 브랜치 정보 업데이트
     * @param repositoryBranchVo GitHub 브랜치 정보
     * @throws Exception
     */
    void updateGitHubBranch(RepositoryBranchVo repositoryBranchVo) throws Exception;
    
    /**
     * 레포지토리의 브랜치 목록 조회
     * @param param 조회 조건 (project_repo_id)
     * @return 브랜치 목록
     * @throws Exception
     */
    List<RepositoryBranchVo> selectGitHubBranches(Map<String, Object> param) throws Exception;

    // ==============================
    // GitHub 웹훅 관리
    // ==============================
    
    /**
     * GitHub 웹훅 생성
     * @param githubWebhookVo 웹훅 정보
     * @return 저장된 웹훅 ID
     * @throws Exception
     */
    String insertGitHubWebhook(GithubWebhookVo githubWebhookVo) throws Exception;
    
    /**
     * GitHub 웹훅 삭제
     * @param githubWebhookId 웹훅 ID
     * @throws Exception
     */
    void deleteGitHubWebhook(String githubWebhookId) throws Exception;
    
    
    /**
     * 웹훅 ID로 연결된 레포지토리 정보 조회
     * @param githubRepoId GitHub 레포지토리 ID
     * @return 웹훅 정보
     * @throws Exception
     */
    Map<String, Object> selectWebhookByRepoId(String githubRepoId) throws Exception;


    // ==============================
    // 통계 및 모니터링
    // ==============================
    
    /**
     * 프로젝트별 GitHub 활동 통계 조회
     * @param param 조회 조건 (project_id, date_from, date_to)
     * @return 활동 통계 정보
     * @throws Exception
     */
    Map<String, Object> selectGitHubActivityStats(Map<String, Object> param) throws Exception;
    
    /**
     * 사용자별 GitHub 활동 통계 조회
     * @param param 조회 조건 (user_id, date_from, date_to)
     * @return 사용자 활동 통계
     * @throws Exception
     */
    Map<String, Object> selectUserGitHubActivityStats(Map<String, Object> param) throws Exception;
    
    /**
     * GitHub 연결 상태 조회
     * @param projectRepoId 프로젝트 레포지토리 ID
     * @return 연결 상태 정보
     * @throws Exception
     */
    Map<String, Object> selectGitHubConnectionStatus(String projectRepoId) throws Exception;

    // ==============================
    // 헬스 체크 및 테스트
    // ==============================
    
    /**
     * 데이터베이스 연결 테스트
     * @return 연결 상태 (true: 성공, false: 실패)
     * @throws Exception
     */
    boolean testConnection() throws Exception;
    
    /**
     * GitHub 통합 서비스 상태 조회
     * @return 서비스 상태 정보
     * @throws Exception
     */
    Map<String, Object> selectServiceStatus() throws Exception;
}