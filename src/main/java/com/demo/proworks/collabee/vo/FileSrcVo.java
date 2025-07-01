package com.demo.proworks.collabee.vo;

import com.inswave.elfw.annotation.ElDto;
import com.inswave.elfw.annotation.ElDtoField;
import com.inswave.elfw.annotation.ElVoField;
import com.fasterxml.jackson.annotation.JsonFilter;

@JsonFilter("elExcludeFilter")
@ElDto(FldYn = "", logicalName = "업로드한 파일 저장 경로")
public class FileSrcVo extends com.demo.proworks.cmmn.ProworksCommVO {
    private static final long serialVersionUID = 1L;

    @ElDtoField(logicalName = "file_id", physicalName = "fileId", type = "String", typeKind = "", fldYn = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private String fileId;

    @ElDtoField(logicalName = "task_version_id", physicalName = "taskVersionId", type = "String", typeKind = "", fldYn = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private String taskVersionId;

    @ElDtoField(logicalName = "file_name", physicalName = "fileName", type = "String", typeKind = "", fldYn = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private String fileName;

    @ElDtoField(logicalName = "file_path", physicalName = "filePath", type = "String", typeKind = "", fldYn = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private String filePath;

    @ElVoField(physicalName = "fileId")
    public String getFileId(){
        return fileId;
    }

    @ElVoField(physicalName = "fileId")
    public void setFileId(String fileId){
        this.fileId = fileId;
    }

    @ElVoField(physicalName = "taskVersionId")
    public String getTaskVersionId(){
        return taskVersionId;
    }

    @ElVoField(physicalName = "taskVersionId")
    public void setTaskVersionId(String taskVersionId){
        this.taskVersionId = taskVersionId;
    }

    @ElVoField(physicalName = "fileName")
    public String getFileName(){
        return fileName;
    }

    @ElVoField(physicalName = "fileName")
    public void setFileName(String fileName){
        this.fileName = fileName;
    }

    @ElVoField(physicalName = "filePath")
    public String getFilePath(){
        return filePath;
    }

    @ElVoField(physicalName = "filePath")
    public void setFilePath(String filePath){
        this.filePath = filePath;
    }

    @Override
    public String toString() {
        return "FileSrcVo [fileId=" + fileId + ",taskVersionId=" + taskVersionId + ",fileName=" + fileName + ",filePath=" + filePath + "]";
    }

    public boolean isFixedLengthVo() {
        return false;
    }

}
