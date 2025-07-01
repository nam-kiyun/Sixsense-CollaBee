package com.demo.proworks.projectuser.vo;

import com.inswave.elfw.annotation.ElDto;
import com.inswave.elfw.annotation.ElDtoField;
import com.inswave.elfw.annotation.ElVoField;
import com.fasterxml.jackson.annotation.JsonFilter;

@JsonFilter("elExcludeFilter")
@ElDto(FldYn = "", logicalName = "프로젝트에 초대(참가)한 사람들")
public class ProjectUserVo extends com.demo.proworks.cmmn.ProworksCommVO {
    private static final long serialVersionUID = 1L;

    @ElDtoField(logicalName = "project_user_id", physicalName = "projectUserId", type = "String", typeKind = "", fldYn = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private String projectUserId;

    @ElDtoField(logicalName = "project_id", physicalName = "projectId", type = "String", typeKind = "", fldYn = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private String projectId;

    @ElDtoField(logicalName = "user_id", physicalName = "userId", type = "String", typeKind = "", fldYn = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private String userId;

    @ElDtoField(logicalName = "role", physicalName = "role", type = "String", typeKind = "", fldYn = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private String role;

    @ElVoField(physicalName = "projectUserId")
    public String getProjectUserId(){
        return projectUserId;
    }

    @ElVoField(physicalName = "projectUserId")
    public void setProjectUserId(String projectUserId){
        this.projectUserId = projectUserId;
    }

    @ElVoField(physicalName = "projectId")
    public String getProjectId(){
        return projectId;
    }

    @ElVoField(physicalName = "projectId")
    public void setProjectId(String projectId){
        this.projectId = projectId;
    }

    @ElVoField(physicalName = "userId")
    public String getUserId(){
        return userId;
    }

    @ElVoField(physicalName = "userId")
    public void setUserId(String userId){
        this.userId = userId;
    }

    @ElVoField(physicalName = "role")
    public String getRole(){
        return role;
    }

    @ElVoField(physicalName = "role")
    public void setRole(String role){
        this.role = role;
    }

    @Override
    public String toString() {
        return "ProjectUserVo [projectUserId=" + projectUserId + ",projectId=" + projectId + ",userId=" + userId + ",role=" + role + "]";
    }

    public boolean isFixedLengthVo() {
        return false;
    }

}
