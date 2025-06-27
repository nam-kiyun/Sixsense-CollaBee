package com.demo.proworks.cmmn;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import com.inswave.elfw.log.AppLog;
import com.inswave.elfw.tcp.TcpSocketServerThreadAdapter;
import com.inswave.elfw.util.ElTcpUtil;
import com.inswave.elfw.util.PropUtil;
import com.inswave.elfw.util.TypeConversionUtil;

public class ProworksTcpSocketServerThreadAdapter extends TcpSocketServerThreadAdapter {
 	 
	InputStream inputStream = null;
	OutputStream outputStream = null; 
	
	private String DATA_ENC = "EUC-KR";
	private String CONTEXT_PATH_URL = PropUtil.getMessage("EL_HTTP_CONTEXT_PATH_BASE_URL"); // "http://localhost:9002/InsWebApp/";
	final private int DATA_LEN_FIELD_LEN = 10; // 전문길이 필드 길이
	final private int READ_DATA_LEN = 1024; // 힌번에 읽을 길이 

	public ProworksTcpSocketServerThreadAdapter(Socket s){
        super(s);
    }
    
    public void proxy(InputStream inputStream, OutputStream outputStream) throws Exception{

		byte[] resData = null;     // 서버소켓 응답전문

		try {
		
			if (getSocket().isConnected()) { // 수신 대기한다.
	
				// 아래 코드트 샘플 예시이므로 프로젝트 상황에 맞게 작성하여야 한다.
			
				byte[] bLen = new byte[DATA_LEN_FIELD_LEN];
				byte[] bReq = null;
				int dataLen = 0;
				if( inputStream.read(bLen) != -1 ) {
					dataLen = com.inswave.elfw.util.TypeConversionUtil.bytesToInt( bLen, 0, DATA_LEN_FIELD_LEN );							
					bReq = new byte[dataLen];
				}	
				
				if( dataLen != 0 ) {
					//dataLen = dataLen - DATA_LEN_FIELD_LEN; // 길이필드 전체길이에 포함하는 경우
					
					AppLog.debug("- Tcp Read Len :: " + dataLen );
					bReq = ElTcpUtil.getStreamBytesFixLen(inputStream, READ_DATA_LEN, dataLen); 

					byte[] bReqTot = new byte[bLen.length + bReq.length];
					System.arraycopy(bLen, 0, bReqTot, 0, bLen.length);
					System.arraycopy(bReq, 0, bReqTot, bLen.length, bReq.length);
					
					AppLog.debug("- Req Data["+ new String(bReqTot,DATA_ENC) +"]");
				
					String serviceId =  TypeConversionUtil.getTrimmedString(bReqTot, 10, 20);
				    serviceId = serviceId.trim();
				    String sendUrl = CONTEXT_PATH_URL + serviceId + ".fld"; // serviceId =>> ex) DmoEmpUpdView.fld
							
					resData = sendPostUrl(sendUrl, bReqTot, "application/fld");
		     
					outputStream.write(resData);
					outputStream.flush();	
				
					AppLog.debug("- Res Data["+ new String(resData,DATA_ENC) +"]");					
				}
								
			}                 
        } catch (Exception e) {
        	AppLog.error("- TcpSocketServerThread-proxy Error", e);
        	e.printStackTrace();
        } 

    }
    
}
