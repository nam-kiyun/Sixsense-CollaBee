package com.demo.proworks.collabee.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.demo.proworks.collabee.vo.ChatMessageVo;

@Service
public class ChatService {

    private static final Logger logger = LoggerFactory.getLogger(ChatService.class);
    
    // 메모리 기반 메시지 저장소 (실제 운영에서는 데이터베이스 사용)
    private final Map<String, List<ChatMessageVo>> channelMessages = new ConcurrentHashMap<>();

    /**
     * 채팅 메시지 저장
     */
    public void saveChatMessage(ChatMessageVo chatMessage) {
        try {
            String channelName = chatMessage.getChannelName();
            
            channelMessages.computeIfAbsent(channelName, k -> new ArrayList<>()).add(chatMessage);
            
            logger.info("메시지 저장 완료: 채널={}, 사용자={}, 메시지={}", 
                    channelName, chatMessage.getUserName(), chatMessage.getMessage());
            
        } catch (Exception e) {
            logger.error("메시지 저장 실패", e);
            throw new RuntimeException("메시지 저장에 실패했습니다.", e);
        }
    }

    /**
     * 채팅 히스토리 조회
     */
    public List<ChatMessageVo> getChatHistory(String channelName, Long afterTimestamp) {
        try {
            List<ChatMessageVo> messages = channelMessages.getOrDefault(channelName, new ArrayList<>());
            
            // afterTimestamp 이후의 메시지만 필터링
            if (afterTimestamp != null && afterTimestamp > 0) {
                messages = messages.stream()
                        .filter(msg -> msg.getTimestamp() > afterTimestamp)
                        .collect(Collectors.toList());
            }
            
            logger.info("채팅 히스토리 조회 완료: 채널={}, 메시지 수={}", channelName, messages.size());
            
            return messages;
            
        } catch (Exception e) {
            logger.error("채팅 히스토리 조회 실패", e);
            return new ArrayList<>();
        }
    }

    /**
     * 모든 채팅 메시지 조회
     */
    public List<ChatMessageVo> getAllChatMessages(String channelName) {
        try {
            List<ChatMessageVo> messages = channelMessages.getOrDefault(channelName, new ArrayList<>());
            
            logger.info("전체 채팅 메시지 조회 완료: 채널={}, 메시지 수={}", channelName, messages.size());
            
            return messages;
            
        } catch (Exception e) {
            logger.error("전체 채팅 메시지 조회 실패", e);
            return new ArrayList<>();
        }
    }

    /**
     * 채널의 메시지 개수 조회
     */
    public int getMessageCount(String channelName) {
        return channelMessages.getOrDefault(channelName, new ArrayList<>()).size();
    }

    /**
     * 채널 삭제 (메모리 정리)
     */
    public void clearChannel(String channelName) {
        channelMessages.remove(channelName);
        logger.info("채널 메시지 정리 완료: {}", channelName);
    }

    /**
     * 특정 메시지 삭제
     */
    public boolean deleteMessage(String channelName, String messageId) {
        try {
            List<ChatMessageVo> messages = channelMessages.get(channelName);
            
            if (messages != null) {
                boolean removed = messages.removeIf(msg -> messageId.equals(msg.getMessageId()));
                
                if (removed) {
                    logger.info("메시지 삭제 완료: 채널={}, 메시지ID={}", channelName, messageId);
                }
                
                return removed;
            }
            
            return false;
            
        } catch (Exception e) {
            logger.error("메시지 삭제 실패", e);
            return false;
        }
    }
} 