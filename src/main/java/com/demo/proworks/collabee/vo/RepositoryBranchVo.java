package com.demo.proworks.collabee.vo;

import com.inswave.elfw.annotation.ElDto;
import com.inswave.elfw.annotation.ElDtoField;
import com.inswave.elfw.annotation.ElVoField;
import com.fasterxml.jackson.annotation.JsonFilter;

@JsonFilter("elExcludeFilter")
@ElDto(FldYn = "", logicalName = "레포지토리의 브랜치 정보")
public class RepositoryBranchVo extends com.demo.proworks.cmmn.ProworksCommVO {
    private static final long serialVersionUID = 1L;

    @ElDtoField(logicalName = "repo_branch_id", physicalName = "repoBranchId", type = "String", typeKind = "", fldYn = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private String repoBranchId;

    @ElDtoField(logicalName = "project_repo_id", physicalName = "projectRepoId", type = "String", typeKind = "", fldYn = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private String projectRepoId;

    @ElDtoField(logicalName = "branch_name", physicalName = "branchName", type = "String", typeKind = "", fldYn = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private String branchName;

    @ElDtoField(logicalName = "created_at", physicalName = "createdAt", type = "String", typeKind = "", fldYn = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private String createdAt;

    @ElDtoField(logicalName = "base_sha", physicalName = "baseSha", type = "String", typeKind = "", fldYn = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private String baseSha;

    @ElVoField(physicalName = "repoBranchId")
    public String getRepoBranchId(){
        return repoBranchId;
    }

    @ElVoField(physicalName = "repoBranchId")
    public void setRepoBranchId(String repoBranchId){
        this.repoBranchId = repoBranchId;
    }

    @ElVoField(physicalName = "projectRepoId")
    public String getProjectRepoId(){
        return projectRepoId;
    }

    @ElVoField(physicalName = "projectRepoId")
    public void setProjectRepoId(String projectRepoId){
        this.projectRepoId = projectRepoId;
    }

    @ElVoField(physicalName = "branchName")
    public String getBranchName(){
        return branchName;
    }

    @ElVoField(physicalName = "branchName")
    public void setBranchName(String branchName){
        this.branchName = branchName;
    }

    @ElVoField(physicalName = "createdAt")
    public String getCreatedAt(){
        return createdAt;
    }

    @ElVoField(physicalName = "createdAt")
    public void setCreatedAt(String createdAt){
        this.createdAt = createdAt;
    }

    @ElVoField(physicalName = "baseSha")
    public String getBaseSha(){
        return baseSha;
    }

    @ElVoField(physicalName = "baseSha")
    public void setBaseSha(String baseSha){
        this.baseSha = baseSha;
    }

    @Override
    public String toString() {
        return "RepositoryBranchVo [repoBranchId=" + repoBranchId + ",projectRepoId=" + projectRepoId + ",branchName=" + branchName + ",createdAt=" + createdAt + ",baseSha=" + baseSha + "]";
    }

    public boolean isFixedLengthVo() {
        return false;
    }

}
