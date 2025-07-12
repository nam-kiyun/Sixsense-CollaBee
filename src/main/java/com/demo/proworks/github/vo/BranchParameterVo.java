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

    @ElDtoField(logicalName = "branchName", physicalName = "branchName", type = "String", typeKind = "", fldYn = "", delimeterYn = "", cryptoGbn = "", cryptoKind = "", length = 0, dotLen = 0, baseValue = "", desc = "", attr = "")
    private String branchName;

    @ElDtoField(logicalName = "fromBranch", physicalName = "fromBranch", type = "String", typeKind = "", fldYn = "", delimeterYn = "", cryptoGbn = "", cryptoKind = "", length = 0, dotLen = 0, baseValue = "", desc = "", attr = "")
    private String fromBranch;

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

    @ElVoField(physicalName = "branchName")
    public String getBranchName(){
        String ret = this.branchName;
        return ret;
    }

    @ElVoField(physicalName = "branchName")
    public void setBranchName(String branchName){
        this.branchName = branchName;
    }

    @ElVoField(physicalName = "fromBranch")
    public String getFromBranch(){
        String ret = this.fromBranch;
        return ret;
    }

    @ElVoField(physicalName = "fromBranch")
    public void setFromBranch(String fromBranch){
        this.fromBranch = fromBranch;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("BranchParameterVo [");
        sb.append("owner").append("=").append(owner).append(",");
        sb.append("repo").append("=").append(repo).append(",");
        sb.append("branchName").append("=").append(branchName).append(",");
        sb.append("fromBranch").append("=").append(fromBranch);
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
