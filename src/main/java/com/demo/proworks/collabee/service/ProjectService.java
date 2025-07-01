package com.demo.proworks.collabee.service;

import java.util.List;

import com.demo.proworks.collabee.vo.ProjectVo;

/**  
 * @subject     : 프로젝트 관련 처리를 담당하는 인터페이스
 * @description : 프로젝트 관련 처리를 담당하는 인터페이스
 * @author      : 남기윤
 * @since       : 2025/07/01
 * @modification
 * ===========================================================
 * DATE				AUTHOR				DESC
 * ===========================================================
 * 2025/07/01			 남기윤	 		최초 생성
 * 
 */
public interface ProjectService {
	
    /**
     * 프로젝트 페이징 처리하여 목록을 조회한다.
     *
     * @param  projectVo 프로젝트 ProjectVo
     * @return 프로젝트 목록 List<ProjectVo>
     * @throws Exception
     */
	public List<ProjectVo> selectListProject(ProjectVo projectVo) throws Exception;
	
    /**
     * 조회한 프로젝트 전체 카운트
     * 
     * @param  projectVo 프로젝트 ProjectVo
     * @return 프로젝트 목록 전체 카운트
     * @throws Exception
     */
	public long selectListCountProject(ProjectVo projectVo) throws Exception;
	
    /**
     * 프로젝트를 상세 조회한다.
     *
     * @param  projectVo 프로젝트 ProjectVo
     * @return 단건 조회 결과
     * @throws Exception
     */
	public ProjectVo selectProject(ProjectVo projectVo) throws Exception;
		
    /**
     * 프로젝트를 등록 처리 한다.
     *
     * @param  projectVo 프로젝트 ProjectVo
     * @return 번호
     * @throws Exception
     */
	public int insertProject(ProjectVo projectVo) throws Exception;
	
    /**
     * 프로젝트를 갱신 처리 한다.
     *
     * @param  projectVo 프로젝트 ProjectVo
     * @return 번호
     * @throws Exception
     */
	public int updateProject(ProjectVo projectVo) throws Exception;
	
    /**
     * 프로젝트를 삭제 처리 한다.
     *
     * @param  projectVo 프로젝트 ProjectVo
     * @return 번호
     * @throws Exception
     */
	public int deleteProject(ProjectVo projectVo) throws Exception;
	
}
