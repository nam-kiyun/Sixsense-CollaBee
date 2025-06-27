package com.demo.proworks.cmmn;

import com.inswave.elfw.annotation.ElDto;
import com.inswave.elfw.annotation.ElDtoField;

@ElDto(FldYn = "", logicalName = "공통VO")
public class ProworksCommVO extends com.inswave.elfw.core.CommVO {
	private static final long serialVersionUID = 1L;
	
    @ElDtoField(logicalName = "페이지Unit", physicalName = "pageUnit", type = "int", typeKind = "", fldYn = "", length = 0, dotLen = 0, baseValue = "10", desc = "")
    private int pageUnit = 10;

    @ElDtoField(logicalName = "페이지크기", physicalName = "pageSize", type = "int", typeKind = "", fldYn = "", length = 0, dotLen = 0, baseValue = "10", desc = "")
    private int pageSize = 10;

    public int getPageUnit(){
        return pageUnit;
    }

    public void setPageUnit(int pageUnit){
        this.pageUnit = pageUnit;
    }

    public int getPageSize(){
        return pageSize;
    }

    public void setPageSize(int pageSize){
        this.pageSize = pageSize;
    }

    @Override
    public String toString() {
        return "DemoCommVO [pageUnit=" + pageUnit + ",pageSize=" + pageSize + "]";
    }

    public boolean isFixedLengthVo() {
        return false;
    }

}
