package com.demo.proworks.emp.web;

import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.inswave.elfw.annotation.ElDescription;
import com.inswave.elfw.annotation.ElService;
import com.inswave.elfw.annotation.ElValidator;
import com.inswave.elfw.core.ElResDataVO;
import com.inswave.elfw.exception.UserException;
import com.inswave.elfw.log.AppLog;
import com.inswave.elfw.login.LoginInfo;
import com.inswave.elfw.login.LoginProcessor;
import com.inswave.elfw.util.ElHttpUtil;

import com.demo.proworks.cmmn.ProworksUserHeader;
import com.demo.proworks.cmmn.tcp.ProworksTcpLengthBaseSendUtil;
import com.demo.proworks.emp.service.EmpFileService;
import com.demo.proworks.emp.service.EmpService;
import com.demo.proworks.emp.vo.DeptListVo;
import com.demo.proworks.emp.vo.EmpListVo;
import com.demo.proworks.emp.vo.EmpVo;

/**
 * @subject		: 데모용 사원 정보 관련 처리를 하는 컨트롤러
 * @description	: 데모용 사원 정보 관련 처리를 하는 컨트롤러
 * @author		: 개발팀
 * @since 		: 2025/05/19
 * @modification
 * ===========================================================
 * DATE				AUTHOR				NOTE
 * ===========================================================
 * 2025/05/19		샘플개발팀			최초 생성
 * 
 */
@Controller
public class EmpController {
	
    @Resource(name = "empServiceImpl")
    private EmpService empService;
    
    @Resource(name = "empFileServiceImpl")
    private EmpFileService empFileService;
    
    @Resource(name = "loginProcess")
	protected LoginProcessor loginProcess;
    
	/**
	 * 로그인을 처리한다.
	 * @param loginVo 로그인 정보 LoginVo
	 * @param request 요청 정보 HttpServletRequest
	 * @throws Exception
	 */
	@ElService(key = "CmmLogin")
    @RequestMapping(value = "CmmLogin")
    @ElDescription(sub = "로그인", desc = "로그인을 처리한다.")
    public void login(com.demo.proworks.emp.vo.LoginVo loginVo, HttpServletRequest request) throws Exception {
    	String id = loginVo.getId();
    	String pw = loginVo.getPw();
    	
    	LoginInfo info = loginProcess.processLogin(request, id, pw);
    	
    	AppLog.debug("- Login 정보 : " + info.toString());
    }
	
	/**
	 * 로그인 폼 페이지를 로드한다.
	 * @param loginVo 로그인 정보 LoginVo
	 * @param request 요청 정보 HttpServletRequest
	 * @throws Exception
	 */
	@ElService(key = "CmmLoginFrm")    
    @RequestMapping(value = "CmmLoginFrm")   
    @ElDescription(sub = "로그인 폼 페이지 로드", desc = "로그인 폼 페이지를 로드한다.")           
    public void loginFrm(com.demo.proworks.emp.vo.LoginVo loginVo, HttpServletRequest request) throws Exception {    
		String id = loginVo.getId();
		
		loginProcess.processLogout(request, id);
    }
    
	/**
	 * 페이징을 처리하여 사원 목록을 조회한다.
	 * @param empVo 사원 정보 EmpVo
	 * @return 사원 목록 정보 EmpListVo
	 * @throws Exception
	 */
	@ElService(key = "DmoEmpList")
    @RequestMapping(value = "DmoEmpList")    
    @ElDescription(sub = "사원 목록 조회", desc = "페이징을 처리하여 사원 목록을 조회한다.")               
    public EmpListVo selectEmpList(EmpVo empVo) throws Exception {
    	AppLog.debug("사원 목록 조회 로그 테스트 : " + empVo);
    	
        List<EmpVo> empList = empService.selectListEmp(empVo);
        long totCnt = empService.selectListCountEmp(empVo);
        
		EmpListVo retEmpVoList = new EmpListVo();
		retEmpVoList.setEmpVoList(empList); 
		retEmpVoList.setDeptVoList(empService.selectListDept(new HashMap()));
		retEmpVoList.setTotalCount(totCnt);

        return retEmpVoList;
    } 

    /**
     * 사원 등록 폼을 위한 조회 처리를 한다.
     * @param empVo 사원 정보 EmpVo
     * @return 사원 등록 폼 정보 DeptListVo
     * @throws Exception
     */
    @ElService(key = "DmoEmpView")    
    @RequestMapping(value = "DmoEmpView")
    @ElDescription(sub = "사원 등록 폼을 위한 조회", desc = "사원 등록 폼을 위한 조회 처리를 한다.")  
    public DeptListVo addEmpView(EmpVo empVo) throws Exception {
    	DeptListVo deptListVo = new DeptListVo();
        deptListVo.setDeptVoList(empService.selectListDept(new HashMap()));
        
        return deptListVo;
    }    

