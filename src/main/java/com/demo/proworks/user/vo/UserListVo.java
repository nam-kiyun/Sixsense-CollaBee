package com.demo.proworks.user.vo;

import com.inswave.elfw.annotation.ElDto;
import com.inswave.elfw.annotation.ElDtoField;
import com.fasterxml.jackson.annotation.JsonFilter;

@JsonFilter("elExcludeFilter")
@ElDto(FldYn = "", logicalName = "사용자 정보")
public class UserListVo extends com.demo.proworks.cmmn.ProworksCommVO {
    private static final long serialVersionUID = 1L;

    @ElDtoField(logicalName = "사용자 정보List", physicalName = "userVoList", type = "com.demo.proworks.user.UserVo", typeKind = "List", fldYn = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private java.util.List<com.demo.proworks.user.vo.UserVo> userVoList;

    public java.util.List<com.demo.proworks.user.vo.UserVo> getUserVoList(){
        return userVoList;
    }

    public void setUserVoList(java.util.List<com.demo.proworks.user.vo.UserVo> userVoList){
        this.userVoList = userVoList;
    }

    @Override
    public String toString() {
        return "UserListVo [userVoList=" + userVoList+ "]";
    }

    public boolean isFixedLengthVo() {
        return false;
    }

}
