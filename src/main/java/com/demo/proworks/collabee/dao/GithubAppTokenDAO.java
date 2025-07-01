package com.demo.proworks.collabee.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.inswave.elfw.exception.ElException;
import com.demo.proworks.collabee.vo.GithubAppTokenVo;
import com.demo.proworks.collabee.dao.GithubAppTokenDAO;

/**  
 * @subject     : 깃허브 앱 토큰 관련 처리를 담당하는 DAO
 * @description : 깃허브 앱 토큰 관련 처리를 담당하는 DAO
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
     * 깃허브 앱 토큰 상세 조회한다.
     *  
     * @param  GithubAppTokenVo 깃허브 앱 토큰
     * @return GithubAppTokenVo 깃허브 앱 토큰
     * @throws ElException
     */
    public GithubAppTokenVo selectGithubAppToken(GithubAppTokenVo vo) throws ElException {
        return (GithubAppTokenVo) selectByPk("com.demo.proworks.collabee.selectGithubAppToken", vo);
    }

    /**
     * 페이징을 처리하여 깃허브 앱 토큰 목록조회를 한다.
     *  
     * @param  GithubAppTokenVo 깃허브 앱 토큰
     * @return List<GithubAppTokenVo> 깃허브 앱 토큰
     * @throws ElException
     */
    public List<GithubAppTokenVo> selectListGithubAppToken(GithubAppTokenVo vo) throws ElException {      	
        return (List<GithubAppTokenVo>)list("com.demo.proworks.collabee.selectListGithubAppToken", vo);
    }

    /**
     * 깃허브 앱 토큰 목록 조회의 전체 카운트를 조회한다.
     *  
     * @param  GithubAppTokenVo 깃허브 앱 토큰
     * @return 깃허브 앱 토큰 조회의 전체 카운트
     * @throws ElException
     */
    public long selectListCountGithubAppToken(GithubAppTokenVo vo)  throws ElException{               
        return (Long)selectByPk("com.demo.proworks.collabee.selectListCountGithubAppToken", vo);
    }
        
    /**
     * 깃허브 앱 토큰를 등록한다.
     *  
     * @param  GithubAppTokenVo 깃허브 앱 토큰
     * @return 번호
     * @throws ElException
     */
    public int insertGithubAppToken(GithubAppTokenVo vo) throws ElException {    	
        return insert("com.demo.proworks.collabee.insertGithubAppToken", vo);
    }

    /**
     * 깃허브 앱 토큰를 갱신한다.
     *  
     * @param  GithubAppTokenVo 깃허브 앱 토큰
     * @return 번호
     * @throws ElException
     */
    public int updateGithubAppToken(GithubAppTokenVo vo) throws ElException {
        return update("com.demo.proworks.collabee.updateGithubAppToken", vo);
    }

    /**
     * 깃허브 앱 토큰를 삭제한다.
     *  
     * @param  GithubAppTokenVo 깃허브 앱 토큰
     * @return 번호
     * @throws ElException
     */
    public int deleteGithubAppToken(GithubAppTokenVo vo) throws ElException {
        return delete("com.demo.proworks.collabee.deleteGithubAppToken", vo);
    }

}
