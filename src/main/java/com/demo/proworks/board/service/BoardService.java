package com.demo.proworks.board.service;

import java.util.List;

import com.demo.proworks.board.vo.BoardVo;

/**  
 * @subject     : 보드 관련 처리를 담당하는 인터페이스
 * @description : 보드 관련 처리를 담당하는 인터페이스
 * @author      : 국다인
 * @since       : 2025/07/01
 * @modification
 * ===========================================================
 * DATE				AUTHOR				DESC
 * ===========================================================
 * 2025/07/01			 국다인	 		최초 생성
 * 
 */
public interface BoardService {
	
    /**
     * 보드 페이징 처리하여 목록을 조회한다.
     *
     * @param  boardVo 보드 BoardVo
     * @return 보드 목록 List<BoardVo>
     * @throws Exception
     */
	public List<BoardVo> selectListBoard(BoardVo boardVo) throws Exception;
	
    /**
     * 조회한 보드 전체 카운트
     * 
     * @param  boardVo 보드 BoardVo
     * @return 보드 목록 전체 카운트
     * @throws Exception
     */
	public long selectListCountBoard(BoardVo boardVo) throws Exception;
	
    /**
     * 보드를 상세 조회한다.
     *
     * @param  boardVo 보드 BoardVo
     * @return 단건 조회 결과
     * @throws Exception
     */
	public BoardVo selectBoard(BoardVo boardVo) throws Exception;
		
    /**
     * 보드를 등록 처리 한다.
     *
     * @param  boardVo 보드 BoardVo
     * @return 번호
     * @throws Exception
     */
	public int insertBoard(BoardVo boardVo) throws Exception;
	
    /**
     * 보드를 갱신 처리 한다.
     *
     * @param  boardVo 보드 BoardVo
     * @return 번호
     * @throws Exception
     */
	public int updateBoard(BoardVo boardVo) throws Exception;
	
    /**
     * 보드를 삭제 처리 한다.
     *
     * @param  boardVo 보드 BoardVo
     * @return 번호
     * @throws Exception
     */
	public int deleteBoard(BoardVo boardVo) throws Exception;
	
	/**
     * 프로젝트 ID 기준으로 보드 목록을 조회한다. (칸반 보드용)
     *
     * @param  projectId 프로젝트 ID
     * @return 보드 목록 List<BoardVo>
     * @throws Exception
     */
	public List<BoardVo> selectBoardsByProject(String projectId) throws Exception;
	
}
