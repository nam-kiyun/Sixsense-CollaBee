package com.demo.proworks.email.vo;

import com.inswave.elfw.annotation.ElDto;
import com.inswave.elfw.annotation.ElDtoField;
import com.inswave.elfw.annotation.ElVoField;
import com.fasterxml.jackson.annotation.JsonFilter;

@JsonFilter("elExcludeFilter")
@ElDto(FldYn = "", delimeterYn = "", logicalName = "이메일VO")
public class EmailVo extends com.demo.proworks.cmmn.ProworksCommVO {
    private static final long serialVersionUID = 1L;

    public EmailVo(){
    }

    @ElDtoField(logicalName = "이메일", physicalName = "email", type = "String", typeKind = "", fldYn = "", delimeterYn = "", cryptoGbn = "", cryptoKind = "", length = 0, dotLen = 0, baseValue = "", desc = "", attr = "")
    private String email;

    @ElDtoField(logicalName = "project_name", physicalName = "projectName", type = "String", typeKind = "", fldYn = "", delimeterYn = "", cryptoGbn = "", cryptoKind = "", length = 0, dotLen = 0, baseValue = "", desc = "", attr = "")
    private String projectName;

    @ElDtoField(logicalName = "user_name", physicalName = "userName", type = "String", typeKind = "", fldYn = "", delimeterYn = "", cryptoGbn = "", cryptoKind = "", length = 0, dotLen = 0, baseValue = "", desc = "", attr = "")
    private String userName;

    @ElDtoField(logicalName = "project_image_url", physicalName = "projectImageUrl", type = "String", typeKind = "", fldYn = "", delimeterYn = "", cryptoGbn = "", cryptoKind = "", length = 0, dotLen = 0, baseValue = "", desc = "", attr = "")
    private String projectImageUrl;

    @ElDtoField(logicalName = "email_send_time", physicalName = "emailSendTime", type = "String", typeKind = "", fldYn = "", delimeterYn = "", cryptoGbn = "", cryptoKind = "", length = 0, dotLen = 0, baseValue = "", desc = "", attr = "")
    private String emailSendTime;

    @ElDtoField(logicalName = "user_id", physicalName = "userId", type = "String", typeKind = "", fldYn = "", delimeterYn = "", cryptoGbn = "", cryptoKind = "", length = 0, dotLen = 0, baseValue = "", desc = "", attr = "")
    private String userId;

    @ElDtoField(logicalName = "project_id", physicalName = "projectId", type = "String", typeKind = "", fldYn = "", delimeterYn = "", cryptoGbn = "", cryptoKind = "", length = 0, dotLen = 0, baseValue = "", desc = "", attr = "")
    private String projectId;

    @ElVoField(physicalName = "email")
    public String getEmail(){
        String ret = this.email;
        return ret;
    }

    @ElVoField(physicalName = "email")
    public void setEmail(String email){
        this.email = email;
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

    @ElVoField(physicalName = "userName")
    public String getUserName(){
        String ret = this.userName;
        return ret;
    }

    @ElVoField(physicalName = "userName")
    public void setUserName(String userName){
        this.userName = userName;
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

    @ElVoField(physicalName = "projectId")
    public String getProjectId(){
        String ret = this.projectId;
        return ret;
    }

    @ElVoField(physicalName = "projectId")
    public void setProjectId(String projectId){
        this.projectId = projectId;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("EmailVo [");
        sb.append("email").append("=").append(email).append(",");
        sb.append("projectName").append("=").append(projectName).append(",");
        sb.append("userName").append("=").append(userName).append(",");
        sb.append("projectImageUrl").append("=").append(projectImageUrl).append(",");
        sb.append("emailSendTime").append("=").append(emailSendTime).append(",");
        sb.append("userId").append("=").append(userId).append(",");
        sb.append("projectId").append("=").append(projectId);
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
