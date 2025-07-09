package com.demo.proworks.repobranch.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.demo.proworks.repobranch.util.GitHubBranchManager;
import com.demo.proworks.projectrepo.service.ProjectRepositoryService;
import com.demo.proworks.projectrepo.vo.ProjectRepositoryVo;
import com.inswave.elfw.annotation.ElDescription;
import com.inswave.elfw.annotation.ElService;
import com.inswave.elfw.annotation.ElValidator;

/**
 * @subject     : GitHub Repository Branch 관리를 담당하는 Controller
 * @description : GitHub API를 통한 브랜치 생성, 삭제, 조회 기능 제공
 * @author      : 남기윤
 * @since       : 2025/07/07
 * @modification
 * ===========================================================
 * DATE              AUTHOR             DESC
 * ===========================================================
 * 2025/07/07              남기윤             최초 생성
 * 
 */
@Controller
public class GitHubBranchController {

    @Autowired
    private GitHubBranchManager gitHubBranchManager;
    
    @Autowired
    private ProjectRepositoryService projectRepositoryService;

    /**
     * 레포지토리의 브랜치 목록을 조회한다.
     * 
     * @param request HttpServletRequest
     * @param projectRepoId 프로젝트 레포지토리 ID
     * @param userAccessToken 사용자 액세스 토큰
     * @return ResponseEntity<Map<String, Object>>
     */
    @ElService(key = "SvcGHBRANCHLIST01")
    @RequestMapping(value = "/github/branches/list")
    @ElDescription(sub = "브랜치 목록 조회", desc = "레포지토리의 브랜치 목록을 조회합니다.")
    public @ResponseBody ResponseEntity<Map<String, Object>> getBranches(HttpServletRequest request,
            @RequestParam("projectRepoId") String projectRepoId,
            @RequestParam("userAccessToken") String userAccessToken) {
        
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 프로젝트 레포지토리 정보 조회
            ProjectRepositoryVo repoSearchVo = new ProjectRepositoryVo();
            repoSearchVo.setProjectRepoId(projectRepoId);
            ProjectRepositoryVo repoInfo = projectRepositoryService.selectProjectRepository(repoSearchVo);
            
            if (repoInfo == null) {
                result.put("success", false);
                result.put("message", "프로젝트 레포지토리 정보를 찾을 수 없습니다.");
                return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
            }
            
            String repoFullName = repoInfo.getRepoOwner() + "/" + repoInfo.getRepoName();
            
            // 브랜치 목록 조회
            List<Map<String, Object>> branches = gitHubBranchManager.getBranches(repoFullName, userAccessToken);
            
            result.put("success", true);
            result.put("message", "브랜치 목록을 성공적으로 조회했습니다.");
            result.put("branches", branches);
            result.put("repository", repoFullName);
            result.put("default_branch", repoInfo.getDefaultBranch());
            
            return new ResponseEntity<>(result, HttpStatus.OK);
            
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "브랜치 목록 조회에 실패했습니다: " + e.getMessage());
            result.put("error", e.getMessage());
            
            return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 특정 브랜치의 상세 정보를 조회한다.
     * 
     * @param request HttpServletRequest
     * @param projectRepoId 프로젝트 레포지토리 ID
     * @param branchName 브랜치 이름
     * @param userAccessToken 사용자 액세스 토큰
     * @return ResponseEntity<Map<String, Object>>
     */
    @ElService(key = "SvcGHBRANCHDETAIL01")
    @RequestMapping(value = "/github/branches/detail")
    @ElDescription(sub = "브랜치 상세 조회", desc = "특정 브랜치의 상세 정보를 조회합니다.")
    public @ResponseBody ResponseEntity<Map<String, Object>> getBranchDetails(HttpServletRequest request,
            @RequestParam("projectRepoId") String projectRepoId,
            @RequestParam("branchName") String branchName,
            @RequestParam("userAccessToken") String userAccessToken) {
        
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 프로젝트 레포지토리 정보 조회
            ProjectRepositoryVo repoSearchVo = new ProjectRepositoryVo();
            repoSearchVo.setProjectRepoId(projectRepoId);
            ProjectRepositoryVo repoInfo = projectRepositoryService.selectProjectRepository(repoSearchVo);
            
            if (repoInfo == null) {
                result.put("success", false);
                result.put("message", "프로젝트 레포지토리 정보를 찾을 수 없습니다.");
                return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
            }
            
            String repoFullName = repoInfo.getRepoOwner() + "/" + repoInfo.getRepoName();
            
            // 브랜치 상세 정보 조회
            Map<String, Object> branchDetails = gitHubBranchManager.getBranchDetails(repoFullName, branchName, userAccessToken);
            
            result.put("success", true);
            result.put("message", "브랜치 상세 정보를 성공적으로 조회했습니다.");
            result.put("branch", branchDetails);
            result.put("repository", repoFullName);
            
            return new ResponseEntity<>(result, HttpStatus.OK);
            
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "브랜치 상세 정보 조회에 실패했습니다: " + e.getMessage());
            result.put("error", e.getMessage());
            
