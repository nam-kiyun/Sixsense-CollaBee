package com.demo.proworks.videochat.web;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.fasterxml.jackson.core.type.TypeReference;

import com.inswave.elfw.annotation.ElDescription;
import com.inswave.elfw.annotation.ElService;
import com.demo.proworks.videochat.vo.ChatVo;
import com.demo.proworks.videochat.vo.ChatListVo;
import com.demo.proworks.videochat.service.ChatService;
import com.demo.proworks.videochat.vo.ChatMessageVo;

@Controller
public class ChatController {
    
    private static final Logger logger = LoggerFactory.getLogger(ChatController.class);
    
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    
    @Autowired
    private ChatService chatService;
    
    private final ObjectMapper objectMapper;
    
    public ChatController() {
        this.objectMapper = new ObjectMapper();
        // 알 수 없는 필드 무시 (@class 등)
        this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        
        // elExcludeFilter 설정 - 모든 속성 허용
        SimpleFilterProvider filterProvider = new SimpleFilterProvider();
        filterProvider.addFilter("elExcludeFilter", SimpleBeanPropertyFilter.serializeAll());
        this.objectMapper.setFilterProvider(filterProvider);
        
        
    }
    
    // Redis에서 사용할 키 접두사
    private static final String REDIS_CHAT_PREFIX = "chat:messages:";
    
    /**
     * 채팅 메시지 전송
     */
    @ElService(key = "ChatSend")
    @RequestMapping(value = "ChatSend")
    @ElDescription(sub = "채팅 메시지 전송", desc = "채팅 메시지를 Redis에 저장")
    public ChatListVo sendMessage(ChatVo chatVo, HttpServletRequest request) {
        ChatListVo result = new ChatListVo();
        
        
        
        try {
            // 현재 시간을 메시지 ID로 사용
            long messageId = System.currentTimeMillis();
            
            // Redis에 저장할 메시지 객체 생성
            Map<String, Object> message = new HashMap<>();
            message.put("messageId", String.valueOf(messageId));
            message.put("channelName", chatVo.getChannelName());
            message.put("userId", chatVo.getUserId());
            message.put("userName", chatVo.getUserName());
            message.put("message", chatVo.getMessage());
            message.put("timestamp", String.valueOf(messageId));
            
            // Redis에 메시지 저장 (리스트로 저장)
            String redisKey = REDIS_CHAT_PREFIX + chatVo.getChannelName();
            
            
            // Map을 JSON 문자열로 변환해서 저장
            String jsonMessage = objectMapper.writeValueAsString(message);
            redisTemplate.opsForList().rightPush(redisKey, jsonMessage);
            
            
            // 메시지 개수 제한 (최대 1000개)
            Long listSize = redisTemplate.opsForList().size(redisKey);
            
            if (listSize > 1000) {
                redisTemplate.opsForList().leftPop(redisKey);
                
            }
            
            // TTL 설정 (24시간)
            redisTemplate.expire(redisKey, Duration.ofHours(24));
            
            
            result.setSuccess(true);
            result.setResultMessage("메시지가 전송되었습니다.");
            result.setCount(1);
            
            
        } catch (JsonProcessingException e) {
            
            e.printStackTrace();
            result.setSuccess(false);
            result.setResultMessage("메시지 JSON 변환 중 오류가 발생했습니다: " + e.getMessage());
            result.setCount(0);
        } catch (Exception e) {
            
            e.printStackTrace();
            result.setSuccess(false);
            result.setResultMessage("메시지 전송 중 오류가 발생했습니다: " + e.getMessage());
            result.setCount(0);
        }
        
        return result;
    }
    
