package com.demo.proworks.collabee.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.inswave.elfw.exception.ElException;
import com.demo.proworks.collabee.vo.CommentVo;
import com.demo.proworks.collabee.dao.CommentDAO;

/**  
 * @subject     : Task(업무)에 달리는 댓글 (채팅) 관련 처리를 담당하는 DAO
 * @description : Task(업무)에 달리는 댓글 (채팅) 관련 처리를 담당하는 DAO
 * @author      : 남기윤
 * @since       : 2025/07/01
 * @modification
 * ===========================================================
 * DATE				AUTHOR				DESC
 * ===========================================================
 * 2025/07/01			 남기윤	 		최초 생성
 * 
 */
@Repository("commentDAO")
public class CommentDAO extends com.demo.proworks.cmmn.dao.ProworksDefaultAbstractDAO {

    /**
     * Task(업무)에 달리는 댓글 (채팅) 상세 조회한다.
     *  
     * @param  CommentVo Task(업무)에 달리는 댓글 (채팅)
     * @return CommentVo Task(업무)에 달리는 댓글 (채팅)
     * @throws ElException
     */
    public CommentVo selectComment(CommentVo vo) throws ElException {
        return (CommentVo) selectByPk("com.demo.proworks.collabee.selectComment", vo);
    }

    /**
     * 페이징을 처리하여 Task(업무)에 달리는 댓글 (채팅) 목록조회를 한다.
     *  
     * @param  CommentVo Task(업무)에 달리는 댓글 (채팅)
     * @return List<CommentVo> Task(업무)에 달리는 댓글 (채팅)
     * @throws ElException
     */
    public List<CommentVo> selectListComment(CommentVo vo) throws ElException {      	
        return (List<CommentVo>)list("com.demo.proworks.collabee.selectListComment", vo);
    }

    /**
     * Task(업무)에 달리는 댓글 (채팅) 목록 조회의 전체 카운트를 조회한다.
     *  
     * @param  CommentVo Task(업무)에 달리는 댓글 (채팅)
     * @return Task(업무)에 달리는 댓글 (채팅) 조회의 전체 카운트
     * @throws ElException
     */
    public long selectListCountComment(CommentVo vo)  throws ElException{               
        return (Long)selectByPk("com.demo.proworks.collabee.selectListCountComment", vo);
    }
        
    /**
     * Task(업무)에 달리는 댓글 (채팅)를 등록한다.
     *  
     * @param  CommentVo Task(업무)에 달리는 댓글 (채팅)
     * @return 번호
     * @throws ElException
     */
    public int insertComment(CommentVo vo) throws ElException {    	
        return insert("com.demo.proworks.collabee.insertComment", vo);
    }

    /**
     * Task(업무)에 달리는 댓글 (채팅)를 갱신한다.
     *  
     * @param  CommentVo Task(업무)에 달리는 댓글 (채팅)
     * @return 번호
     * @throws ElException
     */
    public int updateComment(CommentVo vo) throws ElException {
        return update("com.demo.proworks.collabee.updateComment", vo);
    }

    /**
     * Task(업무)에 달리는 댓글 (채팅)를 삭제한다.
     *  
     * @param  CommentVo Task(업무)에 달리는 댓글 (채팅)
     * @return 번호
     * @throws ElException
     */
    public int deleteComment(CommentVo vo) throws ElException {
        return delete("com.demo.proworks.collabee.deleteComment", vo);
    }

}
