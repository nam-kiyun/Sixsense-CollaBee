package com.demo.proworks.project.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.amazonaws.services.s3.AmazonS3;
import com.demo.proworks.board.dao.BoardDAO;
import com.demo.proworks.board.service.BoardService;
import com.demo.proworks.board.vo.BoardVo;
import com.demo.proworks.comment.dao.CommentDAO;
import com.demo.proworks.comment.vo.CommentVo;
import com.demo.proworks.email.vo.EmailVo;
import com.demo.proworks.filesrc.dao.FileSrcDAO;
import com.demo.proworks.filesrc.vo.FileSrcVo;
import com.demo.proworks.project.dao.ProjectDAO;
import com.demo.proworks.project.service.ProjectService;
import com.demo.proworks.project.vo.ProjectVo;
import com.demo.proworks.projectuser.dao.ProjectUserDAO;
import com.demo.proworks.projectuser.vo.ProjectUserVo;
import com.demo.proworks.sse.web.SseController;
import com.demo.proworks.task.dao.TaskDAO;
import com.demo.proworks.task.vo.TaskVo;
import com.demo.proworks.taskversion.dao.TaskVersionDAO;
import com.demo.proworks.taskversion.vo.TaskVersionVo;

/**
 * @subject : 프로젝트 정보 관련 처리를 담당하는 ServiceImpl
 * @description : 프로젝트 정보 관련 처리를 담당하는 ServiceImpl
 * @author : 국다인
 * @since : 2025/07/01
 * @modification ===========================================================
 *               DATE AUTHOR DESC
 *               ===========================================================
 *               2025/07/01 국다인 최초 생성
 * 
 */
@Service("projectServiceImpl")
//@EnableScheduling
public class ProjectServiceImpl implements ProjectService {

	private Set<String> sentTodayTasks = new HashSet<>();

	@Resource(name = "projectDAO")
	private ProjectDAO projectDAO;

	@Resource(name = "boardServiceImpl")
	private BoardService boardService;

	@Resource(name = "messageSource")
	private MessageSource messageSource;

	@Resource(name = "projectUserDAO")
	private ProjectUserDAO projectUserDAO;

	@Resource(name = "boardDAO")
	private BoardDAO boardDAO;

	@Resource(name = "taskDAO")
	private TaskDAO taskDAO;

	@Resource(name = "taskVersionDAO")
	private TaskVersionDAO taskVersionDAO;

	@Resource(name = "commentDAO")
	private CommentDAO commentDAO;

	@Resource(name = "fileSrcDAO")
	private FileSrcDAO fileSrcDAO;

	@Resource(name = "amazonS3")
	private AmazonS3 amazonS3;

	@Resource(name = "mailSender")
	private JavaMailSender mailSender;

	@Value("${spring.mail.username}")
	private String username;

	@Value("${spring.mail.password}")
	private String password;

	@Autowired
	private SseController sseController;

	/**
	 * 프로젝트 정보 목록을 조회합니다.
	 *
	 * @process 1. 프로젝트 정보 페이징 처리하여 목록을 조회한다. 2. 결과 List<ProjectVo>을(를) 리턴한다.
	 * 
	 * @param projectVo 프로젝트 정보 ProjectVo
	 * @return 프로젝트 정보 목록 List<ProjectVo>
	 * @throws Exception
	 */
	public List<ProjectVo> selectListProject(ProjectVo projectVo) throws Exception {
		List<ProjectVo> list = projectDAO.selectListProject(projectVo);

		return list;
	}

	/**
	 * 조회한 프로젝트 정보 전체 카운트
	 *
	 * @process 1. 프로젝트 정보 조회하여 전체 카운트를 리턴한다.
	 * 
	 * @param projectVo 프로젝트 정보 ProjectVo
	 * @return 프로젝트 정보 목록 전체 카운트
	 * @throws Exception
	 */
	public long selectListCountProject(ProjectVo projectVo) throws Exception {
		return projectDAO.selectListCountProject(projectVo);
	}

