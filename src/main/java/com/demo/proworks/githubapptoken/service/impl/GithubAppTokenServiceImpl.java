package com.demo.proworks.githubapptoken.service.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.demo.proworks.githubapptoken.dao.GithubAppTokenDAO;
import com.demo.proworks.githubapptoken.service.GithubAppTokenService;
import com.demo.proworks.githubapptoken.util.GitHubAppAuthUtil;
import com.demo.proworks.githubapptoken.util.GitHubApiClient;
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
            System.err.println("❌ Error generating installation token: " + e.getMessage());
            throw new Exception("Installation 토큰 생성 실패: " + e.getMessage());
        }
    }

    /**
     * 프로젝트 레포지토리 ID로 유효한 App Token을 조회한다.
     */
    @Override
    public String getValidAppToken(String projectRepoId) throws Exception {
        try {
            GithubAppTokenVo searchVo = new GithubAppTokenVo();
            searchVo.setProjectRepoId(projectRepoId);
            
            List<GithubAppTokenVo> tokens = githubAppTokenDAO.selectListGithubAppToken(searchVo);
            
            if (tokens != null && !tokens.isEmpty()) {
                for (GithubAppTokenVo token : tokens) {
                    // 만료 시간 확인
                    if (token.getExpiredAt() != null && !token.getExpiredAt().isEmpty()) {
                        LocalDateTime expiredAt = LocalDateTime.parse(token.getExpiredAt(), DATE_FORMATTER);
                        if (expiredAt.isAfter(LocalDateTime.now().plusMinutes(5))) { // 5분 여유를 둠
                            return token.getAppToken();
                        }
                    }
                }
            }
            
            return null; // 유효한 토큰이 없음
        } catch (Exception e) {
            System.err.println("❌ Error getting valid app token: " + e.getMessage());
            throw new Exception("App Token 조회 실패: " + e.getMessage());
        }
    }

    /**
     * App Token을 갱신하거나 새로 생성한다.
     */
    @Override
    @Transactional
    public String refreshAppToken(String projectRepoId, String userAccessToken, String repoOwner) throws Exception {
        try {
            // 새로운 Installation Token 생성
            String newToken = generateInstallationToken(userAccessToken, repoOwner);
            
            // 기존 토큰 삭제
            GithubAppTokenVo deleteVo = new GithubAppTokenVo();
            deleteVo.setProjectRepoId(projectRepoId);
            List<GithubAppTokenVo> existingTokens = githubAppTokenDAO.selectListGithubAppToken(deleteVo);
            for (GithubAppTokenVo existingToken : existingTokens) {
                githubAppTokenDAO.deleteGithubAppToken(existingToken);
            }
            
            // 새 토큰 저장
            GithubAppTokenVo newTokenVo = new GithubAppTokenVo();
            newTokenVo.setGithubAppTokenId(generateTokenId());
            newTokenVo.setProjectRepoId(projectRepoId);
            newTokenVo.setAppToken(newToken);
            
            // Installation Token은 1시간 유효
            LocalDateTime expiredAt = LocalDateTime.now().plusHours(1);
            newTokenVo.setExpiredAt(expiredAt.format(DATE_FORMATTER));
            newTokenVo.setCreatedAt(LocalDateTime.now().format(DATE_FORMATTER));
            
            githubAppTokenDAO.insertGithubAppToken(newTokenVo);
            
            return newToken;
        } catch (Exception e) {
            System.err.println("❌ Error refreshing app token: " + e.getMessage());
            throw new Exception("App Token 갱신 실패: " + e.getMessage());
        }
    }
    
    /**
     * 고유한 토큰 ID를 생성한다.
     */
    private String generateTokenId() {
        return "GAT_" + System.currentTimeMillis();
    }
}
