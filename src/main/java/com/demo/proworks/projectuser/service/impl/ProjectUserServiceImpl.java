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
	
}
