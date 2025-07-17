/**
 * WebSocket 연결 테스트
 * 간단한 테스트용 파일
 */

function testWebSocketConnection() {
    console.log("=== WebSocket 연결 테스트 시작 ===");
    
    // 채팅에서 성공한 URL 패턴을 그대로 사용
    var protocol = window.location.protocol === 'https:' ? 'wss:' : 'ws:';
    var host = window.location.host;
    
    // 테스트할 URL 목록
    var testUrls = [
        protocol + '//' + host + '/InsWebApp/kanban',
        protocol + '//' + host + '/InsWebApp/websocket/kanban', 
        'ws://localhost:9093/InsWebApp/kanban',
        'ws://localhost:9093/InsWebApp/websocket/kanban'
    ];
    
    console.log("테스트할 URL 목록:");
    testUrls.forEach(function(url, index) {
        console.log((index + 1) + ". " + url);
    });
    
    // 첫 번째 URL 테스트
    testSingleUrl(testUrls[0], 0, testUrls);
}

function testSingleUrl(url, index, allUrls) {
    console.log(">>> " + (index + 1) + "번째 URL 테스트: " + url);
    
    try {
        var ws = new WebSocket(url);
        
        ws.onopen = function(event) {
            console.log("✅ 연결 성공! URL: " + url);
            
            // 테스트 메시지 전송
            var testMessage = {
                type: "USER_JOIN",
                userId: "test-user-" + Date.now(),
                timestamp: Date.now()
            };
            
            ws.send(JSON.stringify(testMessage));
            console.log("테스트 메시지 전송:", testMessage);
            
            // 5초 후 연결 종료
            setTimeout(function() {
                ws.close();
                console.log("테스트 완료 - 연결 종료");
            }, 5000);
        };
        
        ws.onmessage = function(event) {
            console.log("📩 서버 응답:", event.data);
        };
        
        ws.onclose = function(event) {
            console.log("연결 종료:", event.code, event.reason);
        };
        
        ws.onerror = function(event) {
            console.error("❌ 연결 실패: " + url);
            
            // 다음 URL 시도
            if (index + 1 < allUrls.length) {
                setTimeout(function() {
                    testSingleUrl(allUrls[index + 1], index + 1, allUrls);
                }, 1000);
            } else {
                console.error("❌ 모든 URL 테스트 실패");
            }
        };
        
    } catch (error) {
        console.error("❌ WebSocket 생성 실패:", error);
        
        // 다음 URL 시도
        if (index + 1 < allUrls.length) {
            setTimeout(function() {
                testSingleUrl(allUrls[index + 1], index + 1, allUrls);
            }, 1000);
        } else {
            console.error("❌ 모든 URL 테스트 실패");
        }
    }
}

// 자동 실행
if (typeof window !== 'undefined') {
    // 페이지 로드 후 실행
    setTimeout(testWebSocketConnection, 1000);
}