package com.demo.proworks.github.dao.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.demo.proworks.github.dao.GitHubDAO;
import com.demo.proworks.projectrepo.vo.ProjectRepositoryVo;
import com.demo.proworks.userpersonaltoken.vo.UserPersonalTokenVo;
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
    public String upsertGitHubUser(Map<String, Object> param) throws Exception {
        sqlSession.insert("GitHubDAO.upsertGitHubUser", param);
        return param.get("user_personal_token_id") != null ? param.get("user_personal_token_id").toString() : null;
    }
    
    @Override
    public UserPersonalTokenVo selectGitHubUserByLocalId(String userId) throws Exception {
        return sqlSession.selectOne("GitHubDAO.selectGitHubUserByLocalId", userId);
    }

    // ==============================
    // GitHub 레포지토리 관리
    // ==============================
    
    @Override
    public ProjectRepositoryVo selectUserSelectedRepository(String userId) throws Exception {
        return sqlSession.selectOne("GitHubDAO.selectUserSelectedRepository", userId);
    }
    
    @Override
    public String insertUserSelectedRepository(Map<String, Object> param) throws Exception {
        sqlSession.insert("GitHubDAO.insertUserSelectedRepository", param);
        return param.get("id") != null ? param.get("id").toString() : null;
    }
    
    @Override
    public void updateUserSelectedRepository(Map<String, Object> param) throws Exception {
        sqlSession.update("GitHubDAO.updateUserSelectedRepository", param);
    }
    
    @Override
    public Map<String, Object> selectProjectRepositoryByGitHubId(String githubRepoId) throws Exception {
        return sqlSession.selectOne("GitHubDAO.selectProjectRepositoryByGitHubId", githubRepoId);
    }

    // ==============================
    // GitHub 브랜치 관리
    // ==============================
    
    
    @Override
    public String insertGitHubBranch(RepositoryBranchVo repositoryBranchVo) throws Exception {
        sqlSession.insert("GitHubDAO.insertGitHubBranch", repositoryBranchVo);
        return repositoryBranchVo.getRepoBranchId();
    }
    
    @Override
    public void updateGitHubBranch(RepositoryBranchVo repositoryBranchVo) throws Exception {
        sqlSession.update("GitHubDAO.updateGitHubBranch", repositoryBranchVo);
    }
    
    @Override
    public List<RepositoryBranchVo> selectGitHubBranches(Map<String, Object> param) throws Exception {
        return sqlSession.selectList("GitHubDAO.selectGitHubBranches", param);
    }

    // ==============================
    // GitHub 웹훅 관리
    // ==============================
    
    @Override
    public String insertGitHubWebhook(GithubWebhookVo githubWebhookVo) throws Exception {
        sqlSession.insert("GitHubDAO.insertGitHubWebhook", githubWebhookVo);
        return githubWebhookVo.getGithubWebhookId();
    }
    
    @Override
    public void deleteGitHubWebhook(String githubWebhookId) throws Exception {
        sqlSession.delete("GitHubDAO.deleteGitHubWebhook", githubWebhookId);
    }
    
    
    @Override
    public Map<String, Object> selectWebhookByRepoId(String githubRepoId) throws Exception {
        return sqlSession.selectOne("GitHubDAO.selectWebhookByRepoId", githubRepoId);
    }


    // ==============================
    // 통계 및 모니터링
    // ==============================
    
    @Override
    public Map<String, Object> selectGitHubActivityStats(Map<String, Object> param) throws Exception {
        return sqlSession.selectOne("GitHubDAO.selectGitHubActivityStats", param);
    }
    
    @Override
    public Map<String, Object> selectUserGitHubActivityStats(Map<String, Object> param) throws Exception {
        return sqlSession.selectOne("GitHubDAO.selectUserGitHubActivityStats", param);
    }
    
    @Override
    public Map<String, Object> selectGitHubConnectionStatus(String projectRepoId) throws Exception {
        return sqlSession.selectOne("GitHubDAO.selectGitHubConnectionStatus", projectRepoId);
    }

    // ==============================
    // 헬스 체크 및 테스트
    // ==============================
    
    @Override
    public boolean testConnection() throws Exception {
        Integer result = sqlSession.selectOne("GitHubDAO.testConnection");
        return result != null && result == 1;
    }
    
    @Override
    public Map<String, Object> selectServiceStatus() throws Exception {
        return sqlSession.selectOne("GitHubDAO.selectServiceStatus");
    }
}