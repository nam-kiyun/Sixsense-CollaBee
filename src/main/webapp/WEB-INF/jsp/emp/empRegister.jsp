<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@page import="com.inswave.elfw.util.*"%>
<%@page import="com.demo.proworks.emp.vo.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="validator" uri="http://www.springmodules.org/tags/commons-validator" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<%
    java.util.Enumeration eAttr = request.getAttributeNames();
while (eAttr.hasMoreElements()) {
    String aName = (String)eAttr.nextElement();
    Object obj = request.getAttribute(aName);    
    //out.println(aName + " : " + obj.toString() + "<br>");    
}

/**
* @Class Name : empRegister.jsp
* @Description : 사원 상세정보 처리 화면
* @Modification Information
* 
*   수정일         수정자                   수정내용
*  -------    --------    ---------------------------
*  2022.06.01            최초 생성
*
* author 실행환경 개발팀
* since 2022.06.01
*  
*/
%>


<%
    /* 공통정보 */
com.inswave.elfw.core.ElHeader  elHeader = (com.inswave.elfw.core.ElHeader)request.getAttribute("elHeader");
boolean resSuc = elHeader.isResSuc();
String resCode = elHeader.getResCode();
String resMsg  = elHeader.getResMsg(); 
java.util.HashMap<String,String> errorMap  = elHeader.getResValidateErr();

EmpListVo empListVo = (EmpListVo)request.getAttribute("empListVo");

/* 업무정보 */
EmpVo empVo = empListVo.getEmpVo();
if(empVo==null) empVo = new EmpVo();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>사원정보 <c:out value="${registerFlag}"/> </title>
<link type="text/css" rel="stylesheet" href="<c:url value='/css/ElDemo.css'/>"/>
<script type="text/javascript" src="<c:url value='/js/comm/ElComm.js'/>"></script>

<script type="text/javaScript" language="javascript" defer="defer">
<!--
/* 글 목록 화면 function */
function fn_emp_selectList() {
	location.href = "html/emp/empList.html";
}


function fn_downEmpFile(){
	document.detailForm.action = "<c:url value='/DmoEmpDown.do'/>";
   	document.detailForm.submit();		
}

-->
</script>
</head>
<body style="text-align:center; margin:0 auto; display:inline; padding-top:100px;">


<form name="detailForm" method="post"  enctype="multipart/form-data">
<div id="content_pop">
	<!-- 타이틀 -->
	<div id="title">
		<ul>
			<li><img src="<c:url value='/images/sample/rte/title_dot.gif'/>"> 사원상세 정보</li>
		</ul>
	</div>

	<!-- // 타이틀 -->
	<div id="table">	
	<table width="100%" border="1" cellpadding="0" cellspacing="0" bordercolor="#D3E2EC" bordercolordark="#FFFFFF" style="BORDER-TOP:#C2D0DB 2px solid; BORDER-LEFT:#ffffff 1px solid; BORDER-RIGHT:#ffffff 1px solid; BORDER-BOTTOM:#C2D0DB 1px solid; border-collapse: collapse;">
		<colgroup>
			<col width="150">
			<col width="">
		</colgroup>

		<tr>
			<td class="tbtd_caption">* 사번</td>
			<td class="tbtd_content" colspan='2'>
					<input type='text' name="empno" class="essentiality" readOnly="true" maxlength="4" value="<%=empVo.getEmpno()%>"/>
			</td>			
		</tr>
		 
		<tr>
			<td class="tbtd_caption">* 성명</td>
			<td class="tbtd_content" colspan='2'>
				<input type='text' name="ename" maxlength="10" class="txt" value="<%=empVo.getEname()%>"/>
			</td>
		</tr>
		<tr>
			<td class="tbtd_caption">* 직업</td>
			<td class="tbtd_content" colspan='2'>
				<input type='text' name="job" maxlength="10" class="txt" value="<%=empVo.getJob()%>"/>
			</td>
		</tr>	
		<tr>
			<td class="tbtd_caption">* 직속상관</td>
			<td class="tbtd_content" colspan='2'>
				<input type='text' name="mgr" maxlength="4" class="txt" value="<%=empVo.getMgr()%>"/>					
			</td>
		</tr>	
		
		<tr>
			<td class="tbtd_caption">입사일</td>
			<td class="tbtd_content" colspan='2'>
				<input type='text' name="hiredate" maxlength="20" class="txt" value="<%=empVo.getHiredate()%>"/>	
							
			</td>
		</tr>
		
		<tr>
			<td class="tbtd_caption">* 급여</td>
			<td class="tbtd_content" colspan='2'>
				<input type='text' name="sal" maxlength="10" class="txt" value="<%=empVo.getSal()%>"/>								
			</td>
		</tr>	
		
		<tr>
			<td class="tbtd_caption">상여</td>
			<td class="tbtd_content" colspan='2'>
				<input type='text' name="comm" maxlength="10" class="txt" value="<%=empVo.getComm()%>"/>				
			</td>
		</tr>						
		
		<tr>
			<td class="tbtd_caption">* 부서코드</td>
			<td class="tbtd_content" colspan='2'>	

				<select name="deptno" class="use">
					<option value="" selected>-전체-</option>
					<%
					java.util.List<DeptVo> listDeptVo = empListVo.getDeptVoList();
					if( listDeptVo != null ) {
						for( DeptVo deptVo: listDeptVo){
							String selected = "";
							if( empVo.getDeptno() != null && deptVo.getDeptno() != null && empVo.getDeptno().equals(deptVo.getDeptno())) {
								selected = " selected";
							}
						%>
							<option value="<%=deptVo.getDeptno()%>"<%=selected%>><%=deptVo.getDname()%></option>	
						<%
						}
					}
					%>
				</select>												
			</td>
		</tr>	
		
		<tr>
			<td class="tbtd_caption">설명</td>
			<td class="tbtd_content" colspan='2'>
				<textarea name="account" class="txt" cols="65" rows="5"><%=empVo.getAccount()==null ? "" : empVo.getAccount()%></textarea>								
			</td>
		</tr>	
			
		<tr>
			<td class="tbtd_caption">첨부파일</td>
			<td class="tbtd_content" colspan='2'>
						<a href='javascript:fn_downEmpFile()'><span id='span_fileName'>&nbsp;&nbsp;&nbsp;<%=empVo.getFileName()%></span></a> 												
			</td>
		</tr>	
			
	</table>
    </div>
	
	<div id="sysbtn">
		<ul>
			<li><span class="btn_blue_l"><a href="javascript:fn_emp_selectList();">List</a><img src="<c:url value='/images/sample/rte/btn_bg_r.gif'/>" style="margin-left:6px;"></span></li>
	</div>
</div>


</form>
</body>
</html>

