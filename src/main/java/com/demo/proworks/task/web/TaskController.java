package com.demo.proworks.task.web;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.demo.proworks.task.service.TaskService;
import com.demo.proworks.task.vo.TaskVo;
import com.demo.proworks.taskversion.service.TaskVersionService;
import com.demo.proworks.taskversion.vo.TaskVersionVo;
import com.demo.proworks.task.vo.TaskListVo;
import com.demo.proworks.task.vo.TaskUpdateVo;
import com.inswave.elfw.annotation.ElDescription;
import com.inswave.elfw.annotation.ElService;
import com.inswave.elfw.annotation.ElValidator;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @subject : 업무(Task) 정보 관련 처리를 담당하는 컨트롤러
 * @description : 업무(Task) 정보 관련 처리를 담당하는 컨트롤러
 * @author : 남기윤
 * @since : 2025/07/01
 * @modification ===========================================================
 *               DATE AUTHOR DESC
 *               ===========================================================
 *               2025/07/01 남기윤 최초 생성
 * 
 */
@Controller
public class TaskController {

	/** TaskService */
	@Resource(name = "taskServiceImpl")
	private TaskService taskService;

	/** TaskVersionService */
	@Resource(name = "taskVersionServiceImpl")
	private TaskVersionService taskVersionService;

	/**
	 * 업무(Task) 정보와 업무버전(TaskVersion)을 처리함
	 *
	 * @param taskVo 업무(Task) 정보
	 * @throws Exception
	 */
	@ElService(key = "task/update")
	@RequestMapping(value = "task/update")
	@ElDescription(sub = "업무(Task) 정보와 업무버전(TaskVersion)을 처리", desc = "업무(Task) 정보와 업무버전(TaskVersion)을 처리한다")
	public void saveTask(TaskUpdateVo updateVo) throws Exception {
		taskService.saveTask(updateVo);
	}

	/**
	 * 업무(Task) 정보 목록을 조회합니다.
	 *
	 * @param taskVo 업무(Task) 정보
	 * @return 목록조회 결과
	 * @throws Exception
	 */
	 /*
	@ElService(key = "TaskList")
	@RequestMapping(value = "TaskList")
	@ElDescription(sub = "업무(Task) 정보 목록조회", desc = "페이징을 처리하여 업무(Task) 정보 목록 조회를 한다.")
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
*/

	/**
	 * 업무(Task) 정보을 단건 조회 처리 한다.
	 *
	 * @param taskVo 업무(Task) 정보
	 * @return 단건 조회 결과
	 * @throws Exception
	 */
	@ElService(key = "task/{taskId}")
	@RequestMapping(value = "task/{taskId}")
	@ElDescription(sub = "업무(Task) 정보 갱신 폼을 위한 조회", desc = "업무(Task) 정보 갱신 폼을 위한 조회를 한다.")
	public TaskUpdateVo selectTask(@PathVariable("taskId") int taskId, @RequestParam(value = "projectId", required = false) Integer projectId) throws Exception {
		TaskUpdateVo taskVo = new TaskUpdateVo();
		taskVo.setTaskId(taskId);
		taskVo.setProjectId(projectId);
		
		TaskUpdateVo selectTaskVo = taskService.selectTask(taskVo);

		return selectTaskVo;
	}

	/**
	 * 업무(Task) 정보를 등록 처리 한다.
	 *
	 * @param taskVo 업무(Task) 정보
	 * @throws Exception
	 */
	@ElService(key = "task/create")
	@RequestMapping(value = "task/create")
	@ElDescription(sub = "업무(Task) 정보 등록처리", desc = "업무(Task) 정보를 등록 처리 한다.")
	public void insertTask(TaskVo taskVo) throws Exception {
		taskService.insertTask(taskVo);
	}

	/**
	 * 업무(Task) 정보를 갱신 처리 한다.
	 *
	 * @param taskVo 업무(Task) 정보
	 * @throws Exception
	 */
	/*
	@ElService(key = "TaskUpd")
	@RequestMapping(value = "TaskUpd")
	@ElValidator(errUrl = "/task/taskRegister", errContinue = true)
	@ElDescription(sub = "업무(Task) 정보 갱신처리", desc = "업무(Task) 정보를 갱신 처리 한다.")
	public void updateTask(TaskVo taskVo) throws Exception {

		taskService.updateTask(taskVo);
	}
	 */

	/**
	 * 업무(Task) 정보를 삭제 처리한다.
	 *
	 * @param taskVo 업무(Task) 정보
	 * @throws Exception
	 */
	@ElService(key = "task/delete")
	@RequestMapping(value = "task/delete")
	@ElDescription(sub = "업무(Task) 정보 삭제처리", desc = "업무(Task) 정보를 삭제 처리한다.")
	public void deleteTask(TaskVo taskVo) throws Exception {
		taskService.deleteTask(taskVo);
	}

}
