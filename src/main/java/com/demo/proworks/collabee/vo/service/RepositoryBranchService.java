package com.demo.proworks.collabee.vo.service;

import java.util.List;

import com.demo.proworks.collabee.vo.vo.RepositoryBranchVo;

/**  
 * @subject     : 연결된레포지토리브랜치 관련 처리를 담당하는 인터페이스
 * @description : 연결된레포지토리브랜치 관련 처리를 담당하는 인터페이스
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
     * 연결된레포지토리브랜치 페이징 처리하여 목록을 조회한다.
     *
     * @param  repositoryBranchVo 연결된레포지토리브랜치 RepositoryBranchVo
     * @return 연결된레포지토리브랜치 목록 List<RepositoryBranchVo>
     * @throws Exception
     */
	public List<RepositoryBranchVo> selectListRepositoryBranch(RepositoryBranchVo repositoryBranchVo) throws Exception;
	
    /**
     * 조회한 연결된레포지토리브랜치 전체 카운트
     * 
     * @param  repositoryBranchVo 연결된레포지토리브랜치 RepositoryBranchVo
     * @return 연결된레포지토리브랜치 목록 전체 카운트
     * @throws Exception
     */
	public long selectListCountRepositoryBranch(RepositoryBranchVo repositoryBranchVo) throws Exception;
	
    /**
     * 연결된레포지토리브랜치를 상세 조회한다.
     *
     * @param  repositoryBranchVo 연결된레포지토리브랜치 RepositoryBranchVo
     * @return 단건 조회 결과
     * @throws Exception
     */
	public RepositoryBranchVo selectRepositoryBranch(RepositoryBranchVo repositoryBranchVo) throws Exception;
		
    /**
     * 연결된레포지토리브랜치를 등록 처리 한다.
     *
     * @param  repositoryBranchVo 연결된레포지토리브랜치 RepositoryBranchVo
     * @return 번호
     * @throws Exception
     */
	public int insertRepositoryBranch(RepositoryBranchVo repositoryBranchVo) throws Exception;
	
    /**
     * 연결된레포지토리브랜치를 갱신 처리 한다.
     *
     * @param  repositoryBranchVo 연결된레포지토리브랜치 RepositoryBranchVo
     * @return 번호
     * @throws Exception
     */
	public int updateRepositoryBranch(RepositoryBranchVo repositoryBranchVo) throws Exception;
	
    /**
     * 연결된레포지토리브랜치를 삭제 처리 한다.
     *
     * @param  repositoryBranchVo 연결된레포지토리브랜치 RepositoryBranchVo
     * @return 번호
     * @throws Exception
     */
	public int deleteRepositoryBranch(RepositoryBranchVo repositoryBranchVo) throws Exception;
	
}
