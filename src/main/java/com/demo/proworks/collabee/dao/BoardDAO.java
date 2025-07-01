package com.demo.proworks.collabee.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.inswave.elfw.exception.ElException;
import com.demo.proworks.collabee.vo.BoardVo;
import com.demo.proworks.collabee.dao.BoardDAO;

/**  
 * @subject     : 칸반보드의 보드 관련 처리를 담당하는 DAO
 * @description : 칸반보드의 보드 관련 처리를 담당하는 DAO
 * @author      : 남기윤
 * @since       : 2025/07/01
 * @modification
 * ===========================================================
 * DATE				AUTHOR				DESC
 * ===========================================================
 * 2025/07/01			 남기윤	 		최초 생성
 * 
 */
@Repository("boardDAO")
public class BoardDAO extends com.demo.proworks.cmmn.dao.ProworksDefaultAbstractDAO {

    /**
     * 칸반보드의 보드 상세 조회한다.
     *  
     * @param  BoardVo 칸반보드의 보드
     * @return BoardVo 칸반보드의 보드
     * @throws ElException
     */
    public BoardVo selectBoard(BoardVo vo) throws ElException {
        return (BoardVo) selectByPk("com.demo.proworks.collabee.selectBoard", vo);
    }

    /**
     * 페이징을 처리하여 칸반보드의 보드 목록조회를 한다.
     *  
     * @param  BoardVo 칸반보드의 보드
     * @return List<BoardVo> 칸반보드의 보드
     * @throws ElException
     */
    public List<BoardVo> selectListBoard(BoardVo vo) throws ElException {      	
        return (List<BoardVo>)list("com.demo.proworks.collabee.selectListBoard", vo);
    }

    /**
     * 칸반보드의 보드 목록 조회의 전체 카운트를 조회한다.
     *  
     * @param  BoardVo 칸반보드의 보드
     * @return 칸반보드의 보드 조회의 전체 카운트
     * @throws ElException
     */
    public long selectListCountBoard(BoardVo vo)  throws ElException{               
        return (Long)selectByPk("com.demo.proworks.collabee.selectListCountBoard", vo);
    }
        
    /**
     * 칸반보드의 보드를 등록한다.
     *  
     * @param  BoardVo 칸반보드의 보드
     * @return 번호
     * @throws ElException
     */
    public int insertBoard(BoardVo vo) throws ElException {    	
        return insert("com.demo.proworks.collabee.insertBoard", vo);
    }

    /**
     * 칸반보드의 보드를 갱신한다.
     *  
     * @param  BoardVo 칸반보드의 보드
     * @return 번호
     * @throws ElException
     */
    public int updateBoard(BoardVo vo) throws ElException {
        return update("com.demo.proworks.collabee.updateBoard", vo);
    }

    /**
     * 칸반보드의 보드를 삭제한다.
     *  
     * @param  BoardVo 칸반보드의 보드
     * @return 번호
     * @throws ElException
     */
    public int deleteBoard(BoardVo vo) throws ElException {
        return delete("com.demo.proworks.collabee.deleteBoard", vo);
    }

}
