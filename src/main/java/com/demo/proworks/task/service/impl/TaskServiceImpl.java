package com.demo.proworks.task.service.impl;

import java.util.List;

import javax.annotation.Resource;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.demo.proworks.task.service.TaskService;
import com.demo.proworks.task.vo.TaskUpdateVo;
import com.demo.proworks.task.vo.TaskVo;
import com.demo.proworks.taskversion.dao.TaskVersionDAO;
import com.demo.proworks.taskversion.vo.TaskVersionVo;
import com.demo.proworks.filesrc.dao.FileSrcDAO;
import com.demo.proworks.filesrc.vo.FileSrcVo;
import com.demo.proworks.manager.dao.ManagerDAO;
import com.demo.proworks.manager.vo.ManagerListVo;
import com.demo.proworks.manager.vo.ManagerVo;
import com.demo.proworks.projectuser.dao.ProjectUserDAO;
import com.demo.proworks.projectuser.vo.ProjectUserVo;
import com.demo.proworks.task.dao.TaskDAO;

/**
 * @subject : 업무(Task) 정보 관련 처리를 담당하는 ServiceImpl
 * @description : 업무(Task) 정보 관련 처리를 담당하는 ServiceImpl
 * @author : 남기윤
 * @since : 2025/07/01
 * @modification ===========================================================
 *               DATE AUTHOR DESC
 *               ===========================================================
 *               2025/07/01 남기윤 최초 생성
 * 
 */
@Service("taskServiceImpl")
public class TaskServiceImpl implements TaskService {

	@Resource(name = "taskDAO")
	private TaskDAO taskDAO;

	@Resource(name = "taskVersionDAO")
	private TaskVersionDAO taskVersionDAO;

	@Resource(name = "fileSrcDAO")
	private FileSrcDAO fileSrcDAO;

	@Resource(name = "managerDAO")
	private ManagerDAO managerDAO;

	@Resource(name = "messageSource")
	private MessageSource messageSource;

	@Resource(name = "projectUserDAO")
	private ProjectUserDAO projectUserDAO;

	/**
	 * 업무(Task) 정보를 갱신 처리 한다.
	 *
	 * @process 1. 업무(Task) 정보를 갱신 처리 한다.
	 * 
	 * @param taskVo 업무(Task) 정보 TaskVo
	 * @return 번호
	 * @throws Exception
	 */
	@Transactional(rollbackFor = Exception.class)
	public int saveTask(TaskUpdateVo updateVo) throws Exception {

		// Task 내용 업데이트
		TaskVo taskVo = new TaskVo();

		taskVo.setTaskId(updateVo.getTaskId());
		taskVo.setBoardId(updateVo.getBoardId());
		taskVo.setProjectUserId(updateVo.getProjectUserId());
		taskVo.setProjectRepoId(updateVo.getProjectRepoId());
		taskVo.setTaskTitle(updateVo.getTaskTitle());
		taskVo.setPriority(updateVo.getPriority());
		taskVo.setStartDate(updateVo.getStartDate());
		taskVo.setEndDate(updateVo.getEndDate());
		taskVo.setTags(updateVo.getTags());

		int taskVersionId = taskDAO.updateTask(taskVo);

		// Task 내용 저장
		TaskVersionVo versionVo = new TaskVersionVo();

		versionVo.setTaskVersionId(taskVersionId);
		versionVo.setTaskId(updateVo.getTaskId());
		versionVo.setContent(updateVo.getContent());

		taskVersionDAO.insertTaskVersion(versionVo);

		// File 내용 저장
		FileSrcVo srcVo = new FileSrcVo();

		// S3 저장하는거...
		if (updateVo.getFileSrcVo() != null && !updateVo.getFileSrcVo().isEmpty()) {
			for (FileSrcVo file : updateVo.getFileSrcVo()) {
				fileSrcDAO.insertFileSrc(file);
			}
		}

		fileSrcDAO.insertFileSrc(srcVo);

		if (updateVo.getManagerVo() != null && !updateVo.getManagerVo().isEmpty()) {
			for (ManagerVo manager : updateVo.getManagerVo()) {
				managerDAO.insertManager(manager);
			}
		}

		return 1;
	}

	/**
	 * 업무(Task) 정보 목록을 조회합니다.
	 *
	 * @process 1. 업무(Task) 정보 페이징 처리하여 목록을 조회한다. 2. 결과 List<TaskVo>을(를) 리턴한다.
	 * 
	 * @param taskVo 업무(Task) 정보 TaskVo
	 * @return 업무(Task) 정보 목록 List<TaskVo>
	 * @throws Exception
	 */
	public List<TaskVo> selectListTask(TaskVo taskVo) throws Exception {
		List<TaskVo> list = taskDAO.selectListTask(taskVo);

		return list;
	}

