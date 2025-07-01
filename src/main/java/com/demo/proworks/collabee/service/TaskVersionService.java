package com.demo.proworks.collabee.service;

import java.util.List;

import com.demo.proworks.collabee.vo.TaskVersionVo;

/**  
 * @subject     : 업무 버전 관리 관련 처리를 담당하는 인터페이스
 * @description : 업무 버전 관리 관련 처리를 담당하는 인터페이스
 * @author      : 남기윤
 * @since       : 2025/07/01
 * @modification
 * ===========================================================
 * DATE				AUTHOR				DESC
 * ===========================================================
 * 2025/07/01			 남기윤	 		최초 생성
 * 
 */
public interface TaskVersionService {
	
    /**
     * 업무 버전 관리 페이징 처리하여 목록을 조회한다.
     *
     * @param  taskVersionVo 업무 버전 관리 TaskVersionVo
     * @return 업무 버전 관리 목록 List<TaskVersionVo>
     * @throws Exception
     */
	public List<TaskVersionVo> selectListTaskVersion(TaskVersionVo taskVersionVo) throws Exception;
	
    /**
     * 조회한 업무 버전 관리 전체 카운트
     * 
     * @param  taskVersionVo 업무 버전 관리 TaskVersionVo
     * @return 업무 버전 관리 목록 전체 카운트
     * @throws Exception
     */
	public long selectListCountTaskVersion(TaskVersionVo taskVersionVo) throws Exception;
	
    /**
     * 업무 버전 관리를 상세 조회한다.
     *
     * @param  taskVersionVo 업무 버전 관리 TaskVersionVo
     * @return 단건 조회 결과
     * @throws Exception
     */
	public TaskVersionVo selectTaskVersion(TaskVersionVo taskVersionVo) throws Exception;
		
    /**
     * 업무 버전 관리를 등록 처리 한다.
     *
     * @param  taskVersionVo 업무 버전 관리 TaskVersionVo
     * @return 번호
     * @throws Exception
     */
	public int insertTaskVersion(TaskVersionVo taskVersionVo) throws Exception;
	
    /**
     * 업무 버전 관리를 갱신 처리 한다.
     *
     * @param  taskVersionVo 업무 버전 관리 TaskVersionVo
     * @return 번호
     * @throws Exception
     */
	public int updateTaskVersion(TaskVersionVo taskVersionVo) throws Exception;
	
    /**
     * 업무 버전 관리를 삭제 처리 한다.
     *
     * @param  taskVersionVo 업무 버전 관리 TaskVersionVo
     * @return 번호
     * @throws Exception
     */
	public int deleteTaskVersion(TaskVersionVo taskVersionVo) throws Exception;
	
}
