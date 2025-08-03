package com.demo.proworks.projectLog.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.demo.proworks.projectLog.service.ProjectLogService;
import com.demo.proworks.projectLog.vo.ProjectLogVo;
import com.demo.proworks.projectLog.dao.ProjectLogDAO;

/**  
 * @subject     : 프로젝트 로그 관련 처리를 담당하는 ServiceImpl
 * @description	: 프로젝트 로그 관련 처리를 담당하는 ServiceImpl
 * @author      : 국다인
 * @since       : 2025/07/01
 * @modification
 * ===========================================================
 * DATE				AUTHOR				DESC
 * ===========================================================
 * 2025/07/01			 국다인	 		최초 생성
 * 
 */
@Service("projectLogServiceImpl")
public class ProjectLogServiceImpl implements ProjectLogService {

    @Resource(name="projectLogDAO")
    private ProjectLogDAO projectLogDAO;
	
	@Resource(name = "messageSource")
	private MessageSource messageSource;

    /**
     * 프로젝트 로그 목록을 조회합니다.
     *
     * @process
     * 1. 프로젝트 로그 페이징 처리하여 목록을 조회한다.
     * 2. 결과 List<ProjectLogVo>을(를) 리턴한다.
     * 
     * @param  projectLogVo 프로젝트 로그 ProjectLogVo
     * @return 프로젝트 로그 목록 List<ProjectLogVo>
     * @throws Exception
     */
	public List<ProjectLogVo> selectListProjectLog(ProjectLogVo projectLogVo) throws Exception {
		List<ProjectLogVo> list = projectLogDAO.selectListProjectLog(projectLogVo);	
	
		return list;
	}

    /**
     * 조회한 프로젝트 로그 전체 카운트
     *
     * @process
     * 1. 프로젝트 로그 조회하여 전체 카운트를 리턴한다.
     * 
     * @param  projectLogVo 프로젝트 로그 ProjectLogVo
     * @return 프로젝트 로그 목록 전체 카운트
     * @throws Exception
     */
	public long selectListCountProjectLog(ProjectLogVo projectLogVo) throws Exception {
		return projectLogDAO.selectListCountProjectLog(projectLogVo);
	}

    /**
     * 프로젝트 로그를 상세 조회한다.
     *
     * @process
     * 1. 프로젝트 로그를 상세 조회한다.
     * 2. 결과 ProjectLogVo을(를) 리턴한다.
     * 
     * @param  projectLogVo 프로젝트 로그 ProjectLogVo
     * @return 단건 조회 결과
     * @throws Exception
     */
	public ProjectLogVo selectProjectLog(ProjectLogVo projectLogVo) throws Exception {
		ProjectLogVo resultVO = projectLogDAO.selectProjectLog(projectLogVo);			
        
        return resultVO;
	}

    /**
     * 프로젝트 로그를 등록 처리 한다.
     *
     * @process
     * 1. 프로젝트 로그를 등록 처리 한다.
     * 
     * @param  projectLogVo 프로젝트 로그 ProjectLogVo
     * @return 번호
     * @throws Exception
     */
	public int insertProjectLog(ProjectLogVo projectLogVo) throws Exception {
		return projectLogDAO.insertProjectLog(projectLogVo);	
	}
	
    /**
     * 프로젝트 로그를 갱신 처리 한다.
     *
     * @process
     * 1. 프로젝트 로그를 갱신 처리 한다.
     * 
     * @param  projectLogVo 프로젝트 로그 ProjectLogVo
     * @return 번호
     * @throws Exception
     */
	public int updateProjectLog(ProjectLogVo projectLogVo) throws Exception {				
		return projectLogDAO.updateProjectLog(projectLogVo);	   		
	}

    /**
     * 프로젝트 로그를 삭제 처리 한다.
     *
     * @process
     * 1. 프로젝트 로그를 삭제 처리 한다.
     * 
     * @param  projectLogVo 프로젝트 로그 ProjectLogVo
     * @return 번호
     * @throws Exception
     */
	public int deleteProjectLog(ProjectLogVo projectLogVo) throws Exception {
		return projectLogDAO.deleteProjectLog(projectLogVo);
	}
	
}
