package com.demo.proworks.collabee.web;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.demo.proworks.collabee.service.ProjectLogService;
import com.demo.proworks.collabee.vo.ProjectLogVo;
import com.demo.proworks.collabee.vo.ProjectLogListVo;

import com.inswave.elfw.annotation.ElDescription;
import com.inswave.elfw.annotation.ElService;
import com.inswave.elfw.annotation.ElValidator;

/**  
 * @subject     : 프로젝트 로그 관련 처리를 담당하는 컨트롤러
 * @description : 프로젝트 로그 관련 처리를 담당하는 컨트롤러
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
public class ProjectLogController {
	
    /** ProjectLogService */
    @Resource(name = "projectLogServiceImpl")
    private ProjectLogService projectLogService;
	
    
    /**
     * 프로젝트 로그 목록을 조회합니다.
     *
     * @param  projectLogVo 프로젝트 로그
     * @return 목록조회 결과
     * @throws Exception
     */
    @ElService(key="ProjectLogList")
    @RequestMapping(value="ProjectLogList")    
    @ElDescription(sub="프로젝트 로그 목록조회",desc="페이징을 처리하여 프로젝트 로그 목록 조회를 한다.")               
    public ProjectLogListVo selectListProjectLog(ProjectLogVo projectLogVo) throws Exception {    	   	

        List<ProjectLogVo> projectLogList = projectLogService.selectListProjectLog(projectLogVo);                  
        long totCnt = projectLogService.selectListCountProjectLog(projectLogVo);
	
		ProjectLogListVo retProjectLogList = new ProjectLogListVo();
		retProjectLogList.setProjectLogVoList(projectLogList); 
		retProjectLogList.setTotalCount(totCnt);
		retProjectLogList.setPageSize(projectLogVo.getPageSize());
		retProjectLogList.setPageIndex(projectLogVo.getPageIndex());

        return retProjectLogList;            
    }  
        
    /**
     * 프로젝트 로그을 단건 조회 처리 한다.
     *
     * @param  projectLogVo 프로젝트 로그
     * @return 단건 조회 결과
     * @throws Exception
     */
    @ElService(key = "ProjectLogUpdView")    
    @RequestMapping(value="ProjectLogUpdView") 
    @ElDescription(sub = "프로젝트 로그 갱신 폼을 위한 조회", desc = "프로젝트 로그 갱신 폼을 위한 조회를 한다.")    
    public ProjectLogVo selectProjectLog(ProjectLogVo projectLogVo) throws Exception {
    	ProjectLogVo selectProjectLogVo = projectLogService.selectProjectLog(projectLogVo);    	    
		
        return selectProjectLogVo;
    } 
 
    /**
     * 프로젝트 로그를 등록 처리 한다.
     *
     * @param  projectLogVo 프로젝트 로그
     * @throws Exception
     */
    @ElService(key="ProjectLogIns")    
    @RequestMapping(value="ProjectLogIns")
    @ElDescription(sub="프로젝트 로그 등록처리",desc="프로젝트 로그를 등록 처리 한다.")
    public void insertProjectLog(ProjectLogVo projectLogVo) throws Exception {    	 
    	projectLogService.insertProjectLog(projectLogVo);   
    }
       
    /**
     * 프로젝트 로그를 갱신 처리 한다.
     *
     * @param  projectLogVo 프로젝트 로그
     * @throws Exception
     */
    @ElService(key="ProjectLogUpd")    
    @RequestMapping(value="ProjectLogUpd")    
    @ElValidator(errUrl="/projectLog/projectLogRegister", errContinue=true)
    @ElDescription(sub="프로젝트 로그 갱신처리",desc="프로젝트 로그를 갱신 처리 한다.")    
    public void updateProjectLog(ProjectLogVo projectLogVo) throws Exception {  
 
    	projectLogService.updateProjectLog(projectLogVo);                                            
    }

    /**
     * 프로젝트 로그를 삭제 처리한다.
     *
     * @param  projectLogVo 프로젝트 로그    
     * @throws Exception
     */
    @ElService(key = "ProjectLogDel")    
    @RequestMapping(value="ProjectLogDel")
    @ElDescription(sub = "프로젝트 로그 삭제처리", desc = "프로젝트 로그를 삭제 처리한다.")    
    public void deleteProjectLog(ProjectLogVo projectLogVo) throws Exception {
        projectLogService.deleteProjectLog(projectLogVo);
    }
   
}
