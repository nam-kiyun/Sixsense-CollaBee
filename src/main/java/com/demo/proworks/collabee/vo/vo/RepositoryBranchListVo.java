package com.demo.proworks.collabee.vo.vo;

import com.inswave.elfw.annotation.ElDto;
import com.inswave.elfw.annotation.ElDtoField;
import com.fasterxml.jackson.annotation.JsonFilter;

@JsonFilter("elExcludeFilter")
@ElDto(FldYn = "", logicalName = "연결된레포지토리브랜치")
public class RepositoryBranchListVo extends com.demo.proworks.cmmn.ProworksCommVO {
    private static final long serialVersionUID = 1L;

    @ElDtoField(logicalName = "연결된레포지토리브랜치List", physicalName = "repositoryBranchVoList", type = "com.demo.proworks.collabee.vo.RepositoryBranchVo", typeKind = "List", fldYn = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private java.util.List<com.demo.proworks.collabee.vo.vo.RepositoryBranchVo> repositoryBranchVoList;

    public java.util.List<com.demo.proworks.collabee.vo.vo.RepositoryBranchVo> getRepositoryBranchVoList(){
        return repositoryBranchVoList;
    }

    public void setRepositoryBranchVoList(java.util.List<com.demo.proworks.collabee.vo.vo.RepositoryBranchVo> repositoryBranchVoList){
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
