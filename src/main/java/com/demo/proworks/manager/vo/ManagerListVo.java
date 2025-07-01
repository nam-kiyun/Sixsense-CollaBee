package com.demo.proworks.manager.vo;

import com.inswave.elfw.annotation.ElDto;
import com.inswave.elfw.annotation.ElDtoField;
import com.fasterxml.jackson.annotation.JsonFilter;

@JsonFilter("elExcludeFilter")
@ElDto(FldYn = "", logicalName = "업무 담당자 정보")
public class ManagerListVo extends com.demo.proworks.cmmn.ProworksCommVO {
    private static final long serialVersionUID = 1L;

    @ElDtoField(logicalName = "업무 담당자 정보List", physicalName = "managerVoList", type = "com.demo.proworks.manager.ManagerVo", typeKind = "List", fldYn = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private java.util.List<com.demo.proworks.manager.vo.ManagerVo> managerVoList;

    public java.util.List<com.demo.proworks.manager.vo.ManagerVo> getManagerVoList(){
        return managerVoList;
    }

    public void setManagerVoList(java.util.List<com.demo.proworks.manager.vo.ManagerVo> managerVoList){
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
