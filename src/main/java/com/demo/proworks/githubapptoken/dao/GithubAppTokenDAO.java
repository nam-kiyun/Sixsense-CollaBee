package com.demo.proworks.githubapptoken.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.inswave.elfw.exception.ElException;
import com.demo.proworks.githubapptoken.vo.GithubAppTokenVo;
import com.demo.proworks.githubapptoken.dao.GithubAppTokenDAO;

/**  
 * @subject     : 깃허브 앱 토큰 저장 관련 처리를 담당하는 DAO
 * @description : 깃허브 앱 토큰 저장 관련 처리를 담당하는 DAO
 * @author      : 남기윤
 * @since       : 2025/07/01
 * @modification
 * ===========================================================
 * DATE				AUTHOR				DESC
 * ===========================================================
 * 2025/07/01			 남기윤	 		최초 생성
 * 
 */
@Repository("githubAppTokenDAO")
public class GithubAppTokenDAO extends com.demo.proworks.cmmn.dao.ProworksDefaultAbstractDAO {

    /**
     * 깃허브 앱 토큰 저장 상세 조회한다.
     *  
     * @param  GithubAppTokenVo 깃허브 앱 토큰 저장
     * @return GithubAppTokenVo 깃허브 앱 토큰 저장
     * @throws ElException
     */
    public GithubAppTokenVo selectGithubAppToken(GithubAppTokenVo vo) throws ElException {
        return (GithubAppTokenVo) selectByPk("com.demo.proworks.githubapptoken.selectGithubAppToken", vo);
    }

    /**
     * 페이징을 처리하여 깃허브 앱 토큰 저장 목록조회를 한다.
     *  
     * @param  GithubAppTokenVo 깃허브 앱 토큰 저장
     * @return List<GithubAppTokenVo> 깃허브 앱 토큰 저장
     * @throws ElException
     */
    public List<GithubAppTokenVo> selectListGithubAppToken(GithubAppTokenVo vo) throws ElException {      	
        return (List<GithubAppTokenVo>)list("com.demo.proworks.githubapptoken.selectListGithubAppToken", vo);
    }

    /**
     * 깃허브 앱 토큰 저장 목록 조회의 전체 카운트를 조회한다.
     *  
     * @param  GithubAppTokenVo 깃허브 앱 토큰 저장
     * @return 깃허브 앱 토큰 저장 조회의 전체 카운트
     * @throws ElException
     */
    public long selectListCountGithubAppToken(GithubAppTokenVo vo)  throws ElException{               
        return (Long)selectByPk("com.demo.proworks.githubapptoken.selectListCountGithubAppToken", vo);
    }
        
    /**
     * 깃허브 앱 토큰 저장를 등록한다.
     *  
     * @param  GithubAppTokenVo 깃허브 앱 토큰 저장
     * @return 번호
     * @throws ElException
     */
    public int insertGithubAppToken(GithubAppTokenVo vo) throws ElException {    	
        return insert("com.demo.proworks.githubapptoken.insertGithubAppToken", vo);
    }

    /**
     * 깃허브 앱 토큰 저장를 갱신한다.
     *  
     * @param  GithubAppTokenVo 깃허브 앱 토큰 저장
     * @return 번호
     * @throws ElException
     */
    public int updateGithubAppToken(GithubAppTokenVo vo) throws ElException {
        return update("com.demo.proworks.githubapptoken.updateGithubAppToken", vo);
    }

    /**
     * 깃허브 앱 토큰 저장를 삭제한다.
     *  
     * @param  GithubAppTokenVo 깃허브 앱 토큰 저장
     * @return 번호
     * @throws ElException
     */
    public int deleteGithubAppToken(GithubAppTokenVo vo) throws ElException {
        return delete("com.demo.proworks.githubapptoken.deleteGithubAppToken", vo);
    }

}
