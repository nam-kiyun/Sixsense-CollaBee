package com.demo.proworks.taskversion.vo;

import com.inswave.elfw.annotation.ElDto;
import com.inswave.elfw.annotation.ElDtoField;
import com.fasterxml.jackson.annotation.JsonFilter;

@JsonFilter("elExcludeFilter")
@ElDto(FldYn = "", logicalName = "버전관리를 위한 Task(업무) 정보")
public class TaskVersionListVo extends com.demo.proworks.cmmn.ProworksCommVO {
    private static final long serialVersionUID = 1L;

    @ElDtoField(logicalName = "버전관리를 위한 Task(업무) 정보List", physicalName = "taskVersionVoList", type = "com.demo.proworks.taskversion.TaskVersionVo", typeKind = "List", fldYn = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private java.util.List<com.demo.proworks.taskversion.vo.TaskVersionVo> taskVersionVoList;

    public java.util.List<com.demo.proworks.taskversion.vo.TaskVersionVo> getTaskVersionVoList(){
        return taskVersionVoList;
    }

    public void setTaskVersionVoList(java.util.List<com.demo.proworks.taskversion.vo.TaskVersionVo> taskVersionVoList){
        this.taskVersionVoList = taskVersionVoList;
    }

    @Override
    public String toString() {
        return "TaskVersionListVo [taskVersionVoList=" + taskVersionVoList+ "]";
    }

    public boolean isFixedLengthVo() {
        return false;
    }

}
