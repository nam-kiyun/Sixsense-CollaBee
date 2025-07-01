package com.demo.proworks.collabee.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.inswave.elfw.exception.ElException;
import com.demo.proworks.collabee.vo.UserPersonalTokenVo;
import com.demo.proworks.collabee.dao.UserPersonalTokenDAO;

/**  
 * @subject     : 깃허브 개인 처리를 위한 PAT토큰 관련 처리를 담당하는 DAO
 * @description : 깃허브 개인 처리를 위한 PAT토큰 관련 처리를 담당하는 DAO
 * @author      : 남기윤
 * @since       : 2025/07/01
 * @modification
 * ===========================================================
 * DATE				AUTHOR				DESC
 * ===========================================================
 * 2025/07/01			 남기윤	 		최초 생성
 * 
 */
@Repository("userPersonalTokenDAO")
public class UserPersonalTokenDAO extends com.demo.proworks.cmmn.dao.ProworksDefaultAbstractDAO {

    /**
     * 깃허브 개인 처리를 위한 PAT토큰 상세 조회한다.
     *  
     * @param  UserPersonalTokenVo 깃허브 개인 처리를 위한 PAT토큰
     * @return UserPersonalTokenVo 깃허브 개인 처리를 위한 PAT토큰
     * @throws ElException
     */
    public UserPersonalTokenVo selectUserPersonalToken(UserPersonalTokenVo vo) throws ElException {
        return (UserPersonalTokenVo) selectByPk("com.demo.proworks.collabee.selectUserPersonalToken", vo);
    }

    /**
     * 페이징을 처리하여 깃허브 개인 처리를 위한 PAT토큰 목록조회를 한다.
     *  
     * @param  UserPersonalTokenVo 깃허브 개인 처리를 위한 PAT토큰
     * @return List<UserPersonalTokenVo> 깃허브 개인 처리를 위한 PAT토큰
     * @throws ElException
     */
    public List<UserPersonalTokenVo> selectListUserPersonalToken(UserPersonalTokenVo vo) throws ElException {      	
        return (List<UserPersonalTokenVo>)list("com.demo.proworks.collabee.selectListUserPersonalToken", vo);
    }

    /**
     * 깃허브 개인 처리를 위한 PAT토큰 목록 조회의 전체 카운트를 조회한다.
     *  
     * @param  UserPersonalTokenVo 깃허브 개인 처리를 위한 PAT토큰
     * @return 깃허브 개인 처리를 위한 PAT토큰 조회의 전체 카운트
     * @throws ElException
     */
    public long selectListCountUserPersonalToken(UserPersonalTokenVo vo)  throws ElException{               
        return (Long)selectByPk("com.demo.proworks.collabee.selectListCountUserPersonalToken", vo);
    }
        
    /**
     * 깃허브 개인 처리를 위한 PAT토큰를 등록한다.
     *  
     * @param  UserPersonalTokenVo 깃허브 개인 처리를 위한 PAT토큰
     * @return 번호
     * @throws ElException
     */
    public int insertUserPersonalToken(UserPersonalTokenVo vo) throws ElException {    	
        return insert("com.demo.proworks.collabee.insertUserPersonalToken", vo);
    }

    /**
     * 깃허브 개인 처리를 위한 PAT토큰를 갱신한다.
     *  
     * @param  UserPersonalTokenVo 깃허브 개인 처리를 위한 PAT토큰
     * @return 번호
     * @throws ElException
     */
    public int updateUserPersonalToken(UserPersonalTokenVo vo) throws ElException {
        return update("com.demo.proworks.collabee.updateUserPersonalToken", vo);
    }

    /**
     * 깃허브 개인 처리를 위한 PAT토큰를 삭제한다.
     *  
     * @param  UserPersonalTokenVo 깃허브 개인 처리를 위한 PAT토큰
     * @return 번호
     * @throws ElException
     */
    public int deleteUserPersonalToken(UserPersonalTokenVo vo) throws ElException {
        return delete("com.demo.proworks.collabee.deleteUserPersonalToken", vo);
    }

}
