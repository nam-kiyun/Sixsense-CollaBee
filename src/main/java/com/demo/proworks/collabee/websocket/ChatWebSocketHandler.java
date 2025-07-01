package com.demo.proworks.collabee.websocket;

import java.io.IOException;
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

import com.demo.proworks.collabee.service.ChatService;
import com.demo.proworks.collabee.vo.ChatMessageVo;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class ChatWebSocketHandler extends TextWebSocketHandler {

    static {
        System.out.println("🔥 ChatWebSocketHandler 클래스 로딩됨!");
    }
    
    public ChatWebSocketHandler() {
        System.out.println("🔥 ChatWebSocketHandler 생성자 호출됨!");
    }

    private static final Logger logger = LoggerFactory.getLogger(ChatWebSocketHandler.class);
    
    @Autowired
    private ChatService chatService;
    
    private ObjectMapper objectMapper = new ObjectMapper();
    
    // 세션 관리: 채널별로 세션들을 그룹화
    private final Map<String, List<WebSocketSession>> channelSessions = new ConcurrentHashMap<>();
    
    // 사용자 정보 관리: 세션ID -> 사용자 정보
    private final Map<String, ChatMessageVo> sessionUsers = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        logger.info("🔗 웹소켓 연결 성공! Session ID: {}, URI: {}", session.getId(), session.getUri());
        System.out.println("🔗 웹소켓 연결 성공! Session ID: " + session.getId());
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
                default:
                    logger.warn("알 수 없는 메시지 타입: {}", chatMessage.getType());
            }
            
        } catch (Exception e) {
            logger.error("메시지 처리 중 오류 발생", e);
            sendErrorMessage(session, "메시지 처리 중 오류가 발생했습니다.");
        }
    }

    private void handleJoin(WebSocketSession session, ChatMessageVo chatMessage) throws IOException {
        String channelName = chatMessage.getChannelName();
        
        // 채널에 세션 추가
        channelSessions.computeIfAbsent(channelName, k -> new CopyOnWriteArrayList<>()).add(session);
        
        // 사용자 정보 저장
        sessionUsers.put(session.getId(), chatMessage);
        
        logger.info("사용자 {}가 채널 {}에 참가했습니다.", chatMessage.getUserName(), channelName);
        
        // 다른 사용자들에게 참가 알림
        ChatMessageVo joinNotification = new ChatMessageVo();
        joinNotification.setType("user_joined");
        joinNotification.setChannelName(channelName);
        joinNotification.setUserId(chatMessage.getUserId());
        joinNotification.setUserName(chatMessage.getUserName());
        joinNotification.setMessage(null);
        joinNotification.setTimestamp(System.currentTimeMillis());
        broadcastToChannel(channelName, joinNotification, session.getId());
    }

    private void handleMessage(WebSocketSession session, ChatMessageVo chatMessage) throws IOException {
        String channelName = chatMessage.getChannelName();
        
        // 타임스탬프 설정
        chatMessage.setTimestamp(System.currentTimeMillis());
        chatMessage.setMessageId(String.valueOf(chatMessage.getTimestamp()));
        
        // 데이터베이스에 메시지 저장
        try {
            chatService.saveChatMessage(chatMessage);
        } catch (Exception e) {
            logger.error("메시지 저장 실패", e);
        }
        
        // 채널의 모든 사용자에게 메시지 브로드캐스트
        broadcastToChannel(channelName, chatMessage, null);
        
        logger.info("메시지 브로드캐스트 완료: {}", chatMessage.getMessage());
    }

    private void handleHistory(WebSocketSession session, ChatMessageVo chatMessage) throws IOException {
        try {
            // 데이터베이스에서 이전 메시지 조회
            List<ChatMessageVo> messages = chatService.getChatHistory(
                    chatMessage.getChannelName(), 
                    chatMessage.getAfterTimestamp()
            );
            
            // 히스토리 응답 메시지 생성
            Map<String, Object> historyResponse = new ConcurrentHashMap<>();
            historyResponse.put("type", "history");
            historyResponse.put("messages", messages);
            
            String responseJson = objectMapper.writeValueAsString(historyResponse);
            session.sendMessage(new TextMessage(responseJson));
            
            logger.info("채팅 히스토리 전송 완료: {} 개 메시지", messages.size());
            
        } catch (Exception e) {
            logger.error("채팅 히스토리 조회 실패", e);
            sendErrorMessage(session, "채팅 히스토리를 불러올 수 없습니다.");
        }
    }

    private void handleLeave(WebSocketSession session, ChatMessageVo chatMessage) throws IOException {
        String channelName = chatMessage.getChannelName();
        
        // 다른 사용자들에게 퇴장 알림
        ChatMessageVo leaveNotification = new ChatMessageVo();
        leaveNotification.setType("user_left");
        leaveNotification.setChannelName(channelName);
        leaveNotification.setUserId(chatMessage.getUserId());
        leaveNotification.setUserName(chatMessage.getUserName());
        leaveNotification.setMessage(null);
        leaveNotification.setTimestamp(System.currentTimeMillis());
        broadcastToChannel(channelName, leaveNotification, session.getId());
        
        removeSession(session);
        
        logger.info("사용자 {}가 채널 {}에서 나갔습니다.", chatMessage.getUserName(), channelName);
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

    private void removeSession(WebSocketSession session) {
        ChatMessageVo userInfo = sessionUsers.remove(session.getId());
        
        if (userInfo != null) {
            String channelName = userInfo.getChannelName();
            List<WebSocketSession> sessions = channelSessions.get(channelName);
            
            if (sessions != null) {
                sessions.remove(session);
                
                // 채널에 세션이 없으면 채널 제거
                if (sessions.isEmpty()) {
                    channelSessions.remove(channelName);
                }
            }
        }
    }

    private void broadcastToChannel(String channelName, ChatMessageVo message, String excludeSessionId) throws IOException {
        List<WebSocketSession> sessions = channelSessions.get(channelName);
        
        if (sessions != null) {
            String messageJson = objectMapper.writeValueAsString(message);
            TextMessage textMessage = new TextMessage(messageJson);
            
            for (WebSocketSession session : sessions) {
                if (session.isOpen() && !session.getId().equals(excludeSessionId)) {
                    try {
                        session.sendMessage(textMessage);
                    } catch (IOException e) {
                        logger.error("메시지 전송 실패: " + session.getId(), e);
                        removeSession(session);
                    }
                }
            }
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
} 