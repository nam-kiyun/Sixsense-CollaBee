package com.demo.proworks.videochat.vo;

import com.inswave.elfw.annotation.ElDto;
import com.inswave.elfw.annotation.ElDtoField;
import com.inswave.elfw.annotation.ElVoField;
import com.fasterxml.jackson.annotation.JsonFilter;

@JsonFilter("elExcludeFilter")
@ElDto(FldYn = "", delimeterYn = "", logicalName = "ChatMessageVo")
public class ChatMessageVo extends com.inswave.elfw.core.CommVO {
    private static final long serialVersionUID = 1L;

    public ChatMessageVo(){
    }

    @ElDtoField(logicalName = "메시지타입", physicalName = "type", type = "String", typeKind = "", fldYn = "", delimeterYn = "", cryptoGbn = "", cryptoKind = "", length = 0, dotLen = 0, baseValue = "", desc = "", attr = "")
    private String type;

    @ElDtoField(logicalName = "메시지ID", physicalName = "messageId", type = "String", typeKind = "", fldYn = "", delimeterYn = "", cryptoGbn = "", cryptoKind = "", length = 0, dotLen = 0, baseValue = "", desc = "", attr = "")
    private String messageId;

    @ElDtoField(logicalName = "채널명", physicalName = "channelName", type = "String", typeKind = "", fldYn = "", delimeterYn = "", cryptoGbn = "", cryptoKind = "", length = 0, dotLen = 0, baseValue = "", desc = "", attr = "")
    private String channelName;

    @ElDtoField(logicalName = "사용자ID", physicalName = "userId", type = "String", typeKind = "", fldYn = "", delimeterYn = "", cryptoGbn = "", cryptoKind = "", length = 0, dotLen = 0, baseValue = "", desc = "", attr = "")
    private String userId;

    @ElDtoField(logicalName = "사용자명", physicalName = "userName", type = "String", typeKind = "", fldYn = "", delimeterYn = "", cryptoGbn = "", cryptoKind = "", length = 0, dotLen = 0, baseValue = "", desc = "", attr = "")
    private String userName;

    @ElDtoField(logicalName = "메시지내용", physicalName = "message", type = "String", typeKind = "", fldYn = "", delimeterYn = "", cryptoGbn = "", cryptoKind = "", length = 0, dotLen = 0, baseValue = "", desc = "", attr = "")
    private String message;

    @ElDtoField(logicalName = "타임스탬프", physicalName = "timestamp", type = "long", typeKind = "", fldYn = "", delimeterYn = "", cryptoGbn = "", cryptoKind = "", length = 0, dotLen = 0, baseValue = "", desc = "", attr = "")
    private long timestamp;

    @ElDtoField(logicalName = "이후타임스탬프", physicalName = "afterTimestamp", type = "long", typeKind = "", fldYn = "", delimeterYn = "", cryptoGbn = "", cryptoKind = "", length = 0, dotLen = 0, baseValue = "", desc = "", attr = "")
    private long afterTimestamp;

    @ElDtoField(logicalName = "대상사용자ID", physicalName = "targetUserId", type = "String", typeKind = "", fldYn = "", delimeterYn = "", cryptoGbn = "", cryptoKind = "", length = 0, dotLen = 0, baseValue = "", desc = "", attr = "")
    private String targetUserId;

    @ElDtoField(logicalName = "SDP정보", physicalName = "sdp", type = "String", typeKind = "", fldYn = "", delimeterYn = "", cryptoGbn = "", cryptoKind = "", length = 0, dotLen = 0, baseValue = "", desc = "", attr = "")
    private String sdp;

    @ElDtoField(logicalName = "ICE후보정보", physicalName = "iceCandidate", type = "String", typeKind = "", fldYn = "", delimeterYn = "", cryptoGbn = "", cryptoKind = "", length = 0, dotLen = 0, baseValue = "", desc = "", attr = "")
    private String iceCandidate;

    @ElDtoField(logicalName = "Simple-Peer시그널", physicalName = "signal", type = "String", typeKind = "", fldYn = "", delimeterYn = "", cryptoGbn = "", cryptoKind = "", length = 0, dotLen = 0, baseValue = "", desc = "", attr = "")
    private String signal;

    @ElDtoField(logicalName = "사용자이미지", physicalName = "userImage", type = "String", typeKind = "", fldYn = "", delimeterYn = "", cryptoGbn = "", cryptoKind = "", length = 0, dotLen = 0, baseValue = "", desc = "", attr = "")
    private String userImage;

    @ElDtoField(logicalName = "생성일", physicalName = "createdAt", type = "String", typeKind = "", fldYn = "", delimeterYn = "", cryptoGbn = "", cryptoKind = "", length = 0, dotLen = 0, baseValue = "", desc = "", attr = "")
    private String createdAt;

