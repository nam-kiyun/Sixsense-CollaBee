package com.demo.proworks.githubapptoken.vo;

import com.inswave.elfw.annotation.ElDto;
import com.inswave.elfw.annotation.ElDtoField;
import com.inswave.elfw.annotation.ElVoField;
import com.fasterxml.jackson.annotation.JsonFilter;

@JsonFilter("elExcludeFilter")
@ElDto(FldYn = "", delimeterYn = "", logicalName = "깃허브 앱 토큰 저장")
public class GithubAppTokenVo extends com.demo.proworks.cmmn.ProworksCommVO {
    private static final long serialVersionUID = 1L;

    public GithubAppTokenVo(){
    }

    @ElDtoField(logicalName = "github_app_token_id", physicalName = "githubAppTokenId", type = "String", typeKind = "", fldYn = "", delimeterYn = "", cryptoGbn = "", cryptoKind = "", length = 0, dotLen = 0, baseValue = "", desc = "", attr = "")
    private String githubAppTokenId;

    @ElDtoField(logicalName = "user_id", physicalName = "userId", type = "String", typeKind = "", fldYn = "", delimeterYn = "", cryptoGbn = "", cryptoKind = "", length = 0, dotLen = 0, baseValue = "", desc = "", attr = "")
    private String userId;

    @ElDtoField(logicalName = "created_at", physicalName = "createdAt", type = "String", typeKind = "", fldYn = "", delimeterYn = "", cryptoGbn = "", cryptoKind = "", length = 0, dotLen = 0, baseValue = "", desc = "", attr = "")
    private String createdAt;

    @ElDtoField(logicalName = "github_app_installation_id", physicalName = "githubAppInstallationId", type = "String", typeKind = "", fldYn = "", delimeterYn = "", cryptoGbn = "", cryptoKind = "", length = 0, dotLen = 0, baseValue = "", desc = "", attr = "")
    private String githubAppInstallationId;

    @ElVoField(physicalName = "githubAppTokenId")
    public String getGithubAppTokenId(){
        String ret = this.githubAppTokenId;
        return ret;
    }

    @ElVoField(physicalName = "githubAppTokenId")
    public void setGithubAppTokenId(String githubAppTokenId){
        this.githubAppTokenId = githubAppTokenId;
    }

    @ElVoField(physicalName = "userId")
    public String getUserId(){
        String ret = this.userId;
        return ret;
    }

    @ElVoField(physicalName = "userId")
    public void setUserId(String userId){
        this.userId = userId;
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

    @ElVoField(physicalName = "githubAppInstallationId")
    public String getGithubAppInstallationId(){
        String ret = this.githubAppInstallationId;
        return ret;
    }

    @ElVoField(physicalName = "githubAppInstallationId")
    public void setGithubAppInstallationId(String githubAppInstallationId){
        this.githubAppInstallationId = githubAppInstallationId;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("GithubAppTokenVo [");
        sb.append("githubAppTokenId").append("=").append(githubAppTokenId).append(",");
        sb.append("userId").append("=").append(userId).append(",");
        sb.append("createdAt").append("=").append(createdAt).append(",");
        sb.append("githubAppInstallationId").append("=").append(githubAppInstallationId);
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
