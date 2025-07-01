package com.demo.proworks.collabee.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.inswave.elfw.exception.ElException;
import com.demo.proworks.collabee.vo.TaskVersionVo;
import com.demo.proworks.collabee.dao.TaskVersionDAO;

/**  
 * @subject     : 업무 버전 관리 관련 처리를 담당하는 DAO
 * @description : 업무 버전 관리 관련 처리를 담당하는 DAO
 * @author      : 남기윤
 * @since       : 2025/07/01
 * @modification
 * ===========================================================
 * DATE				AUTHOR				DESC
 * ===========================================================
 * 2025/07/01			 남기윤	 		최초 생성
 * 
 */
@Repository("taskVersionDAO")
public class TaskVersionDAO extends com.demo.proworks.cmmn.dao.ProworksDefaultAbstractDAO {

    /**
     * 업무 버전 관리 상세 조회한다.
     *  
     * @param  TaskVersionVo 업무 버전 관리
     * @return TaskVersionVo 업무 버전 관리
     * @throws ElException
     */
    public TaskVersionVo selectTaskVersion(TaskVersionVo vo) throws ElException {
        return (TaskVersionVo) selectByPk("com.demo.proworks.collabee.selectTaskVersion", vo);
    }

    /**
     * 페이징을 처리하여 업무 버전 관리 목록조회를 한다.
     *  
     * @param  TaskVersionVo 업무 버전 관리
     * @return List<TaskVersionVo> 업무 버전 관리
     * @throws ElException
     */
    public List<TaskVersionVo> selectListTaskVersion(TaskVersionVo vo) throws ElException {      	
        return (List<TaskVersionVo>)list("com.demo.proworks.collabee.selectListTaskVersion", vo);
    }

    /**
     * 업무 버전 관리 목록 조회의 전체 카운트를 조회한다.
     *  
     * @param  TaskVersionVo 업무 버전 관리
     * @return 업무 버전 관리 조회의 전체 카운트
     * @throws ElException
     */
    public long selectListCountTaskVersion(TaskVersionVo vo)  throws ElException{               
        return (Long)selectByPk("com.demo.proworks.collabee.selectListCountTaskVersion", vo);
    }
        
    /**
     * 업무 버전 관리를 등록한다.
     *  
     * @param  TaskVersionVo 업무 버전 관리
     * @return 번호
     * @throws ElException
     */
    public int insertTaskVersion(TaskVersionVo vo) throws ElException {    	
        return insert("com.demo.proworks.collabee.insertTaskVersion", vo);
    }

    /**
     * 업무 버전 관리를 갱신한다.
     *  
     * @param  TaskVersionVo 업무 버전 관리
     * @return 번호
     * @throws ElException
     */
    public int updateTaskVersion(TaskVersionVo vo) throws ElException {
        return update("com.demo.proworks.collabee.updateTaskVersion", vo);
    }

    /**
     * 업무 버전 관리를 삭제한다.
     *  
     * @param  TaskVersionVo 업무 버전 관리
     * @return 번호
     * @throws ElException
     */
    public int deleteTaskVersion(TaskVersionVo vo) throws ElException {
        return delete("com.demo.proworks.collabee.deleteTaskVersion", vo);
    }

}
