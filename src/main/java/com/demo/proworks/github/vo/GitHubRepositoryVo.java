package com.demo.proworks.github.vo;

import com.inswave.elfw.annotation.ElDto;
import com.inswave.elfw.annotation.ElDtoField;
import com.inswave.elfw.annotation.ElVoField;
import com.fasterxml.jackson.annotation.JsonFilter;

/**
 * GitHub 레포지토리 정보 VO
 * test 디렉터리의 GitHub API 응답을 기반으로 작성
 */
@JsonFilter("elExcludeFilter")
@ElDto(FldYn = "", logicalName = "GitHub 레포지토리 정보")
public class GitHubRepositoryVo extends com.demo.proworks.cmmn.ProworksCommVO {
    
    private static final long serialVersionUID = 1L;

    @ElDtoField(logicalName = "github_repo_id", physicalName = "githubRepoId", type = "String", typeKind = "", fldYn = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private String githubRepoId;

    @ElDtoField(logicalName = "repo_name", physicalName = "repoName", type = "String", typeKind = "", fldYn = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private String repoName;

    @ElDtoField(logicalName = "repo_full_name", physicalName = "repoFullName", type = "String", typeKind = "", fldYn = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private String repoFullName;

    @ElDtoField(logicalName = "repo_owner", physicalName = "repoOwner", type = "String", typeKind = "", fldYn = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private String repoOwner;

    @ElDtoField(logicalName = "repo_description", physicalName = "repoDescription", type = "String", typeKind = "", fldYn = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private String repoDescription;

    @ElDtoField(logicalName = "repo_url", physicalName = "repoUrl", type = "String", typeKind = "", fldYn = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private String repoUrl;

    @ElDtoField(logicalName = "clone_url", physicalName = "cloneUrl", type = "String", typeKind = "", fldYn = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private String cloneUrl;

    @ElDtoField(logicalName = "ssh_url", physicalName = "sshUrl", type = "String", typeKind = "", fldYn = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private String sshUrl;

    @ElDtoField(logicalName = "default_branch", physicalName = "defaultBranch", type = "String", typeKind = "", fldYn = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private String defaultBranch;

    @ElDtoField(logicalName = "is_private", physicalName = "isPrivate", type = "String", typeKind = "", fldYn = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private String isPrivate;

    @ElDtoField(logicalName = "is_fork", physicalName = "isFork", type = "String", typeKind = "", fldYn = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private String isFork;

    @ElDtoField(logicalName = "language", physicalName = "language", type = "String", typeKind = "", fldYn = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private String language;

    @ElDtoField(logicalName = "stars_count", physicalName = "starsCount", type = "int", typeKind = "", fldYn = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private int starsCount;

    @ElDtoField(logicalName = "forks_count", physicalName = "forksCount", type = "int", typeKind = "", fldYn = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private int forksCount;

    @ElDtoField(logicalName = "created_at", physicalName = "createdAt", type = "String", typeKind = "", fldYn = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private String createdAt;

    @ElDtoField(logicalName = "updated_at", physicalName = "updatedAt", type = "String", typeKind = "", fldYn = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private String updatedAt;

    @ElDtoField(logicalName = "pushed_at", physicalName = "pushedAt", type = "String", typeKind = "", fldYn = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private String pushedAt;

    public GitHubRepositoryVo() {}

    @ElVoField(physicalName = "githubRepoId")
    public String getGithubRepoId() {
        return githubRepoId;
    }

    public void setGithubRepoId(String githubRepoId) {
        this.githubRepoId = githubRepoId;
    }

    @ElVoField(physicalName = "repoName")
    public String getRepoName() {
        return repoName;
    }

    public void setRepoName(String repoName) {
        this.repoName = repoName;
    }

    @ElVoField(physicalName = "repoFullName")
    public String getRepoFullName() {
        return repoFullName;
    }

    public void setRepoFullName(String repoFullName) {
        this.repoFullName = repoFullName;
    }

    @ElVoField(physicalName = "repoOwner")
    public String getRepoOwner() {
        return repoOwner;
    }

    public void setRepoOwner(String repoOwner) {
        this.repoOwner = repoOwner;
    }

    @ElVoField(physicalName = "repoDescription")
    public String getRepoDescription() {
        return repoDescription;
    }

    public void setRepoDescription(String repoDescription) {
        this.repoDescription = repoDescription;
    }

    @ElVoField(physicalName = "repoUrl")
    public String getRepoUrl() {
        return repoUrl;
    }

    public void setRepoUrl(String repoUrl) {
        this.repoUrl = repoUrl;
    }

    @ElVoField(physicalName = "cloneUrl")
    public String getCloneUrl() {
        return cloneUrl;
    }

    public void setCloneUrl(String cloneUrl) {
        this.cloneUrl = cloneUrl;
    }

    @ElVoField(physicalName = "sshUrl")
    public String getSshUrl() {
        return sshUrl;
    }

    public void setSshUrl(String sshUrl) {
        this.sshUrl = sshUrl;
    }

    @ElVoField(physicalName = "defaultBranch")
    public String getDefaultBranch() {
        return defaultBranch;
    }

    public void setDefaultBranch(String defaultBranch) {
        this.defaultBranch = defaultBranch;
    }

    @ElVoField(physicalName = "isPrivate")
    public String getIsPrivate() {
        return isPrivate;
    }

    public void setIsPrivate(String isPrivate) {
        this.isPrivate = isPrivate;
    }

    @ElVoField(physicalName = "isFork")
    public String getIsFork() {
        return isFork;
    }

    public void setIsFork(String isFork) {
        this.isFork = isFork;
    }

    @ElVoField(physicalName = "language")
    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    @ElVoField(physicalName = "starsCount")
    public int getStarsCount() {
        return starsCount;
    }

    public void setStarsCount(int starsCount) {
        this.starsCount = starsCount;
    }

    @ElVoField(physicalName = "forksCount")
    public int getForksCount() {
        return forksCount;
    }

    public void setForksCount(int forksCount) {
        this.forksCount = forksCount;
    }

    @ElVoField(physicalName = "createdAt")
    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    @ElVoField(physicalName = "updatedAt")
    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    @ElVoField(physicalName = "pushedAt")
    public String getPushedAt() {
        return pushedAt;
    }

    public void setPushedAt(String pushedAt) {
        this.pushedAt = pushedAt;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("GitHubRepositoryVo [");
        sb.append("githubRepoId=").append(githubRepoId);
        sb.append(", repoName=").append(repoName);
        sb.append(", repoFullName=").append(repoFullName);
        sb.append(", repoOwner=").append(repoOwner);
        sb.append(", repoDescription=").append(repoDescription);
        sb.append(", repoUrl=").append(repoUrl);
        sb.append(", cloneUrl=").append(cloneUrl);
        sb.append(", sshUrl=").append(sshUrl);
        sb.append(", defaultBranch=").append(defaultBranch);
        sb.append(", isPrivate=").append(isPrivate);
        sb.append(", isFork=").append(isFork);
        sb.append(", language=").append(language);
        sb.append(", starsCount=").append(starsCount);
        sb.append(", forksCount=").append(forksCount);
        sb.append(", createdAt=").append(createdAt);
        sb.append(", updatedAt=").append(updatedAt);
        sb.append(", pushedAt=").append(pushedAt);
        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean isFixedLengthVo() {
        return false;
    }
}