package com.demo.proworks.task.vo;

import com.inswave.elfw.annotation.ElDto;
import com.inswave.elfw.annotation.ElDtoField;
import com.inswave.elfw.annotation.ElVoField;
import com.fasterxml.jackson.annotation.JsonFilter;

@JsonFilter("elExcludeFilter")
@ElDto(FldYn = "", delimeterYn = "", logicalName = "파일 업데이트")
public class fileUpdateVo extends com.demo.proworks.cmmn.ProworksCommVO {
    private static final long serialVersionUID = 1L;

    public fileUpdateVo(){
    }

    @ElDtoField(logicalName = "file_id", physicalName = "fileId", type = "String", typeKind = "", fldYn = "", delimeterYn = "", cryptoGbn = "", cryptoKind = "", length = 0, dotLen = 0, baseValue = "", desc = "", attr = "")
    private String fileId;

    @ElDtoField(logicalName = "task_version_id", physicalName = "taskVersionId", type = "String", typeKind = "", fldYn = "", delimeterYn = "", cryptoGbn = "", cryptoKind = "", length = 0, dotLen = 0, baseValue = "", desc = "", attr = "")
    private String taskVersionId;

    @ElDtoField(logicalName = "file_name", physicalName = "fileName", type = "String", typeKind = "", fldYn = "", delimeterYn = "", cryptoGbn = "", cryptoKind = "", length = 0, dotLen = 0, baseValue = "", desc = "", attr = "")
    private String fileName;

    @ElDtoField(logicalName = "file_path", physicalName = "filePath", type = "String", typeKind = "", fldYn = "", delimeterYn = "", cryptoGbn = "", cryptoKind = "", length = 0, dotLen = 0, baseValue = "", desc = "", attr = "")
    private String filePath;

    @ElDtoField(logicalName = "size", physicalName = "size", type = "String", typeKind = "", fldYn = "", delimeterYn = "", cryptoGbn = "", cryptoKind = "", length = 0, dotLen = 0, baseValue = "", desc = "", attr = "")
    private String size;

    @ElDtoField(logicalName = "type", physicalName = "type", type = "String", typeKind = "", fldYn = "", delimeterYn = "", cryptoGbn = "", cryptoKind = "", length = 0, dotLen = 0, baseValue = "", desc = "", attr = "")
    private String type;

    @ElDtoField(logicalName = "data", physicalName = "data", type = "String", typeKind = "", fldYn = "", delimeterYn = "", cryptoGbn = "", cryptoKind = "", length = 0, dotLen = 0, baseValue = "", desc = "", attr = "")
    private String data;

    @ElDtoField(logicalName = "CHK", physicalName = "CHK", type = "String", typeKind = "", fldYn = "", delimeterYn = "", cryptoGbn = "", cryptoKind = "", length = 0, dotLen = 0, baseValue = "", desc = "", attr = "")
    private String CHK;

    @ElDtoField(logicalName = "num", physicalName = "num", type = "String", typeKind = "", fldYn = "", delimeterYn = "", cryptoGbn = "", cryptoKind = "", length = 0, dotLen = 0, baseValue = "", desc = "", attr = "")
    private String num;

    @ElDtoField(logicalName = "chk", physicalName = "chk", type = "String", typeKind = "", fldYn = "", delimeterYn = "", cryptoGbn = "", cryptoKind = "", length = 0, dotLen = 0, baseValue = "", desc = "", attr = "")
    private String chk;

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

    @ElVoField(physicalName = "size")
    public String getSize(){
        String ret = this.size;
        return ret;
    }

    @ElVoField(physicalName = "size")
    public void setSize(String size){
        this.size = size;
    }

    @ElVoField(physicalName = "type")
    public String getType(){
        String ret = this.type;
        return ret;
    }

    @ElVoField(physicalName = "type")
    public void setType(String type){
        this.type = type;
    }

    @ElVoField(physicalName = "data")
    public String getData(){
        String ret = this.data;
        return ret;
    }

    @ElVoField(physicalName = "data")
    public void setData(String data){
        this.data = data;
    }

    @ElVoField(physicalName = "CHK")
    public String getCHK(){
        String ret = this.CHK;
        return ret;
    }

    @ElVoField(physicalName = "CHK")
    public void setCHK(String CHK){
        this.CHK = CHK;
    }

    @ElVoField(physicalName = "num")
    public String getNum(){
        String ret = this.num;
        return ret;
    }

    @ElVoField(physicalName = "num")
    public void setNum(String num){
        this.num = num;
    }

    @ElVoField(physicalName = "chk")
    public String getChk(){
        String ret = this.chk;
        return ret;
    }

    @ElVoField(physicalName = "chk")
    public void setChk(String chk){
        this.chk = chk;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("fileUpdateVo [");
        sb.append("fileId").append("=").append(fileId).append(",");
        sb.append("taskVersionId").append("=").append(taskVersionId).append(",");
        sb.append("fileName").append("=").append(fileName).append(",");
        sb.append("filePath").append("=").append(filePath).append(",");
        sb.append("size").append("=").append(size).append(",");
        sb.append("type").append("=").append(type).append(",");
        sb.append("data").append("=").append(data).append(",");
        sb.append("CHK").append("=").append(CHK).append(",");
        sb.append("num").append("=").append(num).append(",");
        sb.append("chk").append("=").append(chk);
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
