package com.demo.proworks.manager.web;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.demo.proworks.manager.service.ManagerService;
import com.demo.proworks.manager.vo.ManagerVo;
import com.demo.proworks.manager.vo.ManagerListVo;

import com.inswave.elfw.annotation.ElDescription;
import com.inswave.elfw.annotation.ElService;
import com.inswave.elfw.annotation.ElValidator;

/**  
 * @subject     : 업무 담당자 정보 관련 처리를 담당하는 컨트롤러
 * @description : 업무 담당자 정보 관련 처리를 담당하는 컨트롤러
 * @author      : 국다인
 * @since       : 2025/07/01
 * @modification
 * ===========================================================
 * DATE				AUTHOR				DESC
 * ===========================================================
 * 2025/07/01			 국다인	 		최초 생성
 * 
 */
@Controller
public class ManagerController {
	
    /** ManagerService */
    @Resource(name = "managerServiceImpl")
    private ManagerService managerService;
	
    
    /**
     * 업무 담당자 정보 목록을 조회합니다.
     *
     * @param  managerVo 업무 담당자 정보
     * @return 목록조회 결과
     * @throws Exception
     */
    @ElService(key="ManagerList")
    @RequestMapping(value="ManagerList")    
    @ElDescription(sub="업무 담당자 정보 목록조회",desc="페이징을 처리하여 업무 담당자 정보 목록 조회를 한다.")               
    public ManagerListVo selectListManager(ManagerVo managerVo) throws Exception {    	   	

        List<ManagerVo> managerList = managerService.selectListManager(managerVo);                  
        long totCnt = managerService.selectListCountManager(managerVo);
	
		ManagerListVo retManagerList = new ManagerListVo();
		retManagerList.setManagerVoList(managerList); 
		retManagerList.setTotalCount(totCnt);
		retManagerList.setPageSize(managerVo.getPageSize());
		retManagerList.setPageIndex(managerVo.getPageIndex());

        return retManagerList;            
    }  
        
    /**
     * 업무 담당자 정보을 단건 조회 처리 한다.
     *
     * @param  managerVo 업무 담당자 정보
     * @return 단건 조회 결과
     * @throws Exception
     */
    @ElService(key = "ManagerUpdView")    
    @RequestMapping(value="ManagerUpdView") 
    @ElDescription(sub = "업무 담당자 정보 갱신 폼을 위한 조회", desc = "업무 담당자 정보 갱신 폼을 위한 조회를 한다.")    
    public ManagerVo selectManager(ManagerVo managerVo) throws Exception {
    	ManagerVo selectManagerVo = managerService.selectManager(managerVo);    	    
		
        return selectManagerVo;
    } 
 
    /**
     * 업무 담당자 정보를 등록 처리 한다.
     *
     * @param  managerVo 업무 담당자 정보
     * @throws Exception
     */
    @ElService(key="ManagerIns")    
    @RequestMapping(value="ManagerIns")
    @ElDescription(sub="업무 담당자 정보 등록처리",desc="업무 담당자 정보를 등록 처리 한다.")
    public void insertManager(ManagerVo managerVo) throws Exception {    	 
    	managerService.insertManager(managerVo);   
    }
       
    /**
     * 업무 담당자 정보를 갱신 처리 한다.
     *
     * @param  managerVo 업무 담당자 정보
     * @throws Exception
     */
    @ElService(key="ManagerUpd")    
    @RequestMapping(value="ManagerUpd")    
    @ElValidator(errUrl="/manager/managerRegister", errContinue=true)
    @ElDescription(sub="업무 담당자 정보 갱신처리",desc="업무 담당자 정보를 갱신 처리 한다.")    
    public void updateManager(ManagerVo managerVo) throws Exception {  
 
    	managerService.updateManager(managerVo);                                            
    }

    /**
     * 업무 담당자 정보를 삭제 처리한다.
     *
     * @param  managerVo 업무 담당자 정보    
     * @throws Exception
     */
    @ElService(key = "ManagerDel")    
    @RequestMapping(value="ManagerDel")
    @ElDescription(sub = "업무 담당자 정보 삭제처리", desc = "업무 담당자 정보를 삭제 처리한다.")    
    public void deleteManager(ManagerVo managerVo) throws Exception {
        managerService.deleteManager(managerVo);
    }
   
}
