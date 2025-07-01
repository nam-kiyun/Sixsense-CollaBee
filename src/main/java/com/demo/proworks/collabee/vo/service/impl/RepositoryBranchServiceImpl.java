package com.demo.proworks.collabee.vo.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.demo.proworks.collabee.vo.service.RepositoryBranchService;
import com.demo.proworks.collabee.vo.vo.RepositoryBranchVo;
import com.demo.proworks.collabee.vo.dao.RepositoryBranchDAO;

/**  
 * @subject     : 연결된레포지토리브랜치 관련 처리를 담당하는 ServiceImpl
 * @description	: 연결된레포지토리브랜치 관련 처리를 담당하는 ServiceImpl
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
     * 연결된레포지토리브랜치 목록을 조회합니다.
     *
     * @process
     * 1. 연결된레포지토리브랜치 페이징 처리하여 목록을 조회한다.
     * 2. 결과 List<RepositoryBranchVo>을(를) 리턴한다.
     * 
     * @param  repositoryBranchVo 연결된레포지토리브랜치 RepositoryBranchVo
     * @return 연결된레포지토리브랜치 목록 List<RepositoryBranchVo>
     * @throws Exception
     */
	public List<RepositoryBranchVo> selectListRepositoryBranch(RepositoryBranchVo repositoryBranchVo) throws Exception {
		List<RepositoryBranchVo> list = repositoryBranchDAO.selectListRepositoryBranch(repositoryBranchVo);	
	
		return list;
	}

    /**
     * 조회한 연결된레포지토리브랜치 전체 카운트
     *
     * @process
     * 1. 연결된레포지토리브랜치 조회하여 전체 카운트를 리턴한다.
     * 
     * @param  repositoryBranchVo 연결된레포지토리브랜치 RepositoryBranchVo
     * @return 연결된레포지토리브랜치 목록 전체 카운트
     * @throws Exception
     */
	public long selectListCountRepositoryBranch(RepositoryBranchVo repositoryBranchVo) throws Exception {
		return repositoryBranchDAO.selectListCountRepositoryBranch(repositoryBranchVo);
	}

    /**
     * 연결된레포지토리브랜치를 상세 조회한다.
     *
     * @process
     * 1. 연결된레포지토리브랜치를 상세 조회한다.
     * 2. 결과 RepositoryBranchVo을(를) 리턴한다.
     * 
     * @param  repositoryBranchVo 연결된레포지토리브랜치 RepositoryBranchVo
     * @return 단건 조회 결과
     * @throws Exception
     */
	public RepositoryBranchVo selectRepositoryBranch(RepositoryBranchVo repositoryBranchVo) throws Exception {
		RepositoryBranchVo resultVO = repositoryBranchDAO.selectRepositoryBranch(repositoryBranchVo);			
        
        return resultVO;
	}

    /**
     * 연결된레포지토리브랜치를 등록 처리 한다.
     *
     * @process
     * 1. 연결된레포지토리브랜치를 등록 처리 한다.
     * 
     * @param  repositoryBranchVo 연결된레포지토리브랜치 RepositoryBranchVo
     * @return 번호
     * @throws Exception
     */
	public int insertRepositoryBranch(RepositoryBranchVo repositoryBranchVo) throws Exception {
		return repositoryBranchDAO.insertRepositoryBranch(repositoryBranchVo);	
	}
	
    /**
     * 연결된레포지토리브랜치를 갱신 처리 한다.
     *
     * @process
     * 1. 연결된레포지토리브랜치를 갱신 처리 한다.
     * 
     * @param  repositoryBranchVo 연결된레포지토리브랜치 RepositoryBranchVo
     * @return 번호
     * @throws Exception
     */
	public int updateRepositoryBranch(RepositoryBranchVo repositoryBranchVo) throws Exception {				
		return repositoryBranchDAO.updateRepositoryBranch(repositoryBranchVo);	   		
	}

    /**
     * 연결된레포지토리브랜치를 삭제 처리 한다.
     *
     * @process
     * 1. 연결된레포지토리브랜치를 삭제 처리 한다.
     * 
     * @param  repositoryBranchVo 연결된레포지토리브랜치 RepositoryBranchVo
     * @return 번호
     * @throws Exception
     */
	public int deleteRepositoryBranch(RepositoryBranchVo repositoryBranchVo) throws Exception {
		return repositoryBranchDAO.deleteRepositoryBranch(repositoryBranchVo);
	}
	
}
