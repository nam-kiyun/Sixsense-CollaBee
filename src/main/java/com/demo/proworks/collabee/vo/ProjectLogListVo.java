package com.demo.proworks.collabee.vo;

import com.inswave.elfw.annotation.ElDto;
import com.inswave.elfw.annotation.ElDtoField;
import com.fasterxml.jackson.annotation.JsonFilter;

@JsonFilter("elExcludeFilter")
@ElDto(FldYn = "", logicalName = "프로젝트 로그")
public class ProjectLogListVo extends com.demo.proworks.cmmn.ProworksCommVO {
    private static final long serialVersionUID = 1L;

    @ElDtoField(logicalName = "프로젝트 로그List", physicalName = "projectLogVoList", type = "com.demo.proworks.collabee.ProjectLogVo", typeKind = "List", fldYn = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private java.util.List<com.demo.proworks.collabee.vo.ProjectLogVo> projectLogVoList;

    public java.util.List<com.demo.proworks.collabee.vo.ProjectLogVo> getProjectLogVoList(){
        return projectLogVoList;
    }

    public void setProjectLogVoList(java.util.List<com.demo.proworks.collabee.vo.ProjectLogVo> projectLogVoList){
        this.projectLogVoList = projectLogVoList;
    }

    @Override
    public String toString() {
        return "ProjectLogListVo [projectLogVoList=" + projectLogVoList+ "]";
    }

    public boolean isFixedLengthVo() {
        return false;
    }

}
