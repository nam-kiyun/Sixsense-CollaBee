package com.demo.proworks.collabee.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.inswave.elfw.exception.ElException;
import com.demo.proworks.collabee.vo.RepositoryBranchVo;
import com.demo.proworks.collabee.dao.RepositoryBranchDAO;

/**  
 * @subject     : 레포지토리의 브랜치 정보 관련 처리를 담당하는 DAO
 * @description : 레포지토리의 브랜치 정보 관련 처리를 담당하는 DAO
 * @author      : 남기윤
 * @since       : 2025/07/01
 * @modification
 * ===========================================================
 * DATE				AUTHOR				DESC
 * ===========================================================
 * 2025/07/01			 남기윤	 		최초 생성
 * 
 */
@Repository("repositoryBranchDAO")
public class RepositoryBranchDAO extends com.demo.proworks.cmmn.dao.ProworksDefaultAbstractDAO {

    /**
     * 레포지토리의 브랜치 정보 상세 조회한다.
     *  
     * @param  RepositoryBranchVo 레포지토리의 브랜치 정보
     * @return RepositoryBranchVo 레포지토리의 브랜치 정보
     * @throws ElException
     */
    public RepositoryBranchVo selectRepositoryBranch(RepositoryBranchVo vo) throws ElException {
        return (RepositoryBranchVo) selectByPk("com.demo.proworks.collabee.selectRepositoryBranch", vo);
    }

    /**
     * 페이징을 처리하여 레포지토리의 브랜치 정보 목록조회를 한다.
     *  
     * @param  RepositoryBranchVo 레포지토리의 브랜치 정보
     * @return List<RepositoryBranchVo> 레포지토리의 브랜치 정보
     * @throws ElException
     */
    public List<RepositoryBranchVo> selectListRepositoryBranch(RepositoryBranchVo vo) throws ElException {      	
        return (List<RepositoryBranchVo>)list("com.demo.proworks.collabee.selectListRepositoryBranch", vo);
    }

    /**
     * 레포지토리의 브랜치 정보 목록 조회의 전체 카운트를 조회한다.
     *  
     * @param  RepositoryBranchVo 레포지토리의 브랜치 정보
     * @return 레포지토리의 브랜치 정보 조회의 전체 카운트
     * @throws ElException
     */
    public long selectListCountRepositoryBranch(RepositoryBranchVo vo)  throws ElException{               
        return (Long)selectByPk("com.demo.proworks.collabee.selectListCountRepositoryBranch", vo);
    }
        
    /**
     * 레포지토리의 브랜치 정보를 등록한다.
     *  
     * @param  RepositoryBranchVo 레포지토리의 브랜치 정보
     * @return 번호
     * @throws ElException
     */
    public int insertRepositoryBranch(RepositoryBranchVo vo) throws ElException {    	
        return insert("com.demo.proworks.collabee.insertRepositoryBranch", vo);
    }

    /**
     * 레포지토리의 브랜치 정보를 갱신한다.
     *  
     * @param  RepositoryBranchVo 레포지토리의 브랜치 정보
     * @return 번호
     * @throws ElException
     */
    public int updateRepositoryBranch(RepositoryBranchVo vo) throws ElException {
        return update("com.demo.proworks.collabee.updateRepositoryBranch", vo);
    }

    /**
     * 레포지토리의 브랜치 정보를 삭제한다.
     *  
     * @param  RepositoryBranchVo 레포지토리의 브랜치 정보
     * @return 번호
     * @throws ElException
     */
    public int deleteRepositoryBranch(RepositoryBranchVo vo) throws ElException {
        return delete("com.demo.proworks.collabee.deleteRepositoryBranch", vo);
    }

}