	/**
	 * 조회한 업무(Task) 정보 전체 카운트
	 *
	 * @process 1. 업무(Task) 정보 조회하여 전체 카운트를 리턴한다.
	 * 
	 * @param taskVo 업무(Task) 정보 TaskVo
	 * @return 업무(Task) 정보 목록 전체 카운트
	 * @throws Exception
	 */
	public long selectListCountTask(TaskVo taskVo) throws Exception {
		return taskDAO.selectListCountTask(taskVo);
	}

	/**
	 * 업무(Task) 정보를 상세 조회한다.
	 *
	 * @process 1. 업무(Task) 정보를 상세 조회한다. 2. 결과 TaskVo을(를) 리턴한다.
	 * 
	 * @param taskVo 업무(Task) 정보 TaskVo
	 * @return 단건 조회 결과
	 * @throws Exception
	 */
	/*
	 * public TaskVo selectTask(TaskVo taskVo) throws Exception { TaskVo resultVO =
	 * taskDAO.selectTask(taskVo);
	 * 
	 * return resultVO; }
	 */
	public TaskUpdateVo selectTask(TaskUpdateVo updateVo) throws Exception {
		TaskUpdateVo resultVO = new TaskUpdateVo();

		// Task 세부적인 내용
		TaskVo taskVo = new TaskVo();
		taskVo.setTaskId(updateVo.getTaskId());
		taskVo = taskDAO.selectTask(taskVo);

		resultVO.setTaskId(taskVo.getTaskId());
		resultVO.setBoardId(taskVo.getBoardId());
		resultVO.setProjectUserId(taskVo.getProjectUserId());
		resultVO.setProjectRepoId(taskVo.getProjectRepoId());
		resultVO.setTaskTitle(taskVo.getTaskTitle());
		resultVO.setPriority(taskVo.getPriority());
		resultVO.setStartDate(taskVo.getStartDate());
		resultVO.setEndDate(taskVo.getEndDate());
		resultVO.setTags(taskVo.getTags());
		resultVO.setUserName(taskVo.getUserName());

		// 담당자
		ManagerVo managerVo = new ManagerVo();
		managerVo.setTaskId(updateVo.getTaskId());
		List<ManagerVo> managerListVo = managerDAO.selectManagerByTaskId(managerVo);

		resultVO.setManagerVo(managerListVo);

		// TaskVersion
		TaskVersionVo versionVo = new TaskVersionVo();
		versionVo.setTaskId(updateVo.getTaskId());

		versionVo = taskVersionDAO.selectTaskVersion(versionVo);

		if (versionVo != null) {
			resultVO.setTaskVersionId(versionVo.getTaskVersionId());
			resultVO.setContent(versionVo.getContent());
		} 

		// 파일
		FileSrcVo fileSrcVo = new FileSrcVo();
		fileSrcVo.setTaskVersionId(updateVo.getTaskVersionId());
		List<FileSrcVo> fileSrcListVo = fileSrcDAO.selectFileSrcByTaskVersionId(fileSrcVo);

		resultVO.setFileSrcVo(fileSrcListVo);

		// 프로젝트 구성원
		ProjectUserVo projectUserVo = new ProjectUserVo();
		projectUserVo.setProjectId(updateVo.getProjectId());
		List<ProjectUserVo> ProjectUserListVo = projectUserDAO.selectProjectUserByProjectId(projectUserVo);

		resultVO.setProjectUserVo(ProjectUserListVo);

		return resultVO;
	}

	/**
	 * 업무(Task) 정보를 등록 처리 한다.
	 *
	 * @process 1. 업무(Task) 정보를 등록 처리 한다.
	 * 
	 * @param taskVo 업무(Task) 정보 TaskVo
	 * @return 번호
	 * @throws Exception
	 */
	public int insertTask(TaskVo taskVo) throws Exception {
		return taskDAO.insertTask(taskVo);
	}

	/**
	 * 업무(Task) 정보를 갱신 처리 한다.
	 *
	 * @process 1. 업무(Task) 정보를 갱신 처리 한다.
	 * 
	 * @param taskVo 업무(Task) 정보 TaskVo
	 * @return 번호
	 * @throws Exception
	 */
	public int updateTask(TaskVo taskVo) throws Exception {
		return taskDAO.updateTask(taskVo);
	}

	/**
	 * 업무(Task) 정보를 삭제 처리 한다.
	 *
	 * @process 1. 업무(Task) 정보를 삭제 처리 한다.
	 * 
	 * @param taskVo 업무(Task) 정보 TaskVo
	 * @return 번호
	 * @throws Exception
	 */
	public int deleteTask(TaskVo taskVo) throws Exception {
		return taskDAO.deleteTask(taskVo);
	}

}
