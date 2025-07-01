package com.demo.proworks.projectrepo.vo;

import com.inswave.elfw.annotation.ElDto;
import com.inswave.elfw.annotation.ElDtoField;
import com.fasterxml.jackson.annotation.JsonFilter;

@JsonFilter("elExcludeFilter")
@ElDto(FldYn = "", logicalName = "프로젝트와 연결된 레포지토리 정보")
public class ProjectRepositoryListVo extends com.demo.proworks.cmmn.ProworksCommVO {
    private static final long serialVersionUID = 1L;

    @ElDtoField(logicalName = "프로젝트와 연결된 레포지토리 정보List", physicalName = "projectRepositoryVoList", type = "com.demo.proworks.projectrepo.ProjectRepositoryVo", typeKind = "List", fldYn = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private java.util.List<com.demo.proworks.projectrepo.vo.ProjectRepositoryVo> projectRepositoryVoList;

    public java.util.List<com.demo.proworks.projectrepo.vo.ProjectRepositoryVo> getProjectRepositoryVoList(){
        return projectRepositoryVoList;
    }

    public void setProjectRepositoryVoList(java.util.List<com.demo.proworks.projectrepo.vo.ProjectRepositoryVo> projectRepositoryVoList){
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
