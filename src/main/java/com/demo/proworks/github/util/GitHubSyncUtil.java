package com.demo.proworks.github.util;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.stereotype.Component;

/**
 * GitHub 동기화 유틸리티
 */
@Component
public class GitHubSyncUtil {
    
    
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * GitHub 사용자 정보를 ProWorks 사용자 형식으로 변환
     * @param githubUser GitHub 사용자 정보
     * @param localUserId 로컬 사용자 ID
     * @return ProWorks 사용자 정보
     */
    public Map<String, Object> convertGitHubUserToLocal(Map<String, Object> githubUser, String localUserId) {
        Map<String, Object> localUser = new HashMap<>();
        
        localUser.put("github_user_id", githubUser.get("id").toString());
        localUser.put("local_user_id", localUserId);
        localUser.put("github_username", githubUser.get("login"));
        localUser.put("github_email", githubUser.get("email"));
        localUser.put("display_name", githubUser.get("name"));
        localUser.put("avatar_url", githubUser.get("avatar_url"));
        localUser.put("github_profile_url", githubUser.get("html_url"));
        localUser.put("connected_at", dateFormat.format(new Date()));
        localUser.put("is_active", "Y");
        
        return localUser;
    }
    
    /**
     * GitHub 레포지토리 정보를 ProWorks 형식으로 변환
     * @param githubRepo GitHub 레포지토리 정보
     * @param projectId 연결할 프로젝트 ID
     * @return ProWorks 레포지토리 정보
     */
    public Map<String, Object> convertGitHubRepoToLocal(Map<String, Object> githubRepo, String projectId) {
        Map<String, Object> localRepo = new HashMap<>();
        
        localRepo.put("github_repo_id", githubRepo.get("id").toString());
        localRepo.put("project_id", projectId);
        localRepo.put("repo_name", githubRepo.get("name"));
        localRepo.put("repo_full_name", githubRepo.get("full_name"));
        localRepo.put("repo_owner", ((Map<String, Object>) githubRepo.get("owner")).get("login"));
        localRepo.put("repo_description", githubRepo.get("description"));
        localRepo.put("repo_url", githubRepo.get("html_url"));
        localRepo.put("clone_url", githubRepo.get("clone_url"));
        localRepo.put("ssh_url", githubRepo.get("ssh_url"));
        localRepo.put("default_branch", githubRepo.get("default_branch"));
        localRepo.put("is_private", githubRepo.get("private").toString());
        localRepo.put("is_fork", githubRepo.get("fork").toString());
        localRepo.put("language", githubRepo.get("language"));
        localRepo.put("stars_count", githubRepo.get("stargazers_count"));
        localRepo.put("forks_count", githubRepo.get("forks_count"));
        localRepo.put("created_at", githubRepo.get("created_at"));
        localRepo.put("updated_at", githubRepo.get("updated_at"));
        localRepo.put("pushed_at", githubRepo.get("pushed_at"));
        
        return localRepo;
    }
    
