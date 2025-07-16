package com.demo.proworks.task.service.impl;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.demo.proworks.task.service.TaskService;
import com.demo.proworks.task.vo.TaskVo;
import com.demo.proworks.task.dao.TaskDAO;
import com.demo.proworks.project.dao.ProjectDAO;
import com.demo.proworks.project.vo.ProjectVo;
import com.demo.proworks.projectrepo.dao.ProjectRepositoryDAO;
import com.demo.proworks.projectrepo.vo.ProjectRepositoryVo;
import com.demo.proworks.board.dao.BoardDAO;
import com.demo.proworks.board.vo.BoardVo;
import com.demo.proworks.projectuser.dao.ProjectUserDAO;
import com.demo.proworks.projectuser.vo.ProjectUserVo;
import com.demo.proworks.redis.service.KanbanRedisService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;

/**  
 * @subject     : 업무(Task) 정보 관련 처리를 담당하는 ServiceImpl
 * @description	: 업무(Task) 정보 관련 처리를 담당하는 ServiceImpl
 * @author      : 남기윤
 * @since       : 2025/07/01
 * @modification
 * ===========================================================
 * DATE				AUTHOR				DESC
 * ===========================================================
 * 2025/07/01			 남기윤	 		최초 생성
 * 
 */
@Service("taskServiceImpl")
public class TaskServiceImpl implements TaskService {

    @Resource(name="taskDAO")
    private TaskDAO taskDAO;
    
    @Resource(name="projectDAO")
    private ProjectDAO projectDAO;
    
    @Resource(name="projectRepositoryDAO")
    private ProjectRepositoryDAO projectRepositoryDAO;
    
    @Resource(name="boardDAO")
    private BoardDAO boardDAO;
    
    @Resource(name="projectUserDAO")
    private ProjectUserDAO projectUserDAO;
    
    @Resource(name="kanbanRedisService")
    private KanbanRedisService kanbanRedisService;
	
	@Resource(name = "messageSource")
	private MessageSource messageSource;
	
	private final ObjectMapper objectMapper;
	
	// 생성자에서 ObjectMapper 설정
	public TaskServiceImpl() {
		this.objectMapper = new ObjectMapper();
		// @JsonFilter 어노테이션 무시 설정
		this.objectMapper.configure(MapperFeature.USE_ANNOTATIONS, false);
		this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}

    /**
     * 업무(Task) 정보 목록을 조회합니다.
     *
     * @process
     * 1. 업무(Task) 정보 페이징 처리하여 목록을 조회한다.
     * 2. 결과 List<TaskVo>을(를) 리턴한다.
     * 
     * @param  taskVo 업무(Task) 정보 TaskVo
     * @return 업무(Task) 정보 목록 List<TaskVo>
     * @throws Exception
     */
	@SuppressWarnings("unchecked")
	public List<TaskVo> selectListTask(TaskVo taskVo) throws Exception {
		// 1. boardId를 통해 프로젝트 ID 조회 후 Redis 캐시 활용
		String projectId = null;
		if (taskVo.getBoardId() != null && kanbanRedisService.isRedisConnected()) {
			try {
				projectId = this.getProjectIdByBoardId(taskVo.getBoardId());
				if (projectId != null) {
					// Redis에서 프로젝트 태스크 목록 조회
					List<java.util.Map<String, Object>> cachedTasks = kanbanRedisService.getProjectTasksFromCache(projectId);
					
					if (cachedTasks != null && !cachedTasks.isEmpty()) {
						// 캐시된 데이터를 TaskVo 리스트로 변환
						List<TaskVo> taskList = new java.util.ArrayList<>();
						for (java.util.Map<String, Object> taskMap : cachedTasks) {
							TaskVo task = convertMapToTaskVo(taskMap);
							taskList.add(task);
						}
						
						System.out.println("✅ Redis 캐시에서 태스크 목록 조회 성공: " + projectId + " (" + taskList.size() + "개)");
						return taskList;
					}
				}
			} catch (Exception e) {
				System.err.println("❌ Redis 캐시 조회 실패, DB 조회로 대체: " + e.getMessage());
			}
		}
		
		// 2. 캐시에 없거나 Redis 연결 실패 시 DB에서 조회
		List<TaskVo> list = taskDAO.selectListTask(taskVo);
		
		// 태그 필터링은 프론트엔드에서 처리
		
		// 3. 프로젝트 ID가 있고 Redis 연결 가능한 경우 캐시에 저장
		if (projectId != null && kanbanRedisService.isRedisConnected() && list != null && !list.isEmpty()) {
			try {
				// TaskVo 리스트를 Map 리스트로 변환하여 캐시에 저장
				List<java.util.Map<String, Object>> taskMapList = new java.util.ArrayList<>();
				for (TaskVo task : list) {
					java.util.Map<String, Object> taskMap = convertTaskVoToMap(task);
					taskMapList.add(taskMap);
				}
				
				kanbanRedisService.cacheProjectTasks(projectId, taskMapList);
				System.out.println("🔧 DB 조회 결과를 Redis 캐시에 저장: " + projectId + " (" + list.size() + "개)");
			} catch (Exception e) {
				System.err.println("❌ Redis 캐시 저장 실패: " + e.getMessage());
			}
		}

		return list;
	}

