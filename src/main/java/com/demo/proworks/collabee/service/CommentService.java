package com.demo.proworks.collabee.service;

import java.util.List;

import com.demo.proworks.collabee.vo.CommentVo;

/**  
 * @subject     : Task(업무)에 달리는 댓글 (채팅) 관련 처리를 담당하는 인터페이스
 * @description : Task(업무)에 달리는 댓글 (채팅) 관련 처리를 담당하는 인터페이스
 * @author      : 남기윤
 * @since       : 2025/07/01
 * @modification
 * ===========================================================
 * DATE				AUTHOR				DESC
 * ===========================================================
 * 2025/07/01			 남기윤	 		최초 생성
 * 
 */
public interface CommentService {
	
    /**
     * Task(업무)에 달리는 댓글 (채팅) 페이징 처리하여 목록을 조회한다.
     *
     * @param  commentVo Task(업무)에 달리는 댓글 (채팅) CommentVo
     * @return Task(업무)에 달리는 댓글 (채팅) 목록 List<CommentVo>
     * @throws Exception
     */
	public List<CommentVo> selectListComment(CommentVo commentVo) throws Exception;
	
    /**
     * 조회한 Task(업무)에 달리는 댓글 (채팅) 전체 카운트
     * 
     * @param  commentVo Task(업무)에 달리는 댓글 (채팅) CommentVo
     * @return Task(업무)에 달리는 댓글 (채팅) 목록 전체 카운트
     * @throws Exception
     */
	public long selectListCountComment(CommentVo commentVo) throws Exception;
	
    /**
     * Task(업무)에 달리는 댓글 (채팅)를 상세 조회한다.
     *
     * @param  commentVo Task(업무)에 달리는 댓글 (채팅) CommentVo
     * @return 단건 조회 결과
     * @throws Exception
     */
	public CommentVo selectComment(CommentVo commentVo) throws Exception;
		
    /**
     * Task(업무)에 달리는 댓글 (채팅)를 등록 처리 한다.
     *
     * @param  commentVo Task(업무)에 달리는 댓글 (채팅) CommentVo
     * @return 번호
     * @throws Exception
     */
	public int insertComment(CommentVo commentVo) throws Exception;
	
    /**
     * Task(업무)에 달리는 댓글 (채팅)를 갱신 처리 한다.
     *
     * @param  commentVo Task(업무)에 달리는 댓글 (채팅) CommentVo
     * @return 번호
     * @throws Exception
     */
	public int updateComment(CommentVo commentVo) throws Exception;
	
    /**
     * Task(업무)에 달리는 댓글 (채팅)를 삭제 처리 한다.
     *
     * @param  commentVo Task(업무)에 달리는 댓글 (채팅) CommentVo
     * @return 번호
     * @throws Exception
     */
	public int deleteComment(CommentVo commentVo) throws Exception;
	
}
