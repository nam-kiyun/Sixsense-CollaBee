package com.demo.proworks.board.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.demo.proworks.board.service.BoardService;
import com.demo.proworks.board.vo.BoardVo;
import com.demo.proworks.board.dao.BoardDAO;

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
	
	@Resource(name = "messageSource")
	private MessageSource messageSource;

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
		return boardDAO.insertBoard(boardVo);	
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
		return boardDAO.updateBoard(boardVo);	   		
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
		return boardDAO.deleteBoard(boardVo);
	}
	
}
