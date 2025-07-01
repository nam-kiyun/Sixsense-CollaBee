package com.demo.proworks.collabee.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.demo.proworks.collabee.vo.ChatMessageVo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

@Service
public class ChatService {
    
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    
    private final ObjectMapper objectMapper;
    
    public ChatService() {
        this.objectMapper = new ObjectMapper();
        // 알 수 없는 필드 무시 (@class 등)
        this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        
        // elExcludeFilter 설정 - 모든 속성 허용
        SimpleFilterProvider filterProvider = new SimpleFilterProvider();
        filterProvider.addFilter("elExcludeFilter", SimpleBeanPropertyFilter.serializeAll());
        this.objectMapper.setFilterProvider(filterProvider);
        
    }
    
    private static final String CHAT_MESSAGES_KEY = "chat:messages:";
    private static final String CHANNEL_LIST_KEY = "chat:channels";
    
    /**
     * 메시지 저장 (Redis)
     */
    public void saveMessage(ChatMessageVo message) {
        try {
            String channelKey = CHAT_MESSAGES_KEY + message.getChannelName();
            
            // ChatMessageVo를 JSON 문자열로 변환
            String jsonMessage = objectMapper.writeValueAsString(message);
            
            // Redis List에 JSON 문자열 추가 (오른쪽 끝에 추가)
            redisTemplate.opsForList().rightPush(channelKey, jsonMessage);
            
            // 채널 목록에 채널명 추가 (Set으로 중복 방지)
            redisTemplate.opsForSet().add(CHANNEL_LIST_KEY, message.getChannelName());
            
            
            
        } catch (JsonProcessingException e) {
            
            e.printStackTrace();
        } catch (Exception e) {
            
            e.printStackTrace();
        }
    }
    
    /**
     * 채널의 메시지 조회 (Redis)
     */
    public List<ChatMessageVo> getChannelMessages(String channelName) {
        try {
            String channelKey = CHAT_MESSAGES_KEY + channelName;
            
            // Redis List에서 모든 메시지 조회 (0부터 -1까지 = 전체)
            List<String> rawMessages = redisTemplate.opsForList().range(channelKey, 0, -1);
            
            if (rawMessages == null || rawMessages.isEmpty()) {
                //System.out.println("📭 Redis에서 조회된 메시지 없음: " + channelName);
                return new ArrayList<>();
            }
            
            // JSON 문자열을 ChatMessageVo로 변환
            List<ChatMessageVo> messages = new ArrayList<>();
            for (String jsonMessage : rawMessages) {
                try {
                    ChatMessageVo message = objectMapper.readValue(jsonMessage, ChatMessageVo.class);
                    if (message != null) {
                        messages.add(message);
                    }
                } catch (Exception e) {
                    //System.err.println("⚠️ JSON 파싱 실패 (무시): " + jsonMessage + " - " + e.getMessage());
                    // 기존 데이터가 호환되지 않는 경우 무시하고 계속 진행
                }
            }
                
            //System.out.println("📬 Redis에서 메시지 조회: " + channelName + " (" + messages.size() + "개)");
            return messages;
            
        } catch (Exception e) {
            //System.err.println("❌ Redis 메시지 조회 실패: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    
    /**
     * 특정 시간 이후의 메시지 조회 (Redis)
     */
    public List<ChatMessageVo> getMessagesAfterTimestamp(String channelName, long timestamp) {
        try {
            List<ChatMessageVo> allMessages = getChannelMessages(channelName);
            
            // 타임스탬프 필터링
            List<ChatMessageVo> filteredMessages = allMessages.stream()
                .filter(msg -> msg.getTimestamp() > timestamp)
                .collect(Collectors.toList());
                
            //System.out.println("⏰ 시간 필터링 메시지: " + channelName + " (" + filteredMessages.size() + "개)");
            return filteredMessages;
            
        } catch (Exception e) {
            //System.err.println("❌ Redis 시간 필터링 실패: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    
    /**
     * 채널의 최신 메시지 개수 제한 조회 (Redis)
     */
    public List<ChatMessageVo> getRecentMessages(String channelName, int limit) {
        try {
            String channelKey = CHAT_MESSAGES_KEY + channelName;
            
            // Redis List에서 최신 N개 메시지 조회 (오른쪽 끝에서부터)
            long totalSize = redisTemplate.opsForList().size(channelKey);
            long start = Math.max(0, totalSize - limit);
            List<String> rawMessages = redisTemplate.opsForList().range(channelKey, start, -1);
            
            if (rawMessages == null || rawMessages.isEmpty()) {
                return new ArrayList<>();
            }
            
            // JSON 문자열을 ChatMessageVo로 변환
            List<ChatMessageVo> messages = new ArrayList<>();
            for (String jsonMessage : rawMessages) {
                try {
                    ChatMessageVo message = objectMapper.readValue(jsonMessage, ChatMessageVo.class);
                    if (message != null) {
                        messages.add(message);
                    }
                } catch (Exception e) {
                    //System.err.println("⚠️ JSON 파싱 실패 (무시): " + jsonMessage + " - " + e.getMessage());
                    // 기존 데이터가 호환되지 않는 경우 무시하고 계속 진행
                }
            }
                
           // System.out.println("📱 Redis에서 최신 메시지 조회: " + channelName + " (최대 " + limit + "개, 실제 " + messages.size() + "개)");
            return messages;
            
        } catch (Exception e) {
            //System.err.println("❌ Redis 최신 메시지 조회 실패: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    
    /**
     * 모든 채널 목록 조회 (Redis)
     */
    public List<String> getAllChannels() {
        try {
            // Redis Set에서 채널 목록 조회
            return new ArrayList<>(redisTemplate.opsForSet().members(CHANNEL_LIST_KEY));
                
        } catch (Exception e) {
            //System.err.println("❌ Redis 채널 목록 조회 실패: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    
    /**
     * 채널 삭제 (Redis) - 개발/테스트용
     */
    public void deleteChannel(String channelName) {
        try {
            String channelKey = CHAT_MESSAGES_KEY + channelName;
            
            // 메시지 삭제
            redisTemplate.delete(channelKey);
            
            // 채널 목록에서 제거
            redisTemplate.opsForSet().remove(CHANNEL_LIST_KEY, channelName);
            
            //System.out.println("🗑️ Redis에서 채널 삭제: " + channelName);
            
        } catch (Exception e) {
            //System.err.println("❌ Redis 채널 삭제 실패: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Redis 연결 상태 확인
     */
    public boolean isRedisConnected() {
        try {
            redisTemplate.opsForValue().set("test:connection", "OK");
            String result = redisTemplate.opsForValue().get("test:connection");
            redisTemplate.delete("test:connection");
            
            boolean connected = "OK".equals(result);
            //System.out.println("🔍 Redis 연결 상태: " + (connected ? "정상" : "실패"));
            return connected;
            
        } catch (Exception e) {
            //System.err.println("❌ Redis 연결 확인 실패: " + e.getMessage());
            return false;
        }
    }
} 