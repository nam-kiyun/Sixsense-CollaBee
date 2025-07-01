package com.demo.proworks.collabee.vo;

import com.inswave.elfw.annotation.ElDto;
import com.inswave.elfw.annotation.ElDtoField;
import com.fasterxml.jackson.annotation.JsonFilter;

@JsonFilter("elExcludeFilter")
@ElDto(FldYn = "", logicalName = "업무 담당자")
public class ManagerListVo extends com.demo.proworks.cmmn.ProworksCommVO {
    private static final long serialVersionUID = 1L;

    @ElDtoField(logicalName = "업무 담당자List", physicalName = "managerVoList", type = "com.demo.proworks.collabee.ManagerVo", typeKind = "List", fldYn = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private java.util.List<com.demo.proworks.collabee.vo.ManagerVo> managerVoList;

    public java.util.List<com.demo.proworks.collabee.vo.ManagerVo> getManagerVoList(){
        return managerVoList;
    }

    public void setManagerVoList(java.util.List<com.demo.proworks.collabee.vo.ManagerVo> managerVoList){
        this.managerVoList = managerVoList;
    }

    @Override
    public String toString() {
        return "ManagerListVo [managerVoList=" + managerVoList+ "]";
    }

    public boolean isFixedLengthVo() {
        return false;
    }

}
