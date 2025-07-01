package com.demo.proworks.filesrc.web;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.demo.proworks.filesrc.service.FileSrcService;
import com.demo.proworks.filesrc.vo.FileSrcVo;
import com.demo.proworks.filesrc.vo.FileSrcListVo;

import com.inswave.elfw.annotation.ElDescription;
import com.inswave.elfw.annotation.ElService;
import com.inswave.elfw.annotation.ElValidator;

/**  
 * @subject     : 파일  관련 처리를 담당하는 컨트롤러
 * @description : 파일  관련 처리를 담당하는 컨트롤러
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
public class FileSrcController {
	
    /** FileSrcService */
    @Resource(name = "fileSrcServiceImpl")
    private FileSrcService fileSrcService;
	
    
    /**
     * 파일  목록을 조회합니다.
     *
     * @param  fileSrcVo 파일 
     * @return 목록조회 결과
     * @throws Exception
     */
    @ElService(key="FileSrcList")
    @RequestMapping(value="FileSrcList")    
    @ElDescription(sub="파일  목록조회",desc="페이징을 처리하여 파일  목록 조회를 한다.")               
    public FileSrcListVo selectListFileSrc(FileSrcVo fileSrcVo) throws Exception {    	   	

        List<FileSrcVo> fileSrcList = fileSrcService.selectListFileSrc(fileSrcVo);                  
        long totCnt = fileSrcService.selectListCountFileSrc(fileSrcVo);
	
		FileSrcListVo retFileSrcList = new FileSrcListVo();
		retFileSrcList.setFileSrcVoList(fileSrcList); 
		retFileSrcList.setTotalCount(totCnt);
		retFileSrcList.setPageSize(fileSrcVo.getPageSize());
		retFileSrcList.setPageIndex(fileSrcVo.getPageIndex());

        return retFileSrcList;            
    }  
        
    /**
     * 파일 을 단건 조회 처리 한다.
     *
     * @param  fileSrcVo 파일 
     * @return 단건 조회 결과
     * @throws Exception
     */
    @ElService(key = "FileSrcUpdView")    
    @RequestMapping(value="FileSrcUpdView") 
    @ElDescription(sub = "파일  갱신 폼을 위한 조회", desc = "파일  갱신 폼을 위한 조회를 한다.")    
    public FileSrcVo selectFileSrc(FileSrcVo fileSrcVo) throws Exception {
    	FileSrcVo selectFileSrcVo = fileSrcService.selectFileSrc(fileSrcVo);    	    
		
        return selectFileSrcVo;
    } 
 
    /**
     * 파일 를 등록 처리 한다.
     *
     * @param  fileSrcVo 파일 
     * @throws Exception
     */
    @ElService(key="FileSrcIns")    
    @RequestMapping(value="FileSrcIns")
    @ElDescription(sub="파일  등록처리",desc="파일 를 등록 처리 한다.")
    public void insertFileSrc(FileSrcVo fileSrcVo) throws Exception {    	 
    	fileSrcService.insertFileSrc(fileSrcVo);   
    }
       
    /**
     * 파일 를 갱신 처리 한다.
     *
     * @param  fileSrcVo 파일 
     * @throws Exception
     */
    @ElService(key="FileSrcUpd")    
    @RequestMapping(value="FileSrcUpd")    
    @ElValidator(errUrl="/fileSrc/fileSrcRegister", errContinue=true)
    @ElDescription(sub="파일  갱신처리",desc="파일 를 갱신 처리 한다.")    
    public void updateFileSrc(FileSrcVo fileSrcVo) throws Exception {  
 
    	fileSrcService.updateFileSrc(fileSrcVo);                                            
    }

    /**
     * 파일 를 삭제 처리한다.
     *
     * @param  fileSrcVo 파일     
     * @throws Exception
     */
    @ElService(key = "FileSrcDel")    
    @RequestMapping(value="FileSrcDel")
    @ElDescription(sub = "파일  삭제처리", desc = "파일 를 삭제 처리한다.")    
    public void deleteFileSrc(FileSrcVo fileSrcVo) throws Exception {
        fileSrcService.deleteFileSrc(fileSrcVo);
    }
   
}
