package com.demo.proworks.projectuser.vo;

import com.inswave.elfw.annotation.ElDto;
import com.inswave.elfw.annotation.ElDtoField;
import com.fasterxml.jackson.annotation.JsonFilter;

@JsonFilter("elExcludeFilter")
@ElDto(FldYn = "", logicalName = "프로젝트에 초대(참가)한 사람들")
public class ProjectUserListVo extends com.demo.proworks.cmmn.ProworksCommVO {
    private static final long serialVersionUID = 1L;

    @ElDtoField(logicalName = "프로젝트에 초대(참가)한 사람들List", physicalName = "projectUserVoList", type = "com.demo.proworks.projectuser.ProjectUserVo", typeKind = "List", fldYn = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private java.util.List<com.demo.proworks.projectuser.vo.ProjectUserVo> projectUserVoList;

    public java.util.List<com.demo.proworks.projectuser.vo.ProjectUserVo> getProjectUserVoList(){
        return projectUserVoList;
    }

    public void setProjectUserVoList(java.util.List<com.demo.proworks.projectuser.vo.ProjectUserVo> projectUserVoList){
        this.projectUserVoList = projectUserVoList;
    }

    @Override
    public String toString() {
        return "ProjectUserListVo [projectUserVoList=" + projectUserVoList+ "]";
    }

    public boolean isFixedLengthVo() {
        return false;
    }

}
