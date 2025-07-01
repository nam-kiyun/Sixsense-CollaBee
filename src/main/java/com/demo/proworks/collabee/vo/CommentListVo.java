package com.demo.proworks.collabee.vo;

import com.inswave.elfw.annotation.ElDto;
import com.inswave.elfw.annotation.ElDtoField;
import com.fasterxml.jackson.annotation.JsonFilter;

@JsonFilter("elExcludeFilter")
@ElDto(FldYn = "", logicalName = "Task(업무)에 달리는 댓글 (채팅)")
public class CommentListVo extends com.demo.proworks.cmmn.ProworksCommVO {
    private static final long serialVersionUID = 1L;

    @ElDtoField(logicalName = "Task(업무)에 달리는 댓글 (채팅)List", physicalName = "commentVoList", type = "com.demo.proworks.collabee.CommentVo", typeKind = "List", fldYn = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private java.util.List<com.demo.proworks.collabee.vo.CommentVo> commentVoList;

    public java.util.List<com.demo.proworks.collabee.vo.CommentVo> getCommentVoList(){
        return commentVoList;
    }

    public void setCommentVoList(java.util.List<com.demo.proworks.collabee.vo.CommentVo> commentVoList){
        this.commentVoList = commentVoList;
    }

    @Override
    public String toString() {
        return "CommentListVo [commentVoList=" + commentVoList+ "]";
    }

    public boolean isFixedLengthVo() {
        return false;
    }

}