            return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 새로운 브랜치를 생성한다.
     * 
     * @param request HttpServletRequest
     * @param projectRepoId 프로젝트 레포지토리 ID
     * @param branchName 새 브랜치 이름
     * @param sourceBranch 소스 브랜치 이름
     * @param userAccessToken 사용자 액세스 토큰
     * @return ResponseEntity<Map<String, Object>>
     */
    @ElService(key = "SvcGHBRANCHCREATE01")
    @RequestMapping(value = "/github/branches/create")
    @ElDescription(sub = "브랜치 생성", desc = "새로운 브랜치를 생성합니다.")
    public @ResponseBody ResponseEntity<Map<String, Object>> createBranch(HttpServletRequest request,
            @RequestParam("projectRepoId") String projectRepoId,
            @RequestParam("branchName") String branchName,
            @RequestParam("sourceBranch") String sourceBranch,
            @RequestParam("userAccessToken") String userAccessToken) {
        
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 프로젝트 레포지토리 정보 조회
            ProjectRepositoryVo repoSearchVo = new ProjectRepositoryVo();
            repoSearchVo.setProjectRepoId(projectRepoId);
            ProjectRepositoryVo repoInfo = projectRepositoryService.selectProjectRepository(repoSearchVo);
            
            if (repoInfo == null) {
                result.put("success", false);
                result.put("message", "프로젝트 레포지토리 정보를 찾을 수 없습니다.");
                return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
            }
            
            String repoFullName = repoInfo.getRepoOwner() + "/" + repoInfo.getRepoName();
            
            // 기본 브랜치 삭제 방지
            if (branchName.equals(repoInfo.getDefaultBranch())) {
                result.put("success", false);
                result.put("message", "기본 브랜치와 같은 이름의 브랜치는 생성할 수 없습니다.");
                return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
            }
            
            // 브랜치 생성
            Map<String, Object> createdBranch = gitHubBranchManager.createBranchWithAppToken(
                repoFullName, branchName, sourceBranch, userAccessToken, projectRepoId);
            
            result.put("success", true);
            result.put("message", "브랜치가 성공적으로 생성되었습니다.");
            result.put("branch", createdBranch);
            result.put("repository", repoFullName);
            
            return new ResponseEntity<>(result, HttpStatus.OK);
            
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "브랜치 생성에 실패했습니다: " + e.getMessage());
            result.put("error", e.getMessage());
            
            return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 브랜치를 삭제한다.
     * 
     * @param request HttpServletRequest
     * @param projectRepoId 프로젝트 레포지토리 ID
     * @param branchName 삭제할 브랜치 이름
     * @param userAccessToken 사용자 액세스 토큰
     * @return ResponseEntity<Map<String, Object>>
     */
    @ElService(key = "SvcGHBRANCHDELETE01")
    @RequestMapping(value = "/github/branches/delete")
    @ElDescription(sub = "브랜치 삭제", desc = "기존 브랜치를 삭제합니다.")
    public @ResponseBody ResponseEntity<Map<String, Object>> deleteBranch(HttpServletRequest request,
            @RequestParam("projectRepoId") String projectRepoId,
            @RequestParam("branchName") String branchName,
            @RequestParam("userAccessToken") String userAccessToken) {
        
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 프로젝트 레포지토리 정보 조회
            ProjectRepositoryVo repoSearchVo = new ProjectRepositoryVo();
            repoSearchVo.setProjectRepoId(projectRepoId);
            ProjectRepositoryVo repoInfo = projectRepositoryService.selectProjectRepository(repoSearchVo);
            
            if (repoInfo == null) {
                result.put("success", false);
                result.put("message", "프로젝트 레포지토리 정보를 찾을 수 없습니다.");
                return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
            }
            
            String repoFullName = repoInfo.getRepoOwner() + "/" + repoInfo.getRepoName();
            
            // 기본 브랜치 삭제 방지
            if (branchName.equals(repoInfo.getDefaultBranch())) {
                result.put("success", false);
                result.put("message", "기본 브랜치는 삭제할 수 없습니다.");
                return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
            }
            
            // 브랜치 삭제
            boolean deleted = gitHubBranchManager.deleteBranchWithAppToken(
                repoFullName, branchName, userAccessToken, projectRepoId);
            
            if (deleted) {
                result.put("success", true);
                result.put("message", "브랜치가 성공적으로 삭제되었습니다.");
                result.put("deleted_branch", branchName);
                result.put("repository", repoFullName);
            } else {
                result.put("success", false);
                result.put("message", "브랜치 삭제에 실패했습니다.");
            }
            
            return new ResponseEntity<>(result, HttpStatus.OK);
            
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "브랜치 삭제에 실패했습니다: " + e.getMessage());
            result.put("error", e.getMessage());
            
            return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 브랜치 관리 서비스 상태를 확인한다.
     * 
     * @param request HttpServletRequest
     * @return ResponseEntity<Map<String, Object>>
     */
    @ElService(key = "SvcGHBRANCHSTATUS01")
    @RequestMapping(value = "/github/branches/status")
    @ElDescription(sub = "브랜치 서비스 상태", desc = "브랜치 관리 서비스의 상태를 확인합니다.")
    public @ResponseBody ResponseEntity<Map<String, Object>> getServiceStatus(HttpServletRequest request) {
        
        Map<String, Object> result = new HashMap<>();
        
        try {
            result.put("status", "ok");
            result.put("timestamp", java.time.LocalDateTime.now().toString());
            result.put("service", "GitHub Branch Management Service");
            result.put("version", "1.0.0");
            result.put("features", java.util.Arrays.asList(
                "브랜치 목록 조회",
                "브랜치 상세 정보 조회", 
                "브랜치 생성",
                "브랜치 삭제",
                "Installation Token 지원"
            ));
            
            return new ResponseEntity<>(result, HttpStatus.OK);
            
        } catch (Exception e) {
            result.put("status", "error");
            result.put("message", e.getMessage());
            
            return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
