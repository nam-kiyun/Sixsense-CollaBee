package com.demo.proworks.board.vo;

import com.inswave.elfw.annotation.ElDto;
import com.inswave.elfw.annotation.ElDtoField;
import com.fasterxml.jackson.annotation.JsonFilter;

@JsonFilter("elExcludeFilter")
@ElDto(FldYn = "", logicalName = "보드")
public class BoardListVo extends com.demo.proworks.cmmn.ProworksCommVO {
    private static final long serialVersionUID = 1L;

    @ElDtoField(logicalName = "보드List", physicalName = "boardVoList", type = "com.demo.proworks.board.BoardVo", typeKind = "List", fldYn = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private java.util.List<com.demo.proworks.board.vo.BoardVo> boardVoList;

    public java.util.List<com.demo.proworks.board.vo.BoardVo> getBoardVoList(){
        return boardVoList;
    }

    public void setBoardVoList(java.util.List<com.demo.proworks.board.vo.BoardVo> boardVoList){
        this.boardVoList = boardVoList;
    }

    @Override
    public String toString() {
        return "BoardListVo [boardVoList=" + boardVoList+ "]";
    }

    public boolean isFixedLengthVo() {
        return false;
    }

}