	/**
	 * 프로젝트 정보를 상세 조회한다.
	 *
	 * @process 1. 프로젝트 정보를 상세 조회한다. 2. 결과 ProjectVo을(를) 리턴한다.
	 * 
	 * @param projectVo 프로젝트 정보 ProjectVo
	 * @return 단건 조회 결과
	 * @throws Exception
	 */
	public ProjectVo selectProject(ProjectVo projectVo) throws Exception {
		ProjectVo resultVO = projectDAO.selectProject(projectVo);

		return resultVO;
	}

	/**
	 * 프로젝트 정보를 등록 처리 한다. 프로젝트 생성 시 기본 보드 3개 (할 일, 진행중, 완료)를 자동으로 생성한다.
	 *
	 * @process 1. 프로젝트 정보를 등록 처리 한다. 2. 생성된 프로젝트 ID로 기본 보드 3개를 생성한다. 3. 트랜잭션으로 처리하여
	 *          실패 시 전체 롤백한다.
	 * 
	 * @param projectVo 프로젝트 정보 ProjectVo
	 * @return 번호
	 * @throws Exception
	 */
	@Transactional
	public int insertProject(ProjectVo projectVo) throws Exception {
		System.out.println("ProjectServiceImpl.insertProject - 프로젝트 생성 시작: " + projectVo.getProjectName());

		// 1. 프로젝트 생성
		System.out.println("🔧 프로젝트 생성 전 - projectId: " + projectVo.getProjectId());
		int result = projectDAO.insertProject(projectVo);
		System.out.println("🔧 프로젝트 생성 후 - projectId: " + projectVo.getProjectId() + ", result: " + result);

		if (result > 0) {
			System.out.println("✅ 프로젝트 생성 성공 - projectId: " + projectVo.getProjectId());

			String createdProjectId = projectVo.getProjectId();
			if (createdProjectId != null && !createdProjectId.trim().isEmpty()) {
				// 2. 프로젝트 생성자를 project_user 테이블에 추가 (관리자 권한)
				addProjectCreatorToProjectUser(projectVo);
				System.out.println("✅ 프로젝트 생성자 project_user 테이블 추가 완료 - projectId: " + createdProjectId);

				// 3. 생성된 프로젝트 ID로 기본 보드 3개 생성
				createDefaultBoards(createdProjectId);
				System.out.println("✅ 기본 보드 3개 생성 완료 - projectId: " + createdProjectId);
			} else {
				System.err.println("❌ 생성된 프로젝트 ID가 null이거나 빈 문자열입니다: " + createdProjectId);
				throw new Exception("프로젝트 ID를 가져올 수 없습니다.");
			}
		} else {
			System.err.println("❌ 프로젝트 생성 실패 - result: " + result);
			throw new Exception("프로젝트 생성에 실패했습니다.");
		}

		return result;
	}

	/**
	 * 프로젝트 생성 시 기본 보드 3개를 자동으로 생성한다.
	 * 
	 * @param projectId 생성된 프로젝트 ID
	 * @throws Exception
	 */
	private void createDefaultBoards(String projectId) throws Exception {
		System.out.println("ProjectServiceImpl.createDefaultBoards - 기본 보드 생성 시작: " + projectId);

		// 기본 보드 정보 정의
		String[] defaultBoardTitles = { "할 일", "진행중", "완료" };

		for (String boardTitle : defaultBoardTitles) {
			BoardVo boardVo = new BoardVo();
			boardVo.setProjectId(projectId);
			boardVo.setBoardTitle(boardTitle);

			System.out.println("🔧 기본 보드 생성 중: " + boardTitle + " (projectId: " + projectId + ")");

			int boardResult = boardService.insertBoard(boardVo);

			if (boardResult > 0) {
				System.out.println("✅ 기본 보드 생성 성공: " + boardTitle + " (boardId: " + boardVo.getBoardId() + ", result: "
						+ boardResult + ")");
			} else {
				System.err.println("❌ 기본 보드 생성 실패: " + boardTitle + " (result: " + boardResult + ")");
				throw new Exception("기본 보드 생성에 실패했습니다: " + boardTitle);
			}
		}

		System.out.println("✅ 모든 기본 보드 생성 완료");
	}

