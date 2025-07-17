package com.demo.proworks.videochat.websocket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.demo.proworks.videochat.service.ChatService;
import com.demo.proworks.videochat.vo.ChatMessageVo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

@Component
public class ChatWebSocketHandler extends TextWebSocketHandler {

	static {
		// System.out.println("🔥 ChatWebSocketHandler 클래스 로딩됨!");
	}

	public ChatWebSocketHandler() {
		// System.out.println("🔥 ChatWebSocketHandler 생성자 호출됨!");
		// WebSocket용 ObjectMapper 설정
		initializeObjectMapper();
	}

	private static final Logger logger = LoggerFactory.getLogger(ChatWebSocketHandler.class);

	@Autowired
	private ChatService chatService;

	private ObjectMapper objectMapper;

	private void initializeObjectMapper() {
		this.objectMapper = new ObjectMapper();
		// WebSquare의 elExcludeFilter 문제 해결
		SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.serializeAll();
		FilterProvider filterProvider = new SimpleFilterProvider().addFilter("elExcludeFilter", filter)
				.setFailOnUnknownId(false); // 알 수 없는 필터 ID에 대해 실패하지 않음

		this.objectMapper.setFilterProvider(filterProvider);
	}
    private void handleJoin(WebSocketSession session, ChatMessageVo chatMessage) throws IOException {
        String channelName = chatMessage.getChannelName();
        String userId = chatMessage.getUserId();
        String userName = chatMessage.getUserName();
        
        // 채널에 세션 추가
        channelSessions.computeIfAbsent(channelName, k -> new CopyOnWriteArrayList<>()).add(session);
        
        // 사용자 정보 저장
        sessionUsers.put(session.getId(), chatMessage);
        
        logger.info("사용자 {}가 채널 {}에 참가했습니다.", userName, channelName);
        
        // 🎯 새 기능: 참가 시 이전 채팅 히스토리 자동 전송
        try {
            List<ChatMessageVo> history = chatService.getChannelMessages(channelName);
            
            if (!history.isEmpty()) {
                // 히스토리 응답 메시지 생성
                Map<String, Object> historyResponse = new ConcurrentHashMap<>();
                historyResponse.put("type", "history");
                historyResponse.put("messages", history);
                historyResponse.put("channelName", channelName);
                
                String historyJson = objectMapper.writeValueAsString(historyResponse);
                session.sendMessage(new TextMessage(historyJson));
                
                //System.out.println("📜 채팅 히스토리 전송: " + channelName + " (" + history.size() + "개 메시지)");
            }
        } catch (Exception e) {
            //System.err.println("❌ 히스토리 전송 실패: " + e.getMessage());
        }
        
        // 참가 알림 메시지 생성 및 브로드캐스트
        ChatMessageVo joinNotification = new ChatMessageVo();
        joinNotification.setType("notification");
        joinNotification.setChannelName(channelName);
        joinNotification.setUserId("system");
        joinNotification.setUserName("시스템");
        //joinNotification.setMessage(userName + "님이 채팅방에 참가했습니다.");
        joinNotification.setTimestamp(System.currentTimeMillis());
        
        // 참가 알림은 저장하지 않고 브로드캐스트만
        broadcastToChannel(channelName, joinNotification, session);
    

		

		// System.out.println("🔧 WebSocket ObjectMapper 초기화 완료 (elExcludeFilter 설정됨)");
	}

	// 세션 관리: 채널별로 세션들을 그룹화
	private final Map<String, List<WebSocketSession>> channelSessions = new ConcurrentHashMap<>();

