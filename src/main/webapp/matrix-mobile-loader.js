(function(){
	window.matrixApp = false;

	if(typeof top.matrixMobile != "object"){
		if (navigator.userAgent.indexOf('Android') > 0) {
			// Android matrix webview
			if(typeof matrixBridge == "object"){
				window.matrixApp = true;
				matrixBridge.loadJS("matrix-mobile.js");
			}
		} else {
			// iOS matrix webview
			try {
				if(window.webkit && typeof window.webkit.messageHandlers.matrixMobile == "object"){
					window.matrixApp = true;
					window.webkit.messageHandlers.matrixMobile.postMessage(["MatrixMobile","loadJS:","matrix-mobile.js"]);
				}
			} catch (e) {
				console.log(e);
			}
		}
	}
})();