	/**
	 * 프로젝트 생성자를 project_user 테이블에 관리자 권한으로 추가한다.
	 * 
	 * @param projectVo 생성된 프로젝트 정보
	 * @throws Exception
	 */
	private void addProjectCreatorToProjectUser(ProjectVo projectVo) throws Exception {
		System.out.println("🔧 프로젝트 생성자 project_user 테이블 추가 시작");

		ProjectUserVo projectUserVo = new ProjectUserVo();
		projectUserVo.setProjectId(projectVo.getProjectId());
		projectUserVo.setUserId(projectVo.getUserId());
		projectUserVo.setRole("ADMIN"); // 프로젝트 생성자는 관리자 권한

		int result = projectUserDAO.insertProjectUser(projectUserVo);

		if (result > 0) {
			System.out.println("✅ 프로젝트 생성자 project_user 테이블 추가 성공 - userId: " + projectVo.getUserId() + ", projectId: "
					+ projectVo.getProjectId());
		} else {
			System.err.println("❌ 프로젝트 생성자 project_user 테이블 추가 실패 - result: " + result);
			throw new Exception("프로젝트 생성자 project_user 테이블 추가에 실패했습니다.");
		}
	}

	/**
	 * 프로젝트 정보를 갱신 처리 한다.
	 *
	 * @process 1. 프로젝트 정보를 갱신 처리 한다.
	 * 
	 * @param projectVo 프로젝트 정보 ProjectVo
	 * @return 번호
	 * @throws Exception
	 */
	public int updateProject(ProjectVo projectVo) throws Exception {
		return projectDAO.updateProject(projectVo);
	}

	/**
	 * 프로젝트 정보를 삭제 처리 한다.
	 *
	 * @process 1. 프로젝트 정보를 삭제 처리 한다.
	 * 
	 * @param projectVo 프로젝트 정보 ProjectVo
	 * @return 번호
	 * @throws Exception
	 */
	public int deleteProject(ProjectVo projectVo) throws Exception {
		return projectDAO.deleteProject(projectVo);
	}

	/**
	 * 프로젝트 초대 이메일을 위한 프로젝트 정보와 팀장 정보를 조회합니다.
	 *
	 * @process 1. projectId로 프로젝트 정보와 팀장 정보를 JOIN하여 조회한다. 2. 조회된 데이터를 EmailVo에 매핑하여
	 *          리턴한다.
	 * 
	 * @param projectId 프로젝트 ID
	 * @return EmailVo 이메일 발송을 위한 프로젝트 정보
	 * @throws Exception
	 */
	public EmailVo selectProjectForEmail(String projectId) throws Exception {
		return projectDAO.selectProjectForEmail(projectId);
	}

	@Override
	public List<EmailVo> selectProjectsForEmailSend() throws Exception {
		return projectDAO.selectProjectsForEmailSend();
	}

	/**
	 * 프로젝트와 관련된 모든 데이터를 완전히 삭제 처리한다.
	 *
	 * @process 1. 프로젝트 관련 파일들을 S3에서 삭제한다. 2. 데이터베이스에서 다음 순서로 삭제한다: - FILESRC (파일
	 *          정보) - COMMENT (댓글) - TASK_VERSION (작업 버전) - TASK (작업) - BOARD (보드) -
	 *          PROJECT_USER (프로젝트 멤버) - PROJECT (프로젝트)
	 * 
	 * @param projectId 프로젝트 ID
	 * @return 삭제된 프로젝트 수
	 * @throws Exception
	 */
	@Transactional
	public int deleteProjectCompletely(String projectId) throws Exception {
		try {
			// 1. 프로젝트 관련 파일들을 S3에서 삭제
			deleteS3Files(projectId);

			// 2. 데이터베이스에서 순서대로 삭제

			// 2-1. FILESRC 삭제
			FileSrcVo fileSrcVo = new FileSrcVo();
			fileSrcVo.setProjectId(projectId);
			fileSrcDAO.deleteFileSrcByProjectId(fileSrcVo);

			// 2-2. COMMENT 삭제
			CommentVo commentVo = new CommentVo();
			commentVo.setProjectId(projectId);
			commentDAO.deleteCommentByProjectId(commentVo);

			// 2-3. TASK_VERSION 삭제
			TaskVersionVo taskVersionVo = new TaskVersionVo();
			taskVersionVo.setProjectId(projectId);
			taskVersionDAO.deleteTaskVersionByProjectId(taskVersionVo);

			// 2-4. TASK 삭제
			TaskVo taskVo = new TaskVo();
			taskVo.setProjectId(projectId);
			taskDAO.deleteTaskByProjectId(taskVo);

			// 2-5. BOARD 삭제
			BoardVo boardVo = new BoardVo();
			boardVo.setProjectId(projectId);
			boardDAO.deleteBoardByProjectId(boardVo);

			// 2-6. PROJECT_USER 삭제
			ProjectUserVo projectUserVo = new ProjectUserVo();
			projectUserVo.setProjectId(projectId);
			projectUserDAO.deleteProjectUserByProjectId(projectUserVo);

			// 2-7. PROJECT 삭제
			ProjectVo projectVo = new ProjectVo();
			projectVo.setProjectId(projectId);
			int deletedCount = projectDAO.deleteProject(projectVo);

			return deletedCount;

		} catch (Exception e) {
			throw new Exception("프로젝트 삭제 중 오류가 발생했습니다: " + e.getMessage(), e);
		}
	}

