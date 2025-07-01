package com.demo.proworks.collabee.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.demo.proworks.collabee.service.TaskService;
import com.demo.proworks.collabee.vo.TaskVo;
import com.demo.proworks.collabee.dao.TaskDAO;

/**  
 * @subject     : 업무 관련 처리를 담당하는 ServiceImpl
 * @description	: 업무 관련 처리를 담당하는 ServiceImpl
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
	
	@Resource(name = "messageSource")
	private MessageSource messageSource;

    /**
     * 업무 목록을 조회합니다.
     *
     * @process
     * 1. 업무 페이징 처리하여 목록을 조회한다.
     * 2. 결과 List<TaskVo>을(를) 리턴한다.
     * 
     * @param  taskVo 업무 TaskVo
     * @return 업무 목록 List<TaskVo>
     * @throws Exception
     */
	public List<TaskVo> selectListTask(TaskVo taskVo) throws Exception {
		List<TaskVo> list = taskDAO.selectListTask(taskVo);	
	
		return list;
	}

    /**
     * 조회한 업무 전체 카운트
     *
     * @process
     * 1. 업무 조회하여 전체 카운트를 리턴한다.
     * 
     * @param  taskVo 업무 TaskVo
     * @return 업무 목록 전체 카운트
     * @throws Exception
     */
	public long selectListCountTask(TaskVo taskVo) throws Exception {
		return taskDAO.selectListCountTask(taskVo);
	}

    /**
     * 업무를 상세 조회한다.
     *
     * @process
     * 1. 업무를 상세 조회한다.
     * 2. 결과 TaskVo을(를) 리턴한다.
     * 
     * @param  taskVo 업무 TaskVo
     * @return 단건 조회 결과
     * @throws Exception
     */
	public TaskVo selectTask(TaskVo taskVo) throws Exception {
		TaskVo resultVO = taskDAO.selectTask(taskVo);			
        
        return resultVO;
	}

    /**
     * 업무를 등록 처리 한다.
     *
     * @process
     * 1. 업무를 등록 처리 한다.
     * 
     * @param  taskVo 업무 TaskVo
     * @return 번호
     * @throws Exception
     */
	public int insertTask(TaskVo taskVo) throws Exception {
		return taskDAO.insertTask(taskVo);	
	}
	
    /**
     * 업무를 갱신 처리 한다.
     *
     * @process
     * 1. 업무를 갱신 처리 한다.
     * 
     * @param  taskVo 업무 TaskVo
     * @return 번호
     * @throws Exception
     */
	public int updateTask(TaskVo taskVo) throws Exception {				
		return taskDAO.updateTask(taskVo);	   		
	}

    /**
     * 업무를 삭제 처리 한다.
     *
     * @process
     * 1. 업무를 삭제 처리 한다.
     * 
     * @param  taskVo 업무 TaskVo
     * @return 번호
     * @throws Exception
     */
	public int deleteTask(TaskVo taskVo) throws Exception {
		return taskDAO.deleteTask(taskVo);
	}
	
}
