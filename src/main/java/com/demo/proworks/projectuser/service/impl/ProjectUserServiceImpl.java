package com.demo.proworks.projectuser.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.demo.proworks.projectuser.service.ProjectUserService;
import com.demo.proworks.projectuser.vo.ProjectUserVo;
import com.demo.proworks.projectuser.dao.ProjectUserDAO;

/**  
 * @subject     : 프로젝트에 초대(참가)한 사람들 관련 처리를 담당하는 ServiceImpl
 * @description	: 프로젝트에 초대(참가)한 사람들 관련 처리를 담당하는 ServiceImpl
 * @author      : 남기윤
 * @since       : 2025/07/01
 * @modification
 * ===========================================================
 * DATE				AUTHOR				DESC
 * ===========================================================
 * 2025/07/01			 남기윤	 		최초 생성
 * 
 */
@Service("projectUserServiceImpl")
public class ProjectUserServiceImpl implements ProjectUserService {

    @Resource(name="projectUserDAO")
    private ProjectUserDAO projectUserDAO;
	
	@Resource(name = "messageSource")
	private MessageSource messageSource;

    /**
     * 프로젝트에 초대(참가)한 사람들 목록을 조회합니다.
     *
     * @process
     * 1. 프로젝트에 초대(참가)한 사람들 페이징 처리하여 목록을 조회한다.
     * 2. 결과 List<ProjectUserVo>을(를) 리턴한다.
     * 
     * @param  projectUserVo 프로젝트에 초대(참가)한 사람들 ProjectUserVo
     * @return 프로젝트에 초대(참가)한 사람들 목록 List<ProjectUserVo>
     * @throws Exception
     */
	public List<ProjectUserVo> selectListProjectUser(ProjectUserVo projectUserVo) throws Exception {
		List<ProjectUserVo> list = projectUserDAO.selectListProjectUser(projectUserVo);	
	
		return list;
	}

    /**
     * 조회한 프로젝트에 초대(참가)한 사람들 전체 카운트
     *
     * @process
     * 1. 프로젝트에 초대(참가)한 사람들 조회하여 전체 카운트를 리턴한다.
     * 
     * @param  projectUserVo 프로젝트에 초대(참가)한 사람들 ProjectUserVo
     * @return 프로젝트에 초대(참가)한 사람들 목록 전체 카운트
     * @throws Exception
     */
	public long selectListCountProjectUser(ProjectUserVo projectUserVo) throws Exception {
		return projectUserDAO.selectListCountProjectUser(projectUserVo);
	}

    /**
     * 프로젝트에 초대(참가)한 사람들를 상세 조회한다.
     *
     * @process
     * 1. 프로젝트에 초대(참가)한 사람들를 상세 조회한다.
     * 2. 결과 ProjectUserVo을(를) 리턴한다.
     * 
     * @param  projectUserVo 프로젝트에 초대(참가)한 사람들 ProjectUserVo
     * @return 단건 조회 결과
     * @throws Exception
     */
	public ProjectUserVo selectProjectUser(ProjectUserVo projectUserVo) throws Exception {
		ProjectUserVo resultVO = projectUserDAO.selectProjectUser(projectUserVo);			
        
        return resultVO;
	}

    /**
     * 프로젝트에 초대(참가)한 사람들를 등록 처리 한다.
     *
     * @process
     * 1. 프로젝트에 초대(참가)한 사람들를 등록 처리 한다.
     * 
     * @param  projectUserVo 프로젝트에 초대(참가)한 사람들 ProjectUserVo
     * @return 번호
     * @throws Exception
     */
	public int insertProjectUser(ProjectUserVo projectUserVo) throws Exception {
		return projectUserDAO.insertProjectUser(projectUserVo);	
	}
	
    /**
     * 프로젝트에 초대(참가)한 사람들를 갱신 처리 한다.
     *
     * @process
     * 1. 프로젝트에 초대(참가)한 사람들를 갱신 처리 한다.
     * 
     * @param  projectUserVo 프로젝트에 초대(참가)한 사람들 ProjectUserVo
     * @return 번호
     * @throws Exception
     */
	public int updateProjectUser(ProjectUserVo projectUserVo) throws Exception {				
		return projectUserDAO.updateProjectUser(projectUserVo);	   		
	}

