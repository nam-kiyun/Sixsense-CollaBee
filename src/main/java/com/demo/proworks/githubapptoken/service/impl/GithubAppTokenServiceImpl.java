package com.demo.proworks.githubapptoken.service.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.demo.proworks.githubapptoken.dao.GithubAppTokenDAO;
import com.demo.proworks.github.dao.GitHubDAO;
import com.demo.proworks.githubapptoken.service.GithubAppTokenService;
import com.demo.proworks.githubapptoken.util.GitHubApiClient;
import com.demo.proworks.githubapptoken.util.GitHubAppAuthUtil;
import com.demo.proworks.githubapptoken.vo.GithubAppTokenVo;
import com.inswave.elfw.exception.ElException;

/**  
 * @subject     : 깃허브 앱 토큰 저장 관련 처리를 담당하는 서비스 구현체
 * @description : 깃허브 앱 토큰 저장 관련 처리를 담당하는 서비스 구현체
 * @author      : 남기윤
 * @since       : 2025/07/01
 * @modification
 * ===========================================================
 * DATE				AUTHOR				DESC
 * ===========================================================
 * 2025/07/01			 남기윤	 		최초 생성
 * 2025/07/07			 남기윤	 		GitHub App 기능 추가
 * 
 */
@Service("githubAppTokenService")
public class GithubAppTokenServiceImpl implements GithubAppTokenService {

    @Autowired    
    private GithubAppTokenDAO githubAppTokenDAO;
    
    @Autowired
    private GitHubDAO gitHubDAO;
    
    @Autowired
    private GitHubAppAuthUtil gitHubAppAuthUtil;
    
    @Autowired
    private GitHubApiClient gitHubApiClient;
    
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * 깃허브 앱 토큰 저장 페이징 처리하여 목록을 조회한다.
     */
    @Override
    public List<GithubAppTokenVo> selectListGithubAppToken(GithubAppTokenVo githubAppTokenVo) throws Exception {
        List<GithubAppTokenVo> githubAppTokenVoList = null;
        try {            
            githubAppTokenVoList = githubAppTokenDAO.selectListGithubAppToken(githubAppTokenVo);
        } catch (ElException e) {
            throw new Exception(e.getMessage());
        }                
        return githubAppTokenVoList;
    }

    /**
     * 조회한 깃허브 앱 토큰 저장 전체 카운트
     */
    @Override
    public long selectListCountGithubAppToken(GithubAppTokenVo githubAppTokenVo) throws Exception {        
        long totalCount = 0;
        try {            
            totalCount = githubAppTokenDAO.selectListCountGithubAppToken(githubAppTokenVo);
        } catch (ElException e) {
            throw new Exception(e.getMessage());
        }                
        return totalCount;
    }

    /**
     * 깃허브 앱 토큰 저장를 상세 조회한다.
     */
    @Override
    public GithubAppTokenVo selectGithubAppToken(GithubAppTokenVo githubAppTokenVo) throws Exception {        
        GithubAppTokenVo githubAppTokenVoResult = null;        
        try {            
            githubAppTokenVoResult = githubAppTokenDAO.selectGithubAppToken(githubAppTokenVo);
        } catch (ElException e) {
            throw new Exception(e.getMessage());
        }                
        return githubAppTokenVoResult;        
    }

    /**
     * 깃허브 앱 토큰 저장를 등록 처리 한다.
     */
    @Override
    @Transactional
    public int insertGithubAppToken(GithubAppTokenVo githubAppTokenVo) throws Exception {        
        int result = 0;        
        try {
            if (githubAppTokenVo.getCreatedAt() == null || githubAppTokenVo.getCreatedAt().isEmpty()) {
                githubAppTokenVo.setCreatedAt(LocalDateTime.now().format(DATE_FORMATTER));
            }
            result = githubAppTokenDAO.insertGithubAppToken(githubAppTokenVo);
        } catch (ElException e) {
            throw new Exception(e.getMessage());
        }                
        return result;
    }

    /**
     * 깃허브 앱 토큰 저장를 갱신 처리 한다.
     */
    @Override
    @Transactional
    public int updateGithubAppToken(GithubAppTokenVo githubAppTokenVo) throws Exception {        
        int result = 0;        
        try {            
            result = githubAppTokenDAO.updateGithubAppToken(githubAppTokenVo);
        } catch (ElException e) {
            throw new Exception(e.getMessage());
        }                
        return result;
    }

    /**
     * 깃허브 앱 토큰 저장를 삭제 처리 한다.
     */
    @Override
    @Transactional
    public int deleteGithubAppToken(GithubAppTokenVo githubAppTokenVo) throws Exception {        
        int result = 0;        
        try {            
            result = githubAppTokenDAO.deleteGithubAppToken(githubAppTokenVo);
        } catch (ElException e) {
            throw new Exception(e.getMessage());
        }                
        return result;
    }

    /**
     * GitHub App Installation Token을 생성한다.
     */
    @Override
    public String generateInstallationToken(String userAccessToken, String repoOwner) throws Exception {
        try {
            return gitHubApiClient.getRepoInstallationToken(userAccessToken, repoOwner);
        } catch (Exception e) {
            throw new Exception("Installation 토큰 생성 실패: " + e.getMessage());
        }
    }

    /**
     * 사용자 ID로 Installation ID를 조회한다.
     */
    @Override
    public String getValidAppToken(String userId) throws Exception {
        try {
            GithubAppTokenVo token = gitHubDAO.selectGitHubAppTokenByUserId(userId);
            
            if (token != null && token.getGithubAppInstallationId() != null && !token.getGithubAppInstallationId().isEmpty()) {
                return token.getGithubAppInstallationId(); // Installation ID 반환
            }
            
            return null; // Installation ID가 없음
        } catch (Exception e) {
            throw new Exception("Installation ID 조회 실패: " + e.getMessage());
        }
    }

    /**
     * Installation ID를 업데이트한다.
     */
    @Override
    @Transactional
    public String refreshAppToken(String userId, String userAccessToken, String repoOwner) throws Exception {
        try {
            // 기존 Installation ID 조회
            GithubAppTokenVo existingToken = gitHubDAO.selectGitHubAppTokenByUserId(userId);
            
            if (existingToken != null) {
                // 기존 항목이 있으면 업데이트 (Installation ID는 변경되지 않으므로 실제로는 갱신할 필요 없음)
                return existingToken.getGithubAppInstallationId();
            } else {
                // Installation ID가 없으면 null 반환 (GitHub App 설치 필요)
                return null;
            }
        } catch (Exception e) {
            throw new Exception("Installation ID 갱신 실패: " + e.getMessage());
        }
    }
    
    /**
     * 사용자 ID로 GitHub App Token을 조회한다.
     */
    @Override
    public GithubAppTokenVo selectGithubAppTokenByUserId(String userId) throws Exception {
        try {
            return gitHubDAO.selectGitHubAppTokenByUserId(userId);
        } catch (Exception e) {
            throw new Exception("사용자 ID로 App Token 조회 실패: " + e.getMessage());
        }
    }
    
}
