package com.demo.proworks.project.web;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.demo.proworks.project.service.ProjectService;
import com.demo.proworks.project.vo.ProjectVo;
import com.demo.proworks.project.vo.ProjectListVo;

import com.inswave.elfw.annotation.ElDescription;
import com.inswave.elfw.annotation.ElService;
import com.inswave.elfw.annotation.ElValidator;
import org.springframework.web.bind.annotation.RequestMethod;

/**  
 * @subject     : 프로젝트 정보를 담는 테이블 관련 처리를 담당하는 컨트롤러
 * @description : 프로젝트 정보를 담는 테이블 관련 처리를 담당하는 컨트롤러
 * @author      : 남기윤
 * @since       : 2025/07/04
 * @modification
 * ===========================================================
 * DATE				AUTHOR				DESC
 * ===========================================================
 * 2025/07/04			 남기윤	 		최초 생성
 * 
 */
@Controller
public class ProjectController {
	
    /** ProjectService */
    @Resource(name = "projectServiceImpl")
    private ProjectService projectService;
	
    
    /**
     * 프로젝트 정보를 담는 테이블 목록을 조회합니다.
     *
     * @param  projectVo 프로젝트 정보를 담는 테이블
     * @return 목록조회 결과
     * @throws Exception
     */
    @ElService(key="ProjectList")
    @RequestMapping(value="ProjectList")    
    @ElDescription(sub="프로젝트 정보를 담는 테이블 목록조회",desc="페이징을 처리하여 프로젝트 정보를 담는 테이블 목록 조회를 한다.")               
    public ProjectListVo selectListProject(ProjectVo projectVo) throws Exception {    	   	

        List<ProjectVo> projectList = projectService.selectListProject(projectVo);                  
        long totCnt = projectService.selectListCountProject(projectVo);
	
		ProjectListVo retProjectList = new ProjectListVo();
		retProjectList.setProjectVoList(projectList); 
		retProjectList.setTotalCount(totCnt);
		retProjectList.setPageSize(projectVo.getPageSize());
		retProjectList.setPageIndex(projectVo.getPageIndex());

        return retProjectList;            
    }  
        
    /**
     * 프로젝트 정보를 담는 테이블을 단건 조회 처리 한다.
     *
     * @param  projectVo 프로젝트 정보를 담는 테이블
     * @return 단건 조회 결과
     * @throws Exception
     */
    @ElService(key = "ProjectUpdView")    
    @RequestMapping(value="ProjectUpdView") 
    @ElDescription(sub = "프로젝트 정보를 담는 테이블 갱신 폼을 위한 조회", desc = "프로젝트 정보를 담는 테이블 갱신 폼을 위한 조회를 한다.")    
    public ProjectVo selectProject(HttpServletRequest request) throws Exception {
        System.out.println("=== ProjectController.selectProject 시작 ===");
        
        // 여러 방법으로 project_id 파라미터 확인
        String projectIdFromParam = request.getParameter("project_id");
        
        System.out.println("request.getParameter('project_id'): " + projectIdFromParam);
        
        // 추가 디버깅 정보
        System.out.println("Request Method: " + request.getMethod());
        System.out.println("Content Type: " + request.getContentType());
        
        // 모든 파라미터 확인
        System.out.println("=== 모든 파라미터 확인 ===");
        java.util.Enumeration<String> paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String paramName = paramNames.nextElement();
            String paramValue = request.getParameter(paramName);
            System.out.println("파라미터 [" + paramName + "] = " + paramValue);
        }
        
