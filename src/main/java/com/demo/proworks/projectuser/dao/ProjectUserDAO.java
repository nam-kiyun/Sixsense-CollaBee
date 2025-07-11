package com.demo.proworks.projectuser.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.inswave.elfw.exception.ElException;
import com.demo.proworks.projectuser.vo.ProjectUserVo;
import com.demo.proworks.projectuser.dao.ProjectUserDAO;

/**  
 * @subject     : 프로젝트에 초대(참가)한 사람들 관련 처리를 담당하는 DAO
 * @description : 프로젝트에 초대(참가)한 사람들 관련 처리를 담당하는 DAO
 * @author      : 남기윤
 * @since       : 2025/07/01
 * @modification
 * ===========================================================
 * DATE				AUTHOR				DESC
 * ===========================================================
 * 2025/07/01			 남기윤	 		최초 생성
 * 
 */
@Repository("projectUserDAO")
public class ProjectUserDAO extends com.demo.proworks.cmmn.dao.ProworksDefaultAbstractDAO {

    /**
     * 프로젝트에 초대(참가)한 사람들 상세 조회한다.
     *  
     * @param  ProjectUserVo 프로젝트에 초대(참가)한 사람들
     * @return ProjectUserVo 프로젝트에 초대(참가)한 사람들
     * @throws ElException
     */
    public ProjectUserVo selectProjectUser(ProjectUserVo vo) throws ElException {
        return (ProjectUserVo) selectByPk("com.demo.proworks.projectuser.selectProjectUser", vo);
    }

    /**
     * 페이징을 처리하여 프로젝트에 초대(참가)한 사람들 목록조회를 한다.
     *  
     * @param  ProjectUserVo 프로젝트에 초대(참가)한 사람들
     * @return List<ProjectUserVo> 프로젝트에 초대(참가)한 사람들
     * @throws ElException
     */
    public List<ProjectUserVo> selectListProjectUser(ProjectUserVo vo) throws ElException {      	
        return (List<ProjectUserVo>)list("com.demo.proworks.projectuser.selectListProjectUser", vo);
    }

    /**
     * 프로젝트에 초대(참가)한 사람들 목록 조회의 전체 카운트를 조회한다.
     *  
     * @param  ProjectUserVo 프로젝트에 초대(참가)한 사람들
     * @return 프로젝트에 초대(참가)한 사람들 조회의 전체 카운트
     * @throws ElException
     */
    public long selectListCountProjectUser(ProjectUserVo vo)  throws ElException{               
        return (Long)selectByPk("com.demo.proworks.projectuser.selectListCountProjectUser", vo);
    }
        
    /**
     * 프로젝트에 초대(참가)한 사람들를 등록한다.
     *  
     * @param  ProjectUserVo 프로젝트에 초대(참가)한 사람들
     * @return 번호
     * @throws ElException
     */
    public int insertProjectUser(ProjectUserVo vo) throws ElException {    	
        return insert("com.demo.proworks.projectuser.insertProjectUser", vo);
    }

    /**
     * 프로젝트에 초대(참가)한 사람들를 갱신한다.
     *  
     * @param  ProjectUserVo 프로젝트에 초대(참가)한 사람들
     * @return 번호
     * @throws ElException
     */
    public int updateProjectUser(ProjectUserVo vo) throws ElException {
        return update("com.demo.proworks.projectuser.updateProjectUser", vo);
    }

    /**
     * 프로젝트에 초대(참가)한 사람들를 삭제한다.
     *  
     * @param  ProjectUserVo 프로젝트에 초대(참가)한 사람들
     * @return 번호
     * @throws ElException
     */
    public int deleteProjectUser(ProjectUserVo vo) throws ElException {
        return delete("com.demo.proworks.projectuser.deleteProjectUser", vo);
    }

    /**
     * 특정 프로젝트의 멤버 목록을 사용자 정보와 함께 조회한다.
     *  
     * @param  ProjectUserVo 프로젝트에 초대(참가)한 사람들
     * @return List<ProjectUserVo> 프로젝트 멤버 목록 (사용자 정보 포함)
     * @throws ElException
     */
    public List<ProjectUserVo> selectProjectUsersByProjectId(ProjectUserVo vo) throws ElException {
        return (List<ProjectUserVo>)list("com.demo.proworks.projectuser.selectProjectUsersByProjectId", vo);
    }

    /**
     * user_id(이메일)로 사용자 정보를 조회한다.
     *  
     * @param  userId 사용자 ID (이메일)
     * @return ProjectUserVo 사용자 정보
     * @throws ElException
     */
    public ProjectUserVo selectUserByUserId(String userId) throws ElException {
        return (ProjectUserVo) selectByPk("com.demo.proworks.projectuser.selectUserByUserId", userId);
    }

    /**
     * 프로젝트 사용자 중복 체크를 한다.
     *  
     * @param  ProjectUserVo 프로젝트 사용자 정보
     * @return ProjectUserVo 프로젝트 사용자 정보
     * @throws ElException
     */
    public ProjectUserVo selectProjectUserByIds(ProjectUserVo vo) throws ElException {
        return (ProjectUserVo) selectByPk("com.demo.proworks.projectuser.selectProjectUserByIds", vo);
    }

    /**
     * 검색을 통한 프로젝트 사용자를 추가한다.
     *  
     * @param  ProjectUserVo 프로젝트 사용자 정보
     * @return 번호
     * @throws ElException
     */
    public int insertProjectUserBySearch(ProjectUserVo vo) throws ElException {
        return insert("com.demo.proworks.projectuser.insertProjectUserBySearch", vo);
    }

}
