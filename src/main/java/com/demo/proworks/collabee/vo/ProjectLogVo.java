package com.demo.proworks.collabee.vo;

import com.inswave.elfw.annotation.ElDto;
import com.inswave.elfw.annotation.ElDtoField;
import com.inswave.elfw.annotation.ElVoField;
import com.fasterxml.jackson.annotation.JsonFilter;

@JsonFilter("elExcludeFilter")
@ElDto(FldYn = "", logicalName = "프로젝트 로그")
public class ProjectLogVo extends com.demo.proworks.cmmn.ProworksCommVO {
    private static final long serialVersionUID = 1L;

    @ElDtoField(logicalName = "project_log_id", physicalName = "projectLogId", type = "String", typeKind = "", fldYn = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private String projectLogId;

    @ElDtoField(logicalName = "project_user_id", physicalName = "projectUserId", type = "String", typeKind = "", fldYn = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private String projectUserId;

    @ElDtoField(logicalName = "task_id", physicalName = "taskId", type = "String", typeKind = "", fldYn = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private String taskId;

    @ElDtoField(logicalName = "log_type", physicalName = "logType", type = "String", typeKind = "", fldYn = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private String logType;

    @ElDtoField(logicalName = "log_description", physicalName = "logDescription", type = "String", typeKind = "", fldYn = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private String logDescription;

    @ElDtoField(logicalName = "log_decription", physicalName = "logDecription", type = "String", typeKind = "", fldYn = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private String logDecription;

    @ElVoField(physicalName = "projectLogId")
    public String getProjectLogId(){
        return projectLogId;
    }

    @ElVoField(physicalName = "projectLogId")
    public void setProjectLogId(String projectLogId){
        this.projectLogId = projectLogId;
    }

    @ElVoField(physicalName = "projectUserId")
    public String getProjectUserId(){
        return projectUserId;
    }

    @ElVoField(physicalName = "projectUserId")
    public void setProjectUserId(String projectUserId){
        this.projectUserId = projectUserId;
    }

    @ElVoField(physicalName = "taskId")
    public String getTaskId(){
        return taskId;
    }

    @ElVoField(physicalName = "taskId")
    public void setTaskId(String taskId){
        this.taskId = taskId;
    }

    @ElVoField(physicalName = "logType")
    public String getLogType(){
        return logType;
    }

    @ElVoField(physicalName = "logType")
    public void setLogType(String logType){
        this.logType = logType;
    }

    @ElVoField(physicalName = "logDescription")
    public String getLogDescription(){
        return logDescription;
    }

    @ElVoField(physicalName = "logDescription")
    public void setLogDescription(String logDescription){
        this.logDescription = logDescription;
    }

    @ElVoField(physicalName = "logDecription")
    public String getLogDecription(){
        return logDecription;
    }

    @ElVoField(physicalName = "logDecription")
    public void setLogDecription(String logDecription){
        this.logDecription = logDecription;
    }

    @Override
    public String toString() {
        return "ProjectLogVo [projectLogId=" + projectLogId + ",projectUserId=" + projectUserId + ",taskId=" + taskId + ",logType=" + logType + ",logDescription=" + logDescription + ",logDecription=" + logDecription + "]";
    }

    public boolean isFixedLengthVo() {
        return false;
    }

}
