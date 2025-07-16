package com.demo.proworks.task.vo;

import com.inswave.elfw.annotation.ElDto;
import com.inswave.elfw.annotation.ElDtoField;
import com.inswave.elfw.annotation.ElVoField;
import com.fasterxml.jackson.annotation.JsonFilter;

@JsonFilter("elExcludeFilter")
@ElDto(FldYn = "", delimeterYn = "", logicalName = "업무(Task) 정보")
public class TaskVo extends com.demo.proworks.cmmn.ProworksCommVO {
    private static final long serialVersionUID = 1L;

    public TaskVo(){
    }

    @ElDtoField(logicalName = "task_id", physicalName = "taskId", type = "int", typeKind = "", fldYn = "", delimeterYn = "", cryptoGbn = "", cryptoKind = "", length = 0, dotLen = 0, baseValue = "", desc = "", attr = "")
    private int taskId;

    @ElDtoField(logicalName = "board_id", physicalName = "boardId", type = "int", typeKind = "", fldYn = "", delimeterYn = "", cryptoGbn = "", cryptoKind = "", length = 0, dotLen = 0, baseValue = "", desc = "", attr = "")
    private int boardId;

    @ElDtoField(logicalName = "project_user_id", physicalName = "projectUserId", type = "int", typeKind = "", fldYn = "", delimeterYn = "", cryptoGbn = "", cryptoKind = "", length = 0, dotLen = 0, baseValue = "", desc = "", attr = "")
    private int projectUserId;

    @ElDtoField(logicalName = "project_repo_id", physicalName = "projectRepoId", type = "int", typeKind = "", fldYn = "", delimeterYn = "", cryptoGbn = "", cryptoKind = "", length = 0, dotLen = 0, baseValue = "", desc = "", attr = "")
    private int projectRepoId;

    @ElDtoField(logicalName = "task_title", physicalName = "taskTitle", type = "String", typeKind = "", fldYn = "", delimeterYn = "", cryptoGbn = "", cryptoKind = "", length = 0, dotLen = 0, baseValue = "", desc = "", attr = "")
    private String taskTitle;

    @ElDtoField(logicalName = "priority", physicalName = "priority", type = "String", typeKind = "", fldYn = "", delimeterYn = "", cryptoGbn = "", cryptoKind = "", length = 0, dotLen = 0, baseValue = "", desc = "", attr = "")
    private String priority;

    @ElDtoField(logicalName = "start_date", physicalName = "startDate", type = "String", typeKind = "", fldYn = "", delimeterYn = "", cryptoGbn = "", cryptoKind = "", length = 0, dotLen = 0, baseValue = "", desc = "", attr = "")
    private String startDate;

    @ElDtoField(logicalName = "end_date", physicalName = "endDate", type = "String", typeKind = "", fldYn = "", delimeterYn = "", cryptoGbn = "", cryptoKind = "", length = 0, dotLen = 0, baseValue = "", desc = "", attr = "")
    private String endDate;

    @ElDtoField(logicalName = "tags", physicalName = "tags", type = "String", typeKind = "", fldYn = "", delimeterYn = "", cryptoGbn = "", cryptoKind = "", length = 0, dotLen = 0, baseValue = "", desc = "", attr = "")
    private String tags;

    @ElDtoField(logicalName = "user_name", physicalName = "userName", type = "String", typeKind = "", fldYn = "", delimeterYn = "", cryptoGbn = "", cryptoKind = "", length = 0, dotLen = 0, baseValue = "", desc = "", attr = "")
    private String userName;

    @ElVoField(physicalName = "taskId")
    public int getTaskId(){
        return taskId;
    }

    @ElVoField(physicalName = "taskId")
    public void setTaskId(int taskId){
        this.taskId = taskId;
    }

    @ElVoField(physicalName = "boardId")
    public int getBoardId(){
        return boardId;
    }

    @ElVoField(physicalName = "boardId")
    public void setBoardId(int boardId){
        this.boardId = boardId;
    }

    @ElVoField(physicalName = "projectUserId")
    public int getProjectUserId(){
        return projectUserId;
    }

    @ElVoField(physicalName = "projectUserId")
    public void setProjectUserId(int projectUserId){
        this.projectUserId = projectUserId;
    }

    @ElVoField(physicalName = "projectRepoId")
    public int getProjectRepoId(){
        return projectRepoId;
    }

    @ElVoField(physicalName = "projectRepoId")
    public void setProjectRepoId(int projectRepoId){
        this.projectRepoId = projectRepoId;
    }

    @ElVoField(physicalName = "taskTitle")
    public String getTaskTitle(){
        String ret = this.taskTitle;
        return ret;
    }

    @ElVoField(physicalName = "taskTitle")
    public void setTaskTitle(String taskTitle){
        this.taskTitle = taskTitle;
    }

    @ElVoField(physicalName = "priority")
    public String getPriority(){
        String ret = this.priority;
        return ret;
    }

    @ElVoField(physicalName = "priority")
    public void setPriority(String priority){
        this.priority = priority;
    }

    @ElVoField(physicalName = "startDate")
    public String getStartDate(){
        String ret = this.startDate;
        return ret;
    }

    @ElVoField(physicalName = "startDate")
    public void setStartDate(String startDate){
        this.startDate = startDate;
    }

    @ElVoField(physicalName = "endDate")
    public String getEndDate(){
        String ret = this.endDate;
        return ret;
    }

    @ElVoField(physicalName = "endDate")
    public void setEndDate(String endDate){
        this.endDate = endDate;
    }

    @ElVoField(physicalName = "tags")
    public String getTags(){
        String ret = this.tags;
        return ret;
    }

    @ElVoField(physicalName = "tags")
    public void setTags(String tags){
        this.tags = tags;
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("TaskVo [");
        sb.append("taskId").append("=").append(taskId).append(",");
        sb.append("boardId").append("=").append(boardId).append(",");
        sb.append("projectUserId").append("=").append(projectUserId).append(",");
        sb.append("projectRepoId").append("=").append(projectRepoId).append(",");
        sb.append("taskTitle").append("=").append(taskTitle).append(",");
        sb.append("priority").append("=").append(priority).append(",");
        sb.append("startDate").append("=").append(startDate).append(",");
        sb.append("endDate").append("=").append(endDate).append(",");
        sb.append("tags").append("=").append(tags).append(",");
        sb.append("userName").append("=").append(userName);
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
