package com.demo.proworks.collabee.service;

import java.util.List;

import com.demo.proworks.collabee.vo.TaskVo;

/**  
 * @subject     : 업무 관련 처리를 담당하는 인터페이스
 * @description : 업무 관련 처리를 담당하는 인터페이스
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
     * 업무 페이징 처리하여 목록을 조회한다.
     *
     * @param  taskVo 업무 TaskVo
     * @return 업무 목록 List<TaskVo>
     * @throws Exception
     */
	public List<TaskVo> selectListTask(TaskVo taskVo) throws Exception;
	
    /**
     * 조회한 업무 전체 카운트
     * 
     * @param  taskVo 업무 TaskVo
     * @return 업무 목록 전체 카운트
     * @throws Exception
     */
	public long selectListCountTask(TaskVo taskVo) throws Exception;
	
    /**
     * 업무를 상세 조회한다.
     *
     * @param  taskVo 업무 TaskVo
     * @return 단건 조회 결과
     * @throws Exception
     */
	public TaskVo selectTask(TaskVo taskVo) throws Exception;
		
    /**
     * 업무를 등록 처리 한다.
     *
     * @param  taskVo 업무 TaskVo
     * @return 번호
     * @throws Exception
     */
	public int insertTask(TaskVo taskVo) throws Exception;
	
    /**
     * 업무를 갱신 처리 한다.
     *
     * @param  taskVo 업무 TaskVo
     * @return 번호
     * @throws Exception
     */
	public int updateTask(TaskVo taskVo) throws Exception;
	
    /**
     * 업무를 삭제 처리 한다.
     *
     * @param  taskVo 업무 TaskVo
     * @return 번호
     * @throws Exception
     */
	public int deleteTask(TaskVo taskVo) throws Exception;
	
}
