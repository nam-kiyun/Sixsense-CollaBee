package com.demo.proworks.emp.dao;

import org.springframework.stereotype.Repository;

import com.demo.proworks.cmmn.dao.ProworksDefaultAbstractDAO;
import com.demo.proworks.emp.vo.EmpVo;
import com.inswave.elfw.exception.ElException;

/**
 * @subject		: 데모용 사원 첨부파일 정보 관련 처리를 담당하는 DAO
 * @description	: 데모용 사원 첨부파일 정보 관련 처리를 담당하는 DAO
 * @author		: 개발팀
 * @since 		: 2025/05/19
 * @modification
 * ===========================================================
 * DATE				AUTHOR				NOTE
 * ===========================================================
 * 2025/05/19		샘플개발팀			최초 생성
 * 
 */
@Repository("empFileDAO")
public class EmpFileDAO extends ProworksDefaultAbstractDAO {
	
	/**
	 * 사원 첨부파일 정보를 등록 처리한다.
	 * @param empVo 사원 정보 EmpVo
	 * @return 처리 건수 int
	 * @throws ElException
	 */
    public int insertEmpFile(EmpVo empVo) throws ElException {    	
        return insert("com.demo.proworks.emp.insertEmpFile", empVo);
    }
    
    /**
     * 사원 첨부파일 정보를 갱신 처리한다.
     * @param empVo 사원 정보 EmpVo
     * @return 처리 건수 int
     * @throws ElException
     */
    public int updateEmpFile(EmpVo empVo) throws ElException {
        return update("com.demo.proworks.emp.updateEmpFile", empVo);
    }
    
    /**
     * 사원 첨부파일 정보를 삭제 처리한다.
     * @param empVo 사원 정보 EmpVo
     * @return 처리 건수 int
     * @throws ElException
     */
    public int deleteEmpFile(EmpVo empVo) throws ElException {
        return delete("com.demo.proworks.emp.deleteEmpFile", empVo);
    }
    
    /**
     * 사원 첨부파일 정보를 상세 조회한다.
     * @param empVo 사원 정보 EmpVo
     * @return 사원 정보 EmpVo
     * @throws ElException
     */
    public EmpVo selectEmpFile(EmpVo empVo) throws ElException {
        return (EmpVo) selectByPk("com.demo.proworks.emp.selectEmpFile", empVo);
    }

}
