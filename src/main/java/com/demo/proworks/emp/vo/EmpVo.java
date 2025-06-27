package com.demo.proworks.emp.vo;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.inswave.elfw.annotation.ElDto;
import com.inswave.elfw.annotation.ElDtoField;
import com.fasterxml.jackson.annotation.JsonFilter;
import com.inswave.elfw.log.AppLog;

@JsonFilter("elExcludeFilter")
@ElDto(FldYn = "Y", delimeterYn = "", logicalName = "데모사원정보")
public class EmpVo extends com.demo.proworks.cmmn.ProworksCommVO {
    private static final long serialVersionUID = 1L;

    private int _offset;

    public EmpVo(){
        this._offset = 0;
    }

    public EmpVo(int iOffset){
        this._offset = iOffset;
    }

    @ElDtoField(logicalName = "사원번호", physicalName = "empno", type = "int", typeKind = "", fldYn = "Yes", delimeterYn = "Yes", cryptoGbn = "", cryptoKind = "", length = 4, dotLen = 0, baseValue = "", desc = "사원번호 설명")
    private int empno;

    @ElDtoField(logicalName = "사원명", physicalName = "ename", type = "String", typeKind = "", fldYn = "Yes", delimeterYn = "Yes", cryptoGbn = "", cryptoKind = "", length = 10, dotLen = 0, baseValue = "", desc = "")
    private String ename;

    @ElDtoField(logicalName = "직업", physicalName = "job", type = "String", typeKind = "", fldYn = "Yes", delimeterYn = "Yes", cryptoGbn = "", cryptoKind = "", length = 9, dotLen = 0, baseValue = "", desc = "")
    private String job;

    @ElDtoField(logicalName = "직속상사번호", physicalName = "mgr", type = "int", typeKind = "", fldYn = "Yes", delimeterYn = "Yes", cryptoGbn = "", cryptoKind = "", length = 4, dotLen = 0, baseValue = "", desc = "")
    private int mgr;

    @ElDtoField(logicalName = "입사일", physicalName = "hiredate", type = "String", typeKind = "", fldYn = "Yes", delimeterYn = "Yes", cryptoGbn = "", cryptoKind = "", length = 10, dotLen = 0, baseValue = "", desc = "")
    private String hiredate;

    @ElDtoField(logicalName = "급여", physicalName = "sal", type = "double", typeKind = "", fldYn = "Yes", delimeterYn = "Yes", cryptoGbn = "", cryptoKind = "", length = 7, dotLen = 2, baseValue = "", desc = "")
    private double sal;

    @ElDtoField(logicalName = "상여", physicalName = "comm", type = "double", typeKind = "", fldYn = "Yes", delimeterYn = "Yes", cryptoGbn = "", cryptoKind = "", length = 7, dotLen = 2, baseValue = "", desc = "")
    private double comm;

    @ElDtoField(logicalName = "부서번호", physicalName = "deptno", type = "String", typeKind = "", fldYn = "Yes", delimeterYn = "Yes", cryptoGbn = "", cryptoKind = "", length = 2, dotLen = 0, baseValue = "", desc = "")
    private String deptno;

    @ElDtoField(logicalName = "부서명", physicalName = "dname", type = "String", typeKind = "", fldYn = "Yes", delimeterYn = "Yes", cryptoGbn = "", cryptoKind = "", length = 14, dotLen = 0, baseValue = "", desc = "")
    private String dname;

