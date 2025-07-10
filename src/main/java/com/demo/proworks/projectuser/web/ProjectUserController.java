package com.demo.proworks.projectuser.web;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

import com.demo.proworks.projectuser.service.ProjectUserService;
import com.demo.proworks.projectuser.vo.ProjectUserVo;
import com.demo.proworks.projectuser.vo.ProjectUserListVo;

import com.inswave.elfw.annotation.ElDescription;
import com.inswave.elfw.annotation.ElService;
import com.inswave.elfw.annotation.ElValidator;

/**  
 * @subject     : 프로젝트에 초대(참가)한 사람들 관련 처리를 담당하는 컨트롤러
 * @description : 프로젝트에 초대(참가)한 사람들 관련 처리를 담당하는 컨트롤러
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
public class ProjectUserController {
	
    /** ProjectUserService */
    @Resource(name = "projectUserServiceImpl")
    private ProjectUserService projectUserService;
	
    
    /**
     * 프로젝트에 초대(참가)한 사람들 목록을 조회합니다.
     *
     * @param  projectUserVo 프로젝트에 초대(참가)한 사람들
     * @return 목록조회 결과
     * @throws Exception
     */
    @ElService(key="ProjectUserList")
    @RequestMapping(value="ProjectUserList")    
    @ElDescription(sub="프로젝트에 초대(참가)한 사람들 목록조회",desc="페이징을 처리하여 프로젝트에 초대(참가)한 사람들 목록 조회를 한다.")               
    public ProjectUserListVo selectListProjectUser(ProjectUserVo projectUserVo) throws Exception {    	   	

        List<ProjectUserVo> projectUserList = projectUserService.selectListProjectUser(projectUserVo);                  
        long totCnt = projectUserService.selectListCountProjectUser(projectUserVo);
	
		ProjectUserListVo retProjectUserList = new ProjectUserListVo();
		retProjectUserList.setProjectUserVoList(projectUserList); 
		retProjectUserList.setTotalCount(totCnt);
		retProjectUserList.setPageSize(projectUserVo.getPageSize());
		retProjectUserList.setPageIndex(projectUserVo.getPageIndex());

        return retProjectUserList;            
    }  
        
    /**
     * 프로젝트에 초대(참가)한 사람들을 단건 조회 처리 한다.
     *
     * @param  projectUserVo 프로젝트에 초대(참가)한 사람들
     * @return 단건 조회 결과
     * @throws Exception
     */
    @ElService(key = "ProjectUserUpdView")    
    @RequestMapping(value="ProjectUserUpdView") 
    @ElDescription(sub = "프로젝트에 초대(참가)한 사람들 갱신 폼을 위한 조회", desc = "프로젝트에 초대(참가)한 사람들 갱신 폼을 위한 조회를 한다.")    
    public ProjectUserVo selectProjectUser(ProjectUserVo projectUserVo) throws Exception {
    	ProjectUserVo selectProjectUserVo = projectUserService.selectProjectUser(projectUserVo);    	    
		
        return selectProjectUserVo;
    } 
 
    /**
     * 프로젝트에 초대(참가)한 사람들를 등록 처리 한다.
     *
     * @param  projectUserVo 프로젝트에 초대(참가)한 사람들
     * @throws Exception
     */
    @ElService(key="ProjectUserIns")    
    @RequestMapping(value="ProjectUserIns")
    @ElDescription(sub="프로젝트에 초대(참가)한 사람들 등록처리",desc="프로젝트에 초대(참가)한 사람들를 등록 처리 한다.")
    public void insertProjectUser(ProjectUserVo projectUserVo) throws Exception {    	 
    	projectUserService.insertProjectUser(projectUserVo);   
    }
       
    /**
     * 프로젝트에 초대(참가)한 사람들를 갱신 처리 한다.
     *
     * @param  projectUserVo 프로젝트에 초대(참가)한 사람들
     * @throws Exception
     */
    @ElService(key="ProjectUserUpd")    
    @RequestMapping(value="ProjectUserUpd")    
    @ElValidator(errUrl="/projectUser/projectUserRegister", errContinue=true)
    @ElDescription(sub="프로젝트에 초대(참가)한 사람들 갱신처리",desc="프로젝트에 초대(참가)한 사람들를 갱신 처리 한다.")    
    public void updateProjectUser(ProjectUserVo projectUserVo) throws Exception {  
 
    	projectUserService.updateProjectUser(projectUserVo);                                            
    }

    /**
     * 프로젝트에 초대(참가)한 사람들를 삭제 처리한다.
     *
     * @param  projectUserVo 프로젝트에 초대(참가)한 사람들    
     * @throws Exception
     */
    @ElService(key = "ProjectUserDel")    
    @RequestMapping(value="ProjectUserDel")
    @ElDescription(sub = "프로젝트에 초대(참가)한 사람들 삭제처리", desc = "프로젝트에 초대(참가)한 사람들를 삭제 처리한다.")    
    public void deleteProjectUser(ProjectUserVo projectUserVo) throws Exception {
        projectUserService.deleteProjectUser(projectUserVo);
    }

    /**
     * 특정 프로젝트의 멤버 목록을 사용자 정보와 함께 조회한다.
     *
     * @param  request HTTP 요청 객체
     * @return 프로젝트 멤버 목록 (사용자 정보 포함)
     * @throws Exception
     */
    @ElService(key = "ProjectAccessSettings")    
    @RequestMapping(value="ProjectAccessSettings")
    @ElDescription(sub = "프로젝트 멤버 목록 조회", desc = "특정 프로젝트의 멤버 목록을 사용자 정보와 함께 조회한다.")    
    public ProjectUserListVo selectProjectUsersByProjectId(HttpServletRequest request) throws Exception {
        
        System.out.println("=== 프로젝트 멤버 목록 조회 시작 ===");
        
        // 여러 방법으로 projectId 파라미터 확인
        String projectIdFromParam = request.getParameter("projectId");
        String projectIdFromBody = null;
        
        System.out.println("파라미터에서 projectId: " + projectIdFromParam);
        
        // POST body에서 직접 읽기 시도
        if ("POST".equals(request.getMethod())) {
            try {
                java.io.BufferedReader reader = request.getReader();
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                String body = sb.toString();
                System.out.println("POST Body: " + body);
                
                // URL 인코딩된 데이터 파싱
                if (body.contains("projectId=")) {
                    String[] pairs = body.split("&");
                    for (String pair : pairs) {
                        if (pair.contains("=")) {
                            String[] keyValue = pair.split("=", 2);
                            if (keyValue.length == 2 && "projectId".equals(keyValue[0])) {
                                projectIdFromBody = keyValue[1];
                                System.out.println("Body에서 projectId: " + projectIdFromBody);
                                break;
                            }
                        }
                    }
                }
            } catch (Exception e) {
                System.err.println("POST body 읽기 오류: " + e.getMessage());
            }
        }
        
        // projectId 결정 (POST body 우선, 그 다음 파라미터)
        String projectId = null;
        if (projectIdFromBody != null && !projectIdFromBody.trim().isEmpty()) {
            projectId = projectIdFromBody;
        } else if (projectIdFromParam != null && !projectIdFromParam.trim().isEmpty()) {
            projectId = projectIdFromParam;
        }
        
        System.out.println("최종 사용할 projectId: " + projectId);
        
        if (projectId == null || projectId.trim().isEmpty()) {
            System.err.println("프로젝트 ID가 없습니다.");
            throw new Exception("프로젝트 ID가 필요합니다.");
        }
        
        ProjectUserVo projectUserVo = new ProjectUserVo();
        projectUserVo.setProjectId(projectId);
        
        List<ProjectUserVo> projectUserList = projectUserService.selectProjectUsersByProjectId(projectUserVo);
        System.out.println("조회된 멤버 수: " + projectUserList.size());
        
        ProjectUserListVo retProjectUserList = new ProjectUserListVo();
        retProjectUserList.setProjectUserVoList(projectUserList); 
        retProjectUserList.setTotalCount(projectUserList.size());
        retProjectUserList.setPageSize(projectUserList.size()); // 조회된 데이터 수와 같게
        retProjectUserList.setPageIndex(1); // 첫 번째 페이지
        
        System.out.println("=== 프로젝트 멤버 목록 조회 완료 ===");
        return retProjectUserList;
    }
   
}
