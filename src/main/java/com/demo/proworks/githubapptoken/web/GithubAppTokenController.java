package com.demo.proworks.githubapptoken.web;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.demo.proworks.githubapptoken.service.GithubAppTokenService;
import com.demo.proworks.githubapptoken.vo.GithubAppTokenVo;
import com.demo.proworks.githubapptoken.vo.GithubAppTokenListVo;

import com.inswave.elfw.annotation.ElDescription;
import com.inswave.elfw.annotation.ElService;
import com.inswave.elfw.annotation.ElValidator;

/**  
 * @subject     : 깃허브 앱 토큰 저장 관련 처리를 담당하는 컨트롤러
 * @description : 깃허브 앱 토큰 저장 관련 처리를 담당하는 컨트롤러
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
public class GithubAppTokenController {
	
    /** GithubAppTokenService */
    @Resource(name = "githubAppTokenServiceImpl")
    private GithubAppTokenService githubAppTokenService;
	
    
    /**
     * 깃허브 앱 토큰 저장 목록을 조회합니다.
     *
     * @param  githubAppTokenVo 깃허브 앱 토큰 저장
     * @return 목록조회 결과
     * @throws Exception
     */
    @ElService(key="GithubAppTokenList")
    @RequestMapping(value="GithubAppTokenList")    
    @ElDescription(sub="깃허브 앱 토큰 저장 목록조회",desc="페이징을 처리하여 깃허브 앱 토큰 저장 목록 조회를 한다.")               
    public GithubAppTokenListVo selectListGithubAppToken(GithubAppTokenVo githubAppTokenVo) throws Exception {    	   	

        List<GithubAppTokenVo> githubAppTokenList = githubAppTokenService.selectListGithubAppToken(githubAppTokenVo);                  
        long totCnt = githubAppTokenService.selectListCountGithubAppToken(githubAppTokenVo);
	
		GithubAppTokenListVo retGithubAppTokenList = new GithubAppTokenListVo();
		retGithubAppTokenList.setGithubAppTokenVoList(githubAppTokenList); 
		retGithubAppTokenList.setTotalCount(totCnt);
		retGithubAppTokenList.setPageSize(githubAppTokenVo.getPageSize());
		retGithubAppTokenList.setPageIndex(githubAppTokenVo.getPageIndex());

        return retGithubAppTokenList;            
    }  
        
    /**
     * 깃허브 앱 토큰 저장을 단건 조회 처리 한다.
     *
     * @param  githubAppTokenVo 깃허브 앱 토큰 저장
     * @return 단건 조회 결과
     * @throws Exception
     */
    @ElService(key = "GithubAppTokenUpdView")    
    @RequestMapping(value="GithubAppTokenUpdView") 
    @ElDescription(sub = "깃허브 앱 토큰 저장 갱신 폼을 위한 조회", desc = "깃허브 앱 토큰 저장 갱신 폼을 위한 조회를 한다.")    
    public GithubAppTokenVo selectGithubAppToken(GithubAppTokenVo githubAppTokenVo) throws Exception {
    	GithubAppTokenVo selectGithubAppTokenVo = githubAppTokenService.selectGithubAppToken(githubAppTokenVo);    	    
		
        return selectGithubAppTokenVo;
    } 
 
    /**
     * 깃허브 앱 토큰 저장를 등록 처리 한다.
     *
     * @param  githubAppTokenVo 깃허브 앱 토큰 저장
     * @throws Exception
     */
    @ElService(key="GithubAppTokenIns")    
    @RequestMapping(value="GithubAppTokenIns")
    @ElDescription(sub="깃허브 앱 토큰 저장 등록처리",desc="깃허브 앱 토큰 저장를 등록 처리 한다.")
    public void insertGithubAppToken(GithubAppTokenVo githubAppTokenVo) throws Exception {    	 
    	githubAppTokenService.insertGithubAppToken(githubAppTokenVo);   
    }
       
    /**
     * 깃허브 앱 토큰 저장를 갱신 처리 한다.
     *
     * @param  githubAppTokenVo 깃허브 앱 토큰 저장
     * @throws Exception
     */
    @ElService(key="GithubAppTokenUpd")    
    @RequestMapping(value="GithubAppTokenUpd")    
    @ElValidator(errUrl="/githubAppToken/githubAppTokenRegister", errContinue=true)
    @ElDescription(sub="깃허브 앱 토큰 저장 갱신처리",desc="깃허브 앱 토큰 저장를 갱신 처리 한다.")    
    public void updateGithubAppToken(GithubAppTokenVo githubAppTokenVo) throws Exception {  
 
    	githubAppTokenService.updateGithubAppToken(githubAppTokenVo);                                            
    }

    /**
     * 깃허브 앱 토큰 저장를 삭제 처리한다.
     *
     * @param  githubAppTokenVo 깃허브 앱 토큰 저장    
     * @throws Exception
     */
    @ElService(key = "GithubAppTokenDel")    
    @RequestMapping(value="GithubAppTokenDel")
    @ElDescription(sub = "깃허브 앱 토큰 저장 삭제처리", desc = "깃허브 앱 토큰 저장를 삭제 처리한다.")    
    public void deleteGithubAppToken(GithubAppTokenVo githubAppTokenVo) throws Exception {
        githubAppTokenService.deleteGithubAppToken(githubAppTokenVo);
    }
   
}
