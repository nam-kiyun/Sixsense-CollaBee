package com.demo.proworks.comment.web;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.demo.proworks.comment.service.CommentService;
import com.demo.proworks.comment.vo.CommentVo;
import com.demo.proworks.comment.vo.CommentListSearchVo;
import com.demo.proworks.comment.vo.CommentListVo;

import com.inswave.elfw.annotation.ElDescription;
import com.inswave.elfw.annotation.ElService;
import com.inswave.elfw.annotation.ElValidator;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @subject : 댓글정보 관련 처리를 담당하는 컨트롤러
 * @description : 댓글정보 관련 처리를 담당하는 컨트롤러
 * @author : 국다인
 * @since : 2025/07/01
 * @modification ===========================================================
 *               DATE AUTHOR DESC
 *               ===========================================================
 *               2025/07/01 국다인 최초 생성
 * 
 */
@Controller
public class CommentController {

	/** CommentService */
	@Resource(name = "commentServiceImpl")
	private CommentService commentService;

	@Resource(name = "redisTemplate")
	private RedisTemplate<String, String> redisTemplate;

	/**
	 * 댓글정보 목록을 조회합니다.
	 *
	 * @param commentVo 댓글정보
	 * @return 목록조회 결과
	 * @throws Exception
	 */
	@ElService(key = "comment/{taskId}")
	@RequestMapping(value = "comment/{taskId}")
	@ElDescription(sub = "댓글정보 목록조회", desc = "페이징을 처리하여 댓글정보 목록 조회를 한다.")
	public CommentListVo selectListComment(@PathVariable("taskId") int taskId, CommentListSearchVo searchVo) throws Exception {
		long totCnt = commentService.selectListCountByTaskId(taskId); //해당 task의 댓글 개수
		int page = searchVo.getPage();
		searchVo.setTaskId(taskId);
		searchVo.setPageSize(10);
		searchVo.setStartOffset((page - 1) * 10);
		
		List<CommentVo> commentList = commentService.selectListCommentByTaskId(searchVo); //해당 task의 댓글을 조회
		
		CommentListVo listVo = new CommentListVo();
		listVo.setCommentVoList(commentList);
		listVo.setTotalCount(totCnt);
		listVo.setPageSize((int)Math.ceil(totCnt / 10));
		listVo.setPageIndex((long)page);
		System.out.println("댓글정보 목록을 조회: " + commentList);
		
		return listVo;
	}

	/**
	 * 댓글정보을 단건 조회 처리 한다.
	 *
	 * @param commentVo 댓글정보
	 * @return 단건 조회 결과
	 * @throws Exception
	 */
	@ElService(key = "comment/{taskId}/{commentId}")
	@RequestMapping(value = "comment/{taskId}/{commentId}")
	@ElDescription(sub = "댓글정보 갱신 폼을 위한 조회", desc = "댓글정보 갱신 폼을 위한 조회를 한다.")
	public CommentVo selectComment(CommentVo commentVo) throws Exception {
		CommentVo selectCommentVo = commentService.selectComment(commentVo);

		return selectCommentVo;
	}

	/**
	 * 댓글정보를 등록 처리 한다.
	 *
	 * @param commentVo 댓글정보
	 * @throws Exception
	 */
	@ElService(key = "comment/create")
	@RequestMapping(value = "comment/create")
	@ElDescription(sub = "댓글정보 등록처리", desc = "댓글정보를 등록 처리 한다.")
	public void insertComment(CommentVo commentVo) throws Exception {
		commentService.insertComment(commentVo);
	}

	/**
	 * 댓글정보를 갱신 처리 한다.
	 *
	 * @param commentVo 댓글정보
	 * @throws Exception
	 */
	@ElService(key = "comment/update")
	@RequestMapping(value = "comment/update")
	@ElDescription(sub = "댓글정보 갱신처리", desc = "댓글정보를 갱신 처리 한다.")
	public void updateComment(CommentVo commentVo) throws Exception {

		commentService.updateComment(commentVo);
	}

	/**
	 * 댓글정보를 삭제 처리한다.
	 *
	 * @param commentVo 댓글정보
	 * @throws Exception
	 */
	@ElService(key = "comment/delete")
	@RequestMapping(value = "comment/delete")
	@ElDescription(sub = "댓글정보 삭제처리", desc = "댓글정보를 삭제 처리한다.")
	public void deleteComment(CommentVo commentVo) throws Exception {
		commentService.deleteComment(commentVo);
	}

}
