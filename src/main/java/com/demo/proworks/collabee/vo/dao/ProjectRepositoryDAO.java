package com.demo.proworks.collabee.vo.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.inswave.elfw.exception.ElException;
import com.demo.proworks.collabee.vo.vo.ProjectRepositoryVo;
import com.demo.proworks.collabee.vo.dao.ProjectRepositoryDAO;

/**  
 * @subject     : 프로젝트와연결된레포지토리 관련 처리를 담당하는 DAO
 * @description : 프로젝트와연결된레포지토리 관련 처리를 담당하는 DAO
 * @author      : 남기윤
 * @since       : 2025/07/01
 * @modification
 * ===========================================================
 * DATE				AUTHOR				DESC
 * ===========================================================
 * 2025/07/01			 남기윤	 		최초 생성
 * 
 */
@Repository("projectRepositoryDAO")
public class ProjectRepositoryDAO extends com.demo.proworks.cmmn.dao.ProworksDefaultAbstractDAO {

    /**
     * 프로젝트와연결된레포지토리 상세 조회한다.
     *  
     * @param  ProjectRepositoryVo 프로젝트와연결된레포지토리
     * @return ProjectRepositoryVo 프로젝트와연결된레포지토리
     * @throws ElException
     */
    public ProjectRepositoryVo selectProjectRepository(ProjectRepositoryVo vo) throws ElException {
        return (ProjectRepositoryVo) selectByPk("com.demo.proworks.collabee.vo.selectProjectRepository", vo);
    }

    /**
     * 페이징을 처리하여 프로젝트와연결된레포지토리 목록조회를 한다.
     *  
     * @param  ProjectRepositoryVo 프로젝트와연결된레포지토리
     * @return List<ProjectRepositoryVo> 프로젝트와연결된레포지토리
     * @throws ElException
     */
    public List<ProjectRepositoryVo> selectListProjectRepository(ProjectRepositoryVo vo) throws ElException {      	
        return (List<ProjectRepositoryVo>)list("com.demo.proworks.collabee.vo.selectListProjectRepository", vo);
    }

    /**
     * 프로젝트와연결된레포지토리 목록 조회의 전체 카운트를 조회한다.
     *  
     * @param  ProjectRepositoryVo 프로젝트와연결된레포지토리
     * @return 프로젝트와연결된레포지토리 조회의 전체 카운트
     * @throws ElException
     */
    public long selectListCountProjectRepository(ProjectRepositoryVo vo)  throws ElException{               
        return (Long)selectByPk("com.demo.proworks.collabee.vo.selectListCountProjectRepository", vo);
    }
        
    /**
     * 프로젝트와연결된레포지토리를 등록한다.
     *  
     * @param  ProjectRepositoryVo 프로젝트와연결된레포지토리
     * @return 번호
     * @throws ElException
     */
    public int insertProjectRepository(ProjectRepositoryVo vo) throws ElException {    	
        return insert("com.demo.proworks.collabee.vo.insertProjectRepository", vo);
    }

    /**
     * 프로젝트와연결된레포지토리를 갱신한다.
     *  
     * @param  ProjectRepositoryVo 프로젝트와연결된레포지토리
     * @return 번호
     * @throws ElException
     */
    public int updateProjectRepository(ProjectRepositoryVo vo) throws ElException {
        return update("com.demo.proworks.collabee.vo.updateProjectRepository", vo);
    }

    /**
     * 프로젝트와연결된레포지토리를 삭제한다.
     *  
     * @param  ProjectRepositoryVo 프로젝트와연결된레포지토리
     * @return 번호
     * @throws ElException
     */
    public int deleteProjectRepository(ProjectRepositoryVo vo) throws ElException {
        return delete("com.demo.proworks.collabee.vo.deleteProjectRepository", vo);
    }

}
