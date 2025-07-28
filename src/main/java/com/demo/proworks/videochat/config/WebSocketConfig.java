package com.demo.proworks.videochat.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import com.demo.proworks.videochat.websocket.ChatWebSocketHandler;
import com.demo.proworks.websocket.handler.KanbanWebSocketHandler;

/**
 * 통합 WebSocket 설정 클래스
 * 화상채팅과 칸반보드 WebSocket을 모두 관리
 */
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Autowired
    private ChatWebSocketHandler chatWebSocketHandler;
    
    @Autowired
    private KanbanWebSocketHandler kanbanWebSocketHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        System.out.println("🔧 통합 WebSocket 핸들러 등록 시작...");
        System.out.println("🔧 서버 포트 정보 - 현재 요청 컨텍스트에서 WebSocket 엔드포인트 등록");
        
        try {
            // 화상채팅용 WebSocket (순수 WebSocket)
            registry.addHandler(chatWebSocketHandler, "/chat")
                    .setAllowedOriginPatterns("*");
            System.out.println("✅ 화상채팅 WebSocket 등록 성공: /chat");
            
            // 칸반보드용 WebSocket (단일 경로로 통일)
            registry.addHandler(kanbanWebSocketHandler, "/kanban")
                    .setAllowedOriginPatterns("*");
            System.out.println("✅ 칸반보드 WebSocket 등록 성공: /kanban");
            
        } catch (Exception e) {
            System.err.println("❌ WebSocket 핸들러 등록 실패: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println("🔧 통합 WebSocket 핸들러 등록 완료");
        System.out.println("🌐 WebSocket 엔드포인트:");
        System.out.println("   - /InsWebApp/kanban (칸반보드)");
    }
} 