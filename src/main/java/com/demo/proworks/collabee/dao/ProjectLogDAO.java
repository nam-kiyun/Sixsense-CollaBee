package com.demo.proworks.collabee.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.inswave.elfw.exception.ElException;
import com.demo.proworks.collabee.vo.ProjectLogVo;
import com.demo.proworks.collabee.dao.ProjectLogDAO;

/**  
 * @subject     : 프로젝트 로그 관련 처리를 담당하는 DAO
 * @description : 프로젝트 로그 관련 처리를 담당하는 DAO
 * @author      : 남기윤
 * @since       : 2025/07/01
 * @modification
 * ===========================================================
 * DATE				AUTHOR				DESC
 * ===========================================================
 * 2025/07/01			 남기윤	 		최초 생성
 * 
 */
@Repository("projectLogDAO")
public class ProjectLogDAO extends com.demo.proworks.cmmn.dao.ProworksDefaultAbstractDAO {

    /**
     * 프로젝트 로그 상세 조회한다.
     *  
     * @param  ProjectLogVo 프로젝트 로그
     * @return ProjectLogVo 프로젝트 로그
     * @throws ElException
     */
    public ProjectLogVo selectProjectLog(ProjectLogVo vo) throws ElException {
        return (ProjectLogVo) selectByPk("com.demo.proworks.collabee.selectProjectLog", vo);
    }

    /**
     * 페이징을 처리하여 프로젝트 로그 목록조회를 한다.
     *  
     * @param  ProjectLogVo 프로젝트 로그
     * @return List<ProjectLogVo> 프로젝트 로그
     * @throws ElException
     */
    public List<ProjectLogVo> selectListProjectLog(ProjectLogVo vo) throws ElException {      	
        return (List<ProjectLogVo>)list("com.demo.proworks.collabee.selectListProjectLog", vo);
    }

    /**
     * 프로젝트 로그 목록 조회의 전체 카운트를 조회한다.
     *  
     * @param  ProjectLogVo 프로젝트 로그
     * @return 프로젝트 로그 조회의 전체 카운트
     * @throws ElException
     */
    public long selectListCountProjectLog(ProjectLogVo vo)  throws ElException{               
        return (Long)selectByPk("com.demo.proworks.collabee.selectListCountProjectLog", vo);
    }
        
    /**
     * 프로젝트 로그를 등록한다.
     *  
     * @param  ProjectLogVo 프로젝트 로그
     * @return 번호
     * @throws ElException
     */
    public int insertProjectLog(ProjectLogVo vo) throws ElException {    	
        return insert("com.demo.proworks.collabee.insertProjectLog", vo);
    }

    /**
     * 프로젝트 로그를 갱신한다.
     *  
     * @param  ProjectLogVo 프로젝트 로그
     * @return 번호
     * @throws ElException
     */
    public int updateProjectLog(ProjectLogVo vo) throws ElException {
        return update("com.demo.proworks.collabee.updateProjectLog", vo);
    }

    /**
     * 프로젝트 로그를 삭제한다.
     *  
     * @param  ProjectLogVo 프로젝트 로그
     * @return 번호
     * @throws ElException
     */
    public int deleteProjectLog(ProjectLogVo vo) throws ElException {
        return delete("com.demo.proworks.collabee.deleteProjectLog", vo);
    }

}
