package com.demo.proworks.collabee.vo;

import com.inswave.elfw.annotation.ElDto;
import com.inswave.elfw.annotation.ElDtoField;
import com.fasterxml.jackson.annotation.JsonFilter;

@JsonFilter("elExcludeFilter")
@ElDto(FldYn = "", logicalName = "업로드한 파일 저장 경로")
public class FileSrcListVo extends com.demo.proworks.cmmn.ProworksCommVO {
    private static final long serialVersionUID = 1L;

    @ElDtoField(logicalName = "업로드한 파일 저장 경로List", physicalName = "fileSrcVoList", type = "com.demo.proworks.collabee.FileSrcVo", typeKind = "List", fldYn = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private java.util.List<com.demo.proworks.collabee.vo.FileSrcVo> fileSrcVoList;

    public java.util.List<com.demo.proworks.collabee.vo.FileSrcVo> getFileSrcVoList(){
        return fileSrcVoList;
    }

    public void setFileSrcVoList(java.util.List<com.demo.proworks.collabee.vo.FileSrcVo> fileSrcVoList){
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
