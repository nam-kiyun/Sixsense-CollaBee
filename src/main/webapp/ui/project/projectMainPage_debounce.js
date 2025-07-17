/**
 * 칸반 보드 디바운싱 로직
 * 카드 이동 시 즉시 서버로 전송하지 않고 2초 디바운싱 적용
 * 
 * @author Claude AI
 * @since 2025-01-15
 */

/**
 * 디바운싱을 적용한 카드 이동 메시지 전송
 * @param {string} taskId 태스크 ID
 * @param {string} fromBoardId 출발 보드 ID  
 * @param {string} toBoardId 도착 보드 ID
 */
scwin.sendCardMoveMessageWithDebounce = function(taskId, fromBoardId, toBoardId) {
    console.log("디바운싱 카드 이동 메시지:", taskId, "->", toBoardId);
    
    // 이전 타이머가 있으면 취소
    if (scwin.debounceTimer) {
        clearTimeout(scwin.debounceTimer);
        scwin.debounceTimer = null;
        console.log("이전 디바운싱 타이머 취소됨");
    }
    
    // 새로운 타이머 설정 (2초 후 실행)
    scwin.debounceTimer = setTimeout(function() {
        console.log("디바운싱 완료 - 카드 이동 메시지 전송:", taskId);
        
        var message = {
            type: "CARD_MOVE",
            taskId: taskId,
            fromBoardId: fromBoardId,
            toBoardId: toBoardId,
            userId: "user01", // 실제 사용자 ID로 변경 필요
            timestamp: Date.now()
        };
        
        // WebSocket으로 즉시 브로드캐스트
        scwin.sendWebSocketMessage(message);
        
        // 디바운싱 타이머 초기화
        scwin.debounceTimer = null;
        
    }, scwin.debounceDelay);
    
    console.log("디바운싱 타이머 설정됨: " + scwin.debounceDelay + "ms");
};

/**
 * 연속적인 카드 이동 처리
 * 사용자가 카드를 여러 번 빠르게 이동시킬 때 마지막 위치만 서버에 전송
 */
scwin.handleContinuousCardMove = function(taskId, fromBoardId, toBoardId) {
    // 로컬 캐시에 최신 이동 정보 저장
    if (!scwin.cardMoveCache) {
        scwin.cardMoveCache = {};
    }
    
    scwin.cardMoveCache[taskId] = {
        fromBoardId: fromBoardId,
        toBoardId: toBoardId,
        timestamp: Date.now()
    };
    
    console.log("카드 이동 캐시 업데이트:", taskId, scwin.cardMoveCache[taskId]);
    
    // 디바운싱 적용하여 메시지 전송
    scwin.sendCardMoveMessageWithDebounce(taskId, fromBoardId, toBoardId);
};

/**
 * 드래그 앤 드롭 이벤트와 연동
 * 기존 칸반 드래그 함수에서 호출
 */
scwin.onCardDropped = function(draggedElement, targetBoard) {
    try {
        // 드래그된 카드에서 taskId 추출
        var taskId = draggedElement.getAttribute('data-task-id');
        if (!taskId) {
            console.warn("taskId를 찾을 수 없습니다:", draggedElement);
            return;
        }
        
        // 이전 보드 ID 추출
        var fromBoardId = draggedElement.getAttribute('data-board-id');
        
        // 새로운 보드 ID 추출
        var toBoardId = targetBoard.getAttribute('data-board-id');
        
        if (!fromBoardId || !toBoardId) {
            console.warn("보드 ID를 찾을 수 없습니다:", fromBoardId, toBoardId);
            return;
        }
        
        // 같은 보드 내 이동은 무시
        if (fromBoardId === toBoardId) {
            console.log("같은 보드 내 이동이므로 무시합니다:", taskId);
            return;
        }
        
        console.log("카드 드롭 감지:", taskId, fromBoardId, "->", toBoardId);
        
        // 연속적인 카드 이동 처리 (디바운싱 적용)
        scwin.handleContinuousCardMove(taskId, fromBoardId, toBoardId);
        
        // 로컬 UI 즉시 업데이트
        scwin.updateLocalCardPosition(draggedElement, targetBoard, toBoardId);
        
    } catch (error) {
        console.error("카드 드롭 처리 중 오류:", error);
    }
};

/**
 * 로컬 UI 즉시 업데이트
 * 서버 응답을 기다리지 않고 사용자 경험 향상
 */
scwin.updateLocalCardPosition = function(cardElement, targetBoard, toBoardId) {
    try {
        // 카드의 보드 ID 속성 업데이트
        cardElement.setAttribute('data-board-id', toBoardId);
        
        // 시각적 피드백 (옵션)
        cardElement.style.opacity = '0.7';
        setTimeout(function() {
            cardElement.style.opacity = '1.0';
        }, 300);
        
        console.log("로컬 UI 업데이트 완료:", cardElement, toBoardId);
        
    } catch (error) {
        console.error("로컬 UI 업데이트 중 오류:", error);
    }
};

/**
 * 배치 처리를 위한 pending 카드 이동 목록 관리
 */
scwin.getPendingCardMoves = function() {
    var pendingMoves = [];
    
    if (scwin.cardMoveCache) {
        for (var taskId in scwin.cardMoveCache) {
            var moveInfo = scwin.cardMoveCache[taskId];
            pendingMoves.push({
                taskId: taskId,
                fromBoardId: moveInfo.fromBoardId,
                toBoardId: moveInfo.toBoardId,
                timestamp: moveInfo.timestamp
            });
        }
    }
    
    return pendingMoves;
};

/**
 * 캐시 정리 함수
 */
scwin.clearCardMoveCache = function() {
    scwin.cardMoveCache = {};
    console.log("카드 이동 캐시 정리 완료");
};

/**
 * 디바운싱 상태 확인 함수
 */
scwin.isDebouncing = function() {
    return scwin.debounceTimer !== null;
};

/**
 * 강제 디바운싱 완료 (테스트용)
 */
scwin.forceDebounceComplete = function() {
    if (scwin.debounceTimer) {
        clearTimeout(scwin.debounceTimer);
        scwin.debounceTimer = null;
        console.log("디바운싱 강제 완료됨");
    }
};

/**
 * 디바운싱 지연 시간 동적 조정
 */
scwin.setDebounceDelay = function(newDelay) {
    scwin.debounceDelay = newDelay;
    console.log("디바운싱 지연 시간 변경됨:", newDelay + "ms");
};

/**
 * 디바운싱 통계 정보
 */
scwin.getDebounceStats = function() {
    return {
        isActive: scwin.isDebouncing(),
        delay: scwin.debounceDelay,
        cacheSize: scwin.cardMoveCache ? Object.keys(scwin.cardMoveCache).length : 0,
        pendingMoves: scwin.getPendingCardMoves()
    };
};