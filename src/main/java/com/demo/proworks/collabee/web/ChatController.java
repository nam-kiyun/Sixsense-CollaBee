package com.demo.proworks.collabee.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.inswave.elfw.annotation.ElDescription;
import com.inswave.elfw.annotation.ElService;
import com.demo.proworks.collabee.vo.ChatVo;
import com.demo.proworks.collabee.vo.ChatListVo;

@Controller
public class ChatController {
    
    // 메모리에 채팅 메시지 저장 (간단한 구현용)
    private static final Map<String, List<Map<String, Object>>> channelMessages = new ConcurrentHashMap<>();
    
    /**
     * 채팅 메시지 전송
     */
    @ElService(key = "ChatSend")
    @RequestMapping(value = "ChatSend")
    @ElDescription(sub = "채팅 메시지 전송", desc = "채팅방에 메시지를 전송합니다")
    public ChatListVo sendMessage(ChatVo chatVo) {
        ChatListVo result = new ChatListVo();
        
        try {
            String channelName = chatVo.getChannelName();
            String userId = chatVo.getUserId();
            String userName = chatVo.getUserName();
            String message = chatVo.getMessage();
            
            // 메시지 객체 생성
            Map<String, Object> messageObj = new HashMap<>();
            String messageId = String.valueOf(System.currentTimeMillis());
            messageObj.put("messageId", messageId);
            messageObj.put("channelName", channelName);
            messageObj.put("userId", userId);
            messageObj.put("userName", userName);
            messageObj.put("message", message);
            messageObj.put("timestamp", String.valueOf(System.currentTimeMillis()));
            
            // 채널별 메시지 저장
            channelMessages.computeIfAbsent(channelName, k -> new ArrayList<>()).add(messageObj);
            
            result.setSuccess(true);
            result.setResultMessage("메시지 전송 성공");
            result.setCount(1);
            
        } catch (Exception e) {
            result.setSuccess(false);
            result.setResultMessage("메시지 전송 실패: " + e.getMessage());
            result.setCount(0);
        }
        
        return result;
    }
    
    /**
     * 채팅 메시지 조회 (특정 시간 이후)
     */
    @ElService(key = "ChatMessages")
    @RequestMapping(value = "ChatMessages")
    @ElDescription(sub = "새 채팅 메시지 조회", desc = "특정 시간 이후의 새 메시지를 조회합니다")
    public List<ChatVo> getMessages(ChatVo chatVo) {
        List<ChatVo> result = new ArrayList<>();
        
        try {
            String channelName = chatVo.getChannelName();
            String afterTimestamp = chatVo.getAfterTimestamp();
            long timestamp = 0;
            
            if (afterTimestamp != null && !afterTimestamp.isEmpty()) {
                timestamp = Long.parseLong(afterTimestamp);
            }
            
            List<Map<String, Object>> messages = channelMessages.getOrDefault(channelName, new ArrayList<>());
            
            // afterTimestamp 이후의 메시지만 필터링
            for (Map<String, Object> msg : messages) {
                String msgTimestamp = (String) msg.get("messageId");
                if (Long.parseLong(msgTimestamp) > timestamp) {
                    ChatVo msgVo = new ChatVo();
                    msgVo.setMessageId((String) msg.get("messageId"));
                    msgVo.setChannelName((String) msg.get("channelName"));
                    msgVo.setUserId((String) msg.get("userId"));
                    msgVo.setUserName((String) msg.get("userName"));
                    msgVo.setMessage((String) msg.get("message"));
                    msgVo.setTimestamp((String) msg.get("timestamp"));
                    result.add(msgVo);
                }
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return result;
    }
    
    /**
     * 채널의 모든 메시지 조회
     */
    @ElService(key = "ChatAll")
    @RequestMapping(value = "ChatAll")
    @ElDescription(sub = "전체 채팅 메시지 조회", desc = "채널의 모든 메시지를 조회합니다")
    public List<ChatVo> getAllMessages(ChatVo chatVo) {
        List<ChatVo> result = new ArrayList<>();
        
        try {
            String channelName = chatVo.getChannelName();
            List<Map<String, Object>> messages = channelMessages.getOrDefault(channelName, new ArrayList<>());
            
            for (Map<String, Object> msg : messages) {
                ChatVo msgVo = new ChatVo();
                msgVo.setMessageId((String) msg.get("messageId"));
                msgVo.setChannelName((String) msg.get("channelName"));
                msgVo.setUserId((String) msg.get("userId"));
                msgVo.setUserName((String) msg.get("userName"));
                msgVo.setMessage((String) msg.get("message"));
                msgVo.setTimestamp((String) msg.get("timestamp"));
                result.add(msgVo);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return result;
    }
    
    /**
     * 채팅방 정보 조회
     */
    @ElService(key = "ChatInfo")
    @RequestMapping(value = "ChatInfo")
    @ElDescription(sub = "채팅방 정보 조회", desc = "채팅방의 기본 정보를 조회합니다")
    public ChatListVo getChannelInfo(ChatVo chatVo) {
        ChatListVo result = new ChatListVo();
        
        try {
            String channelName = chatVo.getChannelName();
            List<Map<String, Object>> messages = channelMessages.getOrDefault(channelName, new ArrayList<>());
            
            result.setSuccess(true);
            result.setResultMessage("채널 정보 조회 성공");
            result.setCount(messages.size());
            
        } catch (Exception e) {
            result.setSuccess(false);
            result.setResultMessage("채널 정보 조회 실패: " + e.getMessage());
            result.setCount(0);
        }
        
        return result;
    }
} 