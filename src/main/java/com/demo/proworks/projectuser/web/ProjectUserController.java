package com.demo.proworks.projectuser.web;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.demo.proworks.projectuser.service.ProjectUserService;
import com.demo.proworks.projectuser.vo.ProjectUserVo;
import com.demo.proworks.projectuser.vo.ProjectUserListVo;

import com.inswave.elfw.annotation.ElDescription;
import com.inswave.elfw.annotation.ElService;
import com.inswave.elfw.annotation.ElValidator;

/**  
 * @subject     : 프로젝트에 초대(참가)한 사람들 관련 처리를 담당하는 컨트롤러
 * @description : 프로젝트에 초대(참가)한 사람들 관련 처리를 담당하는 컨트롤러
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
public class ProjectUserController {
	
    /** ProjectUserService */
    @Resource(name = "projectUserServiceImpl")
    private ProjectUserService projectUserService;
	
    
    /**
     * 프로젝트에 초대(참가)한 사람들 목록을 조회합니다.
     *
     * @param  projectUserVo 프로젝트에 초대(참가)한 사람들
     * @return 목록조회 결과
     * @throws Exception
     */
    @ElService(key="ProjectUserList")
    @RequestMapping(value="ProjectUserList")    
    @ElDescription(sub="프로젝트에 초대(참가)한 사람들 목록조회",desc="페이징을 처리하여 프로젝트에 초대(참가)한 사람들 목록 조회를 한다.")               
    public ProjectUserListVo selectListProjectUser(ProjectUserVo projectUserVo) throws Exception {    	   	

        List<ProjectUserVo> projectUserList = projectUserService.selectListProjectUser(projectUserVo);                  
        long totCnt = projectUserService.selectListCountProjectUser(projectUserVo);
	
		ProjectUserListVo retProjectUserList = new ProjectUserListVo();
		retProjectUserList.setProjectUserVoList(projectUserList); 
		retProjectUserList.setTotalCount(totCnt);
		retProjectUserList.setPageSize(projectUserVo.getPageSize());
		retProjectUserList.setPageIndex(projectUserVo.getPageIndex());

        return retProjectUserList;            
    }  
        
    /**
     * 프로젝트에 초대(참가)한 사람들을 단건 조회 처리 한다.
     *
     * @param  projectUserVo 프로젝트에 초대(참가)한 사람들
     * @return 단건 조회 결과
     * @throws Exception
     */
    @ElService(key = "ProjectUserUpdView")    
    @RequestMapping(value="ProjectUserUpdView") 
    @ElDescription(sub = "프로젝트에 초대(참가)한 사람들 갱신 폼을 위한 조회", desc = "프로젝트에 초대(참가)한 사람들 갱신 폼을 위한 조회를 한다.")    
    public ProjectUserVo selectProjectUser(ProjectUserVo projectUserVo) throws Exception {
    	ProjectUserVo selectProjectUserVo = projectUserService.selectProjectUser(projectUserVo);    	    
		
        return selectProjectUserVo;
    } 
 
    /**
     * 프로젝트에 초대(참가)한 사람들를 등록 처리 한다.
     *
     * @param  projectUserVo 프로젝트에 초대(참가)한 사람들
     * @throws Exception
     */
    @ElService(key="ProjectUserIns")    
    @RequestMapping(value="ProjectUserIns")
    @ElDescription(sub="프로젝트에 초대(참가)한 사람들 등록처리",desc="프로젝트에 초대(참가)한 사람들를 등록 처리 한다.")
    public void insertProjectUser(ProjectUserVo projectUserVo) throws Exception {    	 
    	projectUserService.insertProjectUser(projectUserVo);   
    }
       
    /**
     * 프로젝트에 초대(참가)한 사람들를 갱신 처리 한다.
     *
     * @param  projectUserVo 프로젝트에 초대(참가)한 사람들
     * @throws Exception
     */
    @ElService(key="ProjectUserUpd")    
    @RequestMapping(value="ProjectUserUpd")    
    @ElValidator(errUrl="/projectUser/projectUserRegister", errContinue=true)
    @ElDescription(sub="프로젝트에 초대(참가)한 사람들 갱신처리",desc="프로젝트에 초대(참가)한 사람들를 갱신 처리 한다.")    
    public void updateProjectUser(ProjectUserVo projectUserVo) throws Exception {  
 
    	projectUserService.updateProjectUser(projectUserVo);                                            
    }

    /**
     * 프로젝트에 초대(참가)한 사람들를 삭제 처리한다.
     *
     * @param  projectUserVo 프로젝트에 초대(참가)한 사람들    
     * @throws Exception
     */
    @ElService(key = "ProjectUserDel")    
    @RequestMapping(value="ProjectUserDel")
    @ElDescription(sub = "프로젝트에 초대(참가)한 사람들 삭제처리", desc = "프로젝트에 초대(참가)한 사람들를 삭제 처리한다.")    
    public void deleteProjectUser(ProjectUserVo projectUserVo) throws Exception {
        projectUserService.deleteProjectUser(projectUserVo);
    }
   
}