    /**
     * 조회한 업무(Task) 정보 전체 카운트
     *
     * @process
     * 1. 업무(Task) 정보 조회하여 전체 카운트를 리턴한다.
     * 
     * @param  taskVo 업무(Task) 정보 TaskVo
     * @return 업무(Task) 정보 목록 전체 카운트
     * @throws Exception
     */
	public long selectListCountTask(TaskVo taskVo) throws Exception {
		return taskDAO.selectListCountTask(taskVo);
	}

    /**
     * 업무(Task) 정보를 상세 조회한다.
     *
     * @process
     * 1. 업무(Task) 정보를 상세 조회한다.
     * 2. 결과 TaskVo을(를) 리턴한다.
     * 
     * @param  taskVo 업무(Task) 정보 TaskVo
     * @return 단건 조회 결과
     * @throws Exception
     */
	public TaskVo selectTask(TaskVo taskVo) throws Exception {
		TaskVo resultVO = taskDAO.selectTask(taskVo);			
        
        return resultVO;
	}

    /**
     * 업무(Task) 정보를 등록 처리 한다.
     *
     * @process
     * 1. 업무(Task) 정보를 등록 처리 한다.
     * 2. 필수 외래키 값들을 실제 DB에서 조회하여 설정한다.
     * 
     * @param  taskVo 업무(Task) 정보 TaskVo
     * @return 번호
     * @throws Exception
     */
	public int insertTask(TaskVo taskVo) throws Exception {
		System.out.println("TaskService.insertTask - 입력 데이터: " + taskVo.toString());
		
		// 필수 값 유효성 검사
		if (taskVo.getTaskTitle() == null || taskVo.getTaskTitle().trim().isEmpty()) {
			throw new IllegalArgumentException("태스크 제목은 필수입니다.");
		}
		
		if (taskVo.getBoardId() == null || taskVo.getBoardId().trim().isEmpty()) {
			throw new IllegalArgumentException("보드 ID는 필수입니다.");
		}
		
		// 기본값 설정
		if (taskVo.getPriority() == null) {
			taskVo.setPriority("MEDIUM"); // 기본 우선순위
		}
		
		// 1. boardId로 해당 보드의 프로젝트 정보 조회
		String projectId = this.getProjectIdByBoardId(taskVo.getBoardId());
		if (projectId == null) {
			throw new IllegalArgumentException("보드에 연결된 프로젝트를 찾을 수 없습니다.");
		}
		System.out.println("조회된 프로젝트 ID: " + projectId);
		
		// 2. PROJECT_USER_ID 조회 (세션의 userId와 projectId로 조회)
		String currentUserId = this.getCurrentUserIdFromSession();
		if (currentUserId == null) {
			throw new IllegalArgumentException("로그인 정보를 찾을 수 없습니다. 다시 로그인해주세요.");
		}
		System.out.println("세션에서 가져온 현재 사용자 ID: " + currentUserId);
		
		String projectUserId = this.getProjectUserIdByUserIdAndProjectId(currentUserId, projectId);
		if (projectUserId == null) {
			throw new IllegalArgumentException("해당 프로젝트에 참여하지 않은 사용자입니다.");
		}
		taskVo.setProjectUserId(projectUserId);
		System.out.println("조회된 PROJECT_USER_ID: " + projectUserId);
		
		// 3. PROJECT_REPO_ID 조회 (projectId로 조회, 없으면 null)
		String projectRepoId = this.getProjectRepoIdByProjectId(projectId);
		taskVo.setProjectRepoId(projectRepoId); // null일 수 있음
		System.out.println("조회된 PROJECT_REPO_ID: " + projectRepoId);
		
		System.out.println("TaskService.insertTask - 처리 전 데이터: " + taskVo.toString());
		
		int result = taskDAO.insertTask(taskVo);
		
		System.out.println("TaskService.insertTask - DB 삽입 결과: " + result + ", 생성된 taskId: " + taskVo.getTaskId());
		
		// 새 태스크 생성 시 Redis 캐시 무효화 (최신 데이터 보장)
		if (result > 0 && projectId != null && kanbanRedisService.isRedisConnected()) {
			try {
				kanbanRedisService.invalidateProjectCache(projectId);
				System.out.println("🗑️ 새 태스크 생성으로 인한 프로젝트 캐시 무효화: " + projectId);
			} catch (Exception e) {
				System.err.println("❌ 캐시 무효화 실패: " + e.getMessage());
			}
		}
		
		return result;
	}
	