    /**
     * 채팅 메시지 조회
     */
    @ElService(key = "ChatMessages")
    @RequestMapping(value = "ChatMessages")
    @ElDescription(sub = "채팅 메시지 조회", desc = "Redis에서 채팅 메시지 목록 조회")
    public ChatListVo getMessages(ChatVo chatVo, HttpServletRequest request) {
        ChatListVo result = new ChatListVo();
        
        
        
        try {
            String redisKey = REDIS_CHAT_PREFIX + chatVo.getChannelName();
            
            
            // Redis에서 메시지 목록 조회
            List<String> redisMessages = redisTemplate.opsForList().range(redisKey, 0, -1);
            
            
            List<ChatVo> chatVoList = new ArrayList<>();
            
            if (redisMessages != null) {
                long afterTimestamp = 0;
                try {
                    String afterTimestampStr = chatVo.getAfterTimestamp();
                    if (afterTimestampStr != null && !afterTimestampStr.isEmpty()) {
                        afterTimestamp = Long.parseLong(afterTimestampStr);
                    }
                } catch (NumberFormatException e) {
                    afterTimestamp = 0;
                }
                
                for (String jsonMessage : redisMessages) {
                    try {
                        // JSON 문자열을 Map으로 변환
                        Map<String, Object> msgMap = objectMapper.readValue(jsonMessage, 
                            new TypeReference<Map<String, Object>>() {});
                        
                        // afterTimestamp 이후의 메시지만 필터링
                        String timestampStr = (String) msgMap.get("timestamp");
                        long timestamp = 0;
                        if (timestampStr != null) {
                            try {
                                timestamp = Long.parseLong(timestampStr);
                            } catch (NumberFormatException e) {
                                timestamp = 0;
                            }
                        }
                        
                        if (timestamp > afterTimestamp) {
                            // Map을 ChatVo 객체로 변환
                            ChatVo msgVo = new ChatVo();
                            msgVo.setMessageId((String) msgMap.get("messageId"));
                            msgVo.setChannelName((String) msgMap.get("channelName"));
                            msgVo.setUserId((String) msgMap.get("userId"));
                            msgVo.setUserName((String) msgMap.get("userName"));
                            msgVo.setMessage((String) msgMap.get("message"));
                            msgVo.setTimestamp((String) msgMap.get("timestamp"));
                            
                            chatVoList.add(msgVo);
                        }
                    } catch (Exception e) {
                        
                        // 파싱 실패한 메시지는 무시하고 계속 진행
                    }
                }
            }
            
            result.setChatVo(chatVoList);
            result.setSuccess(true);
            result.setResultMessage("메시지 조회 성공");
            result.setCount(chatVoList.size());
            
            
            
        } catch (Exception e) {
            
            e.printStackTrace();
            result.setSuccess(false);
            result.setResultMessage("메시지 조회 중 오류가 발생했습니다: " + e.getMessage());
            result.setChatVo(new ArrayList<>());
            result.setCount(0);
        }
        
        return result;
    }
    
    /**
     * 모든 채팅 메시지 조회
     */
    @ElService(key = "ChatAll")
    @RequestMapping(value = "ChatAll")
    @ElDescription(sub = "모든 채팅 메시지 조회", desc = "채널의 모든 메시지 조회")
    public ChatListVo getAllMessages(ChatVo chatVo, HttpServletRequest request) {
        ChatListVo result = new ChatListVo();
        
        try {
            String redisKey = REDIS_CHAT_PREFIX + chatVo.getChannelName();
            
            // Redis에서 모든 메시지 조회
            List<String> redisMessages = redisTemplate.opsForList().range(redisKey, 0, -1);
            
            List<ChatVo> chatVoList = new ArrayList<>();
            
            if (redisMessages != null) {
                for (String jsonMessage : redisMessages) {
                    try {
                        // JSON 문자열을 Map으로 변환
                        Map<String, Object> msgMap = objectMapper.readValue(jsonMessage, 
                            new TypeReference<Map<String, Object>>() {});
                        
                        // Map을 ChatVo 객체로 변환
                        ChatVo msgVo = new ChatVo();
                        msgVo.setMessageId((String) msgMap.get("messageId"));
                        msgVo.setChannelName((String) msgMap.get("channelName"));
                        msgVo.setUserId((String) msgMap.get("userId"));
                        msgVo.setUserName((String) msgMap.get("userName"));
                        msgVo.setMessage((String) msgMap.get("message"));
                        msgVo.setTimestamp((String) msgMap.get("timestamp"));
                        
                        chatVoList.add(msgVo);
                    } catch (Exception e) {
                        
                        // 파싱 실패한 메시지는 무시하고 계속 진행
                    }
                }
            }
            
            result.setChatVo(chatVoList);
            result.setSuccess(true);
            result.setResultMessage("전체 메시지 조회 성공");
            result.setCount(chatVoList.size());
            
        } catch (Exception e) {
            result.setSuccess(false);
            result.setResultMessage("메시지 조회 중 오류가 발생했습니다: " + e.getMessage());
            result.setChatVo(new ArrayList<>());
            result.setCount(0);
        }
        
        return result;
    }
} 