package com.demo.proworks.redis.service;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.demo.proworks.websocket.message.KanbanMessage;

/**
 * 칸반 보드 Redis 캐싱 서비스
 * 실시간 데이터 임시 저장 및 배치 처리를 위한 Redis 관리
 * 
 * @author Claude AI
 * @since 2025-01-15
 */
@Service
public class KanbanRedisService {

    @Autowired
    private RedisTemplate<String, Object> kanbanRedisTemplate;
    
    private final ObjectMapper objectMapper;
    
    // 생성자에서 ObjectMapper 설정
    public KanbanRedisService() {
        this.objectMapper = new ObjectMapper();
        // @JsonFilter 어노테이션 무시 설정
        this.objectMapper.configure(MapperFeature.USE_ANNOTATIONS, false);
        this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }
    
    /**
     * 서버 시작 시 Redis 캐시 초기화
     * 이전 서버 실행의 오래된 캐시 데이터를 제거하여 데이터 일관성 보장
     */
    @PostConstruct
    public void initializeCache() {
        System.out.println("🚀 서버 시작 - Redis 캐시 초기화 시작");
        try {
            // 프로젝트 관련 캐시 삭제
            deleteKeysByPattern("kanban:project:*");
            
            // 배치 큐 정리
            long queueSize = getBatchQueueSize();
            if (queueSize > 0) {
                kanbanRedisTemplate.delete(BATCH_QUEUE_KEY);
                System.out.println("🧹 배치 큐 정리 완료: " + queueSize + "개 항목 삭제");
            }
            
            // 만료된 태스크 이동 데이터 정리
            deleteKeysByPattern(TASK_MOVE_KEY + "*");
            
            System.out.println("✅ 서버 시작 시 Redis 캐시 초기화 완료 - 새로운 세션에서 최신 DB 데이터로 시작됩니다");
            
        } catch (Exception e) {
            System.err.println("❌ 서버 시작 시 Redis 캐시 초기화 실패: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    // Redis 키 상수
    private static final String TASK_MOVE_KEY = "kanban:task:move:";
    private static final String BATCH_QUEUE_KEY = "kanban:batch:queue";
    private static final String USER_SESSION_KEY = "kanban:user:session:";
    private static final String BOARD_CACHE_KEY = "kanban:board:cache:";
    private static final String PROJECT_BOARDS_CACHE_KEY = "kanban:project:";
    private static final String PROJECT_TASKS_CACHE_KEY = "kanban:project:";
    
    // TTL 설정 최적화 (초)
    private static final long TASK_MOVE_TTL = 300; // 5분 (빠른 처리를 위해 단축)
    private static final long USER_SESSION_TTL = 3600; // 1시간 (사용자 세션 연장)
    private static final long BOARD_CACHE_TTL = 600; // 10분 (캐시 효율성 향상)
    private static final long PROJECT_DATA_CACHE_TTL = 600; // 10분 (프로젝트 데이터 캐시)

    /**
     * 태스크 이동 정보를 Redis에 저장
     * 디바운싱을 위한 임시 저장
     */
    public void saveTaskMove(String taskId, String fromBoardId, String toBoardId, String userId) {
        try {
            KanbanMessage moveMessage = new KanbanMessage();
            moveMessage.setType("CARD_MOVE");
            moveMessage.setTaskId(taskId);
            moveMessage.setFromBoardId(fromBoardId);
            moveMessage.setToBoardId(toBoardId);
            moveMessage.setUserId(userId);
            moveMessage.setTimestamp(System.currentTimeMillis());
            
            String key = TASK_MOVE_KEY + taskId;
            String json = objectMapper.writeValueAsString(moveMessage);
            
            kanbanRedisTemplate.opsForValue().set(key, json, TASK_MOVE_TTL, TimeUnit.SECONDS);
            
            // 배치 처리 큐에 추가
            kanbanRedisTemplate.opsForList().leftPush(BATCH_QUEUE_KEY, taskId);
            
            System.out.println("Redis에 태스크 이동 정보 저장: " + taskId);
            
        } catch (JsonProcessingException e) {
            System.err.println("태스크 이동 정보 저장 실패: " + e.getMessage());
        }
    }

    /**
     * WebSocket 메시지 기반 태스크 이동 정보 저장 (디바운싱된 메시지용)
     */
    public void saveTaskMove(String taskId, String fromBoardId, String toBoardId, 
                           String userId, String projectId) {
        try {
            // KanbanMessage 객체 생성
            KanbanMessage moveMessage = new KanbanMessage();
            moveMessage.setType("CARD_MOVE");
            moveMessage.setTaskId(taskId);
            moveMessage.setFromBoardId(fromBoardId);
            moveMessage.setToBoardId(toBoardId);
            moveMessage.setUserId(userId);
            moveMessage.setProjectId(projectId);
            moveMessage.setTimestamp(System.currentTimeMillis());
            
            // Redis에 저장
            String key = TASK_MOVE_KEY + taskId;
            String json = objectMapper.writeValueAsString(moveMessage);
            
            // TTL 5분으로 설정
            kanbanRedisTemplate.opsForValue().set(key, json, Duration.ofMinutes(5));
            
            // 배치 처리 큐에 추가
            kanbanRedisTemplate.opsForList().leftPush(BATCH_QUEUE_KEY, taskId);
            
            System.out.println("📝 Redis에 디바운싱된 태스크 이동 저장: " + taskId + " (" + fromBoardId + " → " + toBoardId + ")");
            
        } catch (JsonProcessingException e) {
            System.err.println("❌ 디바운싱된 태스크 이동 저장 실패: " + e.getMessage());
            throw new RuntimeException("Redis 저장 실패", e);
        }
    }

    /**
     * 태스크 이동 정보를 Redis에서 조회
     */
    public KanbanMessage getTaskMove(String taskId) {
        try {
            String key = TASK_MOVE_KEY + taskId;
            String json = (String) kanbanRedisTemplate.opsForValue().get(key);
            
            if (json != null) {
                return objectMapper.readValue(json, KanbanMessage.class);
            }
            
        } catch (JsonProcessingException e) {
            System.err.println("태스크 이동 정보 조회 실패: " + e.getMessage());
        }
        
        return null;
    }

    /**
     * 프로젝트 보드 목록 캐시에서 조회
     */
    public String getCachedBoards(String projectId) {
        try {
            String key = PROJECT_BOARDS_CACHE_KEY + projectId + ":boards";
            return (String) kanbanRedisTemplate.opsForValue().get(key);
        } catch (Exception e) {
            System.err.println("보드 캐시 조회 실패: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * 프로젝트 보드 목록 캐시에 저장
     */
    public void cacheBoards(String projectId, String boardsJson) {
        try {
            String key = PROJECT_BOARDS_CACHE_KEY + projectId + ":boards";
            kanbanRedisTemplate.opsForValue().set(key, boardsJson, Duration.ofSeconds(BOARD_CACHE_TTL));
            System.out.println("📝 보드 목록 캐시 저장: " + projectId + " (TTL: " + BOARD_CACHE_TTL + "초)");
        } catch (Exception e) {
            System.err.println("보드 캐시 저장 실패: " + e.getMessage());
        }
    }
    
    /**
     * 보드별 태스크 목록 캐시에서 조회
     */
    public String getCachedTasks(String boardId) {
        try {
            String key = PROJECT_TASKS_CACHE_KEY + boardId;
            return (String) kanbanRedisTemplate.opsForValue().get(key);
        } catch (Exception e) {
            System.err.println("태스크 캐시 조회 실패: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * 보드별 태스크 목록 캐시에 저장
     */
    public void cacheTasks(String boardId, String tasksJson) {
        try {
            String key = PROJECT_TASKS_CACHE_KEY + boardId;
            kanbanRedisTemplate.opsForValue().set(key, tasksJson, Duration.ofSeconds(PROJECT_DATA_CACHE_TTL));
            System.out.println("📝 태스크 목록 캐시 저장: " + boardId + " (TTL: " + PROJECT_DATA_CACHE_TTL + "초)");
        } catch (Exception e) {
            System.err.println("태스크 캐시 저장 실패: " + e.getMessage());
        }
    }
    
    
    /**
     * 특정 보드의 태스크 캐시 무효화
     */
    public void invalidateBoardTasksCache(String boardId) {
        try {
            String key = PROJECT_TASKS_CACHE_KEY + boardId;
            kanbanRedisTemplate.delete(key);
            System.out.println("🗑️ 보드 태스크 캐시 무효화: " + boardId);
        } catch (Exception e) {
            System.err.println("보드 태스크 캐시 무효화 실패: " + e.getMessage());
        }
    }

    /**
     * 태스크 이동 정보를 Redis에서 삭제
     */
    public void deleteTaskMove(String taskId) {
        String key = TASK_MOVE_KEY + taskId;
        kanbanRedisTemplate.delete(key);
        System.out.println("Redis에서 태스크 이동 정보 삭제: " + taskId);
    }

    /**
     * 배치 처리 큐에서 태스크 ID 가져오기
     * 큐가 비어있으면 null 반환
     */
    public String popFromBatchQueue() {
        return (String) kanbanRedisTemplate.opsForList().rightPop(BATCH_QUEUE_KEY);
    }

    /**
     * 배치 처리 큐에서 모든 태스크 ID 가져오기
     * 큐를 비우고 모든 요소를 반환
     */
    public java.util.List<String> popAllFromBatchQueue() {
        java.util.List<String> allTasks = new java.util.ArrayList<>();
        String taskId;
        
        while ((taskId = (String) kanbanRedisTemplate.opsForList().rightPop(BATCH_QUEUE_KEY)) != null) {
            allTasks.add(taskId);
        }
        
        return allTasks;
    }

    /**
     * 배치 처리 큐 크기 확인
     */
    public long getBatchQueueSize() {
        return kanbanRedisTemplate.opsForList().size(BATCH_QUEUE_KEY);
    }

    /**
     * 사용자 세션 정보 저장
     */
    public void saveUserSession(String userId, String sessionId) {
        String key = USER_SESSION_KEY + userId;
        kanbanRedisTemplate.opsForValue().set(key, sessionId, USER_SESSION_TTL, TimeUnit.SECONDS);
        System.out.println("Redis에 사용자 세션 저장: " + userId + " -> " + sessionId);
    }

    /**
     * 사용자 세션 정보 조회
     */
    public String getUserSession(String userId) {
        String key = USER_SESSION_KEY + userId;
        return (String) kanbanRedisTemplate.opsForValue().get(key);
    }

    /**
     * 사용자 세션 정보 삭제
     */
    public void deleteUserSession(String userId) {
        String key = USER_SESSION_KEY + userId;
        kanbanRedisTemplate.delete(key);
        System.out.println("Redis에서 사용자 세션 삭제: " + userId);
    }

    /**
     * 보드 캐시 저장
     */
    public void saveBoardCache(String boardId, Object boardData) {
        try {
            String key = BOARD_CACHE_KEY + boardId;
            String json = objectMapper.writeValueAsString(boardData);
            kanbanRedisTemplate.opsForValue().set(key, json, BOARD_CACHE_TTL, TimeUnit.SECONDS);
            System.out.println("Redis에 보드 캐시 저장: " + boardId);
        } catch (JsonProcessingException e) {
            System.err.println("보드 캐시 저장 실패: " + e.getMessage());
        }
    }

    /**
     * 보드 캐시 조회
     */
    public String getBoardCache(String boardId) {
        String key = BOARD_CACHE_KEY + boardId;
        return (String) kanbanRedisTemplate.opsForValue().get(key);
    }

    /**
     * 보드 캐시 삭제
     */
    public void deleteBoardCache(String boardId) {
        String key = BOARD_CACHE_KEY + boardId;
        kanbanRedisTemplate.delete(key);
        System.out.println("Redis에서 보드 캐시 삭제: " + boardId);
    }

    /**
     * 특정 패턴의 모든 키 삭제
     */
    public void deleteKeysByPattern(String pattern) {
        try {
            java.util.Set<String> keys = kanbanRedisTemplate.keys(pattern);
            if (keys != null && !keys.isEmpty()) {
                kanbanRedisTemplate.delete(keys);
                System.out.println("Redis에서 패턴 매칭 키 삭제: " + pattern + " (개수: " + keys.size() + ")");
            }
        } catch (Exception e) {
            System.err.println("패턴 매칭 키 삭제 실패: " + e.getMessage());
        }
    }

    /**
     * Redis 연결 상태 확인
     */
    public boolean isRedisConnected() {
        try {
            kanbanRedisTemplate.opsForValue().set("test:connection", "ping", 1, TimeUnit.SECONDS);
            String result = (String) kanbanRedisTemplate.opsForValue().get("test:connection");
            return "ping".equals(result);
        } catch (Exception e) {
            System.err.println("Redis 연결 확인 실패: " + e.getMessage());
            return false;
        }
    }

    /**
     * Redis 메모리 정리
     */
    public void cleanupExpiredKeys() {
        try {
            // 만료된 키들을 수동으로 정리
            java.util.Set<String> expiredKeys = new java.util.HashSet<>();
            
            // 태스크 이동 키 확인
            java.util.Set<String> taskKeys = kanbanRedisTemplate.keys(TASK_MOVE_KEY + "*");
            if (taskKeys != null) {
                for (String key : taskKeys) {
                    if (kanbanRedisTemplate.getExpire(key) < 0) {
                        expiredKeys.add(key);
                    }
                }
            }
            
            // 사용자 세션 키 확인
            java.util.Set<String> userKeys = kanbanRedisTemplate.keys(USER_SESSION_KEY + "*");
            if (userKeys != null) {
                for (String key : userKeys) {
                    if (kanbanRedisTemplate.getExpire(key) < 0) {
                        expiredKeys.add(key);
                    }
                }
            }
            
            // 보드 캐시 키 확인
            java.util.Set<String> boardKeys = kanbanRedisTemplate.keys(BOARD_CACHE_KEY + "*");
            if (boardKeys != null) {
                for (String key : boardKeys) {
                    if (kanbanRedisTemplate.getExpire(key) < 0) {
                        expiredKeys.add(key);
                    }
                }
            }
            
            if (!expiredKeys.isEmpty()) {
                kanbanRedisTemplate.delete(expiredKeys);
                System.out.println("만료된 키 정리 완료: " + expiredKeys.size() + "개");
            }
            
        } catch (Exception e) {
            System.err.println("만료된 키 정리 실패: " + e.getMessage());
        }
    }

    // ================== 프로젝트 데이터 캐싱 메서드 ==================
    
    /**
     * 프로젝트의 보드 목록을 Redis에 캐싱 (Map 형태로 저장)
     */
    public void cacheProjectBoards(String projectId, java.util.List<java.util.Map<String, Object>> boards) {
        try {
            String key = PROJECT_BOARDS_CACHE_KEY + projectId + ":boards";
            String json = objectMapper.writeValueAsString(boards);
            kanbanRedisTemplate.opsForValue().set(key, json, PROJECT_DATA_CACHE_TTL, TimeUnit.SECONDS);
            System.out.println("🔧 Redis에 프로젝트 보드 목록 캐싱: " + projectId + " (보드 수: " + boards.size() + ")");
        } catch (JsonProcessingException e) {
            System.err.println("❌ 프로젝트 보드 목록 캐싱 실패: " + e.getMessage());
        }
    }
    
    /**
     * 프로젝트의 보드 목록을 Redis에서 조회
     */
    @SuppressWarnings("unchecked")
    public java.util.List<java.util.Map<String, Object>> getProjectBoardsFromCache(String projectId) {
        try {
            String key = PROJECT_BOARDS_CACHE_KEY + projectId + ":boards";
            String json = (String) kanbanRedisTemplate.opsForValue().get(key);
            
            if (json != null) {
                System.out.println("✅ Redis에서 프로젝트 보드 목록 조회 성공: " + projectId);
                return objectMapper.readValue(json, java.util.List.class);
            } else {
                System.out.println("⚠️ Redis에 프로젝트 보드 목록 캐시 없음: " + projectId);
            }
        } catch (JsonProcessingException e) {
            System.err.println("❌ 프로젝트 보드 목록 조회 실패: " + e.getMessage());
        }
        
        return null;
    }
    
    /**
     * 프로젝트의 태스크 목록을 Redis에 캐싱 (Map 형태로 저장)
     */
    public void cacheProjectTasks(String projectId, java.util.List<java.util.Map<String, Object>> tasks) {
        try {
            String key = PROJECT_TASKS_CACHE_KEY + projectId + ":tasks";
            String json = objectMapper.writeValueAsString(tasks);
            kanbanRedisTemplate.opsForValue().set(key, json, PROJECT_DATA_CACHE_TTL, TimeUnit.SECONDS);
            System.out.println("🔧 Redis에 프로젝트 태스크 목록 캐싱: " + projectId + " (태스크 수: " + tasks.size() + ")");
        } catch (JsonProcessingException e) {
            System.err.println("❌ 프로젝트 태스크 목록 캐싱 실패: " + e.getMessage());
        }
    }
    
    /**
     * 프로젝트의 태스크 목록을 Redis에서 조회 (성능 최적화 버전)
     * 통합 캐시(tasks_with_username)에서 데이터를 추출하여 반환
     */
    @SuppressWarnings("unchecked")
    public java.util.List<java.util.Map<String, Object>> getProjectTasksFromCache(String projectId) {
        try {
            // 1. 먼저 기존 일반 태스크 캐시 확인
            String tasksKey = PROJECT_TASKS_CACHE_KEY + projectId + ":tasks";
            String json = (String) kanbanRedisTemplate.opsForValue().get(tasksKey);
            
            if (json != null) {
                System.out.println("✅ Redis에서 일반 태스크 캐시 조회 성공: " + projectId);
                return objectMapper.readValue(json, java.util.List.class);
            }
            
            // 2. 일반 캐시가 없으면 통합 캐시(사용자 이름 포함)에서 추출
            String userNameKey = PROJECT_TASKS_CACHE_KEY + projectId + ":tasks_with_username";
            String userNameJson = (String) kanbanRedisTemplate.opsForValue().get(userNameKey);
            
            if (userNameJson != null) {
                System.out.println("🔄 통합 캐시에서 일반 태스크 데이터 추출 시도: " + projectId);
                java.util.List<java.util.Map<String, Object>> userNameTasks = objectMapper.readValue(userNameJson, java.util.List.class);
                
                // 사용자 이름 제거한 일반 태스크 데이터로 변환 (필요시)
                // 현재는 동일한 데이터 구조이므로 그대로 반환
                System.out.println("✅ 통합 캐시에서 태스크 목록 추출 성공: " + userNameTasks.size() + "개");
                return userNameTasks;
            } else {
                System.out.println("⚠️ Redis에 프로젝트 태스크 캐시 없음 (일반/통합 모두): " + projectId);
            }
        } catch (JsonProcessingException e) {
            System.err.println("❌ 프로젝트 태스크 목록 조회 실패: " + e.getMessage());
        }
        
        return null;
    }
    
    /**
     * 정렬이 적용된 프로젝트 태스크 목록을 Redis 캐시에서 조회
     * Redis에 저장된 임시 이동 데이터를 먼저 적용한 후 정렬 수행
     */
    public java.util.List<java.util.Map<String, Object>> getProjectTasksFromCacheWithSort(String projectId, String sortField, String sortOrder) {
        System.out.println("🔄 Redis에서 정렬된 태스크 목록 조회 시작 - 프로젝트: " + projectId + ", 정렬: " + sortField + " " + sortOrder);
        
        java.util.List<java.util.Map<String, Object>> tasks = getProjectTasksFromCache(projectId);
        
        if (tasks != null && !tasks.isEmpty()) {
            System.out.println("📊 정렬 전 태스크 상태 (처음 5개):");
            for (int i = 0; i < Math.min(5, tasks.size()); i++) {
                java.util.Map<String, Object> task = tasks.get(i);
                System.out.println("  - taskId: " + task.get("taskId") + ", boardId: " + task.get("boardId") + ", title: " + task.get("taskTitle"));
            }
            
            // 1. Redis 임시 이동 데이터를 먼저 적용
            java.util.List<java.util.Map<String, Object>> updatedTasks = applyPendingMovesToTasks(tasks);
            System.out.println("✅ Redis 임시 이동 데이터 적용 완료: " + updatedTasks.size() + "개");
            
            System.out.println("📊 임시 이동 적용 후 태스크 상태 (처음 5개):");
            for (int i = 0; i < Math.min(5, updatedTasks.size()); i++) {
                java.util.Map<String, Object> task = updatedTasks.get(i);
                System.out.println("  - taskId: " + task.get("taskId") + ", boardId: " + task.get("boardId") + ", title: " + task.get("taskTitle"));
            }
            
            // 2. 업데이트된 데이터를 정렬
            tasks = sortTaskMapList(updatedTasks, sortField, sortOrder);
            System.out.println("✅ Redis 캐시 데이터 정렬 완료: " + tasks.size() + "개");
            
            System.out.println("📊 정렬 후 최종 태스크 상태 (처음 5개):");
            for (int i = 0; i < Math.min(5, tasks.size()); i++) {
                java.util.Map<String, Object> task = tasks.get(i);
                System.out.println("  - taskId: " + task.get("taskId") + ", boardId: " + task.get("boardId") + ", title: " + task.get("taskTitle"));
            }
        }
        
        return tasks;
    }
    
    /**
     * Redis에 저장된 대기 중인 임시 이동 데이터를 태스크 목록에 적용
     */
    private java.util.List<java.util.Map<String, Object>> applyPendingMovesToTasks(java.util.List<java.util.Map<String, Object>> tasks) {
        try {
            System.out.println("🔄 Redis 임시 이동 데이터 적용 시작");
            System.out.println("🔍 검색할 Redis 키 패턴: " + TASK_MOVE_KEY + "*");
            
            // Redis에서 모든 임시 이동 키 조회
            java.util.Set<String> moveKeys = kanbanRedisTemplate.keys(TASK_MOVE_KEY + "*");
            
            if (moveKeys == null || moveKeys.isEmpty()) {
                System.out.println("⚠️ Redis에 임시 이동 데이터가 없음 - 패턴: " + TASK_MOVE_KEY + "*");
                
                // Redis에 실제로 어떤 키들이 있는지 확인
                java.util.Set<String> allKeys = kanbanRedisTemplate.keys("kanban:*");
                System.out.println("🔍 Redis에 존재하는 kanban 관련 키들: " + (allKeys != null ? allKeys.size() : 0) + "개");
                if (allKeys != null) {
                    for (String key : allKeys) {
                        System.out.println("  - " + key);
                    }
                }
                
                return tasks;
            }
            
            System.out.println("📋 발견된 임시 이동 데이터: " + moveKeys.size() + "개");
            for (String key : moveKeys) {
                System.out.println("  - Redis 키: " + key);
            }
            
            int appliedCount = 0;
            
            // 각 임시 이동 데이터를 태스크에 적용
            for (String moveKey : moveKeys) {
                try {
                    System.out.println("🔍 Redis 키에서 데이터 조회: " + moveKey);
                    String moveJson = (String) kanbanRedisTemplate.opsForValue().get(moveKey);
                    
                    if (moveJson != null) {
                        System.out.println("📄 Redis에서 조회한 JSON: " + moveJson);
                        KanbanMessage moveMessage = objectMapper.readValue(moveJson, KanbanMessage.class);
                        System.out.println("📋 파싱된 이동 메시지: taskId=" + moveMessage.getTaskId() + 
                                        ", from=" + moveMessage.getFromBoardId() + 
                                        ", to=" + moveMessage.getToBoardId());
                        
                        // 해당 태스크를 찾아서 boardId 업데이트
                        boolean taskFound = false;
                        for (java.util.Map<String, Object> task : tasks) {
                            if (moveMessage.getTaskId().equals(task.get("taskId"))) {
                                String oldBoardId = (String) task.get("boardId");
                                task.put("boardId", moveMessage.getToBoardId());
                                
                                System.out.println("🔄 임시 이동 적용 성공: taskId=" + moveMessage.getTaskId() + 
                                                ", " + oldBoardId + " → " + moveMessage.getToBoardId());
                                appliedCount++;
                                taskFound = true;
                                break;
                            }
                        }
                        
                        if (!taskFound) {
                            System.out.println("⚠️ 해당 taskId를 태스크 목록에서 찾을 수 없음: " + moveMessage.getTaskId());
                        }
                        
                    } else {
                        System.out.println("⚠️ Redis 키에 데이터가 없음: " + moveKey);
                    }
                } catch (Exception e) {
                    System.err.println("❌ 개별 임시 이동 데이터 적용 실패 (" + moveKey + "): " + e.getMessage());
                    e.printStackTrace();
                }
            }
            
            System.out.println("✅ Redis 임시 이동 데이터 적용 완료 - 적용된 개수: " + appliedCount + "/" + moveKeys.size());
            return tasks;
            
        } catch (Exception e) {
            System.err.println("❌ Redis 임시 이동 데이터 적용 중 오류: " + e.getMessage());
            e.printStackTrace();
            return tasks; // 오류 시 원본 데이터 반환
        }
    }
    
    /**
     * Map 형태의 태스크 리스트를 정렬합니다.
     */
    private java.util.List<java.util.Map<String, Object>> sortTaskMapList(java.util.List<java.util.Map<String, Object>> tasks, String sortField, String sortOrder) {
        if (tasks == null || tasks.isEmpty()) {
            return tasks;
        }
        
        System.out.println("🔄 KanbanRedisService에서 정렬 시작 - 필드: " + sortField + ", 순서: " + sortOrder + ", 개수: " + tasks.size());
        
        tasks.sort((a, b) -> {
            Object valueA = a.get(sortField);
            Object valueB = b.get(sortField);
            
            // null 처리
            if (valueA == null && valueB == null) return 0;
            if (valueA == null) return 1; // null은 뒤로
            if (valueB == null) return -1; // null은 뒤로
            
            try {
                String strA = valueA.toString().trim();
                String strB = valueB.toString().trim();
                
                // 빈 문자열 처리
                if (strA.isEmpty() && strB.isEmpty()) return 0;
                if (strA.isEmpty()) return 1;
                if (strB.isEmpty()) return -1;
                
                // 날짜 형식 파싱 시도
                java.util.Date dateA = parseDate(strA);
                java.util.Date dateB = parseDate(strB);
                
                int comparison = dateA.compareTo(dateB);
                
                // 내림차순인 경우 결과를 뒤집음
                return "desc".equals(sortOrder) ? -comparison : comparison;
                
            } catch (Exception e) {
                System.err.println("Redis 정렬 중 오류 발생: " + e.getMessage());
                // 문자열 비교로 폴백
                String strA = valueA.toString();
                String strB = valueB.toString();
                int comparison = strA.compareTo(strB);
                return "desc".equals(sortOrder) ? -comparison : comparison;
            }
        });
        
        System.out.println("✅ KanbanRedisService 정렬 완료: " + tasks.size() + "개");
        return tasks;
    }
    
    /**
     * 날짜 문자열을 Date 객체로 변환합니다.
     */
    private java.util.Date parseDate(String dateString) {
        if (dateString == null || dateString.trim().isEmpty()) {
            return new java.util.Date(0); // 기본값 (1970-01-01)
        }
        
        try {
            // 다양한 날짜 형식 지원
            java.text.SimpleDateFormat[] formats = {
                new java.text.SimpleDateFormat("yyyy-MM-dd"),
                new java.text.SimpleDateFormat("yyyy/MM/dd"),
                new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss"),
                new java.text.SimpleDateFormat("yyyy/MM/dd HH:mm:ss"),
                new java.text.SimpleDateFormat("MM/dd/yyyy"),
                new java.text.SimpleDateFormat("dd/MM/yyyy")
            };
            
            for (java.text.SimpleDateFormat format : formats) {
                try {
                    return format.parse(dateString.trim());
                } catch (java.text.ParseException e) {
                    // 다음 형식 시도
                }
            }
            
            // 모든 형식 실패 시 기본값
            System.err.println("Redis에서 날짜 파싱 실패: " + dateString);
            return new java.util.Date(0);
            
        } catch (Exception e) {
            System.err.println("Redis 날짜 파싱 중 예외 발생: " + e.getMessage());
            return new java.util.Date(0);
        }
    }
    
    /**
     * 특정 태스크를 Redis 캐시에서 업데이트 (실시간 반영)
     */
    @SuppressWarnings("unchecked")
    public void updateTaskInCache(String projectId, String taskId, String newBoardId) {
        try {
            String key = PROJECT_TASKS_CACHE_KEY + projectId + ":tasks";
            String json = (String) kanbanRedisTemplate.opsForValue().get(key);
            
            if (json != null) {
                java.util.List<java.util.Map<String, Object>> tasks = objectMapper.readValue(json, java.util.List.class);
                
                // 해당 태스크 찾아서 보드 ID 업데이트
                for (java.util.Map<String, Object> task : tasks) {
                    if (taskId.equals(task.get("taskId"))) {
                        task.put("boardId", newBoardId);
                        System.out.println("🔄 Redis 캐시에서 태스크 업데이트: " + taskId + " → " + newBoardId);
                        break;
                    }
                }
                
                // 업데이트된 데이터를 다시 캐싱
                String updatedJson = objectMapper.writeValueAsString(tasks);
                kanbanRedisTemplate.opsForValue().set(key, updatedJson, PROJECT_DATA_CACHE_TTL, TimeUnit.SECONDS);
                
                System.out.println("✅ Redis 캐시 태스크 업데이트 완료: " + taskId);
            } else {
                System.out.println("⚠️ Redis에 프로젝트 태스크 캐시가 없어 업데이트 불가: " + projectId);
            }
        } catch (Exception e) {
            System.err.println("❌ Redis 캐시 태스크 업데이트 실패: " + e.getMessage());
        }
    }
    
    /**
     * 태스크의 여러 속성을 캐시에서 직접 업데이트 (성능 최적화 버전)
     * 통합 캐시(tasks_with_username)에서만 업데이트하고 일반 캐시는 무효화
     * @param projectId 프로젝트 ID
     * @param taskId 태스크 ID  
     * @param properties 업데이트할 속성들 (key-value 형태)
     */
    @SuppressWarnings("unchecked")
    public void updateTaskPropertiesInCache(String projectId, String taskId, java.util.Map<String, Object> properties) {
        try {
            System.out.println("🔄 태스크 속성 캐시 업데이트 시도 (최적화 버전): " + projectId + ", taskId: " + taskId);
            
            // 통합 캐시(사용자 이름 포함)에서 태스크 속성 업데이트
            String tasksWithUserNameKey = PROJECT_TASKS_CACHE_KEY + projectId + ":tasks_with_username";
            String userNameJson = (String) kanbanRedisTemplate.opsForValue().get(tasksWithUserNameKey);
            
            if (userNameJson != null) {
                java.util.List<java.util.Map<String, Object>> userNameTasks = objectMapper.readValue(userNameJson, java.util.List.class);
                
                // 해당 태스크 찾아서 속성들 업데이트
                boolean taskFound = false;
                for (java.util.Map<String, Object> task : userNameTasks) {
                    if (taskId.equals(task.get("taskId"))) {
                        // 전달받은 속성들을 모두 업데이트
                        for (java.util.Map.Entry<String, Object> entry : properties.entrySet()) {
                            task.put(entry.getKey(), entry.getValue());
                            System.out.println("🔄 Redis 통합 캐시 태스크 속성 업데이트: " + taskId + " [" + entry.getKey() + "] → " + entry.getValue());
                        }
                        taskFound = true;
                        break;
                    }
                }
                
                if (taskFound) {
                    // 업데이트된 데이터를 다시 캐싱
                    String updatedUserNameJson = objectMapper.writeValueAsString(userNameTasks);
                    kanbanRedisTemplate.opsForValue().set(tasksWithUserNameKey, updatedUserNameJson, PROJECT_DATA_CACHE_TTL, TimeUnit.SECONDS);
                    
                    System.out.println("✅ Redis 통합 캐시 태스크 속성 업데이트 완료: " + taskId + " (" + properties.size() + "개 속성)");
                    
                    // 일반 태스크 캐시 무효화 (다음 조회 시 통합 캐시에서 추출)
                    String tasksKey = PROJECT_TASKS_CACHE_KEY + projectId + ":tasks";
                    kanbanRedisTemplate.delete(tasksKey);
                    System.out.println("🗑️ 일반 태스크 캐시 무효화 (통합 캐시 동기화): " + tasksKey);
                    
                } else {
                    System.out.println("⚠️ 업데이트할 태스크를 통합 캐시에서 찾을 수 없음: " + taskId);
                }
            } else {
                System.out.println("⚠️ Redis에 프로젝트 통합 태스크 캐시가 없어 업데이트 불가: " + projectId);
            }
            
        } catch (Exception e) {
            System.err.println("❌ Redis 캐시 태스크 속성 업데이트 실패: " + e.getMessage());
            // 캐시 업데이트 실패 시 해당 프로젝트 캐시 무효화로 대체
            System.out.println("🗑️ 캐시 업데이트 실패로 인한 프로젝트 캐시 무효화: " + projectId);
            invalidateProjectCache(projectId);
            invalidateTasksWithUserNameCache(projectId);
        }
    }
    
    /**
     * 프로젝트 관련 캐시 무효화 (보드 및 태스크)
     */
    public void invalidateProjectCache(String projectId) {
        try {
            String boardsKey = PROJECT_BOARDS_CACHE_KEY + projectId + ":boards";
            String tasksKey = PROJECT_TASKS_CACHE_KEY + projectId + ":tasks";
            
            kanbanRedisTemplate.delete(boardsKey);
            kanbanRedisTemplate.delete(tasksKey);
            
            System.out.println("🗑️ 프로젝트 캐시 무효화 완료: " + projectId);
        } catch (Exception e) {
            System.err.println("❌ 프로젝트 캐시 무효화 실패: " + e.getMessage());
        }
    }
    
    /**
     * 사용자 이름 포함 태스크 캐시 무효화
     */
    public void invalidateTasksWithUserNameCache(String projectId) {
        try {
            String tasksWithUserNameKey = PROJECT_TASKS_CACHE_KEY + projectId + ":tasks_with_username";
            kanbanRedisTemplate.delete(tasksWithUserNameKey);
            
            System.out.println("🗑️ 사용자 이름 포함 태스크 캐시 무효화 완료: " + projectId);
        } catch (Exception e) {
            System.err.println("❌ 사용자 이름 포함 태스크 캐시 무효화 실패: " + e.getMessage());
        }
    }
    
    /**
     * 프로젝트 태스크 캐시에 새 태스크 추가 (성능 최적화 버전)
     * Redis 부하를 줄이기 위해 사용자 이름 포함 캐시만 업데이트하고
     * 일반 태스크는 해당 캐시에서 추출하여 사용
     */
    @SuppressWarnings("unchecked")
    public void addTaskToProjectCache(String projectId, Object taskData) {
        try {
            System.out.println("📝 캐시에 새 태스크 추가 시도 (최적화 버전): " + projectId);
            System.out.println("📝 추가하려는 태스크: " + objectMapper.writeValueAsString(taskData));
            
            // 사용자 이름 포함 태스크 캐시만 업데이트 (통합 캐시 전략)
            String tasksWithUserNameKey = PROJECT_TASKS_CACHE_KEY + projectId + ":tasks_with_username";
            String userNameJson = (String) kanbanRedisTemplate.opsForValue().get(tasksWithUserNameKey);
            
            if (userNameJson != null) {
                // 캐시가 존재하면 새 태스크를 추가
                java.util.List<java.util.Map<String, Object>> userNameTasks = objectMapper.readValue(userNameJson, java.util.List.class);
                
                // TaskVo 객체를 Map으로 변환 (모든 필드 포함)
                java.util.Map<String, Object> taskMap = convertTaskToMap(taskData);
                
                // 기존 태스크 목록에 새 태스크 추가
                userNameTasks.add(taskMap);
                
                // 업데이트된 목록을 다시 캐싱
                String updatedUserNameJson = objectMapper.writeValueAsString(userNameTasks);
                kanbanRedisTemplate.opsForValue().set(tasksWithUserNameKey, updatedUserNameJson, PROJECT_DATA_CACHE_TTL, TimeUnit.SECONDS);
                
                System.out.println("✅ Redis 통합 태스크 캐시에 새 태스크 추가 완료: " + taskMap.get("taskId") + " (총 " + userNameTasks.size() + "개)");
                
                // 기존 일반 태스크 캐시는 무효화 (다음 조회 시 통합 캐시에서 추출)
                String tasksKey = PROJECT_TASKS_CACHE_KEY + projectId + ":tasks";
                kanbanRedisTemplate.delete(tasksKey);
                System.out.println("🗑️ 일반 태스크 캐시 무효화 (통합 캐시 사용으로 전환): " + tasksKey);
                
            } else {
                // 캐시가 없으면 무효화하여 다음 조회 시 DB에서 최신 데이터 로드
                System.out.println("⚠️ 프로젝트 태스크 캐시가 없어 캐시 무효화로 처리: " + projectId);
                invalidateProjectCache(projectId);
                invalidateTasksWithUserNameCache(projectId);
                System.out.println("✅ 캐시 무효화 완료 - 다음 조회 시 최신 DB 데이터로 캐시 재생성됩니다: " + projectId);
            }
            
        } catch (Exception e) {
            System.err.println("❌ 캐시에 태스크 추가 실패: " + e.getMessage());
            System.out.println("🔄 캐시 추가 실패로 인한 캐시 무효화: " + projectId);
            invalidateProjectCache(projectId);
            invalidateTasksWithUserNameCache(projectId);
        }
    }
    
    /**
     * TaskVo 또는 다른 태스크 객체를 Map으로 변환
     */
    private java.util.Map<String, Object> convertTaskToMap(Object taskData) {
        try {
            if (taskData instanceof java.util.Map) {
                return (java.util.Map<String, Object>) taskData;
            }
            
            // TaskVo 객체를 JSON으로 직렬화 후 Map으로 역직렬화
            String json = objectMapper.writeValueAsString(taskData);
            return objectMapper.readValue(json, java.util.Map.class);
            
        } catch (Exception e) {
            System.err.println("❌ 태스크 객체를 Map으로 변환 실패: " + e.getMessage());
            return new java.util.HashMap<>();
        }
    }

    /**
     * 프로젝트 보드 캐시에 새 보드 추가 (실제 캐시 업데이트)
     */
    @SuppressWarnings("unchecked")
    public void addBoardToProjectCache(String projectId, Object boardData) {
        try {
            System.out.println("📝 캐시에 새 보드 추가 시도: " + projectId);
            System.out.println("📝 추가하려는 보드: " + objectMapper.writeValueAsString(boardData));
            
            String boardsKey = PROJECT_BOARDS_CACHE_KEY + projectId + ":boards";
            String json = (String) kanbanRedisTemplate.opsForValue().get(boardsKey);
            
            if (json != null) {
                // 캐시가 존재하면 새 보드를 추가
                java.util.List<java.util.Map<String, Object>> boards = objectMapper.readValue(json, java.util.List.class);
                
                // BoardVo 객체를 Map으로 변환
                java.util.Map<String, Object> boardMap = convertTaskToMap(boardData);
                
                // 기존 보드 목록에 새 보드 추가
                boards.add(boardMap);
                
                // 업데이트된 목록을 다시 캐싱
                String updatedJson = objectMapper.writeValueAsString(boards);
                kanbanRedisTemplate.opsForValue().set(boardsKey, updatedJson, PROJECT_DATA_CACHE_TTL, TimeUnit.SECONDS);
                
                System.out.println("✅ Redis 캐시에 새 보드 추가 완료: " + boardMap.get("boardId") + " (총 " + boards.size() + "개)");
                
            } else {
                // 캐시가 없으면 무효화하여 다음 조회 시 DB에서 최신 데이터 로드
                System.out.println("⚠️ 프로젝트 보드 캐시가 없어 캐시 무효화로 처리: " + projectId);
                invalidateProjectCache(projectId);
                System.out.println("✅ 캐시 무효화 완료 - 다음 조회 시 최신 DB 데이터로 캐시 재생성됩니다: " + projectId);
            }
            
        } catch (Exception e) {
            System.err.println("❌ 캐시에 보드 추가 실패: " + e.getMessage());
            System.out.println("🔄 캐시 추가 실패로 인한 캐시 무효화: " + projectId);
            invalidateProjectCache(projectId);
        }
    }
    
    /**
     * 프로젝트 보드 캐시에서 특정 보드 제거
     */
    @SuppressWarnings("unchecked")
    public void removeBoardFromProjectCache(String projectId, String boardId) {
        try {
            System.out.println("🗑️ 캐시에서 보드 제거 시도: " + projectId + ", boardId: " + boardId);
            
            String boardsKey = PROJECT_BOARDS_CACHE_KEY + projectId + ":boards";
            String json = (String) kanbanRedisTemplate.opsForValue().get(boardsKey);
            
            if (json != null) {
                java.util.List<java.util.Map<String, Object>> boards = objectMapper.readValue(json, java.util.List.class);
                
                // 해당 보드를 목록에서 제거
                boolean removed = boards.removeIf(board -> boardId.equals(board.get("boardId")));
                
                if (removed) {
                    // 업데이트된 목록을 다시 캐싱
                    String updatedJson = objectMapper.writeValueAsString(boards);
                    kanbanRedisTemplate.opsForValue().set(boardsKey, updatedJson, PROJECT_DATA_CACHE_TTL, TimeUnit.SECONDS);
                    
                    System.out.println("✅ Redis 캐시에서 보드 제거 완료: " + boardId + " (남은 " + boards.size() + "개)");
                } else {
                    System.out.println("⚠️ 제거할 보드를 캐시에서 찾을 수 없음: " + boardId);
                }
                
            } else {
                System.out.println("⚠️ 프로젝트 보드 캐시가 없어 제거 불가: " + projectId);
            }
            
        } catch (Exception e) {
            System.err.println("❌ 캐시에서 보드 제거 실패: " + e.getMessage());
            System.out.println("🔄 캐시 제거 실패로 인한 캐시 무효화: " + projectId);
            invalidateProjectCache(projectId);
        }
    }
    
    /**
     * 프로젝트 태스크 캐시에서 특정 태스크 제거 (성능 최적화 버전)
     * 통합 캐시(tasks_with_username)에서만 제거하고 일반 캐시는 무효화
     */
    @SuppressWarnings("unchecked")
    public void removeTaskFromProjectCache(String projectId, String taskId) {
        try {
            System.out.println("🗑️ 캐시에서 태스크 제거 시도 (최적화 버전): " + projectId + ", taskId: " + taskId);
            
            // 통합 캐시(사용자 이름 포함)에서 태스크 제거
            String tasksWithUserNameKey = PROJECT_TASKS_CACHE_KEY + projectId + ":tasks_with_username";
            String userNameJson = (String) kanbanRedisTemplate.opsForValue().get(tasksWithUserNameKey);
            
            if (userNameJson != null) {
                java.util.List<java.util.Map<String, Object>> userNameTasks = objectMapper.readValue(userNameJson, java.util.List.class);
                
                // 해당 태스크를 목록에서 제거
                boolean removed = userNameTasks.removeIf(task -> taskId.equals(task.get("taskId")));
                
                if (removed) {
                    // 업데이트된 목록을 다시 캐싱
                    String updatedUserNameJson = objectMapper.writeValueAsString(userNameTasks);
                    kanbanRedisTemplate.opsForValue().set(tasksWithUserNameKey, updatedUserNameJson, PROJECT_DATA_CACHE_TTL, TimeUnit.SECONDS);
                    
                    System.out.println("✅ Redis 통합 캐시에서 태스크 제거 완료: " + taskId + " (남은 " + userNameTasks.size() + "개)");
                    
                    // 일반 태스크 캐시 무효화 (다음 조회 시 통합 캐시에서 추출)
                    String tasksKey = PROJECT_TASKS_CACHE_KEY + projectId + ":tasks";
                    kanbanRedisTemplate.delete(tasksKey);
                    System.out.println("🗑️ 일반 태스크 캐시 무효화 (통합 캐시 동기화): " + tasksKey);
                    
                } else {
                    System.out.println("⚠️ 제거할 태스크를 통합 캐시에서 찾을 수 없음: " + taskId);
                }
            } else {
                System.out.println("⚠️ 프로젝트 통합 태스크 캐시가 없어 제거 불가: " + projectId);
            }
            
        } catch (Exception e) {
            System.err.println("❌ 캐시에서 태스크 제거 실패: " + e.getMessage());
            System.out.println("🔄 캐시 제거 실패로 인한 캐시 무효화: " + projectId);
            invalidateProjectCache(projectId);
            invalidateTasksWithUserNameCache(projectId);
        }
    }

    /**
     * Redis 통계 정보 조회
     */
    public java.util.Map<String, Object> getRedisStats() {
        java.util.Map<String, Object> stats = new java.util.HashMap<>();
        
        try {
            // 각 키 타입별 개수 조회
            java.util.Set<String> taskKeys = kanbanRedisTemplate.keys(TASK_MOVE_KEY + "*");
            java.util.Set<String> userKeys = kanbanRedisTemplate.keys(USER_SESSION_KEY + "*");
            java.util.Set<String> boardKeys = kanbanRedisTemplate.keys(BOARD_CACHE_KEY + "*");
            java.util.Set<String> projectBoardKeys = kanbanRedisTemplate.keys(PROJECT_BOARDS_CACHE_KEY + "*");
            java.util.Set<String> projectTaskKeys = kanbanRedisTemplate.keys(PROJECT_TASKS_CACHE_KEY + "*");
            
            stats.put("taskMoveCount", taskKeys != null ? taskKeys.size() : 0);
            stats.put("userSessionCount", userKeys != null ? userKeys.size() : 0);
            stats.put("boardCacheCount", boardKeys != null ? boardKeys.size() : 0);
            stats.put("projectBoardsCount", projectBoardKeys != null ? projectBoardKeys.size() : 0);
            stats.put("projectTasksCount", projectTaskKeys != null ? projectTaskKeys.size() : 0);
            stats.put("batchQueueSize", getBatchQueueSize());
            stats.put("isConnected", isRedisConnected());
            
        } catch (Exception e) {
            System.err.println("Redis 통계 조회 실패: " + e.getMessage());
            stats.put("error", e.getMessage());
        }
        
        return stats;
    }
    
    // ================== 사용자 이름 포함 태스크 캐싱 메서드 (성능 최적화) ==================
    
    /**
     * 사용자 이름 포함 태스크 목록을 Redis에 캐싱
     */
    public void cacheTasksWithUserName(String cacheKey, java.util.List<java.util.Map<String, Object>> tasks) {
        try {
            String json = objectMapper.writeValueAsString(tasks);
            kanbanRedisTemplate.opsForValue().set(cacheKey, json, PROJECT_DATA_CACHE_TTL, TimeUnit.SECONDS);
            System.out.println("🔧 Redis에 사용자 이름 포함 태스크 목록 캐싱: " + cacheKey + " (태스크 수: " + tasks.size() + ")");
        } catch (JsonProcessingException e) {
            System.err.println("❌ 사용자 이름 포함 태스크 목록 캐싱 실패: " + e.getMessage());
        }
    }
    
    /**
     * 사용자 이름 포함 태스크 목록을 Redis에서 조회
     */
    @SuppressWarnings("unchecked")
    public java.util.List<java.util.Map<String, Object>> getCachedTasksWithUserName(String cacheKey) {
        try {
            String json = (String) kanbanRedisTemplate.opsForValue().get(cacheKey);
            
            if (json != null) {
                System.out.println("✅ Redis에서 사용자 이름 포함 태스크 목록 조회 성공: " + cacheKey);
                return objectMapper.readValue(json, java.util.List.class);
            } else {
                System.out.println("⚠️ Redis에 사용자 이름 포함 태스크 목록 캐시 없음: " + cacheKey);
            }
        } catch (JsonProcessingException e) {
            System.err.println("❌ 사용자 이름 포함 태스크 목록 조회 실패: " + e.getMessage());
        }
        
        return null;
    }
    
    // ================== Redis 디버깅 메서드들 ==================
    
    /**
     * 특정 프로젝트의 Redis 캐시 상태를 상세 조회 (일반 태스크 및 사용자 이름 포함 태스크 캐시 모두 확인)
     */
    @SuppressWarnings("unchecked")
    public void debugProjectCache(String projectId) {
        System.out.println("🔍 ========== Redis 캐시 디버깅: " + projectId + " ==========");
        
        try {
            // 1. 일반 태스크 캐시 확인
            String tasksKey = PROJECT_TASKS_CACHE_KEY + projectId + ":tasks";
            String tasksJson = (String) kanbanRedisTemplate.opsForValue().get(tasksKey);
            
            if (tasksJson != null) {
                java.util.List<java.util.Map<String, Object>> tasks = objectMapper.readValue(tasksJson, java.util.List.class);
                System.out.println("✅ 일반 태스크 캐시 존재: " + tasks.size() + "개");
                
                // 최근 5개 태스크 ID 출력
                System.out.println("📋 최근 태스크들 (일반 캐시):");
                for (int i = Math.max(0, tasks.size() - 5); i < tasks.size(); i++) {
                    java.util.Map<String, Object> task = tasks.get(i);
                    System.out.println("  - taskId: " + task.get("taskId") + ", title: " + task.get("taskTitle"));
                }
            } else {
                System.out.println("❌ 일반 태스크 캐시 없음: " + tasksKey);
            }
            
            // 2. 사용자 이름 포함 태스크 캐시 확인
            String tasksWithUserNameKey = PROJECT_TASKS_CACHE_KEY + projectId + ":tasks_with_username";
            String userNameTasksJson = (String) kanbanRedisTemplate.opsForValue().get(tasksWithUserNameKey);
            
            if (userNameTasksJson != null) {
                java.util.List<java.util.Map<String, Object>> userNameTasks = objectMapper.readValue(userNameTasksJson, java.util.List.class);
                System.out.println("✅ 사용자 이름 포함 태스크 캐시 존재: " + userNameTasks.size() + "개");
                
                // 최근 5개 태스크 ID 출력
                System.out.println("📋 최근 태스크들 (사용자 이름 포함 캐시):");
                for (int i = Math.max(0, userNameTasks.size() - 5); i < userNameTasks.size(); i++) {
                    java.util.Map<String, Object> task = userNameTasks.get(i);
                    System.out.println("  - taskId: " + task.get("taskId") + ", title: " + task.get("taskTitle") + ", userName: " + task.get("userName"));
                }
            } else {
                System.out.println("❌ 사용자 이름 포함 태스크 캐시 없음: " + tasksWithUserNameKey);
            }
            
            // 3. 보드 캐시 확인
            String boardsKey = PROJECT_BOARDS_CACHE_KEY + projectId + ":boards";
            String boardsJson = (String) kanbanRedisTemplate.opsForValue().get(boardsKey);
            
            if (boardsJson != null) {
                java.util.List<java.util.Map<String, Object>> boards = objectMapper.readValue(boardsJson, java.util.List.class);
                System.out.println("✅ 보드 캐시 존재: " + boards.size() + "개");
                
                for (java.util.Map<String, Object> board : boards) {
                    System.out.println("  - boardId: " + board.get("boardId") + ", title: " + board.get("boardTitle"));
                }
            } else {
                System.out.println("❌ 보드 캐시 없음: " + boardsKey);
            }
            
            // 4. TTL 확인
            Long tasksTtl = kanbanRedisTemplate.getExpire(tasksKey);
            Long userNameTasksTtl = kanbanRedisTemplate.getExpire(tasksWithUserNameKey);
            Long boardsTtl = kanbanRedisTemplate.getExpire(boardsKey);
            System.out.println("⏰ TTL - 일반 태스크: " + tasksTtl + "초, 사용자 이름 포함 태스크: " + userNameTasksTtl + "초, 보드: " + boardsTtl + "초");
            
        } catch (Exception e) {
            System.err.println("❌ Redis 캐시 디버깅 실패: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println("🔍 ========== Redis 캐시 디버깅 완료 ==========");
    }
    
    /**
     * 모든 프로젝트 캐시 키 목록 조회
     */
    public void debugAllCacheKeys() {
        System.out.println("🔍 ========== 모든 Redis 캐시 키 조회 ==========");
        
        try {
            java.util.Set<String> taskKeys = kanbanRedisTemplate.keys(PROJECT_TASKS_CACHE_KEY + "*");
            java.util.Set<String> boardKeys = kanbanRedisTemplate.keys(PROJECT_BOARDS_CACHE_KEY + "*");
            
            System.out.println("📋 태스크 캐시 키들 (" + (taskKeys != null ? taskKeys.size() : 0) + "개):");
            if (taskKeys != null) {
                for (String key : taskKeys) {
                    System.out.println("  - " + key);
                }
            }
            
            System.out.println("📋 보드 캐시 키들 (" + (boardKeys != null ? boardKeys.size() : 0) + "개):");
            if (boardKeys != null) {
                for (String key : boardKeys) {
                    System.out.println("  - " + key);
                }
            }
            
        } catch (Exception e) {
            System.err.println("❌ 캐시 키 조회 실패: " + e.getMessage());
        }
        
        System.out.println("🔍 ========== 캐시 키 조회 완료 ==========");
    }
}