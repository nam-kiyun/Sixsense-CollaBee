package com.demo.proworks.collabee.web;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.demo.proworks.collabee.service.BoardService;
import com.demo.proworks.collabee.vo.BoardVo;
import com.demo.proworks.collabee.vo.BoardListVo;

import com.inswave.elfw.annotation.ElDescription;
import com.inswave.elfw.annotation.ElService;
import com.inswave.elfw.annotation.ElValidator;

/**  
 * @subject     : 칸반보드의 보드 관련 처리를 담당하는 컨트롤러
 * @description : 칸반보드의 보드 관련 처리를 담당하는 컨트롤러
 * @author      : 남기윤
 * @since       : 2025/07/01
 * @modification
 * ===========================================================
 * DATE				AUTHOR				DESC
 * ===========================================================
 * 2025/07/01			 남기윤	 		최초 생성
 * 
 */
@Controller
public class BoardController {
	
    /** BoardService */
    @Resource(name = "boardServiceImpl")
    private BoardService boardService;
	
    
    /**
     * 칸반보드의 보드 목록을 조회합니다.
     *
     * @param  boardVo 칸반보드의 보드
     * @return 목록조회 결과
     * @throws Exception
     */
    @ElService(key="BoardList")
    @RequestMapping(value="BoardList")    
    @ElDescription(sub="칸반보드의 보드 목록조회",desc="페이징을 처리하여 칸반보드의 보드 목록 조회를 한다.")               
    public BoardListVo selectListBoard(BoardVo boardVo) throws Exception {    	   	

        List<BoardVo> boardList = boardService.selectListBoard(boardVo);                  
        long totCnt = boardService.selectListCountBoard(boardVo);
	
		BoardListVo retBoardList = new BoardListVo();
		retBoardList.setBoardVoList(boardList); 
		retBoardList.setTotalCount(totCnt);
		retBoardList.setPageSize(boardVo.getPageSize());
		retBoardList.setPageIndex(boardVo.getPageIndex());

        return retBoardList;            
    }  
        
    /**
     * 칸반보드의 보드을 단건 조회 처리 한다.
     *
     * @param  boardVo 칸반보드의 보드
     * @return 단건 조회 결과
     * @throws Exception
     */
    @ElService(key = "BoardUpdView")    
    @RequestMapping(value="BoardUpdView") 
    @ElDescription(sub = "칸반보드의 보드 갱신 폼을 위한 조회", desc = "칸반보드의 보드 갱신 폼을 위한 조회를 한다.")    
    public BoardVo selectBoard(BoardVo boardVo) throws Exception {
    	BoardVo selectBoardVo = boardService.selectBoard(boardVo);    	    
		
        return selectBoardVo;
    } 
 
    /**
     * 칸반보드의 보드를 등록 처리 한다.
     *
     * @param  boardVo 칸반보드의 보드
     * @throws Exception
     */
    @ElService(key="BoardIns")    
    @RequestMapping(value="BoardIns")
    @ElDescription(sub="칸반보드의 보드 등록처리",desc="칸반보드의 보드를 등록 처리 한다.")
    public void insertBoard(BoardVo boardVo) throws Exception {    	 
    	boardService.insertBoard(boardVo);   
    }
       
    /**
     * 칸반보드의 보드를 갱신 처리 한다.
     *
     * @param  boardVo 칸반보드의 보드
     * @throws Exception
     */
    @ElService(key="BoardUpd")    
    @RequestMapping(value="BoardUpd")    
    @ElValidator(errUrl="/board/boardRegister", errContinue=true)
    @ElDescription(sub="칸반보드의 보드 갱신처리",desc="칸반보드의 보드를 갱신 처리 한다.")    
    public void updateBoard(BoardVo boardVo) throws Exception {  
 
    	boardService.updateBoard(boardVo);                                            
    }

    /**
     * 칸반보드의 보드를 삭제 처리한다.
     *
     * @param  boardVo 칸반보드의 보드    
     * @throws Exception
     */
    @ElService(key = "BoardDel")    
    @RequestMapping(value="BoardDel")
    @ElDescription(sub = "칸반보드의 보드 삭제처리", desc = "칸반보드의 보드를 삭제 처리한다.")    
    public void deleteBoard(BoardVo boardVo) throws Exception {
        boardService.deleteBoard(boardVo);
    }
   
}
