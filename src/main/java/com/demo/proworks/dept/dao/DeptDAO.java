package com.demo.proworks.dept.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.inswave.elfw.exception.ElException;
import com.demo.proworks.dept.vo.DeptVo;
import com.demo.proworks.dept.dao.DeptDAO;

/**  
 * @subject     : 부서 정보 관련 처리를 담당하는 DAO
 * @description : 부서 정보 관련 처리를 담당하는 DAO
 * @author      : 남기윤
 * @since       : 2025/06/10
 * @modification
 * ===========================================================
 * DATE				AUTHOR				DESC
 * ===========================================================
 * 2025/06/10			 남기윤	 		최초 생성
 * 
 */
@Repository("deptDAO")
public class DeptDAO extends com.demo.proworks.cmmn.dao.ProworksDefaultAbstractDAO {

    /**
     * 부서 정보 상세 조회한다.
     *  
     * @param  DeptVo 부서 정보
     * @return DeptVo 부서 정보
     * @throws ElException
     */
    public DeptVo selectDept(DeptVo vo) throws ElException {
        return (DeptVo) selectByPk("com.demo.proworks.dept.selectDept", vo);
    }

    /**
     * 페이징을 처리하여 부서 정보 목록조회를 한다.
     *  
     * @param  DeptVo 부서 정보
     * @return List<DeptVo> 부서 정보
     * @throws ElException
     */
    public List<DeptVo> selectListDept(DeptVo vo) throws ElException {      	
        return (List<DeptVo>)list("com.demo.proworks.dept.selectListDept", vo);
    }

    /**
     * 부서 정보 목록 조회의 전체 카운트를 조회한다.
     *  
     * @param  DeptVo 부서 정보
     * @return 부서 정보 조회의 전체 카운트
     * @throws ElException
     */
    public long selectListCountDept(DeptVo vo)  throws ElException{               
        return (Long)selectByPk("com.demo.proworks.dept.selectListCountDept", vo);
    }
        
    /**
     * 부서 정보를 등록한다.
     *  
     * @param  DeptVo 부서 정보
     * @return 번호
     * @throws ElException
     */
    public int insertDept(DeptVo vo) throws ElException {    	
        return insert("com.demo.proworks.dept.insertDept", vo);
    }

    /**
     * 부서 정보를 갱신한다.
     *  
     * @param  DeptVo 부서 정보
     * @return 번호
     * @throws ElException
     */
    public int updateDept(DeptVo vo) throws ElException {
        return update("com.demo.proworks.dept.updateDept", vo);
    }

    /**
     * 부서 정보를 삭제한다.
     *  
     * @param  DeptVo 부서 정보
     * @return 번호
     * @throws ElException
     */
    public int deleteDept(DeptVo vo) throws ElException {
        return delete("com.demo.proworks.dept.deleteDept", vo);
    }

}
