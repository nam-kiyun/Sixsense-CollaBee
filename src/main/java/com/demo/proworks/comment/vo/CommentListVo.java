package com.demo.proworks.comment.vo;

import com.inswave.elfw.annotation.ElDto;
import com.inswave.elfw.annotation.ElDtoField;
import com.fasterxml.jackson.annotation.JsonFilter;

@JsonFilter("elExcludeFilter")
@ElDto(FldYn = "", logicalName = "댓글정보")
public class CommentListVo extends com.demo.proworks.cmmn.ProworksCommVO {
    private static final long serialVersionUID = 1L;

    @ElDtoField(logicalName = "댓글정보List", physicalName = "commentVoList", type = "com.demo.proworks.comment.CommentVo", typeKind = "List", fldYn = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private java.util.List<com.demo.proworks.comment.vo.CommentVo> commentVoList;

    public java.util.List<com.demo.proworks.comment.vo.CommentVo> getCommentVoList(){
        return commentVoList;
    }

    public void setCommentVoList(java.util.List<com.demo.proworks.comment.vo.CommentVo> commentVoList){
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
