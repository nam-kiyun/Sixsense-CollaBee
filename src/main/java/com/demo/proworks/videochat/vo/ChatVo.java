package com.demo.proworks.videochat.vo;

import com.inswave.elfw.annotation.ElDto;
import com.inswave.elfw.annotation.ElDtoField;
import com.inswave.elfw.annotation.ElVoField;
import com.fasterxml.jackson.annotation.JsonFilter;

@JsonFilter("elExcludeFilter")
@ElDto(FldYn = "", delimeterYn = "", logicalName = "ChatVo")
public class ChatVo extends com.inswave.elfw.core.CommVO {
    private static final long serialVersionUID = 1L;

    public ChatVo(){
    }

    @ElDtoField(logicalName = "채널명", physicalName = "channelName", type = "String", typeKind = "", fldYn = "", delimeterYn = "", cryptoGbn = "", cryptoKind = "", length = 0, dotLen = 0, baseValue = "", desc = "", attr = "")
    private String channelName;

    @ElDtoField(logicalName = "사용자ID", physicalName = "userId", type = "String", typeKind = "", fldYn = "", delimeterYn = "", cryptoGbn = "", cryptoKind = "", length = 0, dotLen = 0, baseValue = "", desc = "", attr = "")
    private String userId;

    @ElDtoField(logicalName = "사용자명", physicalName = "userName", type = "String", typeKind = "", fldYn = "", delimeterYn = "", cryptoGbn = "", cryptoKind = "", length = 0, dotLen = 0, baseValue = "", desc = "", attr = "")
    private String userName;

    @ElDtoField(logicalName = "메시지", physicalName = "message", type = "String", typeKind = "", fldYn = "", delimeterYn = "", cryptoGbn = "", cryptoKind = "", length = 0, dotLen = 0, baseValue = "", desc = "", attr = "")
    private String message;

    @ElDtoField(logicalName = "메시지ID", physicalName = "messageId", type = "String", typeKind = "", fldYn = "", delimeterYn = "", cryptoGbn = "", cryptoKind = "", length = 0, dotLen = 0, baseValue = "", desc = "", attr = "")
    private String messageId;

    @ElDtoField(logicalName = "전송시간", physicalName = "timestamp", type = "String", typeKind = "", fldYn = "", delimeterYn = "", cryptoGbn = "", cryptoKind = "", length = 0, dotLen = 0, baseValue = "", desc = "", attr = "")
    private String timestamp;

    @ElDtoField(logicalName = "기준시간", physicalName = "afterTimestamp", type = "String", typeKind = "", fldYn = "", delimeterYn = "", cryptoGbn = "", cryptoKind = "", length = 0, dotLen = 0, baseValue = "", desc = "", attr = "")
    private String afterTimestamp;

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

    @ElVoField(physicalName = "messageId")
    public String getMessageId(){
        String ret = this.messageId;
        return ret;
    }

    @ElVoField(physicalName = "messageId")
    public void setMessageId(String messageId){
        this.messageId = messageId;
    }

    @ElVoField(physicalName = "timestamp")
    public String getTimestamp(){
        String ret = this.timestamp;
        return ret;
    }

    @ElVoField(physicalName = "timestamp")
    public void setTimestamp(String timestamp){
        this.timestamp = timestamp;
    }

    @ElVoField(physicalName = "afterTimestamp")
    public String getAfterTimestamp(){
        String ret = this.afterTimestamp;
        return ret;
    }

    @ElVoField(physicalName = "afterTimestamp")
    public void setAfterTimestamp(String afterTimestamp){
        this.afterTimestamp = afterTimestamp;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ChatVo [");
        sb.append("channelName").append("=").append(channelName).append(",");
        sb.append("userId").append("=").append(userId).append(",");
        sb.append("userName").append("=").append(userName).append(",");
        sb.append("message").append("=").append(message).append(",");
        sb.append("messageId").append("=").append(messageId).append(",");
        sb.append("timestamp").append("=").append(timestamp).append(",");
        sb.append("afterTimestamp").append("=").append(afterTimestamp);
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