    /**
     * GitHub 브랜치 정보를 ProWorks 형식으로 변환
     * @param githubBranch GitHub 브랜치 정보
     * @param projectRepoId 프로젝트 레포지토리 ID
     * @param repositoryFullName 레포지토리 전체 이름
     * @return ProWorks 브랜치 정보
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> convertGitHubBranchToLocal(Map<String, Object> githubBranch, String projectRepoId, String repositoryFullName) {
        Map<String, Object> localBranch = new HashMap<>();
        
        localBranch.put("project_repo_id", projectRepoId);
        localBranch.put("repository_full_name", repositoryFullName);
        localBranch.put("branch_name", githubBranch.get("name"));
        
        // 커밋 정보
        Map<String, Object> commit = (Map<String, Object>) githubBranch.get("commit");
        if (commit != null) {
            localBranch.put("commit_sha", commit.get("sha"));
            
            Map<String, Object> commitData = (Map<String, Object>) commit.get("commit");
            if (commitData != null) {
                localBranch.put("commit_message", commitData.get("message"));
                
                Map<String, Object> author = (Map<String, Object>) commitData.get("author");
                if (author != null) {
                    localBranch.put("commit_author", author.get("name"));
                    localBranch.put("commit_date", author.get("date"));
                }
            }
        }
        
        // 보호 여부 (기본값: N)
        localBranch.put("is_protected", githubBranch.get("protected") != null ? 
                                       githubBranch.get("protected").toString() : "N");
        
        // 기본 브랜치 여부는 별도로 설정 필요
        localBranch.put("is_default", "N");
        
        localBranch.put("last_activity", dateFormat.format(new Date()));
        localBranch.put("branch_url", String.format("https://github.com/%s/tree/%s", 
                                                   repositoryFullName, githubBranch.get("name")));
        
        return localBranch;
    }
    
    /**
     * GitHub 이슈를 ProWorks Task로 변환
     * @param githubIssue GitHub 이슈 정보
     * @param projectId 프로젝트 ID
     * @param userId 사용자 ID
     * @return ProWorks Task 정보
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> convertGitHubIssueToTask(Map<String, Object> githubIssue, String projectId, String userId) {
        Map<String, Object> task = new HashMap<>();
        
        task.put("project_id", projectId);
        task.put("task_title", githubIssue.get("title"));
        task.put("task_description", githubIssue.get("body"));
        task.put("task_status", convertIssueStateToTaskStatus((String) githubIssue.get("state")));
        task.put("assigned_user_id", userId);
        task.put("github_issue_number", githubIssue.get("number"));
        task.put("github_url", githubIssue.get("html_url"));
        task.put("created_at", githubIssue.get("created_at"));
        task.put("updated_at", githubIssue.get("updated_at"));
        
        // 라벨을 태그로 변환
        Object labelsObj = githubIssue.get("labels");
        if (labelsObj instanceof List) {
            List<Map<String, Object>> labels = (List<Map<String, Object>>) labelsObj;
            List<String> labelNames = new ArrayList<>();
            for (Map<String, Object> label : labels) {
                labelNames.add((String) label.get("name"));
            }
            task.put("tags", String.join(",", labelNames));
        }
        
        // 우선순위 설정 (라벨 기반)
        task.put("priority", extractPriorityFromLabels(githubIssue));
        
        return task;
    }
    
    /**
     * GitHub Pull Request를 ProWorks Task로 변환
     * @param githubPR GitHub PR 정보
     * @param projectId 프로젝트 ID
     * @param userId 사용자 ID
     * @return ProWorks Task 정보
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> convertGitHubPRToTask(Map<String, Object> githubPR, String projectId, String userId) {
        Map<String, Object> task = new HashMap<>();
        
        task.put("project_id", projectId);
        task.put("task_title", "PR: " + githubPR.get("title"));
        task.put("task_description", githubPR.get("body"));
        task.put("task_status", convertPRStateToTaskStatus((String) githubPR.get("state"), (Boolean) githubPR.get("merged")));
        task.put("assigned_user_id", userId);
        task.put("github_pr_number", githubPR.get("number"));
        task.put("github_url", githubPR.get("html_url"));
        task.put("created_at", githubPR.get("created_at"));
        task.put("updated_at", githubPR.get("updated_at"));
        
        // 브랜치 정보
        Map<String, Object> head = (Map<String, Object>) githubPR.get("head");
        Map<String, Object> base = (Map<String, Object>) githubPR.get("base");
        if (head != null && base != null) {
            task.put("source_branch", head.get("ref"));
            task.put("target_branch", base.get("ref"));
        }
        
        task.put("priority", "MEDIUM"); // PR은 기본적으로 중간 우선순위
        
        return task;
    }
    
    /**
     * GitHub 이슈 상태를 ProWorks Task 상태로 변환
     * @param issueState GitHub 이슈 상태
     * @return ProWorks Task 상태
     */
    private String convertIssueStateToTaskStatus(String issueState) {
        switch (issueState.toLowerCase()) {
            case "open":
                return "TODO";
            case "closed":
                return "DONE";
            default:
                return "TODO";
        }
    }
    
