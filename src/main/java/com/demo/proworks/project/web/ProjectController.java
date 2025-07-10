package com.demo.proworks.project.web;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

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
    
    /** AmazonS3 */
    @Resource(name = "amazonS3")
    private AmazonS3 amazonS3;
	
    
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
        
        
        // 여러 방법으로 project_id 파라미터 확인
        String projectIdFromParam = request.getParameter("project_id");
        
        
        
        
        
        // 모든 파라미터 확인
        
        java.util.Enumeration<String> paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String paramName = paramNames.nextElement();
            String paramValue = request.getParameter(paramName);
            
        }
        
        // WebSquare 환경에서 POST body 직접 읽기
        String projectIdFromBody = null;
        if ("POST".equals(request.getMethod())) {
            try {
                
                java.io.BufferedReader reader = request.getReader();
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                String body = sb.toString();
                
                
                // URL 인코딩된 데이터 파싱
                if (body.contains("project_id=")) {
                    String[] pairs = body.split("&");
                    for (String pair : pairs) {
                        if (pair.contains("=")) {
                            String[] keyValue = pair.split("=", 2);
                            if (keyValue.length == 2 && "project_id".equals(keyValue[0])) {
                                projectIdFromBody = keyValue[1];
                                
                                break;
                            }
                        }
                    }
                }
            } catch (Exception e) {
                
                e.printStackTrace();
            }
        }
        
        // project_id 결정 (POST body 우선, 그 다음 파라미터)
        String projectId = null;
        if (projectIdFromBody != null && !projectIdFromBody.trim().isEmpty()) {
            projectId = projectIdFromBody;
            
        } else if (projectIdFromParam != null && !projectIdFromParam.trim().isEmpty()) {
            projectId = projectIdFromParam;
            
        }
        
        if (projectId == null || projectId.trim().isEmpty()) {
            
            return null;
        }
        
        // 새로운 ProjectVo 생성하여 조회
        ProjectVo queryVo = new ProjectVo();
        queryVo.setProjectId(projectId);
        
        
    	ProjectVo selectProjectVo = projectService.selectProject(queryVo);
    	
    	
    	if (selectProjectVo == null) {
    	    
    	    return null;
    	}
    	
    	// 프로젝트 이미지 URL이 없는 경우 기본 이미지 설정
    	if (selectProjectVo.getProjectImageUrl() == null || 
    		 selectProjectVo.getProjectImageUrl().trim().isEmpty()) {
    		
    		String defaultImageUrl = "https://collabee.s3.ap-northeast-2.amazonaws.com/projectImage/default_project_image.jpg";
    		selectProjectVo.setProjectImageUrl(defaultImageUrl);
    		
    	} else {
    	    
    	}
    	
    	
		
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
    
    /**
     * 프로젝트 이미지 업로드 처리
     *
     * @param file 업로드할 파일
     * @param projectId 프로젝트 ID
     * @return Map<String, Object> 업로드 결과
     * @throws Exception
     */
    @ElService(key = "uploadProjectImage")
    @RequestMapping(value = "uploadProjectImage")
    @ElDescription(sub = "프로젝트 이미지 업로드 처리", desc = "프로젝트 이미지를 S3에 업로드하고 DB에 URL을 저장한다.")
    public Map<String, Object> uploadProjectImage(
            MultipartHttpServletRequest request,
            @RequestParam(value = "projectId", required = false) String projectIdParam) throws Exception {
        
        Map<String, Object> result = new HashMap<>();
        
        try {
            System.out.println("=== 프로젝트 이미지 업로드 시작 ===");
            
            // MultipartHttpServletRequest에서 파일과 파라미터 추출
            MultipartFile file = request.getFile("file");
            String projectId = projectIdParam;
            
            // projectId가 없으면 다른 방법들로 확인
            if (projectId == null || projectId.trim().isEmpty()) {
                projectId = request.getParameter("projectId");
            }
            if (projectId == null || projectId.trim().isEmpty()) {
                projectId = request.getParameter("project_id");
            }
            
            System.out.println("Project ID: " + projectId);
            System.out.println("File name: " + (file != null ? file.getOriginalFilename() : "null"));
            System.out.println("File size: " + (file != null ? file.getSize() : "null"));
            System.out.println("File content type: " + (file != null ? file.getContentType() : "null"));
            
            if (file == null || file.isEmpty()) {
                System.out.println("파일이 없음 또는 비어있음");
                result.put("success", false);
                result.put("message", "파일이 선택되지 않았습니다.");
                System.out.println("응답 결과: " + result);
                return result;
            }
            
            if (projectId == null || projectId.trim().isEmpty()) {
                System.out.println("프로젝트 ID가 없음");
                result.put("success", false);
                result.put("message", "프로젝트 ID가 필요합니다.");
                System.out.println("응답 결과: " + result);
                return result;
            }
            
            // 파일 확장자 검증
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();
            System.out.println("파일 확장자: " + extension);
            
            if (!extension.matches("\\.(jpg|jpeg|png|gif|bmp|webp)$")) {
                System.out.println("지원되지 않는 파일 형식: " + extension);
                result.put("success", false);
                result.put("message", "지원되지 않는 이미지 형식입니다.");
                System.out.println("응답 결과: " + result);
                return result;
            }
            
            // 파일 크기 검증 (10MB 제한)
            if (file.getSize() > 10 * 1024 * 1024) {
                System.out.println("파일 크기 초과: " + file.getSize() + " bytes");
                result.put("success", false);
                result.put("message", "파일 크기가 10MB를 초과합니다.");
                System.out.println("응답 결과: " + result);
                return result;
            }
            
            System.out.println("유효성 검사 통과, S3 업로드 시작");
            
            // S3 업로드
            String imageUrl = uploadToS3(file, projectId);
            System.out.println("S3 업로드 완료, URL: " + imageUrl);
            
            // DB에 이미지 URL 저장
            ProjectVo projectVo = new ProjectVo();
            projectVo.setProjectId(projectId);
            projectVo.setProjectImageUrl(imageUrl);
            
            System.out.println("DB 업데이트 시작");
            projectService.updateProject(projectVo);
            System.out.println("DB 업데이트 완료");
            
            result.put("success", true);
            result.put("imageUrl", imageUrl);
            result.put("message", "이미지가 성공적으로 업로드되었습니다.");
            
            System.out.println("=== 프로젝트 이미지 업로드 완료 ===");
            System.out.println("최종 응답 결과: " + result);
            
        } catch (Exception e) {
            System.err.println("=== 프로젝트 이미지 업로드 오류 ===");
            System.err.println("오류 메시지: " + e.getMessage());
            System.err.println("오류 클래스: " + e.getClass().getName());
            e.printStackTrace();
            
            result.put("success", false);
            result.put("message", "이미지 업로드 중 오류가 발생했습니다: " + e.getMessage());
            
            System.out.println("오류 응답 결과: " + result);
        }
        
        return result;
    }
    
    /**
     * S3에 파일 업로드
     *
     * @param file 업로드할 파일
     * @param projectId 프로젝트 ID
     * @return S3 URL
     */
    private String uploadToS3(MultipartFile file, String projectId) throws IOException {
        String bucketName = "collabee";
        
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        
        // 고유한 파일명 생성
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String uuid = UUID.randomUUID().toString().substring(0, 8);
        String fileName = "project_" + projectId + "_" + timestamp + "_" + uuid + extension;
        String s3Key = "projectImage/" + fileName;
        
        System.out.println("S3 업로드 시작:");
        System.out.println("- 파일명: " + fileName);
        System.out.println("- S3 Key: " + s3Key);
        System.out.println("- 파일 크기: " + file.getSize() + " bytes");
        
        // S3 업로드를 위한 메타데이터 설정
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());
        
        // S3에 파일 업로드
        amazonS3.putObject(new PutObjectRequest(bucketName, s3Key, file.getInputStream(), metadata));
        
        // S3 URL 생성
        String s3Url = "https://" + bucketName + ".s3.ap-northeast-2.amazonaws.com/" + s3Key;
        
        System.out.println("S3 업로드 완료: " + s3Url);
        
        return s3Url;
    }
   
}
