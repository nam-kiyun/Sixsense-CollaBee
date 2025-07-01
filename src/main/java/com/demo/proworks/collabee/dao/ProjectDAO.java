package com.demo.proworks.collabee.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.inswave.elfw.exception.ElException;
import com.demo.proworks.collabee.vo.ProjectVo;
import com.demo.proworks.collabee.dao.ProjectDAO;

/**  
 * @subject     : 프로젝트 관련 처리를 담당하는 DAO
 * @description : 프로젝트 관련 처리를 담당하는 DAO
 * @author      : 남기윤
 * @since       : 2025/07/01
 * @modification
 * ===========================================================
 * DATE				AUTHOR				DESC
 * ===========================================================
 * 2025/07/01			 남기윤	 		최초 생성
 * 
 */
@Repository("projectDAO")
public class ProjectDAO extends com.demo.proworks.cmmn.dao.ProworksDefaultAbstractDAO {

    /**
     * 프로젝트 상세 조회한다.
     *  
     * @param  ProjectVo 프로젝트
     * @return ProjectVo 프로젝트
     * @throws ElException
     */
    public ProjectVo selectProject(ProjectVo vo) throws ElException {
        return (ProjectVo) selectByPk("com.demo.proworks.collabee.selectProject", vo);
    }

    /**
     * 페이징을 처리하여 프로젝트 목록조회를 한다.
     *  
     * @param  ProjectVo 프로젝트
     * @return List<ProjectVo> 프로젝트
     * @throws ElException
     */
    public List<ProjectVo> selectListProject(ProjectVo vo) throws ElException {      	
        return (List<ProjectVo>)list("com.demo.proworks.collabee.selectListProject", vo);
    }

    /**
     * 프로젝트 목록 조회의 전체 카운트를 조회한다.
     *  
     * @param  ProjectVo 프로젝트
     * @return 프로젝트 조회의 전체 카운트
     * @throws ElException
     */
    public long selectListCountProject(ProjectVo vo)  throws ElException{               
        return (Long)selectByPk("com.demo.proworks.collabee.selectListCountProject", vo);
    }
        
    /**
     * 프로젝트를 등록한다.
     *  
     * @param  ProjectVo 프로젝트
     * @return 번호
     * @throws ElException
     */
    public int insertProject(ProjectVo vo) throws ElException {    	
        return insert("com.demo.proworks.collabee.insertProject", vo);
    }

    /**
     * 프로젝트를 갱신한다.
     *  
     * @param  ProjectVo 프로젝트
     * @return 번호
     * @throws ElException
     */
    public int updateProject(ProjectVo vo) throws ElException {
        return update("com.demo.proworks.collabee.updateProject", vo);
    }

    /**
     * 프로젝트를 삭제한다.
     *  
     * @param  ProjectVo 프로젝트
     * @return 번호
     * @throws ElException
     */
    public int deleteProject(ProjectVo vo) throws ElException {
        return delete("com.demo.proworks.collabee.deleteProject", vo);
    }

}
