package com.demo.proworks.emp.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.inswave.elfw.exception.ElException;

import com.demo.proworks.emp.vo.DeptVo;
import com.demo.proworks.emp.vo.EmpVo;

/**
 * @subject		: 데모용 사원 정보 관련 처리를 하는 서비스 인터페이스
 * @description	: 데모용 사원 정보 관련 처리를 하는 서비스 인터페이스
 * @author		: 개발팀
 * @since 		: 2025/05/19
 * @modification
 * ===========================================================
 * DATE				AUTHOR				NOTE
 * ===========================================================
 * 2025/05/19		샘플개발팀			최초 생성
 * 
 */
public interface EmpService {
	
	/**
	 * 페이징을 처리하여 사원 정보 목록을 조회한다.
	 * @param empVo 사원 정보 EmpVo
	 * @return 사원 정보 목록 List<EmpVo>
	 * @throws ElException
	 */
	public List<EmpVo> selectListEmp(EmpVo empVo) throws ElException;
	
	/**
	 * 사원 정보 목록 건수를 조회한다.
	 * @param empVo 사원 정보 EmpVo
	 * @return 사원 정보 목록 건수 long
	 * @throws ElException
	 */
	public long selectListCountEmp(EmpVo empVo) throws ElException;
	
	/**
	 * 사원 정보를 상세 조회한다.
	 * @param empVo 사원 정보 EmpVo
	 * @return 사원 정보 EmpVo
	 * @throws ElException
	 */
	public EmpVo selectEmp(EmpVo empVo) throws ElException;
		
	/**
	 * 사원 정보를 등록 처리한다.
	 * @param empVo 사원 정보 EmpVo
	 * @return 처리 건수 int
	 * @throws Exception
	 */
	public int insertEmp(EmpVo empVo) throws Exception;
	
	/**
	 * 사원 정보를 갱신 처리한다.
	 * @param empVo 사원 정보 EmpVo
	 * @return 처리 건수 int
	 * @throws Exception
	 */
	public int updateEmp(EmpVo empVo) throws Exception;
	
	/**
	 * 사원 정보를 삭제 처리한다.
	 * @param empVo 사원 정보 EmpVo
	 * @return 처리 건수 int
	 * @throws Exception
	 */
	public int deleteEmp(EmpVo empVo) throws Exception;
	
	/**
	 * 부서 정보 목록을 조회한다.
	 * @param searchKeyHm 조건 검색 정보 HashMap 
	 * @return 부서 정보 목록 ArrayList<DeptVo>
	 * @throws Exception
	 */
	public ArrayList<DeptVo> selectListDept(HashMap searchKeyHm) throws Exception;
	
}
