package com.demo.proworks.task.vo;

import com.inswave.elfw.annotation.ElDto;
import com.inswave.elfw.annotation.ElDtoField;
import com.inswave.elfw.annotation.ElVoField;
import com.fasterxml.jackson.annotation.JsonFilter;

@JsonFilter("elExcludeFilter")
@ElDto(FldYn = "", logicalName = "업무(Task) 정보")
public class TaskVo extends com.demo.proworks.cmmn.ProworksCommVO {
    private static final long serialVersionUID = 1L;

    @ElDtoField(logicalName = "task_id", physicalName = "taskId", type = "String", typeKind = "", fldYn = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private String taskId;

    @ElDtoField(logicalName = "board_id", physicalName = "boardId", type = "String", typeKind = "", fldYn = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private String boardId;

    @ElDtoField(logicalName = "project_user_id", physicalName = "projectUserId", type = "String", typeKind = "", fldYn = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private String projectUserId;

    @ElDtoField(logicalName = "project_repo_id", physicalName = "projectRepoId", type = "String", typeKind = "", fldYn = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private String projectRepoId;

    @ElDtoField(logicalName = "task_title", physicalName = "taskTitle", type = "String", typeKind = "", fldYn = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private String taskTitle;

    @ElDtoField(logicalName = "priority", physicalName = "priority", type = "String", typeKind = "", fldYn = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private String priority;

    @ElDtoField(logicalName = "start_date", physicalName = "startDate", type = "String", typeKind = "", fldYn = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private String startDate;

    @ElDtoField(logicalName = "end_date", physicalName = "endDate", type = "String", typeKind = "", fldYn = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private String endDate;

    @ElDtoField(logicalName = "tags", physicalName = "tags", type = "String", typeKind = "", fldYn = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private String tags;

    @ElVoField(physicalName = "taskId")
    public String getTaskId(){
        return taskId;
    }

    @ElVoField(physicalName = "taskId")
    public void setTaskId(String taskId){
        this.taskId = taskId;
    }

    @ElVoField(physicalName = "boardId")
    public String getBoardId(){
        return boardId;
    }

    @ElVoField(physicalName = "boardId")
    public void setBoardId(String boardId){
        this.boardId = boardId;
    }

    @ElVoField(physicalName = "projectUserId")
    public String getProjectUserId(){
        return projectUserId;
    }

    @ElVoField(physicalName = "projectUserId")
    public void setProjectUserId(String projectUserId){
        this.projectUserId = projectUserId;
    }

    @ElVoField(physicalName = "projectRepoId")
    public String getProjectRepoId(){
        return projectRepoId;
    }

    @ElVoField(physicalName = "projectRepoId")
    public void setProjectRepoId(String projectRepoId){
        this.projectRepoId = projectRepoId;
    }

    @ElVoField(physicalName = "taskTitle")
    public String getTaskTitle(){
        return taskTitle;
    }

    @ElVoField(physicalName = "taskTitle")
    public void setTaskTitle(String taskTitle){
        this.taskTitle = taskTitle;
    }

    @ElVoField(physicalName = "priority")
    public String getPriority(){
        return priority;
    }

    @ElVoField(physicalName = "priority")
    public void setPriority(String priority){
        this.priority = priority;
    }

    @ElVoField(physicalName = "startDate")
    public String getStartDate(){
        return startDate;
    }

    @ElVoField(physicalName = "startDate")
    public void setStartDate(String startDate){
        this.startDate = startDate;
    }

    @ElVoField(physicalName = "endDate")
    public String getEndDate(){
        return endDate;
    }

    @ElVoField(physicalName = "endDate")
    public void setEndDate(String endDate){
        this.endDate = endDate;
    }

    @ElVoField(physicalName = "tags")
    public String getTags(){
        return tags;
    }

    @ElVoField(physicalName = "tags")
    public void setTags(String tags){
        this.tags = tags;
    }

    @Override
    public String toString() {
        return "TaskVo [taskId=" + taskId + ",boardId=" + boardId + ",projectUserId=" + projectUserId + ",projectRepoId=" + projectRepoId + ",taskTitle=" + taskTitle + ",priority=" + priority + ",startDate=" + startDate + ",endDate=" + endDate + ",tags=" + tags + "]";
    }

    public boolean isFixedLengthVo() {
        return false;
    }

}
