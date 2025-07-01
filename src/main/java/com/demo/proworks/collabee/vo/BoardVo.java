package com.demo.proworks.collabee.vo;

import com.inswave.elfw.annotation.ElDto;
import com.inswave.elfw.annotation.ElDtoField;
import com.inswave.elfw.annotation.ElVoField;
import com.fasterxml.jackson.annotation.JsonFilter;

@JsonFilter("elExcludeFilter")
@ElDto(FldYn = "", logicalName = "칸반보드의 보드")
public class BoardVo extends com.demo.proworks.cmmn.ProworksCommVO {
    private static final long serialVersionUID = 1L;

    @ElDtoField(logicalName = "board_id", physicalName = "boardId", type = "String", typeKind = "", fldYn = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private String boardId;

    @ElDtoField(logicalName = "project_id", physicalName = "projectId", type = "String", typeKind = "", fldYn = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private String projectId;

    @ElDtoField(logicalName = "board_title", physicalName = "boardTitle", type = "String", typeKind = "", fldYn = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private String boardTitle;

    @ElVoField(physicalName = "boardId")
    public String getBoardId(){
        return boardId;
    }

    @ElVoField(physicalName = "boardId")
    public void setBoardId(String boardId){
        this.boardId = boardId;
    }

    @ElVoField(physicalName = "projectId")
    public String getProjectId(){
        return projectId;
    }

    @ElVoField(physicalName = "projectId")
    public void setProjectId(String projectId){
        this.projectId = projectId;
    }

    @ElVoField(physicalName = "boardTitle")
    public String getBoardTitle(){
        return boardTitle;
    }

    @ElVoField(physicalName = "boardTitle")
    public void setBoardTitle(String boardTitle){
        this.boardTitle = boardTitle;
    }

    @Override
    public String toString() {
        return "BoardVo [boardId=" + boardId + ",projectId=" + projectId + ",boardTitle=" + boardTitle + "]";
    }

    public boolean isFixedLengthVo() {
        return false;
    }

}