    /**
     * 사원 정보를 등록 처리한다.
     * @param empVo 사원 정보 EmpVo
     * @return 사원 등록 폼 정보 DeptListVo
     * @throws Exception
     */
    @ElService(key = "DmoEmpIns")    
    @RequestMapping(value = "DmoEmpIns")
    @ElDescription(sub = "사원 등록 처리", desc = "사원 정보를 등록 처리한다.")
    @ElValidator(errUrl="")
    public DeptListVo addEmp(EmpVo empVo) throws Exception {    	
    	DeptListVo deptListVo = new DeptListVo();
    	deptListVo.setDeptVoList(empService.selectListDept(new HashMap()));
    	
    	empService.insertEmp(empVo);   
    	
    	return deptListVo;
    }
        
    /**
     * 사원 갱신 폼을 위한 조회 처리를 한다.
     * @param empVo 사원 정보 EmpVo
     * @return 사원 갱신 폼 정보 EmpListVo
     * @throws Exception
     */
    @ElService(key = "DmoEmpUpdView")    
    @RequestMapping(value = "DmoEmpUpdView") 
    @ElDescription(sub = "사원 갱신 폼을 위한 조회", desc = "사원 갱신 폼을 위한 조회 처리를 한다.")    
    public EmpListVo updateEmpView(EmpVo empVo) throws Exception {
    	EmpVo selectEmpVo = empService.selectEmp(empVo);
		EmpVo empFileVo = empFileService.selectEmpFile(empVo);
		
		if(empFileVo != null) {
			selectEmpVo.setFileName(empFileVo.getFileName());
		}		 	
  
        EmpListVo retEmpVoList = new EmpListVo();
        retEmpVoList.setEmpVo(selectEmpVo);
        retEmpVoList.setDeptVoList(empService.selectListDept(new HashMap()));
        
		return retEmpVoList;
    } 
    
	/**
	 * 사원 정보를 갱신 처리한다.
	 * @param empVo 사원 정보 EmpVo
	 * @return 사원 갱신 폼 정보 DeptListVo
	 * @throws Exception
	 */
	@ElService(key = "DmoEmpUpd")    
    @RequestMapping(value = "DmoEmpUpd")    
    @ElDescription(sub = "사원 갱신 처리", desc = "사원 정보를 갱신 처리한다.")    
	@ElValidator(errUrl = "")
    public DeptListVo updateEmp(EmpVo empVo) throws Exception {  
		DeptListVo deptListVo = new DeptListVo();
		deptListVo.setDeptVoList(empService.selectListDept(new HashMap()));
		
    	empService.updateEmp(empVo);
    	
    	return deptListVo;
    }

    /**
     * 사원 정보를 삭제 처리한다.
     * @param empVo 사원 정보 EmpVo
     * @throws Exception
     */
    @ElService(key = "DmoEmpDel")    
    @RequestMapping(value = "DmoEmpDel")
    @ElDescription(sub = "사원 삭제 처리", desc = "사원 정보를 삭제 처리한다.")    
    public void deleteEmp(EmpVo empVo) throws Exception {
        empService.deleteEmp(empVo);
    }
   
