package com.demo.proworks.collabee.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.demo.proworks.collabee.service.RepositoryBranchService;
import com.demo.proworks.collabee.vo.RepositoryBranchVo;
import com.demo.proworks.collabee.dao.RepositoryBranchDAO;

/**  
 * @subject     : 레포지토리의 브랜치 정보 관련 처리를 담당하는 ServiceImpl
 * @description	: 레포지토리의 브랜치 정보 관련 처리를 담당하는 ServiceImpl
 * @author      : 남기윤
 * @since       : 2025/07/01
 * @modification
 * ===========================================================
 * DATE				AUTHOR				DESC
 * ===========================================================
 * 2025/07/01			 남기윤	 		최초 생성
 * 
 */
@Service("repositoryBranchServiceImpl")
public class RepositoryBranchServiceImpl implements RepositoryBranchService {

    @Resource(name="repositoryBranchDAO")
    private RepositoryBranchDAO repositoryBranchDAO;
	
	@Resource(name = "messageSource")
	private MessageSource messageSource;

    /**
     * 레포지토리의 브랜치 정보 목록을 조회합니다.
     *
     * @process
     * 1. 레포지토리의 브랜치 정보 페이징 처리하여 목록을 조회한다.
     * 2. 결과 List<RepositoryBranchVo>을(를) 리턴한다.
     * 
     * @param  repositoryBranchVo 레포지토리의 브랜치 정보 RepositoryBranchVo
     * @return 레포지토리의 브랜치 정보 목록 List<RepositoryBranchVo>
     * @throws Exception
     */
	public List<RepositoryBranchVo> selectListRepositoryBranch(RepositoryBranchVo repositoryBranchVo) throws Exception {
		List<RepositoryBranchVo> list = repositoryBranchDAO.selectListRepositoryBranch(repositoryBranchVo);	
	
		return list;
	}

    /**
     * 조회한 레포지토리의 브랜치 정보 전체 카운트
     *
     * @process
     * 1. 레포지토리의 브랜치 정보 조회하여 전체 카운트를 리턴한다.
     * 
     * @param  repositoryBranchVo 레포지토리의 브랜치 정보 RepositoryBranchVo
     * @return 레포지토리의 브랜치 정보 목록 전체 카운트
     * @throws Exception
     */
	public long selectListCountRepositoryBranch(RepositoryBranchVo repositoryBranchVo) throws Exception {
		return repositoryBranchDAO.selectListCountRepositoryBranch(repositoryBranchVo);
	}

    /**
     * 레포지토리의 브랜치 정보를 상세 조회한다.
     *
     * @process
     * 1. 레포지토리의 브랜치 정보를 상세 조회한다.
     * 2. 결과 RepositoryBranchVo을(를) 리턴한다.
     * 
     * @param  repositoryBranchVo 레포지토리의 브랜치 정보 RepositoryBranchVo
     * @return 단건 조회 결과
     * @throws Exception
     */
	public RepositoryBranchVo selectRepositoryBranch(RepositoryBranchVo repositoryBranchVo) throws Exception {
		RepositoryBranchVo resultVO = repositoryBranchDAO.selectRepositoryBranch(repositoryBranchVo);			
        
        return resultVO;
	}

    /**
     * 레포지토리의 브랜치 정보를 등록 처리 한다.
     *
     * @process
     * 1. 레포지토리의 브랜치 정보를 등록 처리 한다.
     * 
     * @param  repositoryBranchVo 레포지토리의 브랜치 정보 RepositoryBranchVo
     * @return 번호
     * @throws Exception
     */
	public int insertRepositoryBranch(RepositoryBranchVo repositoryBranchVo) throws Exception {
		return repositoryBranchDAO.insertRepositoryBranch(repositoryBranchVo);	
	}
	
    /**
     * 레포지토리의 브랜치 정보를 갱신 처리 한다.
     *
     * @process
     * 1. 레포지토리의 브랜치 정보를 갱신 처리 한다.
     * 
     * @param  repositoryBranchVo 레포지토리의 브랜치 정보 RepositoryBranchVo
     * @return 번호
     * @throws Exception
     */
	public int updateRepositoryBranch(RepositoryBranchVo repositoryBranchVo) throws Exception {				
		return repositoryBranchDAO.updateRepositoryBranch(repositoryBranchVo);	   		
	}

    /**
     * 레포지토리의 브랜치 정보를 삭제 처리 한다.
     *
     * @process
     * 1. 레포지토리의 브랜치 정보를 삭제 처리 한다.
     * 
     * @param  repositoryBranchVo 레포지토리의 브랜치 정보 RepositoryBranchVo
     * @return 번호
     * @throws Exception
     */
	public int deleteRepositoryBranch(RepositoryBranchVo repositoryBranchVo) throws Exception {
		return repositoryBranchDAO.deleteRepositoryBranch(repositoryBranchVo);
	}
	
}