	// 사용자 정보 관리: 세션ID -> 사용자 정보
	private final Map<String, ChatMessageVo> sessionUsers = new ConcurrentHashMap<>();

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		logger.info("🔗 웹소켓 연결 성공! Session ID: {}, URI: {}", session.getId(), session.getUri());
		// System.out.println("🔗 웹소켓 연결 성공! Session ID: " + session.getId());
	}


    private void removeSession(WebSocketSession session) {
        ChatMessageVo userInfo = sessionUsers.remove(session.getId());
        
        if (userInfo != null) {
            String channelName = userInfo.getChannelName();
            String userId = userInfo.getUserId();
            String userName = userInfo.getUserName();
            
            logger.info("세션 제거 - 사용자: {} ({}), 채널: {}", userName, userId, channelName);
            
            List<WebSocketSession> sessions = channelSessions.get(channelName);
            
            if (sessions != null) {
                sessions.remove(session);
                
                // 🎥 다른 사용자들에게 자동 퇴장 알림 (브라우저 닫기 등)
                try {
                    ChatMessageVo userLeftMessage = new ChatMessageVo();
                    userLeftMessage.setType("user-left");
                    userLeftMessage.setChannelName(channelName);
                    userLeftMessage.setUserId(userId);
                    userLeftMessage.setUserName(userName);
                    userLeftMessage.setTimestamp(System.currentTimeMillis());
                    
                    // 남은 사용자들에게 퇴장 알림 브로드캐스트
                    broadcastToChannel(channelName, userLeftMessage, null);
                    
                    logger.info("사용자 {}의 자동 퇴장 알림 전송 완료", userName);
                    
                } catch (Exception e) {
                    logger.error("자동 퇴장 알림 전송 실패: " + userName, e);
                }
                
                // 채널에 세션이 없으면 채널 제거
                if (sessions.isEmpty()) {
                    channelSessions.remove(channelName);
                    logger.info("빈 채널 제거: {}", channelName);
                }
            }
        } else {
            logger.warn("제거할 세션의 사용자 정보를 찾을 수 없음: {}", session.getId());
        }
    }

	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		try {
			String payload = message.getPayload();
			logger.info("메시지 수신: {}", payload);


			ChatMessageVo chatMessage = objectMapper.readValue(payload, ChatMessageVo.class);

			switch (chatMessage.getType()) {
			case "join":
				handleJoin(session, chatMessage);
				break;
			case "message":
				handleMessage(session, chatMessage);
				break;
			case "history":
				handleHistory(session, chatMessage);
				break;
			case "leave":
				handleLeave(session, chatMessage);
				break;
			case "join-room":
				handleJoinRoom(session, chatMessage);
				break;
			case "leave-room":
				handleLeaveRoom(session, chatMessage);
				break;
			case "offer":
				handleOffer(session, chatMessage);
				break;
			case "answer":
				handleAnswer(session, chatMessage);
				break;
			case "ice-candidate":
				handleIceCandidate(session, chatMessage);
				break;
			case "delete-message":
				handleDeleteMessage(session, chatMessage);
				break;
			default:
				logger.warn("알 수 없는 메시지 타입: {}", chatMessage.getType());
			}
		} catch (Exception e) {

			logger.error("메시지 처리 중 오류 발생", e);
			sendErrorMessage(session, "메시지 처리 중 오류가 발생했습니다.");

		}
	}

	
	private void handleDeleteMessage(WebSocketSession session, ChatMessageVo chatMessage) throws IOException {
		String channelName = chatMessage.getChannelName();

		try {

			// 채널의 모든 세션에 메시지 브로드캐스트
			broadcastToChannel(channelName, chatMessage, null);

		} catch (Exception e) {
			// System.err.println("❌ 메시지 처리 실패: " + e.getMessage());
			e.printStackTrace();
		}
	}

	

	private void handleMessage(WebSocketSession session, ChatMessageVo chatMessage) throws IOException {
		String channelName = chatMessage.getChannelName();

		// 🕒 서버에서 실제 메시지 입력 시간으로 타임스탬프 설정 (중요!)
		chatMessage.setTimestamp(System.currentTimeMillis());
		chatMessage.setMessageId(String.valueOf(chatMessage.getTimestamp()));

		try {
			// Redis에 메시지 저장
			chatService.saveMessage(chatMessage);

			// 채널의 모든 세션에 메시지 브로드캐스트
			broadcastToChannel(channelName, chatMessage, null);

		} catch (Exception e) {
			// System.err.println("❌ 메시지 처리 실패: " + e.getMessage());
			e.printStackTrace();
		}
	}

	private void handleHistory(WebSocketSession session, ChatMessageVo chatMessage) throws IOException {
		try {
			// 히스토리 조회 (타임스탬프 이후)
			List<ChatMessageVo> history = chatService.getChannelMessages(chatMessage.getChannelName());

			// 타임스탬프 필터링이 필요한 경우
			if (chatMessage.getAfterTimestamp() > 0) {
				history = chatService.getMessagesAfterTimestamp(chatMessage.getChannelName(),
						chatMessage.getAfterTimestamp());
			}

			// 히스토리 응답 메시지 생성
			Map<String, Object> historyResponse = new ConcurrentHashMap<>();
			historyResponse.put("type", "history");
			historyResponse.put("messages", history);

			String responseJson = objectMapper.writeValueAsString(historyResponse);
			session.sendMessage(new TextMessage(responseJson));

			logger.info("채팅 히스토리 전송 완료: {} 개 메시지", history.size());

		} catch (Exception e) {
			logger.error("채팅 히스토리 조회 실패", e);
			sendErrorMessage(session, "채팅 히스토리를 불러올 수 없습니다.");
		}
	}

	private void handleLeave(WebSocketSession session, ChatMessageVo chatMessage) throws IOException {
		String channelName = chatMessage.getChannelName();
		String userName = chatMessage.getUserName();

		// 다른 사용자들에게 퇴장 알림
		ChatMessageVo leaveNotification = new ChatMessageVo();
		leaveNotification.setType("notification");
		leaveNotification.setChannelName(channelName);
		leaveNotification.setUserId("system");
		leaveNotification.setUserName("시스템");
		leaveNotification.setMessage(userName + "님이 채팅방을 나갔습니다.");
		leaveNotification.setTimestamp(System.currentTimeMillis());
		broadcastToChannel(channelName, leaveNotification, session);

		removeSession(session);

		logger.info("사용자 {}가 채널 {}에서 나갔습니다.", userName, channelName);
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		removeSession(session);
		logger.info("웹소켓 연결 종료: {}", session.getId());
	}

	@Override
	public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
		logger.error("웹소켓 전송 오류: " + session.getId(), exception);
		removeSession(session);
	}

	

	// 🔧 동시 전송 문제 해결을 위한 동기화된 브로드캐스트
	private synchronized void broadcastToChannel(String channelName, ChatMessageVo message,
			WebSocketSession excludeSession) {
		List<WebSocketSession> sessions = channelSessions.get(channelName);

		if (sessions == null || sessions.isEmpty()) {
			return;
		}

		try {
			String messageJson = objectMapper.writeValueAsString(message);
			List<WebSocketSession> closedSessions = new ArrayList<>();

			for (WebSocketSession session : sessions) {
				if (session.equals(excludeSession)) {
					continue; // 제외할 세션은 건너뛰기
				}

				try {
					if (session.isOpen()) {
						// 동기화된 메시지 전송
						synchronized (session) {
							session.sendMessage(new TextMessage(messageJson));
						}
					} else {
						closedSessions.add(session);
					}
				} catch (Exception e) {
					// System.err.println("❌ 세션 " + session.getId() + "에 메시지 전송 실패: " +
					// e.getMessage());
					closedSessions.add(session);
				}
			}

			// 닫힌 세션들 정리
			for (WebSocketSession closedSession : closedSessions) {
				sessions.remove(closedSession);
				sessionUsers.remove(closedSession.getId());
			}

			int activeSessionCount = sessions.size() - closedSessions.size();
			// System.out.println("📨 메시지 브로드캐스트 완료: " + channelName + " (" +
			// activeSessionCount + "명)");

		} catch (Exception e) {
			// System.err.println("❌ 브로드캐스트 실패: " + e.getMessage());
			e.printStackTrace();
		}
	}

	private void sendErrorMessage(WebSocketSession session, String errorMessage) {
		try {
			Map<String, Object> error = new ConcurrentHashMap<>();
			error.put("type", "error");
			error.put("message", errorMessage);

			String errorJson = objectMapper.writeValueAsString(error);
			session.sendMessage(new TextMessage(errorJson));
		} catch (IOException e) {
			logger.error("오류 메시지 전송 실패", e);
		}
	}

	// 🎥 webRTC 화상통화방 참가 처리
	private void handleJoinRoom(WebSocketSession session, ChatMessageVo chatMessage) throws IOException {
		String channelName = chatMessage.getChannelName();
		String userId = chatMessage.getUserId();
		String userName = chatMessage.getUserName();

		logger.info("사용자 {}가 화상통화방 {}에 참가했습니다.", userName, channelName);

		// 새로운 사용자에게 기존 참가자 목록 전송
		List<WebSocketSession> sessions = channelSessions.get(channelName);
		if (sessions != null) {
			List<String> existingUsers = new ArrayList<>();
			for (WebSocketSession existingSession : sessions) {
				if (!existingSession.equals(session)) {
					ChatMessageVo existingUser = sessionUsers.get(existingSession.getId());
					if (existingUser != null) {
						existingUsers.add(existingUser.getUserId());
					}
				}
			}

			// 새로운 사용자에게 기존 참가자 목록 전송
			if (!existingUsers.isEmpty()) {
				Map<String, Object> roomInfo = new ConcurrentHashMap<>();
				roomInfo.put("type", "existing-users");
				roomInfo.put("users", existingUsers);

				String roomInfoJson = objectMapper.writeValueAsString(roomInfo);
				session.sendMessage(new TextMessage(roomInfoJson));
			}
		}

		// 기존 참가자들에게 새로운 사용자 알림
		ChatMessageVo userJoinedMessage = new ChatMessageVo();
		userJoinedMessage.setType("user-joined");
		userJoinedMessage.setChannelName(channelName);
		userJoinedMessage.setUserId(userId);
		userJoinedMessage.setUserName(userName);

		broadcastToChannel(channelName, userJoinedMessage, session);

		// 채팅 참가도 함께 처리
		handleJoin(session, chatMessage);
	}

	// 🎥 webRTC 화상통화방 나가기 처리
	private void handleLeaveRoom(WebSocketSession session, ChatMessageVo chatMessage) throws IOException {
		String channelName = chatMessage.getChannelName();
		String userId = chatMessage.getUserId();
		String userName = chatMessage.getUserName();

		logger.info("사용자 {}가 화상통화방 {}에서 나갔습니다.", userName, channelName);

		// 다른 참가자들에게 사용자 퇴장 알림
		ChatMessageVo userLeftMessage = new ChatMessageVo();
		userLeftMessage.setType("user-left");
		userLeftMessage.setChannelName(channelName);
		userLeftMessage.setUserId(userId);
		userLeftMessage.setUserName(userName);

		broadcastToChannel(channelName, userLeftMessage, session);

		// 채팅 나가기도 함께 처리
		handleLeave(session, chatMessage);
	}

	// 🎥 webRTC Offer 처리
	private void handleOffer(WebSocketSession session, ChatMessageVo chatMessage) throws IOException {
		String targetUserId = chatMessage.getTargetUserId();
		String channelName = chatMessage.getChannelName();

		logger.info("Offer 전달: {} -> {}", chatMessage.getUserId(), targetUserId);

		// 특정 사용자에게 Offer 전달
		sendToSpecificUser(channelName, targetUserId, chatMessage);
	}

	// 🎥 webRTC Answer 처리
	private void handleAnswer(WebSocketSession session, ChatMessageVo chatMessage) throws IOException {
		String targetUserId = chatMessage.getTargetUserId();
		String channelName = chatMessage.getChannelName();

		logger.info("Answer 전달: {} -> {}", chatMessage.getUserId(), targetUserId);

		// 특정 사용자에게 Answer 전달
		sendToSpecificUser(channelName, targetUserId, chatMessage);
	}

	// 🎥 webRTC ICE Candidate 처리
	private void handleIceCandidate(WebSocketSession session, ChatMessageVo chatMessage) throws IOException {
		String targetUserId = chatMessage.getTargetUserId();
		String channelName = chatMessage.getChannelName();

		logger.info("ICE Candidate 전달: {} -> {}", chatMessage.getUserId(), targetUserId);

		// 특정 사용자에게 ICE Candidate 전달
		sendToSpecificUser(channelName, targetUserId, chatMessage);
	}

	// 🎯 특정 사용자에게 메시지 전송 (webRTC 시그널링용)
	private void sendToSpecificUser(String channelName, String targetUserId, ChatMessageVo message) {
		List<WebSocketSession> sessions = channelSessions.get(channelName);

		if (sessions == null || sessions.isEmpty()) {
			return;
		}

		try {
			String messageJson = objectMapper.writeValueAsString(message);

			for (WebSocketSession session : sessions) {
				ChatMessageVo userInfo = sessionUsers.get(session.getId());
				if (userInfo != null && targetUserId.equals(userInfo.getUserId())) {
					try {
						if (session.isOpen()) {
							synchronized (session) {
								session.sendMessage(new TextMessage(messageJson));
							}
							logger.debug("메시지 전달 완료: -> {}", targetUserId);
							return;
						}
					} catch (Exception e) {
						logger.error("특정 사용자에게 메시지 전송 실패: " + targetUserId, e);
					}
				}
			}

			logger.warn("대상 사용자를 찾을 수 없음: {}", targetUserId);

		} catch (Exception e) {
			logger.error("특정 사용자 메시지 전송 실패", e);
		}
	}
}