package com.demo.proworks.manager.vo;

import com.inswave.elfw.annotation.ElDto;
import com.inswave.elfw.annotation.ElDtoField;
import com.inswave.elfw.annotation.ElVoField;
import com.fasterxml.jackson.annotation.JsonFilter;

@JsonFilter("elExcludeFilter")
@ElDto(FldYn = "", logicalName = "업무 담당자 정보")
public class ManagerVo extends com.demo.proworks.cmmn.ProworksCommVO {
    private static final long serialVersionUID = 1L;

    @ElDtoField(logicalName = "manager_id", physicalName = "managerId", type = "String", typeKind = "", fldYn = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private String managerId;

    @ElDtoField(logicalName = "task_version_id", physicalName = "taskVersionId", type = "String", typeKind = "", fldYn = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private String taskVersionId;

    @ElDtoField(logicalName = "user_id", physicalName = "userId", type = "String", typeKind = "", fldYn = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private String userId;

    @ElVoField(physicalName = "managerId")
    public String getManagerId(){
        return managerId;
    }

    @ElVoField(physicalName = "managerId")
    public void setManagerId(String managerId){
        this.managerId = managerId;
    }

    @ElVoField(physicalName = "taskVersionId")
    public String getTaskVersionId(){
        return taskVersionId;
    }

    @ElVoField(physicalName = "taskVersionId")
    public void setTaskVersionId(String taskVersionId){
        this.taskVersionId = taskVersionId;
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
        return "ManagerVo [managerId=" + managerId + ",taskVersionId=" + taskVersionId + ",userId=" + userId + "]";
    }

    public boolean isFixedLengthVo() {
        return false;
    }

}
