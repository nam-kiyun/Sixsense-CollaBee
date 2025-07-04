package com.demo.proworks.project.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.demo.proworks.project.service.ProjectService;
import com.demo.proworks.project.vo.ProjectVo;
import com.demo.proworks.project.dao.ProjectDAO;

/**  
 * @subject     : 프로젝트 정보 관련 처리를 담당하는 ServiceImpl
 * @description	: 프로젝트 정보 관련 처리를 담당하는 ServiceImpl
 * @author      : 국다인
 * @since       : 2025/07/01
 * @modification
 * ===========================================================
 * DATE				AUTHOR				DESC
 * ===========================================================
 * 2025/07/01			 국다인	 		최초 생성
 * 
 */
@Service("projectServiceImpl")
public class ProjectServiceImpl implements ProjectService {

    @Resource(name="projectDAO")
    private ProjectDAO projectDAO;
	
	@Resource(name = "messageSource")
	private MessageSource messageSource;

    /**
     * 프로젝트 정보 목록을 조회합니다.
     *
     * @process
     * 1. 프로젝트 정보 페이징 처리하여 목록을 조회한다.
     * 2. 결과 List<ProjectVo>을(를) 리턴한다.
     * 
     * @param  projectVo 프로젝트 정보 ProjectVo
     * @return 프로젝트 정보 목록 List<ProjectVo>
     * @throws Exception
     */
	public List<ProjectVo> selectListProject(ProjectVo projectVo) throws Exception {
		List<ProjectVo> list = projectDAO.selectListProject(projectVo);	
	
		return list;
	}

    /**
     * 조회한 프로젝트 정보 전체 카운트
     *
     * @process
     * 1. 프로젝트 정보 조회하여 전체 카운트를 리턴한다.
     * 
     * @param  projectVo 프로젝트 정보 ProjectVo
     * @return 프로젝트 정보 목록 전체 카운트
     * @throws Exception
     */
	public long selectListCountProject(ProjectVo projectVo) throws Exception {
		return projectDAO.selectListCountProject(projectVo);
	}

    /**
     * 프로젝트 정보를 상세 조회한다.
     *
     * @process
     * 1. 프로젝트 정보를 상세 조회한다.
     * 2. 결과 ProjectVo을(를) 리턴한다.
     * 
     * @param  projectVo 프로젝트 정보 ProjectVo
     * @return 단건 조회 결과
     * @throws Exception
     */
	public ProjectVo selectProject(ProjectVo projectVo) throws Exception {
		ProjectVo resultVO = projectDAO.selectProject(projectVo);			
        
        return resultVO;
	}

    /**
     * 프로젝트 정보를 등록 처리 한다.
     *
     * @process
     * 1. 프로젝트 정보를 등록 처리 한다.
     * 
     * @param  projectVo 프로젝트 정보 ProjectVo
     * @return 번호
     * @throws Exception
     */
	public int insertProject(ProjectVo projectVo) throws Exception {
		return projectDAO.insertProject(projectVo);	
	}
	
    /**
     * 프로젝트 정보를 갱신 처리 한다.
     *
     * @process
     * 1. 프로젝트 정보를 갱신 처리 한다.
     * 
     * @param  projectVo 프로젝트 정보 ProjectVo
     * @return 번호
     * @throws Exception
     */
	public int updateProject(ProjectVo projectVo) throws Exception {				
		return projectDAO.updateProject(projectVo);	   		
	}

    /**
     * 프로젝트 정보를 삭제 처리 한다.
     *
     * @process
     * 1. 프로젝트 정보를 삭제 처리 한다.
     * 
     * @param  projectVo 프로젝트 정보 ProjectVo
     * @return 번호
     * @throws Exception
     */
	public int deleteProject(ProjectVo projectVo) throws Exception {
		return projectDAO.deleteProject(projectVo);
	}
	
}
