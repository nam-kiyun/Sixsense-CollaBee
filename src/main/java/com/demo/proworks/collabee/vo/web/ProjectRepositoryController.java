package com.demo.proworks.collabee.vo.web;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.demo.proworks.collabee.vo.service.ProjectRepositoryService;
import com.demo.proworks.collabee.vo.vo.ProjectRepositoryVo;
import com.demo.proworks.collabee.vo.vo.ProjectRepositoryListVo;

import com.inswave.elfw.annotation.ElDescription;
import com.inswave.elfw.annotation.ElService;
import com.inswave.elfw.annotation.ElValidator;

/**  
 * @subject     : 프로젝트와연결된레포지토리 관련 처리를 담당하는 컨트롤러
 * @description : 프로젝트와연결된레포지토리 관련 처리를 담당하는 컨트롤러
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
public class ProjectRepositoryController {
	
    /** ProjectRepositoryService */
    @Resource(name = "projectRepositoryServiceImpl")
    private ProjectRepositoryService projectRepositoryService;
	
    
    /**
     * 프로젝트와연결된레포지토리 목록을 조회합니다.
     *
     * @param  projectRepositoryVo 프로젝트와연결된레포지토리
     * @return 목록조회 결과
     * @throws Exception
     */
    @ElService(key="ProjectRepositoryList")
    @RequestMapping(value="ProjectRepositoryList")    
    @ElDescription(sub="프로젝트와연결된레포지토리 목록조회",desc="페이징을 처리하여 프로젝트와연결된레포지토리 목록 조회를 한다.")               
    public ProjectRepositoryListVo selectListProjectRepository(ProjectRepositoryVo projectRepositoryVo) throws Exception {    	   	

        List<ProjectRepositoryVo> projectRepositoryList = projectRepositoryService.selectListProjectRepository(projectRepositoryVo);                  
        long totCnt = projectRepositoryService.selectListCountProjectRepository(projectRepositoryVo);
	
		ProjectRepositoryListVo retProjectRepositoryList = new ProjectRepositoryListVo();
		retProjectRepositoryList.setProjectRepositoryVoList(projectRepositoryList); 
		retProjectRepositoryList.setTotalCount(totCnt);
		retProjectRepositoryList.setPageSize(projectRepositoryVo.getPageSize());
		retProjectRepositoryList.setPageIndex(projectRepositoryVo.getPageIndex());

        return retProjectRepositoryList;            
    }  
        
    /**
     * 프로젝트와연결된레포지토리을 단건 조회 처리 한다.
     *
     * @param  projectRepositoryVo 프로젝트와연결된레포지토리
     * @return 단건 조회 결과
     * @throws Exception
     */
    @ElService(key = "ProjectRepositoryUpdView")    
    @RequestMapping(value="ProjectRepositoryUpdView") 
    @ElDescription(sub = "프로젝트와연결된레포지토리 갱신 폼을 위한 조회", desc = "프로젝트와연결된레포지토리 갱신 폼을 위한 조회를 한다.")    
    public ProjectRepositoryVo selectProjectRepository(ProjectRepositoryVo projectRepositoryVo) throws Exception {
    	ProjectRepositoryVo selectProjectRepositoryVo = projectRepositoryService.selectProjectRepository(projectRepositoryVo);    	    
		
        return selectProjectRepositoryVo;
    } 
 
    /**
     * 프로젝트와연결된레포지토리를 등록 처리 한다.
     *
     * @param  projectRepositoryVo 프로젝트와연결된레포지토리
     * @throws Exception
     */
    @ElService(key="ProjectRepositoryIns")    
    @RequestMapping(value="ProjectRepositoryIns")
    @ElDescription(sub="프로젝트와연결된레포지토리 등록처리",desc="프로젝트와연결된레포지토리를 등록 처리 한다.")
    public void insertProjectRepository(ProjectRepositoryVo projectRepositoryVo) throws Exception {    	 
    	projectRepositoryService.insertProjectRepository(projectRepositoryVo);   
    }
       
    /**
     * 프로젝트와연결된레포지토리를 갱신 처리 한다.
     *
     * @param  projectRepositoryVo 프로젝트와연결된레포지토리
     * @throws Exception
     */
    @ElService(key="ProjectRepositoryUpd")    
    @RequestMapping(value="ProjectRepositoryUpd")    
    @ElValidator(errUrl="/projectRepository/projectRepositoryRegister", errContinue=true)
    @ElDescription(sub="프로젝트와연결된레포지토리 갱신처리",desc="프로젝트와연결된레포지토리를 갱신 처리 한다.")    
    public void updateProjectRepository(ProjectRepositoryVo projectRepositoryVo) throws Exception {  
 
    	projectRepositoryService.updateProjectRepository(projectRepositoryVo);                                            
    }

    /**
     * 프로젝트와연결된레포지토리를 삭제 처리한다.
     *
     * @param  projectRepositoryVo 프로젝트와연결된레포지토리    
     * @throws Exception
     */
    @ElService(key = "ProjectRepositoryDel")    
    @RequestMapping(value="ProjectRepositoryDel")
    @ElDescription(sub = "프로젝트와연결된레포지토리 삭제처리", desc = "프로젝트와연결된레포지토리를 삭제 처리한다.")    
    public void deleteProjectRepository(ProjectRepositoryVo projectRepositoryVo) throws Exception {
        projectRepositoryService.deleteProjectRepository(projectRepositoryVo);
    }
   
}
