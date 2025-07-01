package com.demo.proworks.collabee.service;

import java.util.List;

import com.demo.proworks.collabee.vo.ProjectRepositoryVo;

/**  
 * @subject     : 프로젝트에 연결된 레포지토리 정보 관련 처리를 담당하는 인터페이스
 * @description : 프로젝트에 연결된 레포지토리 정보 관련 처리를 담당하는 인터페이스
 * @author      : 남기윤
 * @since       : 2025/07/01
 * @modification
 * ===========================================================
 * DATE				AUTHOR				DESC
 * ===========================================================
 * 2025/07/01			 남기윤	 		최초 생성
 * 
 */
public interface ProjectRepositoryService {
	
    /**
     * 프로젝트에 연결된 레포지토리 정보 페이징 처리하여 목록을 조회한다.
     *
     * @param  projectRepositoryVo 프로젝트에 연결된 레포지토리 정보 ProjectRepositoryVo
     * @return 프로젝트에 연결된 레포지토리 정보 목록 List<ProjectRepositoryVo>
     * @throws Exception
     */
	public List<ProjectRepositoryVo> selectListProjectRepository(ProjectRepositoryVo projectRepositoryVo) throws Exception;
	
    /**
     * 조회한 프로젝트에 연결된 레포지토리 정보 전체 카운트
     * 
     * @param  projectRepositoryVo 프로젝트에 연결된 레포지토리 정보 ProjectRepositoryVo
     * @return 프로젝트에 연결된 레포지토리 정보 목록 전체 카운트
     * @throws Exception
     */
	public long selectListCountProjectRepository(ProjectRepositoryVo projectRepositoryVo) throws Exception;
	
    /**
     * 프로젝트에 연결된 레포지토리 정보를 상세 조회한다.
     *
     * @param  projectRepositoryVo 프로젝트에 연결된 레포지토리 정보 ProjectRepositoryVo
     * @return 단건 조회 결과
     * @throws Exception
     */
	public ProjectRepositoryVo selectProjectRepository(ProjectRepositoryVo projectRepositoryVo) throws Exception;
		
    /**
     * 프로젝트에 연결된 레포지토리 정보를 등록 처리 한다.
     *
     * @param  projectRepositoryVo 프로젝트에 연결된 레포지토리 정보 ProjectRepositoryVo
     * @return 번호
     * @throws Exception
     */
	public int insertProjectRepository(ProjectRepositoryVo projectRepositoryVo) throws Exception;
	
    /**
     * 프로젝트에 연결된 레포지토리 정보를 갱신 처리 한다.
     *
     * @param  projectRepositoryVo 프로젝트에 연결된 레포지토리 정보 ProjectRepositoryVo
     * @return 번호
     * @throws Exception
     */
	public int updateProjectRepository(ProjectRepositoryVo projectRepositoryVo) throws Exception;
	
    /**
     * 프로젝트에 연결된 레포지토리 정보를 삭제 처리 한다.
     *
     * @param  projectRepositoryVo 프로젝트에 연결된 레포지토리 정보 ProjectRepositoryVo
     * @return 번호
     * @throws Exception
     */
	public int deleteProjectRepository(ProjectRepositoryVo projectRepositoryVo) throws Exception;
	
}
