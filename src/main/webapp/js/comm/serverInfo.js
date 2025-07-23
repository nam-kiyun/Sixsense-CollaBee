// 서버 정보 관리 유틸리티
// 동적으로 서버 IP 주소를 가져오고 관리하는 기능

window.ServerInfo = (function() {
    
    var serverConfig = {
        httpPort: 9093,
        httpsPort: 19443,
        currentIP: null,
        initialized: false
    };
    
    // 서버 IP 주소 자동 감지
    function detectServerIP() {
        var hostname = window.location.hostname;
        
        // localhost 또는 127.0.0.1인 경우 실제 서버 IP 확인 필요
        if (hostname === 'localhost' || hostname === '127.0.0.1') {
            // 개발 환경에서는 기본값 사용
            return hostname;
        }
        
        // 이미 IP 주소로 접속한 경우 그대로 사용
        return hostname;
    }
    
    // 서버 정보 초기화
    function initialize() {
        if (serverConfig.initialized) {
            return Promise.resolve(serverConfig);
        }
        
        return new Promise(function(resolve, reject) {
            try {
                serverConfig.currentIP = detectServerIP();
                serverConfig.initialized = true;
                
                console.log('ServerInfo 초기화 완료:', {
                    currentIP: serverConfig.currentIP,
                    httpPort: serverConfig.httpPort,
                    httpsPort: serverConfig.httpsPort
                });
                
                resolve(serverConfig);
            } catch (error) {
                console.error('ServerInfo 초기화 실패:', error);
                reject(error);
            }
        });
    }
    
    // HTTP URL 생성
    function getHttpUrl(path) {
        var ip = serverConfig.currentIP || detectServerIP();
        var url = "http://" + ip + ":" + serverConfig.httpPort;
        if (path && !path.startsWith('/')) {
            path = '/' + path;
        }
        return url + (path || '');
    }
    
    // HTTPS URL 생성
    function getHttpsUrl(path) {
        var ip = serverConfig.currentIP || detectServerIP();
        var url = "https://" + ip + ":" + serverConfig.httpsPort;
        if (path && !path.startsWith('/')) {
            path = '/' + path;
        }
        return url + (path || '');
    }
    
    // 화상통화 URL 생성 (HTTPS)
    function getVideoChatUrl(projectId) {
        var basePath = "/InsWebApp/websquare/websquare.html?w2xPath=/InsWebApp/ui/videochat/webRTCVideoChatPage.xml";
        var fullPath = basePath;
        
        if (projectId) {
            fullPath += "&project_id=" + encodeURIComponent(projectId);
        }
        
        return getHttpsUrl(fullPath);
    }
    
    // 프로젝트 메인 페이지 URL 생성 (HTTP)
    function getProjectMainUrl(projectId) {
        var basePath = "/InsWebApp/websquare/websquare.html?w2xPath=/InsWebApp/ui/project/projectMainPage.xml";
        var fullPath = basePath;
        
        if (projectId) {
            fullPath += "&projectId=" + encodeURIComponent(projectId);
        }
        
        return getHttpUrl(fullPath);
    }
    
    // 현재 서버 정보 반환
    function getServerInfo() {
        return {
            ip: serverConfig.currentIP || detectServerIP(),
            httpPort: serverConfig.httpPort,
            httpsPort: serverConfig.httpsPort,
            httpUrl: getHttpUrl(),
            httpsUrl: getHttpsUrl()
        };
    }
    
    // 공개 API
    return {
        initialize: initialize,
        getServerInfo: getServerInfo,
        getHttpUrl: getHttpUrl,
        getHttpsUrl: getHttpsUrl,
        getVideoChatUrl: getVideoChatUrl,
        getProjectMainUrl: getProjectMainUrl,
        
        // 편의 메서드
        getCurrentIP: function() {
            return serverConfig.currentIP || detectServerIP();
        },
        
        // 설정 업데이트
        updateConfig: function(config) {
            if (config.httpPort) serverConfig.httpPort = config.httpPort;
            if (config.httpsPort) serverConfig.httpsPort = config.httpsPort;
            if (config.currentIP) serverConfig.currentIP = config.currentIP;
        }
    };
})();

// 자동 초기화
document.addEventListener('DOMContentLoaded', function() {
    ServerInfo.initialize().catch(function(error) {
        console.error('ServerInfo 자동 초기화 실패:', error);
    });
});