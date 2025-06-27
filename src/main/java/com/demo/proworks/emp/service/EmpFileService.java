package com.demo.proworks.emp.service;

import com.inswave.elfw.exception.ElException;
import com.demo.proworks.emp.vo.EmpVo;

/**
 * @subject		: 데모용 사원 첨부파일 정보 관련 처리를 담당하는 서비스 인터페이스
 * @description	: 데모용 사원 첨부파일 정보 관련 처리를 담당하는 서비스 인터페이스
 * @author		: 개발팀
 * @since 		: 2025/05/19
 * @modification
 * ===========================================================
 * DATE				AUTHOR				NOTE
 * ===========================================================
 * 2025/05/19		샘플개발팀			최초 생성
 * 
 */
public interface EmpFileService {
		
	/**
	 * 사원 첨부파일 정보를 조회한다.
	 * @param empVo 사원 정보 EmpVo
	 * @return 사원 정보 EmpVo
	 * @throws ElException
	 */
	public EmpVo selectEmpFile(EmpVo empVo) throws ElException;
		
	/**
	 * 사원 첨부파일 정보를 등록한다.
	 * @param empVo 사원 정보 EmpVo
	 * @return 처리 건수 int
	 * @throws ElException
	 */
	public int insertEmpFile(EmpVo empVo) throws ElException;
	
	/**
	 * 사원 첨부파일 정보를 갱신한다.
	 * @param empVo 사원 정보 EmpVo
	 * @return 처리 건수 int
	 * @throws ElException
	 */
	public int updateEmpFile(EmpVo empVo) throws ElException;
	
	/**
	 * 사원 첨부파일 정보를 삭제한다.
	 * @param empVo 사원 정보 EmpVo
	 * @return 처리 건수 inst
	 * @throws ElException
	 */
	public int deleteEmpFile(EmpVo empVo) throws ElException;
	
	/**
	 * 사원 첨부파일을 업로드한다.
	 * @param empVo 사원 정보 EmpVo
	 * @throws Exception
	 */
	public void uploadEmpFile(EmpVo empVo) throws Exception;
	
}