	/**
	 * S3에서 프로젝트 관련 파일들을 삭제한다.
	 *
	 * @param projectId 프로젝트 ID
	 * @throws Exception
	 */
	private void deleteS3Files(String projectId) throws Exception {
		try {
			String bucketName = "collabee";

			// 1. 프로젝트 이미지 URL 조회 및 삭제
			try {
				ProjectVo projectVo = new ProjectVo();
				projectVo.setProjectId(projectId);
				ProjectVo existingProject = projectDAO.selectProject(projectVo);

				if (existingProject != null && existingProject.getProjectImageUrl() != null) {
					String imageUrl = existingProject.getProjectImageUrl();

					// 기본 이미지가 아닌 경우에만 삭제
					if (!imageUrl.contains("default_project_image.jpg")) {
						// S3 Key 추출:
						// https://collabee.s3.ap-northeast-2.amazonaws.com/projectImage/project_15_xxxxx.png
						// -> projectImage/project_15_xxxxx.png
						String s3Key = imageUrl.substring(imageUrl.indexOf(".com/") + 5);

						System.out.println("프로젝트 이미지 S3 삭제: " + s3Key);
						amazonS3.deleteObject(bucketName, s3Key);
					}
				}
			} catch (Exception e) {
				System.err.println("프로젝트 이미지 S3 삭제 실패: " + e.getMessage());
			}

			// 2. 작업 관련 파일들 조회 및 삭제
			try {
				// FileSrc 테이블에서 파일 경로들 조회
				FileSrcVo fileSrcVo = new FileSrcVo();
				fileSrcVo.setProjectId(projectId);

				// 프로젝트 관련 파일들 조회 (SQL에서 JOIN으로 조회)
				List<FileSrcVo> fileSrcList = fileSrcDAO.selectFileSrcByProjectId(fileSrcVo);

				if (fileSrcList != null && !fileSrcList.isEmpty()) {
					for (FileSrcVo fileSrc : fileSrcList) {
						if (fileSrc.getFilePath() != null && !fileSrc.getFilePath().trim().isEmpty()) {
							try {
								// S3 Key 추출:
								// https://collabee.s3.ap-northeast-2.amazonaws.com/projectFiles/15/xxxxx.pdf
								// -> projectFiles/15/xxxxx.pdf
								String filePath = fileSrc.getFilePath();
								String s3Key = filePath.substring(filePath.indexOf(".com/") + 5);

								System.out.println("작업 파일 S3 삭제: " + s3Key);
								amazonS3.deleteObject(bucketName, s3Key);
							} catch (Exception e) {
								System.err.println("작업 파일 S3 삭제 실패: " + fileSrc.getFilePath() + " - " + e.getMessage());
							}
						}
					}
				}
			} catch (Exception e) {
				System.err.println("작업 파일들 S3 삭제 실패: " + e.getMessage());
			}

		} catch (Exception e) {
			// S3 삭제 실패는 로그만 남기고 계속 진행
			System.err.println("S3 파일 삭제 처리 실패: " + e.getMessage());
		}
	}

