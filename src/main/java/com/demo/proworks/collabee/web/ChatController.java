package com.demo.proworks.collabee.web;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.inswave.elfw.annotation.ElDescription;
import com.inswave.elfw.annotation.ElService;
import com.demo.proworks.collabee.vo.ChatVo;
import com.demo.proworks.collabee.vo.ChatListVo;

@Controller
public class ChatController {
    
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    
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
            
            redisTemplate.opsForList().rightPush(redisKey, message);
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
            List<Object> redisMessages = redisTemplate.opsForList().range(redisKey, 0, -1);
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
                
                for (Object obj : redisMessages) {
                    if (obj instanceof Map) {
                        @SuppressWarnings("unchecked")
                        Map<String, Object> msgMap = (Map<String, Object>) obj;
                        
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
            List<Object> redisMessages = redisTemplate.opsForList().range(redisKey, 0, -1);
            
            List<ChatVo> chatVoList = new ArrayList<>();
            
            if (redisMessages != null) {
                for (Object obj : redisMessages) {
                    if (obj instanceof Map) {
                        @SuppressWarnings("unchecked")
                        Map<String, Object> msgMap = (Map<String, Object>) obj;
                        
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