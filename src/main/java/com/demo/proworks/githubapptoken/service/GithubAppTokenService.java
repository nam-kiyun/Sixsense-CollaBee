package com.demo.proworks.githubapptoken.service;

import java.util.List;

import com.demo.proworks.githubapptoken.vo.GithubAppTokenVo;

/**  
 * @subject     : 깃허브 앱 토큰 저장 관련 처리를 담당하는 인터페이스
 * @description : 깃허브 앱 토큰 저장 관련 처리를 담당하는 인터페이스
 * @author      : 남기윤
 * @since       : 2025/07/01
 * @modification
 * ===========================================================
 * DATE				AUTHOR				DESC
 * ===========================================================
 * 2025/07/01			 남기윤	 		최초 생성
 * 
 */
public interface GithubAppTokenService {
	
    /**
     * 깃허브 앱 토큰 저장 페이징 처리하여 목록을 조회한다.
     *
     * @param  githubAppTokenVo 깃허브 앱 토큰 저장 GithubAppTokenVo
     * @return 깃허브 앱 토큰 저장 목록 List<GithubAppTokenVo>
     * @throws Exception
     */
	public List<GithubAppTokenVo> selectListGithubAppToken(GithubAppTokenVo githubAppTokenVo) throws Exception;
	
    /**
     * 조회한 깃허브 앱 토큰 저장 전체 카운트
     * 
     * @param  githubAppTokenVo 깃허브 앱 토큰 저장 GithubAppTokenVo
     * @return 깃허브 앱 토큰 저장 목록 전체 카운트
     * @throws Exception
     */
	public long selectListCountGithubAppToken(GithubAppTokenVo githubAppTokenVo) throws Exception;
	
    /**
     * 깃허브 앱 토큰 저장를 상세 조회한다.
     *
     * @param  githubAppTokenVo 깃허브 앱 토큰 저장 GithubAppTokenVo
     * @return 단건 조회 결과
     * @throws Exception
     */
	public GithubAppTokenVo selectGithubAppToken(GithubAppTokenVo githubAppTokenVo) throws Exception;
		
    /**
     * 깃허브 앱 토큰 저장를 등록 처리 한다.
     *
     * @param  githubAppTokenVo 깃허브 앱 토큰 저장 GithubAppTokenVo
     * @return 번호
     * @throws Exception
     */
	public int insertGithubAppToken(GithubAppTokenVo githubAppTokenVo) throws Exception;
	
    /**
     * 깃허브 앱 토큰 저장를 갱신 처리 한다.
     *
     * @param  githubAppTokenVo 깃허브 앱 토큰 저장 GithubAppTokenVo
     * @return 번호
     * @throws Exception
     */
	public int updateGithubAppToken(GithubAppTokenVo githubAppTokenVo) throws Exception;
	
    /**
     * 깃허브 앱 토큰 저장를 삭제 처리 한다.
     *
     * @param  githubAppTokenVo 깃허브 앱 토큰 저장 GithubAppTokenVo
     * @return 번호
     * @throws Exception
     */
	public int deleteGithubAppToken(GithubAppTokenVo githubAppTokenVo) throws Exception;
	
	/**
     * GitHub App Installation Token을 생성한다.
     *
     * @param userAccessToken 사용자 액세스 토큰
     * @param repoOwner 레포지토리 소유자
     * @return Installation Token
     * @throws Exception
     */
	public String generateInstallationToken(String userAccessToken, String repoOwner) throws Exception;
	
	/**
     * 프로젝트 레포지토리 ID로 유효한 App Token을 조회한다.
     *
     * @param projectRepoId 프로젝트 레포지토리 ID
     * @return App Token 또는 null
     * @throws Exception
     */
	public String getValidAppToken(String projectRepoId) throws Exception;
	
	/**
     * App Token을 갱신하거나 새로 생성한다.
     *
     * @param projectRepoId 프로젝트 레포지토리 ID
     * @param userAccessToken 사용자 액세스 토큰
     * @param repoOwner 레포지토리 소유자
     * @return 갱신된 App Token
     * @throws Exception
     */
	public String refreshAppToken(String projectRepoId, String userAccessToken, String repoOwner) throws Exception;
	
}