	@Override
	@Scheduled(cron = "0 0 * * * *")
	public void sendTaskReminder() throws Exception {
		try {
			Calendar cal = Calendar.getInstance();
			int currentHour = cal.get(Calendar.HOUR_OF_DAY);

			List<EmailVo> projects = selectProjectsForEmailSend();

			for (EmailVo project : projects) {
				String emailSendTime = project.getEmailSendTime();
				if (emailSendTime != null && !emailSendTime.isEmpty()) {
					try {
						String[] timeParts = emailSendTime.split(":");
						if (timeParts.length >= 2) {
							int emailHour = Integer.parseInt(timeParts[0]);

							if (emailHour == currentHour) {
							sendTodoTasksToUsers(project.getProjectId());
							System.out.println("메일 발송됩니다");
							sseController
									.sendNotification("📬 프로젝트 " + project.getProjectId() + " 할 일 알림 메일이 전송되었습니다.");

							}
						}
					} catch (Exception e) {
						System.err.println("시간 파싱 오류: " + emailSendTime + ", 오류: " + e.getMessage());
					}
				}
			}

			if (currentHour == 0) {
				sentTodayTasks.clear();
			}

		} catch (Exception e) {
			System.err.println("작업 알림 메일 발송 중 오류 발생: " + e.getMessage());
		}
	}

	private void sendTodoTasksToUsers(String projectId) {
		try {
			List<TaskVo> todoTasks = taskDAO.selectTodoTasksByProjectId(projectId);

			// 사용자별로 할일들을 그룹핑
			Map<String, List<TaskVo>> userTasksMap = new HashMap<>();
			for (TaskVo task : todoTasks) {
				String userKey = task.getProjectUserId(); // 사용자별 그룹핑 키
				userTasksMap.computeIfAbsent(userKey, k -> new ArrayList<>()).add(task);
			}

			// 사용자별로 하나의 메일 발송
			for (Map.Entry<String, List<TaskVo>> entry : userTasksMap.entrySet()) {
				String userKey = entry.getKey();
				List<TaskVo> userTasks = entry.getValue();

				// 해당 사용자의 메일이 오늘 이미 발송되었는지 확인
				String userEmailKey = projectId + "_" + userKey + "_" + getCurrentDateString();

				if (!sentTodayTasks.contains(userEmailKey)) {
					sendTasksReminderEmail(userTasks);
					sentTodayTasks.add(userEmailKey);
				}
			}
		} catch (Exception e) {
			System.err.println("할 일 작업 메일 발송 중 오류: " + e.getMessage());
		}
	}

	private void sendTasksReminderEmail(List<TaskVo> tasks) {
		try {
			if (tasks == null || tasks.isEmpty()) {
				return;
			}

			// 첫 번째 작업에서 사용자 정보 추출 (모든 작업이 같은 사용자에게 할당됨)
			TaskVo firstTask = tasks.get(0);
			String userEmail = taskDAO.selectUserEmailByProjectUserId(firstTask.getProjectUserId());
			String userName = firstTask.getUserName();

			if (userEmail == null || userEmail.isEmpty()) {
				System.err.println("사용자 이메일을 찾을 수 없습니다. ProjectUserId: " + firstTask.getProjectUserId());
				return;
			}

			String subject = "[COLLABEE] 오늘의 할 일 " + tasks.size() + "개";
			String content = createTasksReminderEmailContent(tasks, userName);

			JavaMailSenderImpl impl = (JavaMailSenderImpl) mailSender;
			impl.setUsername(username);
			impl.setPassword(password);

			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

			helper.setTo(userEmail);
			helper.setSubject(subject);
			helper.setText(content, true);

			mailSender.send(message);

			System.out.println("작업 알림 메일 발송 완료: " + userEmail + ", 작업 수: " + tasks.size());

		} catch (Exception e) {
			System.err.println("작업 알림 메일 발송 실패: " + e.getMessage());
		}
	}

