package com.demo.proworks.collabee.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.demo.proworks.collabee.service.FileSrcService;
import com.demo.proworks.collabee.vo.FileSrcVo;
import com.demo.proworks.collabee.dao.FileSrcDAO;

/**  
 * @subject     : 업로드한 파일 저장 경로 관련 처리를 담당하는 ServiceImpl
 * @description	: 업로드한 파일 저장 경로 관련 처리를 담당하는 ServiceImpl
 * @author      : 남기윤
 * @since       : 2025/07/01
 * @modification
 * ===========================================================
 * DATE				AUTHOR				DESC
 * ===========================================================
 * 2025/07/01			 남기윤	 		최초 생성
 * 
 */
@Service("fileSrcServiceImpl")
public class FileSrcServiceImpl implements FileSrcService {

    @Resource(name="fileSrcDAO")
    private FileSrcDAO fileSrcDAO;
	
	@Resource(name = "messageSource")
	private MessageSource messageSource;

    /**
     * 업로드한 파일 저장 경로 목록을 조회합니다.
     *
     * @process
     * 1. 업로드한 파일 저장 경로 페이징 처리하여 목록을 조회한다.
     * 2. 결과 List<FileSrcVo>을(를) 리턴한다.
     * 
     * @param  fileSrcVo 업로드한 파일 저장 경로 FileSrcVo
     * @return 업로드한 파일 저장 경로 목록 List<FileSrcVo>
     * @throws Exception
     */
	public List<FileSrcVo> selectListFileSrc(FileSrcVo fileSrcVo) throws Exception {
		List<FileSrcVo> list = fileSrcDAO.selectListFileSrc(fileSrcVo);	
	
		return list;
	}

    /**
     * 조회한 업로드한 파일 저장 경로 전체 카운트
     *
     * @process
     * 1. 업로드한 파일 저장 경로 조회하여 전체 카운트를 리턴한다.
     * 
     * @param  fileSrcVo 업로드한 파일 저장 경로 FileSrcVo
     * @return 업로드한 파일 저장 경로 목록 전체 카운트
     * @throws Exception
     */
	public long selectListCountFileSrc(FileSrcVo fileSrcVo) throws Exception {
		return fileSrcDAO.selectListCountFileSrc(fileSrcVo);
	}

    /**
     * 업로드한 파일 저장 경로를 상세 조회한다.
     *
     * @process
     * 1. 업로드한 파일 저장 경로를 상세 조회한다.
     * 2. 결과 FileSrcVo을(를) 리턴한다.
     * 
     * @param  fileSrcVo 업로드한 파일 저장 경로 FileSrcVo
     * @return 단건 조회 결과
     * @throws Exception
     */
	public FileSrcVo selectFileSrc(FileSrcVo fileSrcVo) throws Exception {
		FileSrcVo resultVO = fileSrcDAO.selectFileSrc(fileSrcVo);			
        
        return resultVO;
	}

    /**
     * 업로드한 파일 저장 경로를 등록 처리 한다.
     *
     * @process
     * 1. 업로드한 파일 저장 경로를 등록 처리 한다.
     * 
     * @param  fileSrcVo 업로드한 파일 저장 경로 FileSrcVo
     * @return 번호
     * @throws Exception
     */
	public int insertFileSrc(FileSrcVo fileSrcVo) throws Exception {
		return fileSrcDAO.insertFileSrc(fileSrcVo);	
	}
	
    /**
     * 업로드한 파일 저장 경로를 갱신 처리 한다.
     *
     * @process
     * 1. 업로드한 파일 저장 경로를 갱신 처리 한다.
     * 
     * @param  fileSrcVo 업로드한 파일 저장 경로 FileSrcVo
     * @return 번호
     * @throws Exception
     */
	public int updateFileSrc(FileSrcVo fileSrcVo) throws Exception {				
		return fileSrcDAO.updateFileSrc(fileSrcVo);	   		
	}

    /**
     * 업로드한 파일 저장 경로를 삭제 처리 한다.
     *
     * @process
     * 1. 업로드한 파일 저장 경로를 삭제 처리 한다.
     * 
     * @param  fileSrcVo 업로드한 파일 저장 경로 FileSrcVo
     * @return 번호
     * @throws Exception
     */
	public int deleteFileSrc(FileSrcVo fileSrcVo) throws Exception {
		return fileSrcDAO.deleteFileSrc(fileSrcVo);
	}
	
}
