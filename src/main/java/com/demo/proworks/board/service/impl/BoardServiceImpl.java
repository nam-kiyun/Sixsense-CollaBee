package com.demo.proworks.board.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.demo.proworks.board.service.BoardService;
import com.demo.proworks.board.vo.BoardVo;
import com.demo.proworks.board.dao.BoardDAO;
import com.demo.proworks.redis.service.KanbanRedisService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;

/**  
 * @subject     : 보드 관련 처리를 담당하는 ServiceImpl
 * @description	: 보드 관련 처리를 담당하는 ServiceImpl
 * @author      : 국다인
 * @since       : 2025/07/01
 * @modification
 * ===========================================================
 * DATE				AUTHOR				DESC
 * ===========================================================
 * 2025/07/01			 국다인	 		최초 생성
 * 
 */
@Service("boardServiceImpl")
public class BoardServiceImpl implements BoardService {

    @Resource(name="boardDAO")
    private BoardDAO boardDAO;
    
    @Resource(name="kanbanRedisService")
    private KanbanRedisService kanbanRedisService;
	
	@Resource(name = "messageSource")
	private MessageSource messageSource;
	
	private final ObjectMapper objectMapper;
	
	// 생성자에서 ObjectMapper 설정
	public BoardServiceImpl() {
		this.objectMapper = new ObjectMapper();
		// @JsonFilter 어노테이션 무시 설정
		this.objectMapper.configure(MapperFeature.USE_ANNOTATIONS, false);
		this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}

    /**
     * 보드 목록을 조회합니다.
     *
     * @process
     * 1. 보드 페이징 처리하여 목록을 조회한다.
     * 2. 결과 List<BoardVo>을(를) 리턴한다.
     * 
     * @param  boardVo 보드 BoardVo
     * @return 보드 목록 List<BoardVo>
     * @throws Exception
     */
	public List<BoardVo> selectListBoard(BoardVo boardVo) throws Exception {
		List<BoardVo> list = boardDAO.selectListBoard(boardVo);	
	
		return list;
	}

    /**
     * 조회한 보드 전체 카운트
     *
     * @process
     * 1. 보드 조회하여 전체 카운트를 리턴한다.
     * 
     * @param  boardVo 보드 BoardVo
     * @return 보드 목록 전체 카운트
     * @throws Exception
     */
	public long selectListCountBoard(BoardVo boardVo) throws Exception {
		return boardDAO.selectListCountBoard(boardVo);
	}

    /**
     * 보드를 상세 조회한다.
     *
     * @process
     * 1. 보드를 상세 조회한다.
     * 2. 결과 BoardVo을(를) 리턴한다.
     * 
     * @param  boardVo 보드 BoardVo
     * @return 단건 조회 결과
     * @throws Exception
     */
	public BoardVo selectBoard(BoardVo boardVo) throws Exception {
		BoardVo resultVO = boardDAO.selectBoard(boardVo);			
        
        return resultVO;
	}

    /**
     * 보드를 등록 처리 한다.
     *
     * @process
     * 1. 보드를 등록 처리 한다.
     * 
     * @param  boardVo 보드 BoardVo
     * @return 번호
     * @throws Exception
     */
	public int insertBoard(BoardVo boardVo) throws Exception {
		int result = boardDAO.insertBoard(boardVo);
		
		// 보드 생성 시 캐시 업데이트는 BoardController에서 처리하므로 여기서는 생략
		// (중복 처리 방지 및 성능 최적화)
		
		return result;	
	}
	
    /**
     * 보드를 갱신 처리 한다.
     *
     * @process
     * 1. 보드를 갱신 처리 한다.
     * 
     * @param  boardVo 보드 BoardVo
     * @return 번호
     * @throws Exception
     */
	public int updateBoard(BoardVo boardVo) throws Exception {
		int result = boardDAO.updateBoard(boardVo);
		
		// 보드 정보 갱신 시 Redis 캐시 무효화
		if (result > 0 && boardVo.getProjectId() != null && kanbanRedisService.isRedisConnected()) {
			try {
				kanbanRedisService.invalidateProjectCache(boardVo.getProjectId());
				System.out.println("🗑️ 보드 정보 갱신으로 인한 프로젝트 캐시 무효화: " + boardVo.getProjectId());
			} catch (Exception e) {
				System.err.println("❌ 캐시 무효화 실패: " + e.getMessage());
			}
		}
		
		return result;	   		
	}

