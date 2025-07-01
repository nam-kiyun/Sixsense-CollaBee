package com.demo.proworks.collabee.vo.vo;

import com.inswave.elfw.annotation.ElDto;
import com.inswave.elfw.annotation.ElDtoField;
import com.inswave.elfw.annotation.ElVoField;
import com.fasterxml.jackson.annotation.JsonFilter;

@JsonFilter("elExcludeFilter")
@ElDto(FldYn = "", logicalName = "프로젝트와연결된레포지토리")
public class ProjectRepositoryVo extends com.demo.proworks.cmmn.ProworksCommVO {
    private static final long serialVersionUID = 1L;

    @ElDtoField(logicalName = "project_repo_id", physicalName = "projectRepoId", type = "String", typeKind = "", fldYn = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private String projectRepoId;

    @ElDtoField(logicalName = "project_id", physicalName = "projectId", type = "String", typeKind = "", fldYn = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private String projectId;

    @ElDtoField(logicalName = "github_repository_id", physicalName = "githubRepositoryId", type = "String", typeKind = "", fldYn = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private String githubRepositoryId;

    @ElDtoField(logicalName = "repo_owner", physicalName = "repoOwner", type = "String", typeKind = "", fldYn = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private String repoOwner;

    @ElDtoField(logicalName = "repo_name", physicalName = "repoName", type = "String", typeKind = "", fldYn = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private String repoName;

    @ElDtoField(logicalName = "default_branch", physicalName = "defaultBranch", type = "String", typeKind = "", fldYn = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private String defaultBranch;

    @ElDtoField(logicalName = "github_app_installation_id", physicalName = "githubAppInstallationId", type = "String", typeKind = "", fldYn = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private String githubAppInstallationId;

    @ElVoField(physicalName = "projectRepoId")
    public String getProjectRepoId(){
        return projectRepoId;
    }

    @ElVoField(physicalName = "projectRepoId")
    public void setProjectRepoId(String projectRepoId){
        this.projectRepoId = projectRepoId;
    }

    @ElVoField(physicalName = "projectId")
    public String getProjectId(){
        return projectId;
    }

    @ElVoField(physicalName = "projectId")
    public void setProjectId(String projectId){
        this.projectId = projectId;
    }

    @ElVoField(physicalName = "githubRepositoryId")
    public String getGithubRepositoryId(){
        return githubRepositoryId;
    }

    @ElVoField(physicalName = "githubRepositoryId")
    public void setGithubRepositoryId(String githubRepositoryId){
        this.githubRepositoryId = githubRepositoryId;
    }

    @ElVoField(physicalName = "repoOwner")
    public String getRepoOwner(){
        return repoOwner;
    }

    @ElVoField(physicalName = "repoOwner")
    public void setRepoOwner(String repoOwner){
        this.repoOwner = repoOwner;
    }

    @ElVoField(physicalName = "repoName")
    public String getRepoName(){
        return repoName;
    }

    @ElVoField(physicalName = "repoName")
    public void setRepoName(String repoName){
        this.repoName = repoName;
    }

    @ElVoField(physicalName = "defaultBranch")
    public String getDefaultBranch(){
        return defaultBranch;
    }

    @ElVoField(physicalName = "defaultBranch")
    public void setDefaultBranch(String defaultBranch){
        this.defaultBranch = defaultBranch;
    }

    @ElVoField(physicalName = "githubAppInstallationId")
    public String getGithubAppInstallationId(){
        return githubAppInstallationId;
    }

    @ElVoField(physicalName = "githubAppInstallationId")
    public void setGithubAppInstallationId(String githubAppInstallationId){
        this.githubAppInstallationId = githubAppInstallationId;
    }

    @Override
    public String toString() {
        return "ProjectRepositoryVo [projectRepoId=" + projectRepoId + ",projectId=" + projectId + ",githubRepositoryId=" + githubRepositoryId + ",repoOwner=" + repoOwner + ",repoName=" + repoName + ",defaultBranch=" + defaultBranch + ",githubAppInstallationId=" + githubAppInstallationId + "]";
    }

    public boolean isFixedLengthVo() {
        return false;
    }

}
