package com.demo.proworks.github.vo;

import com.inswave.elfw.annotation.ElDto;
import com.inswave.elfw.annotation.ElDtoField;
import com.inswave.elfw.annotation.ElVoField;
import com.fasterxml.jackson.annotation.JsonFilter;

/**
 * GitHub 사용자 정보 VO
 * test 디렉터리의 GitHub OAuth 인증 정보를 기반으로 작성
 */
@JsonFilter("elExcludeFilter")
@ElDto(FldYn = "", logicalName = "GitHub 사용자 정보")
public class GitHubUserVo extends com.demo.proworks.cmmn.ProworksCommVO {
    
    private static final long serialVersionUID = 1L;

    @ElDtoField(logicalName = "github_user_id", physicalName = "githubUserId", type = "String", typeKind = "", fldYn = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private String githubUserId;

    @ElDtoField(logicalName = "local_user_id", physicalName = "localUserId", type = "String", typeKind = "", fldYn = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private String localUserId;

    @ElDtoField(logicalName = "github_username", physicalName = "githubUsername", type = "String", typeKind = "", fldYn = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private String githubUsername;

    @ElDtoField(logicalName = "github_email", physicalName = "githubEmail", type = "String", typeKind = "", fldYn = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private String githubEmail;

    @ElDtoField(logicalName = "display_name", physicalName = "displayName", type = "String", typeKind = "", fldYn = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private String displayName;

    @ElDtoField(logicalName = "avatar_url", physicalName = "avatarUrl", type = "String", typeKind = "", fldYn = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private String avatarUrl;

    @ElDtoField(logicalName = "github_profile_url", physicalName = "githubProfileUrl", type = "String", typeKind = "", fldYn = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private String githubProfileUrl;

    @ElDtoField(logicalName = "access_token", physicalName = "accessToken", type = "String", typeKind = "", fldYn = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private String accessToken;

    @ElDtoField(logicalName = "token_type", physicalName = "tokenType", type = "String", typeKind = "", fldYn = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private String tokenType;

    @ElDtoField(logicalName = "scope", physicalName = "scope", type = "String", typeKind = "", fldYn = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private String scope;

    @ElDtoField(logicalName = "connected_at", physicalName = "connectedAt", type = "String", typeKind = "", fldYn = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private String connectedAt;

    @ElDtoField(logicalName = "last_access_at", physicalName = "lastAccessAt", type = "String", typeKind = "", fldYn = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private String lastAccessAt;

    @ElDtoField(logicalName = "is_active", physicalName = "isActive", type = "String", typeKind = "", fldYn = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private String isActive;

    public GitHubUserVo() {}

    @ElVoField(physicalName = "githubUserId")
    public String getGithubUserId() {
        return githubUserId;
    }

    public void setGithubUserId(String githubUserId) {
        this.githubUserId = githubUserId;
    }

    @ElVoField(physicalName = "localUserId")
    public String getLocalUserId() {
        return localUserId;
    }

    public void setLocalUserId(String localUserId) {
        this.localUserId = localUserId;
    }

    @ElVoField(physicalName = "githubUsername")
    public String getGithubUsername() {
        return githubUsername;
    }

    public void setGithubUsername(String githubUsername) {
        this.githubUsername = githubUsername;
    }

    @ElVoField(physicalName = "githubEmail")
    public String getGithubEmail() {
        return githubEmail;
    }

    public void setGithubEmail(String githubEmail) {
        this.githubEmail = githubEmail;
    }

    @ElVoField(physicalName = "displayName")
    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @ElVoField(physicalName = "avatarUrl")
    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    @ElVoField(physicalName = "githubProfileUrl")
    public String getGithubProfileUrl() {
        return githubProfileUrl;
    }

    public void setGithubProfileUrl(String githubProfileUrl) {
        this.githubProfileUrl = githubProfileUrl;
    }

    @ElVoField(physicalName = "accessToken")
    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    @ElVoField(physicalName = "tokenType")
    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    @ElVoField(physicalName = "scope")
    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    @ElVoField(physicalName = "connectedAt")
    public String getConnectedAt() {
        return connectedAt;
    }

    public void setConnectedAt(String connectedAt) {
        this.connectedAt = connectedAt;
    }

    @ElVoField(physicalName = "lastAccessAt")
    public String getLastAccessAt() {
        return lastAccessAt;
    }

    public void setLastAccessAt(String lastAccessAt) {
        this.lastAccessAt = lastAccessAt;
    }

    @ElVoField(physicalName = "isActive")
    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("GitHubUserVo [");
        sb.append("githubUserId=").append(githubUserId);
        sb.append(", localUserId=").append(localUserId);
        sb.append(", githubUsername=").append(githubUsername);
        sb.append(", githubEmail=").append(githubEmail);
        sb.append(", displayName=").append(displayName);
        sb.append(", avatarUrl=").append(avatarUrl);
        sb.append(", githubProfileUrl=").append(githubProfileUrl);
        sb.append(", accessToken=").append("[PROTECTED]");
        sb.append(", tokenType=").append(tokenType);
        sb.append(", scope=").append(scope);
        sb.append(", connectedAt=").append(connectedAt);
        sb.append(", lastAccessAt=").append(lastAccessAt);
        sb.append(", isActive=").append(isActive);
        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean isFixedLengthVo() {
        return false;
    }
}