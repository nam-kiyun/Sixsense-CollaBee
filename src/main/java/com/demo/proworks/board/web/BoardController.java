package com.demo.proworks.board.web;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.demo.proworks.board.service.BoardService;
import com.demo.proworks.board.vo.BoardVo;
import com.demo.proworks.project.vo.ProjectVo;
import com.demo.proworks.board.vo.BoardListVo;
import com.demo.proworks.redis.service.KanbanRedisService;
import com.demo.proworks.websocket.handler.KanbanWebSocketHandler;

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
    
    /** KanbanRedisService - Redis 캐싱을 위한 서비스 */
    @Autowired
    private KanbanRedisService kanbanRedisService;
    
    /** KanbanWebSocketHandler - WebSocket 메시지 전송을 위한 핸들러 */
    @Autowired
    private KanbanWebSocketHandler kanbanWebSocketHandler;
	
    
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
     * 등록 후 관련 프로젝트의 Redis 캐시를 업데이트합니다.
     *
     * @param  boardVo 보드
     * @throws Exception
     */
    @ElService(key = "board/create")    
    @RequestMapping(value = "board/create")
    @ElDescription(sub = "보드 등록처리", desc = "보드를 등록 처리 한다.")
    public void insertBoard(BoardVo boardVo) throws Exception {    	 
    	
    	boardService.insertBoard(boardVo);
    	
    	
    	// Redis 캐시에 새 보드 추가 (캐시 무효화 대신 직접 업데이트)
    	if (boardVo.getProjectId() != null) {
    	    try {
    	        kanbanRedisService.addBoardToProjectCache(boardVo.getProjectId(), boardVo);
    	    } catch (Exception cacheException) {
    	        kanbanRedisService.invalidateProjectCache(boardVo.getProjectId());
    	    }
    	}
    	
    	// WebSocket을 통한 실시간 보드 생성 브로드캐스트
    	try {
    	    
    	    if (boardVo.getProjectId() != null) {
    	        // 실제 생성된 boardId 사용 (GENERATED 대신)
    	        String actualBoardId = boardVo.getBoardId();
    	        if (actualBoardId == null || actualBoardId.trim().isEmpty()) {
    	            return;
    	        }
    	        
    	        // 보드 생성 메시지 브로드캐스트
    	        java.util.Map<String, Object> messageData = new java.util.HashMap<>();
    	        messageData.put("type", "BOARD_CREATED");
    	        messageData.put("projectId", boardVo.getProjectId());
    	        messageData.put("boardId", actualBoardId);
    	        messageData.put("boardTitle", boardVo.getBoardTitle());
    	        messageData.put("timestamp", System.currentTimeMillis());
    	        
    	        // 모든 사용자에게 실시간 브로드캐스트 (클라이언트에서 프로젝트 ID로 필터링)
    	        kanbanWebSocketHandler.handleBoardCreatedMessage(messageData);
    	    } else {
    	    }
    	} catch (Exception broadcastException) {
    	    broadcastException.printStackTrace();
    	}
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
    	
    	// Redis 캐시 무효화 - 보드 정보 변경은 복잡하므로 무효화 유지
    	if (boardVo.getProjectId() != null) {
    	    kanbanRedisService.invalidateProjectCache(boardVo.getProjectId());
    	}
    }

    /**
     * 보드를 삭제 처리한다.
     * 삭제 후 관련 프로젝트의 Redis 캐시를 업데이트합니다.
     *
     * @param  boardVo 보드    
     * @throws Exception
     */
    @ElService(key = "BoardDel")    
    @RequestMapping(value="BoardDel")
    @ElDescription(sub = "보드 삭제처리", desc = "보드를 삭제 처리한다.")    
    public void deleteBoard(BoardVo boardVo) throws Exception {
        
        // 삭제 전 보드 정보 백업 (WebSocket 메시지용)
        String deletedBoardId = boardVo.getBoardId();
        String deletedBoardTitle = boardVo.getBoardTitle();
        String projectId = boardVo.getProjectId();
        
        // 보드 제목이 없으면 DB에서 조회
        if (deletedBoardTitle == null || deletedBoardTitle.trim().isEmpty()) {
            try {
                BoardVo existingBoard = boardService.selectBoard(boardVo);
                if (existingBoard != null) {
                    deletedBoardTitle = existingBoard.getBoardTitle();
                }
            } catch (Exception e) {
                deletedBoardTitle = "삭제된 보드";
            }
        }
        
        // 보드 삭제 실행
        boardService.deleteBoard(boardVo);
        
        // Redis 캐시에서 보드 제거 (캐시 무효화 대신 직접 업데이트)
        if (projectId != null && deletedBoardId != null) {
            try {
                kanbanRedisService.removeBoardFromProjectCache(projectId, deletedBoardId);
            } catch (Exception cacheException) {
                kanbanRedisService.invalidateProjectCache(projectId);
            }
        }
        
        // WebSocket을 통한 실시간 보드 삭제 브로드캐스트
        try {
            
            if (projectId != null && deletedBoardId != null) {
                // 보드 삭제 메시지 브로드캐스트
                java.util.Map<String, Object> messageData = new java.util.HashMap<>();
                messageData.put("type", "BOARD_DELETED");
                messageData.put("projectId", projectId);
                messageData.put("boardId", deletedBoardId);
                messageData.put("boardTitle", deletedBoardTitle != null ? deletedBoardTitle : "삭제된 보드");
                messageData.put("timestamp", System.currentTimeMillis());
                
                // 모든 사용자에게 실시간 브로드캐스트 (클라이언트에서 프로젝트 ID로 필터링)
                kanbanWebSocketHandler.handleBoardDeletedMessage(messageData);
            } else {
            }
        } catch (Exception broadcastException) {
            broadcastException.printStackTrace();
        }
    }
    
    /**
     * 프로젝트 ID 기준으로 보드 목록을 조회합니다. (칸반 보드용)
     * Redis 캐싱을 적용하여 성능을 최적화합니다.
     *
     * @param  projectVo 프로젝트 정보 (projectId 포함)
     * @return 보드 목록
     * @throws Exception
     */
    @ElService(key = "board/list")    
    @RequestMapping(value = "board/list")
    @ElDescription(sub = "프로젝트별 보드 목록 조회", desc = "프로젝트 ID를 기준으로 보드 목록을 조회한다.")    
    @SuppressWarnings("unchecked")
    public List<BoardVo> selectBoardsByProject(ProjectVo projectVo) throws Exception {
        
        if (projectVo.getProjectId() == null || projectVo.getProjectId().trim().isEmpty()) {
            throw new IllegalArgumentException("프로젝트 ID가 필요합니다.");
        }
        
        String projectId = projectVo.getProjectId();
        
        try {
            // Redis 캐시에서 먼저 조회 시도
            List<java.util.Map<String, Object>> cachedBoards = kanbanRedisService.getProjectBoardsFromCache(projectId);
            if (cachedBoards != null) {
                // Map을 BoardVo로 변환
                List<BoardVo> boardList = convertMapListToBoardVoList(cachedBoards);
                return boardList;
            }
            
            // 캐시 미스 - DB에서 조회
            List<BoardVo> boardList = boardService.selectBoardsByProject(projectId);
            
            // 조회 결과를 Redis에 캐싱
            if (boardList != null && !boardList.isEmpty()) {
                // BoardVo 리스트를 Map 리스트로 변환
                List<java.util.Map<String, Object>> boardMapList = convertBoardVoListToMapList(boardList);
                kanbanRedisService.cacheProjectBoards(projectId, boardMapList);
            }
            
            return boardList;
            
        } catch (Exception e) {
            // Redis 오류 시 DB에서 직접 조회
            List<BoardVo> boardList = boardService.selectBoardsByProject(projectId);
            return boardList;
        }
    }
    
    /**
     * Map 리스트를 BoardVo 리스트로 변환
     */
    private List<BoardVo> convertMapListToBoardVoList(List<java.util.Map<String, Object>> mapList) {
        List<BoardVo> boardList = new java.util.ArrayList<>();
        
        for (java.util.Map<String, Object> map : mapList) {
            BoardVo board = new BoardVo();
            
            // Map에서 BoardVo 필드로 변환 (실제 DB 컬럼명에 맞춤)
            if (map.get("boardId") != null) board.setBoardId(map.get("boardId").toString());
            if (map.get("projectId") != null) board.setProjectId(map.get("projectId").toString());
            if (map.get("boardTitle") != null) board.setBoardTitle(map.get("boardTitle").toString());
            
            boardList.add(board);
        }
        
        return boardList;
    }
    
    /**
     * BoardVo 리스트를 Map 리스트로 변환
     */
    private List<java.util.Map<String, Object>> convertBoardVoListToMapList(List<BoardVo> boardList) {
        List<java.util.Map<String, Object>> mapList = new java.util.ArrayList<>();
        
        for (BoardVo board : boardList) {
            java.util.Map<String, Object> map = new java.util.HashMap<>();
            
            // BoardVo 필드를 Map으로 변환 (실제 DB 컬럼명에 맞춤)
            map.put("boardId", board.getBoardId());
            map.put("projectId", board.getProjectId());
            map.put("boardTitle", board.getBoardTitle());
            
            mapList.add(map);
        }
        
        return mapList;
    }
   
}
