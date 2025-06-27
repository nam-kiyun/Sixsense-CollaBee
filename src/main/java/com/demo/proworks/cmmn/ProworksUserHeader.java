package com.demo.proworks.cmmn;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.inswave.elfw.annotation.ElDto;
import com.inswave.elfw.annotation.ElDtoField;
import com.fasterxml.jackson.annotation.JsonFilter;
import com.inswave.elfw.log.AppLog;

@JsonFilter("elExcludeFilter")
@ElDto(FldYn = "Y", delimeterYn = "", logicalName = "헤더VO")
public class ProworksUserHeader extends com.inswave.elfw.core.UserHeader {
    private static final long serialVersionUID = 1L;

    private int _offset;

    public ProworksUserHeader(){
        this._offset = 0;
    }

    public ProworksUserHeader(int iOffset){
        this._offset = iOffset;
    }

    @ElDtoField(logicalName = "전문길이", physicalName = "fldLen", type = "int", typeKind = "", fldYn = "Yes", delimeterYn = "Yes", cryptoGbn = "", cryptoKind = "", length = 10, dotLen = 0, baseValue = "", desc = "")
    private int fldLen;

    @ElDtoField(logicalName = "서비스ID", physicalName = "svcId", type = "String", typeKind = "", fldYn = "Yes", delimeterYn = "Yes", cryptoGbn = "", cryptoKind = "", length = 20, dotLen = 0, baseValue = "", desc = "")
    private String svcId;

    @ElDtoField(logicalName = "전문입력ID", physicalName = "inInfId", type = "String", typeKind = "", fldYn = "Yes", delimeterYn = "", cryptoGbn = "", cryptoKind = "", length = 30, dotLen = 0, baseValue = "", desc = "")
    private String inInfId;

    @ElDtoField(logicalName = "전문출력ID", physicalName = "outInfId", type = "String", typeKind = "", fldYn = "Yes", delimeterYn = "", cryptoGbn = "", cryptoKind = "", length = 30, dotLen = 0, baseValue = "", desc = "")
    private String outInfId;

    @ElDtoField(logicalName = "성공실패여부", physicalName = "sucYn", type = "String", typeKind = "", fldYn = "Yes", delimeterYn = "Yes", cryptoGbn = "", cryptoKind = "", length = 1, dotLen = 0, baseValue = "", desc = "")
    private String sucYn;

    @ElDtoField(logicalName = "에러코드", physicalName = "errorCode", type = "String", typeKind = "", fldYn = "Yes", delimeterYn = "Yes", cryptoGbn = "", cryptoKind = "", length = 30, dotLen = 0, baseValue = "", desc = "")
    private String errorCode;

    @ElDtoField(logicalName = "에러메시지", physicalName = "errMag", type = "String", typeKind = "", fldYn = "Yes", delimeterYn = "Yes", cryptoGbn = "", cryptoKind = "", length = 100, dotLen = 0, baseValue = "", desc = "")
    private String errMag;

    @ElDtoField(logicalName = "테스트", physicalName = "location", type = "String", typeKind = "", fldYn = "Yes", delimeterYn = "Yes", cryptoGbn = "", cryptoKind = "", length = 10, dotLen = 0, baseValue = "", desc = "")
    private String location;

    @ElDtoField(logicalName = "사용자ID", physicalName = "testId", type = "String", typeKind = "", fldYn = "Yes", delimeterYn = "Yes", cryptoGbn = "", cryptoKind = "", length = 10, dotLen = 0, baseValue = "", desc = "")
    private String testId;

    @ElDtoField(logicalName = "테스트01 카운트", physicalName = "test01Cnt", type = "String", typeKind = "", fldYn = "No", delimeterYn = "", cryptoGbn = "", cryptoKind = "", length = 11, dotLen = 0, baseValue = "", desc = "")
    private String test01Cnt;

    @ElDtoField(logicalName = "테스트01유저헤더", physicalName = "test01UserHeader", type = "", typeKind = "List", fldYn = "No", delimeterYn = "", cryptoGbn = "", cryptoKind = "", length = 0, dotLen = 0, baseValue = "test01Cnt", desc = "")
    private java.util.List<com.demo.proworks.cmmn.Test01UserHeader> test01UserHeader;

