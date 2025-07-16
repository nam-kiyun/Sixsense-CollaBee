package com.demo.proworks.taskversion.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.inswave.elfw.exception.ElException;
import com.demo.proworks.taskversion.vo.TaskVersionSearchVo;
import com.demo.proworks.taskversion.vo.TaskVersionVo;
import com.demo.proworks.taskversion.dao.TaskVersionDAO;

/**
 * @subject : 버전관리를 위한 Task(업무) 정보 관련 처리를 담당하는 DAO
 * @description : 버전관리를 위한 Task(업무) 정보 관련 처리를 담당하는 DAO
 * @author : 남기윤
 * @since : 2025/07/01
 * @modification ===========================================================
 *               DATE AUTHOR DESC
 *               ===========================================================
 *               2025/07/01 남기윤 최초 생성
 * 
 */
@Repository("taskVersionDAO")
public class TaskVersionDAO extends com.demo.proworks.cmmn.dao.ProworksDefaultAbstractDAO {

	/**
	 * 버전관리를 위한 Task(업무) 정보 상세 조회한다.
	 * 
	 * @param TaskVersionVo 버전관리를 위한 Task(업무) 정보
	 * @return TaskVersionVo 버전관리를 위한 Task(업무) 정보
	 * @throws ElException
	 */
	public TaskVersionVo selectTaskVersionByVersionId(TaskVersionVo vo) throws ElException {
		return (TaskVersionVo) selectByPk("com.demo.proworks.taskversion.selectTaskVersionByVersionId", vo);
	}

	/**
	 * 버전관리를 위한 Task(업무) 정보 상세 조회한다.
	 * 
	 * @param TaskVersionVo 버전관리를 위한 Task(업무) 정보
	 * @return TaskVersionVo 버전관리를 위한 Task(업무) 정보
	 * @throws ElException
	 */
	public TaskVersionVo selectTaskVersion(TaskVersionVo vo) throws ElException {
		return (TaskVersionVo) selectByPk("com.demo.proworks.taskversion.selectTaskVersion", vo);
	}

	/**
	 * 버전관리를 위한 Task(업무) 정보 상세 조회한다.
	 * 
	 * @param TaskVersionVo 버전관리를 위한 Task(업무) 정보
	 * @return TaskVersionVo 버전관리를 위한 Task(업무) 정보
	 * @throws ElException
	 */
//    public TaskVersionVo selectTaskVersion(TaskVersionVo vo) throws ElException {
//        return (TaskVersionVo) selectByPk("com.demo.proworks.taskversion.selectTaskVersion", vo);
//    }
	public List<TaskVersionVo> selectTaskVersionByTaskId(TaskVersionVo vo) throws ElException {
		return (List<TaskVersionVo>) list("com.demo.proworks.taskversion.selectTaskVersion", vo);
	}

	/**
	 * 페이징을 처리하여 버전관리를 위한 Task(업무) 정보 목록조회를 한다.
	 * 
	 * @param TaskVersionVo 버전관리를 위한 Task(업무) 정보
	 * @return List<TaskVersionVo> 버전관리를 위한 Task(업무) 정보
	 * @throws ElException
	 */
	public List<TaskVersionVo> selectListTaskVersion(TaskVersionVo vo) throws ElException {
		return (List<TaskVersionVo>) list("com.demo.proworks.taskversion.selectListTaskVersion", vo);
	}

	/**
	 * 버전관리를 위한 Task(업무) 정보 목록 조회의 전체 카운트를 조회한다.
	 * 
	 * @param TaskVersionVo 버전관리를 위한 Task(업무) 정보
	 * @return 버전관리를 위한 Task(업무) 정보 조회의 전체 카운트
	 * @throws ElException
	 */
	public long selectListCountTaskVersion(TaskVersionVo vo) throws ElException {
		return (Long) selectByPk("com.demo.proworks.taskversion.selectListCountTaskVersion", vo);
	}

	/**
	 * 버전관리를 위한 Task(업무) 정보를 등록한다.
	 * 
	 * @param TaskVersionVo 버전관리를 위한 Task(업무) 정보
	 * @return 번호
	 * @throws ElException
	 */
//    public int insertTaskVersion(TaskVersionVo vo) throws ElException {    	
//        return insert("com.demo.proworks.taskversion.insertTaskVersion", vo);
//    }
	public int insertTaskVersion(TaskVersionVo vo) throws ElException {
		insert("com.demo.proworks.taskversion.insertTaskVersion", vo);
		return vo.getTaskVersionId();
	}

	/**
	 * 버전관리를 위한 Task(업무) 정보를 갱신한다.
	 * 
	 * @param TaskVersionVo 버전관리를 위한 Task(업무) 정보
	 * @return 번호
	 * @throws ElException
	 */
	public int updateTaskVersion(TaskVersionVo vo) throws ElException {
		return update("com.demo.proworks.taskversion.updateTaskVersion", vo);
	}

	/**
	 * 버전관리를 위한 Task(업무) 정보를 삭제한다.
	 * 
	 * @param TaskVersionVo 버전관리를 위한 Task(업무) 정보
	 * @return 번호
	 * @throws ElException
	 */
	public int deleteTaskVersion(TaskVersionVo vo) throws ElException {
		return delete("com.demo.proworks.taskversion.deleteTaskVersion", vo);
	}

	// 새롭게 추가
	/**
	 * 페이징을 처리하여 버전관리를 위한 Task(업무) 정보 목록조회를 한다.
	 * 
	 * @param TaskVersionVo 버전관리를 위한 Task(업무) 정보
	 * @return List<TaskVersionVo> 버전관리를 위한 Task(업무) 정보
	 * @throws ElException
	 */
	public List<TaskVersionVo> selectListTaskVersionByTaskId(TaskVersionSearchVo vo) throws ElException {
		return (List<TaskVersionVo>) list("com.demo.proworks.taskversion.selectListTaskVersionByTaskId", vo);
	}

	/**
	 * 버전관리를 위한 Task(업무) 정보 목록 조회의 전체 카운트를 조회한다.
	 * 
	 * @param TaskVersionVo 버전관리를 위한 Task(업무) 정보
	 * @return 버전관리를 위한 Task(업무) 정보 조회의 전체 카운트
	 * @throws ElException
	 */
	public long selectListCountTaskVersionByTaskId(TaskVersionSearchVo vo) throws ElException {
		return (Long) selectByPk("com.demo.proworks.taskversion.selectListCountTaskVersionByTaskId", vo);
	}

	/**
	 * 프로젝트 ID로 작업 버전들을 삭제한다.
	 * 
	 * @param TaskVersionVo 버전관리를 위한 Task(업무) 정보
	 * @return 번호
	 * @throws ElException
	 */
	public int deleteTaskVersionByProjectId(TaskVersionVo vo) throws ElException {
		return delete("com.demo.proworks.taskversion.deleteTaskVersionByProjectId", vo);
	}

}
