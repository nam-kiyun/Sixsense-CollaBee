package com.demo.proworks.websocket.handler;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.demo.proworks.redis.service.KanbanRedisService;
import com.demo.proworks.websocket.message.KanbanMessage;
import com.demo.proworks.batch.service.KanbanBatchService;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 칸반 보드 WebSocket 핸들러 실시간 카드 이동 및 업데이트 처리
 * 
 * @author Claude AI
 * @since 2025-01-15
 */
@Component
public class KanbanWebSocketHandler extends TextWebSocketHandler {

	private final ObjectMapper objectMapper = new ObjectMapper();

	// Redis 서비스 의존성 주입
	@Autowired
	private KanbanRedisService kanbanRedisService;
	
	// 배치 서비스 의존성 주입
	@Autowired
	private KanbanBatchService kanbanBatchService;

	// 활성 세션 관리 (세션ID -> WebSocketSession)
	private final ConcurrentHashMap<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

	// 사용자별 세션 관리 (userId -> sessionId)
	private final ConcurrentHashMap<String, String> userSessions = new ConcurrentHashMap<>();

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		String sessionId = session.getId();
		sessions.put(sessionId, session);

		System.out.println("WebSocket 연결 성공: " + sessionId);
		System.out.println("현재 활성 세션 수: " + sessions.size());

		// 연결 성공 메시지 전송
		KanbanMessage welcomeMessage = new KanbanMessage();
		welcomeMessage.setType("CONNECTION_SUCCESS");
		welcomeMessage.setMessage("칸반 보드 실시간 연결이 성공했습니다.");
		welcomeMessage.setTimestamp(System.currentTimeMillis());

