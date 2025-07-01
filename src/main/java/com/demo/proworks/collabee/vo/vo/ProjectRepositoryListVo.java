package com.demo.proworks.collabee.vo.vo;

import com.inswave.elfw.annotation.ElDto;
import com.inswave.elfw.annotation.ElDtoField;
import com.fasterxml.jackson.annotation.JsonFilter;

@JsonFilter("elExcludeFilter")
@ElDto(FldYn = "", logicalName = "프로젝트와연결된레포지토리")
public class ProjectRepositoryListVo extends com.demo.proworks.cmmn.ProworksCommVO {
    private static final long serialVersionUID = 1L;

    @ElDtoField(logicalName = "프로젝트와연결된레포지토리List", physicalName = "projectRepositoryVoList", type = "com.demo.proworks.collabee.vo.ProjectRepositoryVo", typeKind = "List", fldYn = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private java.util.List<com.demo.proworks.collabee.vo.vo.ProjectRepositoryVo> projectRepositoryVoList;

    public java.util.List<com.demo.proworks.collabee.vo.vo.ProjectRepositoryVo> getProjectRepositoryVoList(){
        return projectRepositoryVoList;
    }

    public void setProjectRepositoryVoList(java.util.List<com.demo.proworks.collabee.vo.vo.ProjectRepositoryVo> projectRepositoryVoList){
        this.projectRepositoryVoList = projectRepositoryVoList;
    }

    @Override
    public String toString() {
        return "ProjectRepositoryListVo [projectRepositoryVoList=" + projectRepositoryVoList+ "]";
    }

    public boolean isFixedLengthVo() {
        return false;
    }

}