    /**
     * GitHub PR 상태를 ProWorks Task 상태로 변환
     * @param prState GitHub PR 상태
     * @param merged 머지 여부
     * @return ProWorks Task 상태
     */
    private String convertPRStateToTaskStatus(String prState, Boolean merged) {
        if ("closed".equals(prState)) {
            return merged != null && merged ? "DONE" : "CANCELLED";
        } else if ("open".equals(prState)) {
            return "IN_PROGRESS";
        } else {
            return "TODO";
        }
    }
    
    /**
     * GitHub 이슈 라벨에서 우선순위 추출
     * @param githubIssue GitHub 이슈 정보
     * @return 우선순위
     */
    @SuppressWarnings("unchecked")
    private String extractPriorityFromLabels(Map<String, Object> githubIssue) {
        Object labelsObj = githubIssue.get("labels");
        if (labelsObj instanceof List) {
            List<Map<String, Object>> labels = (List<Map<String, Object>>) labelsObj;
            for (Map<String, Object> label : labels) {
                String labelName = ((String) label.get("name")).toLowerCase();
                if (labelName.contains("high") || labelName.contains("urgent") || labelName.contains("critical")) {
                    return "HIGH";
                } else if (labelName.contains("low") || labelName.contains("minor")) {
                    return "LOW";
                }
            }
        }
        return "MEDIUM"; // 기본값
    }
    
    /**
     * ProWorks Task를 GitHub 이슈 형식으로 변환
     * @param task ProWorks Task 정보
     * @return GitHub 이슈 생성 정보
     */
    public Map<String, Object> convertTaskToGitHubIssue(Map<String, Object> task) {
        Map<String, Object> issue = new HashMap<>();
        
        issue.put("title", task.get("task_title"));
        issue.put("body", task.get("task_description"));
        
        // 라벨 설정
        List<String> labels = new ArrayList<>();
        String priority = (String) task.get("priority");
        if (priority != null) {
            switch (priority.toUpperCase()) {
                case "HIGH":
                    labels.add("high priority");
                    break;
                case "LOW":
                    labels.add("low priority");
                    break;
                default:
                    labels.add("medium priority");
            }
        }
        
        String tags = (String) task.get("tags");
        if (tags != null && !tags.isEmpty()) {
            String[] tagArray = tags.split(",");
            for (String tag : tagArray) {
                labels.add(tag.trim());
            }
        }
        
        issue.put("labels", labels);
        
        return issue;
    }
    
    /**
     * 동기화 로그 생성
     * @param syncType 동기화 타입 (ISSUE, PR, COMMIT, WEBHOOK)
     * @param syncAction 동기화 액션 (CREATE, UPDATE, DELETE, SYNC)
     * @param githubEntityId GitHub 엔티티 ID
     * @param localEntityId 로컬 엔티티 ID
     * @param status 동기화 상태 (SUCCESS, FAILED, PENDING)
     * @param errorMessage 오류 메시지 (선택사항)
     * @return 동기화 로그 정보
     */
    public Map<String, Object> createSyncLog(String syncType, String syncAction, String githubEntityId, 
                                           String localEntityId, String status, String errorMessage) {
        Map<String, Object> syncLog = new HashMap<>();
        
        syncLog.put("sync_type", syncType);
        syncLog.put("sync_action", syncAction);
        syncLog.put("github_entity_id", githubEntityId);
        syncLog.put("local_entity_id", localEntityId);
        syncLog.put("sync_status", status);
        syncLog.put("error_message", errorMessage);
        syncLog.put("synced_at", dateFormat.format(new Date()));
        
        return syncLog;
    }
    
