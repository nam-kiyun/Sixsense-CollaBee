package com.demo.proworks.comment.vo;

import com.inswave.elfw.annotation.ElDto;
import com.inswave.elfw.annotation.ElDtoField;
import com.inswave.elfw.annotation.ElVoField;
import com.fasterxml.jackson.annotation.JsonFilter;

@JsonFilter("elExcludeFilter")
@ElDto(FldYn = "", delimeterYn = "", logicalName = "댓글 조회 사용 Vo")
public class CommentListSearchVo extends com.demo.proworks.cmmn.ProworksCommVO {
    private static final long serialVersionUID = 1L;

    public CommentListSearchVo(){
    }

    @ElDtoField(logicalName = "task_id", physicalName = "taskId", type = "int", typeKind = "", fldYn = "", delimeterYn = "", cryptoGbn = "", cryptoKind = "", length = 0, dotLen = 0, baseValue = "", desc = "", attr = "")
    private int taskId;

    @ElDtoField(logicalName = "page", physicalName = "page", type = "int", typeKind = "", fldYn = "", delimeterYn = "", cryptoGbn = "", cryptoKind = "", length = 0, dotLen = 0, baseValue = "", desc = "", attr = "")
    private int page;

    @ElDtoField(logicalName = "pageSize", physicalName = "pageSize", type = "int", typeKind = "", fldYn = "", delimeterYn = "", cryptoGbn = "", cryptoKind = "", length = 0, dotLen = 0, baseValue = "", desc = "", attr = "")
    private int pageSize;

    @ElDtoField(logicalName = "startOffset", physicalName = "startOffset", type = "int", typeKind = "", fldYn = "", delimeterYn = "", cryptoGbn = "", cryptoKind = "", length = 0, dotLen = 0, baseValue = "", desc = "", attr = "")
    private int startOffset;

    @ElVoField(physicalName = "taskId")
    public int getTaskId(){
        return taskId;
    }

    @ElVoField(physicalName = "taskId")
    public void setTaskId(int taskId){
        this.taskId = taskId;
    }

    @ElVoField(physicalName = "page")
    public int getPage(){
        return page;
    }

    @ElVoField(physicalName = "page")
    public void setPage(int page){
        this.page = page;
    }

    @ElVoField(physicalName = "pageSize")
    public int getPageSize(){
        return pageSize;
    }

    @ElVoField(physicalName = "pageSize")
    public void setPageSize(int pageSize){
        this.pageSize = pageSize;
    }

    @ElVoField(physicalName = "startOffset")
    public int getStartOffset(){
        return startOffset;
    }

    @ElVoField(physicalName = "startOffset")
    public void setStartOffset(int startOffset){
        this.startOffset = startOffset;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("CommentListSearchVo [");
        sb.append("taskId").append("=").append(taskId).append(",");
        sb.append("page").append("=").append(page).append(",");
        sb.append("pageSize").append("=").append(pageSize).append(",");
        sb.append("startOffset").append("=").append(startOffset);
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
