package com.demo.proworks.emp.service.impl;

import java.io.IOException;
import java.io.InputStream;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.inswave.elfw.exception.ElException;

import com.demo.proworks.emp.dao.EmpFileDAO;
import com.demo.proworks.emp.service.EmpFileService;
import com.demo.proworks.emp.vo.EmpVo;

/**
 * @subject		: 데모용 사원 첨부파일 정보 관련 처리를 담당하는 서비스 구현체
 * @description	: 데모용 사원 첨부파일 정보 관련 처리를 담당하는 서비스 구현체
 * @author		: 개발팀
 * @since 		: 2025/05/19
 * @modification
 * ===========================================================
 * DATE				AUTHOR				NOTE
 * ===========================================================
 * 2025/05/19		샘플개발팀			최초 생성
 * 
 */
@Service("empFileServiceImpl")
public class EmpFileServiceImpl implements EmpFileService {
	
    @Resource(name="empFileDAO")
    private EmpFileDAO empFileDAO;
    
	/**
	 * 사원 첨부파일 정보를 조회한다.
	 * @process
	 * 1. 사원 첨부파일 정보를 조회한다.
	 * 2. 조회 결과를 EmpVo 타입으로 리턴한다.
	 * @param empVo 사원 정보 EmpVo
	 * @return 사원 정보 EmpVo
	 * @throws ElException
	 */
	public EmpVo selectEmpFile(EmpVo empVo) throws ElException {		
        EmpVo retVo = (EmpVo) empVo.clone();
        retVo = empFileDAO.selectEmpFile(retVo);
        return retVo;
	}

	/**
	 * 사원 첨부파일 정보를 등록한다.
	 * @process
	 * 1. 사원 첨부파일 정보를 등록한다.
	 * 2. 등록 건수 결과를 int 타입으로 리턴한다.
	 * @param empVo 사원 정보 EmpVo
	 * @return 처리 건수 int
	 * @throws ElException
	 */
	public int insertEmpFile(EmpVo empVo) throws ElException {
		return empFileDAO.insertEmpFile(empVo);
	}

	/**
	 * 사원 첨부파일 정보를 갱신한다.
	 * @process
	 * 1. 사원 첨부파일 정보를 갱신한다.
	 * 2. 갱신 건수 결과를 int 타입으로 리턴한다.
	 * @param empVo 사원 정보 EmpVo
	 * @return 처리 건수 int
	 * @throws Exception
	 */
	public int updateEmpFile(EmpVo empVo) throws ElException {				
		return empFileDAO.updateEmpFile(empVo);				
	}

	/**
	 * 사원 첨부파일 정보를 삭제한다.
	 * @process
	 * 1. 사원 첨부파일 정보를 삭제한다.
	 * 2. 삭제 건수 결과를 int 타입으로 리턴한다.
	 * @param empVo 사원 정보 EmpVo
	 * @return 처리 건수 inst
	 * @throws Exception
	 */
	public int deleteEmpFile(EmpVo empVo) throws ElException {
		return empFileDAO.deleteEmpFile(empVo);
	}
	
	/**
	 * 사원 첨부파일을 업로드한다.
	 * @process
	 * 1. 기존 사원 첨부파일 정보를 삭제한다.
	 * 2. 신규 사원 첨부파일 정보를 등록한다.
	 * @param empVo 사원 정보 EmpVo
	 * @throws Exception
	 */
	public void uploadEmpFile(EmpVo empVo) throws Exception {
		if(empVo.getInputFileData() != null && empVo.getInputFileData().getSize() > 0) {
            EmpVo retEmpVo = (EmpVo) empVo.clone();
            retEmpVo = getEmpVo(empVo);
            empFileDAO.deleteEmpFile(retEmpVo);
            empFileDAO.insertEmpFile(retEmpVo);
        }
	}

	/**
	 * EmpVo의 MultipartFile을 Stream으로 Read하여 Byte 배열 데이터로 담는다.
	 * @param empVo 사원 정보 EmpVo
	 * @return 사원 정보 EmpVo
	 * @throws ElException
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
