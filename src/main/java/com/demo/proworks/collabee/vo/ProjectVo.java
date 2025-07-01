package com.demo.proworks.collabee.vo;

import com.inswave.elfw.annotation.ElDto;
import com.inswave.elfw.annotation.ElDtoField;
import com.inswave.elfw.annotation.ElVoField;
import com.fasterxml.jackson.annotation.JsonFilter;

@JsonFilter("elExcludeFilter")
@ElDto(FldYn = "", logicalName = "프로젝트")
public class ProjectVo extends com.demo.proworks.cmmn.ProworksCommVO {
    private static final long serialVersionUID = 1L;

    @ElDtoField(logicalName = "project_id", physicalName = "projectId", type = "String", typeKind = "", fldYn = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private String projectId;

    @ElDtoField(logicalName = "project_name", physicalName = "projectName", type = "String", typeKind = "", fldYn = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private String projectName;

    @ElDtoField(logicalName = "description", physicalName = "description", type = "String", typeKind = "", fldYn = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private String description;

    @ElDtoField(logicalName = "project_image_url", physicalName = "projectImageUrl", type = "String", typeKind = "", fldYn = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private String projectImageUrl;

    @ElDtoField(logicalName = "created_at", physicalName = "createdAt", type = "String", typeKind = "", fldYn = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private String createdAt;

    @ElDtoField(logicalName = "email_send_time", physicalName = "emailSendTime", type = "String", typeKind = "", fldYn = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private String emailSendTime;

    @ElDtoField(logicalName = "user_id", physicalName = "userId", type = "String", typeKind = "", fldYn = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private String userId;

    @ElVoField(physicalName = "projectId")
    public String getProjectId(){
        return projectId;
    }

    @ElVoField(physicalName = "projectId")
    public void setProjectId(String projectId){
        this.projectId = projectId;
    }

    @ElVoField(physicalName = "projectName")
    public String getProjectName(){
        return projectName;
    }

    @ElVoField(physicalName = "projectName")
    public void setProjectName(String projectName){
        this.projectName = projectName;
    }

    @ElVoField(physicalName = "description")
    public String getDescription(){
        return description;
    }

    @ElVoField(physicalName = "description")
    public void setDescription(String description){
        this.description = description;
    }

    @ElVoField(physicalName = "projectImageUrl")
    public String getProjectImageUrl(){
        return projectImageUrl;
    }

    @ElVoField(physicalName = "projectImageUrl")
    public void setProjectImageUrl(String projectImageUrl){
        this.projectImageUrl = projectImageUrl;
    }

    @ElVoField(physicalName = "createdAt")
    public String getCreatedAt(){
        return createdAt;
    }

    @ElVoField(physicalName = "createdAt")
    public void setCreatedAt(String createdAt){
        this.createdAt = createdAt;
    }

    @ElVoField(physicalName = "emailSendTime")
    public String getEmailSendTime(){
        return emailSendTime;
    }

    @ElVoField(physicalName = "emailSendTime")
    public void setEmailSendTime(String emailSendTime){
        this.emailSendTime = emailSendTime;
    }

    @ElVoField(physicalName = "userId")
    public String getUserId(){
        return userId;
    }

    @ElVoField(physicalName = "userId")
    public void setUserId(String userId){
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "ProjectVo [projectId=" + projectId + ",projectName=" + projectName + ",description=" + description + ",projectImageUrl=" + projectImageUrl + ",createdAt=" + createdAt + ",emailSendTime=" + emailSendTime + ",userId=" + userId + "]";
    }

    public boolean isFixedLengthVo() {
        return false;
    }

}
