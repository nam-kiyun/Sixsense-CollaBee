package com.demo.proworks.collabee.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.demo.proworks.collabee.service.CommentService;
import com.demo.proworks.collabee.vo.CommentVo;
import com.demo.proworks.collabee.dao.CommentDAO;

/**  
 * @subject     : Task(업무)에 달리는 댓글 (채팅) 관련 처리를 담당하는 ServiceImpl
 * @description	: Task(업무)에 달리는 댓글 (채팅) 관련 처리를 담당하는 ServiceImpl
 * @author      : 남기윤
 * @since       : 2025/07/01
 * @modification
 * ===========================================================
 * DATE				AUTHOR				DESC
 * ===========================================================
 * 2025/07/01			 남기윤	 		최초 생성
 * 
 */
@Service("commentServiceImpl")
public class CommentServiceImpl implements CommentService {

    @Resource(name="commentDAO")
    private CommentDAO commentDAO;
	
	@Resource(name = "messageSource")
	private MessageSource messageSource;

    /**
     * Task(업무)에 달리는 댓글 (채팅) 목록을 조회합니다.
     *
     * @process
     * 1. Task(업무)에 달리는 댓글 (채팅) 페이징 처리하여 목록을 조회한다.
     * 2. 결과 List<CommentVo>을(를) 리턴한다.
     * 
     * @param  commentVo Task(업무)에 달리는 댓글 (채팅) CommentVo
     * @return Task(업무)에 달리는 댓글 (채팅) 목록 List<CommentVo>
     * @throws Exception
     */
	public List<CommentVo> selectListComment(CommentVo commentVo) throws Exception {
		List<CommentVo> list = commentDAO.selectListComment(commentVo);	
	
		return list;
	}

    /**
     * 조회한 Task(업무)에 달리는 댓글 (채팅) 전체 카운트
     *
     * @process
     * 1. Task(업무)에 달리는 댓글 (채팅) 조회하여 전체 카운트를 리턴한다.
     * 
     * @param  commentVo Task(업무)에 달리는 댓글 (채팅) CommentVo
     * @return Task(업무)에 달리는 댓글 (채팅) 목록 전체 카운트
     * @throws Exception
     */
	public long selectListCountComment(CommentVo commentVo) throws Exception {
		return commentDAO.selectListCountComment(commentVo);
	}

    /**
     * Task(업무)에 달리는 댓글 (채팅)를 상세 조회한다.
     *
     * @process
     * 1. Task(업무)에 달리는 댓글 (채팅)를 상세 조회한다.
     * 2. 결과 CommentVo을(를) 리턴한다.
     * 
     * @param  commentVo Task(업무)에 달리는 댓글 (채팅) CommentVo
     * @return 단건 조회 결과
     * @throws Exception
     */
	public CommentVo selectComment(CommentVo commentVo) throws Exception {
		CommentVo resultVO = commentDAO.selectComment(commentVo);			
        
        return resultVO;
	}

    /**
     * Task(업무)에 달리는 댓글 (채팅)를 등록 처리 한다.
     *
     * @process
     * 1. Task(업무)에 달리는 댓글 (채팅)를 등록 처리 한다.
     * 
     * @param  commentVo Task(업무)에 달리는 댓글 (채팅) CommentVo
     * @return 번호
     * @throws Exception
     */
	public int insertComment(CommentVo commentVo) throws Exception {
		return commentDAO.insertComment(commentVo);	
	}
	
    /**
     * Task(업무)에 달리는 댓글 (채팅)를 갱신 처리 한다.
     *
     * @process
     * 1. Task(업무)에 달리는 댓글 (채팅)를 갱신 처리 한다.
     * 
     * @param  commentVo Task(업무)에 달리는 댓글 (채팅) CommentVo
     * @return 번호
     * @throws Exception
     */
	public int updateComment(CommentVo commentVo) throws Exception {				
		return commentDAO.updateComment(commentVo);	   		
	}

    /**
     * Task(업무)에 달리는 댓글 (채팅)를 삭제 처리 한다.
     *
     * @process
     * 1. Task(업무)에 달리는 댓글 (채팅)를 삭제 처리 한다.
     * 
     * @param  commentVo Task(업무)에 달리는 댓글 (채팅) CommentVo
     * @return 번호
     * @throws Exception
     */
	public int deleteComment(CommentVo commentVo) throws Exception {
		return commentDAO.deleteComment(commentVo);
	}
	
}
