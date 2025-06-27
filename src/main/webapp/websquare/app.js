(function() {
    const init = function(){
        var promiseObj = WebSquare.startApplication();
        promiseObj.then(function(resolve, reject) {
            // to do
            if(typeof $h == "object"){
                $h.dismissScreen();
            }
        });
    };
    if(window.matrixApp){
        document.addEventListener("matrixMobileReady",function(){
            init();        
        });
    } else {
        init();
    }
})();