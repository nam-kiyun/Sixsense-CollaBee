package com.demo.proworks.collabee.service;

import java.util.List;

import com.demo.proworks.collabee.vo.FileSrcVo;

/**  
 * @subject     : 업로드한 파일 저장 경로 관련 처리를 담당하는 인터페이스
 * @description : 업로드한 파일 저장 경로 관련 처리를 담당하는 인터페이스
 * @author      : 남기윤
 * @since       : 2025/07/01
 * @modification
 * ===========================================================
 * DATE				AUTHOR				DESC
 * ===========================================================
 * 2025/07/01			 남기윤	 		최초 생성
 * 
 */
public interface FileSrcService {
	
    /**
     * 업로드한 파일 저장 경로 페이징 처리하여 목록을 조회한다.
     *
     * @param  fileSrcVo 업로드한 파일 저장 경로 FileSrcVo
     * @return 업로드한 파일 저장 경로 목록 List<FileSrcVo>
     * @throws Exception
     */
	public List<FileSrcVo> selectListFileSrc(FileSrcVo fileSrcVo) throws Exception;
	
    /**
     * 조회한 업로드한 파일 저장 경로 전체 카운트
     * 
     * @param  fileSrcVo 업로드한 파일 저장 경로 FileSrcVo
     * @return 업로드한 파일 저장 경로 목록 전체 카운트
     * @throws Exception
     */
	public long selectListCountFileSrc(FileSrcVo fileSrcVo) throws Exception;
	
    /**
     * 업로드한 파일 저장 경로를 상세 조회한다.
     *
     * @param  fileSrcVo 업로드한 파일 저장 경로 FileSrcVo
     * @return 단건 조회 결과
     * @throws Exception
     */
	public FileSrcVo selectFileSrc(FileSrcVo fileSrcVo) throws Exception;
		
    /**
     * 업로드한 파일 저장 경로를 등록 처리 한다.
     *
     * @param  fileSrcVo 업로드한 파일 저장 경로 FileSrcVo
     * @return 번호
     * @throws Exception
     */
	public int insertFileSrc(FileSrcVo fileSrcVo) throws Exception;
	
    /**
     * 업로드한 파일 저장 경로를 갱신 처리 한다.
     *
     * @param  fileSrcVo 업로드한 파일 저장 경로 FileSrcVo
     * @return 번호
     * @throws Exception
     */
	public int updateFileSrc(FileSrcVo fileSrcVo) throws Exception;
	
    /**
     * 업로드한 파일 저장 경로를 삭제 처리 한다.
     *
     * @param  fileSrcVo 업로드한 파일 저장 경로 FileSrcVo
     * @return 번호
     * @throws Exception
     */
	public int deleteFileSrc(FileSrcVo fileSrcVo) throws Exception;
	
}
