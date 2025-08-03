package com.demo.proworks.filesrc.vo;

import com.inswave.elfw.annotation.ElDto;
import com.inswave.elfw.annotation.ElDtoField;
import com.inswave.elfw.annotation.ElVoField;
import com.fasterxml.jackson.annotation.JsonFilter;

@JsonFilter("elExcludeFilter")
@ElDto(FldYn = "", delimeterYn = "", logicalName = "파일 ")
public class FileSrcVo extends com.demo.proworks.cmmn.ProworksCommVO {
    private static final long serialVersionUID = 1L;

    public FileSrcVo(){
    }

    @ElDtoField(logicalName = "file_id", physicalName = "fileId", type = "String", typeKind = "", fldYn = "", delimeterYn = "", cryptoGbn = "", cryptoKind = "", length = 0, dotLen = 0, baseValue = "", desc = "", attr = "")
    private String fileId;

    @ElDtoField(logicalName = "task_version_id", physicalName = "taskVersionId", type = "String", typeKind = "", fldYn = "", delimeterYn = "", cryptoGbn = "", cryptoKind = "", length = 0, dotLen = 0, baseValue = "", desc = "", attr = "")
    private String taskVersionId;

    @ElDtoField(logicalName = "file_name", physicalName = "fileName", type = "String", typeKind = "", fldYn = "", delimeterYn = "", cryptoGbn = "", cryptoKind = "", length = 0, dotLen = 0, baseValue = "", desc = "", attr = "")
    private String fileName;

    @ElDtoField(logicalName = "file_path", physicalName = "filePath", type = "String", typeKind = "", fldYn = "", delimeterYn = "", cryptoGbn = "", cryptoKind = "", length = 0, dotLen = 0, baseValue = "", desc = "", attr = "")
    private String filePath;

    @ElDtoField(logicalName = "project_id", physicalName = "projectId", type = "String", typeKind = "", fldYn = "", delimeterYn = "", cryptoGbn = "", cryptoKind = "", length = 0, dotLen = 0, baseValue = "", desc = "", attr = "")
    private String projectId;

    @ElVoField(physicalName = "fileId")
    public String getFileId(){
        String ret = this.fileId;
        return ret;
    }

    @ElVoField(physicalName = "fileId")
    public void setFileId(String fileId){
        this.fileId = fileId;
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

    @ElVoField(physicalName = "fileName")
    public String getFileName(){
        String ret = this.fileName;
        return ret;
    }

    @ElVoField(physicalName = "fileName")
    public void setFileName(String fileName){
        this.fileName = fileName;
    }

    @ElVoField(physicalName = "filePath")
    public String getFilePath(){
        String ret = this.filePath;
        return ret;
    }

    @ElVoField(physicalName = "filePath")
    public void setFilePath(String filePath){
        this.filePath = filePath;
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
        sb.append("FileSrcVo [");
        sb.append("fileId").append("=").append(fileId).append(",");
        sb.append("taskVersionId").append("=").append(taskVersionId).append(",");
        sb.append("fileName").append("=").append(fileName).append(",");
        sb.append("filePath").append("=").append(filePath);
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
