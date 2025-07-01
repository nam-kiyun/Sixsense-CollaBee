package com.demo.proworks.collabee.vo.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.inswave.elfw.exception.ElException;
import com.demo.proworks.collabee.vo.vo.RepositoryBranchVo;
import com.demo.proworks.collabee.vo.dao.RepositoryBranchDAO;

/**  
 * @subject     : 연결된레포지토리브랜치 관련 처리를 담당하는 DAO
 * @description : 연결된레포지토리브랜치 관련 처리를 담당하는 DAO
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
     * 연결된레포지토리브랜치 상세 조회한다.
     *  
     * @param  RepositoryBranchVo 연결된레포지토리브랜치
     * @return RepositoryBranchVo 연결된레포지토리브랜치
     * @throws ElException
     */
    public RepositoryBranchVo selectRepositoryBranch(RepositoryBranchVo vo) throws ElException {
        return (RepositoryBranchVo) selectByPk("com.demo.proworks.collabee.vo.selectRepositoryBranch", vo);
    }

    /**
     * 페이징을 처리하여 연결된레포지토리브랜치 목록조회를 한다.
     *  
     * @param  RepositoryBranchVo 연결된레포지토리브랜치
     * @return List<RepositoryBranchVo> 연결된레포지토리브랜치
     * @throws ElException
     */
    public List<RepositoryBranchVo> selectListRepositoryBranch(RepositoryBranchVo vo) throws ElException {      	
        return (List<RepositoryBranchVo>)list("com.demo.proworks.collabee.vo.selectListRepositoryBranch", vo);
    }

    /**
     * 연결된레포지토리브랜치 목록 조회의 전체 카운트를 조회한다.
     *  
     * @param  RepositoryBranchVo 연결된레포지토리브랜치
     * @return 연결된레포지토리브랜치 조회의 전체 카운트
     * @throws ElException
     */
    public long selectListCountRepositoryBranch(RepositoryBranchVo vo)  throws ElException{               
        return (Long)selectByPk("com.demo.proworks.collabee.vo.selectListCountRepositoryBranch", vo);
    }
        
    /**
     * 연결된레포지토리브랜치를 등록한다.
     *  
     * @param  RepositoryBranchVo 연결된레포지토리브랜치
     * @return 번호
     * @throws ElException
     */
    public int insertRepositoryBranch(RepositoryBranchVo vo) throws ElException {    	
        return insert("com.demo.proworks.collabee.vo.insertRepositoryBranch", vo);
    }

    /**
     * 연결된레포지토리브랜치를 갱신한다.
     *  
     * @param  RepositoryBranchVo 연결된레포지토리브랜치
     * @return 번호
     * @throws ElException
     */
    public int updateRepositoryBranch(RepositoryBranchVo vo) throws ElException {
        return update("com.demo.proworks.collabee.vo.updateRepositoryBranch", vo);
    }

    /**
     * 연결된레포지토리브랜치를 삭제한다.
     *  
     * @param  RepositoryBranchVo 연결된레포지토리브랜치
     * @return 번호
     * @throws ElException
     */
    public int deleteRepositoryBranch(RepositoryBranchVo vo) throws ElException {
        return delete("com.demo.proworks.collabee.vo.deleteRepositoryBranch", vo);
    }

}
