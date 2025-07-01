package com.demo.proworks.collabee.web;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.demo.proworks.collabee.service.RepositoryBranchService;
import com.demo.proworks.collabee.vo.RepositoryBranchVo;
import com.demo.proworks.collabee.vo.RepositoryBranchListVo;

import com.inswave.elfw.annotation.ElDescription;
import com.inswave.elfw.annotation.ElService;
import com.inswave.elfw.annotation.ElValidator;

/**  
 * @subject     : 레포지토리의 브랜치 정보 관련 처리를 담당하는 컨트롤러
 * @description : 레포지토리의 브랜치 정보 관련 처리를 담당하는 컨트롤러
 * @author      : 남기윤
 * @since       : 2025/07/01
 * @modification
 * ===========================================================
 * DATE				AUTHOR				DESC
 * ===========================================================
 * 2025/07/01			 남기윤	 		최초 생성
 * 
 */
@Controller
public class RepositoryBranchController {
	
    /** RepositoryBranchService */
    @Resource(name = "repositoryBranchServiceImpl")
    private RepositoryBranchService repositoryBranchService;
	
    
    /**
     * 레포지토리의 브랜치 정보 목록을 조회합니다.
     *
     * @param  repositoryBranchVo 레포지토리의 브랜치 정보
     * @return 목록조회 결과
     * @throws Exception
     */
    @ElService(key="RepositoryBranchList")
    @RequestMapping(value="RepositoryBranchList")    
    @ElDescription(sub="레포지토리의 브랜치 정보 목록조회",desc="페이징을 처리하여 레포지토리의 브랜치 정보 목록 조회를 한다.")               
    public RepositoryBranchListVo selectListRepositoryBranch(RepositoryBranchVo repositoryBranchVo) throws Exception {    	   	

        List<RepositoryBranchVo> repositoryBranchList = repositoryBranchService.selectListRepositoryBranch(repositoryBranchVo);                  
        long totCnt = repositoryBranchService.selectListCountRepositoryBranch(repositoryBranchVo);
	
		RepositoryBranchListVo retRepositoryBranchList = new RepositoryBranchListVo();
		retRepositoryBranchList.setRepositoryBranchVoList(repositoryBranchList); 
		retRepositoryBranchList.setTotalCount(totCnt);
		retRepositoryBranchList.setPageSize(repositoryBranchVo.getPageSize());
		retRepositoryBranchList.setPageIndex(repositoryBranchVo.getPageIndex());

        return retRepositoryBranchList;            
    }  
        
    /**
     * 레포지토리의 브랜치 정보을 단건 조회 처리 한다.
     *
     * @param  repositoryBranchVo 레포지토리의 브랜치 정보
     * @return 단건 조회 결과
     * @throws Exception
     */
    @ElService(key = "RepositoryBranchUpdView")    
    @RequestMapping(value="RepositoryBranchUpdView") 
    @ElDescription(sub = "레포지토리의 브랜치 정보 갱신 폼을 위한 조회", desc = "레포지토리의 브랜치 정보 갱신 폼을 위한 조회를 한다.")    
    public RepositoryBranchVo selectRepositoryBranch(RepositoryBranchVo repositoryBranchVo) throws Exception {
    	RepositoryBranchVo selectRepositoryBranchVo = repositoryBranchService.selectRepositoryBranch(repositoryBranchVo);    	    
		
        return selectRepositoryBranchVo;
    } 
 
    /**
     * 레포지토리의 브랜치 정보를 등록 처리 한다.
     *
     * @param  repositoryBranchVo 레포지토리의 브랜치 정보
     * @throws Exception
     */
    @ElService(key="RepositoryBranchIns")    
    @RequestMapping(value="RepositoryBranchIns")
    @ElDescription(sub="레포지토리의 브랜치 정보 등록처리",desc="레포지토리의 브랜치 정보를 등록 처리 한다.")
    public void insertRepositoryBranch(RepositoryBranchVo repositoryBranchVo) throws Exception {    	 
    	repositoryBranchService.insertRepositoryBranch(repositoryBranchVo);   
    }
       
    /**
     * 레포지토리의 브랜치 정보를 갱신 처리 한다.
     *
     * @param  repositoryBranchVo 레포지토리의 브랜치 정보
     * @throws Exception
     */
    @ElService(key="RepositoryBranchUpd")    
    @RequestMapping(value="RepositoryBranchUpd")    
    @ElValidator(errUrl="/repositoryBranch/repositoryBranchRegister", errContinue=true)
    @ElDescription(sub="레포지토리의 브랜치 정보 갱신처리",desc="레포지토리의 브랜치 정보를 갱신 처리 한다.")    
    public void updateRepositoryBranch(RepositoryBranchVo repositoryBranchVo) throws Exception {  
 
    	repositoryBranchService.updateRepositoryBranch(repositoryBranchVo);                                            
    }

    /**
     * 레포지토리의 브랜치 정보를 삭제 처리한다.
     *
     * @param  repositoryBranchVo 레포지토리의 브랜치 정보    
     * @throws Exception
     */
    @ElService(key = "RepositoryBranchDel")    
    @RequestMapping(value="RepositoryBranchDel")
    @ElDescription(sub = "레포지토리의 브랜치 정보 삭제처리", desc = "레포지토리의 브랜치 정보를 삭제 처리한다.")    
    public void deleteRepositoryBranch(RepositoryBranchVo repositoryBranchVo) throws Exception {
        repositoryBranchService.deleteRepositoryBranch(repositoryBranchVo);
    }
   
}
