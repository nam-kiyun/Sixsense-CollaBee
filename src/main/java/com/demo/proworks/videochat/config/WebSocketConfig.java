package com.demo.proworks.videochat.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import com.demo.proworks.videochat.websocket.ChatWebSocketHandler;
import com.demo.proworks.websocket.handler.KanbanWebSocketHandler;

/**
 * 통합 WebSocket 설정 클래스 화상채팅과 칸반보드 WebSocket을 모두 관리
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
        
        
        try {
            // 화상채팅용 WebSocket (순수 WebSocket)
            registry.addHandler(chatWebSocketHandler, "/chat")
                    .setAllowedOriginPatterns("*");
            
            
            // 칸반보드용 WebSocket (단일 경로로 통일)
            registry.addHandler(kanbanWebSocketHandler, "/kanban")
                    .setAllowedOriginPatterns("*");
            
            
        } catch (Exception e) {
            
            e.printStackTrace();
        }
        
        
    }
} 