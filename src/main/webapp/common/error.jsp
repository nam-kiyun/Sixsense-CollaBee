<%@page import="com.inswave.elfw.exception.ElException"%>
<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="com.inswave.elfw.log.*"%>
<%@ page import="java.io.*"%>
<%@ page isErrorPage="true" %>

<%
/*
try{
	com.inswave.elfw.core.ElHeader elHeader = (com.inswave.elfw.core.ElHeader)request.getAttribute("elHeader");
	System.out.println("---elHeader:::" + elHeader);
}catch(Exception e){
	
}
*/

out.println("- response code : "+response.getStatus() );


StringWriter errors = new StringWriter();
if( exception != null ) {
	exception.printStackTrace(new PrintWriter(errors));

	AppLog.error(errors.toString());
	
	if( exception instanceof com.inswave.elfw.exception.ElException){
		ElException elException = (ElException)exception;
		out.println("- errCode : " + elException.getMessageKey() + "</br>");
		out.println("- errMsg : " + elException.getMessage());
	} else if( exception instanceof Exception){
		out.println("- exception : " + exception.toString() );
	}else {
		out.println("- etc error : !!! " );
	}
} else {
	out.println("error....");
}


%>

