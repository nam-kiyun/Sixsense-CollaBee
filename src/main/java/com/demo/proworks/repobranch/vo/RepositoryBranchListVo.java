package com.demo.proworks.repobranch.vo;

import com.inswave.elfw.annotation.ElDto;
import com.inswave.elfw.annotation.ElDtoField;
import com.fasterxml.jackson.annotation.JsonFilter;

@JsonFilter("elExcludeFilter")
@ElDto(FldYn = "", logicalName = "리포지토리 브랜치 정보")
public class RepositoryBranchListVo extends com.demo.proworks.cmmn.ProworksCommVO {
    private static final long serialVersionUID = 1L;

    @ElDtoField(logicalName = "리포지토리 브랜치 정보List", physicalName = "repositoryBranchVoList", type = "com.demo.proworks.repobranch.RepositoryBranchVo", typeKind = "List", fldYn = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private java.util.List<com.demo.proworks.repobranch.vo.RepositoryBranchVo> repositoryBranchVoList;

    public java.util.List<com.demo.proworks.repobranch.vo.RepositoryBranchVo> getRepositoryBranchVoList(){
        return repositoryBranchVoList;
    }

    public void setRepositoryBranchVoList(java.util.List<com.demo.proworks.repobranch.vo.RepositoryBranchVo> repositoryBranchVoList){
        this.repositoryBranchVoList = repositoryBranchVoList;
    }

    @Override
    public String toString() {
        return "RepositoryBranchListVo [repositoryBranchVoList=" + repositoryBranchVoList+ "]";
    }

    public boolean isFixedLengthVo() {
        return false;
    }

}
