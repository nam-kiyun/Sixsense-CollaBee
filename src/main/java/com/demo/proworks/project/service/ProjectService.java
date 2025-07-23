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
     * 프로젝트와 관련된 모든 데이터를 완전히 삭제 처리한다.
     *
     * @param  projectId 프로젝트 ID
     * @return 번호
     * @throws Exception
     */
	public int deleteProjectCompletely(String projectId) throws Exception;

    /**
     * 프로젝트 초대 이메일을 위한 프로젝트 정보와 팀장 정보를 조회한다.
     *
     * @param  projectId 프로젝트 ID
     * @return EmailVo 이메일 발송을 위한 프로젝트 정보
     * @throws Exception
     */
	public EmailVo selectProjectForEmail(String projectId) throws Exception;

    /**
     * 이메일 발송이 설정된 모든 프로젝트를 조회한다.
     *
     * @return List<EmailVo> 이메일 발송을 위한 프로젝트 정보 목록
     * @throws Exception
     */
    public List<EmailVo> selectProjectsForEmailSend() throws Exception;

    /**
     * 스케줄러에서 할 일 메일을 발송한다.
     *
     * @throws Exception
     */
    public void sendTaskReminder() throws Exception;
	
	/**
     * 사용자가 참여한 프로젝트 목록을 조회한다.
     *
     * @param  userId 사용자 ID
     * @return 사용자가 참여한 프로젝트 목록 List<ProjectVo>
     * @throws Exception
     */
	public List<ProjectVo> selectUserParticipatedProjects(String userId) throws Exception;

	/**
     * 특정 프로젝트의 참여자 정보를 조회한다.
     *
     * @param  projectId 프로젝트 ID
     * @return 프로젝트 참여자 목록 List<ProjectUserVo>
     * @throws Exception
     */
	public List<com.demo.proworks.projectuser.vo.ProjectUserVo> selectProjectUsers(String projectId) throws Exception;
	
}