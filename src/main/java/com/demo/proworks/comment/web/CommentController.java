package com.demo.proworks.comment.web;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.demo.proworks.comment.service.CommentService;
import com.demo.proworks.comment.vo.CommentVo;
import com.demo.proworks.comment.vo.CommentListVo;

import com.inswave.elfw.annotation.ElDescription;
import com.inswave.elfw.annotation.ElService;
import com.inswave.elfw.annotation.ElValidator;

/**  
 * @subject     : 댓글정보 관련 처리를 담당하는 컨트롤러
 * @description : 댓글정보 관련 처리를 담당하는 컨트롤러
 * @author      : 국다인
 * @since       : 2025/07/01
 * @modification
 * ===========================================================
 * DATE				AUTHOR				DESC
 * ===========================================================
 * 2025/07/01			 국다인	 		최초 생성
 * 
 */
@Controller
public class CommentController {
	
    /** CommentService */
    @Resource(name = "commentServiceImpl")
    private CommentService commentService;
	
    
    /**
     * 댓글정보 목록을 조회합니다.
     *
     * @param  commentVo 댓글정보
     * @return 목록조회 결과
     * @throws Exception
     */
    @ElService(key="CommentList")
    @RequestMapping(value="CommentList")    
    @ElDescription(sub="댓글정보 목록조회",desc="페이징을 처리하여 댓글정보 목록 조회를 한다.")               
    public CommentListVo selectListComment(CommentVo commentVo) throws Exception {    	   	

        List<CommentVo> commentList = commentService.selectListComment(commentVo);                  
        long totCnt = commentService.selectListCountComment(commentVo);
	
		CommentListVo retCommentList = new CommentListVo();
		retCommentList.setCommentVoList(commentList); 
		retCommentList.setTotalCount(totCnt);
		retCommentList.setPageSize(commentVo.getPageSize());
		retCommentList.setPageIndex(commentVo.getPageIndex());

        return retCommentList;            
    }  
        
    /**
     * 댓글정보을 단건 조회 처리 한다.
     *
     * @param  commentVo 댓글정보
     * @return 단건 조회 결과
     * @throws Exception
     */
    @ElService(key = "CommentUpdView")    
    @RequestMapping(value="CommentUpdView") 
    @ElDescription(sub = "댓글정보 갱신 폼을 위한 조회", desc = "댓글정보 갱신 폼을 위한 조회를 한다.")    
    public CommentVo selectComment(CommentVo commentVo) throws Exception {
    	CommentVo selectCommentVo = commentService.selectComment(commentVo);    	    
		
        return selectCommentVo;
    } 
 
    /**
     * 댓글정보를 등록 처리 한다.
     *
     * @param  commentVo 댓글정보
     * @throws Exception
     */
    @ElService(key="CommentIns")    
    @RequestMapping(value="CommentIns")
    @ElDescription(sub="댓글정보 등록처리",desc="댓글정보를 등록 처리 한다.")
    public void insertComment(CommentVo commentVo) throws Exception {    	 
    	commentService.insertComment(commentVo);   
    }
       
    /**
     * 댓글정보를 갱신 처리 한다.
     *
     * @param  commentVo 댓글정보
     * @throws Exception
     */
    @ElService(key="CommentUpd")    
    @RequestMapping(value="CommentUpd")    
    @ElValidator(errUrl="/comment/commentRegister", errContinue=true)
    @ElDescription(sub="댓글정보 갱신처리",desc="댓글정보를 갱신 처리 한다.")    
    public void updateComment(CommentVo commentVo) throws Exception {  
 
    	commentService.updateComment(commentVo);                                            
    }

    /**
     * 댓글정보를 삭제 처리한다.
     *
     * @param  commentVo 댓글정보    
     * @throws Exception
     */
    @ElService(key = "CommentDel")    
    @RequestMapping(value="CommentDel")
    @ElDescription(sub = "댓글정보 삭제처리", desc = "댓글정보를 삭제 처리한다.")    
    public void deleteComment(CommentVo commentVo) throws Exception {
        commentService.deleteComment(commentVo);
    }
   
}