    /**
     * 보드를 삭제 처리 한다.
     *
     * @process
     * 1. 보드를 삭제 처리 한다.
     * 
     * @param  boardVo 보드 BoardVo
     * @return 번호
     * @throws Exception
     */
	public int deleteBoard(BoardVo boardVo) throws Exception {
		int result = boardDAO.deleteBoard(boardVo);
		
		// 보드 삭제 시 캐시 업데이트는 BoardController에서 처리하므로 여기서는 생략
		// (중복 처리 방지 및 성능 최적화)
		
		return result;
	}
	
	/**
     * 프로젝트 ID 기준으로 보드 목록을 조회한다. (칸반 보드용)
     * Redis 캐싱을 통해 성능 최적화
     *
     * @process
     * 1. Redis 캐시에서 프로젝트 보드 목록 조회 시도
     * 2. 캐시에 없으면 데이터베이스에서 조회 후 캐시에 저장
     * 3. 결과 List<BoardVo>을(를) 리턴한다.
     * 
     * @param  projectId 프로젝트 ID
     * @return 보드 목록 List<BoardVo>
     * @throws Exception
     */
	@SuppressWarnings("unchecked")
	public List<BoardVo> selectBoardsByProject(String projectId) throws Exception {
		System.out.println("BoardServiceImpl.selectBoardsByProject - projectId: " + projectId);
		
		// 1. Redis 캐시에서 프로젝트 보드 목록 조회
		if (projectId != null && kanbanRedisService.isRedisConnected()) {
			try {
				List<java.util.Map<String, Object>> cachedBoards = kanbanRedisService.getProjectBoardsFromCache(projectId);
				
				if (cachedBoards != null && !cachedBoards.isEmpty()) {
					// 캐시된 데이터를 BoardVo 리스트로 변환
					List<BoardVo> boardList = new java.util.ArrayList<>();
					for (java.util.Map<String, Object> boardMap : cachedBoards) {
						BoardVo board = convertMapToBoardVo(boardMap);
						boardList.add(board);
					}
					
					System.out.println("✅ Redis 캐시에서 보드 목록 조회 성공: " + projectId + " (" + boardList.size() + "개)");
					return boardList;
				}
			} catch (Exception e) {
				System.err.println("❌ Redis 캐시 조회 실패, DB 조회로 대체: " + e.getMessage());
			}
		}
		
		// 2. 캐시에 없거나 Redis 연결 실패 시 DB에서 조회
		List<BoardVo> list = boardDAO.selectBoardsByProject(projectId);
		System.out.println("BoardServiceImpl - 조회된 보드 개수: " + (list != null ? list.size() : 0));
		
		// 3. DB 조회 결과를 Redis 캐시에 저장
		if (projectId != null && kanbanRedisService.isRedisConnected() && list != null && !list.isEmpty()) {
			try {
				// BoardVo 리스트를 Map 리스트로 변환하여 캐시에 저장
				List<java.util.Map<String, Object>> boardMapList = new java.util.ArrayList<>();
				for (BoardVo board : list) {
					java.util.Map<String, Object> boardMap = convertBoardVoToMap(board);
					boardMapList.add(boardMap);
				}
				
				kanbanRedisService.cacheProjectBoards(projectId, boardMapList);
				System.out.println("🔧 DB 조회 결과를 Redis 캐시에 저장: " + projectId + " (" + list.size() + "개)");
			} catch (Exception e) {
				System.err.println("❌ Redis 캐시 저장 실패: " + e.getMessage());
			}
		}
		
		return list;
	}
	
	/**
	 * BoardVo를 Map으로 변환 (Redis 캐시 저장용)
	 */
	private java.util.Map<String, Object> convertBoardVoToMap(BoardVo boardVo) {
		java.util.Map<String, Object> boardMap = new java.util.HashMap<>();
		
		boardMap.put("boardId", boardVo.getBoardId());
		boardMap.put("boardTitle", boardVo.getBoardTitle());
		boardMap.put("projectId", boardVo.getProjectId());
		
		return boardMap;
	}
	
	/**
	 * Map을 BoardVo로 변환 (Redis 캐시 조회용)
	 */
	private BoardVo convertMapToBoardVo(java.util.Map<String, Object> boardMap) {
		BoardVo boardVo = new BoardVo();
		
		boardVo.setBoardId((String) boardMap.get("boardId"));
		boardVo.setBoardTitle((String) boardMap.get("boardTitle"));
		boardVo.setProjectId((String) boardMap.get("projectId"));
		
		return boardVo;
	}
	
}
