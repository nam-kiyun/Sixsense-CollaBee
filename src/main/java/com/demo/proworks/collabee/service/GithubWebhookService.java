package com.demo.proworks.collabee.service;

import java.util.List;

import com.demo.proworks.collabee.vo.GithubWebhookVo;

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
	
}