	/**
	 * boardId로 해당 보드의 프로젝트 ID를 조회한다.
	 */
	private String getProjectIdByBoardId(String boardId) throws Exception {
		try {
			BoardVo searchVo = new BoardVo();
			searchVo.setBoardId(boardId);
			
			BoardVo boardInfo = boardDAO.selectBoard(searchVo);
			
			if (boardInfo != null && boardInfo.getProjectId() != null) {
				return boardInfo.getProjectId();
			}
			
			System.out.println("보드 정보를 찾을 수 없습니다. boardId: " + boardId);
			return null;
		} catch (Exception e) {
			System.out.println("보드 정보 조회 실패: " + e.getMessage());
			throw new Exception("보드 정보 조회 중 오류가 발생했습니다.", e);
		}
	}
	
	/**
	 * userId와 projectId로 PROJECT_USER 테이블에서 PROJECT_USER_ID를 조회한다.
	 */
	private String getProjectUserIdByUserIdAndProjectId(String userId, String projectId) throws Exception {
		try {
			ProjectUserVo searchVo = new ProjectUserVo();
			searchVo.setUserId(userId);
			searchVo.setProjectId(projectId);
			
			System.out.println("PROJECT_USER 테이블 조회 - userId: " + userId + ", projectId: " + projectId);
			
			// PROJECT_USER 테이블에서 해당 사용자와 프로젝트에 대한 레코드 조회
			ProjectUserVo projectUser = projectUserDAO.selectProjectUser(searchVo);
			
			if (projectUser != null && projectUser.getProjectUserId() != null) {
				System.out.println("PROJECT_USER 조회 성공 - PROJECT_USER_ID: " + projectUser.getProjectUserId());
				return projectUser.getProjectUserId();
			} else {
				System.out.println("PROJECT_USER 테이블에서 해당 사용자를 찾을 수 없습니다.");
				return null;
			}
		} catch (Exception e) {
			System.out.println("PROJECT_USER_ID 조회 실패: " + e.getMessage());
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * projectId로 PROJECT_REPOSITORY 테이블에서 PROJECT_REPO_ID를 조회한다.
	 */
	private String getProjectRepoIdByProjectId(String projectId) throws Exception {
		try {
			ProjectRepositoryVo searchVo = new ProjectRepositoryVo();
			searchVo.setProjectId(projectId);
			
			List<ProjectRepositoryVo> repoList = projectRepositoryDAO.selectListProjectRepository(searchVo);
			
			if (repoList != null && !repoList.isEmpty()) {
				// 첫 번째 리포지토리 ID 반환
				return repoList.get(0).getProjectRepoId();
			}
			
			return null; // 연결된 리포지토리가 없음
		} catch (Exception e) {
			System.out.println("PROJECT_REPO_ID 조회 실패: " + e.getMessage());
			return null;
		}
	}
	
	/**
	 * 세션에서 현재 로그인한 사용자 ID를 가져온다.
	 */
	private String getCurrentUserIdFromSession() {
		try {
			ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
			if (attributes != null) {
				HttpServletRequest request = attributes.getRequest();
				
				// 1. 세션에서 사용자 정보 조회 (다양한 키 시도)
				Object userIdObj = request.getSession().getAttribute("userId");
				if (userIdObj != null) {
					System.out.println("세션에서 userId 발견: " + userIdObj);
					return userIdObj.toString();
				}
				
				userIdObj = request.getSession().getAttribute("userEmail");
				if (userIdObj != null) {
					System.out.println("세션에서 userEmail 발견: " + userIdObj);
					return userIdObj.toString();
				}
				
				userIdObj = request.getSession().getAttribute("user_id");
				if (userIdObj != null) {
					System.out.println("세션에서 user_id 발견: " + userIdObj);
					return userIdObj.toString();
				}
				
				userIdObj = request.getSession().getAttribute("loginUserId");
				if (userIdObj != null) {
					System.out.println("세션에서 loginUserId 발견: " + userIdObj);
					return userIdObj.toString();
				}
				
				// 2. 요청 파라미터에서 사용자 정보 조회 (백업)
				String userIdParam = request.getParameter("userId");
				if (userIdParam != null && !userIdParam.trim().isEmpty()) {
					System.out.println("요청 파라미터에서 userId 발견: " + userIdParam);
					return userIdParam;
				}
				
				// 3. 세션의 모든 속성 로깅 (디버깅용)
				System.out.println("=== 세션 속성 목록 ===");
				java.util.Enumeration<String> sessionNames = request.getSession().getAttributeNames();
				while (sessionNames.hasMoreElements()) {
					String name = sessionNames.nextElement();
					Object value = request.getSession().getAttribute(name);
					System.out.println("세션 속성: " + name + " = " + value);
				}
				System.out.println("=== 세션 속성 목록 끝 ===");
			}
			
			// 4. 세션 정보를 찾을 수 없는 경우 임시값 반환 (개발/테스트용)
			System.out.println("세션에서 사용자 정보를 찾을 수 없어 임시값 사용: user01");
			return "user01"; // 개발/테스트용 임시값
			
		} catch (Exception e) {
			System.out.println("세션 정보 조회 중 오류 발생: " + e.getMessage());
			e.printStackTrace();
			// 오류 발생 시에도 임시값 반환 (개발/테스트용)
			return "user01";
		}
	}
	
    /**
     * 업무(Task) 정보를 갱신 처리 한다.
     *
     * @process
     * 1. 업무(Task) 정보를 갱신 처리 한다.
     * 
     * @param  taskVo 업무(Task) 정보 TaskVo
     * @return 번호
     * @throws Exception
     */
	public int updateTask(TaskVo taskVo) throws Exception {
		// 1. 프로젝트 ID 조회 (캐시 무효화를 위해)
		String projectId = null;
		try {
			if (taskVo.getBoardId() != null) {
				projectId = this.getProjectIdByBoardId(taskVo.getBoardId());
			}
		} catch (Exception e) {
			System.err.println("프로젝트 ID 조회 실패: " + e.getMessage());
		}
		
		// 2. 데이터베이스 업데이트 실행
		int result = taskDAO.updateTask(taskVo);
		
		// 3. DB 업데이트 성공 시 Redis 캐시 무효화
		if (result > 0 && projectId != null && kanbanRedisService.isRedisConnected()) {
			try {
				kanbanRedisService.invalidateProjectCache(projectId);
				System.out.println("🗑️ 태스크 정보 갱신으로 인한 프로젝트 캐시 무효화: " + projectId);
			} catch (Exception e) {
				System.err.println("❌ 캐시 무효화 실패: " + e.getMessage());
			}
		}
		
		return result;	   		
	}
	
    /**
     * 업무(Task)의 보드 위치만 갱신 처리 한다. (칸반 카드 이동용)
     *
     * @process
     * 1. 업무(Task)의 보드 위치만 갱신 처리 한다.
     * 
     * @param  taskVo 업무(Task) 정보 TaskVo (taskId, boardId만 필요)
     * @return 번호
     * @throws Exception
     */
	public int updateTaskBoard(TaskVo taskVo) throws Exception {
		// 1. 먼저 프로젝트 ID를 조회 (캐시 업데이트를 위해)
		String projectId = null;
		try {
			if (taskVo.getBoardId() != null) {
				projectId = this.getProjectIdByBoardId(taskVo.getBoardId());
			}
		} catch (Exception e) {
			System.err.println("프로젝트 ID 조회 실패: " + e.getMessage());
		}
		
		// 2. 데이터베이스 업데이트 실행
		int result = taskDAO.updateTaskBoard(taskVo);
		
		// 3. DB 업데이트 성공 시 Redis 캐시 업데이트
		if (result > 0 && projectId != null && kanbanRedisService.isRedisConnected()) {
			try {
				// Redis 캐시에서 해당 태스크의 보드 위치 업데이트
				kanbanRedisService.updateTaskInCache(projectId, taskVo.getTaskId(), taskVo.getBoardId());
				System.out.println("🔄 Redis 캐시 태스크 보드 위치 업데이트 완료: " + taskVo.getTaskId() + " → " + taskVo.getBoardId());
			} catch (Exception e) {
				System.err.println("❌ Redis 캐시 업데이트 실패: " + e.getMessage());
				// 캐시 업데이트 실패 시 해당 프로젝트 캐시 무효화
				kanbanRedisService.invalidateProjectCache(projectId);
			}
		}
		
		return result;	   		
	}

    /**
     * 업무(Task) 정보를 삭제 처리 한다.
     *
     * @process
     * 1. 업무(Task) 정보를 삭제 처리 한다.
     * 
     * @param  taskVo 업무(Task) 정보 TaskVo
     * @return 번호
     * @throws Exception
     */
	public int deleteTask(TaskVo taskVo) throws Exception {
		// 1. 프로젝트 ID 조회 (캐시 무효화를 위해)
		String projectId = null;
		try {
			if (taskVo.getBoardId() != null) {
				projectId = this.getProjectIdByBoardId(taskVo.getBoardId());
			}
		} catch (Exception e) {
			System.err.println("프로젝트 ID 조회 실패: " + e.getMessage());
		}
		
		// 2. 데이터베이스 삭제 실행
		int result = taskDAO.deleteTask(taskVo);
		
		// 3. DB 삭제 성공 시 Redis 캐시 무효화
		if (result > 0 && projectId != null && kanbanRedisService.isRedisConnected()) {
			try {
				kanbanRedisService.invalidateProjectCache(projectId);
				System.out.println("🗑️ 태스크 삭제로 인한 프로젝트 캐시 무효화: " + projectId);
			} catch (Exception e) {
				System.err.println("❌ 캐시 무효화 실패: " + e.getMessage());
			}
		}
		
		return result;
	}
	
	/**
	 * TaskVo를 Map으로 변환 (Redis 캐시 저장용)
	 */
	private java.util.Map<String, Object> convertTaskVoToMap(TaskVo taskVo) {
		java.util.Map<String, Object> taskMap = new java.util.HashMap<>();
		
		taskMap.put("taskId", taskVo.getTaskId());
		taskMap.put("taskTitle", taskVo.getTaskTitle());
		taskMap.put("boardId", taskVo.getBoardId());
		taskMap.put("projectUserId", taskVo.getProjectUserId());
		taskMap.put("projectRepoId", taskVo.getProjectRepoId());
		taskMap.put("priority", taskVo.getPriority());
		taskMap.put("startDate", taskVo.getStartDate());
		taskMap.put("endDate", taskVo.getEndDate());
		taskMap.put("tags", taskVo.getTags());
		
		return taskMap;
	}
	
	/**
	 * Map을 TaskVo로 변환 (Redis 캐시 조회용)
	 */
	private TaskVo convertMapToTaskVo(java.util.Map<String, Object> taskMap) {
		TaskVo taskVo = new TaskVo();
		
		taskVo.setTaskId((String) taskMap.get("taskId"));
		taskVo.setTaskTitle((String) taskMap.get("taskTitle"));
		taskVo.setBoardId((String) taskMap.get("boardId"));
		taskVo.setProjectUserId((String) taskMap.get("projectUserId"));
		taskVo.setProjectRepoId((String) taskMap.get("projectRepoId"));
		taskVo.setPriority((String) taskMap.get("priority"));
		taskVo.setStartDate((String) taskMap.get("startDate"));
		taskVo.setEndDate((String) taskMap.get("endDate"));
		taskVo.setTags((String) taskMap.get("tags"));
		
		return taskVo;
	}
	
	/**
	 * 프로젝트 ID로 모든 태스크를 조회한다. (칸반 보드용)
	 */
	public List<TaskVo> selectTasksByProject(String projectId) throws Exception {
		System.out.println("TaskServiceImpl.selectTasksByProject - projectId: " + projectId);
		
		// 프로젝트에 속한 모든 보드의 태스크 조회를 위한 쿼리가 필요하지만
		// 현재 구조상 모든 태스크를 조회 후 프로젝트 ID로 필터링
		// 더 나은 방법: 프로젝트 ID로 직접 조회하는 쿼리 추가
		
		TaskVo searchVo = new TaskVo();
		List<TaskVo> allTasks = taskDAO.selectListTask(searchVo);
		
		System.out.println("TaskServiceImpl - 전체 태스크 개수: " + (allTasks != null ? allTasks.size() : 0));
		
		// 프로젝트 ID로 필터링 (boardId를 통해 projectId 확인)
		List<TaskVo> projectTasks = new java.util.ArrayList<>();
		for (TaskVo task : allTasks) {
			try {
				String taskProjectId = this.getProjectIdByBoardId(task.getBoardId());
				System.out.println("TaskServiceImpl - 태스크 필터링: taskId=" + task.getTaskId() + ", boardId=" + task.getBoardId() + ", taskProjectId=" + taskProjectId + ", 요청 projectId=" + projectId);
				if (projectId.equals(taskProjectId)) {
					projectTasks.add(task);
					System.out.println("TaskServiceImpl - 프로젝트 태스크 추가: " + task.getTaskTitle());
				}
			} catch (Exception e) {
				System.err.println("태스크 필터링 중 오류: " + e.getMessage());
			}
		}
		
		System.out.println("TaskServiceImpl - 프로젝트별 태스크 개수: " + projectTasks.size());
		return projectTasks;
	}
	
	
	
}