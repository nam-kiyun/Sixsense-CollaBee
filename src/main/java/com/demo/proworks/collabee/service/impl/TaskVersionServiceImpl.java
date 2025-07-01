package com.demo.proworks.collabee.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.demo.proworks.collabee.service.TaskVersionService;
import com.demo.proworks.collabee.vo.TaskVersionVo;
import com.demo.proworks.collabee.dao.TaskVersionDAO;

/**  
 * @subject     : 업무 버전 관리 관련 처리를 담당하는 ServiceImpl
 * @description	: 업무 버전 관리 관련 처리를 담당하는 ServiceImpl
 * @author      : 남기윤
 * @since       : 2025/07/01
 * @modification
 * ===========================================================
 * DATE				AUTHOR				DESC
 * ===========================================================
 * 2025/07/01			 남기윤	 		최초 생성
 * 
 */
@Service("taskVersionServiceImpl")
public class TaskVersionServiceImpl implements TaskVersionService {

    @Resource(name="taskVersionDAO")
    private TaskVersionDAO taskVersionDAO;
	
	@Resource(name = "messageSource")
	private MessageSource messageSource;

    /**
     * 업무 버전 관리 목록을 조회합니다.
     *
     * @process
     * 1. 업무 버전 관리 페이징 처리하여 목록을 조회한다.
     * 2. 결과 List<TaskVersionVo>을(를) 리턴한다.
     * 
     * @param  taskVersionVo 업무 버전 관리 TaskVersionVo
     * @return 업무 버전 관리 목록 List<TaskVersionVo>
     * @throws Exception
     */
	public List<TaskVersionVo> selectListTaskVersion(TaskVersionVo taskVersionVo) throws Exception {
		List<TaskVersionVo> list = taskVersionDAO.selectListTaskVersion(taskVersionVo);	
	
		return list;
	}

    /**
     * 조회한 업무 버전 관리 전체 카운트
     *
     * @process
     * 1. 업무 버전 관리 조회하여 전체 카운트를 리턴한다.
     * 
     * @param  taskVersionVo 업무 버전 관리 TaskVersionVo
     * @return 업무 버전 관리 목록 전체 카운트
     * @throws Exception
     */
	public long selectListCountTaskVersion(TaskVersionVo taskVersionVo) throws Exception {
		return taskVersionDAO.selectListCountTaskVersion(taskVersionVo);
	}

    /**
     * 업무 버전 관리를 상세 조회한다.
     *
     * @process
     * 1. 업무 버전 관리를 상세 조회한다.
     * 2. 결과 TaskVersionVo을(를) 리턴한다.
     * 
     * @param  taskVersionVo 업무 버전 관리 TaskVersionVo
     * @return 단건 조회 결과
     * @throws Exception
     */
	public TaskVersionVo selectTaskVersion(TaskVersionVo taskVersionVo) throws Exception {
		TaskVersionVo resultVO = taskVersionDAO.selectTaskVersion(taskVersionVo);			
        
        return resultVO;
	}

    /**
     * 업무 버전 관리를 등록 처리 한다.
     *
     * @process
     * 1. 업무 버전 관리를 등록 처리 한다.
     * 
     * @param  taskVersionVo 업무 버전 관리 TaskVersionVo
     * @return 번호
     * @throws Exception
     */
	public int insertTaskVersion(TaskVersionVo taskVersionVo) throws Exception {
		return taskVersionDAO.insertTaskVersion(taskVersionVo);	
	}
	
    /**
     * 업무 버전 관리를 갱신 처리 한다.
     *
     * @process
     * 1. 업무 버전 관리를 갱신 처리 한다.
     * 
     * @param  taskVersionVo 업무 버전 관리 TaskVersionVo
     * @return 번호
     * @throws Exception
     */
	public int updateTaskVersion(TaskVersionVo taskVersionVo) throws Exception {				
		return taskVersionDAO.updateTaskVersion(taskVersionVo);	   		
	}

    /**
     * 업무 버전 관리를 삭제 처리 한다.
     *
     * @process
     * 1. 업무 버전 관리를 삭제 처리 한다.
     * 
     * @param  taskVersionVo 업무 버전 관리 TaskVersionVo
     * @return 번호
     * @throws Exception
     */
	public int deleteTaskVersion(TaskVersionVo taskVersionVo) throws Exception {
		return taskVersionDAO.deleteTaskVersion(taskVersionVo);
	}
	
}
