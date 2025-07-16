package com.demo.proworks.taskversion.vo;

import com.inswave.elfw.annotation.ElDto;
import com.inswave.elfw.annotation.ElDtoField;
import com.inswave.elfw.annotation.ElVoField;
import com.fasterxml.jackson.annotation.JsonFilter;

@JsonFilter("elExcludeFilter")
@ElDto(FldYn = "", delimeterYn = "", logicalName = "버전관리를 위한 Task(업무) 정보")
public class TaskVersionVo extends com.demo.proworks.cmmn.ProworksCommVO {
    private static final long serialVersionUID = 1L;

    public TaskVersionVo(){
    }

    @ElDtoField(logicalName = "task_version_id", physicalName = "taskVersionId", type = "int", typeKind = "", fldYn = "", delimeterYn = "", cryptoGbn = "", cryptoKind = "", length = 0, dotLen = 0, baseValue = "", desc = "", attr = "")
    private int taskVersionId;

    @ElDtoField(logicalName = "task_id", physicalName = "taskId", type = "int", typeKind = "", fldYn = "", delimeterYn = "", cryptoGbn = "", cryptoKind = "", length = 0, dotLen = 0, baseValue = "", desc = "", attr = "")
    private int taskId;

    @ElDtoField(logicalName = "content", physicalName = "content", type = "String", typeKind = "", fldYn = "", delimeterYn = "", cryptoGbn = "", cryptoKind = "", length = 0, dotLen = 0, baseValue = "", desc = "", attr = "")
    private String content;

    @ElDtoField(logicalName = "created_at", physicalName = "createdAt", type = "String", typeKind = "", fldYn = "", delimeterYn = "", cryptoGbn = "", cryptoKind = "", length = 0, dotLen = 0, baseValue = "", desc = "", attr = "")
    private String createdAt;

    @ElDtoField(logicalName = "파일 ", physicalName = "fileSrcVo", type = "", typeKind = "List", fldYn = "", delimeterYn = "", cryptoGbn = "", cryptoKind = "", length = 0, dotLen = 0, baseValue = "", desc = "", attr = "")
    private java.util.List<com.demo.proworks.filesrc.vo.FileSrcVo> fileSrcVo;

    @ElDtoField(logicalName = "project_id", physicalName = "projectId", type = "String", typeKind = "", fldYn = "", delimeterYn = "", cryptoGbn = "", cryptoKind = "", length = 0, dotLen = 0, baseValue = "", desc = "", attr = "")
    private String projectId;

    @ElVoField(physicalName = "taskVersionId")
    public int getTaskVersionId(){
        return taskVersionId;
    }

    @ElVoField(physicalName = "taskVersionId")
    public void setTaskVersionId(int taskVersionId){
        this.taskVersionId = taskVersionId;
    }

    @ElVoField(physicalName = "taskId")
    public int getTaskId(){
        return taskId;
    }

    @ElVoField(physicalName = "taskId")
    public void setTaskId(int taskId){
        this.taskId = taskId;
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("TaskVersionVo [");
        sb.append("taskVersionId").append("=").append(taskVersionId).append(",");
        sb.append("taskId").append("=").append(taskId).append(",");
        sb.append("content").append("=").append(content).append(",");
        sb.append("createdAt").append("=").append(createdAt).append(",");
        sb.append("fileSrcVo").append("=").append(fileSrcVo);
        sb.append("]");
        return sb.toString();

    }

    public boolean isFixedLengthVo() {
        return false;
    }

    @Override
    public void _xStreamEnc() {
        for( int i=0 ; fileSrcVo != null && i < fileSrcVo.size() ; i++ ) {
            com.demo.proworks.filesrc.vo.FileSrcVo vo = (com.demo.proworks.filesrc.vo.FileSrcVo)fileSrcVo.get(i);
            vo._xStreamEnc();	 
        }
    }


    @Override
    public void _xStreamDec() {
        for( int i=0 ; fileSrcVo != null && i < fileSrcVo.size() ; i++ ) {
            com.demo.proworks.filesrc.vo.FileSrcVo vo = (com.demo.proworks.filesrc.vo.FileSrcVo)fileSrcVo.get(i);
            vo._xStreamDec();	 
        }
    }


}