		sendToSession(session, welcomeMessage);
	}

	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		String payload = message.getPayload();
		System.out.println("받은 메시지: " + payload);

		try {
			KanbanMessage kanbanMessage = objectMapper.readValue(payload, KanbanMessage.class);
			processMessage(session, kanbanMessage);
		} catch (Exception e) {
			System.err.println("메시지 처리 오류: " + e.getMessage());
			e.printStackTrace();

			// 에러 메시지 전송
			KanbanMessage errorMessage = new KanbanMessage();
			errorMessage.setType("ERROR");
			errorMessage.setMessage("메시지 처리 중 오류가 발생했습니다: " + e.getMessage());
			errorMessage.setTimestamp(System.currentTimeMillis());

			sendToSession(session, errorMessage);
		}
	}


	@Override
	public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
		System.err.println("WebSocket 전송 오류: " + session.getId());
		exception.printStackTrace();
	}

	/**
	 * 받은 메시지를 타입별로 처리
	 */
	private void processMessage(WebSocketSession session, KanbanMessage message) {
		switch (message.getType()) {
		case "USER_JOIN":
			handleUserJoin(session, message);
			break;
		case "CARD_MOVE":
			handleCardMove(session, message);
			break;
		case "PING":
			handlePing(session, message);
			break;
		default:
			System.out.println("알 수 없는 메시지 타입: " + message.getType());
		}
	}

	/**
	 * 사용자 참여 처리
	 */
	private void handleUserJoin(WebSocketSession session, KanbanMessage message) {
		String userId = message.getUserId();
		String sessionId = session.getId();

		userSessions.put(userId, sessionId);

		System.out.println("사용자 참여: " + userId + " (세션: " + sessionId + ")");

		// 참여 확인 메시지 전송
		KanbanMessage response = new KanbanMessage();
		response.setType("USER_JOIN_SUCCESS");
		response.setUserId(userId);
		response.setMessage("칸반 보드에 참여했습니다.");
		response.setTimestamp(System.currentTimeMillis());

		sendToSession(session, response);
	}

	/**
	 * 카드 이동 처리 (Redis 저장 + 실시간 브로드캐스트)
	 */
	private void handleCardMove(WebSocketSession session, KanbanMessage message) {
		System.out.println("=== 카드 이동 처리 시작 ===");
		System.out.println("📦 받은 메시지: " + message.getTaskId() + " (" + message.getFromBoardId() + " → " + message.getToBoardId() + ")");
		System.out.println("👤 사용자: " + message.getUserId());
		System.out.println("🏷️ 프로젝트: " + message.getProjectId());

		// 메시지 유효성 검증
		if (message.getTaskId() == null || message.getToBoardId() == null) {
			System.err.println("❌ 잘못된 카드 이동 메시지: " + message);
			return;
		}

		// 1. Redis에 카드 이동 정보 저장 (디바운싱된 메시지)
		try {
			kanbanRedisService.saveTaskMove(
				message.getTaskId(),
				message.getFromBoardId(),
				message.getToBoardId(),
				message.getUserId(),
				message.getProjectId()
			);
			System.out.println("✅ Redis 저장 완료: " + message.getTaskId());
		} catch (Exception e) {
			System.err.println("❌ Redis 저장 실패: " + e.getMessage());
			e.printStackTrace();
			// Redis 실패해도 실시간 동기화는 계속 진행
		}

		// 2. 브로드캐스트용 메시지 생성
		KanbanMessage broadcastMessage = new KanbanMessage();
		broadcastMessage.setType("CARD_MOVED");
		broadcastMessage.setTaskId(message.getTaskId());
		broadcastMessage.setFromBoardId(message.getFromBoardId());
		broadcastMessage.setToBoardId(message.getToBoardId());
		broadcastMessage.setUserId(message.getUserId());
		broadcastMessage.setProjectId(message.getProjectId());
		broadcastMessage.setTimestamp(System.currentTimeMillis());

		// 3. 실시간 브로드캐스트
		broadcastToAll(broadcastMessage);
		System.out.println("📡 실시간 브로드캐스트 완료 - 활성 세션: " + sessions.size() + "개");
	}

	/**
	 * 핑 처리 (연결 상태 확인)
	 */
	private void handlePing(WebSocketSession session, KanbanMessage message) {
		KanbanMessage pong = new KanbanMessage();
		pong.setType("PONG");
		pong.setTimestamp(System.currentTimeMillis());

		sendToSession(session, pong);
	}

	/**
	 * 특정 세션에 메시지 전송
	 */
	private void sendToSession(WebSocketSession session, KanbanMessage message) {
		try {
			if (session.isOpen()) {
				String json = objectMapper.writeValueAsString(message);
				session.sendMessage(new TextMessage(json));
			}
		} catch (IOException e) {
			System.err.println("메시지 전송 실패: " + e.getMessage());
		}
	}

	/**
	 * 모든 활성 세션에 메시지 브로드캐스트
	 */
	private void broadcastToAll(KanbanMessage message) {
		String json;
		try {
			json = objectMapper.writeValueAsString(message);
		} catch (Exception e) {
			System.err.println("메시지 직렬화 실패: " + e.getMessage());
			return;
		}

		sessions.values().forEach(session -> {
			try {
				if (session.isOpen()) {
					session.sendMessage(new TextMessage(json));
				}
			} catch (IOException e) {
				System.err.println("브로드캐스트 전송 실패: " + e.getMessage());
			}
		});

		System.out.println("메시지 브로드캐스트 완료: " + sessions.size() + "개 세션");
	}

	/**
	 * WebSocket 연결 해제 처리
	 */
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		String sessionId = session.getId();
		
		// 세션 제거
		sessions.remove(sessionId);
		
		// 사용자 세션 매핑에서도 제거
		userSessions.entrySet().removeIf(entry -> entry.getValue().equals(sessionId));
		
		System.out.println("WebSocket 연결 해제: " + sessionId);
		System.out.println("현재 활성 세션 수: " + sessions.size());
		
		// 모든 사용자가 나가면 데이터 보호 후 캐시 무효화
		if (sessions.size() == 0) {
			System.out.println("🗑️ 모든 사용자 연결 해제 - 데이터 보호 및 캐시 무효화 시작");
			try {
				// 1단계: 미처리된 태스크 이동 데이터를 즉시 DB에 저장 (데이터 손실 방지)
				System.out.println("💾 1단계: 미처리 데이터 즉시 DB 저장");
				kanbanBatchService.processImmediateBatch();
				
				// 2단계: 모든 프로젝트의 캐시 무효화
				System.out.println("🗑️ 2단계: 프로젝트 캐시 무효화");
				kanbanRedisService.deleteKeysByPattern("kanban:project:*");
				
				System.out.println("✅ 데이터 보호 및 캐시 무효화 완료 - 다음 접속 시 최신 DB 데이터로 로드됩니다");
			} catch (Exception e) {
				System.err.println("❌ 데이터 보호 및 캐시 무효화 실패: " + e.getMessage());
				e.printStackTrace();
			}
		}
	}

	/**
	 * 현재 활성 세션 수 반환
	 */
	public int getActiveSessionCount() {
		return sessions.size();
	}
}