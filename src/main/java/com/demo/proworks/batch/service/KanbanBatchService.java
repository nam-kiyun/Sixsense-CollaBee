package com.demo.proworks.batch.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.demo.proworks.redis.service.KanbanRedisService;
import com.demo.proworks.task.service.TaskService;
import com.demo.proworks.task.vo.TaskVo;
import com.demo.proworks.websocket.message.KanbanMessage;
import com.demo.proworks.websocket.handler.KanbanWebSocketHandler;

/**
 * 칸반 보드 배치 처리 서비스
 * Redis 캐시에서 데이터베이스로 주기적 업데이트 처리
 * 
 * @author Claude AI
 * @since 2025-01-15
 */
@Service
public class KanbanBatchService {

    @Autowired
    private KanbanRedisService redisService;
    
    @Autowired
    private TaskService taskService;
    
    // 배치 처리 상태
    private boolean isBatchRunning = false;
    private long lastBatchTime = 0;
    private int totalProcessedTasks = 0;
    private int totalFailedTasks = 0;

    /**
     * 5분마다 배치 처리 실행 (운영용)
     * Redis 캐시의 태스크 이동 데이터를 데이터베이스에 반영
     */
    @Scheduled(fixedDelay = 30000) // 5분 간격 (300초)
    public void processBatchUpdate() {
        if (isBatchRunning) {
            System.out.println("이전 배치 처리가 아직 진행 중입니다. 건너뜁니다.");
            return;
        }

        try {
            isBatchRunning = true;
            lastBatchTime = System.currentTimeMillis();
            
            long queueSize = redisService.getBatchQueueSize();
            if (queueSize == 0) {
                System.out.println("배치 처리할 태스크가 없습니다.");
                return;
            }

            System.out.println("배치 처리 시작 - 대기 중인 태스크: " + queueSize + "개");
            
            // 배치 처리 큐에서 모든 태스크 가져오기
            List<String> taskIds = redisService.popAllFromBatchQueue();
            
            if (taskIds.isEmpty()) {
                System.out.println("배치 처리 중 큐가 비워졌습니다.");
                return;
            }

            // 비동기로 태스크 처리
            CompletableFuture<Void> batchFuture = processBatchAsync(taskIds);
            
            // 최대 30초 대기
            batchFuture.get(30, java.util.concurrent.TimeUnit.SECONDS);
            
            System.out.println("배치 처리 완료 - 처리된 태스크: " + taskIds.size() + "개");
            
        } catch (Exception e) {
            System.err.println("배치 처리 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
        } finally {
            isBatchRunning = false;
        }
    }

    /**
     * 비동기 배치 처리
     */
    @Async
    public CompletableFuture<Void> processBatchAsync(List<String> taskIds) {
        int successCount = 0;
        int failCount = 0;
        
        for (String taskId : taskIds) {
            try {
                // Redis에서 태스크 이동 정보 조회
                KanbanMessage moveMessage = redisService.getTaskMove(taskId);
                
                if (moveMessage == null) {
                    System.out.println("Redis에서 태스크 정보를 찾을 수 없습니다: " + taskId);
                    failCount++;
                    continue;
                }

                // 데이터베이스 업데이트
                boolean updateResult = updateTaskInDatabase(moveMessage);
                
                if (updateResult) {
                    // 성공하면 Redis에서 삭제
                    redisService.deleteTaskMove(taskId);
                    successCount++;
                    System.out.println("태스크 업데이트 성공: " + taskId + " -> " + moveMessage.getToBoardId());
                    
                    // 태스크 이동 후 Redis 캐시 업데이트
                    if (moveMessage.getProjectId() != null) {
                        redisService.updateTaskInCache(moveMessage.getProjectId(), taskId, moveMessage.getToBoardId());
                    }
                } else {
                    failCount++;
                    System.err.println("태스크 업데이트 실패: " + taskId);
                }
                
            } catch (Exception e) {
                failCount++;
                System.err.println("태스크 처리 중 오류: " + taskId + " - " + e.getMessage());
            }
        }
        
        totalProcessedTasks += successCount;
        totalFailedTasks += failCount;
        
        System.out.println("배치 처리 결과 - 성공: " + successCount + ", 실패: " + failCount);
        
        return CompletableFuture.completedFuture(null);
    }

    /**
     * 데이터베이스에 태스크 이동 정보 업데이트
     */
    private boolean updateTaskInDatabase(KanbanMessage moveMessage) {
        try {
            System.out.println("=== DB 업데이트 시작 ===");
            System.out.println("📦 태스크 ID: " + moveMessage.getTaskId());
            System.out.println("📋 이동: " + moveMessage.getFromBoardId() + " → " + moveMessage.getToBoardId());
            System.out.println("👤 사용자: " + moveMessage.getUserId());
            System.out.println("🏷️ 프로젝트: " + moveMessage.getProjectId());
            
            // TaskVo 객체 생성
            TaskVo taskVo = new TaskVo();
            taskVo.setTaskId(moveMessage.getTaskId());
            taskVo.setBoardId(moveMessage.getToBoardId());
            
            long dbStartTime = System.currentTimeMillis();
            
            // TaskService를 통해 보드 위치만 데이터베이스 업데이트 (칸반 카드 이동용)
            int updateCount = taskService.updateTaskBoard(taskVo);
            
            long dbEndTime = System.currentTimeMillis();
            long dbDuration = dbEndTime - dbStartTime;
            
            if (updateCount > 0) {
                System.out.println("✅ DB 업데이트 성공 (" + dbDuration + "ms)");
                System.out.println("📊 영향받은 행 수: " + updateCount);
                return true;
            } else {
                System.err.println("❌ DB 업데이트 실패 - 영향받은 행 없음");
                return false;
            }
            
        } catch (Exception e) {
            System.err.println("❌ 데이터베이스 업데이트 중 예외 발생:");
            System.err.println("   - 태스크 ID: " + moveMessage.getTaskId());
            System.err.println("   - 에러 메시지: " + e.getMessage());
            System.err.println("   - 에러 타입: " + e.getClass().getSimpleName());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 10분마다 Redis 정리 작업 실행
     */
    @Scheduled(fixedDelay = 600000) // 10분 간격
    public void cleanupRedisCache() {
        try {
            System.out.println("Redis 캐시 정리 작업 시작");
            
            // 만료된 키 정리
            redisService.cleanupExpiredKeys();
            
            // Redis 통계 출력
            java.util.Map<String, Object> stats = redisService.getRedisStats();
            System.out.println("Redis 통계: " + stats);
            
            System.out.println("Redis 캐시 정리 작업 완료");
            
        } catch (Exception e) {
            System.err.println("Redis 정리 작업 중 오류: " + e.getMessage());
        }
    }

    /**
     * 30분마다 배치 처리 통계 리포트 출력
     */
    @Scheduled(fixedDelay = 1800000) // 30분 간격
    public void printBatchStatistics() {
        try {
            System.out.println("=== 배치 처리 통계 리포트 ===");
            System.out.println("총 처리된 태스크: " + totalProcessedTasks + "개");
            System.out.println("총 실패한 태스크: " + totalFailedTasks + "개");
            System.out.println("마지막 배치 처리 시간: " + new java.util.Date(lastBatchTime));
            System.out.println("현재 배치 처리 상태: " + (isBatchRunning ? "실행 중" : "대기 중"));
            
            // Redis 상태 확인
            boolean redisConnected = redisService.isRedisConnected();
            System.out.println("Redis 연결 상태: " + (redisConnected ? "연결됨" : "연결 안됨"));
            
            if (redisConnected) {
                long queueSize = redisService.getBatchQueueSize();
                System.out.println("현재 배치 큐 크기: " + queueSize);
            }
            
            System.out.println("==============================");
            
        } catch (Exception e) {
            System.err.println("통계 리포트 출력 중 오류: " + e.getMessage());
        }
    }

    /**
     * 시스템 종료 시 남은 태스크 처리
     */
    @javax.annotation.PreDestroy
    public void onShutdown() {
        try {
            System.out.println("시스템 종료 중 - 남은 배치 태스크 처리");
            
            // 현재 배치가 실행 중이면 완료까지 대기
            int waitCount = 0;
            while (isBatchRunning && waitCount < 10) {
                Thread.sleep(1000);
                waitCount++;
            }
            
            // 남은 태스크가 있으면 마지막으로 한 번 더 처리
            long queueSize = redisService.getBatchQueueSize();
            if (queueSize > 0) {
                System.out.println("남은 태스크 " + queueSize + "개 처리 중...");
                List<String> remainingTasks = redisService.popAllFromBatchQueue();
                processBatchAsync(remainingTasks).get(30, java.util.concurrent.TimeUnit.SECONDS);
            }
            
            System.out.println("시스템 종료 처리 완료");
            
        } catch (Exception e) {
            System.err.println("시스템 종료 처리 중 오류: " + e.getMessage());
        }
    }

    /**
     * 수동 배치 처리 실행
     * 관리자나 테스트에서 사용
     */
    public void triggerBatchProcessing() {
        if (isBatchRunning) {
            throw new RuntimeException("배치 처리가 이미 실행 중입니다.");
        }
        
        try {
            processBatchUpdate();
        } catch (Exception e) {
            throw new RuntimeException("수동 배치 처리 실행 실패: " + e.getMessage(), e);
        }
    }

    /**
     * 세션 종료 시 즉시 배치 처리 실행
     * 데이터 손실 방지를 위해 모든 대기 중인 태스크를 즉시 DB에 저장
     */
    public void processImmediateBatch() {
        System.out.println("🚨 즉시 배치 처리 실행 - 세션 종료로 인한 데이터 보호");
        
        try {
            long queueSize = redisService.getBatchQueueSize();
            if (queueSize == 0) {
                System.out.println("💾 처리할 대기 태스크가 없습니다.");
                return;
            }
            
            System.out.println("💾 대기 중인 태스크 즉시 처리: " + queueSize + "개");
            
            // 배치 처리 큐에서 모든 태스크 가져오기
            List<String> taskIds = redisService.popAllFromBatchQueue();
            
            if (taskIds.isEmpty()) {
                System.out.println("💾 처리 중 큐가 비워졌습니다.");
                return;
            }
            
            // 동기적으로 즉시 처리 (비동기 대신 동기로 완료까지 대기)
            int successCount = 0;
            int failCount = 0;
            
            for (String taskId : taskIds) {
                try {
                    // Redis에서 태스크 이동 정보 조회
                    KanbanMessage moveMessage = redisService.getTaskMove(taskId);
                    
                    if (moveMessage == null) {
                        System.out.println("❌ Redis에서 태스크 정보를 찾을 수 없습니다: " + taskId);
                        failCount++;
                        continue;
                    }
                    
                    // 데이터베이스 업데이트
                    boolean updateResult = updateTaskInDatabase(moveMessage);
                    
                    if (updateResult) {
                        // 성공하면 Redis에서 삭제
                        redisService.deleteTaskMove(taskId);
                        successCount++;
                        System.out.println("✅ 즉시 처리 성공: " + taskId + " -> " + moveMessage.getToBoardId());
                    } else {
                        failCount++;
                        System.err.println("❌ 즉시 처리 실패: " + taskId);
                    }
                    
                } catch (Exception e) {
                    failCount++;
                    System.err.println("❌ 즉시 처리 중 오류: " + taskId + " - " + e.getMessage());
                }
            }
            
            totalProcessedTasks += successCount;
            totalFailedTasks += failCount;
            
            System.out.println("💾 즉시 배치 처리 완료 - 성공: " + successCount + ", 실패: " + failCount);
            
        } catch (Exception e) {
            System.err.println("❌ 즉시 배치 처리 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 배치 처리 상태 조회
     */
    public java.util.Map<String, Object> getBatchStatus() {
        java.util.Map<String, Object> status = new java.util.HashMap<>();
        
        status.put("isRunning", isBatchRunning);
        status.put("lastBatchTime", lastBatchTime);
        status.put("totalProcessedTasks", totalProcessedTasks);
        status.put("totalFailedTasks", totalFailedTasks);
        status.put("redisConnected", redisService.isRedisConnected());
        
        try {
            status.put("queueSize", redisService.getBatchQueueSize());
            status.put("redisStats", redisService.getRedisStats());
        } catch (Exception e) {
            status.put("redisError", e.getMessage());
        }
        
        return status;
    }

    /**
     * 배치 처리 통계 초기화
     */
    public void resetBatchStatistics() {
        totalProcessedTasks = 0;
        totalFailedTasks = 0;
        lastBatchTime = 0;
        System.out.println("배치 처리 통계가 초기화되었습니다.");
    }
}