    /**
     * 사원 첨부파일을 다운로드한다.
     * @param empVo 사원 정보 EmpVo
     * @param response 응답 정보 HttpServletResponse
     * @throws Exception
     */
    @ElService(key = "DmoEmpDown")    
    @RequestMapping(value = "DmoEmpDown")
    @ElDescription(sub = "사원 첨부파일 다운로드", desc = "사원 첨부파일을 다운로드한다.")    
    public void downEmpFile(EmpVo empVo, HttpServletResponse response) throws Exception {
    	empVo = empFileService.selectEmpFile(empVo);
    	
	    byte[] data = empVo.getFileData();  
	    String fileName = empVo.getFileName();
	    
	    fileName = new String(fileName.getBytes("EUC-KR"), "ISO-8859-1");
	    
	    response.reset();  
	    response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");  
	    response.addHeader("Content-Length", "" + data.length);  
	    response.setContentType("application/octet-stream;charset=EUC-KR");
	    
	    OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());  
	    outputStream.write(data);  
	    outputStream.flush();  
	    outputStream.close();
    }
    
    /**
     * 사원 첨부파일을 업로드한다.
     * @param request 요청 정보 HttpServletRequest
     * @param empVo 사원 정보 EmpVo
     * @param model 모델 정보 Model
     * @throws Exception
     */
    @ElService(key = "DmoEmpUploadEmpFile")    
    @RequestMapping(value = "DmoEmpUploadEmpFile")
    @ElDescription(sub = "사원 첨부파일 업로드", desc = "사원 첨부파일을 업로드한다.")
    public String uploadEmpFileToDb(HttpServletRequest request, EmpVo empVo, Model model) throws Exception {  
    	MultipartHttpServletRequest multiPartRequest = (MultipartHttpServletRequest) request;
    	Iterator<String> iter = multiPartRequest.getFileNames();
    	
    	while(iter.hasNext()) {
    		String fileName = iter.next();
    		MultipartFile inputFileData = (MultipartFile) multiPartRequest.getFile(fileName);
    		empVo.setInputFileData(inputFileData);
    		empFileService.uploadEmpFile(empVo);
    	}
    	
    	model.addAttribute("empVo", empVo);
    	
    	return "/cmmn/upload";
    }
    
    /**
     * 사원 첨부파일을 갱신 처리한다.
     * @param request 요청 정보 HttpServletRequest
     * @param empVo 사원 정보 EmpVo
     * @param model 모델 정보 Model
     * @throws Exception
     */
    @ElService(key = "DmoEmpUpdateUploadEmpFile")    
    @RequestMapping(value = "DmoEmpUpdateUploadEmpFile")
    @ElDescription(sub = "사원 첨부파일 갱신 처리", desc = "사원 첨부파일을 갱신 처리한다.")
    public String updateUploadEmpFile(HttpServletRequest request, EmpVo empVo, Model model) throws Exception {  
    	MultipartHttpServletRequest multiPartRequest = (MultipartHttpServletRequest) request;
    	Iterator<String> iter = multiPartRequest.getFileNames();
    	
    	while(iter.hasNext()) {
    		String fileName = iter.next();
    		MultipartFile inputFileData = (MultipartFile) multiPartRequest.getFile(fileName);
    		empVo.setInputFileData(inputFileData);
    		empService.updateEmp(empVo);
    	}
    	
    	model.addAttribute("empVo", empVo);
    	
    	return "/cmmn/upload";
    }
    
    
    ////////////////////////////////////////////////// TCP 샘플 //////////////////////////////////////////////////
    
    /**
     * 사원 정보 상세 조회(TCP 내부 테스트용)를 한다.
     * @param empVo 사원 정보 EmpVo
     * @return 사원 정보 EmpVo
     * @throws Exception
     */
    @ElService(key = "DmoEmpSelect")    
    @RequestMapping(value = "DmoEmpSelect") 
    @ElDescription(sub = "사원 정보 상세 조회(TCP 내부 테스트용)", desc = "사원 정보 상세 조회(TCP 내부 테스트용)를 한다.")    
    public EmpVo selectEmpView(EmpVo empVo) throws Exception {
    	return empService.selectEmp(empVo);
    } 
    
    /**
     * TCP 송신 테스트를 한다.
     * @param empVo 사원 정보 EmpVo
     * @return 사원 정보 EmpVo
     * @throws Exception
     */
	@ElService(key = "DmoTcpSendTest")    
	@RequestMapping(value = "DmoTcpSendTest") 
	@ElDescription(sub = "TCP 송신 테스트", desc = "TCP 송신 테스트를 한다.")    
	public EmpVo tcpSendTest(EmpVo empVo) throws Exception {
		// TCP 테스트 샘플 
		EmpVo eInVo = new EmpVo();
		eInVo.setEmpno(1111);
		EmpVo eOutVo = new EmpVo();
		
		ProworksUserHeader userHeader = new ProworksUserHeader();
		userHeader.setSvcId("DmoEmpSelect");
		userHeader.setTestId("TEST_ID");
		
		ElResDataVO resVo = ProworksTcpLengthBaseSendUtil.sendFld("localhost", 9999, userHeader, eInVo, eOutVo, 10, false, "EUC-KR");
		
		EmpVo resEmpVo = (EmpVo) resVo.getVo();
		ProworksUserHeader resUserHeader =(ProworksUserHeader) resVo.getUserHeader();
		
		AppLog.debug("resTcp EmpVo:" + resEmpVo);
		AppLog.debug("resTcp resUserHeader:" + resUserHeader);
			
		return resEmpVo;
	}    

	
    ////////////////////////////////////////////////// JSP 샘플 //////////////////////////////////////////////////
	
    /**
     * 사원 갱신 폼을 위한 JSP 조회를 한다.
     * @param empVo 사원 정보 EmpVo
     * @return 사원 갱신 폼 정보 EmpListVo
     * @throws Exception
     */
    @ElService(key = "DmoEmpUpdViewJsp")    
    @RequestMapping(value = "DmoEmpUpdViewJsp") 
    @ElDescription(sub = "사원 갱신 폼을 위한 조회 JSP", desc = "사원 갱신 폼을 위한 JSP 조회를 한다.")    
    public EmpListVo updateEmpViewJsp(EmpVo empVo) throws Exception {
    	EmpVo selectEmpVo = empService.selectEmp(empVo);    	    
		EmpVo empFileVo = empFileService.selectEmpFile(empVo);
		
		if(empFileVo != null) {
			selectEmpVo.setFileName(empFileVo.getFileName());
		}		 	
		
        EmpListVo retEmpVoList = new EmpListVo();
        retEmpVoList.setEmpVo(selectEmpVo);
        retEmpVoList.setDeptVoList(empService.selectListDept(new HashMap()));
        
        ElHttpUtil.setViewResolverName("emp/empRegister");
        
		return retEmpVoList;
    }
        
}
