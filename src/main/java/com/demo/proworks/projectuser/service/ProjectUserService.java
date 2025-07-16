package com.demo.proworks.projectuser.service;

import java.util.List;

import com.demo.proworks.projectuser.vo.ProjectUserVo;

/**  
 * @subject     : 프로젝트에 초대(참가)한 사람들 관련 처리를 담당하는 인터페이스
 * @description : 프로젝트에 초대(참가)한 사람들 관련 처리를 담당하는 인터페이스
 * @author      : 남기윤
 * @since       : 2025/07/01
 * @modification
 * ===========================================================
 * DATE				AUTHOR				DESC
 * ===========================================================
 * 2025/07/01			 남기윤	 		최초 생성
 * 
 */
public interface ProjectUserService {
	
    /**
     * 프로젝트에 초대(참가)한 사람들 페이징 처리하여 목록을 조회한다.
     *
     * @param  projectUserVo 프로젝트에 초대(참가)한 사람들 ProjectUserVo
     * @return 프로젝트에 초대(참가)한 사람들 목록 List<ProjectUserVo>
     * @throws Exception
     */
	public List<ProjectUserVo> selectListProjectUser(ProjectUserVo projectUserVo) throws Exception;
	
    /**
     * 조회한 프로젝트에 초대(참가)한 사람들 전체 카운트
     * 
     * @param  projectUserVo 프로젝트에 초대(참가)한 사람들 ProjectUserVo
     * @return 프로젝트에 초대(참가)한 사람들 목록 전체 카운트
     * @throws Exception
     */
	public long selectListCountProjectUser(ProjectUserVo projectUserVo) throws Exception;
	
    /**
     * 프로젝트에 초대(참가)한 사람들를 상세 조회한다.
     *
     * @param  projectUserVo 프로젝트에 초대(참가)한 사람들 ProjectUserVo
     * @return 단건 조회 결과
     * @throws Exception
     */
	public ProjectUserVo selectProjectUser(ProjectUserVo projectUserVo) throws Exception;
		
    /**
     * 프로젝트에 초대(참가)한 사람들를 등록 처리 한다.
     *
     * @param  projectUserVo 프로젝트에 초대(참가)한 사람들 ProjectUserVo
     * @return 번호
     * @throws Exception
     */
	public int insertProjectUser(ProjectUserVo projectUserVo) throws Exception;
	
    /**
     * 프로젝트에 초대(참가)한 사람들를 갱신 처리 한다.
     *
     * @param  projectUserVo 프로젝트에 초대(참가)한 사람들 ProjectUserVo
     * @return 번호
     * @throws Exception
     */
	public int updateProjectUser(ProjectUserVo projectUserVo) throws Exception;
	
    /**
     * 프로젝트에 초대(참가)한 사람들를 삭제 처리 한다.
     *
     * @param  projectUserVo 프로젝트에 초대(참가)한 사람들 ProjectUserVo
     * @return 번호
     * @throws Exception
     */
	public int deleteProjectUser(ProjectUserVo projectUserVo) throws Exception;
	
    /**
     * 특정 프로젝트의 멤버 목록을 사용자 정보와 함께 조회한다.
     *
     * @param  projectUserVo 프로젝트에 초대(참가)한 사람들 ProjectUserVo
     * @return 프로젝트 멤버 목록 (사용자 정보 포함) List<ProjectUserVo>
     * @throws Exception
     */
	public List<ProjectUserVo> selectProjectUsersByProjectId(ProjectUserVo projectUserVo) throws Exception;

    /**
     * user_id(이메일)로 사용자를 검색하고 프로젝트 멤버 여부를 확인한다.
     *
     * @param  userId 사용자 ID (이메일)
     * @param  projectId 프로젝트 ID
     * @return ProjectUserVo 사용자 정보와 프로젝트 멤버 여부
     * @throws Exception
     */
	public ProjectUserVo searchUserByUserId(String userId, String projectId) throws Exception;

    /**
     * 프로젝트에 사용자를 추가한다.
     *
     * @param  projectUserVo 프로젝트 사용자 정보
     * @return 번호
     * @throws Exception
     */
	public int inviteUserToProject(ProjectUserVo projectUserVo) throws Exception;

    /**
     * 프로젝트 사용자의 역할을 변경한다.
     *
     * @param  projectUserVo 프로젝트 사용자 정보
     * @return 번호
     * @throws Exception
     */
	public int updateProjectUserRole(ProjectUserVo projectUserVo) throws Exception;
	
}