    @ElDtoField(logicalName = "테스트 유저헤더 02", physicalName = "test02UserHeader", type = "", typeKind = "List", fldYn = "No", delimeterYn = "", cryptoGbn = "", cryptoKind = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private java.util.List<com.demo.proworks.cmmn.Test02UserHeader> test02UserHeader;

    @ElDtoField(logicalName = "사용자이름", physicalName = "testUserName", type = "String", typeKind = "", fldYn = "", delimeterYn = "", cryptoGbn = "", cryptoKind = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private String testUserName;

    @ElDtoField(logicalName = "부서명", physicalName = "testDeptName", type = "String", typeKind = "", fldYn = "", delimeterYn = "", cryptoGbn = "", cryptoKind = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private String testDeptName;

    @ElDtoField(logicalName = "부서코드", physicalName = "testDeptNo", type = "String", typeKind = "", fldYn = "", delimeterYn = "", cryptoGbn = "", cryptoKind = "", length = 0, dotLen = 0, baseValue = "", desc = "")
    private String testDeptNo;
    
    public int getFldLen(){
        return fldLen;
    }

    public void setFldLen(int fldLen){
        this.fldLen = fldLen;
    }

    public String getSvcId(){
        return svcId;
    }

    public void setSvcId(String svcId){
        this.svcId = svcId;
    }

    public String getInInfId(){
        return inInfId;
    }

    public void setInInfId(String inInfId){
        this.inInfId = inInfId;
    }

    public String getOutInfId(){
        return outInfId;
    }

    public void setOutInfId(String outInfId){
        this.outInfId = outInfId;
    }

    public String getSucYn(){
        return sucYn;
    }

    public void setSucYn(String sucYn){
        this.sucYn = sucYn;
    }

    public String getErrorCode(){
        return errorCode;
    }

    public void setErrorCode(String errorCode){
        this.errorCode = errorCode;
    }

    public String getErrMag(){
        return errMag;
    }

    public void setErrMag(String errMag){
        this.errMag = errMag;
    }

    public String getLocation(){
        return location;
    }

    public void setLocation(String location){
        this.location = location;
    }

    public String getTestId(){
        return testId;
    }

    public void setTestId(String testId){
        this.testId = testId;
    }

    public String getTest01Cnt(){
        return test01Cnt;
    }

    public void setTest01Cnt(String test01Cnt){
        this.test01Cnt = test01Cnt;
    }

    public java.util.List<com.demo.proworks.cmmn.Test01UserHeader> getTest01UserHeader(){
        return test01UserHeader;
    }

    public void setTest01UserHeader(java.util.List<com.demo.proworks.cmmn.Test01UserHeader> test01UserHeader){
        this.test01UserHeader = test01UserHeader;
    }

    public java.util.List<com.demo.proworks.cmmn.Test02UserHeader> getTest02UserHeader(){
        return test02UserHeader;
    }

    public void setTest02UserHeader(java.util.List<com.demo.proworks.cmmn.Test02UserHeader> test02UserHeader){
        this.test02UserHeader = test02UserHeader;
    }

    public String getTestUserName(){
        return testUserName;
    }

    public void setTestUserName(String testUserName){
        this.testUserName = testUserName;
    }

    public String getTestDeptName(){
        return testDeptName;
    }

    public void setTestDeptName(String testDeptName){
        this.testDeptName = testDeptName;
    }

    public String getTestDeptNo(){
        return testDeptNo;
    }

    public void setTestDeptNo(String testDeptNo){
        this.testDeptNo = testDeptNo;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ProworksUserHeader [");
        sb.append("fldLen").append("=").append(fldLen).append(",");
        sb.append("svcId").append("=").append(svcId).append(",");
        sb.append("inInfId").append("=").append(inInfId).append(",");
        sb.append("outInfId").append("=").append(outInfId).append(",");
        sb.append("sucYn").append("=").append(sucYn).append(",");
        sb.append("errorCode").append("=").append(errorCode).append(",");
        sb.append("errMag").append("=").append(errMag).append(",");
        sb.append("location").append("=").append(location).append(",");
        sb.append("testId").append("=").append(testId).append(",");
        sb.append("test01Cnt").append("=").append(test01Cnt).append(",");
        sb.append("test01UserHeader").append("=").append(test01UserHeader).append(",");
        sb.append("test02UserHeader").append("=").append(test02UserHeader).append(",");
        sb.append("testUserName").append("=").append(testUserName).append(",");
        sb.append("testDeptName").append("=").append(testDeptName).append(",");
        sb.append("testDeptNo").append("=").append(testDeptNo);
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
            out.write( com.inswave.elfw.util.TypeConversionUtil.intToBytes(this.fldLen , 10) );
            out.write( com.inswave.elfw.util.TypeConversionUtil.strToSpBytes(this.svcId , 20, encode ) );
            out.write( com.inswave.elfw.util.TypeConversionUtil.strToSpBytes(this.inInfId , 30, encode ) );
            out.write( com.inswave.elfw.util.TypeConversionUtil.strToSpBytes(this.outInfId , 30, encode ) );
            out.write( com.inswave.elfw.util.TypeConversionUtil.strToSpBytes(this.sucYn , 1, encode ) );
            out.write( com.inswave.elfw.util.TypeConversionUtil.strToSpBytes(this.errorCode , 30, encode ) );
            out.write( com.inswave.elfw.util.TypeConversionUtil.strToSpBytes(this.errMag , 100, encode ) );
            out.write( com.inswave.elfw.util.TypeConversionUtil.strToSpBytes(this.location , 10, encode ) );
            out.write( com.inswave.elfw.util.TypeConversionUtil.strToSpBytes(this.testId , 10, encode ) );
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
             this.fldLen = com.inswave.elfw.util.TypeConversionUtil.bytesToInt( bytes, _offset, 10, encode );
             _offset += 10;
            this.svcId = com.inswave.elfw.util.TypeConversionUtil.getTrimmedString( bytes, _offset, 20, encode );
             _offset += 20;
            this.inInfId = com.inswave.elfw.util.TypeConversionUtil.getTrimmedString( bytes, _offset, 30, encode );
             _offset += 30;
            this.outInfId = com.inswave.elfw.util.TypeConversionUtil.getTrimmedString( bytes, _offset, 30, encode );
             _offset += 30;
            this.sucYn = com.inswave.elfw.util.TypeConversionUtil.getTrimmedString( bytes, _offset, 1, encode );
             _offset += 1;
            this.errorCode = com.inswave.elfw.util.TypeConversionUtil.getTrimmedString( bytes, _offset, 30, encode );
             _offset += 30;
            this.errMag = com.inswave.elfw.util.TypeConversionUtil.getTrimmedString( bytes, _offset, 100, encode );
             _offset += 100;
            this.location = com.inswave.elfw.util.TypeConversionUtil.getTrimmedString( bytes, _offset, 10, encode );
             _offset += 10;
            this.testId = com.inswave.elfw.util.TypeConversionUtil.getTrimmedString( bytes, _offset, 10, encode );
             _offset += 10;
        }catch(Exception e) { 
            String errorLine = com.inswave.elfw.util.TypeConversionUtil.getTrimmedString( bytes, 0, bytes.length, encode );
            AppLog.error("unMarshalFld Error:["+ errorLine+"]", e);
            throw e;
        } 
    }

    public int getOffset(){
        return _offset;
    }

    public int getFixedTotalLength(){
        return 241;
    }

    @Override
    public void _xStreamEnc() {
        for( int i=0 ; test01UserHeader != null && i < test01UserHeader.size() ; i++ ) {
            com.demo.proworks.cmmn.Test01UserHeader vo = (com.demo.proworks.cmmn.Test01UserHeader)test01UserHeader.get(i);
            vo._xStreamEnc();	 
        }
        for( int i=0 ; test02UserHeader != null && i < test02UserHeader.size() ; i++ ) {
            com.demo.proworks.cmmn.Test02UserHeader vo = (com.demo.proworks.cmmn.Test02UserHeader)test02UserHeader.get(i);
            vo._xStreamEnc();	 
        }
    }


    @Override
    public void _xStreamDec() {
        for( int i=0 ; test01UserHeader != null && i < test01UserHeader.size() ; i++ ) {
            com.demo.proworks.cmmn.Test01UserHeader vo = (com.demo.proworks.cmmn.Test01UserHeader)test01UserHeader.get(i);
            vo._xStreamDec();	 
        }
        for( int i=0 ; test02UserHeader != null && i < test02UserHeader.size() ; i++ ) {
            com.demo.proworks.cmmn.Test02UserHeader vo = (com.demo.proworks.cmmn.Test02UserHeader)test02UserHeader.get(i);
            vo._xStreamDec();	 
        }
    }


}
