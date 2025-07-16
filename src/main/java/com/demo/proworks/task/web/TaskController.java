package com.demo.proworks.task.web;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.demo.proworks.task.service.TaskService;
import com.demo.proworks.task.vo.TaskVo;
import com.demo.proworks.task.vo.TaskListVo;
import com.demo.proworks.redis.service.KanbanRedisService;
import com.demo.proworks.board.service.BoardService;
import com.demo.proworks.board.vo.BoardVo;

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
    
    /** BoardService - 보드 정보 조회를 위한 서비스 */
    @Resource(name = "boardServiceImpl")
    private BoardService boardService;
    
    /** KanbanRedisService - Redis 캐싱을 위한 서비스 */
    @Autowired
    private KanbanRedisService kanbanRedisService;
	
    
    /**
     * 업무(Task) 정보 목록을 조회합니다.
     * 프로젝트별 태스크 조회 시 Redis 캐싱을 적용하여 성능을 최적화합니다.
     *
     * @param  taskVo 업무(Task) 정보
     * @return 목록조회 결과
     * @throws Exception
     */
    @ElService(key="TaskList")
    @RequestMapping(value="TaskList")    
    @ElDescription(sub="업무(Task) 정보 목록조회",desc="페이징을 처리하여 업무(Task) 정보 목록 조회를 한다.")               
    @SuppressWarnings("unchecked")
    public TaskListVo selectListTask(TaskVo taskVo) throws Exception {    	   	

        System.out.println("🔍 태스크 목록 조회 요청 - boardId: " + taskVo.getBoardId());
        
        // boardId가 있는 경우 Redis 캐싱 적용 (칸반보드용)
        // TaskVo에는 projectId가 없으므로 boardId를 통해 프로젝트를 추정
        if (taskVo.getBoardId() != null && !taskVo.getBoardId().trim().isEmpty()) {
            return selectTasksWithRedisCache(taskVo);
        }
        
        // 일반적인 목록 조회는 기존 방식 유지
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
     * Redis 캐싱을 적용한 태스크 목록 조회 (칸반보드용)
     */
    private TaskListVo selectTasksWithRedisCache(TaskVo taskVo) throws Exception {
        // boardId를 통해 projectId 찾기
        String projectId = null;
        if (taskVo.getBoardId() != null) {
            try {
                BoardVo boardVo = new BoardVo();
                boardVo.setBoardId(taskVo.getBoardId());
                BoardVo board = boardService.selectBoard(boardVo);
                if (board != null) {
                    projectId = board.getProjectId();
                }
            } catch (Exception e) {
                System.err.println("❌ boardId로 projectId 조회 실패: " + e.getMessage());
            }
        }
        
        if (projectId == null) {
            System.out.println("⚠️ projectId를 찾을 수 없어 일반 DB 조회로 진행");
            List<TaskVo> taskList = taskService.selectListTask(taskVo);
            long totCnt = taskService.selectListCountTask(taskVo);
            
            TaskListVo retTaskList = new TaskListVo();
            retTaskList.setTaskVoList(taskList);
            retTaskList.setTotalCount(totCnt);
            retTaskList.setPageSize(taskVo.getPageSize());
            retTaskList.setPageIndex(taskVo.getPageIndex());
            
            return retTaskList;
        }
        
        try {
            // 1. Redis 캐시에서 프로젝트의 전체 태스크 목록 조회
            List<java.util.Map<String, Object>> cachedTasks = kanbanRedisService.getProjectTasksFromCache(projectId);
            
            if (cachedTasks != null) {
                System.out.println("✅ Redis 캐시에서 태스크 목록 조회 성공: " + cachedTasks.size() + "개");
                
                // 캐시된 데이터를 TaskVo로 변환하고 필터링
                List<TaskVo> taskList = convertAndFilterTasks(cachedTasks, taskVo);
                
                TaskListVo retTaskList = new TaskListVo();
                retTaskList.setTaskVoList(taskList);
                retTaskList.setTotalCount(taskList.size());
                retTaskList.setPageSize(taskVo.getPageSize());
                retTaskList.setPageIndex(taskVo.getPageIndex());
                
                return retTaskList;
            }
            
            // 2. 캐시 미스 - DB에서 조회
            System.out.println("⚠️ Redis 캐시 미스 - DB에서 조회 시작");
            List<TaskVo> taskList = taskService.selectListTask(taskVo);
            long totCnt = taskService.selectListCountTask(taskVo);
            
            System.out.println("📊 DB에서 조회된 태스크 개수: " + (taskList != null ? taskList.size() : 0));
            
            // 3. 프로젝트 전체 태스크를 Redis에 캐싱 (boardId 필터링 없이)
            // TaskVo에는 projectId가 없으므로 현재 조회한 태스크를 캐싱
            if (taskList != null && !taskList.isEmpty()) {
                kanbanRedisService.cacheProjectTasks(projectId, taskList);
                System.out.println("💾 보드별 태스크를 Redis에 캐싱 완료: " + taskList.size() + "개");
            }
            
            TaskListVo retTaskList = new TaskListVo();
            retTaskList.setTaskVoList(taskList);
            retTaskList.setTotalCount(totCnt);
            retTaskList.setPageSize(taskVo.getPageSize());
            retTaskList.setPageIndex(taskVo.getPageIndex());
            
            return retTaskList;
            
        } catch (Exception e) {
            System.err.println("❌ 태스크 목록 조회 중 오류 발생: " + e.getMessage());
            // Redis 오류 시 DB에서 직접 조회
            System.out.println("🔄 Redis 오류로 인한 DB 직접 조회 시도");
            
            List<TaskVo> taskList = taskService.selectListTask(taskVo);
            long totCnt = taskService.selectListCountTask(taskVo);
            
            TaskListVo retTaskList = new TaskListVo();
            retTaskList.setTaskVoList(taskList);
            retTaskList.setTotalCount(totCnt);
            retTaskList.setPageSize(taskVo.getPageSize());
            retTaskList.setPageIndex(taskVo.getPageIndex());
            
            return retTaskList;
        }
    }
    
    /**
     * 캐시된 태스크 데이터를 TaskVo로 변환하고 조건에 따라 필터링
     */
    private List<TaskVo> convertAndFilterTasks(List<java.util.Map<String, Object>> cachedTasks, TaskVo filterVo) {
        List<TaskVo> taskList = new java.util.ArrayList<>();
        
        for (java.util.Map<String, Object> map : cachedTasks) {
            TaskVo task = convertMapToTaskVo(map);
            
            // boardId 필터링 적용
            if (filterVo.getBoardId() != null && !filterVo.getBoardId().trim().isEmpty()) {
                if (filterVo.getBoardId().equals(task.getBoardId())) {
                    taskList.add(task);
                }
            } else {
                taskList.add(task);
            }
        }
        
        return taskList;
    }
    
    /**
     * Map을 TaskVo로 변환
     */
    private TaskVo convertMapToTaskVo(java.util.Map<String, Object> map) {
        TaskVo task = new TaskVo();
        
        // Map에서 TaskVo 필드로 변환 (실제 VO 필드명에 맞게 수정)
        if (map.get("taskId") != null) task.setTaskId(map.get("taskId").toString());
        if (map.get("boardId") != null) task.setBoardId(map.get("boardId").toString());
        if (map.get("projectUserId") != null) task.setProjectUserId(map.get("projectUserId").toString());
        if (map.get("projectRepoId") != null) task.setProjectRepoId(map.get("projectRepoId").toString());
        if (map.get("taskTitle") != null) task.setTaskTitle(map.get("taskTitle").toString());
        if (map.get("priority") != null) task.setPriority(map.get("priority").toString());
        if (map.get("startDate") != null) task.setStartDate(map.get("startDate").toString());
        if (map.get("endDate") != null) task.setEndDate(map.get("endDate").toString());
        if (map.get("tags") != null) task.setTags(map.get("tags").toString());
        
        return task;
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
     * 등록 후 관련 프로젝트의 Redis 캐시를 무효화합니다.
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
    	    
    	    // Redis 캐시 무효화 - 프로젝트의 태스크 목록이 변경되었음
    	    invalidateProjectCacheByBoardId(taskVo.getBoardId(), "태스크 등록");
    	    
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
    	
    	// Redis 캐시 무효화 - 프로젝트의 태스크 정보가 변경되었음
    	invalidateProjectCacheByBoardId(taskVo.getBoardId(), "태스크 갱신");
    }

    /**
     * 업무(Task) 정보를 삭제 처리한다.
     * 삭제 후 관련 프로젝트의 Redis 캐시를 무효화합니다.
     *
     * @param  taskVo 업무(Task) 정보    
     * @throws Exception
     */
    @ElService(key = "TaskDel")    
    @RequestMapping(value="TaskDel")
    @ElDescription(sub = "업무(Task) 정보 삭제처리", desc = "업무(Task) 정보를 삭제 처리한다.")    
    public void deleteTask(TaskVo taskVo) throws Exception {
        taskService.deleteTask(taskVo);
        
        // Redis 캐시 무효화 - 프로젝트의 태스크 목록이 변경되었음
        invalidateProjectCacheByBoardId(taskVo.getBoardId(), "태스크 삭제");
    }
    
    /**
     * boardId를 통해 projectId를 찾아 Redis 캐시 무효화
     */
    private void invalidateProjectCacheByBoardId(String boardId, String action) {
        if (boardId == null) {
            System.out.println("⚠️ boardId가 null이어서 캐시 무효화를 건너뜁니다.");
            return;
        }
        
        try {
            BoardVo boardVo = new BoardVo();
            boardVo.setBoardId(boardId);
            BoardVo board = boardService.selectBoard(boardVo);
            
            if (board != null && board.getProjectId() != null) {
                kanbanRedisService.invalidateProjectCache(board.getProjectId());
                System.out.println("🗑️ " + action + "으로 인한 프로젝트 캐시 무효화: " + board.getProjectId());
            } else {
                System.out.println("⚠️ boardId로 프로젝트를 찾을 수 없어 캐시 무효화를 건너뜁니다: " + boardId);
            }
        } catch (Exception e) {
            System.err.println("❌ 캐시 무효화 중 오류 발생: " + e.getMessage());
        }
    }
   
}