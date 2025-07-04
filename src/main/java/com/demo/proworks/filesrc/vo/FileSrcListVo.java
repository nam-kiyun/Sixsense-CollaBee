package com.demo.proworks.filesrc.vo;

import com.inswave.elfw.annotation.ElDto;
import com.inswave.elfw.annotation.ElDtoField;
import com.fasterxml.jackson.annotation.JsonFilter;

@JsonFilter("elExcludeFilter")
@ElDto(FldYn = "", logicalName = "파일 ")
public class FileSrcListVo extends com.demo.proworks.cmmn.ProworksCommVO {
    private static final long serialVersionUID = 1L;

    @ElDtoField(logicalName = "파일 List", physicalName = "fileSrcVoList", type = "com.demo.proworks.filesrc.FileSrcVo", typeKind = "List", fldYn = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private java.util.List<com.demo.proworks.filesrc.vo.FileSrcVo> fileSrcVoList;

    public java.util.List<com.demo.proworks.filesrc.vo.FileSrcVo> getFileSrcVoList(){
        return fileSrcVoList;
    }

    public void setFileSrcVoList(java.util.List<com.demo.proworks.filesrc.vo.FileSrcVo> fileSrcVoList){
        this.fileSrcVoList = fileSrcVoList;
    }

    @Override
    public String toString() {
        return "FileSrcListVo [fileSrcVoList=" + fileSrcVoList+ "]";
    }

    public boolean isFixedLengthVo() {
        return false;
    }

}
