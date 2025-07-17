package com.demo.proworks.task.service.impl;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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
import com.demo.proworks.filesrc.vo.FileSrcListVo;
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
		taskVo.setProjectRepoId(isEmpty(updateVo.getProjectRepoId()) ? "0" : updateVo.getProjectRepoId());
		taskVo.setTaskTitle(updateVo.getTaskTitle());
		taskVo.setPriority(isEmpty(updateVo.getPriority()) ? null : updateVo.getPriority());
		taskVo.setStartDate(isEmpty(updateVo.getStartDate()) ? null : updateVo.getStartDate());
		taskVo.setEndDate(isEmpty(updateVo.getEndDate()) ? null : updateVo.getEndDate());
		taskVo.setTags(isEmpty(updateVo.getTags()) ? null : updateVo.getTags());

		taskDAO.updateTask(taskVo);

		handleManagerUpdate(updateVo.getTaskId(), updateVo.getManagerVo());

		// Task 내용 저장
		TaskVersionVo versionVo = new TaskVersionVo();

		versionVo.setTaskId(updateVo.getTaskId());
		versionVo.setContent(updateVo.getContent());

		String taskVersionId = taskVersionDAO.insertTaskVersion(versionVo);

		// fileSrc 저장
		List<FileSrcVo> fileSrcVos = updateVo.getFileSrcVo();
		if (fileSrcVos != null && !fileSrcVos.isEmpty()) {
			for (FileSrcVo vo : fileSrcVos) {
				vo.setTaskVersionId(taskVersionId); // 여기에 주입!
				System.out.println(vo);
			}

			// 3️⃣ 리스트 insert
			fileSrcDAO.insertFileSrcList(fileSrcVos);
		}

		return 1;
	}

	private boolean isEmpty(Object value) {
		if (value == null)
			return true;
		if (value instanceof String)
			return ((String) value).trim().isEmpty();
		if (value instanceof Number) {
			return ((Number) value).intValue() == 0;
		}
		if (value instanceof Collection) {
			return ((Collection<?>) value).isEmpty();
		}

		if (value instanceof Map) {
			return ((Map<?, ?>) value).isEmpty();
		}

		if (value.getClass().isArray()) {
			return Array.getLength(value) == 0;
		}
		return false;
	}

	private void handleManagerUpdate(String taskId, List<ManagerVo> currentManagerVo) throws Exception {
		if (taskId == "0" || taskId == null || taskId == "undefined")
			return;

		ManagerVo baseVo = new ManagerVo();
		baseVo.setTaskId(taskId);

		currentManagerVo = currentManagerVo.stream()
				.filter(vo -> vo.getUserId() != null && !vo.getUserId().trim().isEmpty() && vo.getUserId() != "") 
				.collect(Collectors.toList());

		List<ManagerVo> prevManagerVo = managerDAO.selectManagerByTaskId(baseVo);

		if (prevManagerVo == null || prevManagerVo.size() == 0)
			prevManagerVo = new ArrayList<>();
		if (currentManagerVo == null || currentManagerVo.size() == 0)
			currentManagerVo = new ArrayList<>();

		boolean isCurrentEmpty = currentManagerVo.isEmpty();
		boolean isPrevEmpty = prevManagerVo.isEmpty();

		// 현재, 과거 둘 다 비어있으면 아무 작업 하지 않고 리턴
		if (isCurrentEmpty && isPrevEmpty)
			return;

		// 현재만 비어있으면 해당 taskId 기준으로 전체 삭제 후 리턴
		if (isCurrentEmpty) {
			managerDAO.deleteManagerByTaskId(baseVo);
			return;
		}

		// 변경사항 반영
		Map<String, ManagerVo> prevMap = prevManagerVo.stream()
				.collect(Collectors.toMap(ManagerVo::getUserId, vo -> vo));

		Set<String> prevUserIds = prevMap.keySet();
		Set<String> currentUserIds = currentManagerVo.stream().map(ManagerVo::getUserId).collect(Collectors.toSet());

		Set<String> toDelete = new HashSet<>(prevUserIds);
		toDelete.removeAll(currentUserIds);

		for (String userId : toDelete) {
			ManagerVo target = prevMap.get(userId);
			if (target != null && target.getManagerId() != "0") {
				managerDAO.deleteManager(target);
			}
		}

		Set<String> toInsert = new HashSet<>(currentUserIds);
		toInsert.removeAll(prevUserIds);

		for (ManagerVo newVo : currentManagerVo) {
			if (toInsert.contains(newVo.getUserId())) {
				newVo.setTaskId(taskId);
				managerDAO.insertManager(newVo);
			}
		}
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
		fileSrcVo.setTaskVersionId(resultVO.getTaskVersionId());
		System.out.println(updateVo.getTaskVersionId());
		List<FileSrcVo> fileSrcListVo = fileSrcDAO.selectFileSrcByTaskVersionId(fileSrcVo);
		System.out.println(fileSrcListVo);

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
