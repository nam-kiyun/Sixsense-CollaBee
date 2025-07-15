package com.demo.proworks.board.web;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.demo.proworks.board.service.BoardService;
import com.demo.proworks.board.vo.BoardVo;
import com.demo.proworks.project.vo.ProjectVo;
import com.demo.proworks.board.vo.BoardListVo;

import com.inswave.elfw.annotation.ElDescription;
import com.inswave.elfw.annotation.ElService;
import com.inswave.elfw.annotation.ElValidator;
import org.springframework.web.bind.annotation.RequestMethod;

/**  
 * @subject     : 보드 관련 처리를 담당하는 컨트롤러
 * @description : 보드 관련 처리를 담당하는 컨트롤러
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
public class BoardController {
	
    /** BoardService */
    @Resource(name = "boardServiceImpl")
    private BoardService boardService;
	
    
    /**
     * 보드 목록을 조회합니다.
     *
     * @param  boardVo 보드
     * @return 목록조회 결과
     * @throws Exception
     */
    @ElService(key="BoardList")
    @RequestMapping(value="BoardList")    
    @ElDescription(sub="보드 목록조회",desc="페이징을 처리하여 보드 목록 조회를 한다.")               
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
     * 보드을 단건 조회 처리 한다.
     *
     * @param  boardVo 보드
     * @return 단건 조회 결과
     * @throws Exception
     */
    @ElService(key = "BoardUpdView")    
    @RequestMapping(value="BoardUpdView") 
    @ElDescription(sub = "보드 갱신 폼을 위한 조회", desc = "보드 갱신 폼을 위한 조회를 한다.")    
    public BoardVo selectBoard(BoardVo boardVo) throws Exception {
    	BoardVo selectBoardVo = boardService.selectBoard(boardVo);    	    
		
        return selectBoardVo;
    } 
 
    /**
     * 보드를 등록 처리 한다.
     *
     * @param  boardVo 보드
     * @throws Exception
     */
    @ElService(key="BoardIns")    
    @RequestMapping(value="BoardIns")
    @ElDescription(sub="보드 등록처리",desc="보드를 등록 처리 한다.")
    public void insertBoard(BoardVo boardVo) throws Exception {    	 
    	boardService.insertBoard(boardVo);   
    }
       
    /**
     * 보드를 갱신 처리 한다.
     *
     * @param  boardVo 보드
     * @throws Exception
     */
    @ElService(key="BoardUpd")    
    @RequestMapping(value="BoardUpd")    
    @ElValidator(errUrl="/board/boardRegister", errContinue=true)
    @ElDescription(sub="보드 갱신처리",desc="보드를 갱신 처리 한다.")    
    public void updateBoard(BoardVo boardVo) throws Exception {  
 
    	boardService.updateBoard(boardVo);                                            
    }

    /**
     * 보드를 삭제 처리한다.
     *
     * @param  boardVo 보드    
     * @throws Exception
     */
    @ElService(key = "BoardDel")    
    @RequestMapping(value="BoardDel")
    @ElDescription(sub = "보드 삭제처리", desc = "보드를 삭제 처리한다.")    
    public void deleteBoard(BoardVo boardVo) throws Exception {
        boardService.deleteBoard(boardVo);
    }
    
    /**
     * 프로젝트 ID 기준으로 보드 목록을 조회합니다. (칸반 보드용)
     *
     * @param  projectVo 프로젝트 정보 (projectId 포함)
     * @return 보드 목록
     * @throws Exception
     */
    @ElService(key = "board/list")    
    @RequestMapping(value = "board/list")
    @ElDescription(sub = "프로젝트별 보드 목록 조회", desc = "프로젝트 ID를 기준으로 보드 목록을 조회한다.")    
    public List<BoardVo> selectBoardsByProject(ProjectVo projectVo) throws Exception {
        System.out.println("보드 목록 조회 요청 - projectId: " + projectVo.getProjectId());
        
        if (projectVo.getProjectId() == null || projectVo.getProjectId().trim().isEmpty()) {
            throw new IllegalArgumentException("프로젝트 ID가 필요합니다.");
        }
        
        List<BoardVo> boardList = boardService.selectBoardsByProject(projectVo.getProjectId());
        System.out.println("조회된 보드 개수: " + (boardList != null ? boardList.size() : 0));
        
        return boardList;
    }
   
}
