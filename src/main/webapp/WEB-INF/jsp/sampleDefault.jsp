<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
com.inswave.elfw.core.ElHeader elHeader = (com.inswave.elfw.core.ElHeader)request.getAttribute("elHeader");
com.inswave.elfw.core.UserHeader userHeader = (com.inswave.elfw.core.UserHeader)request.getAttribute("userHeader");
Object elData = request.getAttribute("elData");
java.util.Map<String,Object> mpModel = new java.util.HashMap<String, Object>();
String reqUrl = elHeader.getReqUrl();

if( reqUrl.endsWith(".do") && elData != null && elData instanceof com.inswave.elfw.core.CommVO) {  // 요청 Url 이 .do 이고  응답 데이터가 VO 타입인 경우 
	com.inswave.elfw.core.CommVO commVo = (com.inswave.elfw.core.CommVO)elData;

	mpModel.put(com.inswave.elfw.ElConstants.EL_HEADER_STRING, elHeader);
	mpModel.put(com.inswave.elfw.ElConstants.USER_HEADER_STRING, userHeader);
	mpModel.put(com.inswave.elfw.ElConstants.EL_DATA_STRING, commVo);

	try{
		String resEncode = "EUC-KR";  // 사용 인코딩 설정 
		
		com.inswave.elfw.view.ElMappingJacksonObjectMapper elJacksonOm 
		  = (com.inswave.elfw.view.ElMappingJacksonObjectMapper)com.inswave.elfw.util.ElBeanUtils.getBean("jsonMapper");
		String outJson = elJacksonOm.writeValueAsString(mpModel); // json 변환 
		
		out.println(outJson);

    } catch(Exception e){
    	com.inswave.elfw.log.AppLog.error("err",e);
        //TODO : 에러 발생에 대한 처리 -> 필요시 업무요건 적용
       	
    } 
} else if ( reqUrl.endsWith(".do") && elData != null && elData instanceof java.util.HashMap ) {
	
	mpModel.put(com.inswave.elfw.ElConstants.EL_HEADER_STRING, elHeader);
	mpModel.put(com.inswave.elfw.ElConstants.USER_HEADER_STRING, userHeader);
	mpModel.put(com.inswave.elfw.ElConstants.EL_DATA_STRING, elData);
	
	try{
		String resEncode = "EUC-KR";  // 사용 인코딩 설정 
		
		com.inswave.elfw.view.ElMappingJacksonObjectMapper elJacksonOm 
		  = (com.inswave.elfw.view.ElMappingJacksonObjectMapper)com.inswave.elfw.util.ElBeanUtils.getBean("jsonMapper");
		String outJson = elJacksonOm.writeValueAsString(mpModel); // json 변환 
		
		out.println(outJson);

    } catch(Exception e){
    	com.inswave.elfw.log.AppLog.error("err",e);
        //TODO : 에러 발생에 대한 처리 -> 필요시 업무요건 적용
       	
    } 
} else {
	//TODO: 업무 요건에 맞게 구성 가능 
}
%>
%>