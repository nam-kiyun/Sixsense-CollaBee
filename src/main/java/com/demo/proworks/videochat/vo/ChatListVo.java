package com.demo.proworks.videochat.vo;

import com.inswave.elfw.annotation.ElDto;
import com.inswave.elfw.annotation.ElDtoField;
import com.inswave.elfw.annotation.ElVoField;
import com.fasterxml.jackson.annotation.JsonFilter;

@JsonFilter("elExcludeFilter")
@ElDto(FldYn = "", delimeterYn = "", logicalName = "ChatListVo")
public class ChatListVo extends com.inswave.elfw.core.CommVO {
    private static final long serialVersionUID = 1L;

    public ChatListVo(){
    }

    @ElDtoField(logicalName = "채팅목록", physicalName = "chatVo", type = "", typeKind = "List", fldYn = "", delimeterYn = "", cryptoGbn = "", cryptoKind = "", length = 0, dotLen = 0, baseValue = "", desc = "", attr = "")
    private java.util.List<com.demo.proworks.videochat.vo.ChatVo> chatVo;

    @ElDtoField(logicalName = "개수", physicalName = "count", type = "int", typeKind = "", fldYn = "", delimeterYn = "", cryptoGbn = "", cryptoKind = "", length = 0, dotLen = 0, baseValue = "", desc = "", attr = "")
    private int count;

    @ElDtoField(logicalName = "성공여부", physicalName = "success", type = "boolean", typeKind = "", fldYn = "", delimeterYn = "", cryptoGbn = "", cryptoKind = "", length = 0, dotLen = 0, baseValue = "", desc = "", attr = "")
    private boolean success;

    @ElDtoField(logicalName = "결과메시지", physicalName = "resultMessage", type = "String", typeKind = "", fldYn = "", delimeterYn = "", cryptoGbn = "", cryptoKind = "", length = 0, dotLen = 0, baseValue = "", desc = "", attr = "")
    private String resultMessage;

    @ElVoField(physicalName = "chatVo")
    public java.util.List<com.demo.proworks.videochat.vo.ChatVo> getChatVo(){
        return chatVo;
    }

    @ElVoField(physicalName = "chatVo")
    public void setChatVo(java.util.List<com.demo.proworks.videochat.vo.ChatVo> chatVo){
        this.chatVo = chatVo;
    }

    @ElVoField(physicalName = "count")
    public int getCount(){
        return count;
    }

    @ElVoField(physicalName = "count")
    public void setCount(int count){
        this.count = count;
    }

    @ElVoField(physicalName = "success")
    public boolean isSuccess(){
        return success;
    }

    @ElVoField(physicalName = "success")
    public void setSuccess(boolean success){
        this.success = success;
    }

    @ElVoField(physicalName = "resultMessage")
    public String getResultMessage(){
        String ret = this.resultMessage;
        return ret;
    }

    @ElVoField(physicalName = "resultMessage")
    public void setResultMessage(String resultMessage){
        this.resultMessage = resultMessage;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ChatListVo [");
        sb.append("chatVo").append("=").append(chatVo).append(",");
        sb.append("count").append("=").append(count).append(",");
        sb.append("success").append("=").append(success).append(",");
        sb.append("resultMessage").append("=").append(resultMessage);
        sb.append("]");
        return sb.toString();

    }

    public boolean isFixedLengthVo() {
        return false;
    }

    @Override
    public void _xStreamEnc() {
        for( int i=0 ; chatVo != null && i < chatVo.size() ; i++ ) {
            com.demo.proworks.videochat.vo.ChatVo vo = (com.demo.proworks.videochat.vo.ChatVo)chatVo.get(i);
            vo._xStreamEnc();	 
        }
    }


    @Override
    public void _xStreamDec() {
        for( int i=0 ; chatVo != null && i < chatVo.size() ; i++ ) {
            com.demo.proworks.videochat.vo.ChatVo vo = (com.demo.proworks.videochat.vo.ChatVo)chatVo.get(i);
            vo._xStreamDec();	 
        }
    }


}
