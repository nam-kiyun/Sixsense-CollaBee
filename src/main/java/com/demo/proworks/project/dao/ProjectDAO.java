package com.demo.proworks.project.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.inswave.elfw.exception.ElException;
import com.demo.proworks.project.vo.ProjectVo;
import com.demo.proworks.project.dao.ProjectDAO;
import com.demo.proworks.email.vo.EmailVo;

/**  
 * @subject     : 프로젝트 정보 관련 처리를 담당하는 DAO
 * @description : 프로젝트 정보 관련 처리를 담당하는 DAO
 * @author      : 국다인
 * @since       : 2025/07/01
 * @modification
 * ===========================================================
 * DATE				AUTHOR				DESC
 * ===========================================================
 * 2025/07/01			 국다인	 		최초 생성
 * 
 */
@Repository("projectDAO")
public class ProjectDAO extends com.demo.proworks.cmmn.dao.ProworksDefaultAbstractDAO {

    /**
     * 프로젝트 정보 상세 조회한다.
     *  
     * @param  ProjectVo 프로젝트 정보
     * @return ProjectVo 프로젝트 정보
     * @throws ElException
     */
    public ProjectVo selectProject(ProjectVo vo) throws ElException {
        return (ProjectVo) selectByPk("com.demo.proworks.project.selectProject", vo);
    }

    /**
     * 페이징을 처리하여 프로젝트 정보 목록조회를 한다.
     *  
     * @param  ProjectVo 프로젝트 정보
     * @return List<ProjectVo> 프로젝트 정보
     * @throws ElException
     */
    public List<ProjectVo> selectListProject(ProjectVo vo) throws ElException {      	
        return (List<ProjectVo>)list("com.demo.proworks.project.selectListProject", vo);
    }

    /**
     * 프로젝트 정보 목록 조회의 전체 카운트를 조회한다.
     *  
     * @param  ProjectVo 프로젝트 정보
     * @return 프로젝트 정보 조회의 전체 카운트
     * @throws ElException
     */
    public long selectListCountProject(ProjectVo vo)  throws ElException{               
        return (Long)selectByPk("com.demo.proworks.project.selectListCountProject", vo);
    }
        
    /**
     * 프로젝트 정보를 등록한다.
     *  
     * @param  ProjectVo 프로젝트 정보
     * @return 번호
     * @throws ElException
     */
    public int insertProject(ProjectVo vo) throws ElException {    	
        return insert("com.demo.proworks.project.insertProject", vo);
    }

    /**
     * 프로젝트 정보를 갱신한다.
     *  
     * @param  ProjectVo 프로젝트 정보
     * @return 번호
     * @throws ElException
     */
    public int updateProject(ProjectVo vo) throws ElException {
        return update("com.demo.proworks.project.updateProject", vo);
    }

    /**
     * 프로젝트 정보를 삭제한다.
     *  
     * @param  ProjectVo 프로젝트 정보
     * @return 번호
     * @throws ElException
     */
    public int deleteProject(ProjectVo vo) throws ElException {
        return delete("com.demo.proworks.project.deleteProject", vo);
    }

    /**
     * 프로젝트 초대 이메일을 위한 프로젝트 정보와 팀장 정보를 조회한다.
     *  
     * @param  projectId 프로젝트 ID
     * @return EmailVo 이메일 발송을 위한 프로젝트 정보
     * @throws ElException
     */
    public EmailVo selectProjectForEmail(String projectId) throws ElException {
        return (EmailVo) selectByPk("com.demo.proworks.project.selectProjectForEmail", projectId);
    }

    /**
     * 사용자가 참여한 프로젝트 목록을 조회한다.
     *  
     * @param  userId 사용자 ID
     * @return List<ProjectVo> 사용자가 참여한 프로젝트 목록
     * @throws ElException
     */
    public List<ProjectVo> selectUserParticipatedProjects(String userId) throws ElException {
        return (List<ProjectVo>) list("com.demo.proworks.project.selectUserParticipatedProjects", userId);
    }

}