    /**
     * 브랜치 활동 로그 생성
     * @param userId 사용자 ID
     * @param repoId 레포지토리 ID
     * @param branchName 브랜치 이름
     * @param action 액션 (create, delete, push, merge)
     * @param sourceBranch 소스 브랜치 (선택사항)
     * @return 브랜치 활동 로그
     */
    public Map<String, Object> createBranchActivityLog(String userId, String repoId, String branchName, 
                                                      String action, String sourceBranch) {
        Map<String, Object> activityLog = new HashMap<>();
        
        activityLog.put("user_id", userId);
        activityLog.put("repo_id", repoId);
        activityLog.put("branch_name", branchName);
        activityLog.put("action", action);
        activityLog.put("source_branch", sourceBranch);
        activityLog.put("action_time", dateFormat.format(new Date()));
        
        return activityLog;
    }
    
    /**
     * GitHub 웹훅 이벤트를 ProWorks 로그 형식으로 변환
     * @param eventType 이벤트 타입
     * @param eventInfo 이벤트 정보
     * @param projectId 프로젝트 ID
     * @param userId 사용자 ID
     * @return ProWorks 로그 정보
     */
    public Map<String, Object> convertWebhookEventToLog(String eventType, Map<String, Object> eventInfo, 
                                                       String projectId, String userId) {
        Map<String, Object> logEntry = new HashMap<>();
        
        logEntry.put("project_id", projectId);
        logEntry.put("user_id", userId);
        logEntry.put("log_type", "GITHUB_" + eventType.toUpperCase());
        logEntry.put("action", eventInfo.get("action"));
        logEntry.put("target_type", getTargetTypeFromEvent(eventType));
        logEntry.put("target_name", getTargetNameFromEvent(eventType, eventInfo));
        logEntry.put("description", generateLogDescription(eventType, eventInfo));
        logEntry.put("github_event_data", eventInfo);
        logEntry.put("created_at", dateFormat.format(new Date()));
        
        return logEntry;
    }
    
    /**
     * 이벤트 타입에서 대상 타입 추출
     */
    private String getTargetTypeFromEvent(String eventType) {
        switch (eventType.toLowerCase()) {
            case "push":
                return "BRANCH";
            case "pull_request":
                return "PULL_REQUEST";
            case "issues":
                return "ISSUE";
            case "create":
            case "delete":
                return "BRANCH";
            default:
                return "REPOSITORY";
        }
    }
    
    /**
     * 이벤트 정보에서 대상 이름 추출
     */
    private String getTargetNameFromEvent(String eventType, Map<String, Object> eventInfo) {
        switch (eventType.toLowerCase()) {
            case "push":
                return (String) eventInfo.get("branch_name");
            case "pull_request":
                return "PR #" + eventInfo.get("pr_number");
            case "issues":
                return "Issue #" + eventInfo.get("issue_number");
            case "create":
            case "delete":
                return (String) eventInfo.get("ref");
            default:
                return (String) eventInfo.get("repository_name");
        }
    }
    
    /**
     * 이벤트 정보에서 로그 설명 생성
     */
    private String generateLogDescription(String eventType, Map<String, Object> eventInfo) {
        String senderLogin = (String) eventInfo.get("sender_login");
        String action = (String) eventInfo.get("action");
        String repositoryName = (String) eventInfo.get("repository_name");
        
        switch (eventType.toLowerCase()) {
            case "push":
                int commitsCount = eventInfo.get("commits_count") != null ? 
                                 (Integer) eventInfo.get("commits_count") : 0;
                return String.format("%s pushed %d commit(s) to %s/%s", 
                                   senderLogin, commitsCount, repositoryName, eventInfo.get("branch_name"));
            case "pull_request":
                return String.format("%s %s pull request #%s in %s", 
                                   senderLogin, action, eventInfo.get("pr_number"), repositoryName);
            case "issues":
                return String.format("%s %s issue #%s in %s", 
                                   senderLogin, action, eventInfo.get("issue_number"), repositoryName);
            case "create":
                return String.format("%s created %s %s in %s", 
                                   senderLogin, eventInfo.get("ref_type"), eventInfo.get("ref"), repositoryName);
            case "delete":
                return String.format("%s deleted %s %s in %s", 
                                   senderLogin, eventInfo.get("ref_type"), eventInfo.get("ref"), repositoryName);
            default:
                return String.format("%s performed %s action in %s", senderLogin, eventType, repositoryName);
        }
    }
}