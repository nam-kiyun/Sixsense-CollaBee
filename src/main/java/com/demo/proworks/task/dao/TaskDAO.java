package com.demo.proworks.task.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.inswave.elfw.exception.ElException;
import com.demo.proworks.task.vo.TaskVo;
import com.demo.proworks.task.dao.TaskDAO;

/**  
 * @subject     : 업무(Task) 정보 관련 처리를 담당하는 DAO
 * @description : 업무(Task) 정보 관련 처리를 담당하는 DAO
 * @author      : 남기윤
 * @since       : 2025/07/01
 * @modification
 * ===========================================================
 * DATE				AUTHOR				DESC
 * ===========================================================
 * 2025/07/01			 남기윤	 		최초 생성
 * 
 */
@Repository("taskDAO")
public class TaskDAO extends com.demo.proworks.cmmn.dao.ProworksDefaultAbstractDAO {

    /**
     * 업무(Task) 정보 상세 조회한다.
     *  
     * @param  TaskVo 업무(Task) 정보
     * @return TaskVo 업무(Task) 정보
     * @throws ElException
     */
    public TaskVo selectTask(TaskVo vo) throws ElException {
        return (TaskVo) selectByPk("com.demo.proworks.task.selectTask", vo);
    }

    /**
     * 페이징을 처리하여 업무(Task) 정보 목록조회를 한다.
     *  
     * @param  TaskVo 업무(Task) 정보
     * @return List<TaskVo> 업무(Task) 정보
     * @throws ElException
     */
    public List<TaskVo> selectListTask(TaskVo vo) throws ElException {      	
        return (List<TaskVo>)list("com.demo.proworks.task.selectListTask", vo);
    }

    /**
     * 업무(Task) 정보 목록 조회의 전체 카운트를 조회한다.
     *  
     * @param  TaskVo 업무(Task) 정보
     * @return 업무(Task) 정보 조회의 전체 카운트
     * @throws ElException
     */
    public long selectListCountTask(TaskVo vo)  throws ElException{               
        return (Long)selectByPk("com.demo.proworks.task.selectListCountTask", vo);
    }
        
    /**
     * 업무(Task) 정보를 등록한다.
     *  
     * @param  TaskVo 업무(Task) 정보
     * @return 번호
     * @throws ElException
     */
    public int insertTask(TaskVo vo) throws ElException {    	
        return insert("com.demo.proworks.task.insertTask", vo);
    }

    /**
     * 업무(Task) 정보를 갱신한다.
     *  
     * @param  TaskVo 업무(Task) 정보
     * @return 번호
     * @throws ElException
     */
    public int updateTask(TaskVo vo) throws ElException {
        return update("com.demo.proworks.task.updateTask", vo);
    }

    /**
     * 업무(Task) 정보를 삭제한다.
     *  
     * @param  TaskVo 업무(Task) 정보
     * @return 번호
     * @throws ElException
     */
    public int deleteTask(TaskVo vo) throws ElException {
        return delete("com.demo.proworks.task.deleteTask", vo);
    }

}