package com.demo.proworks.board.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.inswave.elfw.exception.ElException;
import com.demo.proworks.board.vo.BoardVo;
import com.demo.proworks.board.dao.BoardDAO;

/**  
 * @subject     : 보드 관련 처리를 담당하는 DAO
 * @description : 보드 관련 처리를 담당하는 DAO
 * @author      : 국다인
 * @since       : 2025/07/01
 * @modification
 * ===========================================================
 * DATE				AUTHOR				DESC
 * ===========================================================
 * 2025/07/01			 국다인	 		최초 생성
 * 
 */
@Repository("boardDAO")
public class BoardDAO extends com.demo.proworks.cmmn.dao.ProworksDefaultAbstractDAO {

    /**
     * 보드 상세 조회한다.
     *  
     * @param  BoardVo 보드
     * @return BoardVo 보드
     * @throws ElException
     */
    public BoardVo selectBoard(BoardVo vo) throws ElException {
        return (BoardVo) selectByPk("com.demo.proworks.board.selectBoard", vo);
    }

    /**
     * 페이징을 처리하여 보드 목록조회를 한다.
     *  
     * @param  BoardVo 보드
     * @return List<BoardVo> 보드
     * @throws ElException
     */
    public List<BoardVo> selectListBoard(BoardVo vo) throws ElException {      	
        return (List<BoardVo>)list("com.demo.proworks.board.selectListBoard", vo);
    }

    /**
     * 보드 목록 조회의 전체 카운트를 조회한다.
     *  
     * @param  BoardVo 보드
     * @return 보드 조회의 전체 카운트
     * @throws ElException
     */
    public long selectListCountBoard(BoardVo vo)  throws ElException{               
        return (Long)selectByPk("com.demo.proworks.board.selectListCountBoard", vo);
    }
        
    /**
     * 보드를 등록한다.
     *  
     * @param  BoardVo 보드
     * @return 번호
     * @throws ElException
     */
    public int insertBoard(BoardVo vo) throws ElException {    	
        return insert("com.demo.proworks.board.insertBoard", vo);
    }

    /**
     * 보드를 갱신한다.
     *  
     * @param  BoardVo 보드
     * @return 번호
     * @throws ElException
     */
    public int updateBoard(BoardVo vo) throws ElException {
        return update("com.demo.proworks.board.updateBoard", vo);
    }

    /**
     * 보드를 삭제한다.
     *  
     * @param  BoardVo 보드
     * @return 번호
     * @throws ElException
     */
    public int deleteBoard(BoardVo vo) throws ElException {
        return delete("com.demo.proworks.board.deleteBoard", vo);
    }

}
