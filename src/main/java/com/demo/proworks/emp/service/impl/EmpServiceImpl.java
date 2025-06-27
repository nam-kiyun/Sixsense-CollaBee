package com.demo.proworks.emp.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.inswave.elfw.exception.ElException;

import com.demo.proworks.emp.dao.EmpDAO;
import com.demo.proworks.emp.service.EmpFileService;
import com.demo.proworks.emp.service.EmpService;
import com.demo.proworks.emp.vo.DeptVo;
import com.demo.proworks.emp.vo.EmpVo;

/**
 * @subject		: 데모용 사원 정보 관련 처리를 담당하는 서비스 구현체
 * @description	: 데모용 사원 정보 관련 처리를 담당하는 서비스 구현체
 * @author		: 개발팀
 * @since 		: 2025/05/19
 * @modification
 * ===========================================================
 * DATE				AUTHOR				NOTE
 * ===========================================================
 * 2025/05/19		샘플개발팀			최초 생성
 * 
 */
@Service("empServiceImpl")
public class EmpServiceImpl implements EmpService {
	
    @Resource(name="empDAO")
    private EmpDAO empDAO;
        
    @Resource(name = "empFileServiceImpl")
    private EmpFileService empFileService;
	
    /**
     * 페이징을 처리하여 사원 정보 목록을 조회한다.
     * @process
     * 1. 사원 정보 목록을 조회한다.
     * 2. 조회 결과를 List<EmpVo> 타입으로 리턴한다.
	 * @param empVo 사원 정보 EmpVo
	 * @return 사원 정보 목록 List<EmpVo>
	 * @throws ElException
     */
	public List<EmpVo> selectListEmp(EmpVo empVo) throws ElException {
		List<EmpVo> list = empDAO.selectListEmp(empVo);	
		return list;
	}
	
	/**
	 * 사원 정보 목록 건수를 조회한다.
     * @process
     * 1. 사원 정보 목록 건수를 조회한다.
     * 2. 조회 결과를 long 타입으로 리턴한다.
	 * @param empVo 사원 정보 EmpVo
	 * @return 사원 정보 목록 건수 long
	 * @throws ElException
	 */
	public long selectListCountEmp(EmpVo empVo) throws ElException {
		return empDAO.selectListCountEmp(empVo);
	}

    /**
	 * 사원 정보를 상세 조회한다.
     * @process
     * 1. 사원 정보를 상세 조회한다.
     * 2. 조회 결과를 EmpVo 타입으로 리턴한다.
	 * @param empVo 사원 정보 EmpVo
	 * @return 사원 정보 EmpVo
	 * @throws ElException
     */
	public EmpVo selectEmp(EmpVo empVo) throws ElException {
		EmpVo resultVO = empDAO.selectEmp(empVo);
		
//		if (resultVO == null) {
//			throw new UserException("EL.ERROR.COMM.001"); // 별도의 예외 메시지가 필요한 경우 처리 예시
//		}
		
        return resultVO;
	}

    /**
	 * 사원 정보를 등록 처리한다.
     * @process
     * 1. 사원 정보를 등록 처리한다.
     * 2. 처리 건수를 int 타입으로 리턴한다.
	 * @param empVo 사원 정보 EmpVo
	 * @return 처리 건수 int
	 * @throws ElException
	 * @throws IOException
     */
	public int insertEmp(EmpVo empVo) throws ElException, IOException {
        int iRet = empDAO.insertEmp(empVo);
        
        if(empVo.getInputFileData() != null  && empVo.getInputFileData().getSize() > 0) {
            EmpVo retEmpVo = (EmpVo)empVo.clone();
            retEmpVo = getEmpVo(empVo);
            
            empFileService.insertEmpFile(retEmpVo);
        }
        
        return iRet;
	}
	
    /**
	 * 사원 정보를 갱신 처리한다.
     * @process
     * 1. 사원 정보를 갱신 처리한다.
     * 2. 처리 건수를 int 타입으로 리턴한다.
	 * @param empVo 사원 정보 EmpVo
	 * @return 처리 건수 int
	 * @throws ElException
	 * @throws IOException
     */
	public int updateEmp(EmpVo empVo) throws ElException, IOException {				
        int updRet = empDAO.updateEmp(empVo);
        
        if(empVo.getInputFileData() != null && empVo.getInputFileData().getSize() > 0) {
            EmpVo retEmpVo = (EmpVo)empVo.clone();
            retEmpVo = getEmpVo(empVo);
            
            empFileService.deleteEmpFile(retEmpVo);
            empFileService.insertEmpFile(retEmpVo);
        } else {
        	if(empVo.getFileName() == null || "".equals(empVo.getFileName())) {
        		empFileService.deleteEmpFile(empVo); // 첨부파일 삭제
        	}
        }
        
        return updRet;   		
	}

    /**
	 * 사원 정보를 삭제 처리한다.
     * @process
     * 1. 사원 정보를 삭제 처리한다.
     * 2. 처리 건수를 int 타입으로 리턴한다.
	 * @param empVo 사원 정보 EmpVo
	 * @return 처리 건수 int
	 * @throws ElException
     */
	public int deleteEmp(EmpVo empVo) throws ElException {
		int del = empDAO.deleteEmp(empVo);
		empFileService.deleteEmpFile(empVo);
		
		return del;
	}	
		
    /**
	 * 부서 정보 목록을 조회한다.
     * @process
     * 1. 부서 정보 목록을 조회한다.
     * 2. 조회 결과를 ArrayList<DeptVo> 타입으로 리턴한다.
	 * @param searchKeyHm 조건 검색 정보 HashMap 
	 * @return 부서 정보 목록 ArrayList<DeptVo>
	 * @throws ElException
     */
	public ArrayList<DeptVo> selectListDept(HashMap searchKeyHm) throws ElException {
		return empDAO.selectListDept(searchKeyHm);
	}
	
	/**
	 * EmpVo의 MultipartFile을 Stream으로 Read하여 Byte 배열 데이터로 담는다.
	 * @param empVo 사원 정보 EmpVo
	 * @return 사원 정보 EmpVo
	 * @throws Exception
	 * @throws IOException
	 */
	private EmpVo getEmpVo(EmpVo empVo) throws ElException, IOException { 
		if(empVo.getInputFileData() == null) {
			return null;
		}
		
		long iFileSize = empVo.getInputFileData().getSize();
		
		if(iFileSize > 0) {
			InputStream is = empVo.getInputFileData().getInputStream();  			
			byte[] fileData = new byte[(int) iFileSize];  
			is.read(fileData);
			is.close();
			
			empVo.setFileName(empVo.getInputFileData().getOriginalFilename());
			empVo.setFileData(fileData);	
		}
		
		return empVo;
	}

}
