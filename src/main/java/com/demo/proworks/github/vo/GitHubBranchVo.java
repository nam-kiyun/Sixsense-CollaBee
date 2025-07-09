package com.demo.proworks.github.vo;

import com.inswave.elfw.annotation.ElDto;
import com.inswave.elfw.annotation.ElDtoField;
import com.inswave.elfw.annotation.ElVoField;
import com.fasterxml.jackson.annotation.JsonFilter;

/**
 * GitHub 브랜치 정보 VO
 * test 디렉터리의 브랜치 관리 기능을 기반으로 작성
 */
@JsonFilter("elExcludeFilter")
@ElDto(FldYn = "", logicalName = "GitHub 브랜치 정보")
public class GitHubBranchVo extends com.demo.proworks.cmmn.ProworksCommVO {
    
    private static final long serialVersionUID = 1L;

    @ElDtoField(logicalName = "github_branch_id", physicalName = "githubBranchId", type = "String", typeKind = "", fldYn = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private String githubBranchId;

    @ElDtoField(logicalName = "project_repo_id", physicalName = "projectRepoId", type = "String", typeKind = "", fldYn = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private String projectRepoId;

    @ElDtoField(logicalName = "repository_full_name", physicalName = "repositoryFullName", type = "String", typeKind = "", fldYn = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private String repositoryFullName;

    @ElDtoField(logicalName = "branch_name", physicalName = "branchName", type = "String", typeKind = "", fldYn = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private String branchName;

    @ElDtoField(logicalName = "commit_sha", physicalName = "commitSha", type = "String", typeKind = "", fldYn = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private String commitSha;

    @ElDtoField(logicalName = "commit_message", physicalName = "commitMessage", type = "String", typeKind = "", fldYn = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private String commitMessage;

    @ElDtoField(logicalName = "commit_author", physicalName = "commitAuthor", type = "String", typeKind = "", fldYn = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private String commitAuthor;

    @ElDtoField(logicalName = "commit_date", physicalName = "commitDate", type = "String", typeKind = "", fldYn = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private String commitDate;

    @ElDtoField(logicalName = "is_protected", physicalName = "isProtected", type = "String", typeKind = "", fldYn = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private String isProtected;

    @ElDtoField(logicalName = "is_default", physicalName = "isDefault", type = "String", typeKind = "", fldYn = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private String isDefault;

    @ElDtoField(logicalName = "ahead_by", physicalName = "aheadBy", type = "int", typeKind = "", fldYn = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private int aheadBy;

    @ElDtoField(logicalName = "behind_by", physicalName = "behindBy", type = "int", typeKind = "", fldYn = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private int behindBy;

    @ElDtoField(logicalName = "last_activity", physicalName = "lastActivity", type = "String", typeKind = "", fldYn = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private String lastActivity;

    @ElDtoField(logicalName = "branch_url", physicalName = "branchUrl", type = "String", typeKind = "", fldYn = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private String branchUrl;

    @ElDtoField(logicalName = "compare_url", physicalName = "compareUrl", type = "String", typeKind = "", fldYn = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private String compareUrl;

    public GitHubBranchVo() {}

    @ElVoField(physicalName = "githubBranchId")
    public String getGithubBranchId() {
        return githubBranchId;
    }

    public void setGithubBranchId(String githubBranchId) {
        this.githubBranchId = githubBranchId;
    }

    @ElVoField(physicalName = "projectRepoId")
    public String getProjectRepoId() {
        return projectRepoId;
    }

    public void setProjectRepoId(String projectRepoId) {
        this.projectRepoId = projectRepoId;
    }

    @ElVoField(physicalName = "repositoryFullName")
    public String getRepositoryFullName() {
        return repositoryFullName;
    }

    public void setRepositoryFullName(String repositoryFullName) {
        this.repositoryFullName = repositoryFullName;
    }

    @ElVoField(physicalName = "branchName")
    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    @ElVoField(physicalName = "commitSha")
    public String getCommitSha() {
        return commitSha;
    }

    public void setCommitSha(String commitSha) {
        this.commitSha = commitSha;
    }

    @ElVoField(physicalName = "commitMessage")
    public String getCommitMessage() {
        return commitMessage;
    }

    public void setCommitMessage(String commitMessage) {
        this.commitMessage = commitMessage;
    }

    @ElVoField(physicalName = "commitAuthor")
    public String getCommitAuthor() {
        return commitAuthor;
    }

    public void setCommitAuthor(String commitAuthor) {
        this.commitAuthor = commitAuthor;
    }

    @ElVoField(physicalName = "commitDate")
    public String getCommitDate() {
        return commitDate;
    }

    public void setCommitDate(String commitDate) {
        this.commitDate = commitDate;
    }

    @ElVoField(physicalName = "isProtected")
    public String getIsProtected() {
        return isProtected;
    }

    public void setIsProtected(String isProtected) {
        this.isProtected = isProtected;
    }

    @ElVoField(physicalName = "isDefault")
    public String getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(String isDefault) {
        this.isDefault = isDefault;
    }

    @ElVoField(physicalName = "aheadBy")
    public int getAheadBy() {
        return aheadBy;
    }

    public void setAheadBy(int aheadBy) {
        this.aheadBy = aheadBy;
    }

    @ElVoField(physicalName = "behindBy")
    public int getBehindBy() {
        return behindBy;
    }

    public void setBehindBy(int behindBy) {
        this.behindBy = behindBy;
    }

    @ElVoField(physicalName = "lastActivity")
    public String getLastActivity() {
        return lastActivity;
    }

    public void setLastActivity(String lastActivity) {
        this.lastActivity = lastActivity;
    }

    @ElVoField(physicalName = "branchUrl")
    public String getBranchUrl() {
        return branchUrl;
    }

    public void setBranchUrl(String branchUrl) {
        this.branchUrl = branchUrl;
    }

    @ElVoField(physicalName = "compareUrl")
    public String getCompareUrl() {
        return compareUrl;
    }

    public void setCompareUrl(String compareUrl) {
        this.compareUrl = compareUrl;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("GitHubBranchVo [");
        sb.append("githubBranchId=").append(githubBranchId);
        sb.append(", projectRepoId=").append(projectRepoId);
        sb.append(", repositoryFullName=").append(repositoryFullName);
        sb.append(", branchName=").append(branchName);
        sb.append(", commitSha=").append(commitSha);
        sb.append(", commitMessage=").append(commitMessage);
        sb.append(", commitAuthor=").append(commitAuthor);
        sb.append(", commitDate=").append(commitDate);
        sb.append(", isProtected=").append(isProtected);
        sb.append(", isDefault=").append(isDefault);
        sb.append(", aheadBy=").append(aheadBy);
        sb.append(", behindBy=").append(behindBy);
        sb.append(", lastActivity=").append(lastActivity);
        sb.append(", branchUrl=").append(branchUrl);
        sb.append(", compareUrl=").append(compareUrl);
        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean isFixedLengthVo() {
        return false;
    }
}