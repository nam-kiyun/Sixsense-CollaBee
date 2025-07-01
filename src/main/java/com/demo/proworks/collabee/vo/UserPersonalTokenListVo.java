package com.demo.proworks.collabee.vo;

import com.inswave.elfw.annotation.ElDto;
import com.inswave.elfw.annotation.ElDtoField;
import com.fasterxml.jackson.annotation.JsonFilter;

@JsonFilter("elExcludeFilter")
@ElDto(FldYn = "", logicalName = "깃허브 개인 처리를 위한 PAT토큰")
public class UserPersonalTokenListVo extends com.demo.proworks.cmmn.ProworksCommVO {
    private static final long serialVersionUID = 1L;

    @ElDtoField(logicalName = "깃허브 개인 처리를 위한 PAT토큰List", physicalName = "userPersonalTokenVoList", type = "com.demo.proworks.collabee.UserPersonalTokenVo", typeKind = "List", fldYn = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private java.util.List<com.demo.proworks.collabee.vo.UserPersonalTokenVo> userPersonalTokenVoList;

    public java.util.List<com.demo.proworks.collabee.vo.UserPersonalTokenVo> getUserPersonalTokenVoList(){
        return userPersonalTokenVoList;
    }

    public void setUserPersonalTokenVoList(java.util.List<com.demo.proworks.collabee.vo.UserPersonalTokenVo> userPersonalTokenVoList){
        this.userPersonalTokenVoList = userPersonalTokenVoList;
    }

    @Override
    public String toString() {
        return "UserPersonalTokenListVo [userPersonalTokenVoList=" + userPersonalTokenVoList+ "]";
    }

    public boolean isFixedLengthVo() {
        return false;
    }

}
