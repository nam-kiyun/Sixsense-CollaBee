package com.demo.proworks.github.dao.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.demo.proworks.github.dao.GitHubDAO;
import com.demo.proworks.projectrepo.vo.ProjectRepositoryVo;
import com.demo.proworks.githubapptoken.vo.GithubAppTokenVo;
import com.demo.proworks.repobranch.vo.RepositoryBranchVo;
import com.demo.proworks.githubwebhook.vo.GithubWebhookVo;
import com.demo.proworks.githubapptoken.vo.GithubAppTokenVo;

/**
 * GitHub 통합 DAO 구현체
 */
@Repository("gitHubDAO")
public class GitHubDAOImpl implements GitHubDAO {

    @Resource(name = "sqlSession")
    private SqlSession sqlSession;

    // ==============================
    // GitHub 사용자 관리
    // ==============================
    
    @Override
    public String insertGitHubAppToken(Map<String, Object> param) throws Exception {
        sqlSession.insert("com.demo.proworks.github.insertGitHubAppToken", param);
        return param.get("github_app_token_id") != null ? param.get("github_app_token_id").toString() : null;
    }
    
    @Override
    public void updateGitHubAppToken(Map<String, Object> param) throws Exception {
        sqlSession.update("com.demo.proworks.github.updateGitHubAppToken", param);
    }
    
    @Override
    public GithubAppTokenVo selectGitHubAppTokenByUserId(String userId) throws Exception {
        return sqlSession.selectOne("com.demo.proworks.github.selectGitHubAppTokenByUserId", userId);
    }

    // ==============================
    // GitHub 레포지토리 관리
    // ==============================
    
    @Override
    public ProjectRepositoryVo selectProjectRepositoryByUserId(String userId) throws Exception {
        return sqlSession.selectOne("com.demo.proworks.github.selectProjectRepositoryByUserId", userId);
    }
    
    @Override
    public String insertProjectRepository(Map<String, Object> param) throws Exception {
        sqlSession.insert("com.demo.proworks.github.insertProjectRepository", param);
        return param.get("project_repo_id") != null ? param.get("project_repo_id").toString() : null;
    }
    
    @Override
    public void updateProjectRepository(Map<String, Object> param) throws Exception {
        sqlSession.update("com.demo.proworks.github.updateProjectRepository", param);
    }
    
    @Override
    public Map<String, Object> selectProjectRepositoryByGitHubId(String githubRepoId) throws Exception {
        return sqlSession.selectOne("com.demo.proworks.github.selectProjectRepositoryByGitHubId", githubRepoId);
    }

    // ==============================
    // GitHub 브랜치 관리
    // ==============================
    
    
    @Override
    public String insertRepositoryBranch(RepositoryBranchVo repositoryBranchVo) throws Exception {
        sqlSession.insert("com.demo.proworks.github.insertRepositoryBranch", repositoryBranchVo);
        return repositoryBranchVo.getRepoBranchId();
    }
    
    @Override
    public void updateRepositoryBranch(RepositoryBranchVo repositoryBranchVo) throws Exception {
        sqlSession.update("com.demo.proworks.github.updateRepositoryBranch", repositoryBranchVo);
    }
    
    @Override
    public List<RepositoryBranchVo> selectRepositoryBranches(Map<String, Object> param) throws Exception {
        return sqlSession.selectList("com.demo.proworks.github.selectRepositoryBranches", param);
    }

    // ==============================
    // GitHub 웹훅 관리
    // ==============================
    
    @Override
    public String insertRepositoryWebhook(GithubWebhookVo githubWebhookVo) throws Exception {
        sqlSession.insert("com.demo.proworks.github.insertRepositoryWebhook", githubWebhookVo);
        return githubWebhookVo.getGithubWebhookId();
    }
    
    @Override
    public void deleteRepositoryWebhook(String githubWebhookId) throws Exception {
        sqlSession.delete("com.demo.proworks.github.deleteRepositoryWebhook", githubWebhookId);
    }
    
    
    @Override
    public Map<String, Object> selectRepositoryWebhookByRepoId(String githubRepoId) throws Exception {
        return sqlSession.selectOne("com.demo.proworks.github.selectRepositoryWebhookByRepoId", githubRepoId);
    }


    // ==============================
    // 통계 및 모니터링
    // ==============================
    
    @Override
    public Map<String, Object> selectProjectGitHubActivityStats(Map<String, Object> param) throws Exception {
        return sqlSession.selectOne("com.demo.proworks.github.selectProjectGitHubActivityStats", param);
    }
    
    @Override
    public Map<String, Object> selectUserGitHubActivityStats(Map<String, Object> param) throws Exception {
        return sqlSession.selectOne("com.demo.proworks.github.selectUserGitHubActivityStats", param);
    }
    
    @Override
    public Map<String, Object> selectRepositoryConnectionStatus(String projectRepoId) throws Exception {
        return sqlSession.selectOne("com.demo.proworks.github.selectRepositoryConnectionStatus", projectRepoId);
    }

    // ==============================
    // 헬스 체크 및 테스트
    // ==============================
    
    @Override
    public boolean testDatabaseConnection() throws Exception {
        Integer result = sqlSession.selectOne("com.demo.proworks.github.testDatabaseConnection");
        return result != null && result == 1;
    }
    
    @Override
    public Map<String, Object> selectGitHubServiceStatus() throws Exception {
        return sqlSession.selectOne("com.demo.proworks.github.selectGitHubServiceStatus");
    }
}