package com.demo.proworks.collabee.vo;

import com.inswave.elfw.annotation.ElDto;
import com.inswave.elfw.annotation.ElDtoField;
import com.fasterxml.jackson.annotation.JsonFilter;

@JsonFilter("elExcludeFilter")
@ElDto(FldYn = "", logicalName = "업무")
public class TaskListVo extends com.demo.proworks.cmmn.ProworksCommVO {
    private static final long serialVersionUID = 1L;

    @ElDtoField(logicalName = "업무List", physicalName = "taskVoList", type = "com.demo.proworks.collabee.TaskVo", typeKind = "List", fldYn = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private java.util.List<com.demo.proworks.collabee.vo.TaskVo> taskVoList;

    public java.util.List<com.demo.proworks.collabee.vo.TaskVo> getTaskVoList(){
        return taskVoList;
    }

    public void setTaskVoList(java.util.List<com.demo.proworks.collabee.vo.TaskVo> taskVoList){
        this.taskVoList = taskVoList;
    }

    @Override
    public String toString() {
        return "TaskListVo [taskVoList=" + taskVoList+ "]";
    }

    public boolean isFixedLengthVo() {
        return false;
    }

}
