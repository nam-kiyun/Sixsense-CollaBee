package com.demo.proworks.githubapptoken.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.demo.proworks.githubapptoken.service.GithubAppTokenService;
import com.demo.proworks.githubapptoken.vo.GithubAppTokenVo;
import com.demo.proworks.githubapptoken.dao.GithubAppTokenDAO;

/**  
 * @subject     : 깃허브 앱 토큰 저장 관련 처리를 담당하는 ServiceImpl
 * @description	: 깃허브 앱 토큰 저장 관련 처리를 담당하는 ServiceImpl
 * @author      : 남기윤
 * @since       : 2025/07/01
 * @modification
 * ===========================================================
 * DATE				AUTHOR				DESC
 * ===========================================================
 * 2025/07/01			 남기윤	 		최초 생성
 * 
 */
@Service("githubAppTokenServiceImpl")
public class GithubAppTokenServiceImpl implements GithubAppTokenService {

    @Resource(name="githubAppTokenDAO")
    private GithubAppTokenDAO githubAppTokenDAO;
	
	@Resource(name = "messageSource")
	private MessageSource messageSource;

    /**
     * 깃허브 앱 토큰 저장 목록을 조회합니다.
     *
     * @process
     * 1. 깃허브 앱 토큰 저장 페이징 처리하여 목록을 조회한다.
     * 2. 결과 List<GithubAppTokenVo>을(를) 리턴한다.
     * 
     * @param  githubAppTokenVo 깃허브 앱 토큰 저장 GithubAppTokenVo
     * @return 깃허브 앱 토큰 저장 목록 List<GithubAppTokenVo>
     * @throws Exception
     */
	public List<GithubAppTokenVo> selectListGithubAppToken(GithubAppTokenVo githubAppTokenVo) throws Exception {
		List<GithubAppTokenVo> list = githubAppTokenDAO.selectListGithubAppToken(githubAppTokenVo);	
	
		return list;
	}

    /**
     * 조회한 깃허브 앱 토큰 저장 전체 카운트
     *
     * @process
     * 1. 깃허브 앱 토큰 저장 조회하여 전체 카운트를 리턴한다.
     * 
     * @param  githubAppTokenVo 깃허브 앱 토큰 저장 GithubAppTokenVo
     * @return 깃허브 앱 토큰 저장 목록 전체 카운트
     * @throws Exception
     */
	public long selectListCountGithubAppToken(GithubAppTokenVo githubAppTokenVo) throws Exception {
		return githubAppTokenDAO.selectListCountGithubAppToken(githubAppTokenVo);
	}

    /**
     * 깃허브 앱 토큰 저장를 상세 조회한다.
     *
     * @process
     * 1. 깃허브 앱 토큰 저장를 상세 조회한다.
     * 2. 결과 GithubAppTokenVo을(를) 리턴한다.
     * 
     * @param  githubAppTokenVo 깃허브 앱 토큰 저장 GithubAppTokenVo
     * @return 단건 조회 결과
     * @throws Exception
     */
	public GithubAppTokenVo selectGithubAppToken(GithubAppTokenVo githubAppTokenVo) throws Exception {
		GithubAppTokenVo resultVO = githubAppTokenDAO.selectGithubAppToken(githubAppTokenVo);			
        
        return resultVO;
	}

    /**
     * 깃허브 앱 토큰 저장를 등록 처리 한다.
     *
     * @process
     * 1. 깃허브 앱 토큰 저장를 등록 처리 한다.
     * 
     * @param  githubAppTokenVo 깃허브 앱 토큰 저장 GithubAppTokenVo
     * @return 번호
     * @throws Exception
     */
	public int insertGithubAppToken(GithubAppTokenVo githubAppTokenVo) throws Exception {
		return githubAppTokenDAO.insertGithubAppToken(githubAppTokenVo);	
	}
	
    /**
     * 깃허브 앱 토큰 저장를 갱신 처리 한다.
     *
     * @process
     * 1. 깃허브 앱 토큰 저장를 갱신 처리 한다.
     * 
     * @param  githubAppTokenVo 깃허브 앱 토큰 저장 GithubAppTokenVo
     * @return 번호
     * @throws Exception
     */
	public int updateGithubAppToken(GithubAppTokenVo githubAppTokenVo) throws Exception {				
		return githubAppTokenDAO.updateGithubAppToken(githubAppTokenVo);	   		
	}

    /**
     * 깃허브 앱 토큰 저장를 삭제 처리 한다.
     *
     * @process
     * 1. 깃허브 앱 토큰 저장를 삭제 처리 한다.
     * 
     * @param  githubAppTokenVo 깃허브 앱 토큰 저장 GithubAppTokenVo
     * @return 번호
     * @throws Exception
     */
	public int deleteGithubAppToken(GithubAppTokenVo githubAppTokenVo) throws Exception {
		return githubAppTokenDAO.deleteGithubAppToken(githubAppTokenVo);
	}
	
}
