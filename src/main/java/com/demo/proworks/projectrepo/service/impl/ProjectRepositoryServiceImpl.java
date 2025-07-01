package com.demo.proworks.projectrepo.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.demo.proworks.projectrepo.service.ProjectRepositoryService;
import com.demo.proworks.projectrepo.vo.ProjectRepositoryVo;
import com.demo.proworks.projectrepo.dao.ProjectRepositoryDAO;

/**  
 * @subject     : 프로젝트와 연결된 레포지토리 정보 관련 처리를 담당하는 ServiceImpl
 * @description	: 프로젝트와 연결된 레포지토리 정보 관련 처리를 담당하는 ServiceImpl
 * @author      : 남기윤
 * @since       : 2025/07/01
 * @modification
 * ===========================================================
 * DATE				AUTHOR				DESC
 * ===========================================================
 * 2025/07/01			 남기윤	 		최초 생성
 * 
 */
@Service("projectRepositoryServiceImpl")
public class ProjectRepositoryServiceImpl implements ProjectRepositoryService {

    @Resource(name="projectRepositoryDAO")
    private ProjectRepositoryDAO projectRepositoryDAO;
	
	@Resource(name = "messageSource")
	private MessageSource messageSource;

    /**
     * 프로젝트와 연결된 레포지토리 정보 목록을 조회합니다.
     *
     * @process
     * 1. 프로젝트와 연결된 레포지토리 정보 페이징 처리하여 목록을 조회한다.
     * 2. 결과 List<ProjectRepositoryVo>을(를) 리턴한다.
     * 
     * @param  projectRepositoryVo 프로젝트와 연결된 레포지토리 정보 ProjectRepositoryVo
     * @return 프로젝트와 연결된 레포지토리 정보 목록 List<ProjectRepositoryVo>
     * @throws Exception
     */
	public List<ProjectRepositoryVo> selectListProjectRepository(ProjectRepositoryVo projectRepositoryVo) throws Exception {
		List<ProjectRepositoryVo> list = projectRepositoryDAO.selectListProjectRepository(projectRepositoryVo);	
	
		return list;
	}

    /**
     * 조회한 프로젝트와 연결된 레포지토리 정보 전체 카운트
     *
     * @process
     * 1. 프로젝트와 연결된 레포지토리 정보 조회하여 전체 카운트를 리턴한다.
     * 
     * @param  projectRepositoryVo 프로젝트와 연결된 레포지토리 정보 ProjectRepositoryVo
     * @return 프로젝트와 연결된 레포지토리 정보 목록 전체 카운트
     * @throws Exception
     */
	public long selectListCountProjectRepository(ProjectRepositoryVo projectRepositoryVo) throws Exception {
		return projectRepositoryDAO.selectListCountProjectRepository(projectRepositoryVo);
	}

    /**
     * 프로젝트와 연결된 레포지토리 정보를 상세 조회한다.
     *
     * @process
     * 1. 프로젝트와 연결된 레포지토리 정보를 상세 조회한다.
     * 2. 결과 ProjectRepositoryVo을(를) 리턴한다.
     * 
     * @param  projectRepositoryVo 프로젝트와 연결된 레포지토리 정보 ProjectRepositoryVo
     * @return 단건 조회 결과
     * @throws Exception
     */
	public ProjectRepositoryVo selectProjectRepository(ProjectRepositoryVo projectRepositoryVo) throws Exception {
		ProjectRepositoryVo resultVO = projectRepositoryDAO.selectProjectRepository(projectRepositoryVo);			
        
        return resultVO;
	}

    /**
     * 프로젝트와 연결된 레포지토리 정보를 등록 처리 한다.
     *
     * @process
     * 1. 프로젝트와 연결된 레포지토리 정보를 등록 처리 한다.
     * 
     * @param  projectRepositoryVo 프로젝트와 연결된 레포지토리 정보 ProjectRepositoryVo
     * @return 번호
     * @throws Exception
     */
	public int insertProjectRepository(ProjectRepositoryVo projectRepositoryVo) throws Exception {
		return projectRepositoryDAO.insertProjectRepository(projectRepositoryVo);	
	}
	
    /**
     * 프로젝트와 연결된 레포지토리 정보를 갱신 처리 한다.
     *
     * @process
     * 1. 프로젝트와 연결된 레포지토리 정보를 갱신 처리 한다.
     * 
     * @param  projectRepositoryVo 프로젝트와 연결된 레포지토리 정보 ProjectRepositoryVo
     * @return 번호
     * @throws Exception
     */
	public int updateProjectRepository(ProjectRepositoryVo projectRepositoryVo) throws Exception {				
		return projectRepositoryDAO.updateProjectRepository(projectRepositoryVo);	   		
	}

    /**
     * 프로젝트와 연결된 레포지토리 정보를 삭제 처리 한다.
     *
     * @process
     * 1. 프로젝트와 연결된 레포지토리 정보를 삭제 처리 한다.
     * 
     * @param  projectRepositoryVo 프로젝트와 연결된 레포지토리 정보 ProjectRepositoryVo
     * @return 번호
     * @throws Exception
     */
	public int deleteProjectRepository(ProjectRepositoryVo projectRepositoryVo) throws Exception {
		return projectRepositoryDAO.deleteProjectRepository(projectRepositoryVo);
	}
	
}
