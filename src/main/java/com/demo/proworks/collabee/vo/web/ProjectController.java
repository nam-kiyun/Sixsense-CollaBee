package com.demo.proworks.collabee.vo.web;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.demo.proworks.collabee.vo.service.ProjectService;
import com.demo.proworks.collabee.vo.vo.ProjectVo;
import com.demo.proworks.collabee.vo.vo.ProjectListVo;

import com.inswave.elfw.annotation.ElDescription;
import com.inswave.elfw.annotation.ElService;
import com.inswave.elfw.annotation.ElValidator;

/**  
 * @subject     : 프로젝트 관련 처리를 담당하는 컨트롤러
 * @description : 프로젝트 관련 처리를 담당하는 컨트롤러
 * @author      : 남기윤
 * @since       : 2025/07/01
 * @modification
 * ===========================================================
 * DATE				AUTHOR				DESC
 * ===========================================================
 * 2025/07/01			 남기윤	 		최초 생성
 * 
 */
@Controller
public class ProjectController {
	
    /** ProjectService */
    @Resource(name = "projectServiceImpl")
    private ProjectService projectService;
	
    
    /**
     * 프로젝트 목록을 조회합니다.
     *
     * @param  projectVo 프로젝트
     * @return 목록조회 결과
     * @throws Exception
     */
    @ElService(key="ProjectList")
    @RequestMapping(value="ProjectList")    
    @ElDescription(sub="프로젝트 목록조회",desc="페이징을 처리하여 프로젝트 목록 조회를 한다.")               
    public ProjectListVo selectListProject(ProjectVo projectVo) throws Exception {    	   	

        List<ProjectVo> projectList = projectService.selectListProject(projectVo);                  
        long totCnt = projectService.selectListCountProject(projectVo);
	
		ProjectListVo retProjectList = new ProjectListVo();
		retProjectList.setProjectVoList(projectList); 
		retProjectList.setTotalCount(totCnt);
		retProjectList.setPageSize(projectVo.getPageSize());
		retProjectList.setPageIndex(projectVo.getPageIndex());

        return retProjectList;            
    }  
        
    /**
     * 프로젝트을 단건 조회 처리 한다.
     *
     * @param  projectVo 프로젝트
     * @return 단건 조회 결과
     * @throws Exception
     */
    @ElService(key = "ProjectUpdView")    
    @RequestMapping(value="ProjectUpdView") 
    @ElDescription(sub = "프로젝트 갱신 폼을 위한 조회", desc = "프로젝트 갱신 폼을 위한 조회를 한다.")    
    public ProjectVo selectProject(ProjectVo projectVo) throws Exception {
    	ProjectVo selectProjectVo = projectService.selectProject(projectVo);    	    
		
        return selectProjectVo;
    } 
 
    /**
     * 프로젝트를 등록 처리 한다.
     *
     * @param  projectVo 프로젝트
     * @throws Exception
     */
    @ElService(key="ProjectIns")    
    @RequestMapping(value="ProjectIns")
    @ElDescription(sub="프로젝트 등록처리",desc="프로젝트를 등록 처리 한다.")
    public void insertProject(ProjectVo projectVo) throws Exception {    	 
    	projectService.insertProject(projectVo);   
    }
       
    /**
     * 프로젝트를 갱신 처리 한다.
     *
     * @param  projectVo 프로젝트
     * @throws Exception
     */
    @ElService(key="ProjectUpd")    
    @RequestMapping(value="ProjectUpd")    
    @ElValidator(errUrl="/project/projectRegister", errContinue=true)
    @ElDescription(sub="프로젝트 갱신처리",desc="프로젝트를 갱신 처리 한다.")    
    public void updateProject(ProjectVo projectVo) throws Exception {  
 
    	projectService.updateProject(projectVo);                                            
    }

    /**
     * 프로젝트를 삭제 처리한다.
     *
     * @param  projectVo 프로젝트    
     * @throws Exception
     */
    @ElService(key = "ProjectDel")    
    @RequestMapping(value="ProjectDel")
    @ElDescription(sub = "프로젝트 삭제처리", desc = "프로젝트를 삭제 처리한다.")    
    public void deleteProject(ProjectVo projectVo) throws Exception {
        projectService.deleteProject(projectVo);
    }
   
}
