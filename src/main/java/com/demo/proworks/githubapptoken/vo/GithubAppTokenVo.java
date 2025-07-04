package com.demo.proworks.githubapptoken.vo;

import com.inswave.elfw.annotation.ElDto;
import com.inswave.elfw.annotation.ElDtoField;
import com.inswave.elfw.annotation.ElVoField;
import com.fasterxml.jackson.annotation.JsonFilter;

@JsonFilter("elExcludeFilter")
@ElDto(FldYn = "", logicalName = "깃허브 앱 토큰 저장")
public class GithubAppTokenVo extends com.demo.proworks.cmmn.ProworksCommVO {
    private static final long serialVersionUID = 1L;

    @ElDtoField(logicalName = "github_app_token_id", physicalName = "githubAppTokenId", type = "String", typeKind = "", fldYn = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private String githubAppTokenId;

    @ElDtoField(logicalName = "project_repo_id", physicalName = "projectRepoId", type = "String", typeKind = "", fldYn = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private String projectRepoId;

    @ElDtoField(logicalName = "app_token", physicalName = "appToken", type = "String", typeKind = "", fldYn = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private String appToken;

    @ElDtoField(logicalName = "expired_at", physicalName = "expiredAt", type = "String", typeKind = "", fldYn = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private String expiredAt;

    @ElDtoField(logicalName = "created_at", physicalName = "createdAt", type = "String", typeKind = "", fldYn = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private String createdAt;

    @ElVoField(physicalName = "githubAppTokenId")
    public String getGithubAppTokenId(){
        return githubAppTokenId;
    }

    @ElVoField(physicalName = "githubAppTokenId")
    public void setGithubAppTokenId(String githubAppTokenId){
        this.githubAppTokenId = githubAppTokenId;
    }

    @ElVoField(physicalName = "projectRepoId")
    public String getProjectRepoId(){
        return projectRepoId;
    }

    @ElVoField(physicalName = "projectRepoId")
    public void setProjectRepoId(String projectRepoId){
        this.projectRepoId = projectRepoId;
    }

    @ElVoField(physicalName = "appToken")
    public String getAppToken(){
        return appToken;
    }

    @ElVoField(physicalName = "appToken")
    public void setAppToken(String appToken){
        this.appToken = appToken;
    }

    @ElVoField(physicalName = "expiredAt")
    public String getExpiredAt(){
        return expiredAt;
    }

    @ElVoField(physicalName = "expiredAt")
    public void setExpiredAt(String expiredAt){
        this.expiredAt = expiredAt;
    }

    @ElVoField(physicalName = "createdAt")
    public String getCreatedAt(){
        return createdAt;
    }

    @ElVoField(physicalName = "createdAt")
    public void setCreatedAt(String createdAt){
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "GithubAppTokenVo [githubAppTokenId=" + githubAppTokenId + ",projectRepoId=" + projectRepoId + ",appToken=" + appToken + ",expiredAt=" + expiredAt + ",createdAt=" + createdAt + "]";
    }

    public boolean isFixedLengthVo() {
        return false;
    }

}
