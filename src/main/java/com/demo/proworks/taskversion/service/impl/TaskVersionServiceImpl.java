package com.demo.proworks.taskversion.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.demo.proworks.taskversion.service.TaskVersionService;
import com.demo.proworks.taskversion.vo.TaskVersionSearchVo;
import com.demo.proworks.taskversion.vo.TaskVersionVo;
import com.demo.proworks.filesrc.dao.FileSrcDAO;
import com.demo.proworks.filesrc.vo.FileSrcListVo;
import com.demo.proworks.filesrc.vo.FileSrcVo;
import com.demo.proworks.taskversion.dao.TaskVersionDAO;

/**
 * @subject : 버전관리를 위한 Task(업무) 정보 관련 처리를 담당하는 ServiceImpl
 * @description : 버전관리를 위한 Task(업무) 정보 관련 처리를 담당하는 ServiceImpl
 * @author : 남기윤
 * @since : 2025/07/01
 * @modification ===========================================================
 *               DATE AUTHOR DESC
 *               ===========================================================
 *               2025/07/01 남기윤 최초 생성
 * 
 */
@Service("taskVersionServiceImpl")
public class TaskVersionServiceImpl implements TaskVersionService {

	@Resource(name = "taskVersionDAO")
	private TaskVersionDAO taskVersionDAO;

	@Resource(name = "fileSrcDAO")
	private FileSrcDAO fileSrcDAO;

	@Resource(name = "messageSource")
	private MessageSource messageSource;

	/**
	 * 버전관리를 위한 Task(업무) 정보 목록을 조회합니다.
	 *
	 * @process 1. 버전관리를 위한 Task(업무) 정보 페이징 처리하여 목록을 조회한다. 2. 결과
	 *          List<TaskVersionVo>을(를) 리턴한다.
	 * 
	 * @param taskVersionVo 버전관리를 위한 Task(업무) 정보 TaskVersionVo
	 * @return 버전관리를 위한 Task(업무) 정보 목록 List<TaskVersionVo>
	 * @throws Exception
	 */
	public List<TaskVersionVo> selectListTaskVersion(TaskVersionVo taskVersionVo) throws Exception {
		List<TaskVersionVo> list = taskVersionDAO.selectListTaskVersion(taskVersionVo);

		return list;
	}

	/**
	 * 버전관리를 위한 Task(업무) 정보 목록을 조회합니다.
	 *
	 * @process 1. 버전관리를 위한 Task(업무) 정보 페이징 처리하여 목록을 조회한다. 2. 결과
	 *          List<TaskVersionVo>을(를) 리턴한다.
	 * 
	 * @param taskVersionVo 버전관리를 위한 Task(업무) 정보 TaskVersionVo
	 * @return 버전관리를 위한 Task(업무) 정보 목록 List<TaskVersionVo>
	 * @throws Exception
	 */
	public List<TaskVersionVo> selectListTaskVersionByTaskId(TaskVersionSearchVo taskVersionVo) throws Exception {
		List<TaskVersionVo> list = taskVersionDAO.selectListTaskVersionByTaskId(taskVersionVo);

		return list;
	}

	/**
	 * 조회한 버전관리를 위한 Task(업무) 정보 전체 카운트
	 *
	 * @process 1. 버전관리를 위한 Task(업무) 정보 조회하여 전체 카운트를 리턴한다.
	 * 
	 * @param taskVersionVo 버전관리를 위한 Task(업무) 정보 TaskVersionVo
	 * @return 버전관리를 위한 Task(업무) 정보 목록 전체 카운트
	 * @throws Exception
	 */
	public long selectListCountTaskVersionByTaskId(TaskVersionSearchVo taskVersionVo) throws Exception {
		return taskVersionDAO.selectListCountTaskVersionByTaskId(taskVersionVo);
	}

