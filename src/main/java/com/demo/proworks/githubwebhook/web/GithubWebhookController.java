package com.demo.proworks.githubwebhook.web;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.demo.proworks.githubwebhook.service.GithubWebhookService;
import com.demo.proworks.githubwebhook.vo.GithubWebhookVo;
import com.demo.proworks.githubwebhook.vo.GithubWebhookListVo;

import com.inswave.elfw.annotation.ElDescription;
import com.inswave.elfw.annotation.ElService;
import com.inswave.elfw.annotation.ElValidator;

/**  
 * @subject     : 깃허브 웹훅 관련 처리를 담당하는 컨트롤러
 * @description : 깃허브 웹훅 관련 처리를 담당하는 컨트롤러
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
public class GithubWebhookController {
	
    /** GithubWebhookService */
    @Resource(name = "githubWebhookServiceImpl")
    private GithubWebhookService githubWebhookService;
	
    
    /**
     * 깃허브 웹훅 목록을 조회합니다.
     *
     * @param  githubWebhookVo 깃허브 웹훅
     * @return 목록조회 결과
     * @throws Exception
     */
    @ElService(key="GithubWebhookList")
    @RequestMapping(value="GithubWebhookList")    
    @ElDescription(sub="깃허브 웹훅 목록조회",desc="페이징을 처리하여 깃허브 웹훅 목록 조회를 한다.")               
    public GithubWebhookListVo selectListGithubWebhook(GithubWebhookVo githubWebhookVo) throws Exception {    	   	

        List<GithubWebhookVo> githubWebhookList = githubWebhookService.selectListGithubWebhook(githubWebhookVo);                  
        long totCnt = githubWebhookService.selectListCountGithubWebhook(githubWebhookVo);
	
		GithubWebhookListVo retGithubWebhookList = new GithubWebhookListVo();
		retGithubWebhookList.setGithubWebhookVoList(githubWebhookList); 
		retGithubWebhookList.setTotalCount(totCnt);
		retGithubWebhookList.setPageSize(githubWebhookVo.getPageSize());
		retGithubWebhookList.setPageIndex(githubWebhookVo.getPageIndex());

        return retGithubWebhookList;            
    }  
        
    /**
     * 깃허브 웹훅을 단건 조회 처리 한다.
     *
     * @param  githubWebhookVo 깃허브 웹훅
     * @return 단건 조회 결과
     * @throws Exception
     */
    @ElService(key = "GithubWebhookUpdView")    
    @RequestMapping(value="GithubWebhookUpdView") 
    @ElDescription(sub = "깃허브 웹훅 갱신 폼을 위한 조회", desc = "깃허브 웹훅 갱신 폼을 위한 조회를 한다.")    
    public GithubWebhookVo selectGithubWebhook(GithubWebhookVo githubWebhookVo) throws Exception {
    	GithubWebhookVo selectGithubWebhookVo = githubWebhookService.selectGithubWebhook(githubWebhookVo);    	    
		
        return selectGithubWebhookVo;
    } 
 
    /**
     * 깃허브 웹훅를 등록 처리 한다.
     *
     * @param  githubWebhookVo 깃허브 웹훅
     * @throws Exception
     */
    @ElService(key="GithubWebhookIns")    
    @RequestMapping(value="GithubWebhookIns")
    @ElDescription(sub="깃허브 웹훅 등록처리",desc="깃허브 웹훅를 등록 처리 한다.")
    public void insertGithubWebhook(GithubWebhookVo githubWebhookVo) throws Exception {    	 
    	githubWebhookService.insertGithubWebhook(githubWebhookVo);   
    }
       
    /**
     * 깃허브 웹훅를 갱신 처리 한다.
     *
     * @param  githubWebhookVo 깃허브 웹훅
     * @throws Exception
     */
    @ElService(key="GithubWebhookUpd")    
    @RequestMapping(value="GithubWebhookUpd")    
    @ElValidator(errUrl="/githubWebhook/githubWebhookRegister", errContinue=true)
    @ElDescription(sub="깃허브 웹훅 갱신처리",desc="깃허브 웹훅를 갱신 처리 한다.")    
    public void updateGithubWebhook(GithubWebhookVo githubWebhookVo) throws Exception {  
 
    	githubWebhookService.updateGithubWebhook(githubWebhookVo);                                            
    }

    /**
     * 깃허브 웹훅를 삭제 처리한다.
     *
     * @param  githubWebhookVo 깃허브 웹훅    
     * @throws Exception
     */
    @ElService(key = "GithubWebhookDel")    
    @RequestMapping(value="GithubWebhookDel")
    @ElDescription(sub = "깃허브 웹훅 삭제처리", desc = "깃허브 웹훅를 삭제 처리한다.")    
    public void deleteGithubWebhook(GithubWebhookVo githubWebhookVo) throws Exception {
        githubWebhookService.deleteGithubWebhook(githubWebhookVo);
    }
   
}
