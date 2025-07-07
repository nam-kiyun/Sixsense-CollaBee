package com.demo.proworks.comment.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.inswave.elfw.exception.ElException;
import com.demo.proworks.comment.vo.CommentListSearchVo;
import com.demo.proworks.comment.vo.CommentVo;
import com.demo.proworks.comment.dao.CommentDAO;

/**
 * @subject : 댓글정보 관련 처리를 담당하는 DAO
 * @description : 댓글정보 관련 처리를 담당하는 DAO
 * @author : 국다인
 * @since : 2025/07/01
 * @modification ===========================================================
 *               DATE AUTHOR DESC
 *               ===========================================================
 *               2025/07/01 국다인 최초 생성
 * 
 */
@Repository("commentDAO")
public class CommentDAO extends com.demo.proworks.cmmn.dao.ProworksDefaultAbstractDAO {

	/**
	 * 특정 Task ID에 해당하는 댓글 정보 목록을 조회한다.
	 *
	 * @param commentVo taskId를 포함하는 댓글 정보 (taskId 필드가 CommentVo에 있어야 합니다.)
	 * @return List<CommentVo> 댓글 정보 목록
	 * @throws ElException
	 */
	public List<CommentVo> selectListCommentByTaskId(CommentListSearchVo searchVo) throws ElException {
		return (List<CommentVo>) list("com.demo.proworks.comment.selectListCommentByTaskId", searchVo);
	}

	/**
	 * 댓글정보 목록 조회의 전체 카운트를 조회한다.
	 * 
	 * @param CommentVo 댓글정보
	 * @return 댓글정보 조회의 전체 카운트
	 * @throws ElException
	 */
	 
	
	public long selectListCountByTaskId(int taskId) throws ElException {
		CommentVo commentVo = new CommentVo();
		commentVo.setTaskId(taskId);
		return (Long) selectByPk("com.demo.proworks.comment.selectListCountByTaskId", commentVo);
	}

	/**
	 * 댓글정보 상세 조회한다.
	 * 
	 * @param CommentVo 댓글정보
	 * @return CommentVo 댓글정보
	 * @throws ElException
	 */
	public CommentVo selectComment(CommentVo vo) throws ElException {
		return (CommentVo) selectByPk("com.demo.proworks.comment.selectComment", vo);
	}

	/**
	 * 페이징을 처리하여 댓글정보 목록조회를 한다.
	 * 
	 * @param CommentVo 댓글정보
	 * @return List<CommentVo> 댓글정보
	 * @throws ElException
	 */
	public List<CommentVo> selectListComment(CommentVo vo) throws ElException {
		return (List<CommentVo>) list("com.demo.proworks.comment.selectListComment", vo);
	}

	/**
	 * 댓글정보 목록 조회의 전체 카운트를 조회한다.
	 * 
	 * @param CommentVo 댓글정보
	 * @return 댓글정보 조회의 전체 카운트
	 * @throws ElException
	 */
	public long selectListCountComment(CommentVo vo) throws ElException {
		return (Long) selectByPk("com.demo.proworks.comment.selectListCountComment", vo);
	}

	/**
	 * 댓글정보를 등록한다.
	 * 
	 * @param CommentVo 댓글정보
	 * @return 번호
	 * @throws ElException
	 */
	public int insertComment(CommentVo vo) throws ElException {
		return insert("com.demo.proworks.comment.insertComment", vo);
	}

	/**
	 * 댓글정보를 갱신한다.
	 * 
	 * @param CommentVo 댓글정보
	 * @return 번호
	 * @throws ElException
	 */
	public int updateComment(CommentVo vo) throws ElException {
		return update("com.demo.proworks.comment.updateComment", vo);
	}

	/**
	 * 댓글정보를 삭제한다.
	 * 
	 * @param CommentVo 댓글정보
	 * @return 번호
	 * @throws ElException
	 */
	public int deleteComment(CommentVo vo) throws ElException {
		return delete("com.demo.proworks.comment.deleteComment", vo);
	}

}
