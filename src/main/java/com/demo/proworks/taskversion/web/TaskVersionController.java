package com.demo.proworks.taskversion.web;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.demo.proworks.taskversion.service.TaskVersionService;
import com.demo.proworks.taskversion.vo.TaskVersionVo;
import com.demo.proworks.taskversion.vo.TaskVersionListVo;
import com.demo.proworks.taskversion.vo.TaskVersionSearchVo;
import com.inswave.elfw.annotation.ElDescription;
import com.inswave.elfw.annotation.ElService;
import com.inswave.elfw.annotation.ElValidator;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @subject : 버전관리를 위한 Task(업무) 정보 관련 처리를 담당하는 컨트롤러
 * @description : 버전관리를 위한 Task(업무) 정보 관련 처리를 담당하는 컨트롤러
 * @author : 남기윤
 * @since : 2025/07/01
 * @modification ===========================================================
 *               DATE AUTHOR DESC
 *               ===========================================================
 *               2025/07/01 남기윤 최초 생성
 * 
 */
@Controller
public class TaskVersionController {

	/** TaskVersionService */
	@Resource(name = "taskVersionServiceImpl")
	private TaskVersionService taskVersionService;
	
	@Value("${aws.bucketName}")
    private String bucketName;

	/**
	 * 버전관리를 위한 Task(업무) 정보 목록을 조회합니다.
	 *
	 * @param taskVersionVo 버전관리를 위한 Task(업무) 정보
	 * @return 목록조회 결과
	 * @throws Exception
	 */
	@ElService(key = "task/{taskId}/version")
	@RequestMapping(value = "task/{taskId}/version")
	@ElDescription(sub = "버전관리를 위한 Task(업무) 정보 목록조회", desc = "페이징을 처리하여 버전관리를 위한 Task(업무) 정보 목록 조회를 한다.")
	public TaskVersionListVo selectTaskVersionByTaskId(TaskVersionSearchVo searchVo) throws Exception {

		searchVo.setPageSize(20);
		searchVo.setStartOffset((searchVo.getPage() - 1) * 20);

		List<TaskVersionVo> taskVersionList = taskVersionService.selectListTaskVersionByTaskId(searchVo);

		long totCnt = taskVersionService.selectListCountTaskVersionByTaskId(searchVo);
		int totalPageCount = (totCnt == 0) ? 0 : (int) Math.ceil((double) totCnt / 10);

		TaskVersionListVo resultVo = new TaskVersionListVo();
		resultVo.setTaskVersionVoList(taskVersionList);
		resultVo.setTotalCount(totalPageCount);
		resultVo.setPageSize(20);
		resultVo.setPageIndex(searchVo.getPageIndex());
		
		System.out.println(resultVo);

		return resultVo;
	}

	/**
	 * 버전관리를 위한 Task(업무) 정보 목록을 조회합니다.
	 *
	 * @param taskVersionVo 버전관리를 위한 Task(업무) 정보
	 * @return 목록조회 결과
	 * @throws Exception
	 */
	@ElService(key = "task/{taskId}/version/{taskVersionId}")
	@RequestMapping(value = "task/{taskId}/version/{taskVersionId}")
	@ElDescription(sub = "버전관리를 위한 Task(업무) 정보 목록조회", desc = "페이징을 처리하여 버전관리를 위한 Task(업무) 정보 목록 조회를 한다.")
	public TaskVersionVo selectTaskVersionByTaskVersionId(@PathVariable int taskId, @PathVariable int taskVersionId)
			throws Exception {
		TaskVersionVo versionVo = new TaskVersionVo();
		versionVo.setTaskVersionId(String.valueOf(taskVersionId));
		versionVo = taskVersionService.selectTaskVersionByVersionId(versionVo);
		return versionVo;
	}

	/**
	 * 버전관리를 위한 Task(업무) 정보 목록을 조회합니다.
	 *
	 * @param taskVersionVo 버전관리를 위한 Task(업무) 정보
	 * @return 목록조회 결과
	 * @throws Exception
	 */
	@ElService(key = "TaskVersionList")
	@RequestMapping(value = "TaskVersionList")
	@ElDescription(sub = "버전관리를 위한 Task(업무) 정보 목록조회", desc = "페이징을 처리하여 버전관리를 위한 Task(업무) 정보 목록 조회를 한다.")
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
	 * 버전관리를 위한 Task(업무) 정보을 단건 조회 처리 한다.
	 *
	 * @param taskVersionVo 버전관리를 위한 Task(업무) 정보
	 * @return 단건 조회 결과
	 * @throws Exception
	 */
	@ElService(key = "TaskVersionUpdView")
	@RequestMapping(value = "TaskVersionUpdView")
	@ElDescription(sub = "버전관리를 위한 Task(업무) 정보 갱신 폼을 위한 조회", desc = "버전관리를 위한 Task(업무) 정보 갱신 폼을 위한 조회를 한다.")
	public TaskVersionVo selectTaskVersion(TaskVersionVo taskVersionVo) throws Exception {
		TaskVersionVo selectTaskVersionVo = taskVersionService.selectTaskVersion(taskVersionVo);

		return selectTaskVersionVo;
	}

	/**
	 * 버전관리를 위한 Task(업무) 정보를 등록 처리 한다.
	 *
	 * @param taskVersionVo 버전관리를 위한 Task(업무) 정보
	 * @throws Exception
	 */
	@ElService(key = "TaskVersionIns")
	@RequestMapping(value = "TaskVersionIns")
	@ElDescription(sub = "버전관리를 위한 Task(업무) 정보 등록처리", desc = "버전관리를 위한 Task(업무) 정보를 등록 처리 한다.")
	public void insertTaskVersion(TaskVersionVo taskVersionVo) throws Exception {
		taskVersionService.insertTaskVersion(taskVersionVo);
	}

	/**
	 * 버전관리를 위한 Task(업무) 정보를 갱신 처리 한다.
	 *
	 * @param taskVersionVo 버전관리를 위한 Task(업무) 정보
	 * @throws Exception
	 */
	@ElService(key = "TaskVersionUpd")
	@RequestMapping(value = "TaskVersionUpd")
	@ElValidator(errUrl = "/taskVersion/taskVersionRegister", errContinue = true)
	@ElDescription(sub = "버전관리를 위한 Task(업무) 정보 갱신처리", desc = "버전관리를 위한 Task(업무) 정보를 갱신 처리 한다.")
	public void updateTaskVersion(TaskVersionVo taskVersionVo) throws Exception {

		taskVersionService.updateTaskVersion(taskVersionVo);
	}

	/**
	 * 버전관리를 위한 Task(업무) 정보를 삭제 처리한다.
	 *
	 * @param taskVersionVo 버전관리를 위한 Task(업무) 정보
	 * @throws Exception
	 */
	@ElService(key = "TaskVersionDel")
	@RequestMapping(value = "TaskVersionDel")
	@ElDescription(sub = "버전관리를 위한 Task(업무) 정보 삭제처리", desc = "버전관리를 위한 Task(업무) 정보를 삭제 처리한다.")
	public void deleteTaskVersion(TaskVersionVo taskVersionVo) throws Exception {
		taskVersionService.deleteTaskVersion(taskVersionVo);
	}

}
