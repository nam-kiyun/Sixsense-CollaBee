package com.demo.proworks.videochat.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import com.demo.proworks.videochat.websocket.ChatWebSocketHandler;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Autowired
    private ChatWebSocketHandler chatWebSocketHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        //System.out.println("🔧 WebSocket 핸들러 등록 시작...");
        
        registry.addHandler(chatWebSocketHandler, "/chat")
                .setAllowedOriginPatterns("*"); // SockJS 제거하고 순수 웹소켓만 사용
        
        //System.out.println("🔧 WebSocket 핸들러 등록 완료: /chat (순수 웹소켓)");
    }
} 