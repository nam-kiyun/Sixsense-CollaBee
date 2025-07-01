package com.demo.proworks.collabee.vo;

import com.inswave.elfw.annotation.ElDto;
import com.inswave.elfw.annotation.ElDtoField;
import com.inswave.elfw.annotation.ElVoField;
import com.fasterxml.jackson.annotation.JsonFilter;

@JsonFilter("elExcludeFilter")
@ElDto(FldYn = "", logicalName = "깃허브 개인 처리를 위한 PAT토큰")
public class UserPersonalTokenVo extends com.demo.proworks.cmmn.ProworksCommVO {
    private static final long serialVersionUID = 1L;

    @ElDtoField(logicalName = "user_personal_token_id", physicalName = "userPersonalTokenId", type = "String", typeKind = "", fldYn = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private String userPersonalTokenId;

    @ElDtoField(logicalName = "user_id", physicalName = "userId", type = "String", typeKind = "", fldYn = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private String userId;

    @ElDtoField(logicalName = "access_token", physicalName = "accessToken", type = "String", typeKind = "", fldYn = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private String accessToken;

    @ElDtoField(logicalName = "scope", physicalName = "scope", type = "String", typeKind = "", fldYn = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private String scope;

    @ElDtoField(logicalName = "created_at", physicalName = "createdAt", type = "String", typeKind = "", fldYn = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private String createdAt;

    @ElDtoField(logicalName = "expired_at", physicalName = "expiredAt", type = "String", typeKind = "", fldYn = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private String expiredAt;

    @ElVoField(physicalName = "userPersonalTokenId")
    public String getUserPersonalTokenId(){
        return userPersonalTokenId;
    }

    @ElVoField(physicalName = "userPersonalTokenId")
    public void setUserPersonalTokenId(String userPersonalTokenId){
        this.userPersonalTokenId = userPersonalTokenId;
    }

    @ElVoField(physicalName = "userId")
    public String getUserId(){
        return userId;
    }

    @ElVoField(physicalName = "userId")
    public void setUserId(String userId){
        this.userId = userId;
    }

    @ElVoField(physicalName = "accessToken")
    public String getAccessToken(){
        return accessToken;
    }

    @ElVoField(physicalName = "accessToken")
    public void setAccessToken(String accessToken){
        this.accessToken = accessToken;
    }

    @ElVoField(physicalName = "scope")
    public String getScope(){
        return scope;
    }

    @ElVoField(physicalName = "scope")
    public void setScope(String scope){
        this.scope = scope;
    }

    @ElVoField(physicalName = "createdAt")
    public String getCreatedAt(){
        return createdAt;
    }

    @ElVoField(physicalName = "createdAt")
    public void setCreatedAt(String createdAt){
        this.createdAt = createdAt;
    }

    @ElVoField(physicalName = "expiredAt")
    public String getExpiredAt(){
        return expiredAt;
    }

    @ElVoField(physicalName = "expiredAt")
    public void setExpiredAt(String expiredAt){
        this.expiredAt = expiredAt;
    }

    @Override
    public String toString() {
        return "UserPersonalTokenVo [userPersonalTokenId=" + userPersonalTokenId + ",userId=" + userId + ",accessToken=" + accessToken + ",scope=" + scope + ",createdAt=" + createdAt + ",expiredAt=" + expiredAt + "]";
    }

    public boolean isFixedLengthVo() {
        return false;
    }

}
