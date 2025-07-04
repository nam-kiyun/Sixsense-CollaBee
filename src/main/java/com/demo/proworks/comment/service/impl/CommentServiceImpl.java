package com.demo.proworks.comment.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.demo.proworks.comment.service.CommentService;
import com.demo.proworks.comment.vo.CommentVo;
import com.demo.proworks.comment.dao.CommentDAO;

/**  
 * @subject     : 댓글정보 관련 처리를 담당하는 ServiceImpl
 * @description	: 댓글정보 관련 처리를 담당하는 ServiceImpl
 * @author      : 국다인
 * @since       : 2025/07/01
 * @modification
 * ===========================================================
 * DATE				AUTHOR				DESC
 * ===========================================================
 * 2025/07/01			 국다인	 		최초 생성
 * 
 */
@Service("commentServiceImpl")
public class CommentServiceImpl implements CommentService {

    @Resource(name="commentDAO")
    private CommentDAO commentDAO;
	
	@Resource(name = "messageSource")
	private MessageSource messageSource;

    /**
     * 댓글정보 목록을 조회합니다.
     *
     * @process
     * 1. 댓글정보 페이징 처리하여 목록을 조회한다.
     * 2. 결과 List<CommentVo>을(를) 리턴한다.
     * 
     * @param  commentVo 댓글정보 CommentVo
     * @return 댓글정보 목록 List<CommentVo>
     * @throws Exception
     */
	public List<CommentVo> selectListComment(CommentVo commentVo) throws Exception {
		List<CommentVo> list = commentDAO.selectListComment(commentVo);	
	
		return list;
	}

    /**
     * 조회한 댓글정보 전체 카운트
     *
     * @process
     * 1. 댓글정보 조회하여 전체 카운트를 리턴한다.
     * 
     * @param  commentVo 댓글정보 CommentVo
     * @return 댓글정보 목록 전체 카운트
     * @throws Exception
     */
	public long selectListCountComment(CommentVo commentVo) throws Exception {
		return commentDAO.selectListCountComment(commentVo);
	}

    /**
     * 댓글정보를 상세 조회한다.
     *
     * @process
     * 1. 댓글정보를 상세 조회한다.
     * 2. 결과 CommentVo을(를) 리턴한다.
     * 
     * @param  commentVo 댓글정보 CommentVo
     * @return 단건 조회 결과
     * @throws Exception
     */
	public CommentVo selectComment(CommentVo commentVo) throws Exception {
		CommentVo resultVO = commentDAO.selectComment(commentVo);			
        
        return resultVO;
	}

    /**
     * 댓글정보를 등록 처리 한다.
     *
     * @process
     * 1. 댓글정보를 등록 처리 한다.
     * 
     * @param  commentVo 댓글정보 CommentVo
     * @return 번호
     * @throws Exception
     */
	public int insertComment(CommentVo commentVo) throws Exception {
		return commentDAO.insertComment(commentVo);	
	}
	
    /**
     * 댓글정보를 갱신 처리 한다.
     *
     * @process
     * 1. 댓글정보를 갱신 처리 한다.
     * 
     * @param  commentVo 댓글정보 CommentVo
     * @return 번호
     * @throws Exception
     */
	public int updateComment(CommentVo commentVo) throws Exception {				
		return commentDAO.updateComment(commentVo);	   		
	}

    /**
     * 댓글정보를 삭제 처리 한다.
     *
     * @process
     * 1. 댓글정보를 삭제 처리 한다.
     * 
     * @param  commentVo 댓글정보 CommentVo
     * @return 번호
     * @throws Exception
     */
	public int deleteComment(CommentVo commentVo) throws Exception {
		return commentDAO.deleteComment(commentVo);
	}
	
}
