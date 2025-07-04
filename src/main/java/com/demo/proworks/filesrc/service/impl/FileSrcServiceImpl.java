package com.demo.proworks.filesrc.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.demo.proworks.filesrc.service.FileSrcService;
import com.demo.proworks.filesrc.vo.FileSrcVo;
import com.demo.proworks.filesrc.dao.FileSrcDAO;

/**  
 * @subject     : 파일  관련 처리를 담당하는 ServiceImpl
 * @description	: 파일  관련 처리를 담당하는 ServiceImpl
 * @author      : 국다인
 * @since       : 2025/07/01
 * @modification
 * ===========================================================
 * DATE				AUTHOR				DESC
 * ===========================================================
 * 2025/07/01			 국다인	 		최초 생성
 * 
 */
@Service("fileSrcServiceImpl")
public class FileSrcServiceImpl implements FileSrcService {

    @Resource(name="fileSrcDAO")
    private FileSrcDAO fileSrcDAO;
	
	@Resource(name = "messageSource")
	private MessageSource messageSource;

    /**
     * 파일  목록을 조회합니다.
     *
     * @process
     * 1. 파일  페이징 처리하여 목록을 조회한다.
     * 2. 결과 List<FileSrcVo>을(를) 리턴한다.
     * 
     * @param  fileSrcVo 파일  FileSrcVo
     * @return 파일  목록 List<FileSrcVo>
     * @throws Exception
     */
	public List<FileSrcVo> selectListFileSrc(FileSrcVo fileSrcVo) throws Exception {
		List<FileSrcVo> list = fileSrcDAO.selectListFileSrc(fileSrcVo);	
	
		return list;
	}

    /**
     * 조회한 파일  전체 카운트
     *
     * @process
     * 1. 파일  조회하여 전체 카운트를 리턴한다.
     * 
     * @param  fileSrcVo 파일  FileSrcVo
     * @return 파일  목록 전체 카운트
     * @throws Exception
     */
	public long selectListCountFileSrc(FileSrcVo fileSrcVo) throws Exception {
		return fileSrcDAO.selectListCountFileSrc(fileSrcVo);
	}

    /**
     * 파일 를 상세 조회한다.
     *
     * @process
     * 1. 파일 를 상세 조회한다.
     * 2. 결과 FileSrcVo을(를) 리턴한다.
     * 
     * @param  fileSrcVo 파일  FileSrcVo
     * @return 단건 조회 결과
     * @throws Exception
     */
	public FileSrcVo selectFileSrc(FileSrcVo fileSrcVo) throws Exception {
		FileSrcVo resultVO = fileSrcDAO.selectFileSrc(fileSrcVo);			
        
        return resultVO;
	}

    /**
     * 파일 를 등록 처리 한다.
     *
     * @process
     * 1. 파일 를 등록 처리 한다.
     * 
     * @param  fileSrcVo 파일  FileSrcVo
     * @return 번호
     * @throws Exception
     */
	public int insertFileSrc(FileSrcVo fileSrcVo) throws Exception {
		return fileSrcDAO.insertFileSrc(fileSrcVo);	
	}
	
    /**
     * 파일 를 갱신 처리 한다.
     *
     * @process
     * 1. 파일 를 갱신 처리 한다.
     * 
     * @param  fileSrcVo 파일  FileSrcVo
     * @return 번호
     * @throws Exception
     */
	public int updateFileSrc(FileSrcVo fileSrcVo) throws Exception {				
		return fileSrcDAO.updateFileSrc(fileSrcVo);	   		
	}

    /**
     * 파일 를 삭제 처리 한다.
     *
     * @process
     * 1. 파일 를 삭제 처리 한다.
     * 
     * @param  fileSrcVo 파일  FileSrcVo
     * @return 번호
     * @throws Exception
     */
	public int deleteFileSrc(FileSrcVo fileSrcVo) throws Exception {
		return fileSrcDAO.deleteFileSrc(fileSrcVo);
	}
	
}
