package com.demo.proworks.project.vo;

import com.inswave.elfw.annotation.ElDto;
import com.inswave.elfw.annotation.ElDtoField;
import com.inswave.elfw.annotation.ElVoField;
import com.fasterxml.jackson.annotation.JsonFilter;

@JsonFilter("elExcludeFilter")
@ElDto(FldYn = "", delimeterYn = "", logicalName = "프로젝트 정보")
public class ProjectVo extends com.demo.proworks.cmmn.ProworksCommVO {
    private static final long serialVersionUID = 1L;

    public ProjectVo(){
    }

    @ElDtoField(logicalName = "project_id", physicalName = "projectId", type = "String", typeKind = "", fldYn = "", delimeterYn = "", cryptoGbn = "", cryptoKind = "", length = 0, dotLen = 0, baseValue = "", desc = "", attr = "")
    private String projectId;

    @ElDtoField(logicalName = "project_name", physicalName = "projectName", type = "String", typeKind = "", fldYn = "", delimeterYn = "", cryptoGbn = "", cryptoKind = "", length = 0, dotLen = 0, baseValue = "", desc = "", attr = "")
    private String projectName;

    @ElDtoField(logicalName = "description", physicalName = "description", type = "String", typeKind = "", fldYn = "", delimeterYn = "", cryptoGbn = "", cryptoKind = "", length = 0, dotLen = 0, baseValue = "", desc = "", attr = "")
    private String description;

    @ElDtoField(logicalName = "project_image_url", physicalName = "projectImageUrl", type = "String", typeKind = "", fldYn = "", delimeterYn = "", cryptoGbn = "", cryptoKind = "", length = 0, dotLen = 0, baseValue = "", desc = "", attr = "")
    private String projectImageUrl;

    @ElDtoField(logicalName = "created_at", physicalName = "createdAt", type = "String", typeKind = "", fldYn = "", delimeterYn = "", cryptoGbn = "", cryptoKind = "", length = 0, dotLen = 0, baseValue = "", desc = "", attr = "")
    private String createdAt;

    @ElDtoField(logicalName = "email_send_time", physicalName = "emailSendTime", type = "String", typeKind = "", fldYn = "", delimeterYn = "", cryptoGbn = "", cryptoKind = "", length = 0, dotLen = 0, baseValue = "", desc = "", attr = "")
    private String emailSendTime;

    @ElDtoField(logicalName = "user_id", physicalName = "userId", type = "String", typeKind = "", fldYn = "", delimeterYn = "", cryptoGbn = "", cryptoKind = "", length = 0, dotLen = 0, baseValue = "", desc = "", attr = "")
    private String userId;

    @ElVoField(physicalName = "projectId")
    public String getProjectId(){
        String ret = this.projectId;
        return ret;
    }

    @ElVoField(physicalName = "projectId")
    public void setProjectId(String projectId){
        this.projectId = projectId;
    }

    @ElVoField(physicalName = "projectName")
    public String getProjectName(){
        String ret = this.projectName;
        return ret;
    }

    @ElVoField(physicalName = "projectName")
    public void setProjectName(String projectName){
        this.projectName = projectName;
    }

    @ElVoField(physicalName = "description")
    public String getDescription(){
        String ret = this.description;
        return ret;
    }

    @ElVoField(physicalName = "description")
    public void setDescription(String description){
        this.description = description;
    }

    @ElVoField(physicalName = "projectImageUrl")
    public String getProjectImageUrl(){
        String ret = this.projectImageUrl;
        return ret;
    }

    @ElVoField(physicalName = "projectImageUrl")
    public void setProjectImageUrl(String projectImageUrl){
        this.projectImageUrl = projectImageUrl;
    }

    @ElVoField(physicalName = "createdAt")
    public String getCreatedAt(){
        String ret = this.createdAt;
        return ret;
    }

    @ElVoField(physicalName = "createdAt")
    public void setCreatedAt(String createdAt){
        this.createdAt = createdAt;
    }

    @ElVoField(physicalName = "emailSendTime")
    public String getEmailSendTime(){
        String ret = this.emailSendTime;
        return ret;
    }

    @ElVoField(physicalName = "emailSendTime")
    public void setEmailSendTime(String emailSendTime){
        this.emailSendTime = emailSendTime;
    }

    @ElVoField(physicalName = "userId")
    public String getUserId(){
        String ret = this.userId;
        return ret;
    }

    @ElVoField(physicalName = "userId")
    public void setUserId(String userId){
        this.userId = userId;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ProjectVo [");
        sb.append("projectId").append("=").append(projectId).append(",");
        sb.append("projectName").append("=").append(projectName).append(",");
        sb.append("description").append("=").append(description).append(",");
        sb.append("projectImageUrl").append("=").append(projectImageUrl).append(",");
        sb.append("createdAt").append("=").append(createdAt).append(",");
        sb.append("emailSendTime").append("=").append(emailSendTime).append(",");
        sb.append("userId").append("=").append(userId);
        sb.append("]");
        return sb.toString();

    }

    public boolean isFixedLengthVo() {
        return false;
    }

    @Override
    public void _xStreamEnc() {
    }


    @Override
    public void _xStreamDec() {
    }


}
