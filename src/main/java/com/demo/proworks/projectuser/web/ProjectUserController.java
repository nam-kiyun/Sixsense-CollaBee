package com.demo.proworks.projectuser.web;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

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
        
        
        
        // 여러 방법으로 projectId 파라미터 확인
        String projectIdFromParam = request.getParameter("projectId");
        String projectIdFromBody = null;
        
        
        
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
                
                
                // URL 인코딩된 데이터 파싱
                if (body.contains("projectId=")) {
                    String[] pairs = body.split("&");
                    for (String pair : pairs) {
                        if (pair.contains("=")) {
                            String[] keyValue = pair.split("=", 2);
                            if (keyValue.length == 2 && "projectId".equals(keyValue[0])) {
                                projectIdFromBody = keyValue[1];
                                
                                break;
                            }
                        }
                    }
                }
            } catch (Exception e) {
                
            }
        }
        
        // projectId 결정 (POST body 우선, 그 다음 파라미터)
        String projectId = null;
        if (projectIdFromBody != null && !projectIdFromBody.trim().isEmpty()) {
            projectId = projectIdFromBody;
        } else if (projectIdFromParam != null && !projectIdFromParam.trim().isEmpty()) {
            projectId = projectIdFromParam;
        }
        
       
        
        if (projectId == null || projectId.trim().isEmpty()) {
            
            throw new Exception("프로젝트 ID가 필요합니다.");
        }
        
        ProjectUserVo projectUserVo = new ProjectUserVo();
        projectUserVo.setProjectId(projectId);
        
        List<ProjectUserVo> projectUserList = projectUserService.selectProjectUsersByProjectId(projectUserVo);
        
        
        ProjectUserListVo retProjectUserList = new ProjectUserListVo();
        retProjectUserList.setProjectUserVoList(projectUserList); 
        retProjectUserList.setTotalCount(projectUserList.size());
        retProjectUserList.setPageSize(projectUserList.size()); // 조회된 데이터 수와 같게
        retProjectUserList.setPageIndex(1); // 첫 번째 페이지
        
        
        return retProjectUserList;
    }

    /**
     * userId로 사용자를 검색한다.
     *
     * @param request HTTP 요청 객체
     * @return Map<String, Object> 검색 결과
     * @throws Exception
     */
    @ElService(key="ProjectUserSearch")    
    @RequestMapping(value="ProjectUserSearch")
    @ElDescription(sub="사용자 ID 검색", desc="userId로 사용자를 검색하고 프로젝트 멤버 여부를 확인한다.")    
    public Map<String, Object> searchUser(HttpServletRequest request) throws Exception {
        
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 파라미터 읽기
            String userId = request.getParameter("userId");
            String projectId = request.getParameter("projectId");
            
            if (userId == null || projectId == null) {
                result.put("found", false);
                result.put("isProjectMember", false);
                result.put("error", "필수 파라미터가 누락되었습니다.");
                return result;
            }
            
            // userId로 사용자 검색
            ProjectUserVo userInfo = projectUserService.searchUserByUserId(userId, projectId);
            
            if (userInfo == null) {
                // 사용자가 존재하지 않음
                result.put("found", false);
                result.put("isProjectMember", false);
                result.put("userId", "");
                result.put("userName", "");
                result.put("email", userId);
                result.put("profileImageSrc", "");
            } else {
                // 사용자가 존재함
                result.put("found", true);
                result.put("userId", userInfo.getUserId());
                result.put("userName", userInfo.getUserName());
                result.put("email", userInfo.getUserId()); // user_id가 이메일이므로
                result.put("profileImageSrc", ""); // 프로필 이미지는 추후 구현
                
                // 이미 프로젝트 멤버인지 확인 (role이 있으면 멤버)
                boolean isProjectMember = (userInfo.getRole() != null && !userInfo.getRole().isEmpty());
                result.put("isProjectMember", isProjectMember);
            }
            
        } catch (Exception e) {
            result.put("found", false);
            result.put("isProjectMember", false);
            result.put("error", "사용자 검색 중 오류가 발생했습니다.");
        }
        
        return result;
    }

    /**
     * 프로젝트에 사용자를 초대한다.
     *
     * @param request HTTP 요청
     * @return Map<String, Object> 초대 결과
     * @throws Exception
     */
    @RequestMapping(value = "/project/{projectId}/settings/access", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> inviteUser(HttpServletRequest request) throws Exception {
        
        Map<String, Object> result = new HashMap<>();
        
        try {
            // URL 경로에서 projectId 추출
            String requestURI = request.getRequestURI();
            String projectId = null;
            if (requestURI.contains("/project/")) {
                String[] parts = requestURI.split("/");
                for (int i = 0; i < parts.length; i++) {
                    if ("project".equals(parts[i]) && i + 1 < parts.length) {
                        projectId = parts[i + 1];
                        break;
                    }
                }
            }
            
            // POST body에서 파라미터 읽기
            String userId = null;
            String role = "editor"; // 기본값
            
            try {
                java.io.BufferedReader reader = request.getReader();
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                String body = sb.toString();
                
                // JSON 형태로 파싱 (간단한 방식)
                if (body.contains("\"userId\"")) {
                    // JSON 파싱
                    String[] pairs = body.replace("{", "").replace("}", "").replace("\"", "").split(",");
                    for (String pair : pairs) {
                        String[] keyValue = pair.split(":", 2);
                        if (keyValue.length == 2) {
                            String key = keyValue[0].trim();
                            String value = keyValue[1].trim();
                            
                            if ("userId".equals(key)) {
                                userId = value;
                            } else if ("role".equals(key)) {
                                role = value;
                            }
                        }
                    }
                }
            } catch (Exception e) {
                // JSON 파싱 실패 시 URL 인코딩된 파라미터로 시도
                userId = request.getParameter("userId");
                String roleParam = request.getParameter("role");
                if (roleParam != null && !roleParam.isEmpty()) {
                    role = roleParam;
                }
            }
            
            if (projectId == null || userId == null) {
                result.put("success", false);
                result.put("message", "필수 파라미터가 누락되었습니다.");
                return result;
            }
            
            // ProjectUser 추가
            ProjectUserVo projectUserVo = new ProjectUserVo();
            projectUserVo.setProjectId(projectId);
            projectUserVo.setUserId(userId);
            projectUserVo.setRole(role);
            
            int insertResult = projectUserService.inviteUserToProject(projectUserVo);
            
            if (insertResult > 0) {
                result.put("success", true);
                result.put("message", "사용자가 성공적으로 프로젝트에 추가되었습니다.");
            } else {
                result.put("success", false);
                result.put("message", "사용자 추가에 실패했습니다.");
            }
            
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "사용자 초대 중 오류가 발생했습니다: " + e.getMessage());
        }
        
        return result;
    }

    /**
     * 프로젝트 사용자의 역할을 변경한다.
     *
     * @param request HTTP 요청 객체
     * @return Map<String, Object> 역할 변경 결과
     * @throws Exception
     */
    @ElService(key = "updateProjectUserRole")    
    @RequestMapping(value="updateProjectUserRole")
    @ElDescription(sub = "프로젝트 사용자 역할 변경", desc = "프로젝트 사용자의 역할을 변경한다.")    
    public Map<String, Object> updateProjectUserRole(HttpServletRequest request) throws Exception {
        Map<String, Object> result = new HashMap<>();
        
        try {
            String projectId = request.getParameter("projectId");
            String userId = request.getParameter("userId");
            String role = request.getParameter("role");
            
            if (projectId == null || userId == null || role == null) {
                result.put("success", false);
                result.put("message", "필수 파라미터가 누락되었습니다.");
                return result;
            }
            
            ProjectUserVo projectUserVo = new ProjectUserVo();
            projectUserVo.setProjectId(projectId);
            projectUserVo.setUserId(userId);
            projectUserVo.setRole(role);
            
            int updateResult = projectUserService.updateProjectUserRole(projectUserVo);
            
            if (updateResult > 0) {
                result.put("success", true);
                result.put("message", "역할이 성공적으로 변경되었습니다.");
            } else {
                result.put("success", false);
                result.put("message", "역할 변경에 실패했습니다.");
            }
            
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "역할 변경 중 오류가 발생했습니다: " + e.getMessage());
        }
        
        return result;
    }

    /**
     * 특정 사용자의 프로젝트 내 역할을 확인한다.
     *
     * @param request HTTP 요청 객체
     * @return Map<String, Object> 사용자 역할 정보
     * @throws Exception
     */
    @ElService(key = "checkUserRole")    
    @RequestMapping(value="checkUserRole")
    @ElDescription(sub = "사용자 역할 확인", desc = "특정 사용자의 프로젝트 내 역할을 확인한다.")    
    public Map<String, Object> checkUserRole(HttpServletRequest request) throws Exception {
        Map<String, Object> result = new HashMap<>();
        
        try {
            String projectId = request.getParameter("projectId");
            String userId = request.getParameter("userId");
            
            if (projectId == null || userId == null) {
                result.put("success", false);
                result.put("role", null);
                result.put("isAdmin", false);
                result.put("message", "필수 파라미터가 누락되었습니다.");
                return result;
            }
            
            // 프로젝트 사용자 조회
            ProjectUserVo projectUserVo = new ProjectUserVo();
            projectUserVo.setProjectId(projectId);
            projectUserVo.setUserId(userId);
            
            ProjectUserVo userRole = projectUserService.selectProjectUser(projectUserVo);
            
            if (userRole != null && userRole.getRole() != null) {
                String role = userRole.getRole();
                result.put("success", true);
                result.put("role", role);
                result.put("isAdmin", "admin".equals(role));
                result.put("message", "역할 조회 성공");
            } else {
                result.put("success", false);
                result.put("role", null);
                result.put("isAdmin", false);
                result.put("message", "프로젝트 멤버가 아닙니다.");
            }
            
        } catch (Exception e) {
            result.put("success", false);
            result.put("role", null);
            result.put("isAdmin", false);
            result.put("message", "역할 확인 중 오류가 발생했습니다: " + e.getMessage());
        }
        
        return result;
    }

    /**
     * 프로젝트 참여 처리 (이메일 링크를 통한 접근)
     * 
     * @param projectId 프로젝트 ID
     * @param userId 사용자 ID (이메일)
     * @return 참여 결과
     * @throws Exception
     */
    @ElService(key = "JoinProject")
    @RequestMapping(value = "JoinProject.do", method = RequestMethod.POST)
    @ElDescription(sub = "프로젝트 참여 처리", desc = "이메일 폼을 통한 프로젝트 참여 처리를 한다.")
    public ModelAndView joinProject(ProjectUserVo projectUserVo) throws Exception {
        
        ModelAndView mav = new ModelAndView();
        String message = "";
        boolean success = false;
        
        try {
            // 1. 파라미터 검증
            String projectId = projectUserVo.getProjectId();
            String userId = projectUserVo.getUserId();
            
            if (projectId == null || projectId.trim().isEmpty()) {
                message = "프로젝트 ID가 필요합니다.";
                mav.setViewName("redirect:/websquare/websquare.html?w2xPath=/InsWebApp/ui/common/errorPage.xml&message=" + java.net.URLEncoder.encode(message, "UTF-8"));
                return mav;
            }
            
            if (userId == null || userId.trim().isEmpty()) {
                message = "사용자 ID가 필요합니다.";
                mav.setViewName("redirect:/websquare/websquare.html?w2xPath=/InsWebApp/ui/common/errorPage.xml&message=" + java.net.URLEncoder.encode(message, "UTF-8"));
                return mav;
            }
            
            // 2. 사용자 존재 여부 및 프로젝트 멤버 여부 체크
            ProjectUserVo userInfo = projectUserService.searchUserByUserId(userId, projectId);
            
            if (userInfo == null) {
                message = "존재하지 않는 사용자입니다.";
                mav.setViewName("redirect:/websquare/websquare.html?w2xPath=/InsWebApp/ui/common/errorPage.xml&message=" + java.net.URLEncoder.encode(message, "UTF-8"));
                return mav;
            }
            
            if (userInfo.getRole() != null) {
                message = "이미 프로젝트에 참여한 멤버입니다.";
                mav.setViewName("redirect:/websquare/websquare.html?w2xPath=/InsWebApp/ui/project/projectSettingMainPage.xml&projectId=" + projectId + "&message=" + java.net.URLEncoder.encode(message, "UTF-8"));
                return mav;
            }
            
            // 3. 프로젝트 멤버로 추가
            ProjectUserVo newMember = new ProjectUserVo();
            newMember.setProjectId(projectId);
            newMember.setUserId(userId);
            newMember.setRole("editor"); // 기본 역할을 editor로 설정
            
            int insertResult = projectUserService.inviteUserToProject(newMember);
            
            if (insertResult > 0) {
                message = "프로젝트에 성공적으로 참여하였습니다.";
                success = true;
                // 성공 시 프로젝트 메인 페이지로 이동
                //mav.setViewName("redirect:/websquare/websquare.html?w2xPath=/InsWebApp/ui/project/projectSettingMainPage.xml&projectId=" + projectId + "&message=" + java.net.URLEncoder.encode(message, "UTF-8"));
                mav.setViewName("redirect:/websquare/websquare.html?w2xPath=/InsWebApp/ui/landingPage.xml");
            } else {
                message = "프로젝트 참여에 실패하였습니다.";
                mav.setViewName("redirect:/websquare/websquare.html?w2xPath=/InsWebApp/ui/common/errorPage.xml&message=" + java.net.URLEncoder.encode(message, "UTF-8"));
            }
            
        } catch (Exception e) {
            message = "프로젝트 참여 처리 중 오류가 발생했습니다: " + e.getMessage();
            mav.setViewName("redirect:/websquare/websquare.html?w2xPath=/InsWebApp/ui/common/errorPage.xml&message=" + java.net.URLEncoder.encode(message, "UTF-8"));
        }
        
        return mav;
    }
   
}
