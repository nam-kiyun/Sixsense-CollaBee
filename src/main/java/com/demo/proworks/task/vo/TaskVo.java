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

    @ElDtoField(logicalName = "task_id", physicalName = "taskId", type = "String", typeKind = "", fldYn = "", delimeterYn = "", cryptoGbn = "", cryptoKind = "", length = 0, dotLen = 0, baseValue = "", desc = "", attr = "")
    private String taskId;

    @ElDtoField(logicalName = "board_id", physicalName = "boardId", type = "String", typeKind = "", fldYn = "", delimeterYn = "", cryptoGbn = "", cryptoKind = "", length = 0, dotLen = 0, baseValue = "", desc = "", attr = "")
    private String boardId;

    @ElDtoField(logicalName = "project_user_id", physicalName = "projectUserId", type = "String", typeKind = "", fldYn = "", delimeterYn = "", cryptoGbn = "", cryptoKind = "", length = 0, dotLen = 0, baseValue = "", desc = "", attr = "")
    private String projectUserId;

    @ElDtoField(logicalName = "project_repo_id", physicalName = "projectRepoId", type = "String", typeKind = "", fldYn = "", delimeterYn = "", cryptoGbn = "", cryptoKind = "", length = 0, dotLen = 0, baseValue = "", desc = "", attr = "")
    private String projectRepoId;

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

    @ElDtoField(logicalName = "sortField", physicalName = "sortField", type = "String", typeKind = "", fldYn = "", delimeterYn = "", cryptoGbn = "", cryptoKind = "", length = 0, dotLen = 0, baseValue = "", desc = "", attr = "")
    private String sortField;

    @ElDtoField(logicalName = "sortOrder", physicalName = "sortOrder", type = "String", typeKind = "", fldYn = "", delimeterYn = "", cryptoGbn = "", cryptoKind = "", length = 0, dotLen = 0, baseValue = "", desc = "", attr = "")
    private String sortOrder;

    @ElDtoField(logicalName = "user_name", physicalName = "userName", type = "String", typeKind = "", fldYn = "", delimeterYn = "", cryptoGbn = "", cryptoKind = "", length = 0, dotLen = 0, baseValue = "", desc = "", attr = "")
    private String userName;

    @ElDtoField(logicalName = "project_id", physicalName = "projectId", type = "String", typeKind = "", fldYn = "", delimeterYn = "", cryptoGbn = "", cryptoKind = "", length = 0, dotLen = 0, baseValue = "", desc = "", attr = "")
    private String projectId;

    @ElVoField(physicalName = "taskId")
    public String getTaskId(){
        String ret = this.taskId;
        return ret;
    }

    @ElVoField(physicalName = "taskId")
    public void setTaskId(String taskId){
        this.taskId = taskId;
    }

    @ElVoField(physicalName = "boardId")
    public String getBoardId(){
        String ret = this.boardId;
        return ret;
    }

    @ElVoField(physicalName = "boardId")
    public void setBoardId(String boardId){
        this.boardId = boardId;
    }

    @ElVoField(physicalName = "projectUserId")
    public String getProjectUserId(){
        String ret = this.projectUserId;
        return ret;
    }

    @ElVoField(physicalName = "projectUserId")
    public void setProjectUserId(String projectUserId){
        this.projectUserId = projectUserId;
    }

    @ElVoField(physicalName = "projectRepoId")
    public String getProjectRepoId(){
        String ret = this.projectRepoId;
        return ret;
    }

    @ElVoField(physicalName = "projectRepoId")
    public void setProjectRepoId(String projectRepoId){
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

    @ElVoField(physicalName = "sortField")
    public String getSortField(){
        String ret = this.sortField;
        return ret;
    }

    @ElVoField(physicalName = "sortField")
    public void setSortField(String sortField){
        this.sortField = sortField;
    }

    @ElVoField(physicalName = "sortOrder")
    public String getSortOrder(){
        String ret = this.sortOrder;
        return ret;
    }

    @ElVoField(physicalName = "sortOrder")
    public void setSortOrder(String sortOrder){
        this.sortOrder = sortOrder;
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
        sb.append("userName").append("=").append(userName).append(",");
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
