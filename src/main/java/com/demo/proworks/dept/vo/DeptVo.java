package com.demo.proworks.dept.vo;

import com.inswave.elfw.annotation.ElDto;
import com.inswave.elfw.annotation.ElDtoField;
import com.inswave.elfw.annotation.ElVoField;
import com.fasterxml.jackson.annotation.JsonFilter;

@JsonFilter("elExcludeFilter")
@ElDto(FldYn = "", delimeterYn = "", logicalName = "부서 정보")
public class DeptVo extends com.demo.proworks.cmmn.ProworksCommVO {
    private static final long serialVersionUID = 1L;

    public DeptVo(){
    }

    @ElDtoField(logicalName = "DEPTNO", physicalName = "deptno", type = "String", typeKind = "", fldYn = "", delimeterYn = "", cryptoGbn = "", cryptoKind = "", length = 0, dotLen = 0, baseValue = "", desc = "", attr = "")
    private String deptno;

    @ElDtoField(logicalName = "DNAME", physicalName = "dname", type = "String", typeKind = "", fldYn = "", delimeterYn = "", cryptoGbn = "", cryptoKind = "", length = 0, dotLen = 0, baseValue = "", desc = "", attr = "")
    private String dname;

    @ElDtoField(logicalName = "LOC", physicalName = "loc", type = "String", typeKind = "", fldYn = "", delimeterYn = "", cryptoGbn = "", cryptoKind = "", length = 0, dotLen = 0, baseValue = "", desc = "", attr = "")
    private String loc;

    @ElDtoField(logicalName = "search_DEPTNO", physicalName = "scDeptno", type = "String", typeKind = "", fldYn = "", delimeterYn = "", cryptoGbn = "", cryptoKind = "", length = 0, dotLen = 0, baseValue = "", desc = "", attr = "")
    private String scDeptno;

    @ElDtoField(logicalName = "search_DNAME", physicalName = "scDname", type = "String", typeKind = "", fldYn = "", delimeterYn = "", cryptoGbn = "", cryptoKind = "", length = 0, dotLen = 0, baseValue = "", desc = "", attr = "")
    private String scDname;

    @ElDtoField(logicalName = "rowStatus", physicalName = "rowStatus", type = "String", typeKind = "", fldYn = "", delimeterYn = "", cryptoGbn = "", cryptoKind = "", length = 0, dotLen = 0, baseValue = "", desc = "", attr = "")
    private String rowStatus;

    @ElVoField(physicalName = "deptno")
    public String getDeptno(){
        String ret = this.deptno;
        return ret;
    }

    @ElVoField(physicalName = "deptno")
    public void setDeptno(String deptno){
        this.deptno = deptno;
    }

    @ElVoField(physicalName = "dname")
    public String getDname(){
        String ret = this.dname;
        return ret;
    }

    @ElVoField(physicalName = "dname")
    public void setDname(String dname){
        this.dname = dname;
    }

    @ElVoField(physicalName = "loc")
    public String getLoc(){
        String ret = this.loc;
        return ret;
    }

    @ElVoField(physicalName = "loc")
    public void setLoc(String loc){
        this.loc = loc;
    }

    @ElVoField(physicalName = "scDeptno")
    public String getScDeptno(){
        String ret = this.scDeptno;
        return ret;
    }

    @ElVoField(physicalName = "scDeptno")
    public void setScDeptno(String scDeptno){
        this.scDeptno = scDeptno;
    }

    @ElVoField(physicalName = "scDname")
    public String getScDname(){
        String ret = this.scDname;
        return ret;
    }

    @ElVoField(physicalName = "scDname")
    public void setScDname(String scDname){
        this.scDname = scDname;
    }

    @ElVoField(physicalName = "rowStatus")
    public String getRowStatus(){
        String ret = this.rowStatus;
        return ret;
    }

    @ElVoField(physicalName = "rowStatus")
    public void setRowStatus(String rowStatus){
        this.rowStatus = rowStatus;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("DeptVo [");
        sb.append("deptno").append("=").append(deptno).append(",");
        sb.append("dname").append("=").append(dname).append(",");
        sb.append("loc").append("=").append(loc).append(",");
        sb.append("scDeptno").append("=").append(scDeptno).append(",");
        sb.append("scDname").append("=").append(scDname).append(",");
        sb.append("rowStatus").append("=").append(rowStatus);
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