    /**
     * 프로젝트에 초대(참가)한 사람들를 삭제 처리 한다.
     *
     * @process
     * 1. 프로젝트에 초대(참가)한 사람들를 삭제 처리 한다.
     * 
     * @param  projectUserVo 프로젝트에 초대(참가)한 사람들 ProjectUserVo
     * @return 번호
     * @throws Exception
     */
	public int deleteProjectUser(ProjectUserVo projectUserVo) throws Exception {
		return projectUserDAO.deleteProjectUser(projectUserVo);
	}

    /**
     * 특정 프로젝트의 멤버 목록을 사용자 정보와 함께 조회한다.
     *
     * @process
     * 1. 특정 프로젝트의 멤버 목록을 사용자 정보와 함께 조회한다.
     * 2. 결과 List<ProjectUserVo>을(를) 리턴한다.
     * 
     * @param  projectUserVo 프로젝트에 초대(참가)한 사람들 ProjectUserVo
     * @return 프로젝트 멤버 목록 (사용자 정보 포함) List<ProjectUserVo>
     * @throws Exception
     */
	public List<ProjectUserVo> selectProjectUsersByProjectId(ProjectUserVo projectUserVo) throws Exception {
		List<ProjectUserVo> list = projectUserDAO.selectProjectUsersByProjectId(projectUserVo);
		
		return list;
	}

    /**
     * user_id(이메일)로 사용자를 검색하고 프로젝트 멤버 여부를 확인한다.
     *
     * @process
     * 1. user_id(이메일)로 사용자 정보를 조회한다.
     * 2. 조회된 사용자가 해당 프로젝트 멤버인지 확인한다.
     * 3. 결과를 담은 ProjectUserVo를 리턴한다.
     * 
     * @param  userId 사용자 ID (이메일)
     * @param  projectId 프로젝트 ID
     * @return ProjectUserVo 사용자 정보와 프로젝트 멤버 여부
     * @throws Exception
     */
	public ProjectUserVo searchUserByUserId(String userId, String projectId) throws Exception {
		// 1. user_id(이메일)로 사용자 정보 조회
		ProjectUserVo userInfo = projectUserDAO.selectUserByUserId(userId);
		
		System.out.println("=== searchUserByUserId 디버깅 ===");
		System.out.println("검색 userId: " + userId);
		System.out.println("조회된 userInfo: " + userInfo);
		if (userInfo != null) {
			System.out.println("userInfo.getUserId(): " + userInfo.getUserId());
			System.out.println("userInfo.getUserName(): " + userInfo.getUserName());
		}
		
		if (userInfo == null) {
			// 사용자가 존재하지 않는 경우
			return null;
		}
		
		// 2. 해당 사용자가 프로젝트 멤버인지 확인
		ProjectUserVo checkVo = new ProjectUserVo();
		checkVo.setProjectId(projectId);
		checkVo.setUserId(userInfo.getUserId());
		
		ProjectUserVo existingMember = projectUserDAO.selectProjectUserByIds(checkVo);
		
		// 3. 결과 설정
		userInfo.setProjectId(projectId);
		// 기존 멤버 여부를 role 필드로 표시 (null이면 멤버 아님)
		if (existingMember != null) {
			userInfo.setRole(existingMember.getRole());
		}
		
		return userInfo;
	}

    /**
     * 프로젝트에 사용자를 추가한다.
     *
     * @process
     * 1. 프로젝트 사용자 정보를 등록한다.
     * 
     * @param  projectUserVo 프로젝트 사용자 정보
     * @return 번호
     * @throws Exception
     */
	public int inviteUserToProject(ProjectUserVo projectUserVo) throws Exception {
		// 기본 role 설정 (editor)
		if (projectUserVo.getRole() == null || projectUserVo.getRole().isEmpty()) {
			projectUserVo.setRole("editor");
		}
		
		return projectUserDAO.insertProjectUserBySearch(projectUserVo);
	}
	
}
