package com.demo.proworks.comment.vo;

import com.inswave.elfw.annotation.ElDto;
import com.inswave.elfw.annotation.ElDtoField;
import com.inswave.elfw.annotation.ElVoField;
import com.fasterxml.jackson.annotation.JsonFilter;

@JsonFilter("elExcludeFilter")
@ElDto(FldYn = "", delimeterYn = "", logicalName = "댓글 리스트")
public class CommentAllListVo extends com.demo.proworks.cmmn.ProworksCommVO {
    private static final long serialVersionUID = 1L;

    public CommentAllListVo(){
    }

    @ElDtoField(logicalName = "댓글정보 전체", physicalName = "commentAllVo", type = "", typeKind = "List", fldYn = "", delimeterYn = "", cryptoGbn = "", cryptoKind = "", length = 0, dotLen = 0, baseValue = "", desc = "", attr = "")
    private java.util.List<com.demo.proworks.comment.vo.CommentAllVo> commentAllVo;

    @ElVoField(physicalName = "commentAllVo")
    public java.util.List<com.demo.proworks.comment.vo.CommentAllVo> getCommentAllVo(){
        return commentAllVo;
    }

    @ElVoField(physicalName = "commentAllVo")
    public void setCommentAllVo(java.util.List<com.demo.proworks.comment.vo.CommentAllVo> commentAllVo){
        this.commentAllVo = commentAllVo;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("CommentAllListVo [");
        sb.append("commentAllVo").append("=").append(commentAllVo);
        sb.append("]");
        return sb.toString();

    }

    public boolean isFixedLengthVo() {
        return false;
    }

    @Override
    public void _xStreamEnc() {
        for( int i=0 ; commentAllVo != null && i < commentAllVo.size() ; i++ ) {
            com.demo.proworks.comment.vo.CommentAllVo vo = (com.demo.proworks.comment.vo.CommentAllVo)commentAllVo.get(i);
            vo._xStreamEnc();	 
        }
    }


    @Override
    public void _xStreamDec() {
        for( int i=0 ; commentAllVo != null && i < commentAllVo.size() ; i++ ) {
            com.demo.proworks.comment.vo.CommentAllVo vo = (com.demo.proworks.comment.vo.CommentAllVo)commentAllVo.get(i);
            vo._xStreamDec();	 
        }
    }


}
