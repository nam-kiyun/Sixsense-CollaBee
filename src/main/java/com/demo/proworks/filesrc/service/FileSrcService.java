package com.demo.proworks.filesrc.service;

import java.util.List;

import com.demo.proworks.filesrc.vo.FileSrcVo;

/**  
 * @subject     : 파일  관련 처리를 담당하는 인터페이스
 * @description : 파일  관련 처리를 담당하는 인터페이스
 * @author      : 국다인
 * @since       : 2025/07/01
 * @modification
 * ===========================================================
 * DATE				AUTHOR				DESC
 * ===========================================================
 * 2025/07/01			 국다인	 		최초 생성
 * 
 */
public interface FileSrcService {
	
    /**
     * 파일  페이징 처리하여 목록을 조회한다.
     *
     * @param  fileSrcVo 파일  FileSrcVo
     * @return 파일  목록 List<FileSrcVo>
     * @throws Exception
     */
	public List<FileSrcVo> selectListFileSrc(FileSrcVo fileSrcVo) throws Exception;
	
    /**
     * 조회한 파일  전체 카운트
     * 
     * @param  fileSrcVo 파일  FileSrcVo
     * @return 파일  목록 전체 카운트
     * @throws Exception
     */
	public long selectListCountFileSrc(FileSrcVo fileSrcVo) throws Exception;
	
    /**
     * 파일 를 상세 조회한다.
     *
     * @param  fileSrcVo 파일  FileSrcVo
     * @return 단건 조회 결과
     * @throws Exception
     */
	public FileSrcVo selectFileSrc(FileSrcVo fileSrcVo) throws Exception;
		
    /**
     * 파일 를 등록 처리 한다.
     *
     * @param  fileSrcVo 파일  FileSrcVo
     * @return 번호
     * @throws Exception
     */
	public int insertFileSrc(FileSrcVo fileSrcVo) throws Exception;
	
    /**
     * 파일 를 갱신 처리 한다.
     *
     * @param  fileSrcVo 파일  FileSrcVo
     * @return 번호
     * @throws Exception
     */
	public int updateFileSrc(FileSrcVo fileSrcVo) throws Exception;
	
    /**
     * 파일 를 삭제 처리 한다.
     *
     * @param  fileSrcVo 파일  FileSrcVo
     * @return 번호
     * @throws Exception
     */
	public int deleteFileSrc(FileSrcVo fileSrcVo) throws Exception;
	
}
