package com.demo.proworks.projectLog.service;

import java.util.List;

import com.demo.proworks.projectLog.vo.ProjectLogVo;

/**  
 * @subject     : 프로젝트 로그 관련 처리를 담당하는 인터페이스
 * @description : 프로젝트 로그 관련 처리를 담당하는 인터페이스
 * @author      : 국다인
 * @since       : 2025/07/01
 * @modification
 * ===========================================================
 * DATE				AUTHOR				DESC
 * ===========================================================
 * 2025/07/01			 국다인	 		최초 생성
 * 
 */
public interface ProjectLogService {
	
    /**
     * 프로젝트 로그 페이징 처리하여 목록을 조회한다.
     *
     * @param  projectLogVo 프로젝트 로그 ProjectLogVo
     * @return 프로젝트 로그 목록 List<ProjectLogVo>
     * @throws Exception
     */
	public List<ProjectLogVo> selectListProjectLog(ProjectLogVo projectLogVo) throws Exception;
	
    /**
     * 조회한 프로젝트 로그 전체 카운트
     * 
     * @param  projectLogVo 프로젝트 로그 ProjectLogVo
     * @return 프로젝트 로그 목록 전체 카운트
     * @throws Exception
     */
	public long selectListCountProjectLog(ProjectLogVo projectLogVo) throws Exception;
	
    /**
     * 프로젝트 로그를 상세 조회한다.
     *
     * @param  projectLogVo 프로젝트 로그 ProjectLogVo
     * @return 단건 조회 결과
     * @throws Exception
     */
	public ProjectLogVo selectProjectLog(ProjectLogVo projectLogVo) throws Exception;
		
    /**
     * 프로젝트 로그를 등록 처리 한다.
     *
     * @param  projectLogVo 프로젝트 로그 ProjectLogVo
     * @return 번호
     * @throws Exception
     */
	public int insertProjectLog(ProjectLogVo projectLogVo) throws Exception;
	
    /**
     * 프로젝트 로그를 갱신 처리 한다.
     *
     * @param  projectLogVo 프로젝트 로그 ProjectLogVo
     * @return 번호
     * @throws Exception
     */
	public int updateProjectLog(ProjectLogVo projectLogVo) throws Exception;
	
    /**
     * 프로젝트 로그를 삭제 처리 한다.
     *
     * @param  projectLogVo 프로젝트 로그 ProjectLogVo
     * @return 번호
     * @throws Exception
     */
	public int deleteProjectLog(ProjectLogVo projectLogVo) throws Exception;
	
}