	private String createTasksReminderEmailContent(List<TaskVo> tasks, String userName) {
		String content = "<html lang='ko'>" + "<head><meta charset='UTF-8'/><title>작업 알림</title>" + "<style>"
				+ "body { margin: 0; padding: 40px; font-family: 'Arial', sans-serif; text-align: center; }"
				+ ".header { max-width: 607px; margin: 0 auto; display: flex; align-items: center; justify-content: center; background-color: rgb(104, 101, 101); border-radius: 10px 10px 0 0; padding: 30px 20px; }"
				+ ".header img { width: 48px; height: 48px; margin-right: 14px; object-fit: contain; }"
				+ ".email-title { margin: 0; font-size: 32px; color: #ffb823; text-shadow: 1px 1px 3px rgba(0, 0, 0, 0.3); }"
				+ ".email-container { max-width: 600px; margin: 0 auto; background-color: white; border-radius: 0 0 10px 10px; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); padding: 24px; text-align: center; }"
				+ ".email-content { font-size: 15px; line-height: 1.5; margin-top: 10px; }"
				+ ".task-card { margin: 20px 0; text-align: left; padding: 20px; border: 1px solid #ddd; border-radius: 8px; background-color: #f9f9f9; }"
				+ ".task-title { font-size: 20px; font-weight: bold; color: #333; margin-bottom: 10px; }"
				+ ".task-info { font-size: 14px; color: #666; margin: 5px 0; }"
				+ ".email-footer { text-align: center; font-size: 14px; color: gray; margin-top: 20px; }"
				+ ".task-count { font-weight: bold; color: #ffb823; }" + "</style></head>" + "<body>"
				+ "<div class='header'>"
				+ "<img src='https://collabee.s3.ap-northeast-2.amazonaws.com/collabee.png' alt='COLLABEE 로고' />"
				+ "<h1 class='email-title'>COLLABEE</h1>" + "</div>" + "<div class='email-container'>"
				+ "<p class='email-content'>" + "안녕하세요, " + userName + "님!<br />"
				+ "오늘 처리하실 작업이 <span class='task-count'>" + tasks.size() + "개</span> 있습니다.<br />" + "아래 작업들을 확인해보세요."
				+ "</p>";

		// 각 할일에 대해 카드 생성
		for (int i = 0; i < tasks.size(); i++) {
			TaskVo task = tasks.get(i);
			content += "<div class='task-card'>" + "<div class='task-title'>" + task.getTaskTitle() + "</div>"
					+ "<div class='task-info'>우선순위: " + (task.getPriority() != null ? task.getPriority() : "보통")
					+ "</div>" + "<div class='task-info'>시작일: "
					+ (task.getStartDate() != null ? task.getStartDate() : "미정") + "</div>"
					+ "<div class='task-info'>종료일: " + (task.getEndDate() != null ? task.getEndDate() : "미정") + "</div>"
					+ "</div>";
		}

		content += "<p class='email-footer'>좋은 하루 되세요!</p>" + "</div>" + "</body>" + "</html>";

		return content;
	}

	private String getCurrentDateString() {
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(new java.util.Date());
	}

