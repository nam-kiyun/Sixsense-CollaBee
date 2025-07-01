package com.demo.proworks.collabee.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.demo.proworks.collabee.service.GithubWebhookService;
import com.demo.proworks.collabee.vo.GithubWebhookVo;
import com.demo.proworks.collabee.dao.GithubWebhookDAO;

/**  
 * @subject     : 깃허브 웹훅 관련 처리를 담당하는 ServiceImpl
 * @description	: 깃허브 웹훅 관련 처리를 담당하는 ServiceImpl
 * @author      : 남기윤
 * @since       : 2025/07/01
 * @modification
 * ===========================================================
 * DATE				AUTHOR				DESC
 * ===========================================================
 * 2025/07/01			 남기윤	 		최초 생성
 * 
 */
@Service("githubWebhookServiceImpl")
public class GithubWebhookServiceImpl implements GithubWebhookService {

    @Resource(name="githubWebhookDAO")
    private GithubWebhookDAO githubWebhookDAO;
	
	@Resource(name = "messageSource")
	private MessageSource messageSource;

    /**
     * 깃허브 웹훅 목록을 조회합니다.
     *
     * @process
     * 1. 깃허브 웹훅 페이징 처리하여 목록을 조회한다.
     * 2. 결과 List<GithubWebhookVo>을(를) 리턴한다.
     * 
     * @param  githubWebhookVo 깃허브 웹훅 GithubWebhookVo
     * @return 깃허브 웹훅 목록 List<GithubWebhookVo>
     * @throws Exception
     */
	public List<GithubWebhookVo> selectListGithubWebhook(GithubWebhookVo githubWebhookVo) throws Exception {
		List<GithubWebhookVo> list = githubWebhookDAO.selectListGithubWebhook(githubWebhookVo);	
	
		return list;
	}

    /**
     * 조회한 깃허브 웹훅 전체 카운트
     *
     * @process
     * 1. 깃허브 웹훅 조회하여 전체 카운트를 리턴한다.
     * 
     * @param  githubWebhookVo 깃허브 웹훅 GithubWebhookVo
     * @return 깃허브 웹훅 목록 전체 카운트
     * @throws Exception
     */
	public long selectListCountGithubWebhook(GithubWebhookVo githubWebhookVo) throws Exception {
		return githubWebhookDAO.selectListCountGithubWebhook(githubWebhookVo);
	}

    /**
     * 깃허브 웹훅를 상세 조회한다.
     *
     * @process
     * 1. 깃허브 웹훅를 상세 조회한다.
     * 2. 결과 GithubWebhookVo을(를) 리턴한다.
     * 
     * @param  githubWebhookVo 깃허브 웹훅 GithubWebhookVo
     * @return 단건 조회 결과
     * @throws Exception
     */
	public GithubWebhookVo selectGithubWebhook(GithubWebhookVo githubWebhookVo) throws Exception {
		GithubWebhookVo resultVO = githubWebhookDAO.selectGithubWebhook(githubWebhookVo);			
        
        return resultVO;
	}

    /**
     * 깃허브 웹훅를 등록 처리 한다.
     *
     * @process
     * 1. 깃허브 웹훅를 등록 처리 한다.
     * 
     * @param  githubWebhookVo 깃허브 웹훅 GithubWebhookVo
     * @return 번호
     * @throws Exception
     */
	public int insertGithubWebhook(GithubWebhookVo githubWebhookVo) throws Exception {
		return githubWebhookDAO.insertGithubWebhook(githubWebhookVo);	
	}
	
    /**
     * 깃허브 웹훅를 갱신 처리 한다.
     *
     * @process
     * 1. 깃허브 웹훅를 갱신 처리 한다.
     * 
     * @param  githubWebhookVo 깃허브 웹훅 GithubWebhookVo
     * @return 번호
     * @throws Exception
     */
	public int updateGithubWebhook(GithubWebhookVo githubWebhookVo) throws Exception {				
		return githubWebhookDAO.updateGithubWebhook(githubWebhookVo);	   		
	}

    /**
     * 깃허브 웹훅를 삭제 처리 한다.
     *
     * @process
     * 1. 깃허브 웹훅를 삭제 처리 한다.
     * 
     * @param  githubWebhookVo 깃허브 웹훅 GithubWebhookVo
     * @return 번호
     * @throws Exception
     */
	public int deleteGithubWebhook(GithubWebhookVo githubWebhookVo) throws Exception {
		return githubWebhookDAO.deleteGithubWebhook(githubWebhookVo);
	}
	
}
