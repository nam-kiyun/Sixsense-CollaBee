package com.demo.proworks.collabee.web;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.demo.proworks.collabee.service.UserPersonalTokenService;
import com.demo.proworks.collabee.vo.UserPersonalTokenVo;
import com.demo.proworks.collabee.vo.UserPersonalTokenListVo;

import com.inswave.elfw.annotation.ElDescription;
import com.inswave.elfw.annotation.ElService;
import com.inswave.elfw.annotation.ElValidator;

/**  
 * @subject     : 깃허브 개인 처리를 위한 PAT토큰 관련 처리를 담당하는 컨트롤러
 * @description : 깃허브 개인 처리를 위한 PAT토큰 관련 처리를 담당하는 컨트롤러
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
public class UserPersonalTokenController {
	
    /** UserPersonalTokenService */
    @Resource(name = "userPersonalTokenServiceImpl")
    private UserPersonalTokenService userPersonalTokenService;
	
    
    /**
     * 깃허브 개인 처리를 위한 PAT토큰 목록을 조회합니다.
     *
     * @param  userPersonalTokenVo 깃허브 개인 처리를 위한 PAT토큰
     * @return 목록조회 결과
     * @throws Exception
     */
    @ElService(key="UserPersonalTokenList")
    @RequestMapping(value="UserPersonalTokenList")    
    @ElDescription(sub="깃허브 개인 처리를 위한 PAT토큰 목록조회",desc="페이징을 처리하여 깃허브 개인 처리를 위한 PAT토큰 목록 조회를 한다.")               
    public UserPersonalTokenListVo selectListUserPersonalToken(UserPersonalTokenVo userPersonalTokenVo) throws Exception {    	   	

        List<UserPersonalTokenVo> userPersonalTokenList = userPersonalTokenService.selectListUserPersonalToken(userPersonalTokenVo);                  
        long totCnt = userPersonalTokenService.selectListCountUserPersonalToken(userPersonalTokenVo);
	
		UserPersonalTokenListVo retUserPersonalTokenList = new UserPersonalTokenListVo();
		retUserPersonalTokenList.setUserPersonalTokenVoList(userPersonalTokenList); 
		retUserPersonalTokenList.setTotalCount(totCnt);
		retUserPersonalTokenList.setPageSize(userPersonalTokenVo.getPageSize());
		retUserPersonalTokenList.setPageIndex(userPersonalTokenVo.getPageIndex());

        return retUserPersonalTokenList;            
    }  
        
    /**
     * 깃허브 개인 처리를 위한 PAT토큰을 단건 조회 처리 한다.
     *
     * @param  userPersonalTokenVo 깃허브 개인 처리를 위한 PAT토큰
     * @return 단건 조회 결과
     * @throws Exception
     */
    @ElService(key = "UserPersonalTokenUpdView")    
    @RequestMapping(value="UserPersonalTokenUpdView") 
    @ElDescription(sub = "깃허브 개인 처리를 위한 PAT토큰 갱신 폼을 위한 조회", desc = "깃허브 개인 처리를 위한 PAT토큰 갱신 폼을 위한 조회를 한다.")    
    public UserPersonalTokenVo selectUserPersonalToken(UserPersonalTokenVo userPersonalTokenVo) throws Exception {
    	UserPersonalTokenVo selectUserPersonalTokenVo = userPersonalTokenService.selectUserPersonalToken(userPersonalTokenVo);    	    
		
        return selectUserPersonalTokenVo;
    } 
 
    /**
     * 깃허브 개인 처리를 위한 PAT토큰를 등록 처리 한다.
     *
     * @param  userPersonalTokenVo 깃허브 개인 처리를 위한 PAT토큰
     * @throws Exception
     */
    @ElService(key="UserPersonalTokenIns")    
    @RequestMapping(value="UserPersonalTokenIns")
    @ElDescription(sub="깃허브 개인 처리를 위한 PAT토큰 등록처리",desc="깃허브 개인 처리를 위한 PAT토큰를 등록 처리 한다.")
    public void insertUserPersonalToken(UserPersonalTokenVo userPersonalTokenVo) throws Exception {    	 
    	userPersonalTokenService.insertUserPersonalToken(userPersonalTokenVo);   
    }
       
    /**
     * 깃허브 개인 처리를 위한 PAT토큰를 갱신 처리 한다.
     *
     * @param  userPersonalTokenVo 깃허브 개인 처리를 위한 PAT토큰
     * @throws Exception
     */
    @ElService(key="UserPersonalTokenUpd")    
    @RequestMapping(value="UserPersonalTokenUpd")    
    @ElValidator(errUrl="/userPersonalToken/userPersonalTokenRegister", errContinue=true)
    @ElDescription(sub="깃허브 개인 처리를 위한 PAT토큰 갱신처리",desc="깃허브 개인 처리를 위한 PAT토큰를 갱신 처리 한다.")    
    public void updateUserPersonalToken(UserPersonalTokenVo userPersonalTokenVo) throws Exception {  
 
    	userPersonalTokenService.updateUserPersonalToken(userPersonalTokenVo);                                            
    }

    /**
     * 깃허브 개인 처리를 위한 PAT토큰를 삭제 처리한다.
     *
     * @param  userPersonalTokenVo 깃허브 개인 처리를 위한 PAT토큰    
     * @throws Exception
     */
    @ElService(key = "UserPersonalTokenDel")    
    @RequestMapping(value="UserPersonalTokenDel")
    @ElDescription(sub = "깃허브 개인 처리를 위한 PAT토큰 삭제처리", desc = "깃허브 개인 처리를 위한 PAT토큰를 삭제 처리한다.")    
    public void deleteUserPersonalToken(UserPersonalTokenVo userPersonalTokenVo) throws Exception {
        userPersonalTokenService.deleteUserPersonalToken(userPersonalTokenVo);
    }
   
}
