package com.demo.proworks.dept.service;

import java.util.List;

import com.demo.proworks.dept.vo.DeptVo;

/**  
 * @subject     : 부서 정보 관련 처리를 담당하는 인터페이스
 * @description : 부서 정보 관련 처리를 담당하는 인터페이스
 * @author      : 남기윤
 * @since       : 2025/06/10
 * @modification
 * ===========================================================
 * DATE				AUTHOR				DESC
 * ===========================================================
 * 2025/06/10			 남기윤	 		최초 생성
 * 
 */
public interface DeptService {
	
    /**
     * 부서 정보 페이징 처리하여 목록을 조회한다.
     *
     * @param  deptVo 부서 정보 DeptVo
     * @return 부서 정보 목록 List<DeptVo>
     * @throws Exception
     */
	public List<DeptVo> selectListDept(DeptVo deptVo) throws Exception;
	
    /**
     * 조회한 부서 정보 전체 카운트
     * 
     * @param  deptVo 부서 정보 DeptVo
     * @return 부서 정보 목록 전체 카운트
     * @throws Exception
     */
	public long selectListCountDept(DeptVo deptVo) throws Exception;
	
    /**
     * 부서 정보를 상세 조회한다.
     *
     * @param  deptVo 부서 정보 DeptVo
     * @return 단건 조회 결과
     * @throws Exception
     */
	public DeptVo selectDept(DeptVo deptVo) throws Exception;
		
    /**
     * 부서 정보를 등록 처리 한다.
     *
     * @param  deptVo 부서 정보 DeptVo
     * @return 번호
     * @throws Exception
     */
	public int insertDept(DeptVo deptVo) throws Exception;
	
    /**
     * 부서 정보를 갱신 처리 한다.
     *
     * @param  deptVo 부서 정보 DeptVo
     * @return 번호
     * @throws Exception
     */
	public int updateDept(DeptVo deptVo) throws Exception;
	
    /**
     * 부서 정보를 삭제 처리 한다.
     *
     * @param  deptVo 부서 정보 DeptVo
     * @return 번호
     * @throws Exception
     */
	public int deleteDept(DeptVo deptVo) throws Exception;
	
}
