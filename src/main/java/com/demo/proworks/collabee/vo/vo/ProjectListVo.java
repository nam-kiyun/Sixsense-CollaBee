package com.demo.proworks.collabee.vo.vo;

import com.inswave.elfw.annotation.ElDto;
import com.inswave.elfw.annotation.ElDtoField;
import com.fasterxml.jackson.annotation.JsonFilter;

@JsonFilter("elExcludeFilter")
@ElDto(FldYn = "", logicalName = "프로젝트")
public class ProjectListVo extends com.demo.proworks.cmmn.ProworksCommVO {
    private static final long serialVersionUID = 1L;

    @ElDtoField(logicalName = "프로젝트List", physicalName = "projectVoList", type = "com.demo.proworks.collabee.vo.ProjectVo", typeKind = "List", fldYn = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private java.util.List<com.demo.proworks.collabee.vo.vo.ProjectVo> projectVoList;

    public java.util.List<com.demo.proworks.collabee.vo.vo.ProjectVo> getProjectVoList(){
        return projectVoList;
    }

    public void setProjectVoList(java.util.List<com.demo.proworks.collabee.vo.vo.ProjectVo> projectVoList){
        this.projectVoList = projectVoList;
    }

    @Override
    public String toString() {
        return "ProjectListVo [projectVoList=" + projectVoList+ "]";
    }

    public boolean isFixedLengthVo() {
        return false;
    }

}