    @ElDtoField(logicalName = "댓글 아이디", physicalName = "commentId", type = "String", typeKind = "", fldYn = "", delimeterYn = "", cryptoGbn = "", cryptoKind = "", length = 0, dotLen = 0, baseValue = "", desc = "", attr = "")
    private String commentId;

    @ElVoField(physicalName = "type")
    public String getType(){
        String ret = this.type;
        return ret;
    }

    @ElVoField(physicalName = "type")
    public void setType(String type){
        this.type = type;
    }

    @ElVoField(physicalName = "messageId")
    public String getMessageId(){
        String ret = this.messageId;
        return ret;
    }

    @ElVoField(physicalName = "messageId")
    public void setMessageId(String messageId){
        this.messageId = messageId;
    }

    @ElVoField(physicalName = "channelName")
    public String getChannelName(){
        String ret = this.channelName;
        return ret;
    }

    @ElVoField(physicalName = "channelName")
    public void setChannelName(String channelName){
        this.channelName = channelName;
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

    @ElVoField(physicalName = "userName")
    public String getUserName(){
        String ret = this.userName;
        return ret;
    }

    @ElVoField(physicalName = "userName")
    public void setUserName(String userName){
        this.userName = userName;
    }

    @ElVoField(physicalName = "message")
    public String getMessage(){
        String ret = this.message;
        return ret;
    }

    @ElVoField(physicalName = "message")
    public void setMessage(String message){
        this.message = message;
    }

    @ElVoField(physicalName = "timestamp")
    public long getTimestamp(){
        return timestamp;
    }

    @ElVoField(physicalName = "timestamp")
    public void setTimestamp(long timestamp){
        this.timestamp = timestamp;
    }

    @ElVoField(physicalName = "afterTimestamp")
    public long getAfterTimestamp(){
        return afterTimestamp;
    }

    @ElVoField(physicalName = "afterTimestamp")
    public void setAfterTimestamp(long afterTimestamp){
        this.afterTimestamp = afterTimestamp;
    }

    @ElVoField(physicalName = "targetUserId")
    public String getTargetUserId(){
        String ret = this.targetUserId;
        return ret;
    }

    @ElVoField(physicalName = "targetUserId")
    public void setTargetUserId(String targetUserId){
        this.targetUserId = targetUserId;
    }

    @ElVoField(physicalName = "sdp")
    public String getSdp(){
        String ret = this.sdp;
        return ret;
    }

    @ElVoField(physicalName = "sdp")
    public void setSdp(String sdp){
        this.sdp = sdp;
    }

    @ElVoField(physicalName = "iceCandidate")
    public String getIceCandidate(){
        String ret = this.iceCandidate;
        return ret;
    }

    @ElVoField(physicalName = "iceCandidate")
    public void setIceCandidate(String iceCandidate){
        this.iceCandidate = iceCandidate;
    }

    @ElVoField(physicalName = "signal")
    public String getSignal(){
        String ret = this.signal;
        return ret;
    }

    @ElVoField(physicalName = "signal")
    public void setSignal(String signal){
        this.signal = signal;
    }

    @ElVoField(physicalName = "userImage")
    public String getUserImage(){
        String ret = this.userImage;
        return ret;
    }

    @ElVoField(physicalName = "userImage")
    public void setUserImage(String userImage){
        this.userImage = userImage;
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

    @ElVoField(physicalName = "commentId")
    public String getCommentId(){
        String ret = this.commentId;
        return ret;
    }

    @ElVoField(physicalName = "commentId")
    public void setCommentId(String commentId){
        this.commentId = commentId;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ChatMessageVo [");
        sb.append("type").append("=").append(type).append(",");
        sb.append("messageId").append("=").append(messageId).append(",");
        sb.append("channelName").append("=").append(channelName).append(",");
        sb.append("userId").append("=").append(userId).append(",");
        sb.append("userName").append("=").append(userName).append(",");
        sb.append("message").append("=").append(message).append(",");
        sb.append("timestamp").append("=").append(timestamp).append(",");
        sb.append("afterTimestamp").append("=").append(afterTimestamp).append(",");
        sb.append("targetUserId").append("=").append(targetUserId).append(",");
        sb.append("sdp").append("=").append(sdp).append(",");
        sb.append("iceCandidate").append("=").append(iceCandidate).append(",");
        sb.append("signal").append("=").append(signal).append(",");
        sb.append("userImage").append("=").append(userImage).append(",");
        sb.append("createdAt").append("=").append(createdAt).append(",");
        sb.append("commentId").append("=").append(commentId);
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
