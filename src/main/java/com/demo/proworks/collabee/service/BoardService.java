package com.demo.proworks.collabee.service;

import java.util.List;

import com.demo.proworks.collabee.vo.BoardVo;

/**  
 * @subject     : 칸반보드의 보드 관련 처리를 담당하는 인터페이스
 * @description : 칸반보드의 보드 관련 처리를 담당하는 인터페이스
 * @author      : 남기윤
 * @since       : 2025/07/01
 * @modification
 * ===========================================================
 * DATE				AUTHOR				DESC
 * ===========================================================
 * 2025/07/01			 남기윤	 		최초 생성
 * 
 */
public interface BoardService {
	
    /**
     * 칸반보드의 보드 페이징 처리하여 목록을 조회한다.
     *
     * @param  boardVo 칸반보드의 보드 BoardVo
     * @return 칸반보드의 보드 목록 List<BoardVo>
     * @throws Exception
     */
	public List<BoardVo> selectListBoard(BoardVo boardVo) throws Exception;
	
    /**
     * 조회한 칸반보드의 보드 전체 카운트
     * 
     * @param  boardVo 칸반보드의 보드 BoardVo
     * @return 칸반보드의 보드 목록 전체 카운트
     * @throws Exception
     */
	public long selectListCountBoard(BoardVo boardVo) throws Exception;
	
    /**
     * 칸반보드의 보드를 상세 조회한다.
     *
     * @param  boardVo 칸반보드의 보드 BoardVo
     * @return 단건 조회 결과
     * @throws Exception
     */
	public BoardVo selectBoard(BoardVo boardVo) throws Exception;
		
    /**
     * 칸반보드의 보드를 등록 처리 한다.
     *
     * @param  boardVo 칸반보드의 보드 BoardVo
     * @return 번호
     * @throws Exception
     */
	public int insertBoard(BoardVo boardVo) throws Exception;
	
    /**
     * 칸반보드의 보드를 갱신 처리 한다.
     *
     * @param  boardVo 칸반보드의 보드 BoardVo
     * @return 번호
     * @throws Exception
     */
	public int updateBoard(BoardVo boardVo) throws Exception;
	
    /**
     * 칸반보드의 보드를 삭제 처리 한다.
     *
     * @param  boardVo 칸반보드의 보드 BoardVo
     * @return 번호
     * @throws Exception
     */
	public int deleteBoard(BoardVo boardVo) throws Exception;
	
}