	/**
	 * 조회한 버전관리를 위한 Task(업무) 정보 전체 카운트
	 *
	 * @process 1. 버전관리를 위한 Task(업무) 정보 조회하여 전체 카운트를 리턴한다.
	 * 
	 * @param taskVersionVo 버전관리를 위한 Task(업무) 정보 TaskVersionVo
	 * @return 버전관리를 위한 Task(업무) 정보 목록 전체 카운트
	 * @throws Exception
	 */
	public long selectListCountTaskVersion(TaskVersionVo taskVersionVo) throws Exception {
		return taskVersionDAO.selectListCountTaskVersion(taskVersionVo);
	}

	/**
	 * 버전관리를 위한 Task(업무) 정보를 상세 조회한다.
	 *
	 * @process 1. 버전관리를 위한 Task(업무) 정보를 상세 조회한다. 2. 결과 TaskVersionVo을(를) 리턴한다.
	 * 
	 * @param taskVersionVo 버전관리를 위한 Task(업무) 정보 TaskVersionVo
	 * @return 단건 조회 결과
	 * @throws Exception
	 */
	public TaskVersionVo selectTaskVersion(TaskVersionVo taskVersionVo) throws Exception {
		TaskVersionVo resultVO = taskVersionDAO.selectTaskVersion(taskVersionVo);
		System.out.println(resultVO.toString());

		return resultVO;
	}

	/**
	 * 버전관리를 위한 Task(업무) 정보를 등록 처리 한다.
	 *
	 * @process 1. 버전관리를 위한 Task(업무) 정보를 등록 처리 한다.
	 * 
	 * @param taskVersionVo 버전관리를 위한 Task(업무) 정보 TaskVersionVo
	 * @return 번호
	 * @throws Exception
	 */
	public int insertTaskVersion(TaskVersionVo taskVersionVo) throws Exception {
		return taskVersionDAO.insertTaskVersion(taskVersionVo);
	}

	/**
	 * 버전관리를 위한 Task(업무) 정보를 갱신 처리 한다.
	 *
	 * @process 1. 버전관리를 위한 Task(업무) 정보를 갱신 처리 한다.
	 * 
	 * @param taskVersionVo 버전관리를 위한 Task(업무) 정보 TaskVersionVo
	 * @return 번호
	 * @throws Exception
	 */
	public int updateTaskVersion(TaskVersionVo taskVersionVo) throws Exception {
		return taskVersionDAO.updateTaskVersion(taskVersionVo);
	}

	/**
	 * 버전관리를 위한 Task(업무) 정보를 삭제 처리 한다.
	 *
	 * @process 1. 버전관리를 위한 Task(업무) 정보를 삭제 처리 한다.
	 * 
	 * @param taskVersionVo 버전관리를 위한 Task(업무) 정보 TaskVersionVo
	 * @return 번호
	 * @throws Exception
	 */
	public int deleteTaskVersion(TaskVersionVo taskVersionVo) throws Exception {
		return taskVersionDAO.deleteTaskVersion(taskVersionVo);
	}

	/**
	 * 버전관리를 위한 Task(업무) 정보를 상세 조회한다.
	 *
	 * @process 1. 버전관리를 위한 Task(업무) 정보를 상세 조회한다. 2. 결과 TaskVersionVo을(를) 리턴한다.
	 * 
	 * @param taskVersionVo 버전관리를 위한 Task(업무) 정보 TaskVersionVo
	 * @return 단건 조회 결과
	 * @throws Exception
	 */
	public TaskVersionVo selectTaskVersionByVersionId(TaskVersionVo taskVersionVo) throws Exception {
		TaskVersionVo resultVO = taskVersionDAO.selectTaskVersionByVersionId(taskVersionVo);
		FileSrcVo srcVo = new FileSrcVo();
		srcVo.setTaskVersionId(taskVersionVo.getTaskVersionId());
		List<FileSrcVo> listVo = fileSrcDAO.selectFileSrcByTaskVersionId(srcVo);
		System.out.println(listVo);
		resultVO.setFileSrcVo(listVo);
		
		return resultVO;
	}
}
