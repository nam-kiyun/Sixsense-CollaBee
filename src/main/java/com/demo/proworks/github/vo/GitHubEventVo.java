package com.demo.proworks.github.vo;

import com.inswave.elfw.annotation.ElDto;
import com.inswave.elfw.annotation.ElDtoField;
import com.inswave.elfw.annotation.ElVoField;
import com.fasterxml.jackson.annotation.JsonFilter;

/**
 * GitHub 웹훅 이벤트 정보 VO
 * test 디렉터리의 웹훅 이벤트 로그를 기반으로 작성
 */
@JsonFilter("elExcludeFilter")
@ElDto(FldYn = "", logicalName = "GitHub 웹훅 이벤트 정보")
public class GitHubEventVo extends com.demo.proworks.cmmn.ProworksCommVO {
    
    private static final long serialVersionUID = 1L;

    @ElDtoField(logicalName = "event_id", physicalName = "eventId", type = "String", typeKind = "", fldYn = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private String eventId;

    @ElDtoField(logicalName = "webhook_id", physicalName = "webhookId", type = "String", typeKind = "", fldYn = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private String webhookId;

    @ElDtoField(logicalName = "project_repo_id", physicalName = "projectRepoId", type = "String", typeKind = "", fldYn = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private String projectRepoId;

    @ElDtoField(logicalName = "event_type", physicalName = "eventType", type = "String", typeKind = "", fldYn = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private String eventType;

    @ElDtoField(logicalName = "action", physicalName = "action", type = "String", typeKind = "", fldYn = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private String action;

    @ElDtoField(logicalName = "delivery_id", physicalName = "deliveryId", type = "String", typeKind = "", fldYn = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private String deliveryId;

    @ElDtoField(logicalName = "repository_name", physicalName = "repositoryName", type = "String", typeKind = "", fldYn = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private String repositoryName;

    @ElDtoField(logicalName = "repository_full_name", physicalName = "repositoryFullName", type = "String", typeKind = "", fldYn = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private String repositoryFullName;

    @ElDtoField(logicalName = "sender_login", physicalName = "senderLogin", type = "String", typeKind = "", fldYn = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private String senderLogin;

    @ElDtoField(logicalName = "sender_id", physicalName = "senderId", type = "String", typeKind = "", fldYn = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private String senderId;

    @ElDtoField(logicalName = "ref", physicalName = "ref", type = "String", typeKind = "", fldYn = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private String ref;

    @ElDtoField(logicalName = "before_sha", physicalName = "beforeSha", type = "String", typeKind = "", fldYn = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private String beforeSha;

    @ElDtoField(logicalName = "after_sha", physicalName = "afterSha", type = "String", typeKind = "", fldYn = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private String afterSha;

    @ElDtoField(logicalName = "commits_count", physicalName = "commitsCount", type = "int", typeKind = "", fldYn = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private int commitsCount;

    @ElDtoField(logicalName = "pull_request_number", physicalName = "pullRequestNumber", type = "int", typeKind = "", fldYn = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private int pullRequestNumber;

    @ElDtoField(logicalName = "pull_request_title", physicalName = "pullRequestTitle", type = "String", typeKind = "", fldYn = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private String pullRequestTitle;

    @ElDtoField(logicalName = "issue_number", physicalName = "issueNumber", type = "int", typeKind = "", fldYn = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private int issueNumber;

    @ElDtoField(logicalName = "issue_title", physicalName = "issueTitle", type = "String", typeKind = "", fldYn = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private String issueTitle;

    @ElDtoField(logicalName = "payload_json", physicalName = "payloadJson", type = "String", typeKind = "", fldYn = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private String payloadJson;

    @ElDtoField(logicalName = "signature", physicalName = "signature", type = "String", typeKind = "", fldYn = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private String signature;

    @ElDtoField(logicalName = "status", physicalName = "status", type = "String", typeKind = "", fldYn = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private String status;

    @ElDtoField(logicalName = "processed_at", physicalName = "processedAt", type = "String", typeKind = "", fldYn = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private String processedAt;

    @ElDtoField(logicalName = "error_message", physicalName = "errorMessage", type = "String", typeKind = "", fldYn = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private String errorMessage;

    public GitHubEventVo() {}

    @ElVoField(physicalName = "eventId")
    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    @ElVoField(physicalName = "webhookId")
    public String getWebhookId() {
        return webhookId;
    }

    public void setWebhookId(String webhookId) {
        this.webhookId = webhookId;
    }

    @ElVoField(physicalName = "projectRepoId")
    public String getProjectRepoId() {
        return projectRepoId;
    }

    public void setProjectRepoId(String projectRepoId) {
        this.projectRepoId = projectRepoId;
    }

    @ElVoField(physicalName = "eventType")
    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    @ElVoField(physicalName = "action")
    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    @ElVoField(physicalName = "deliveryId")
    public String getDeliveryId() {
        return deliveryId;
    }

    public void setDeliveryId(String deliveryId) {
        this.deliveryId = deliveryId;
    }

    @ElVoField(physicalName = "repositoryName")
    public String getRepositoryName() {
        return repositoryName;
    }

    public void setRepositoryName(String repositoryName) {
        this.repositoryName = repositoryName;
    }

    @ElVoField(physicalName = "repositoryFullName")
    public String getRepositoryFullName() {
        return repositoryFullName;
    }

    public void setRepositoryFullName(String repositoryFullName) {
        this.repositoryFullName = repositoryFullName;
    }

    @ElVoField(physicalName = "senderLogin")
    public String getSenderLogin() {
        return senderLogin;
    }

    public void setSenderLogin(String senderLogin) {
        this.senderLogin = senderLogin;
    }

    @ElVoField(physicalName = "senderId")
    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    @ElVoField(physicalName = "ref")
    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    @ElVoField(physicalName = "beforeSha")
    public String getBeforeSha() {
        return beforeSha;
    }

    public void setBeforeSha(String beforeSha) {
        this.beforeSha = beforeSha;
    }

    @ElVoField(physicalName = "afterSha")
    public String getAfterSha() {
        return afterSha;
    }

    public void setAfterSha(String afterSha) {
        this.afterSha = afterSha;
    }

    @ElVoField(physicalName = "commitsCount")
    public int getCommitsCount() {
        return commitsCount;
    }

    public void setCommitsCount(int commitsCount) {
        this.commitsCount = commitsCount;
    }

    @ElVoField(physicalName = "pullRequestNumber")
    public int getPullRequestNumber() {
        return pullRequestNumber;
    }

    public void setPullRequestNumber(int pullRequestNumber) {
        this.pullRequestNumber = pullRequestNumber;
    }

    @ElVoField(physicalName = "pullRequestTitle")
    public String getPullRequestTitle() {
        return pullRequestTitle;
    }

    public void setPullRequestTitle(String pullRequestTitle) {
        this.pullRequestTitle = pullRequestTitle;
    }

    @ElVoField(physicalName = "issueNumber")
    public int getIssueNumber() {
        return issueNumber;
    }

    public void setIssueNumber(int issueNumber) {
        this.issueNumber = issueNumber;
    }

    @ElVoField(physicalName = "issueTitle")
    public String getIssueTitle() {
        return issueTitle;
    }

    public void setIssueTitle(String issueTitle) {
        this.issueTitle = issueTitle;
    }

    @ElVoField(physicalName = "payloadJson")
    public String getPayloadJson() {
        return payloadJson;
    }

    public void setPayloadJson(String payloadJson) {
        this.payloadJson = payloadJson;
    }

    @ElVoField(physicalName = "signature")
    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    @ElVoField(physicalName = "status")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @ElVoField(physicalName = "processedAt")
    public String getProcessedAt() {
        return processedAt;
    }

    public void setProcessedAt(String processedAt) {
        this.processedAt = processedAt;
    }

    @ElVoField(physicalName = "errorMessage")
    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("GitHubEventVo [");
        sb.append("eventId=").append(eventId);
        sb.append(", webhookId=").append(webhookId);
        sb.append(", projectRepoId=").append(projectRepoId);
        sb.append(", eventType=").append(eventType);
        sb.append(", action=").append(action);
        sb.append(", deliveryId=").append(deliveryId);
        sb.append(", repositoryName=").append(repositoryName);
        sb.append(", repositoryFullName=").append(repositoryFullName);
        sb.append(", senderLogin=").append(senderLogin);
        sb.append(", senderId=").append(senderId);
        sb.append(", ref=").append(ref);
        sb.append(", beforeSha=").append(beforeSha);
        sb.append(", afterSha=").append(afterSha);
        sb.append(", commitsCount=").append(commitsCount);
        sb.append(", pullRequestNumber=").append(pullRequestNumber);
        sb.append(", pullRequestTitle=").append(pullRequestTitle);
        sb.append(", issueNumber=").append(issueNumber);
        sb.append(", issueTitle=").append(issueTitle);
        sb.append(", payloadJson=").append(payloadJson != null ? "[JSON_DATA]" : null);
        sb.append(", signature=").append(signature != null ? "[SIGNATURE]" : null);
        sb.append(", status=").append(status);
        sb.append(", processedAt=").append(processedAt);
        sb.append(", errorMessage=").append(errorMessage);
        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean isFixedLengthVo() {
        return false;
    }
}