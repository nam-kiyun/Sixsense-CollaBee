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
    
    @Autowired
    private com.demo.proworks.task.service.TaskService taskService;
    
    @Autowired
    private com.demo.proworks.board.service.BoardService boardService;
    
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

    // ================== 프로젝트 데이터 캐싱 메서드 (최적화 버전) ==================
    
    /**
     * 프로젝트의 보드 목록을 Redis에 캐싱 (Map 형태로 저장)
     * 캐싱 전략: 프로젝트 단위로 모든 보드를 한 번에 캐싱하여 효율성 증대
     */
    public void cacheProjectBoards(String projectId, java.util.List<java.util.Map<String, Object>> boards) {
        try {
            String key = PROJECT_BOARDS_CACHE_KEY + projectId + ":boards";
            String json = objectMapper.writeValueAsString(boards);
            kanbanRedisTemplate.opsForValue().set(key, json, PROJECT_DATA_CACHE_TTL, TimeUnit.SECONDS);
            System.out.println("🔧 Redis에 프로젝트 보드 목록 캐싱: " + projectId + " (보드 수: " + boards.size() + ")");
            
            // 보드 요약 정보도 별도 캐싱 (빠른 조회용)
            cacheProjectBoardSummary(projectId, boards);
        } catch (JsonProcessingException e) {
            System.err.println("❌ 프로젝트 보드 목록 캐싱 실패: " + e.getMessage());
        }
    }
    
    /**
     * 프로젝트 보드 요약 정보 캐싱 (ID와 제목만 포함하여 네트워크 트래픽 최소화)
     */
    private void cacheProjectBoardSummary(String projectId, java.util.List<java.util.Map<String, Object>> boards) {
        try {
            java.util.List<java.util.Map<String, Object>> boardSummary = new java.util.ArrayList<>();
            
            for (java.util.Map<String, Object> board : boards) {
                java.util.Map<String, Object> summary = new java.util.HashMap<>();
                summary.put("boardId", board.get("boardId"));
                summary.put("boardTitle", board.get("boardTitle"));
                summary.put("projectId", board.get("projectId"));
                boardSummary.add(summary);
            }
            
            String summaryKey = PROJECT_BOARDS_CACHE_KEY + projectId + ":boards_summary";
            String summaryJson = objectMapper.writeValueAsString(boardSummary);
            kanbanRedisTemplate.opsForValue().set(summaryKey, summaryJson, PROJECT_DATA_CACHE_TTL, TimeUnit.SECONDS);
            
            System.out.println("📋 프로젝트 보드 요약 정보 캐싱 완료: " + projectId + " (요약 데이터 크기 최적화)");
        } catch (Exception e) {
            System.err.println("❌ 프로젝트 보드 요약 정보 캐싱 실패: " + e.getMessage());
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
     * 프로젝트의 태스크 목록을 Redis에 캐싱 (통일된 캐시 사용)
     * tasks_with_username 키만 사용하여 일관성 보장
     */
    public void cacheProjectTasks(String projectId, java.util.List<java.util.Map<String, Object>> tasks) {
        try {
            String key = PROJECT_TASKS_CACHE_KEY + projectId + ":tasks_with_username";
            String json = objectMapper.writeValueAsString(tasks);
            kanbanRedisTemplate.opsForValue().set(key, json, PROJECT_DATA_CACHE_TTL, TimeUnit.SECONDS);
            System.out.println("🔧 Redis에 프로젝트 태스크 목록 캐싱: " + projectId + " (태스크 수: " + tasks.size() + ")");
            
            // 통계 정보도 함께 캐싱하여 향후 최적화에 활용
            cacheProjectTaskStats(projectId, tasks);
        } catch (JsonProcessingException e) {
            System.err.println("❌ 프로젝트 태스크 목록 캐싱 실패: " + e.getMessage());
        }
    }
    
    /**
     * 프로젝트 태스크 통계 정보 캐싱 (성능 모니터링 및 최적화 목적)
     */
    private void cacheProjectTaskStats(String projectId, java.util.List<java.util.Map<String, Object>> tasks) {
        try {
            java.util.Map<String, Object> stats = new java.util.HashMap<>();
            java.util.Map<String, Integer> boardTaskCount = new java.util.HashMap<>();
            java.util.Map<String, Integer> userTaskCount = new java.util.HashMap<>();
            
            // 보드별, 사용자별 태스크 개수 집계
            for (java.util.Map<String, Object> task : tasks) {
                String boardId = (String) task.get("boardId");
                String userId = (String) task.get("projectUserId");
                
                if (boardId != null) {
                    boardTaskCount.put(boardId, boardTaskCount.getOrDefault(boardId, 0) + 1);
                }
                if (userId != null) {
                    userTaskCount.put(userId, userTaskCount.getOrDefault(userId, 0) + 1);
                }
            }
            
            stats.put("totalTasks", tasks.size());
            stats.put("boardCount", boardTaskCount.size());
            stats.put("userCount", userTaskCount.size());
            stats.put("boardTaskCount", boardTaskCount);
            stats.put("userTaskCount", userTaskCount);
            stats.put("cacheTimestamp", System.currentTimeMillis());
            
            String statsKey = PROJECT_TASKS_CACHE_KEY + projectId + ":stats";
            String statsJson = objectMapper.writeValueAsString(stats);
            kanbanRedisTemplate.opsForValue().set(statsKey, statsJson, PROJECT_DATA_CACHE_TTL, TimeUnit.SECONDS);
            
            System.out.println("📊 프로젝트 태스크 통계 캐싱 완료: " + projectId + 
                             " (보드 " + boardTaskCount.size() + "개, 사용자 " + userTaskCount.size() + "명)");
            
        } catch (Exception e) {
            System.err.println("❌ 프로젝트 태스크 통계 캐싱 실패: " + e.getMessage());
        }
    }
    
    /**
     * 프로젝트의 태스크 목록을 Redis에서 조회 (통일된 캐시 사용)
     * tasks_with_username 키만 사용하여 일관성 보장
     * 임시 이동 데이터도 실시간으로 적용하여 최신 상태 반영
     */
    @SuppressWarnings("unchecked")
    public java.util.List<java.util.Map<String, Object>> getProjectTasksFromCache(String projectId) {
        System.out.println("🔍 ========== Redis 캐시 조회 디버깅 시작 ==========");
        System.out.println("📋 요청 프로젝트 ID: " + projectId);
        System.out.println("⏰ 조회 시각: " + new java.util.Date());
        
        try {
            String tasksWithUserNameKey = PROJECT_TASKS_CACHE_KEY + projectId + ":tasks_with_username";
            System.out.println("🔑 Redis 캐시 키: " + tasksWithUserNameKey);
            
            String json = (String) kanbanRedisTemplate.opsForValue().get(tasksWithUserNameKey);
            
            if (json != null) {
                System.out.println("✅ Redis에서 프로젝트 태스크 캐시 조회 성공: " + projectId);
                System.out.println("📄 캐시 JSON 길이: " + json.length() + " 문자");
                
                java.util.List<java.util.Map<String, Object>> tasks = objectMapper.readValue(json, java.util.List.class);
                System.out.println("📊 캐시에서 파싱된 태스크 수: " + tasks.size() + "개");
                
                // 캐시 데이터 상태 디버깅 (처음 5개만)
                System.out.println("📋 캐시 원본 데이터 상태 (처음 5개):");
                for (int i = 0; i < Math.min(5, tasks.size()); i++) {
                    java.util.Map<String, Object> task = tasks.get(i);
                    System.out.println("  [" + i + "] taskId: " + task.get("taskId") + 
                                     ", boardId: " + task.get("boardId") + 
                                     ", title: " + task.get("taskTitle"));
                }
                
                // 🚀 Redis 임시 이동 데이터를 실시간으로 적용하여 최신 상태 반영
                System.out.println("🔄 Redis 임시 이동 데이터 적용 시작...");
                java.util.List<java.util.Map<String, Object>> updatedTasks = applyPendingMovesToTasks(tasks);
                System.out.println("✅ Redis 임시 이동 데이터 적용 완료: " + updatedTasks.size() + "개");
                
                // 임시 이동 적용 후 데이터 상태 디버깅 (처음 5개만)
                System.out.println("📋 임시 이동 적용 후 데이터 상태 (처음 5개):");
                for (int i = 0; i < Math.min(5, updatedTasks.size()); i++) {
                    java.util.Map<String, Object> task = updatedTasks.get(i);
                    System.out.println("  [" + i + "] taskId: " + task.get("taskId") + 
                                     ", boardId: " + task.get("boardId") + 
                                     ", title: " + task.get("taskTitle"));
                }
                
                System.out.println("🔍 ========== Redis 캐시 조회 디버깅 완료 ==========");
                return updatedTasks;
            } else {
                System.out.println("⚠️ Redis에 프로젝트 태스크 캐시 없음: " + projectId);
                System.out.println("🔍 캐시 키 존재 여부 재확인: " + tasksWithUserNameKey);
                
                // 관련 키들이 있는지 확인
                java.util.Set<String> relatedKeys = kanbanRedisTemplate.keys("kanban:project:" + projectId + "*");
                System.out.println("🔍 프로젝트 관련 Redis 키들: " + (relatedKeys != null ? relatedKeys.size() : 0) + "개");
                if (relatedKeys != null) {
                    for (String key : relatedKeys) {
                        System.out.println("  - " + key);
                    }
                }
                
                System.out.println("🔍 ========== Redis 캐시 조회 디버깅 완료 (캐시 없음) ==========");
            }
        } catch (JsonProcessingException e) {
            System.err.println("❌ 프로젝트 태스크 목록 조회 실패: " + e.getMessage());
            e.printStackTrace();
            System.out.println("🔍 ========== Redis 캐시 조회 디버깅 완료 (오류) ==========");
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
     * 통합 캐시(tasks_with_username)만 사용하여 일관성 보장
     */
    @SuppressWarnings("unchecked")
    public void updateTaskInCache(String projectId, String taskId, String newBoardId) {
        try {
            String tasksWithUserNameKey = PROJECT_TASKS_CACHE_KEY + projectId + ":tasks_with_username";
            String userNameJson = (String) kanbanRedisTemplate.opsForValue().get(tasksWithUserNameKey);
            
            if (userNameJson != null) {
                java.util.List<java.util.Map<String, Object>> userNameTasks = objectMapper.readValue(userNameJson, java.util.List.class);
                
                System.out.println("🔍 캐시에서 태스크 업데이트 시도: taskId=" + taskId + ", 캐시 내 태스크 수=" + userNameTasks.size());
                
                // 해당 태스크 찾아서 보드 ID 업데이트
                boolean taskFound = false;
                for (java.util.Map<String, Object> task : userNameTasks) {
                    if (taskId.equals(task.get("taskId"))) {
                        String oldBoardId = (String) task.get("boardId");
                        task.put("boardId", newBoardId);
                        System.out.println("🔄 Redis 캐시에서 태스크 업데이트: " + taskId + " (" + oldBoardId + " → " + newBoardId + ")");
                        taskFound = true;
                        break;
                    }
                }
                
                if (taskFound) {
                    // 업데이트된 데이터를 다시 캐싱
                    String updatedUserNameJson = objectMapper.writeValueAsString(userNameTasks);
                    kanbanRedisTemplate.opsForValue().set(tasksWithUserNameKey, updatedUserNameJson, PROJECT_DATA_CACHE_TTL, TimeUnit.SECONDS);
                    
                    System.out.println("✅ Redis 캐시 태스크 업데이트 완료: " + taskId);
                } else {
                    // 태스크를 찾을 수 없는 경우 캐시 상태 디버깅 정보 출력
                    System.out.println("⚠️ 업데이트할 태스크를 캐시에서 찾을 수 없음: " + taskId);
                    System.out.println("📊 현재 캐시 내 태스크 ID 목록 (처음 10개):");
                    for (int i = 0; i < Math.min(10, userNameTasks.size()); i++) {
                        java.util.Map<String, Object> task = userNameTasks.get(i);
                        System.out.println("  - taskId: " + task.get("taskId") + ", boardId: " + task.get("boardId"));
                    }
                    
                    // 캐시에 태스크가 없는 경우 캐시를 무효화하여 다음 조회 시 DB에서 최신 데이터 로드
                    System.out.println("🗑️ 프로젝트 캐시 무효화 완료: " + projectId + " (보드, 태스크, 통계 포함)");
                    invalidateProjectCache(projectId);
                    System.out.println("🗑️ 프로젝트 캐시 무효화 완료 - 다음 조회 시 최신 DB 데이터로 캐시 재생성: " + projectId);
                    
                    System.out.println("❌ Redis 캐시 태스크 업데이트 실패: 캐시에서 태스크를 찾을 수 없음: " + taskId);
                    throw new RuntimeException("캐시에서 태스크를 찾을 수 없음: " + taskId);
                }
            } else {
                System.out.println("⚠️ Redis에 프로젝트 태스크 캐시가 없어 업데이트 불가: " + projectId);
                System.out.println("🔄 캐시 워밍업을 통해 빈 캐시라도 생성 시도");
                
                // 캐시 워밍업 시도
                try {
                    warmUpProjectCache(projectId);
                    System.out.println("✅ 프로젝트 캐시 워밍업 완료: " + projectId + " (빈 캐시 생성, 첫 조회 시 자동 로드됩니다)");
                } catch (Exception warmupException) {
                    System.err.println("❌ 캐시 워밍업 실패: " + warmupException.getMessage());
                }
                
                System.out.println("❌ Redis 캐시 태스크 업데이트 실패: 프로젝트 캐시가 존재하지 않음: " + projectId);
                throw new RuntimeException("프로젝트 캐시가 존재하지 않음: " + projectId);
            }
        } catch (JsonProcessingException e) {
            System.err.println("❌ JSON 처리 실패: " + e.getMessage());
            throw new RuntimeException("JSON 처리 실패", e);
        } catch (Exception e) {
            System.err.println("❌ Redis 캐시 태스크 업데이트 실패: " + e.getMessage());
            throw new RuntimeException("Redis 캐시 업데이트 실패", e);
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
     * 프로젝트 관련 캐시 무효화 (통일된 캐시 사용)
     */
    public void invalidateProjectCache(String projectId) {
        try {
            // 통일된 캐시 키들
            String boardsKey = PROJECT_BOARDS_CACHE_KEY + projectId + ":boards";
            String boardsSummaryKey = PROJECT_BOARDS_CACHE_KEY + projectId + ":boards_summary";
            String tasksWithUserNameKey = PROJECT_TASKS_CACHE_KEY + projectId + ":tasks_with_username";
            String tasksStatsKey = PROJECT_TASKS_CACHE_KEY + projectId + ":stats";
            
            kanbanRedisTemplate.delete(boardsKey);
            kanbanRedisTemplate.delete(boardsSummaryKey);
            kanbanRedisTemplate.delete(tasksWithUserNameKey);
            kanbanRedisTemplate.delete(tasksStatsKey);
            
            System.out.println("🗑️ 프로젝트 캐시 무효화 완료: " + projectId + " (보드, 태스크, 통계 포함)");
        } catch (Exception e) {
            System.err.println("❌ 프로젝트 캐시 무효화 실패: " + e.getMessage());
        }
    }
    
    /**
     * 전체 프로젝트 관련 캐시 무효화 (스마트 캐싱 전략 대응)
     */
    public void invalidateAllProjectCaches(String projectId) {
        try {
            System.out.println("🧹 전체 프로젝트 캐시 무효화 시작: " + projectId);
            
            // 1. 기본 프로젝트 캐시 무효화
            invalidateProjectCache(projectId);
            
            // 2. 사용자 이름 포함 태스크 캐시 무효화
            invalidateTasksWithUserNameCache(projectId);
            
            // 3. 패턴 매칭으로 누락된 캐시 정리
            String projectPattern = "*:" + projectId + ":*";
            deleteKeysByPattern(projectPattern);
            
            System.out.println("✅ 전체 프로젝트 캐시 무효화 완료: " + projectId + " (모든 관련 캐시 정리됨)");
            
        } catch (Exception e) {
            System.err.println("❌ 전체 프로젝트 캐시 무효화 실패: " + e.getMessage());
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
     * 사용자 이름 포함 태스크 목록을 Redis에 캐싱 (최적화된 버전)
     * 프로젝트 전체 데이터를 한 번에 캐싱하여 중복 조회 최소화
     */
    public void cacheTasksWithUserName(String cacheKey, java.util.List<java.util.Map<String, Object>> tasks) {
        try {
            String json = objectMapper.writeValueAsString(tasks);
            kanbanRedisTemplate.opsForValue().set(cacheKey, json, PROJECT_DATA_CACHE_TTL, TimeUnit.SECONDS);
            System.out.println("🔧 Redis에 사용자 이름 포함 태스크 목록 캐싱: " + cacheKey + " (태스크 수: " + tasks.size() + ")");
            
            // 캐싱 효율성 로깅 추가
            long dataSize = json.getBytes().length;
            System.out.println("📏 캐시 데이터 크기: " + formatBytes(dataSize) + " (1회 조회로 전체 프로젝트 커버)");
            
        } catch (JsonProcessingException e) {
            System.err.println("❌ 사용자 이름 포함 태스크 목록 캐싱 실패: " + e.getMessage());
        }
    }
    
    /**
     * 바이트 크기를 읽기 쉬운 형태로 포맷팅
     */
    private String formatBytes(long bytes) {
        if (bytes < 1024) return bytes + " B";
        if (bytes < 1024 * 1024) return String.format("%.1f KB", bytes / 1024.0);
        if (bytes < 1024 * 1024 * 1024) return String.format("%.1f MB", bytes / (1024.0 * 1024.0));
        return String.format("%.1f GB", bytes / (1024.0 * 1024.0 * 1024.0));
    }
    
    /**
     * 스마트 캐싱 전략 - 프로젝트 데이터를 통합적으로 관리
     * 사용자의 제안에 따라 projectId 기반으로 모든 보드와 태스크 데이터를 효율적으로 관리
     */
    public void smartCacheProjectData(String projectId, 
                                    java.util.List<java.util.Map<String, Object>> boards,
                                    java.util.List<java.util.Map<String, Object>> tasks) {
        try {
            System.out.println("🧠 스마트 캐싱 전략 시작: projectId=" + projectId);
            
            // 1. 프로젝트 보드 데이터 캐싱 (요약 정보 포함)
            cacheProjectBoards(projectId, boards);
            
            // 2. 프로젝트 태스크 데이터 캐싱 (통계 정보 포함)
            cacheProjectTasks(projectId, tasks);
            
            // 3. 사용자 이름 포함 태스크 데이터 캐싱 (통합 버전)
            String userNameCacheKey = PROJECT_TASKS_CACHE_KEY + projectId + ":tasks_with_username";
            cacheTasksWithUserName(userNameCacheKey, tasks);
            
            // 4. 캐싱 효율성 보고
            int totalBoards = boards != null ? boards.size() : 0;
            int totalTasks = tasks != null ? tasks.size() : 0;
            
            System.out.println("✅ 스마트 캐싱 완료: " + projectId + 
                             " (보드 " + totalBoards + "개, 태스크 " + totalTasks + "개)");
            System.out.println("💡 최적화 효과: 1회 Redis 조회로 전체 프로젝트 데이터 접근 가능, " +
                             "애플리케이션 레벨 필터링으로 네트워크 트래픽 최소화");
            
        } catch (Exception e) {
            System.err.println("❌ 스마트 캐싱 전략 실행 실패: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * 사용자 이름 포함 태스크 목록을 Redis에서 조회 (최적화 버전)
     * 필요한 데이터만 추출하여 메모리 사용량과 네트워크 트래픽 최적화
     */
    @SuppressWarnings("unchecked")
    public java.util.List<java.util.Map<String, Object>> getCachedTasksWithUserName(String cacheKey) {
        try {
            String json = (String) kanbanRedisTemplate.opsForValue().get(cacheKey);
            
            if (json != null) {
                System.out.println("✅ Redis에서 사용자 이름 포함 태스크 목록 조회 성공: " + cacheKey);
                java.util.List<java.util.Map<String, Object>> allTasks = objectMapper.readValue(json, java.util.List.class);
                System.out.println("📊 총 조회된 태스크 수: " + allTasks.size() + "개 (프로젝트 전체)");
                return allTasks;
            } else {
                System.out.println("⚠️ Redis에 사용자 이름 포함 태스크 목록 캐시 없음: " + cacheKey);
            }
        } catch (JsonProcessingException e) {
            System.err.println("❌ 사용자 이름 포함 태스크 목록 조회 실패: " + e.getMessage());
        }
        
        return null;
    }
    
    /**
     * 특정 보드의 사용자 이름 포함 태스크만 필터링하여 조회 (성능 최적화)
     * 전체 프로젝트 데이터를 조회한 후 애플리케이션 레벨에서 필터링
     */
    @SuppressWarnings("unchecked")
    public java.util.List<java.util.Map<String, Object>> getCachedTasksWithUserNameByBoard(String projectId, String boardId) {
        try {
            String cacheKey = PROJECT_TASKS_CACHE_KEY + projectId + ":tasks_with_username";
            java.util.List<java.util.Map<String, Object>> allTasks = getCachedTasksWithUserName(cacheKey);
            
            if (allTasks != null) {
                // 특정 보드의 태스크만 필터링
                java.util.List<java.util.Map<String, Object>> filteredTasks = new java.util.ArrayList<>();
                
                for (java.util.Map<String, Object> task : allTasks) {
                    if (boardId.equals(task.get("boardId"))) {
                        filteredTasks.add(task);
                    }
                }
                
                System.out.println("🎯 보드별 태스크 필터링 완료: " + filteredTasks.size() + "개 (boardId: " + boardId + ")");
                return filteredTasks;
            }
        } catch (Exception e) {
            System.err.println("❌ 보드별 태스크 필터링 실패: " + e.getMessage());
        }
        
        return null;
    }
    
    /**
     * 특정 사용자의 태스크만 필터링하여 조회 (성능 최적화)
     */
    @SuppressWarnings("unchecked")
    public java.util.List<java.util.Map<String, Object>> getCachedTasksWithUserNameByUser(String projectId, String userId) {
        try {
            String cacheKey = PROJECT_TASKS_CACHE_KEY + projectId + ":tasks_with_username";
            java.util.List<java.util.Map<String, Object>> allTasks = getCachedTasksWithUserName(cacheKey);
            
            if (allTasks != null) {
                // 특정 사용자의 태스크만 필터링
                java.util.List<java.util.Map<String, Object>> filteredTasks = new java.util.ArrayList<>();
                
                for (java.util.Map<String, Object> task : allTasks) {
                    if (userId.equals(task.get("projectUserId"))) {
                        filteredTasks.add(task);
                    }
                }
                
                System.out.println("👤 사용자별 태스크 필터링 완료: " + filteredTasks.size() + "개 (userId: " + userId + ")");
                return filteredTasks;
            }
        } catch (Exception e) {
            System.err.println("❌ 사용자별 태스크 필터링 실패: " + e.getMessage());
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
            // 통일된 태스크 캐시 확인 (tasks_with_username만 사용)
            String tasksWithUserNameKey = PROJECT_TASKS_CACHE_KEY + projectId + ":tasks_with_username";
            String userNameTasksJson = (String) kanbanRedisTemplate.opsForValue().get(tasksWithUserNameKey);
            
            if (userNameTasksJson != null) {
                java.util.List<java.util.Map<String, Object>> userNameTasks = objectMapper.readValue(userNameTasksJson, java.util.List.class);
                System.out.println("✅ 프로젝트 태스크 캐시 존재: " + userNameTasks.size() + "개");
                
                // 최근 5개 태스크 ID 출력
                System.out.println("📋 최근 태스크들:");
                for (int i = Math.max(0, userNameTasks.size() - 5); i < userNameTasks.size(); i++) {
                    java.util.Map<String, Object> task = userNameTasks.get(i);
                    System.out.println("  - taskId: " + task.get("taskId") + ", title: " + task.get("taskTitle") + ", userName: " + task.get("userName"));
                }
            } else {
                System.out.println("❌ 프로젝트 태스크 캐시 없음: " + tasksWithUserNameKey);
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
            Long userNameTasksTtl = kanbanRedisTemplate.getExpire(tasksWithUserNameKey);
            Long boardsTtl = kanbanRedisTemplate.getExpire(boardsKey);
            System.out.println("⏰ TTL - 태스크: " + userNameTasksTtl + "초, 보드: " + boardsTtl + "초");
            
        } catch (Exception e) {
            System.err.println("❌ Redis 캐시 디버깅 실패: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println("🔍 ========== Redis 캐시 디버깅 완료 ==========");
    }
    
    /**
     * 모든 프로젝트 캐시 키 목록 조회 (최적화 효과 분석 포함)
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
            
            // 최적화 효과 분석
            analyzeOptimizationEffects(taskKeys, boardKeys);
            
        } catch (Exception e) {
            System.err.println("❌ 캐시 키 조회 실패: " + e.getMessage());
        }
        
        System.out.println("🔍 ========== 캐시 키 조회 완료 ==========");
    }
    
    /**
     * Redis 캐싱 최적화 효과 분석
     */
    private void analyzeOptimizationEffects(java.util.Set<String> taskKeys, java.util.Set<String> boardKeys) {
        try {
            System.out.println("📊 ========== 캐싱 최적화 효과 분석 ==========");
            
            int projectCount = 0;
            long totalCacheSize = 0;
            java.util.Map<String, Integer> projectStats = new java.util.HashMap<>();
            
            // 프로젝트별 캐시 통계 수집
            if (taskKeys != null) {
                for (String key : taskKeys) {
                    if (key.contains(":tasks_with_username")) {
                        String[] parts = key.split(":");
                        if (parts.length >= 3) {
                            String projectId = parts[2];
                            projectStats.put(projectId, projectStats.getOrDefault(projectId, 0) + 1);
                            projectCount++;
                            
                            // 캐시 크기 추정
                            try {
                                String data = (String) kanbanRedisTemplate.opsForValue().get(key);
                                if (data != null) {
                                    totalCacheSize += data.getBytes().length;
                                }
                            } catch (Exception e) {
                                // 크기 측정 실패는 무시
                            }
                        }
                    }
                }
            }
            
            System.out.println("📊 최적화 효과:");
            System.out.println("  - 활성 프로젝트 수: " + projectStats.size() + "개");
            System.out.println("  - 총 캐시 크기: " + formatBytes(totalCacheSize));
            System.out.println("  - 평균 프로젝트당 캐시 크기: " + 
                             (projectStats.size() > 0 ? formatBytes(totalCacheSize / projectStats.size()) : "0 B"));
            
            System.out.println("💡 최적화 장점:");
            System.out.println("  ✅ 1회 Redis 조회로 전체 프로젝트 데이터 접근");
            System.out.println("  ✅ 애플리케이션 레벨 필터링으로 네트워크 트래픽 최소화");
            System.out.println("  ✅ 중복 데이터 요청 제거로 Redis 부하 감소");
            System.out.println("  ✅ 통합 캐시 관리로 데이터 일관성 향상");
            
        } catch (Exception e) {
            System.err.println("❌ 최적화 효과 분석 실패: " + e.getMessage());
        }
    }
    
    /**
     * 프로젝트 캐시 워밍업 - 사용자 접속 시 미리 캐시 생성
     * USER_JOIN 시 호출되어 Redis 캐시를 미리 생성하여 카드 이동 시 캐시 업데이트 가능하게 함
     * 
     * 개선사항: 빈 캐시 대신 실제 DB 데이터로 캐시를 생성하여 태스크 업데이트 시 오류 방지
     */
    public void warmUpProjectCache(String projectId) {
        if (projectId == null || projectId.trim().isEmpty()) {
            System.err.println("❌ 프로젝트 캐시 워밍업 실패: 프로젝트 ID가 null 또는 빈값입니다");
            return;
        }
        
        try {
            System.out.println("🔥 프로젝트 캐시 워밍업 시작: " + projectId);
            
            // 캐시 키 생성
            String tasksWithUserNameKey = PROJECT_TASKS_CACHE_KEY + projectId + ":tasks_with_username";
            
            // 이미 캐시가 존재하는지 확인
            String existingCache = (String) kanbanRedisTemplate.opsForValue().get(tasksWithUserNameKey);
            if (existingCache != null) {
                System.out.println("ℹ️ 프로젝트 캐시가 이미 존재함: " + projectId + " (워밍업 생략)");
                return;
            }
            
            // 🚀 실제 DB에서 프로젝트 태스크 데이터 조회하여 캐시 생성
            System.out.println("📊 DB에서 프로젝트 태스크 데이터 조회 시작: " + projectId);
            
            // 1. 프로젝트의 모든 보드 조회
            java.util.List<com.demo.proworks.board.vo.BoardVo> boards = boardService.selectBoardsByProject(projectId);
            
            if (boards == null || boards.isEmpty()) {
                System.out.println("⚠️ 프로젝트에 보드가 없음 - 빈 캐시 생성: " + projectId);
                java.util.List<java.util.Map<String, Object>> emptyTasks = new java.util.ArrayList<>();
                String emptyJson = objectMapper.writeValueAsString(emptyTasks);
                kanbanRedisTemplate.opsForValue().set(tasksWithUserNameKey, emptyJson, PROJECT_DATA_CACHE_TTL, TimeUnit.SECONDS);
                System.out.println("✅ 빈 캐시 생성 완료: " + projectId);
                return;
            }
            
            // 2. 보드 ID 리스트 생성
            java.util.List<String> boardIds = new java.util.ArrayList<>();
            for (com.demo.proworks.board.vo.BoardVo board : boards) {
                boardIds.add(board.getBoardId());
            }
            
            System.out.println("📋 조회된 보드 개수: " + boards.size() + "개, 보드 IDs: " + boardIds);
            
            // 3. 프로젝트의 모든 태스크 조회 (사용자 이름 포함)
            java.util.List<com.demo.proworks.task.vo.TaskVo> tasks = taskService.selectTaskListWithUserNameBatch(projectId, boardIds);
            
            // 4. TaskVo를 Map으로 변환
            java.util.List<java.util.Map<String, Object>> taskMaps = new java.util.ArrayList<>();
            if (tasks != null) {
                for (com.demo.proworks.task.vo.TaskVo task : tasks) {
                    java.util.Map<String, Object> taskMap = convertTaskVoToMap(task);
                    taskMaps.add(taskMap);
                }
            }
            
            // 5. 캐시에 저장
            String tasksJson = objectMapper.writeValueAsString(taskMaps);
            kanbanRedisTemplate.opsForValue().set(tasksWithUserNameKey, tasksJson, PROJECT_DATA_CACHE_TTL, TimeUnit.SECONDS);
            
            System.out.println("✅ 프로젝트 캐시 워밍업 완료: " + projectId + " (실제 DB 데이터 " + taskMaps.size() + "개로 캐시 생성)");
            
        } catch (Exception e) {
            System.err.println("❌ 프로젝트 캐시 워밍업 실패: " + e.getMessage());
            e.printStackTrace();
            
            // 실패 시 빈 캐시라도 생성하여 업데이트 오류 방지
            try {
                String tasksWithUserNameKey = PROJECT_TASKS_CACHE_KEY + projectId + ":tasks_with_username";
                java.util.List<java.util.Map<String, Object>> emptyTasks = new java.util.ArrayList<>();
                String emptyJson = objectMapper.writeValueAsString(emptyTasks);
                kanbanRedisTemplate.opsForValue().set(tasksWithUserNameKey, emptyJson, PROJECT_DATA_CACHE_TTL, TimeUnit.SECONDS);
                System.out.println("🔄 워밍업 실패로 인한 빈 캐시 생성: " + projectId);
            } catch (Exception fallbackException) {
                System.err.println("❌ 빈 캐시 생성도 실패: " + fallbackException.getMessage());
            }
        }
    }

   
    /**
     * TaskVo를 Map으로 변환하는 헬퍼 메서드
     */
    private java.util.Map<String, Object> convertTaskVoToMap(com.demo.proworks.task.vo.TaskVo task) {
        java.util.Map<String, Object> map = new java.util.HashMap<>();
        
        map.put("taskId", task.getTaskId());
        map.put("boardId", task.getBoardId());
        map.put("projectId", task.getProjectId());
        map.put("taskTitle", task.getTaskTitle());
        map.put("projectUserId", task.getProjectUserId());
        map.put("projectRepoId", task.getProjectRepoId());
        map.put("userName", task.getUserName());
        map.put("priority", task.getPriority());
        map.put("startDate", task.getStartDate());
        map.put("endDate", task.getEndDate());
        map.put("tags", task.getTags());
        map.put("sortField", task.getSortField());
        map.put("sortOrder", task.getSortOrder());
        map.put("boardIds", task.getBoardIds());
        
        return map;
    }
}