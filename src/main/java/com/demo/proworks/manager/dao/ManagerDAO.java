package com.demo.proworks.manager.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.inswave.elfw.exception.ElException;
import com.demo.proworks.manager.vo.ManagerVo;
import com.demo.proworks.manager.dao.ManagerDAO;

/**  
 * @subject     : 업무 담당자 정보 관련 처리를 담당하는 DAO
 * @description : 업무 담당자 정보 관련 처리를 담당하는 DAO
 * @author      : 국다인
 * @since       : 2025/07/01
 * @modification
 * ===========================================================
 * DATE				AUTHOR				DESC
 * ===========================================================
 * 2025/07/01			 국다인	 		최초 생성
 * 
 */
@Repository("managerDAO")
public class ManagerDAO extends com.demo.proworks.cmmn.dao.ProworksDefaultAbstractDAO {

	/**
     * 업무 담당자 정보 상세 조회한다.
     *  
     * @param  ManagerVo 업무 담당자 정보
     * @return ManagerVo 업무 담당자 정보
     * @throws ElException
     */
    public List<ManagerVo> selectManagerByTaskId(ManagerVo vo) throws ElException {
        return (List<ManagerVo>)list("com.demo.proworks.manager.selectManagerByTaskId", vo);
    }


    /**
     * 업무 담당자 정보 상세 조회한다.
     *  
     * @param  ManagerVo 업무 담당자 정보
     * @return ManagerVo 업무 담당자 정보
     * @throws ElException
     */
    public ManagerVo selectManager(ManagerVo vo) throws ElException {
        return (ManagerVo) selectByPk("com.demo.proworks.manager.selectManager", vo);
    }

    /**
     * 페이징을 처리하여 업무 담당자 정보 목록조회를 한다.
     *  
     * @param  ManagerVo 업무 담당자 정보
     * @return List<ManagerVo> 업무 담당자 정보
     * @throws ElException
     */
    public List<ManagerVo> selectListManager(ManagerVo vo) throws ElException {      	
        return (List<ManagerVo>)list("com.demo.proworks.manager.selectListManager", vo);
    }

    /**
     * 업무 담당자 정보 목록 조회의 전체 카운트를 조회한다.
     *  
     * @param  ManagerVo 업무 담당자 정보
     * @return 업무 담당자 정보 조회의 전체 카운트
     * @throws ElException
     */
    public long selectListCountManager(ManagerVo vo)  throws ElException{               
        return (Long)selectByPk("com.demo.proworks.manager.selectListCountManager", vo);
    }
        
    /**
     * 업무 담당자 정보를 등록한다.
     *  
     * @param  ManagerVo 업무 담당자 정보
     * @return 번호
     * @throws ElException
     */
    public int insertManager(ManagerVo vo) throws ElException { 
    	System.out.println("매니저 부분 " + vo);
        return insert("com.demo.proworks.manager.insertManager", vo);
    }

    /**
     * 업무 담당자 정보를 갱신한다.
     *  
     * @param  ManagerVo 업무 담당자 정보
     * @return 번호
     * @throws ElException
     */
    public int updateManager(ManagerVo vo) throws ElException {
        return update("com.demo.proworks.manager.updateManager", vo);
    }

    /**
     * 업무 담당자 정보를 삭제한다.
     *  
     * @param  ManagerVo 업무 담당자 정보
     * @return 번호
     * @throws ElException
     */
    public int deleteManager(ManagerVo vo) throws ElException {
        return delete("com.demo.proworks.manager.deleteManager", vo);
    }
    
    public int deleteManagerByTaskId(ManagerVo vo) throws ElException {
        return delete("com.demo.proworks.manager.deleteManagerByTaskId", vo);
    }

}
