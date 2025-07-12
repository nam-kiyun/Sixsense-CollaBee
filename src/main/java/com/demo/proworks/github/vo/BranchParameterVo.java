package com.demo.proworks.github.vo;

import com.inswave.elfw.annotation.ElDto;
import com.inswave.elfw.annotation.ElDtoField;
import com.inswave.elfw.annotation.ElVoField;
import com.fasterxml.jackson.annotation.JsonFilter;

@JsonFilter("elExcludeFilter")
@ElDto(FldYn = "", delimeterYn = "", logicalName = "브랜치파라미터용 VO")
public class BranchParameterVo extends com.demo.proworks.cmmn.ProworksCommVO {
    private static final long serialVersionUID = 1L;

    public BranchParameterVo(){
    }

    @ElDtoField(logicalName = "owner", physicalName = "owner", type = "String", typeKind = "", fldYn = "", delimeterYn = "", cryptoGbn = "", cryptoKind = "", length = 0, dotLen = 0, baseValue = "", desc = "", attr = "")
    private String owner;

    @ElDtoField(logicalName = "repo", physicalName = "repo", type = "String", typeKind = "", fldYn = "", delimeterYn = "", cryptoGbn = "", cryptoKind = "", length = 0, dotLen = 0, baseValue = "", desc = "", attr = "")
    private String repo;

    @ElVoField(physicalName = "owner")
    public String getOwner(){
        String ret = this.owner;
        return ret;
    }

    @ElVoField(physicalName = "owner")
    public void setOwner(String owner){
        this.owner = owner;
    }

    @ElVoField(physicalName = "repo")
    public String getRepo(){
        String ret = this.repo;
        return ret;
    }

    @ElVoField(physicalName = "repo")
    public void setRepo(String repo){
        this.repo = repo;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("branchParameterVo [");
        sb.append("owner").append("=").append(owner).append(",");
        sb.append("repo").append("=").append(repo);
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