    @ElDtoField(logicalName = "직속상사명", physicalName = "mgrname", type = "String", typeKind = "", fldYn = "No", delimeterYn = "", cryptoGbn = "", cryptoKind = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private String mgrname;

    @ElDtoField(logicalName = "사원설명", physicalName = "account", type = "String", typeKind = "", fldYn = "No", delimeterYn = "", cryptoGbn = "", cryptoKind = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private String account;

    @ElDtoField(logicalName = "컬럼상태", physicalName = "rowStatus", type = "String", typeKind = "", fldYn = "No", delimeterYn = "", cryptoGbn = "", cryptoKind = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private String rowStatus;

    @ElDtoField(logicalName = "검색_사원번호", physicalName = "scEmpno", type = "String", typeKind = "", fldYn = "No", delimeterYn = "", cryptoGbn = "", cryptoKind = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private String scEmpno;

    @ElDtoField(logicalName = "검색_사원명", physicalName = "scEname", type = "String", typeKind = "", fldYn = "No", delimeterYn = "", cryptoGbn = "", cryptoKind = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private String scEname;

    @ElDtoField(logicalName = "검색_직책", physicalName = "scJob", type = "String", typeKind = "", fldYn = "No", delimeterYn = "", cryptoGbn = "", cryptoKind = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private String scJob;

    @ElDtoField(logicalName = "검색_부서코드", physicalName = "scDeptno", type = "String", typeKind = "", fldYn = "No", delimeterYn = "", cryptoGbn = "", cryptoKind = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private String scDeptno;

    @ElDtoField(logicalName = "검색_부서명", physicalName = "scDname", type = "String", typeKind = "", fldYn = "No", delimeterYn = "", cryptoGbn = "", cryptoKind = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private String scDname;

    @ElDtoField(logicalName = "파일명", physicalName = "fileName", type = "String", typeKind = "", fldYn = "No", delimeterYn = "", cryptoGbn = "", cryptoKind = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private String fileName;

    @ElDtoField(logicalName = "파일데이터", physicalName = "fileData", type = "byte[]", typeKind = "", fldYn = "No", delimeterYn = "", cryptoGbn = "", cryptoKind = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private byte[] fileData;

    @ElDtoField(logicalName = "멀티파트파일", physicalName = "inputFileData", type = "MultipartFile", typeKind = "", fldYn = "", delimeterYn = "", cryptoGbn = "", cryptoKind = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private org.springframework.web.multipart.MultipartFile inputFileData;

    public int getEmpno(){
        return empno;
    }

    public void setEmpno(int empno){
        this.empno = empno;
    }

    public String getEname(){
        return ename;
    }

    public void setEname(String ename){
        this.ename = ename;
    }

    public String getJob(){
        return job;
    }

    public void setJob(String job){
        this.job = job;
    }

    public int getMgr(){
        return mgr;
    }

    public void setMgr(int mgr){
        this.mgr = mgr;
    }

    public String getHiredate(){
        return hiredate;
    }

    public void setHiredate(String hiredate){
        this.hiredate = hiredate;
    }

    public double getSal(){
        return sal;
    }

    public void setSal(double sal){
        this.sal = sal;
    }

    public double getComm(){
        return comm;
    }

    public void setComm(double comm){
        this.comm = comm;
    }

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

    public String getMgrname(){
        return mgrname;
    }

    public void setMgrname(String mgrname){
        this.mgrname = mgrname;
    }

    public String getAccount(){
        return account;
    }

    public void setAccount(String account){
        this.account = account;
    }

    public String getRowStatus(){
        return rowStatus;
    }

    public void setRowStatus(String rowStatus){
        this.rowStatus = rowStatus;
    }

    public String getScEmpno(){
        return scEmpno;
    }

    public void setScEmpno(String scEmpno){
        this.scEmpno = scEmpno;
    }

    public String getScEname(){
        return scEname;
    }

    public void setScEname(String scEname){
        this.scEname = scEname;
    }

    public String getScJob(){
        return scJob;
    }

    public void setScJob(String scJob){
        this.scJob = scJob;
    }

    public String getScDeptno(){
        return scDeptno;
    }

    public void setScDeptno(String scDeptno){
        this.scDeptno = scDeptno;
    }

    public String getScDname(){
        return scDname;
    }

    public void setScDname(String scDname){
        this.scDname = scDname;
    }

    public String getFileName(){
        return fileName;
    }

    public void setFileName(String fileName){
        this.fileName = fileName;
    }

    public byte[] getFileData(){
        return fileData;
    }

    public void setFileData(byte[] fileData){
        this.fileData = fileData;
    }

    public org.springframework.web.multipart.MultipartFile getInputFileData(){
        return inputFileData;
    }

    public void setInputFileData(org.springframework.web.multipart.MultipartFile inputFileData){
        this.inputFileData = inputFileData;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("EmpVo [");
        sb.append("empno").append("=").append(empno).append(",");
        sb.append("ename").append("=").append(ename).append(",");
        sb.append("job").append("=").append(job).append(",");
        sb.append("mgr").append("=").append(mgr).append(",");
        sb.append("hiredate").append("=").append(hiredate).append(",");
        sb.append("sal").append("=").append(sal).append(",");
        sb.append("comm").append("=").append(comm).append(",");
        sb.append("deptno").append("=").append(deptno).append(",");
        sb.append("dname").append("=").append(dname).append(",");
        sb.append("mgrname").append("=").append(mgrname).append(",");
        sb.append("account").append("=").append(account).append(",");
        sb.append("rowStatus").append("=").append(rowStatus).append(",");
        sb.append("scEmpno").append("=").append(scEmpno).append(",");
        sb.append("scEname").append("=").append(scEname).append(",");
        sb.append("scJob").append("=").append(scJob).append(",");
        sb.append("scDeptno").append("=").append(scDeptno).append(",");
        sb.append("scDname").append("=").append(scDname).append(",");
        sb.append("fileName").append("=").append(fileName).append(",");
        sb.append("fileData").append("=").append(fileData).append(",");
        sb.append("inputFileData").append("=").append(inputFileData);
        sb.append("]");
        return sb.toString();

    }

    public boolean isFixedLengthVo() {
        return true;
    }

    public byte[] marshalFld() throws Exception{
        return marshalFld( com.inswave.elfw.ElConfig.getFldEncode() ); 
    }

	public byte[] marshalFld(String encode) throws Exception{
    	ByteArrayOutputStream bout = new ByteArrayOutputStream();
        DataOutputStream out = null;
        try {
            out = new DataOutputStream(bout);
            out.write( com.inswave.elfw.util.TypeConversionUtil.intToBytes(this.empno , 4) );
            out.write( com.inswave.elfw.util.TypeConversionUtil.strToSpBytes(this.ename , 10, encode ) );
            out.write( com.inswave.elfw.util.TypeConversionUtil.strToSpBytes(this.job , 9, encode ) );
            out.write( com.inswave.elfw.util.TypeConversionUtil.intToBytes(this.mgr , 4) );
            out.write( com.inswave.elfw.util.TypeConversionUtil.strToSpBytes(this.hiredate , 10, encode ) );
            out.write( com.inswave.elfw.util.TypeConversionUtil.doubleToBytes(this.sal , 7,2) );
            out.write( com.inswave.elfw.util.TypeConversionUtil.doubleToBytes(this.comm , 7,2) );
            out.write( com.inswave.elfw.util.TypeConversionUtil.strToSpBytes(this.deptno , 2, encode ) );
            out.write( com.inswave.elfw.util.TypeConversionUtil.strToSpBytes(this.dname , 14, encode ) );
        } catch (Exception e) {
                AppLog.error("marshalFld Error:["+ toString()+"]", e);
                throw e;
        } finally {
            try	{
                if (out != null) out.close();
           } catch (IOException ie) {
                AppLog.error("marshalFld out close Error", ie);
           }
            try	{
                if (bout != null) bout.close();
           } catch (IOException ie) {
                AppLog.error("marshalFld bout close Error", ie);
           }
        }
        return bout.toByteArray();
    }

    public void unMarshalFld( byte[] bytes ) throws Exception{
        unMarshalFld( bytes, com.inswave.elfw.ElConfig.getFldEncode() ); 
    }

    public void unMarshalFld( byte[] bytes , String encode) throws Exception{
        try{ 
             this.empno = com.inswave.elfw.util.TypeConversionUtil.bytesToInt( bytes, _offset, 4, encode );
             _offset += 4;
            this.ename = com.inswave.elfw.util.TypeConversionUtil.getTrimmedString( bytes, _offset, 10, encode );
             _offset += 10;
            this.job = com.inswave.elfw.util.TypeConversionUtil.getTrimmedString( bytes, _offset, 9, encode );
             _offset += 9;
             this.mgr = com.inswave.elfw.util.TypeConversionUtil.bytesToInt( bytes, _offset, 4, encode );
             _offset += 4;
            this.hiredate = com.inswave.elfw.util.TypeConversionUtil.getTrimmedString( bytes, _offset, 10, encode );
             _offset += 10;
             this.sal = com.inswave.elfw.util.TypeConversionUtil.bytesToDouble( bytes, _offset, 7, encode );
             _offset += 7;
             this.comm = com.inswave.elfw.util.TypeConversionUtil.bytesToDouble( bytes, _offset, 7, encode );
             _offset += 7;
            this.deptno = com.inswave.elfw.util.TypeConversionUtil.getTrimmedString( bytes, _offset, 2, encode );
             _offset += 2;
            this.dname = com.inswave.elfw.util.TypeConversionUtil.getTrimmedString( bytes, _offset, 14, encode );
             _offset += 14;
        }catch(Exception e) { 
            String errorLine = com.inswave.elfw.util.TypeConversionUtil.getTrimmedString( bytes, 0, bytes.length, encode );
            AppLog.error("unMarshalFld Error:["+ errorLine+"]", e);
            throw e;
        } 
    }

    public int getOffset(){
        return _offset;
    }

    @Override
    public void _xStreamEnc() {
    }


    @Override
    public void _xStreamDec() {
    }


}
