package com.demo.proworks.comment.service;

import java.util.List;

import com.demo.proworks.comment.vo.CommentVo;

/**  
 * @subject     : 댓글정보 관련 처리를 담당하는 인터페이스
 * @description : 댓글정보 관련 처리를 담당하는 인터페이스
 * @author      : 국다인
 * @since       : 2025/07/01
 * @modification
 * ===========================================================
 * DATE				AUTHOR				DESC
 * ===========================================================
 * 2025/07/01			 국다인	 		최초 생성
 * 
 */
public interface CommentService {
	
    /**
     * 댓글정보 페이징 처리하여 목록을 조회한다.
     *
     * @param  commentVo 댓글정보 CommentVo
     * @return 댓글정보 목록 List<CommentVo>
     * @throws Exception
     */
	public List<CommentVo> selectListComment(CommentVo commentVo) throws Exception;
	
    /**
     * 조회한 댓글정보 전체 카운트
     * 
     * @param  commentVo 댓글정보 CommentVo
     * @return 댓글정보 목록 전체 카운트
     * @throws Exception
     */
	public long selectListCountComment(CommentVo commentVo) throws Exception;
	
    /**
     * 댓글정보를 상세 조회한다.
     *
     * @param  commentVo 댓글정보 CommentVo
     * @return 단건 조회 결과
     * @throws Exception
     */
	public CommentVo selectComment(CommentVo commentVo) throws Exception;
		
    /**
     * 댓글정보를 등록 처리 한다.
     *
     * @param  commentVo 댓글정보 CommentVo
     * @return 번호
     * @throws Exception
     */
	public int insertComment(CommentVo commentVo) throws Exception;
	
    /**
     * 댓글정보를 갱신 처리 한다.
     *
     * @param  commentVo 댓글정보 CommentVo
     * @return 번호
     * @throws Exception
     */
	public int updateComment(CommentVo commentVo) throws Exception;
	
    /**
     * 댓글정보를 삭제 처리 한다.
     *
     * @param  commentVo 댓글정보 CommentVo
     * @return 번호
     * @throws Exception
     */
	public int deleteComment(CommentVo commentVo) throws Exception;
	
}
