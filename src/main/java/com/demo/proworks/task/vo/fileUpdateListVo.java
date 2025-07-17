package com.demo.proworks.task.vo;

import com.inswave.elfw.annotation.ElDto;
import com.inswave.elfw.annotation.ElDtoField;
import com.inswave.elfw.annotation.ElVoField;
import com.fasterxml.jackson.annotation.JsonFilter;

@JsonFilter("elExcludeFilter")
@ElDto(FldYn = "", delimeterYn = "", logicalName = "파일 업데이트 리스트")
public class fileUpdateListVo extends com.demo.proworks.cmmn.ProworksCommVO {
    private static final long serialVersionUID = 1L;

    public fileUpdateListVo(){
    }

    @ElDtoField(logicalName = "파일 업데이트", physicalName = "fileUpdateVo", type = "", typeKind = "List", fldYn = "", delimeterYn = "", cryptoGbn = "", cryptoKind = "", length = 0, dotLen = 0, baseValue = "", desc = "", attr = "")
    private java.util.List<com.demo.proworks.task.vo.fileUpdateVo> fileUpdateVo;

    @ElVoField(physicalName = "fileUpdateVo")
    public java.util.List<com.demo.proworks.task.vo.fileUpdateVo> getFileUpdateVo(){
        return fileUpdateVo;
    }

    @ElVoField(physicalName = "fileUpdateVo")
    public void setFileUpdateVo(java.util.List<com.demo.proworks.task.vo.fileUpdateVo> fileUpdateVo){
        this.fileUpdateVo = fileUpdateVo;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("fileUpdateListVo [");
        sb.append("fileUpdateVo").append("=").append(fileUpdateVo);
        sb.append("]");
        return sb.toString();

    }

    public boolean isFixedLengthVo() {
        return false;
    }

    @Override
    public void _xStreamEnc() {
        for( int i=0 ; fileUpdateVo != null && i < fileUpdateVo.size() ; i++ ) {
            com.demo.proworks.task.vo.fileUpdateVo vo = (com.demo.proworks.task.vo.fileUpdateVo)fileUpdateVo.get(i);
            vo._xStreamEnc();	 
        }
    }


    @Override
    public void _xStreamDec() {
        for( int i=0 ; fileUpdateVo != null && i < fileUpdateVo.size() ; i++ ) {
            com.demo.proworks.task.vo.fileUpdateVo vo = (com.demo.proworks.task.vo.fileUpdateVo)fileUpdateVo.get(i);
            vo._xStreamDec();	 
        }
    }


}
