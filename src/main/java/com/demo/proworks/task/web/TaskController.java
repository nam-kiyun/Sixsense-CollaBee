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

        System.out.println("🔍 태스크 목록 조회 요청 - boardId: " + taskVo.getBoardId() + ", tags: " + taskVo.getTags());
        
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
        // 정렬 파라미터 추출
        String sortField = taskVo.getSortField();
        String sortOrder = taskVo.getSortOrder();
        
        System.out.println("🔄 정렬 파라미터 확인 - sortField: " + sortField + ", sortOrder: " + sortOrder);
        
        // 기본값 설정
        if (sortField == null || sortField.trim().isEmpty()) {
            sortField = "startDate";
        }
        if (sortOrder == null || sortOrder.trim().isEmpty()) {
            sortOrder = "asc";
        }
        
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
            // 1. Redis 캐시에서 프로젝트의 전체 태스크 목록 조회 (정렬 적용)
            List<java.util.Map<String, Object>> cachedTasks = kanbanRedisService.getProjectTasksFromCacheWithSort(projectId, sortField, sortOrder);
            
            if (cachedTasks != null) {
                System.out.println("✅ Redis 캐시에서 정렬된 태스크 목록 조회 성공: " + cachedTasks.size() + "개 (정렬: " + sortField + " " + sortOrder + ")");
                
                // 캐시된 데이터를 TaskVo로 변환하고 필터링 (이미 정렬됨)
                List<TaskVo> taskList = convertAndFilterTasks(cachedTasks, taskVo);
                
                System.out.println("📊 Redis 캐시에서 필터링된 태스크 개수: " + taskList.size() + "개 (boardId: " + taskVo.getBoardId() + ")");
                
                TaskListVo retTaskList = new TaskListVo();
                retTaskList.setTaskVoList(taskList);
                retTaskList.setTotalCount(taskList.size());
                retTaskList.setPageSize(taskVo.getPageSize());
                retTaskList.setPageIndex(taskVo.getPageIndex());
                
                // 최종 응답 데이터 로깅
                System.out.println("📤 클라이언트로 전송할 최종 응답 데이터 (boardId: " + taskVo.getBoardId() + "):");
                for (TaskVo task : taskList) {
                    System.out.println("  - taskId: " + task.getTaskId() + ", boardId: " + task.getBoardId() + ", title: " + task.getTaskTitle());
                }
                
                return retTaskList;
            }
            
            // 3. 캐시 미스 - DB에서 프로젝트 전체 태스크 조회
            System.out.println("⚠️ Redis 캐시 미스 - 프로젝트 전체 태스크 DB 조회 시작");
            
            // 프로젝트 전체 태스크 조회 (새로운 메서드 사용)
            List<TaskVo> allProjectTasks = taskService.selectTasksByProject(projectId);
            
            System.out.println("📊 프로젝트 전체 태스크 조회 완료: " + (allProjectTasks != null ? allProjectTasks.size() : 0) + "개");
            
            // 각 태스크의 boardId 확인 (디버깅용)
            if (allProjectTasks != null) {
                for (TaskVo task : allProjectTasks) {
                    System.out.println("DEBUG - 조회된 태스크: taskId=" + task.getTaskId() + ", boardId=" + task.getBoardId() + ", taskTitle=" + task.getTaskTitle());
                }
            }
            
            // 4. 프로젝트 전체 태스크를 정렬 후 Redis에 캐싱
            if (allProjectTasks != null && !allProjectTasks.isEmpty()) {
                // 4-1. 정렬 적용
                allProjectTasks = sortTaskList(allProjectTasks, sortField, sortOrder);
                System.out.println("🔄 DB 조회 데이터 정렬 완료: " + sortField + " " + sortOrder);
                
                // 4-2. TaskVo 리스트를 Map 리스트로 변환
                List<java.util.Map<String, Object>> taskMapList = convertTaskVoListToMapList(allProjectTasks);
                kanbanRedisService.cacheProjectTasks(projectId, taskMapList);
                System.out.println("💾 정렬된 프로젝트 전체 태스크를 Redis에 캐싱 완료: " + allProjectTasks.size() + "개");
            }
            
            // 5. 현재 요청한 보드의 태스크만 필터링하여 반환
            List<TaskVo> taskList = new java.util.ArrayList<>();
            if (allProjectTasks != null) {
                for (TaskVo task : allProjectTasks) {
                    System.out.println("DEBUG - 태스크 필터링 확인: taskId=" + task.getTaskId() + ", boardId=" + task.getBoardId() + ", 요청 boardId=" + taskVo.getBoardId() + ", tags=" + task.getTags());
                    
                    // boardId 필터링
                    boolean boardMatch = true;
                    if (taskVo.getBoardId() != null && !taskVo.getBoardId().trim().isEmpty()) {
                        boardMatch = taskVo.getBoardId().equals(task.getBoardId());
                    }
                    
                    // 태그 필터링은 프론트엔드에서 처리
                    boolean tagMatch = true;
                    
                    // 모든 조건 만족 시 추가
                    if (boardMatch && tagMatch) {
                        taskList.add(task);
                        System.out.println("DEBUG - 필터링된 태스크: " + task.getTaskTitle());
                    }
                }
            }
            
            long totCnt = taskList.size();
            System.out.println("📊 필터링된 보드별 태스크 개수: " + totCnt + "개 (boardId: " + taskVo.getBoardId() + ")");
            
            TaskListVo retTaskList = new TaskListVo();
            retTaskList.setTaskVoList(taskList);
            retTaskList.setTotalCount(totCnt);
            retTaskList.setPageSize(taskVo.getPageSize());
            retTaskList.setPageIndex(taskVo.getPageIndex());
            
            return retTaskList;
            
        } catch (Exception e) {
            System.err.println("❌ 태스크 목록 조회 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
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
            boolean boardMatch = true;
            if (filterVo.getBoardId() != null && !filterVo.getBoardId().trim().isEmpty()) {
                boardMatch = filterVo.getBoardId().equals(task.getBoardId());
            }
            
            // 태그 필터링은 프론트엔드에서 처리
            boolean tagMatch = true;
            
            // 모든 조건이 만족하는 경우만 추가
            if (boardMatch && tagMatch) {
                taskList.add(task);
            }
        }
        
        System.out.println("🔍 캐시 필터링 결과: " + taskList.size() + "개 태스크");
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
     * TaskVo 리스트를 Map 리스트로 변환
     */
    private List<java.util.Map<String, Object>> convertTaskVoListToMapList(List<TaskVo> taskList) {
        List<java.util.Map<String, Object>> mapList = new java.util.ArrayList<>();
        
        for (TaskVo task : taskList) {
            java.util.Map<String, Object> map = new java.util.HashMap<>();
            
            // TaskVo 필드를 Map으로 변환 (실제 DB 컬럼명에 맞춤)
            map.put("taskId", task.getTaskId());
            map.put("taskTitle", task.getTaskTitle());
            map.put("boardId", task.getBoardId());
            map.put("projectUserId", task.getProjectUserId());
            map.put("projectRepoId", task.getProjectRepoId());
            map.put("priority", task.getPriority());
            map.put("startDate", task.getStartDate());
            map.put("endDate", task.getEndDate());
            map.put("tags", task.getTags());
            
            mapList.add(map);
        }
        
        return mapList;
    }
    
    /**
     * TaskVo 리스트를 정렬합니다.
     */
    private List<TaskVo> sortTaskList(List<TaskVo> taskList, String sortField, String sortOrder) {
        if (taskList == null || taskList.isEmpty()) {
            return taskList;
        }
        
        System.out.println("🔄 TaskController에서 정렬 시작 - 필드: " + sortField + ", 순서: " + sortOrder + ", 개수: " + taskList.size());
        
        taskList.sort((a, b) -> {
            String valueA = null;
            String valueB = null;
            
            // 정렬 필드에 따라 값 추출
            if ("startDate".equals(sortField)) {
                valueA = a.getStartDate();
                valueB = b.getStartDate();
            } else if ("endDate".equals(sortField)) {
                valueA = a.getEndDate();
                valueB = b.getEndDate();
            } else {
                // 기본값으로 startDate 사용
                valueA = a.getStartDate();
                valueB = b.getStartDate();
            }
            
            // null/empty 처리
            if ((valueA == null || valueA.trim().isEmpty()) && (valueB == null || valueB.trim().isEmpty())) {
                return 0;
            }
            if (valueA == null || valueA.trim().isEmpty()) {
                return 1; // null은 뒤로
            }
            if (valueB == null || valueB.trim().isEmpty()) {
                return -1; // null은 뒤로
            }
            
            try {
                // 날짜 문자열을 Date 객체로 변환하여 비교
                java.util.Date dateA = parseDate(valueA);
                java.util.Date dateB = parseDate(valueB);
                
                int comparison = dateA.compareTo(dateB);
                
                // 내림차순인 경우 결과를 뒤집음
                return "desc".equals(sortOrder) ? -comparison : comparison;
                
            } catch (Exception e) {
                System.err.println("날짜 비교 중 오류 발생: " + e.getMessage());
                // 문자열 비교로 폴백
                int comparison = valueA.compareTo(valueB);
                return "desc".equals(sortOrder) ? -comparison : comparison;
            }
        });
        
        System.out.println("✅ TaskController 정렬 완료: " + taskList.size() + "개");
        return taskList;
    }
    
    /**
     * 날짜 문자열을 Date 객체로 변환합니다.
     */
    private java.util.Date parseDate(String dateString) {
        if (dateString == null || dateString.trim().isEmpty()) {
            return new java.util.Date(0); // 기본값
        }
        
        try {
            // 다양한 날짜 형식 지원
            java.text.SimpleDateFormat[] formats = {
                new java.text.SimpleDateFormat("yyyy-MM-dd"),
                new java.text.SimpleDateFormat("yyyy/MM/dd"),
                new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss"),
                new java.text.SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
            };
            
            for (java.text.SimpleDateFormat format : formats) {
                try {
                    return format.parse(dateString.trim());
                } catch (java.text.ParseException e) {
                    // 다음 형식 시도
                }
            }
            
            // 모든 형식 실패 시 기본값
            System.err.println("날짜 파싱 실패: " + dateString);
            return new java.util.Date(0);
            
        } catch (Exception e) {
            System.err.println("날짜 파싱 중 예외 발생: " + e.getMessage());
            return new java.util.Date(0);
        }
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