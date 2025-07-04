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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

@RestController
@RequestMapping("/InsWebApp")
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
        
        //System.out.println("✅ ChatController ObjectMapper 설정 완료 - elExcludeFilter 및 알 수 없는 필드 처리");
    }
    
    // Redis에서 사용할 키 접두사
    private static final String REDIS_CHAT_PREFIX = "chat:messages:";
    
    /**
     * 채팅 메시지 전송 (HTTP 폴백용)
     */
    @PostMapping("/ChatSend.pwkjson")
    public ResponseEntity<Map<String, Object>> sendChatMessage(@RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> chatVoMap = (Map<String, Object>) request.get("chatVo");
            
            if (chatVoMap == null) {
                response.put("resCode", "FAIL.SVR.001");
                response.put("resMsg", "채팅 데이터가 없습니다.");
                return ResponseEntity.badRequest().body(response);
            }
            
            // ChatMessageVo 생성
            ChatMessageVo chatMessage = new ChatMessageVo();
            chatMessage.setType("message");
            chatMessage.setChannelName((String) chatVoMap.get("channelName"));
            chatMessage.setUserId((String) chatVoMap.get("userId"));
            chatMessage.setUserName((String) chatVoMap.get("userName"));
            chatMessage.setMessage((String) chatVoMap.get("message"));
            chatMessage.setTimestamp(System.currentTimeMillis());
            chatMessage.setMessageId(String.valueOf(chatMessage.getTimestamp()));
            
            // 메시지 저장
            chatService.saveMessage(chatMessage);
            
            response.put("resCode", "SUCC.SVR.001");
            response.put("resMsg", "메시지 전송 성공");
            response.put("messageId", chatMessage.getMessageId());
            
            logger.info("HTTP 메시지 전송 성공: {}", chatMessage.getMessage());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("HTTP 메시지 전송 실패", e);
            
            response.put("resCode", "FAIL.SVR.002");
            response.put("resMsg", "메시지 전송 중 오류가 발생했습니다.");
            
            return ResponseEntity.internalServerError().body(response);
        }
    }

    /**
     * 모든 채팅 메시지 조회 (HTTP 폴백용)
     */
    @PostMapping("/ChatAll.pwkjson")
    public ResponseEntity<Map<String, Object>> getAllChatMessages(@RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> chatVoMap = (Map<String, Object>) request.get("chatVo");
            
            if (chatVoMap == null) {
                response.put("resCode", "FAIL.SVR.001");
                response.put("resMsg", "채팅 데이터가 없습니다.");
                return ResponseEntity.badRequest().body(response);
            }
            
            String channelName = (String) chatVoMap.get("channelName");
            List<ChatMessageVo> messages = chatService.getChannelMessages(channelName);
            
            Map<String, Object> elData = new HashMap<>();
            elData.put("chatVo", messages);
            
            response.put("elData", elData);
            response.put("resCode", "SUCC.SVR.001");
            response.put("resMsg", "메시지 조회 성공");
            
            logger.info("HTTP 전체 메시지 조회 성공: 채널={}, 메시지 수={}", channelName, messages.size());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("HTTP 전체 메시지 조회 실패", e);
            
            response.put("resCode", "FAIL.SVR.002");
            response.put("resMsg", "메시지 조회 중 오류가 발생했습니다.");
            
            return ResponseEntity.internalServerError().body(response);
        }
    }

    /**
     * 새 채팅 메시지 조회 (HTTP 폴백용)
     */
    @PostMapping("/ChatMessages.pwkjson")
    public ResponseEntity<Map<String, Object>> getNewChatMessages(@RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> chatVoMap = (Map<String, Object>) request.get("chatVo");
            
            if (chatVoMap == null) {
                response.put("resCode", "FAIL.SVR.001");
                response.put("resMsg", "채팅 데이터가 없습니다.");
                return ResponseEntity.badRequest().body(response);
            }
            
            String channelName = (String) chatVoMap.get("channelName");
            String afterTimestampStr = (String) chatVoMap.get("afterTimestamp");
            
            Long afterTimestamp = null;
            if (afterTimestampStr != null && !afterTimestampStr.isEmpty()) {
                try {
                    afterTimestamp = Long.parseLong(afterTimestampStr);
                } catch (NumberFormatException e) {
                    logger.warn("타임스탬프 파싱 실패: {}", afterTimestampStr);
                }
            }
            
            List<ChatMessageVo> messages = chatService.getMessagesAfterTimestamp(channelName, afterTimestamp);
            
            Map<String, Object> elData = new HashMap<>();
            elData.put("chatVo", messages);
            
            response.put("elData", elData);
            response.put("resCode", "SUCC.SVR.001");
            response.put("resMsg", "새 메시지 조회 성공");
            
            logger.info("HTTP 새 메시지 조회 성공: 채널={}, 메시지 수={}", channelName, messages.size());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("HTTP 새 메시지 조회 실패", e);
            
            response.put("resCode", "FAIL.SVR.002");
            response.put("resMsg", "새 메시지 조회 중 오류가 발생했습니다.");
            
            return ResponseEntity.internalServerError().body(response);
        }
    }
    
    /**
     * 채팅 메시지 전송
     */
    @ElService(key = "ChatSend")
    @RequestMapping(value = "ChatSend")
    @ElDescription(sub = "채팅 메시지 전송", desc = "채팅 메시지를 Redis에 저장")
    public ChatListVo sendMessage(ChatVo chatVo, HttpServletRequest request) {
        ChatListVo result = new ChatListVo();
        
        // 디버깅 로그 추가
        System.out.println("=== 채팅 메시지 전송 요청 ===");
        System.out.println("channelName: " + chatVo.getChannelName());
        System.out.println("userId: " + chatVo.getUserId());
        System.out.println("userName: " + chatVo.getUserName());
        System.out.println("message: " + chatVo.getMessage());
        
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
            System.out.println("Redis 키: " + redisKey);
            System.out.println("저장할 메시지: " + message);
            
            // Map을 JSON 문자열로 변환해서 저장
            String jsonMessage = objectMapper.writeValueAsString(message);
            redisTemplate.opsForList().rightPush(redisKey, jsonMessage);
            System.out.println("Redis에 메시지 저장 완료");
            
            // 메시지 개수 제한 (최대 1000개)
            Long listSize = redisTemplate.opsForList().size(redisKey);
            System.out.println("현재 메시지 개수: " + listSize);
            if (listSize > 1000) {
                redisTemplate.opsForList().leftPop(redisKey);
                System.out.println("오래된 메시지 삭제");
            }
            
            // TTL 설정 (24시간)
            redisTemplate.expire(redisKey, Duration.ofHours(24));
            System.out.println("TTL 설정 완료");
            
            result.setSuccess(true);
            result.setResultMessage("메시지가 전송되었습니다.");
            result.setCount(1);
            System.out.println("메시지 전송 성공!");
            System.out.println("=== 메시지 전송 완료 ===");
            
        } catch (JsonProcessingException e) {
            System.out.println("❌ JSON 직렬화 오류: " + e.getMessage());
            e.printStackTrace();
            result.setSuccess(false);
            result.setResultMessage("메시지 JSON 변환 중 오류가 발생했습니다: " + e.getMessage());
            result.setCount(0);
        } catch (Exception e) {
            System.out.println("❌ 메시지 전송 오류: " + e.getMessage());
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
        
        // 디버깅 로그 추가
        System.out.println("=== 채팅 메시지 조회 요청 ===");
        System.out.println("channelName: " + chatVo.getChannelName());
        System.out.println("afterTimestamp: " + chatVo.getAfterTimestamp());
        
        try {
            String redisKey = REDIS_CHAT_PREFIX + chatVo.getChannelName();
            System.out.println("조회할 Redis 키: " + redisKey);
            
            // Redis에서 메시지 목록 조회
            List<String> redisMessages = redisTemplate.opsForList().range(redisKey, 0, -1);
            System.out.println("Redis에서 조회된 전체 메시지 개수: " + (redisMessages != null ? redisMessages.size() : 0));
            
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
                        System.err.println("⚠️ JSON 파싱 실패 (무시): " + jsonMessage + " - " + e.getMessage());
                        // 파싱 실패한 메시지는 무시하고 계속 진행
                    }
                }
            }
            
            result.setChatVo(chatVoList);
            result.setSuccess(true);
            result.setResultMessage("메시지 조회 성공");
            result.setCount(chatVoList.size());
            
            System.out.println("반환할 메시지 개수: " + chatVoList.size());
            System.out.println("=== 메시지 조회 완료 ===");
            
        } catch (Exception e) {
            System.out.println("❌ 메시지 조회 오류: " + e.getMessage());
            e.printStackTrace();
            result.setSuccess(false);
            result.setResultMessage("메시지 조회 중 오류가 발생했습니다: " + e.getMessage());
            result.setChatVo(new ArrayList<>());
            result.setCount(0);
        }
        
        return result;
    }
    
    /**
     * 모든 채팅 메시지 조회 (관리용)
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
                        System.err.println("⚠️ JSON 파싱 실패 (무시): " + jsonMessage + " - " + e.getMessage());
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