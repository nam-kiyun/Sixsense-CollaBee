package com.demo.proworks.task.vo;

import com.inswave.elfw.annotation.ElDto;
import com.inswave.elfw.annotation.ElDtoField;
import com.inswave.elfw.annotation.ElVoField;
import com.fasterxml.jackson.annotation.JsonFilter;

@JsonFilter("elExcludeFilter")
@ElDto(FldYn = "", delimeterYn = "", logicalName = "버전관리를 위한 Task(업무) 정보")
public class TaskUpdateVo extends com.demo.proworks.cmmn.ProworksCommVO {
    private static final long serialVersionUID = 1L;

    public TaskUpdateVo(){
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

    @ElDtoField(logicalName = "task_version_id", physicalName = "taskVersionId", type = "String", typeKind = "", fldYn = "", delimeterYn = "", cryptoGbn = "", cryptoKind = "", length = 0, dotLen = 0, baseValue = "", desc = "", attr = "")
    private String taskVersionId;

    @ElDtoField(logicalName = "content", physicalName = "content", type = "String", typeKind = "", fldYn = "", delimeterYn = "", cryptoGbn = "", cryptoKind = "", length = 0, dotLen = 0, baseValue = "", desc = "", attr = "")
    private String content;

    @ElDtoField(logicalName = "created_at", physicalName = "createdAt", type = "String", typeKind = "", fldYn = "", delimeterYn = "", cryptoGbn = "", cryptoKind = "", length = 0, dotLen = 0, baseValue = "", desc = "", attr = "")
    private String createdAt;

    @ElDtoField(logicalName = "업무 담당자 정보", physicalName = "managerVo", type = "", typeKind = "List", fldYn = "", delimeterYn = "", cryptoGbn = "", cryptoKind = "", length = 0, dotLen = 0, baseValue = "", desc = "", attr = "")
    private java.util.List<com.demo.proworks.manager.vo.ManagerVo> managerVo;

    @ElDtoField(logicalName = "파일 ", physicalName = "fileSrcVo", type = "", typeKind = "List", fldYn = "", delimeterYn = "", cryptoGbn = "", cryptoKind = "", length = 0, dotLen = 0, baseValue = "", desc = "", attr = "")
    private java.util.List<com.demo.proworks.filesrc.vo.FileSrcVo> fileSrcVo;

    @ElDtoField(logicalName = "project_id", physicalName = "projectId", type = "String", typeKind = "", fldYn = "", delimeterYn = "", cryptoGbn = "", cryptoKind = "", length = 0, dotLen = 0, baseValue = "", desc = "", attr = "")
    private String projectId;

    @ElDtoField(logicalName = "프로젝트에 초대(참가)한 사람들", physicalName = "projectUserVo", type = "", typeKind = "List", fldYn = "", delimeterYn = "", cryptoGbn = "", cryptoKind = "", length = 0, dotLen = 0, baseValue = "", desc = "", attr = "")
    private java.util.List<com.demo.proworks.projectuser.vo.ProjectUserVo> projectUserVo;

    @ElDtoField(logicalName = "user_name", physicalName = "userName", type = "String", typeKind = "", fldYn = "", delimeterYn = "", cryptoGbn = "", cryptoKind = "", length = 0, dotLen = 0, baseValue = "", desc = "", attr = "")
    private String userName;

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

    @ElVoField(physicalName = "taskVersionId")
    public String getTaskVersionId(){
        String ret = this.taskVersionId;
        return ret;
    }

    @ElVoField(physicalName = "taskVersionId")
    public void setTaskVersionId(String taskVersionId){
        this.taskVersionId = taskVersionId;
    }

    @ElVoField(physicalName = "content")
    public String getContent(){
        String ret = this.content;
        return ret;
    }

    @ElVoField(physicalName = "content")
    public void setContent(String content){
        this.content = content;
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

    @ElVoField(physicalName = "managerVo")
    public java.util.List<com.demo.proworks.manager.vo.ManagerVo> getManagerVo(){
        return managerVo;
    }

    @ElVoField(physicalName = "managerVo")
    public void setManagerVo(java.util.List<com.demo.proworks.manager.vo.ManagerVo> managerVo){
        this.managerVo = managerVo;
    }

    @ElVoField(physicalName = "fileSrcVo")
    public java.util.List<com.demo.proworks.filesrc.vo.FileSrcVo> getFileSrcVo(){
        return fileSrcVo;
    }

    @ElVoField(physicalName = "fileSrcVo")
    public void setFileSrcVo(java.util.List<com.demo.proworks.filesrc.vo.FileSrcVo> fileSrcVo){
        this.fileSrcVo = fileSrcVo;
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

    @ElVoField(physicalName = "projectUserVo")
    public java.util.List<com.demo.proworks.projectuser.vo.ProjectUserVo> getProjectUserVo(){
        return projectUserVo;
    }

    @ElVoField(physicalName = "projectUserVo")
    public void setProjectUserVo(java.util.List<com.demo.proworks.projectuser.vo.ProjectUserVo> projectUserVo){
        this.projectUserVo = projectUserVo;
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
        sb.append("TaskUpdateVo [");
        sb.append("taskId").append("=").append(taskId).append(",");
        sb.append("boardId").append("=").append(boardId).append(",");
        sb.append("projectUserId").append("=").append(projectUserId).append(",");
        sb.append("projectRepoId").append("=").append(projectRepoId).append(",");
        sb.append("taskTitle").append("=").append(taskTitle).append(",");
        sb.append("priority").append("=").append(priority).append(",");
        sb.append("startDate").append("=").append(startDate).append(",");
        sb.append("endDate").append("=").append(endDate).append(",");
        sb.append("tags").append("=").append(tags).append(",");
        sb.append("taskVersionId").append("=").append(taskVersionId).append(",");
        sb.append("content").append("=").append(content).append(",");
        sb.append("createdAt").append("=").append(createdAt).append(",");
        sb.append("managerVo").append("=").append(managerVo).append(",");
        sb.append("fileSrcVo").append("=").append(fileSrcVo).append(",");
        sb.append("projectId").append("=").append(projectId).append(",");
        sb.append("projectUserVo").append("=").append(projectUserVo).append(",");
        sb.append("userName").append("=").append(userName);
        sb.append("]");
        return sb.toString();

    }

    public boolean isFixedLengthVo() {
        return false;
    }

    @Override
    public void _xStreamEnc() {
        for( int i=0 ; managerVo != null && i < managerVo.size() ; i++ ) {
            com.demo.proworks.manager.vo.ManagerVo vo = (com.demo.proworks.manager.vo.ManagerVo)managerVo.get(i);
            vo._xStreamEnc();	 
        }
        for( int i=0 ; fileSrcVo != null && i < fileSrcVo.size() ; i++ ) {
            com.demo.proworks.filesrc.vo.FileSrcVo vo = (com.demo.proworks.filesrc.vo.FileSrcVo)fileSrcVo.get(i);
            vo._xStreamEnc();	 
        }
        for( int i=0 ; projectUserVo != null && i < projectUserVo.size() ; i++ ) {
            com.demo.proworks.projectuser.vo.ProjectUserVo vo = (com.demo.proworks.projectuser.vo.ProjectUserVo)projectUserVo.get(i);
            vo._xStreamEnc();	 
        }
    }


    @Override
    public void _xStreamDec() {
        for( int i=0 ; managerVo != null && i < managerVo.size() ; i++ ) {
            com.demo.proworks.manager.vo.ManagerVo vo = (com.demo.proworks.manager.vo.ManagerVo)managerVo.get(i);
            vo._xStreamDec();	 
        }
        for( int i=0 ; fileSrcVo != null && i < fileSrcVo.size() ; i++ ) {
            com.demo.proworks.filesrc.vo.FileSrcVo vo = (com.demo.proworks.filesrc.vo.FileSrcVo)fileSrcVo.get(i);
            vo._xStreamDec();	 
        }
        for( int i=0 ; projectUserVo != null && i < projectUserVo.size() ; i++ ) {
            com.demo.proworks.projectuser.vo.ProjectUserVo vo = (com.demo.proworks.projectuser.vo.ProjectUserVo)projectUserVo.get(i);
            vo._xStreamDec();	 
        }
    }


}