	/**
	 * 사용자가 참여한 프로젝트 목록을 조회합니다.
	 *
	 * @process 1. userId로 project_user 테이블에서 참여한 project_id들을 조회한다. 2. 조회된
	 *          project_id들로 project 테이블에서 프로젝트 상세 정보를 조회한다. 3. 각 프로젝트에 대해
	 *          selectProjectUsers를 호출하여 참여자 정보를 조회하고 설정한다.
	 * 
	 * @param userId 사용자 ID
	 * @return List<ProjectVo> 사용자가 참여한 프로젝트 목록 (참여자 정보 포함)
	 * @throws Exception
	 */
	public List<ProjectVo> selectUserParticipatedProjects(String userId) throws Exception {
		System.out.println("=== selectUserParticipatedProjects 메서드 호출됨 ===");
		System.out.println("1. 입력 파라미터 - userId: " + userId);

		// 사용자가 참여한 프로젝트 목록 조회
		System.out.println("2. DAO에서 프로젝트 목록 조회 시작...");
		List<ProjectVo> projects = projectDAO.selectUserParticipatedProjects(userId);

		System.out.println("3. DAO에서 반환된 프로젝트 개수: " + (projects != null ? projects.size() : "null"));
		if (projects != null) {
			System.out.println("4. 프로젝트 목록 상세:");
			for (int i = 0; i < projects.size(); i++) {
				ProjectVo p = projects.get(i);
				System.out.println("   프로젝트[" + i + "] - ID: " + p.getProjectId() + ", 이름: " + p.getProjectName());
			}
		}

		// 각 프로젝트에 대해 참여자 정보를 추가로 조회하여 설정
		if (projects != null && !projects.isEmpty()) {
			System.out.println("5. 각 프로젝트에 대해 참여자 정보 조회 시작...");
			for (int i = 0; i < projects.size(); i++) {
				ProjectVo project = projects.get(i);
				System.out.println("   [" + i + "] 프로젝트 처리 중 - ID: " + project.getProjectId() + ", 이름: "
						+ project.getProjectName());

				if (project.getProjectId() != null) {
					System.out.println("   [" + i + "] selectProjectUsers 호출 - projectId: " + project.getProjectId());
					// 프로젝트 참여자 정보 조회
					List<ProjectUserVo> projectUsers = selectProjectUsers(project.getProjectId());

					System.out.println("   [" + i + "] selectProjectUsers 결과 - 참여자 수: "
							+ (projectUsers != null ? projectUsers.size() : "null"));
					if (projectUsers != null) {
						for (int j = 0; j < projectUsers.size(); j++) {
							ProjectUserVo pu = projectUsers.get(j);
							System.out.println("      참여자[" + j + "] - userId: " + pu.getUserId() + ", userName: "
									+ pu.getUserName() + ", role: " + pu.getRole());
						}
					}

					// ProjectVo에 참여자 정보 설정
					project.setProjectUsers(projectUsers);
					System.out.println("   [" + i + "] 프로젝트에 참여자 정보 설정 완료");

					// 설정 후 확인
					List<ProjectUserVo> setProjectUsers = project.getProjectUsers();
					System.out.println("   [" + i + "] 설정 확인 - 프로젝트에 설정된 참여자 수: "
							+ (setProjectUsers != null ? setProjectUsers.size() : "null"));
				} else {
					System.out.println("   [" + i + "] 프로젝트 ID가 null이므로 건너뜀");
				}
			}
		} else {
			System.out.println("5. 조회된 프로젝트가 없어 참여자 정보 조회 건너뜀");
		}

		System.out.println("6. 최종 반환할 프로젝트 목록:");
		if (projects != null) {
			for (int i = 0; i < projects.size(); i++) {
				ProjectVo finalProject = projects.get(i);
				List<ProjectUserVo> finalUsers = finalProject.getProjectUsers();
				System.out.println("   최종[" + i + "] - ID: " + finalProject.getProjectId() + ", 이름: "
						+ finalProject.getProjectName() + ", 참여자 수: "
						+ (finalUsers != null ? finalUsers.size() : "null"));
			}
		}

		System.out.println("=== selectUserParticipatedProjects 메서드 완료 ===");
		return projects;
	}

	/**
	 * 특정 프로젝트의 참여자 정보를 조회합니다.
	 *
	 * @process 1. projectId로 project_user 테이블에서 해당 프로젝트에 참여한 사용자들을 조회한다. 2. 조회된 사용자
	 *          정보를 List<ProjectUserVo>로 리턴한다.
	 * 
	 * @param projectId 프로젝트 ID
	 * @return List<ProjectUserVo> 프로젝트 참여자 목록
	 * @throws Exception
	 */
	public List<ProjectUserVo> selectProjectUsers(String projectId) throws Exception {
		System.out.println("   >>> selectProjectUsers 호출 - projectId: " + projectId);
		List<ProjectUserVo> result = projectDAO.selectProjectUsers(projectId);
		System.out.println("   >>> selectProjectUsers DAO 결과 - 참여자 수: " + (result != null ? result.size() : "null"));
		if (result != null) {
			for (int i = 0; i < result.size(); i++) {
				ProjectUserVo pu = result.get(i);
				System.out.println("   >>> DAO 참여자[" + i + "] - userId: " + pu.getUserId() + ", userName: "
						+ pu.getUserName() + ", role: " + pu.getRole());
			}
		}
		return result;
	}

}
