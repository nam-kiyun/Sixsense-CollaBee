package com.demo.proworks.collabee.service;

import java.util.List;

import com.demo.proworks.collabee.vo.RepositoryBranchVo;

/**  
 * @subject     : 레포지토리의 브랜치 정보 관련 처리를 담당하는 인터페이스
 * @description : 레포지토리의 브랜치 정보 관련 처리를 담당하는 인터페이스
 * @author      : 남기윤
 * @since       : 2025/07/01
 * @modification
 * ===========================================================
 * DATE				AUTHOR				DESC
 * ===========================================================
 * 2025/07/01			 남기윤	 		최초 생성
 * 
 */
public interface RepositoryBranchService {
	
    /**
     * 레포지토리의 브랜치 정보 페이징 처리하여 목록을 조회한다.
     *
     * @param  repositoryBranchVo 레포지토리의 브랜치 정보 RepositoryBranchVo
     * @return 레포지토리의 브랜치 정보 목록 List<RepositoryBranchVo>
     * @throws Exception
     */
	public List<RepositoryBranchVo> selectListRepositoryBranch(RepositoryBranchVo repositoryBranchVo) throws Exception;
	
    /**
     * 조회한 레포지토리의 브랜치 정보 전체 카운트
     * 
     * @param  repositoryBranchVo 레포지토리의 브랜치 정보 RepositoryBranchVo
     * @return 레포지토리의 브랜치 정보 목록 전체 카운트
     * @throws Exception
     */
	public long selectListCountRepositoryBranch(RepositoryBranchVo repositoryBranchVo) throws Exception;
	
    /**
     * 레포지토리의 브랜치 정보를 상세 조회한다.
     *
     * @param  repositoryBranchVo 레포지토리의 브랜치 정보 RepositoryBranchVo
     * @return 단건 조회 결과
     * @throws Exception
     */
	public RepositoryBranchVo selectRepositoryBranch(RepositoryBranchVo repositoryBranchVo) throws Exception;
		
    /**
     * 레포지토리의 브랜치 정보를 등록 처리 한다.
     *
     * @param  repositoryBranchVo 레포지토리의 브랜치 정보 RepositoryBranchVo
     * @return 번호
     * @throws Exception
     */
	public int insertRepositoryBranch(RepositoryBranchVo repositoryBranchVo) throws Exception;
	
    /**
     * 레포지토리의 브랜치 정보를 갱신 처리 한다.
     *
     * @param  repositoryBranchVo 레포지토리의 브랜치 정보 RepositoryBranchVo
     * @return 번호
     * @throws Exception
     */
	public int updateRepositoryBranch(RepositoryBranchVo repositoryBranchVo) throws Exception;
	
    /**
     * 레포지토리의 브랜치 정보를 삭제 처리 한다.
     *
     * @param  repositoryBranchVo 레포지토리의 브랜치 정보 RepositoryBranchVo
     * @return 번호
     * @throws Exception
     */
	public int deleteRepositoryBranch(RepositoryBranchVo repositoryBranchVo) throws Exception;
	
}
