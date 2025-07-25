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
	
	// 프로젝트별 세션 관리 (projectId -> Set<sessionId>)
	private final ConcurrentHashMap<String, java.util.Set<String>> projectSessions = new ConcurrentHashMap<>();
	
	// 세션별 프로젝트 관리 (sessionId -> projectId)
	private final ConcurrentHashMap<String, String> sessionProjects = new ConcurrentHashMap<>();

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
		case "CARD_LOCK":
			handleCardLock(session, message);
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
		String projectId = message.getProjectId();
		String sessionId = session.getId();

		// 사용자별 세션 매핑 저장
		userSessions.put(userId, sessionId);
		
		// 프로젝트별 세션 관리
		if (projectId != null) {
			// 세션별 프로젝트 매핑 저장
			sessionProjects.put(sessionId, projectId);
			
			// 프로젝트별 세션 목록에 추가
			projectSessions.computeIfAbsent(projectId, k -> ConcurrentHashMap.newKeySet()).add(sessionId);
			
			System.out.println("사용자 참여: " + userId + " (세션: " + sessionId + ", 프로젝트: " + projectId + ")");
		} else {
			System.out.println("사용자 참여: " + userId + " (세션: " + sessionId + ", 프로젝트 정보 없음)");
		}

		// 참여 확인 메시지 전송
		KanbanMessage response = new KanbanMessage();
		response.setType("USER_JOIN_SUCCESS");
		response.setUserId(userId);
		response.setProjectId(projectId);
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
	 * 🔄 카드 락/언락 처리 (편집 및 이동 락킹)
	 */
	private void handleCardLock(WebSocketSession session, KanbanMessage message) {
		System.out.println("=== 카드 락 처리 시작 ===");
		System.out.println("📦 받은 메시지: " + message.getTaskId() + " (" + message.getMessage() + ")");
		System.out.println("👤 사용자: " + message.getUserId());
		System.out.println("🏷️ 프로젝트: " + message.getProjectId());

		// 메시지 유효성 검증
		if (message.getTaskId() == null || message.getMessage() == null) {
			System.err.println("❌ 잘못된 카드 락 메시지: " + message);
			return;
		}

		// 메시지에서 액션 추출 (LOCK, UNLOCK, MOVE_LOCK, MOVE_UNLOCK)
		String[] messageParts = message.getMessage().split("\\|");
		String action = messageParts.length > 0 ? messageParts[0] : "";
		
		System.out.println("🔒 락 액션: " + action);

		// 브로드캐스트용 메시지 생성 (원본 메시지 그대로 전달)
		KanbanMessage broadcastMessage = new KanbanMessage();
		broadcastMessage.setType("CARD_LOCK");
		broadcastMessage.setTaskId(message.getTaskId());
		broadcastMessage.setUserId(message.getUserId());
		broadcastMessage.setMessage(message.getMessage());
		broadcastMessage.setProjectId(message.getProjectId());
		broadcastMessage.setTimestamp(System.currentTimeMillis());

		// 실시간 브로드캐스트 (프로젝트별 또는 전체)
		if (message.getProjectId() != null) {
			// 프로젝트별 브로드캐스트 (성능 최적화)
			try {
				java.util.Map<String, Object> messageData = new java.util.HashMap<>();
				messageData.put("type", "CARD_LOCK");
				messageData.put("taskId", message.getTaskId());
				messageData.put("userId", message.getUserId());
				messageData.put("message", message.getMessage());
				messageData.put("projectId", message.getProjectId());
				messageData.put("timestamp", System.currentTimeMillis());
				
				broadcastToProject(message.getProjectId(), messageData);
				System.out.println("📡 프로젝트별 락 브로드캐스트 완료: " + message.getProjectId());
			} catch (Exception e) {
				System.err.println("❌ 프로젝트별 브로드캐스트 실패, 전체 브로드캐스트로 대체");
				broadcastToAll(broadcastMessage);
			}
		} else {
			// 전체 브로드캐스트 (fallback)
			broadcastToAll(broadcastMessage);
			System.out.println("📡 전체 락 브로드캐스트 완료 - 활성 세션: " + sessions.size() + "개");
		}
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
	 * 특정 프로젝트의 활성 세션에만 메시지 브로드캐스트
	 */
	public void broadcastToProject(String projectId, java.util.Map<String, Object> messageData) {
		if (projectId == null || messageData == null) {
			System.err.println("❌ 프로젝트 브로드캐스트 실패: projectId 또는 messageData가 null입니다.");
			return;
		}

		// Map을 KanbanMessage로 변환
		KanbanMessage message = new KanbanMessage();
		message.setType((String) messageData.get("type"));
		message.setProjectId((String) messageData.get("projectId"));
		message.setTaskId((String) messageData.get("taskId"));
		message.setBoardId((String) messageData.get("boardId"));
		message.setUserId((String) messageData.get("userId"));
		message.setMessage((String) messageData.get("message"));
		message.setTimestamp((Long) messageData.getOrDefault("timestamp", System.currentTimeMillis()));

		String json;
		try {
			json = objectMapper.writeValueAsString(message);
		} catch (Exception e) {
			System.err.println("❌ 프로젝트 브로드캐스트 메시지 직렬화 실패: " + e.getMessage());
			return;
		}

		// 해당 프로젝트의 세션들 가져오기
		java.util.Set<String> projectSessionIds = projectSessions.get(projectId);
		if (projectSessionIds == null || projectSessionIds.isEmpty()) {
			System.out.println("ℹ️ 프로젝트에 활성 세션이 없음: " + projectId);
			return;
		}

		int successCount = 0;
		int totalSessions = projectSessionIds.size();

		// 프로젝트의 각 세션에 메시지 전송
		for (String sessionId : projectSessionIds) {
			WebSocketSession session = sessions.get(sessionId);
			if (session != null && session.isOpen()) {
				try {
					session.sendMessage(new TextMessage(json));
					successCount++;
				} catch (IOException e) {
					System.err.println("❌ 프로젝트 브로드캐스트 전송 실패 (세션: " + sessionId + "): " + e.getMessage());
				}
			} else {
				System.out.println("⚠️ 비활성 세션 발견 (정리 필요): " + sessionId);
			}
		}

		System.out.println("📡 프로젝트 브로드캐스트 완료: " + projectId + " (" + successCount + "/" + totalSessions + " 세션)");
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
		
		// 프로젝트별 세션 관리에서도 제거
		String projectId = sessionProjects.remove(sessionId);
		if (projectId != null) {
			java.util.Set<String> projectSessionSet = projectSessions.get(projectId);
			if (projectSessionSet != null) {
				projectSessionSet.remove(sessionId);
				// 프로젝트에 세션이 더 이상 없으면 프로젝트 자체를 제거
				if (projectSessionSet.isEmpty()) {
					projectSessions.remove(projectId);
					System.out.println("프로젝트의 모든 세션 해제됨: " + projectId);
				}
			}
		}
		
		// 🔄 연결 해제된 사용자의 모든 락 해제 알림
		try {
			String disconnectedUserId = getUserIdBySessionId(sessionId);
			if (disconnectedUserId != null && projectId != null) {
				// 해제 메시지를 프로젝트의 다른 사용자들에게 브로드캐스트
				java.util.Map<String, Object> unlockMessage = new java.util.HashMap<>();
				unlockMessage.put("type", "USER_DISCONNECT_UNLOCK");
				unlockMessage.put("userId", disconnectedUserId);
				unlockMessage.put("projectId", projectId);
				unlockMessage.put("message", "사용자 연결 해제로 인한 모든 락 해제");
				unlockMessage.put("timestamp", System.currentTimeMillis());
				
				broadcastToProject(projectId, unlockMessage);
				System.out.println("🔓 연결 해제된 사용자의 락 해제 알림 전송: " + disconnectedUserId);
			}
		} catch (Exception e) {
			System.err.println("❌ 연결 해제 락 해제 알림 실패: " + e.getMessage());
		}
		
		System.out.println("WebSocket 연결 해제: " + sessionId + (projectId != null ? " (프로젝트: " + projectId + ")" : ""));
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
	
	/**
	 * 🔄 세션 ID로 사용자 ID 조회 (락 해제용)
	 */
	private String getUserIdBySessionId(String sessionId) {
		for (java.util.Map.Entry<String, String> entry : userSessions.entrySet()) {
			if (sessionId.equals(entry.getValue())) {
				return entry.getKey(); // userId 반환
			}
		}
		return null;
	}
}