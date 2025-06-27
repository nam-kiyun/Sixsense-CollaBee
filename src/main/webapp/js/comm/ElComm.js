
/**
 * 전역변수 선언
 * @date 2019.09.29
 * @memberOf
 * @param 
 * @returns
 * @author 
 * @example
 */
var CONTEXT_PATH = "/InsWebApp/";

/**
 * 쿠키를 저장한다.
 * @date 2019.09.29
 * @memberOf
 * @param 
 * @returns
 * @author 
 * @example
 */	
function setCookie(key, value, date) {
	
	var expire = new Date();
       expire.setDate(expire.getDate() + date);
       cookies = key + '=' + escape(value) + '; path=/';
       if(typeof date != 'undefined') cookies += ';expires=' + expire.toGMTString() + ';';
       document.cookie = cookies;
       
}

/**
 * 쿠키를 가져온다.
 * @date 2019.09.29
 * @memberOf
 * @param 
 * @returns
 * @author 
 * @example
 */	
function getCookie(key) {
	key = key + '=';
    var cookieData = document.cookie;
    var start = cookieData.indexOf(key);
    var value = '';
    if(start != -1){
        start += key.length;
        var end = cookieData.indexOf(';', start);
        if(end == -1) end = cookieData.length;
        value = cookieData.substring(start, end);
    }
    return unescape(value);
}

/**
 * 요청 정보를 받아서 ajax를 실행한다.
 * @date 2019.09.29
 * @memberOf
 * @param
 * @returns
 * @author 
 * @example
 */
function executeAjax(url, object, callback) {
	
	$.ajax({
		url 	    : url,
		type        : "post",
		dataType    : "json",
		data        : object,
		contentType : "application/json; charset=utf-8",
		headers : {
			"Proworks-Body":"Y",  // HTTP Body로 JSON 객체를 전달하기 위한 여부
			"Proworks-Lang":"ko"  // 다국어 처리를 위한 언어 설정 (미설정 시 프레임워크에서 ko 세팅)
		},	
	})
	.done(function(data) {
		callback(data);
	})
	.fail(function(request, status, error) {
		var data = {"code":request.status, "message":request.responseText, "error":error};
		alert("[ HTTP 상태 ] " + data.code + " 에러가 발생했습니다.");
	})
	.always(function() {
		
	});
	
}

/**
 * elHeader 처리 예외 발생 시 Alert을 발생시긴다.
 * @date 2019.09.29
 * @memberOf
 * @param
 * @returns
 * @author 
 * @example
 */
function exceptionAlert(funcName, resCode, resMsg) {
	alert(funcName + " 처리 중 예외가 발생했습니다." + "\n" + "- CODE: " + resCode + "\n" + "- MESSAGE : " + resMsg);
}

/**
 * 해당 페이지로 이동한다.
 * @date 2019.09.29
 * @memberOf
 * @param
 * @returns
 * @author 
 * @example
 */
function moveLocation(url) {
	$(location).attr("href", CONTEXT_PATH + url);
}
