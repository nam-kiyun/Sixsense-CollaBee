package com.demo.proworks.githubwebhook.vo;

import com.inswave.elfw.annotation.ElDto;
import com.inswave.elfw.annotation.ElDtoField;
import com.inswave.elfw.annotation.ElVoField;
import com.fasterxml.jackson.annotation.JsonFilter;

@JsonFilter("elExcludeFilter")
@ElDto(FldYn = "", logicalName = "깃허브 웹훅")
public class GithubWebhookVo extends com.demo.proworks.cmmn.ProworksCommVO {
    private static final long serialVersionUID = 1L;

    @ElDtoField(logicalName = "github_webhook_id", physicalName = "githubWebhookId", type = "String", typeKind = "", fldYn = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private String githubWebhookId;

    @ElDtoField(logicalName = "project_repo_id", physicalName = "projectRepoId", type = "String", typeKind = "", fldYn = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private String projectRepoId;

    @ElDtoField(logicalName = "hook_id", physicalName = "hookId", type = "String", typeKind = "", fldYn = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private String hookId;

    @ElDtoField(logicalName = "events", physicalName = "events", type = "String", typeKind = "", fldYn = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private String events;

    @ElDtoField(logicalName = "config_url", physicalName = "configUrl", type = "String", typeKind = "", fldYn = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private String configUrl;

    @ElDtoField(logicalName = "created_at", physicalName = "createdAt", type = "String", typeKind = "", fldYn = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private String createdAt;

    @ElVoField(physicalName = "githubWebhookId")
    public String getGithubWebhookId(){
        return githubWebhookId;
    }

    @ElVoField(physicalName = "githubWebhookId")
    public void setGithubWebhookId(String githubWebhookId){
        this.githubWebhookId = githubWebhookId;
    }

    @ElVoField(physicalName = "projectRepoId")
    public String getProjectRepoId(){
        return projectRepoId;
    }

    @ElVoField(physicalName = "projectRepoId")
    public void setProjectRepoId(String projectRepoId){
        this.projectRepoId = projectRepoId;
    }

    @ElVoField(physicalName = "hookId")
    public String getHookId(){
        return hookId;
    }

    @ElVoField(physicalName = "hookId")
    public void setHookId(String hookId){
        this.hookId = hookId;
    }

    @ElVoField(physicalName = "events")
    public String getEvents(){
        return events;
    }

    @ElVoField(physicalName = "events")
    public void setEvents(String events){
        this.events = events;
    }

    @ElVoField(physicalName = "configUrl")
    public String getConfigUrl(){
        return configUrl;
    }

    @ElVoField(physicalName = "configUrl")
    public void setConfigUrl(String configUrl){
        this.configUrl = configUrl;
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
        return "GithubWebhookVo [githubWebhookId=" + githubWebhookId + ",projectRepoId=" + projectRepoId + ",hookId=" + hookId + ",events=" + events + ",configUrl=" + configUrl + ",createdAt=" + createdAt + "]";
    }

    public boolean isFixedLengthVo() {
        return false;
    }

}
