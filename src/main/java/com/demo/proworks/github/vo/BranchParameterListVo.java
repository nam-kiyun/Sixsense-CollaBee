package com.demo.proworks.github.vo;

import com.inswave.elfw.annotation.ElDto;
import com.inswave.elfw.annotation.ElDtoField;
import com.inswave.elfw.annotation.ElVoField;
import com.fasterxml.jackson.annotation.JsonFilter;

@JsonFilter("elExcludeFilter")
@ElDto(FldYn = "", delimeterYn = "", logicalName = "깃허브 브랜치 목록")
public class BranchParameterListVo extends com.demo.proworks.cmmn.ProworksCommVO {
    private static final long serialVersionUID = 1L;

    public BranchParameterListVo(){
    }

    @ElDtoField(logicalName = "브랜치파라미터용 VO", physicalName = "branchParameterVo", type = "", typeKind = "List", fldYn = "", delimeterYn = "", cryptoGbn = "", cryptoKind = "", length = 0, dotLen = 0, baseValue = "", desc = "", attr = "")
    private java.util.List<com.demo.proworks.github.vo.BranchParameterVo> branchParameterVo;

    @ElVoField(physicalName = "branchParameterVo")
    public java.util.List<com.demo.proworks.github.vo.BranchParameterVo> getBranchParameterVo(){
        return branchParameterVo;
    }

    @ElVoField(physicalName = "branchParameterVo")
    public void setBranchParameterVo(java.util.List<com.demo.proworks.github.vo.BranchParameterVo> branchParameterVo){
        this.branchParameterVo = branchParameterVo;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("BranchParameterListVo [");
        sb.append("branchParameterVo").append("=").append(branchParameterVo);
        sb.append("]");
        return sb.toString();

    }

    public boolean isFixedLengthVo() {
        return false;
    }

    @Override
    public void _xStreamEnc() {
        for( int i=0 ; branchParameterVo != null && i < branchParameterVo.size() ; i++ ) {
            com.demo.proworks.github.vo.BranchParameterVo vo = (com.demo.proworks.github.vo.BranchParameterVo)branchParameterVo.get(i);
            vo._xStreamEnc();	 
        }
    }


    @Override
    public void _xStreamDec() {
        for( int i=0 ; branchParameterVo != null && i < branchParameterVo.size() ; i++ ) {
            com.demo.proworks.github.vo.BranchParameterVo vo = (com.demo.proworks.github.vo.BranchParameterVo)branchParameterVo.get(i);
            vo._xStreamDec();	 
        }
    }


}
