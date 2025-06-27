(function(){
	if(typeof top.wmatrix != "object"){
		if (navigator.userAgent.indexOf('Android') > 0) {
			wmatrixBridge.loadJS("wmatrix.js");
		} else {
			window.webkit.messageHandlers.wmatrix.postMessage(["WMatrix","loadJS:","wmatrix.js"]);
		}
	}
})();