package com.demo.proworks.project.service;

import java.util.List;

import com.demo.proworks.project.vo.ProjectVo;
import com.demo.proworks.email.vo.EmailVo;

/**  
 * @subject     : 프로젝트 정보 관련 처리를 담당하는 인터페이스
 * @description : 프로젝트 정보 관련 처리를 담당하는 인터페이스
 * @author      : 국다인
 * @since       : 2025/07/01
 * @modification
 * ===========================================================
 * DATE				AUTHOR				DESC
 * ===========================================================
 * 2025/07/01			 국다인	 		최초 생성
 * 
 */
public interface ProjectService {
	
    /**
     * 프로젝트 정보 페이징 처리하여 목록을 조회한다.
     *
     * @param  projectVo 프로젝트 정보 ProjectVo
     * @return 프로젝트 정보 목록 List<ProjectVo>
     * @throws Exception
     */
	public List<ProjectVo> selectListProject(ProjectVo projectVo) throws Exception;
	
    /**
     * 조회한 프로젝트 정보 전체 카운트
     * 
     * @param  projectVo 프로젝트 정보 ProjectVo
     * @return 프로젝트 정보 목록 전체 카운트
     * @throws Exception
     */
	public long selectListCountProject(ProjectVo projectVo) throws Exception;
	
    /**
     * 프로젝트 정보를 상세 조회한다.
     *
     * @param  projectVo 프로젝트 정보 ProjectVo
     * @return 단건 조회 결과
     * @throws Exception
     */
	public ProjectVo selectProject(ProjectVo projectVo) throws Exception;
		
    /**
     * 프로젝트 정보를 등록 처리 한다.
     *
     * @param  projectVo 프로젝트 정보 ProjectVo
     * @return 번호
     * @throws Exception
     */
	public int insertProject(ProjectVo projectVo) throws Exception;
	
    /**
     * 프로젝트 정보를 갱신 처리 한다.
     *
     * @param  projectVo 프로젝트 정보 ProjectVo
     * @return 번호
     * @throws Exception
     */
	public int updateProject(ProjectVo projectVo) throws Exception;
	
    /**
     * 프로젝트 정보를 삭제 처리 한다.
     *
     * @param  projectVo 프로젝트 정보 ProjectVo
     * @return 번호
     * @throws Exception
     */
	public int deleteProject(ProjectVo projectVo) throws Exception;

    /**
     * 프로젝트 초대 이메일을 위한 프로젝트 정보와 팀장 정보를 조회한다.
     *
     * @param  projectId 프로젝트 ID
     * @return EmailVo 이메일 발송을 위한 프로젝트 정보
     * @throws Exception
     */
	public EmailVo selectProjectForEmail(String projectId) throws Exception;
	
}
