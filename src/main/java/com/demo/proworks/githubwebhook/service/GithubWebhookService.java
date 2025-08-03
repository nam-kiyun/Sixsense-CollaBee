package com.demo.proworks.githubwebhook.service;

import java.util.List;
import java.util.Map;

import com.demo.proworks.githubwebhook.vo.GithubWebhookVo;

/**  
 * @subject     : 깃허브 웹훅 관련 처리를 담당하는 인터페이스
 * @description : 깃허브 웹훅 관련 처리를 담당하는 인터페이스
 * @author      : 남기윤
 * @since       : 2025/07/01
 * @modification
 * ===========================================================
 * DATE				AUTHOR				DESC
 * ===========================================================
 * 2025/07/01			 남기윤	 		최초 생성
 * 
 */
public interface GithubWebhookService {
	
    /**
     * 깃허브 웹훅 페이징 처리하여 목록을 조회한다.
     *
     * @param  githubWebhookVo 깃허브 웹훅 GithubWebhookVo
     * @return 깃허브 웹훅 목록 List<GithubWebhookVo>
     * @throws Exception
     */
	public List<GithubWebhookVo> selectListGithubWebhook(GithubWebhookVo githubWebhookVo) throws Exception;
	
    /**
     * 조회한 깃허브 웹훅 전체 카운트
     * 
     * @param  githubWebhookVo 깃허브 웹훅 GithubWebhookVo
     * @return 깃허브 웹훅 목록 전체 카운트
     * @throws Exception
     */
	public long selectListCountGithubWebhook(GithubWebhookVo githubWebhookVo) throws Exception;
	
    /**
     * 깃허브 웹훅를 상세 조회한다.
     *
     * @param  githubWebhookVo 깃허브 웹훅 GithubWebhookVo
     * @return 단건 조회 결과
     * @throws Exception
     */
	public GithubWebhookVo selectGithubWebhook(GithubWebhookVo githubWebhookVo) throws Exception;
		
    /**
     * 깃허브 웹훅를 등록 처리 한다.
     *
     * @param  githubWebhookVo 깃허브 웹훅 GithubWebhookVo
     * @return 번호
     * @throws Exception
     */
	public int insertGithubWebhook(GithubWebhookVo githubWebhookVo) throws Exception;
	
    /**
     * 깃허브 웹훅를 갱신 처리 한다.
     *
     * @param  githubWebhookVo 깃허브 웹훅 GithubWebhookVo
     * @return 번호
     * @throws Exception
     */
	public int updateGithubWebhook(GithubWebhookVo githubWebhookVo) throws Exception;
	
    /**
     * 깃허브 웹훅를 삭제 처리 한다.
     *
     * @param  githubWebhookVo 깃허브 웹훅 GithubWebhookVo
     * @return 번호
     * @throws Exception
     */
	public int deleteGithubWebhook(GithubWebhookVo githubWebhookVo) throws Exception;
	
	/**
     * GitHub API를 통해 웹훅을 생성한다.
     *
     * @param projectRepoId 프로젝트 레포지토리 ID
     * @param repoFullName 레포지토리 전체 이름 (owner/repo)
     * @param webhookUrl 웹훅 URL
     * @param userAccessToken 사용자 액세스 토큰
     * @return 생성된 웹훅 정보
     * @throws Exception
     */
	public GithubWebhookVo createWebhookViaAPI(String projectRepoId, String repoFullName, String webhookUrl, String userAccessToken) throws Exception;
	
	/**
     * 프로젝트 레포지토리 ID로 웹훅을 조회한다.
     *
     * @param projectRepoId 프로젝트 레포지토리 ID
     * @return 웹훅 정보
     * @throws Exception
     */
	public GithubWebhookVo getWebhookByProjectRepoId(String projectRepoId) throws Exception;
	
	/**
     * GitHub API를 통해 웹훅을 삭제한다.
     *
     * @param projectRepoId 프로젝트 레포지토리 ID
     * @param userAccessToken 사용자 액세스 토큰
     * @return 삭제 성공 여부
     * @throws Exception
     */
	public boolean deleteWebhookViaAPI(String projectRepoId, String userAccessToken) throws Exception;
	
	/**
     * 웹훅 이벤트를 처리한다.
     *
     * @param eventType 이벤트 타입
     * @param payload 이벤트 페이로드
     * @param signature 서명
     * @return 처리 결과
     * @throws Exception
     */
	public String processWebhookEvent(String eventType, String payload, String signature) throws Exception;
	
	/**
     * 웹훅 상태를 확인한다.
     *
     * @param projectRepoId 프로젝트 레포지토리 ID
     * @param userAccessToken 사용자 액세스 토큰
     * @return 웹훅 상태 정보
     * @throws Exception
     */
	public Map<String, Object> getWebhookStatus(String projectRepoId, String userAccessToken) throws Exception;
	
}
