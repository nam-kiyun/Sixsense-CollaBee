package com.demo.proworks.collabee.web;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.demo.proworks.collabee.service.TaskVersionService;
import com.demo.proworks.collabee.vo.TaskVersionVo;
import com.demo.proworks.collabee.vo.TaskVersionListVo;

import com.inswave.elfw.annotation.ElDescription;
import com.inswave.elfw.annotation.ElService;
import com.inswave.elfw.annotation.ElValidator;

/**  
 * @subject     : 업무 버전 관리 관련 처리를 담당하는 컨트롤러
 * @description : 업무 버전 관리 관련 처리를 담당하는 컨트롤러
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
public class TaskVersionController {
	
    /** TaskVersionService */
    @Resource(name = "taskVersionServiceImpl")
    private TaskVersionService taskVersionService;
	
    
    /**
     * 업무 버전 관리 목록을 조회합니다.
     *
     * @param  taskVersionVo 업무 버전 관리
     * @return 목록조회 결과
     * @throws Exception
     */
    @ElService(key="TaskVersionList")
    @RequestMapping(value="TaskVersionList")    
    @ElDescription(sub="업무 버전 관리 목록조회",desc="페이징을 처리하여 업무 버전 관리 목록 조회를 한다.")               
    public TaskVersionListVo selectListTaskVersion(TaskVersionVo taskVersionVo) throws Exception {    	   	

        List<TaskVersionVo> taskVersionList = taskVersionService.selectListTaskVersion(taskVersionVo);                  
        long totCnt = taskVersionService.selectListCountTaskVersion(taskVersionVo);
	
		TaskVersionListVo retTaskVersionList = new TaskVersionListVo();
		retTaskVersionList.setTaskVersionVoList(taskVersionList); 
		retTaskVersionList.setTotalCount(totCnt);
		retTaskVersionList.setPageSize(taskVersionVo.getPageSize());
		retTaskVersionList.setPageIndex(taskVersionVo.getPageIndex());

        return retTaskVersionList;            
    }  
        
    /**
     * 업무 버전 관리을 단건 조회 처리 한다.
     *
     * @param  taskVersionVo 업무 버전 관리
     * @return 단건 조회 결과
     * @throws Exception
     */
    @ElService(key = "TaskVersionUpdView")    
    @RequestMapping(value="TaskVersionUpdView") 
    @ElDescription(sub = "업무 버전 관리 갱신 폼을 위한 조회", desc = "업무 버전 관리 갱신 폼을 위한 조회를 한다.")    
    public TaskVersionVo selectTaskVersion(TaskVersionVo taskVersionVo) throws Exception {
    	TaskVersionVo selectTaskVersionVo = taskVersionService.selectTaskVersion(taskVersionVo);    	    
		
        return selectTaskVersionVo;
    } 
 
    /**
     * 업무 버전 관리를 등록 처리 한다.
     *
     * @param  taskVersionVo 업무 버전 관리
     * @throws Exception
     */
    @ElService(key="TaskVersionIns")    
    @RequestMapping(value="TaskVersionIns")
    @ElDescription(sub="업무 버전 관리 등록처리",desc="업무 버전 관리를 등록 처리 한다.")
    public void insertTaskVersion(TaskVersionVo taskVersionVo) throws Exception {    	 
    	taskVersionService.insertTaskVersion(taskVersionVo);   
    }
       
    /**
     * 업무 버전 관리를 갱신 처리 한다.
     *
     * @param  taskVersionVo 업무 버전 관리
     * @throws Exception
     */
    @ElService(key="TaskVersionUpd")    
    @RequestMapping(value="TaskVersionUpd")    
    @ElValidator(errUrl="/taskVersion/taskVersionRegister", errContinue=true)
    @ElDescription(sub="업무 버전 관리 갱신처리",desc="업무 버전 관리를 갱신 처리 한다.")    
    public void updateTaskVersion(TaskVersionVo taskVersionVo) throws Exception {  
 
    	taskVersionService.updateTaskVersion(taskVersionVo);                                            
    }

    /**
     * 업무 버전 관리를 삭제 처리한다.
     *
     * @param  taskVersionVo 업무 버전 관리    
     * @throws Exception
     */
    @ElService(key = "TaskVersionDel")    
    @RequestMapping(value="TaskVersionDel")
    @ElDescription(sub = "업무 버전 관리 삭제처리", desc = "업무 버전 관리를 삭제 처리한다.")    
    public void deleteTaskVersion(TaskVersionVo taskVersionVo) throws Exception {
        taskVersionService.deleteTaskVersion(taskVersionVo);
    }
   
}
