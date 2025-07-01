package com.demo.proworks.collabee.vo.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.inswave.elfw.exception.ElException;
import com.demo.proworks.collabee.vo.vo.GithubWebhookVo;
import com.demo.proworks.collabee.vo.dao.GithubWebhookDAO;

/**  
 * @subject     : 깃허브_웹훅 관련 처리를 담당하는 DAO
 * @description : 깃허브_웹훅 관련 처리를 담당하는 DAO
 * @author      : 남기윤
 * @since       : 2025/06/30
 * @modification
 * ===========================================================
 * DATE				AUTHOR				DESC
 * ===========================================================
 * 2025/06/30			 남기윤	 		최초 생성
 * 
 */
@Repository("githubWebhookDAO")
public class GithubWebhookDAO extends com.demo.proworks.cmmn.dao.ProworksDefaultAbstractDAO {

    /**
     * 깃허브_웹훅 상세 조회한다.
     *  
     * @param  GithubWebhookVo 깃허브_웹훅
     * @return GithubWebhookVo 깃허브_웹훅
     * @throws ElException
     */
    public GithubWebhookVo selectGithubWebhook(GithubWebhookVo vo) throws ElException {
        return (GithubWebhookVo) selectByPk("com.demo.proworks.collabee.vo.selectGithubWebhook", vo);
    }

    /**
     * 페이징을 처리하여 깃허브_웹훅 목록조회를 한다.
     *  
     * @param  GithubWebhookVo 깃허브_웹훅
     * @return List<GithubWebhookVo> 깃허브_웹훅
     * @throws ElException
     */
    public List<GithubWebhookVo> selectListGithubWebhook(GithubWebhookVo vo) throws ElException {      	
        return (List<GithubWebhookVo>)list("com.demo.proworks.collabee.vo.selectListGithubWebhook", vo);
    }

    /**
     * 깃허브_웹훅 목록 조회의 전체 카운트를 조회한다.
     *  
     * @param  GithubWebhookVo 깃허브_웹훅
     * @return 깃허브_웹훅 조회의 전체 카운트
     * @throws ElException
     */
    public long selectListCountGithubWebhook(GithubWebhookVo vo)  throws ElException{               
        return (Long)selectByPk("com.demo.proworks.collabee.vo.selectListCountGithubWebhook", vo);
    }
        
    /**
     * 깃허브_웹훅를 등록한다.
     *  
     * @param  GithubWebhookVo 깃허브_웹훅
     * @return 번호
     * @throws ElException
     */
    public int insertGithubWebhook(GithubWebhookVo vo) throws ElException {    	
        return insert("com.demo.proworks.collabee.vo.insertGithubWebhook", vo);
    }

    /**
     * 깃허브_웹훅를 갱신한다.
     *  
     * @param  GithubWebhookVo 깃허브_웹훅
     * @return 번호
     * @throws ElException
     */
    public int updateGithubWebhook(GithubWebhookVo vo) throws ElException {
        return update("com.demo.proworks.collabee.vo.updateGithubWebhook", vo);
    }

    /**
     * 깃허브_웹훅를 삭제한다.
     *  
     * @param  GithubWebhookVo 깃허브_웹훅
     * @return 번호
     * @throws ElException
     */
    public int deleteGithubWebhook(GithubWebhookVo vo) throws ElException {
        return delete("com.demo.proworks.collabee.vo.deleteGithubWebhook", vo);
    }

}
