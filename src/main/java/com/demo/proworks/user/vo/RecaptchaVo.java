package com.demo.proworks.user.vo;

import com.inswave.elfw.annotation.ElDto;
import com.inswave.elfw.annotation.ElDtoField;
import com.inswave.elfw.annotation.ElVoField;
import com.fasterxml.jackson.annotation.JsonFilter;

@JsonFilter("elExcludeFilter")
@ElDto(FldYn = "", delimeterYn = "", logicalName = "리캡챠Vo")
public class RecaptchaVo extends com.demo.proworks.cmmn.ProworksCommVO {
    private static final long serialVersionUID = 1L;

    public RecaptchaVo(){
    }

    @ElDtoField(logicalName = "비밀키", physicalName = "secretKey", type = "String", typeKind = "", fldYn = "", delimeterYn = "", cryptoGbn = "", cryptoKind = "", length = 0, dotLen = 0, baseValue = "", desc = "", attr = "")
    private String secretKey;

    @ElDtoField(logicalName = "토큰", physicalName = "token", type = "String", typeKind = "", fldYn = "", delimeterYn = "", cryptoGbn = "", cryptoKind = "", length = 0, dotLen = 0, baseValue = "", desc = "", attr = "")
    private String token;

    @ElVoField(physicalName = "secretKey")
    public String getSecretKey(){
        String ret = this.secretKey;
        return ret;
    }

    @ElVoField(physicalName = "secretKey")
    public void setSecretKey(String secretKey){
        this.secretKey = secretKey;
    }

    @ElVoField(physicalName = "token")
    public String getToken(){
        String ret = this.token;
        return ret;
    }

    @ElVoField(physicalName = "token")
    public void setToken(String token){
        this.token = token;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("RecaptchaVo [");
        sb.append("secretKey").append("=").append(secretKey).append(",");
        sb.append("token").append("=").append(token);
        sb.append("]");
        return sb.toString();

    }

    public boolean isFixedLengthVo() {
        return false;
    }

    @Override
    public void _xStreamEnc() {
    }


    @Override
    public void _xStreamDec() {
    }


}
