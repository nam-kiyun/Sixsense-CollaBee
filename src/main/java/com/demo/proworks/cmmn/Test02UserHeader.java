package com.demo.proworks.cmmn;

import com.inswave.elfw.annotation.ElDto;
import com.inswave.elfw.annotation.ElDtoField;
import com.fasterxml.jackson.annotation.JsonFilter;

@JsonFilter("elExcludeFilter")
@ElDto(FldYn = "", delimeterYn = "", logicalName = "테스트 유저헤더 02")
public class Test02UserHeader extends com.inswave.elfw.core.UserHeader {
    private static final long serialVersionUID = 1L;

    public Test02UserHeader(){
    }

    @ElDtoField(logicalName = "부서번호", physicalName = "deptno", type = "String", typeKind = "", fldYn = "No", delimeterYn = "", cryptoGbn = "", cryptoKind = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private String deptno;

    @ElDtoField(logicalName = "부서명", physicalName = "dname", type = "String", typeKind = "", fldYn = "No", delimeterYn = "", cryptoGbn = "", cryptoKind = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private String dname;

    @ElDtoField(logicalName = "지역", physicalName = "loc", type = "String", typeKind = "", fldYn = "No", delimeterYn = "", cryptoGbn = "", cryptoKind = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private String loc;

    public String getDeptno(){
        return deptno;
    }

    public void setDeptno(String deptno){
        this.deptno = deptno;
    }

    public String getDname(){
        return dname;
    }

    public void setDname(String dname){
        this.dname = dname;
    }

    public String getLoc(){
        return loc;
    }

    public void setLoc(String loc){
        this.loc = loc;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Test02UserHeader [");
        sb.append("deptno").append("=").append(deptno).append(",");
        sb.append("dname").append("=").append(dname).append(",");
        sb.append("loc").append("=").append(loc);
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
