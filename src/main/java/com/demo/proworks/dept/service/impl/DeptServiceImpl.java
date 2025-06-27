package com.demo.proworks.dept.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.demo.proworks.dept.service.DeptService;
import com.demo.proworks.dept.vo.DeptVo;
import com.demo.proworks.dept.dao.DeptDAO;

/**  
 * @subject     : 부서 정보 관련 처리를 담당하는 ServiceImpl
 * @description	: 부서 정보 관련 처리를 담당하는 ServiceImpl
 * @author      : 남기윤
 * @since       : 2025/06/10
 * @modification
 * ===========================================================
 * DATE				AUTHOR				DESC
 * ===========================================================
 * 2025/06/10			 남기윤	 		최초 생성
 * 
 */
@Service("deptServiceImpl")
public class DeptServiceImpl implements DeptService {

    @Resource(name="deptDAO")
    private DeptDAO deptDAO;
	
	@Resource(name = "messageSource")
	private MessageSource messageSource;

    /**
     * 부서 정보 목록을 조회합니다.
     *
     * @process
     * 1. 부서 정보 페이징 처리하여 목록을 조회한다.
     * 2. 결과 List<DeptVo>을(를) 리턴한다.
     * 
     * @param  deptVo 부서 정보 DeptVo
     * @return 부서 정보 목록 List<DeptVo>
     * @throws Exception
     */
	public List<DeptVo> selectListDept(DeptVo deptVo) throws Exception {
		List<DeptVo> list = deptDAO.selectListDept(deptVo);	
	
		return list;
	}

    /**
     * 조회한 부서 정보 전체 카운트
     *
     * @process
     * 1. 부서 정보 조회하여 전체 카운트를 리턴한다.
     * 
     * @param  deptVo 부서 정보 DeptVo
     * @return 부서 정보 목록 전체 카운트
     * @throws Exception
     */
	public long selectListCountDept(DeptVo deptVo) throws Exception {
		return deptDAO.selectListCountDept(deptVo);
	}

    /**
     * 부서 정보를 상세 조회한다.
     *
     * @process
     * 1. 부서 정보를 상세 조회한다.
     * 2. 결과 DeptVo을(를) 리턴한다.
     * 
     * @param  deptVo 부서 정보 DeptVo
     * @return 단건 조회 결과
     * @throws Exception
     */
	public DeptVo selectDept(DeptVo deptVo) throws Exception {
		DeptVo resultVO = deptDAO.selectDept(deptVo);			
        
        return resultVO;
	}

    /**
     * 부서 정보를 등록 처리 한다.
     *
     * @process
     * 1. 부서 정보를 등록 처리 한다.
     * 
     * @param  deptVo 부서 정보 DeptVo
     * @return 번호
     * @throws Exception
     */
	public int insertDept(DeptVo deptVo) throws Exception {
		return deptDAO.insertDept(deptVo);	
	}
	
    /**
     * 부서 정보를 갱신 처리 한다.
     *
     * @process
     * 1. 부서 정보를 갱신 처리 한다.
     * 
     * @param  deptVo 부서 정보 DeptVo
     * @return 번호
     * @throws Exception
     */
	public int updateDept(DeptVo deptVo) throws Exception {				
		return deptDAO.updateDept(deptVo);	   		
	}

    /**
     * 부서 정보를 삭제 처리 한다.
     *
     * @process
     * 1. 부서 정보를 삭제 처리 한다.
     * 
     * @param  deptVo 부서 정보 DeptVo
     * @return 번호
     * @throws Exception
     */
	public int deleteDept(DeptVo deptVo) throws Exception {
		return deptDAO.deleteDept(deptVo);
	}
	
}
