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

		// WebSocket 연결 성공

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
		// 메시지 수신

		try {
			KanbanMessage kanbanMessage = objectMapper.readValue(payload, KanbanMessage.class);
			processMessage(session, kanbanMessage);
		} catch (Exception e) {
			// 메시지 처리 오류
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
		String sessionId = session.getId();
		// WebSocket 전송 오류
		
		if (exception instanceof java.io.EOFException) {
			// 클라이언트 연결 종료
			// 세션 정리는 afterConnectionClosed에서 처리되므로 여기서는 로깅만
		} else {
			// 예상치 못한 WebSocket 전송 오류
			exception.printStackTrace();
		}
		
		// 세션이 여전히 열려있다면 정리
		if (session.isOpen()) {
			try {
				session.close();
			} catch (Exception e) {
				// 세션 종료 중 오류
			}
		}
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
		case "TASK_UPDATED":
			handleTaskUpdated(session, message);
			break;
		case "TASK_DELETED":
			handleTaskDeleted(session, message);
			break;
		case "BOARD_CREATED":
			handleBoardCreated(session, message);
			break;
		case "BOARD_DELETED":
			handleBoardDeleted(session, message);
			break;
		case "TASK_CREATED":
			handleTaskCreated(session, message);
			break;
		case "PING":
			handlePing(session, message);
			break;
		default:
			// 알 수 없는 메시지 타입
			break;
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
			
			// 프로젝트 첫 참여자인 경우 Redis 캐시 워밍
			if (projectSessions.get(projectId).size() == 1) {
				try {
					kanbanRedisService.warmUpProjectCache(projectId);
				} catch (Exception e) {
					// 프로젝트 캐시 워밍 실패
				}
			}
			
			// 사용자 참여 성공
		} else {
			// 사용자 참여 (프로젝트 정보 없음)
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
		// 카드 이동 처리 시작

		// 메시지 유효성 검증
		if (message.getTaskId() == null || message.getToBoardId() == null) {
			// 잘못된 카드 이동 메시지
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
		} catch (Exception e) {
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
	}

	/**
	 *  카드 락/언락 처리 (편집 및 이동 락킹)
	 */
	private void handleCardLock(WebSocketSession session, KanbanMessage message) {
		// 카드 락 처리 시작

		// 메시지 유효성 검증
		if (message.getTaskId() == null || message.getMessage() == null) {
			// 잘못된 카드 락 메시지
			return;
		}

		// 메시지에서 액션 추출 (LOCK, UNLOCK, MOVE_LOCK, MOVE_UNLOCK)
		String[] messageParts = message.getMessage().split("\\|");
		String action = messageParts.length > 0 ? messageParts[0] : "";

		// 브로드캐스트용 메시지 생성 (원본 메시지 그대로 전달)
		KanbanMessage broadcastMessage = new KanbanMessage();
		broadcastMessage.setType("CARD_LOCK");
		broadcastMessage.setTaskId(message.getTaskId());
		broadcastMessage.setUserId(message.getUserId());
		broadcastMessage.setMessage(message.getMessage());
		broadcastMessage.setProjectId(message.getProjectId());
		broadcastMessage.setTimestamp(System.currentTimeMillis());

		// CARD_LOCK 메시지는 taskId 기반이므로 항상 모든 세션에 브로드캐스트
		broadcastToAll(broadcastMessage);
	}

	/**
	 * 태스크 업데이트 처리 (태스크 정보 변경 시 실시간 동기화)
	 */
	private void handleTaskUpdated(WebSocketSession session, KanbanMessage message) {
		// 태스크 업데이트 처리 시작

		// 메시지 유효성 검증
		if (message.getTaskId() == null || message.getBoardId() == null) {
			// 잘못된 태스크 업데이트 메시지
			return;
		}

		// 브로드캐스트용 메시지 생성
		KanbanMessage broadcastMessage = new KanbanMessage();
		broadcastMessage.setType("TASK_UPDATED");
		broadcastMessage.setTaskId(message.getTaskId());
		broadcastMessage.setBoardId(message.getBoardId());
		broadcastMessage.setUserId(message.getUserId());
		broadcastMessage.setProjectId(message.getProjectId());
		broadcastMessage.setMessage("태스크가 업데이트되었습니다.");
		broadcastMessage.setTimestamp(System.currentTimeMillis());

		// 전체 브로드캐스트 (클라이언트에서 프로젝트 ID로 필터링)
		broadcastToAll(broadcastMessage);
	}

	/**
	 * 태스크 삭제 처리 (태스크 삭제 시 실시간 동기화)
	 */
	private void handleTaskDeleted(WebSocketSession session, KanbanMessage message) {

		// 메시지 유효성 검증
		if (message.getTaskId() == null || message.getBoardId() == null) {
			return;
		}

		// 브로드캐스트용 메시지 생성
		KanbanMessage broadcastMessage = new KanbanMessage();
		broadcastMessage.setType("TASK_DELETED");
		broadcastMessage.setTaskId(message.getTaskId());
		broadcastMessage.setBoardId(message.getBoardId());
		broadcastMessage.setUserId(message.getUserId());
		broadcastMessage.setProjectId(message.getProjectId());
		broadcastMessage.setMessage("태스크가 삭제되었습니다.");
		broadcastMessage.setTimestamp(System.currentTimeMillis());

		// 전체 브로드캐스트 (클라이언트에서 프로젝트 ID로 필터링)
		broadcastToAll(broadcastMessage);
	}

	/**
	 * 보드 생성 처리 (보드 생성 시 실시간 동기화)
	 */
	private void handleBoardCreated(WebSocketSession session, KanbanMessage message) {

		// 메시지 유효성 검증
		if (message.getBoardId() == null || message.getProjectId() == null) {
			return;
		}

		// 브로드캐스트용 메시지 생성
		KanbanMessage broadcastMessage = new KanbanMessage();
		broadcastMessage.setType("BOARD_CREATED");
		broadcastMessage.setBoardId(message.getBoardId());
		broadcastMessage.setUserId(message.getUserId());
		broadcastMessage.setProjectId(message.getProjectId());
		broadcastMessage.setMessage(message.getMessage()); // 보드 제목 등 추가 정보
		broadcastMessage.setTimestamp(System.currentTimeMillis());

		// 전체 브로드캐스트 (클라이언트에서 프로젝트 ID로 필터링)
		broadcastToAll(broadcastMessage);
	}

	/**
	 * 외부에서 보드 생성 메시지를 처리할 수 있는 public 메서드
	 */
	public void handleBoardCreatedMessage(java.util.Map<String, Object> messageData) {
		try {
			// Map을 KanbanMessage 객체로 변환
			KanbanMessage message = new KanbanMessage();
			message.setType((String) messageData.get("type"));
			message.setBoardId((String) messageData.get("boardId"));
			message.setProjectId((String) messageData.get("projectId"));
			message.setMessage((String) messageData.get("boardTitle")); // 보드 제목을 message 필드에 저장
			message.setTimestamp((Long) messageData.getOrDefault("timestamp", System.currentTimeMillis()));
			
			// 내부 핸들러 호출
			handleBoardCreated(null, message);
		} catch (Exception e) {
		}
	}

	/**
	 * 보드 삭제 처리 (보드 삭제 시 실시간 동기화)
	 */
	private void handleBoardDeleted(WebSocketSession session, KanbanMessage message) {

		// 메시지 유효성 검증
		if (message.getBoardId() == null || message.getProjectId() == null) {
			return;
		}

		// 브로드캐스트용 메시지 생성
		KanbanMessage broadcastMessage = new KanbanMessage();
		broadcastMessage.setType("BOARD_DELETED");
		broadcastMessage.setBoardId(message.getBoardId());
		broadcastMessage.setUserId(message.getUserId());
		broadcastMessage.setProjectId(message.getProjectId());
		broadcastMessage.setMessage(message.getMessage()); // 보드 제목 등 추가 정보
		broadcastMessage.setTimestamp(System.currentTimeMillis());

		// 전체 브로드캐스트 (클라이언트에서 프로젝트 ID로 필터링)
		broadcastToAll(broadcastMessage);
	}

	/**
	 * 외부에서 보드 삭제 메시지를 처리할 수 있는 public 메서드
	 */
	public void handleBoardDeletedMessage(java.util.Map<String, Object> messageData) {
		try {
			// Map을 KanbanMessage 객체로 변환
			KanbanMessage message = new KanbanMessage();
			message.setType((String) messageData.get("type"));
			message.setBoardId((String) messageData.get("boardId"));
			message.setProjectId((String) messageData.get("projectId"));
			message.setMessage((String) messageData.get("boardTitle")); // 보드 제목을 message 필드에 저장
			message.setTimestamp((Long) messageData.getOrDefault("timestamp", System.currentTimeMillis()));
			
			// 내부 핸들러 호출
			handleBoardDeleted(null, message);
		} catch (Exception e) {
		}
	}

	/**
	 * 태스크 생성 처리 (태스크 생성 시 실시간 동기화)
	 */
	private void handleTaskCreated(WebSocketSession session, KanbanMessage message) {

		// 메시지 유효성 검증
		if (message.getTaskId() == null || message.getBoardId() == null) {
			return;
		}

		// 브로드캐스트용 메시지 생성
		KanbanMessage broadcastMessage = new KanbanMessage();
		broadcastMessage.setType("TASK_CREATED");
		broadcastMessage.setTaskId(message.getTaskId());
		broadcastMessage.setBoardId(message.getBoardId());
		broadcastMessage.setUserId(message.getUserId());
		broadcastMessage.setProjectId(message.getProjectId());
		broadcastMessage.setMessage("새 태스크가 생성되었습니다.");
		broadcastMessage.setTimestamp(System.currentTimeMillis());

		// 전체 브로드캐스트 (클라이언트에서 프로젝트 ID로 필터링)
		broadcastToAll(broadcastMessage);
	}

	/**
	 * 외부에서 태스크 생성 메시지를 처리할 수 있는 public 메서드
	 */
	public void handleTaskCreatedMessage(java.util.Map<String, Object> messageData) {
		try {
			// Map을 KanbanMessage 객체로 변환하되, 모든 태스크 정보를 포함
			KanbanMessage message = new KanbanMessage();
			message.setType((String) messageData.get("type"));
			message.setTaskId((String) messageData.get("taskId"));
			message.setBoardId((String) messageData.get("boardId"));
			message.setProjectId((String) messageData.get("projectId"));
			message.setUserId((String) messageData.get("userId"));
			message.setTimestamp((Long) messageData.getOrDefault("timestamp", System.currentTimeMillis()));
			
			// 태스크 상세 정보 설정
			message.setTaskTitle((String) messageData.get("taskTitle"));
			message.setProjectUserId((String) messageData.get("projectUserId")); // projectUserId 추가
			message.setPriority((String) messageData.get("priority"));
			message.setStartDate((String) messageData.get("startDate"));
			message.setEndDate((String) messageData.get("endDate"));
			message.setTags((String) messageData.get("tags"));
			message.setUserName((String) messageData.get("userName")); // 사용자 이름 추가
			
			// message 필드는 태스크 제목으로 설정 (기존 호환성 유지)
			message.setMessage((String) messageData.get("taskTitle"));
			
			// 브로드캐스트 실행
			broadcastToAll(message);
		} catch (Exception e) {
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
		if (session == null) {
			return;
		}
		
		try {
			if (session.isOpen()) {
				String json = objectMapper.writeValueAsString(message);
				session.sendMessage(new TextMessage(json));
			} else {
			}
		} catch (java.io.EOFException e) {
		} catch (IOException e) {
			if (e.getMessage() != null && e.getMessage().contains("Connection reset")) {
			} else {
			}
		} catch (Exception e) {
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
			return;
		}

		sessions.values().forEach(session -> {
			try {
				if (session != null && session.isOpen()) {
					session.sendMessage(new TextMessage(json));
				}
			} catch (java.io.EOFException e) {
			} catch (IOException e) {
				if (e.getMessage() != null && e.getMessage().contains("Connection reset")) {
				} else {
				}
			} catch (Exception e) {
			}
		});

	}

	/**
	 * 특정 프로젝트의 활성 세션에만 메시지 브로드캐스트
	 */
	public void broadcastToProject(String projectId, java.util.Map<String, Object> messageData) {
		if (projectId == null || messageData == null) {
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
			return;
		}

		// 해당 프로젝트의 세션들 가져오기
		java.util.Set<String> projectSessionIds = projectSessions.get(projectId);
		if (projectSessionIds == null || projectSessionIds.isEmpty()) {
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
				} catch (java.io.EOFException e) {
				} catch (IOException e) {
					if (e.getMessage() != null && e.getMessage().contains("Connection reset")) {
					} else {
					}
				} catch (Exception e) {
				}
			} else {
			}
		}

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
				}
			}
		}
		
		// 연결 해제된 사용자의 모든 락 해제 알림
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
			}
		} catch (Exception e) {
		}
		
		
		// 모든 사용자가 나가면 데이터 보호 후 캐시 무효화
		if (sessions.size() == 0) {
			try {
				// 1단계: 미처리된 태스크 이동 데이터를 즉시 DB에 저장 (데이터 손실 방지)
				kanbanBatchService.processImmediateBatch();
				
				// 2단계: 모든 프로젝트의 캐시 무효화
				kanbanRedisService.deleteKeysByPattern("kanban:project:*");
				
			} catch (Exception e) {
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
	 * 세션 ID로 사용자 ID 조회 (락 해제용)
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