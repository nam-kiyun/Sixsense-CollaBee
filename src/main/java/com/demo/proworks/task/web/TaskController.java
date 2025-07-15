package com.demo.proworks.task.web;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.demo.proworks.task.service.TaskService;
import com.demo.proworks.task.vo.TaskVo;
import com.demo.proworks.task.vo.TaskListVo;

import com.inswave.elfw.annotation.ElDescription;
import com.inswave.elfw.annotation.ElService;
import com.inswave.elfw.annotation.ElValidator;
import org.springframework.web.bind.annotation.RequestMethod;

/**  
 * @subject     : 업무(Task) 정보 관련 처리를 담당하는 컨트롤러
 * @description : 업무(Task) 정보 관련 처리를 담당하는 컨트롤러
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
public class TaskController {
	
    /** TaskService */
    @Resource(name = "taskServiceImpl")
    private TaskService taskService;
	
    
    /**
     * 업무(Task) 정보 목록을 조회합니다.
     *
     * @param  taskVo 업무(Task) 정보
     * @return 목록조회 결과
     * @throws Exception
     */
    @ElService(key="TaskList")
    @RequestMapping(value="TaskList")    
    @ElDescription(sub="업무(Task) 정보 목록조회",desc="페이징을 처리하여 업무(Task) 정보 목록 조회를 한다.")               
    public TaskListVo selectListTask(TaskVo taskVo) throws Exception {    	   	

        List<TaskVo> taskList = taskService.selectListTask(taskVo);                  
        long totCnt = taskService.selectListCountTask(taskVo);
	
		TaskListVo retTaskList = new TaskListVo();
		retTaskList.setTaskVoList(taskList); 
		retTaskList.setTotalCount(totCnt);
		retTaskList.setPageSize(taskVo.getPageSize());
		retTaskList.setPageIndex(taskVo.getPageIndex());

        return retTaskList;            
    }  
        
    /**
     * 업무(Task) 정보을 단건 조회 처리 한다.
     *
     * @param  taskVo 업무(Task) 정보
     * @return 단건 조회 결과
     * @throws Exception
     */
    @ElService(key = "TaskUpdView")    
    @RequestMapping(value="TaskUpdView") 
    @ElDescription(sub = "업무(Task) 정보 갱신 폼을 위한 조회", desc = "업무(Task) 정보 갱신 폼을 위한 조회를 한다.")    
    public TaskVo selectTask(TaskVo taskVo) throws Exception {
    	TaskVo selectTaskVo = taskService.selectTask(taskVo);    	    
		
        return selectTaskVo;
    } 
 
    /**
     * 업무(Task) 정보를 등록 처리 한다.
     *
     * @param  taskVo 업무(Task) 정보
     * @return 생성된 태스크 정보
     * @throws Exception
     */
    @ElService(key = "task/create")    
    @RequestMapping(value = "task/create")
    @ElDescription(sub = "업무(Task) 정보 등록처리", desc = "업무(Task) 정보를 등록 처리 한다.")
    public TaskVo insertTask(TaskVo taskVo) throws Exception {
        System.out.println("TaskController.insertTask - 요청 데이터: " + taskVo.toString());
        
    	int result = taskService.insertTask(taskVo);
    	
    	if (result > 0) {
    	    System.out.println("TaskController.insertTask - 생성 성공, taskId: " + taskVo.getTaskId());
    	    return taskVo; // 생성된 태스크 정보 반환 (AUTO_INCREMENT로 생성된 taskId 포함)
    	} else {
    	    throw new RuntimeException("태스크 생성에 실패했습니다.");
    	}
    }
       
    /**
     * 업무(Task) 정보를 갱신 처리 한다.
     *
     * @param  taskVo 업무(Task) 정보
     * @throws Exception
     */
    @ElService(key="TaskUpd")    
    @RequestMapping(value="TaskUpd")    
    @ElValidator(errUrl="/task/taskRegister", errContinue=true)
    @ElDescription(sub="업무(Task) 정보 갱신처리",desc="업무(Task) 정보를 갱신 처리 한다.")    
    public void updateTask(TaskVo taskVo) throws Exception {  
 
    	taskService.updateTask(taskVo);                                            
    }

    /**
     * 업무(Task) 정보를 삭제 처리한다.
     *
     * @param  taskVo 업무(Task) 정보    
     * @throws Exception
     */
    @ElService(key = "TaskDel")    
    @RequestMapping(value="TaskDel")
    @ElDescription(sub = "업무(Task) 정보 삭제처리", desc = "업무(Task) 정보를 삭제 처리한다.")    
    public void deleteTask(TaskVo taskVo) throws Exception {
        taskService.deleteTask(taskVo);
    }
   
}