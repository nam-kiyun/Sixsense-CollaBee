package com.demo.proworks.user.vo;

import com.inswave.elfw.annotation.ElDto;
import com.inswave.elfw.annotation.ElDtoField;
import com.inswave.elfw.annotation.ElVoField;
import com.fasterxml.jackson.annotation.JsonFilter;

@JsonFilter("elExcludeFilter")
@ElDto(FldYn = "", delimeterYn = "", logicalName = "사용자 정보")
public class UserVo extends com.demo.proworks.cmmn.ProworksCommVO {
    private static final long serialVersionUID = 1L;

    public UserVo(){
    }

    @ElDtoField(logicalName = "user_id", physicalName = "userId", type = "String", typeKind = "", fldYn = "", delimeterYn = "", cryptoGbn = "", cryptoKind = "", length = 0, dotLen = 0, baseValue = "", desc = "", attr = "")
    private String userId;

    @ElDtoField(logicalName = "user_name", physicalName = "userName", type = "String", typeKind = "", fldYn = "", delimeterYn = "", cryptoGbn = "", cryptoKind = "", length = 0, dotLen = 0, baseValue = "", desc = "", attr = "")
    private String userName;

    @ElDtoField(logicalName = "password", physicalName = "password", type = "String", typeKind = "", fldYn = "", delimeterYn = "", cryptoGbn = "IO+DB", cryptoKind = "", length = 0, dotLen = 0, baseValue = "", desc = "", attr = "")
    private String password;

    @ElDtoField(logicalName = "profile_image_url", physicalName = "profileImageUrl", type = "String", typeKind = "", fldYn = "", delimeterYn = "", cryptoGbn = "", cryptoKind = "", length = 0, dotLen = 0, baseValue = "", desc = "", attr = "")
    private String profileImageUrl;

    @ElDtoField(logicalName = "is_active", physicalName = "isActive", type = "String", typeKind = "", fldYn = "", delimeterYn = "", cryptoGbn = "", cryptoKind = "", length = 0, dotLen = 0, baseValue = "", desc = "", attr = "")
    private String isActive;

    @ElDtoField(logicalName = "created_at", physicalName = "createdAt", type = "String", typeKind = "", fldYn = "", delimeterYn = "", cryptoGbn = "", cryptoKind = "", length = 0, dotLen = 0, baseValue = "", desc = "", attr = "")
    private String createdAt;

    @ElVoField(physicalName = "userId")
    public String getUserId(){
        String ret = this.userId;
        return ret;
    }

    @ElVoField(physicalName = "userId")
    public void setUserId(String userId){
        this.userId = userId;
    }

    @ElVoField(physicalName = "userName")
    public String getUserName(){
        String ret = this.userName;
        return ret;
    }

    @ElVoField(physicalName = "userName")
    public void setUserName(String userName){
        this.userName = userName;
    }

    @ElVoField(physicalName = "password")
    public String getPassword(){
        String ret = this.password;
        ret = com.inswave.elfw.security.ElCryptoUtil.getEncrypt("", ret, true, true);
        return ret;
    }

    @ElVoField(physicalName = "password")
    public void setPassword(String password){
        this.password = com.inswave.elfw.security.ElCryptoUtil.getDecrypt("", password, true, true);
    }

    @ElVoField(physicalName = "profileImageUrl")
    public String getProfileImageUrl(){
        String ret = this.profileImageUrl;
        return ret;
    }

    @ElVoField(physicalName = "profileImageUrl")
    public void setProfileImageUrl(String profileImageUrl){
        this.profileImageUrl = profileImageUrl;
    }

    @ElVoField(physicalName = "isActive")
    public String getIsActive(){
        String ret = this.isActive;
        return ret;
    }

    @ElVoField(physicalName = "isActive")
    public void setIsActive(String isActive){
        this.isActive = isActive;
    }

    @ElVoField(physicalName = "createdAt")
    public String getCreatedAt(){
        String ret = this.createdAt;
        return ret;
    }

    @ElVoField(physicalName = "createdAt")
    public void setCreatedAt(String createdAt){
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("UserVo [");
        sb.append("userId").append("=").append(userId).append(",");
        sb.append("userName").append("=").append(userName).append(",");
        sb.append("password").append("=").append(this.getPassword()).append(",");
        sb.append("profileImageUrl").append("=").append(profileImageUrl).append(",");
        sb.append("isActive").append("=").append(isActive).append(",");
        sb.append("createdAt").append("=").append(createdAt);
        sb.append("]");
        return sb.toString();

    }

    public boolean isFixedLengthVo() {
        return false;
    }

    @Override
    public void _xStreamEnc() {
        this.password = getPassword();
    }


    @Override
    public void _xStreamDec() {
        setPassword(this.password);
    }


}
