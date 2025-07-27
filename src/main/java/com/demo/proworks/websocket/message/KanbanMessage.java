package com.demo.proworks.websocket.message;

/**
 * 칸반 보드 WebSocket 메시지 클래스
 * 클라이언트와 서버 간 통신을 위한 메시지 구조
 * 
 * @author Claude AI
 * @since 2025-01-15
 */
public class KanbanMessage {
    
    private String type;           // 메시지 타입 (CARD_MOVE, USER_JOIN, PING 등)
    private String taskId;         // 태스크 ID
    private String fromBoardId;    // 출발 보드 ID
    private String toBoardId;      // 도착 보드 ID
    private String boardId;        // 보드 ID (일반적인 보드 참조용)
    private String projectId;      // 프로젝트 ID
    private String userId;         // 사용자 ID
    private String message;        // 메시지 내용
    private Long timestamp;        // 타임스탬프
    
    // 태스크 상세 정보 필드들
    private String taskTitle;      // 태스크 제목
    private String projectUserId;  // 프로젝트 사용자 ID
    private String priority;       // 우선순위
    private String startDate;      // 시작일
    private String endDate;        // 종료일
    private String tags;           // 태그
    private String userName;       // 사용자 이름
    
    // 기본 생성자
    public KanbanMessage() {}
    
    // 생성자
    public KanbanMessage(String type, String message) {
        this.type = type;
        this.message = message;
        this.timestamp = System.currentTimeMillis();
    }

    // Getter & Setter
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getFromBoardId() {
        return fromBoardId;
    }

    public void setFromBoardId(String fromBoardId) {
        this.fromBoardId = fromBoardId;
    }

    public String getToBoardId() {
        return toBoardId;
    }

    public void setToBoardId(String toBoardId) {
        this.toBoardId = toBoardId;
    }

    public String getBoardId() {
        return boardId;
    }

    public void setBoardId(String boardId) {
        this.boardId = boardId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getTaskTitle() {
        return taskTitle;
    }

    public void setTaskTitle(String taskTitle) {
        this.taskTitle = taskTitle;
    }

    public String getProjectUserId() {
        return projectUserId;
    }

    public void setProjectUserId(String projectUserId) {
        this.projectUserId = projectUserId;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String toString() {
        return "KanbanMessage{" +
                "type='" + type + '\'' +
                ", taskId='" + taskId + '\'' +
                ", fromBoardId='" + fromBoardId + '\'' +
                ", toBoardId='" + toBoardId + '\'' +
                ", boardId='" + boardId + '\'' +
                ", projectId='" + projectId + '\'' +
                ", userId='" + userId + '\'' +
                ", message='" + message + '\'' +
                ", taskTitle='" + taskTitle + '\'' +
                ", projectUserId='" + projectUserId + '\'' +
                ", priority='" + priority + '\'' +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", tags='" + tags + '\'' +
                ", userName='" + userName + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}