        // WebSquare 환경에서 POST body 직접 읽기
        String projectIdFromBody = null;
        if ("POST".equals(request.getMethod())) {
            try {
                System.out.println("=== POST body 직접 읽기 ===");
                java.io.BufferedReader reader = request.getReader();
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                String body = sb.toString();
                System.out.println("POST body 내용: " + body);
                
                // URL 인코딩된 데이터 파싱
                if (body.contains("project_id=")) {
                    String[] pairs = body.split("&");
                    for (String pair : pairs) {
                        if (pair.contains("=")) {
                            String[] keyValue = pair.split("=", 2);
                            if (keyValue.length == 2 && "project_id".equals(keyValue[0])) {
                                projectIdFromBody = keyValue[1];
                                System.out.println("POST body에서 project_id 추출: " + projectIdFromBody);
                                break;
                            }
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println("POST body 읽기 오류: " + e.getMessage());
                e.printStackTrace();
            }
        }
        
        // project_id 결정 (POST body 우선, 그 다음 파라미터)
        String projectId = null;
        if (projectIdFromBody != null && !projectIdFromBody.trim().isEmpty()) {
            projectId = projectIdFromBody;
            System.out.println("POST body에서 project_id 사용: " + projectId);
        } else if (projectIdFromParam != null && !projectIdFromParam.trim().isEmpty()) {
            projectId = projectIdFromParam;
            System.out.println("파라미터에서 project_id 사용: " + projectId);
        }
        
        if (projectId == null || projectId.trim().isEmpty()) {
            System.out.println("ERROR: project_id가 null 또는 빈값입니다.");
            return null;
        }
        
        // 새로운 ProjectVo 생성하여 조회
        ProjectVo queryVo = new ProjectVo();
        queryVo.setProjectId(projectId);
        System.out.println("DB 조회용 ProjectVo 생성: " + queryVo.toString());
        
    	ProjectVo selectProjectVo = projectService.selectProject(queryVo);
    	System.out.println("DB 조회 결과: " + (selectProjectVo != null ? selectProjectVo.toString() : "null"));
    	
    	if (selectProjectVo == null) {
    	    System.out.println("ERROR: DB에서 project_id=" + projectId + " 데이터를 찾을 수 없습니다.");
    	    return null;
    	}
    	
    	// 프로젝트 이미지 URL이 없는 경우 기본 이미지 설정
    	if (selectProjectVo.getProjectImageUrl() == null || 
    		 selectProjectVo.getProjectImageUrl().trim().isEmpty()) {
    		
    		String defaultImageUrl = "https://collabee.s3.ap-northeast-2.amazonaws.com/projectImage/default_project_image.jpg";
    		selectProjectVo.setProjectImageUrl(defaultImageUrl);
    		System.out.println("프로젝트 " + selectProjectVo.getProjectId() + "에 기본 이미지 설정: " + defaultImageUrl);
    	} else {
    	    System.out.println("기존 프로젝트 이미지 URL 사용: " + selectProjectVo.getProjectImageUrl());
    	}
    	
    	System.out.println("최종 반환 데이터: " + selectProjectVo.toString());
    	System.out.println("=== ProjectController.selectProject 완료 ===");
		
        return selectProjectVo;
    } 
 
    /**
     * 프로젝트 정보를 담는 테이블를 등록 처리 한다.
     *
     * @param  projectVo 프로젝트 정보를 담는 테이블
     * @throws Exception
     */
    @ElService(key = "project/create")    
    @RequestMapping(value = "project/create")
    @ElDescription(sub = "프로젝트 정보를 담는 테이블 등록처리", desc = "프로젝트 정보를 담는 테이블를 등록 처리 한다.")
    public void insertProject(ProjectVo projectVo) throws Exception {    	 
    	projectService.insertProject(projectVo);   
    }
       
    /**
     * 프로젝트 정보를 담는 테이블를 갱신 처리 한다.
     *
     * @param  projectVo 프로젝트 정보를 담는 테이블
     * @throws Exception
     */
    @ElService(key="ProjectUpd")    
    @RequestMapping(value="ProjectUpd")    
    @ElValidator(errUrl="/project/projectRegister", errContinue=true)
    @ElDescription(sub="프로젝트 정보를 담는 테이블 갱신처리",desc="프로젝트 정보를 담는 테이블를 갱신 처리 한다.")    
    public void updateProject(ProjectVo projectVo) throws Exception {  
 
    	projectService.updateProject(projectVo);                                            
    }

    /**
     * 프로젝트 정보를 담는 테이블를 삭제 처리한다.
     *
     * @param  projectVo 프로젝트 정보를 담는 테이블    
     * @throws Exception
     */
    @ElService(key = "ProjectDel")    
    @RequestMapping(value="ProjectDel")
    @ElDescription(sub = "프로젝트 정보를 담는 테이블 삭제처리", desc = "프로젝트 정보를 담는 테이블를 삭제 처리한다.")    
    public void deleteProject(ProjectVo projectVo) throws Exception {
        projectService.deleteProject(projectVo);
    }
   
}
