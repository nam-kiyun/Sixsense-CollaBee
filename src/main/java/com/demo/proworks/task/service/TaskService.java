package com.demo.proworks.task.service;

import java.util.List;

import com.demo.proworks.task.vo.TaskUpdateVo;
import com.demo.proworks.task.vo.TaskVo;

/**  
 * @subject     : 업무(Task) 정보 관련 처리를 담당하는 인터페이스
 * @description : 업무(Task) 정보 관련 처리를 담당하는 인터페이스
 * @author      : 남기윤
 * @since       : 2025/07/01
 * @modification
 * ===========================================================
 * DATE				AUTHOR				DESC
 * ===========================================================
 * 2025/07/01			 남기윤	 		최초 생성
 * 
 */
public interface TaskService {
	/**
	 * 업무(Task) 정보를 갱신 처리 한다.
	 *
	 * @process 1. 업무(Task) 정보를 갱신 처리 한다.
	 * 
	 * @param taskVo 업무(Task) 정보 TaskVo
	 * @return 번호
	 * @throws Exception
	 */
	public int saveTask(TaskUpdateVo updateVo) throws Exception;
	
    /**
     * 업무(Task) 정보 페이징 처리하여 목록을 조회한다.
     *
     * @param  taskVo 업무(Task) 정보 TaskVo
     * @return 업무(Task) 정보 목록 List<TaskVo>
     * @throws Exception
     */
	public List<TaskVo> selectListTask(TaskVo taskVo) throws Exception;
	
    /**
     * 조회한 업무(Task) 정보 전체 카운트
     * 
     * @param  taskVo 업무(Task) 정보 TaskVo
     * @return 업무(Task) 정보 목록 전체 카운트
     * @throws Exception
     */
	public long selectListCountTask(TaskVo taskVo) throws Exception;
	
    /**
     * 업무(Task) 정보를 상세 조회한다.
     *
     * @param  taskVo 업무(Task) 정보 TaskVo
     * @return 단건 조회 결과
     * @throws Exception
     */
	public TaskUpdateVo selectTask(TaskUpdateVo taskVo) throws Exception;
		
    /**
     * 업무(Task) 정보를 등록 처리 한다.
     *
     * @param  taskVo 업무(Task) 정보 TaskVo
     * @return 번호
     * @throws Exception
     */
	public int insertTask(TaskVo taskVo) throws Exception;
	
    /**
     * 업무(Task) 정보를 갱신 처리 한다.
     *
     * @param  taskVo 업무(Task) 정보 TaskVo
     * @return 번호
     * @throws Exception
     */
	public int updateTask(TaskVo taskVo) throws Exception;
	
    /**
     * 업무(Task)의 보드 위치만 갱신 처리 한다. (칸반 카드 이동용)
     *
     * @param  taskVo 업무(Task) 정보 TaskVo (taskId, boardId만 필요)
     * @return 번호
     * @throws Exception
     */
	public int updateTaskBoard(TaskVo taskVo) throws Exception;
	
    /**
     * 업무(Task) 정보를 삭제 처리 한다.
     *
     * @param  taskVo 업무(Task) 정보 TaskVo
     * @return 번호
     * @throws Exception
     */
	public int deleteTask(TaskVo taskVo) throws Exception;
	
    /**
     * 프로젝트 ID로 모든 태스크를 조회한다. (칸반 보드용)
     *
     * @param  projectId 프로젝트 ID
     * @return 태스크 목록 List<TaskVo>
     * @throws Exception
     */
	public List<TaskVo> selectTasksByProject(String projectId) throws Exception;
	
	/**
	 * 사용자 이름을 포함한 태스크 목록 조회
	 * 
	 * @param taskVo 검색 조건을 담은 TaskVo
	 * @return 사용자 이름이 포함된 TaskVo 리스트
	 * @throws Exception
	 */
	public List<TaskVo> selectTaskListWithUserName(TaskVo taskVo) throws Exception;
	
	/**
	 * 여러 보드의 사용자 이름 포함 태스크를 한 번의 쿼리로 배치 조회 (진짜 배치)
	 * 
	 * @param boardIds 보드 ID 리스트 (예: ["1", "2", "3", "4", "5", "6"])
	 * @return 사용자 이름이 포함된 TaskVo 리스트
	 * @throws Exception
	 */
	public List<TaskVo> selectTaskListWithUserNameBatch(List<String> boardIds) throws Exception;
	
}