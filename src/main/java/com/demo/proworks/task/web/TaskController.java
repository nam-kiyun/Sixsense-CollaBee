package com.demo.proworks.task.web;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.demo.proworks.filesrc.vo.FileSrcVo;
import com.demo.proworks.task.service.TaskService;
import com.demo.proworks.task.vo.TaskUpdateVo;
import com.demo.proworks.task.vo.TaskVo;
import com.demo.proworks.task.vo.TaskListVo;
import com.demo.proworks.redis.service.KanbanRedisService;
import com.demo.proworks.board.service.BoardService;
import com.demo.proworks.board.vo.BoardVo;
import com.demo.proworks.taskversion.service.TaskVersionService;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.inswave.elfw.annotation.ElDescription;
import com.inswave.elfw.annotation.ElService;
import com.inswave.elfw.annotation.ElValidator;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @subject : 업무(Task) 정보 관련 처리를 담당하는 컨트롤러
 * @description : 업무(Task) 정보 관련 처리를 담당하는 컨트롤러
 * @author : 남기윤
 * @since : 2025/07/01
 * @modification ===========================================================
 *               DATE AUTHOR DESC
 *               ===========================================================
 *               2025/07/01 남기윤 최초 생성
 * 
 */
@Controller
public class TaskController {

	/** TaskService */
	@Resource(name = "taskServiceImpl")
	private TaskService taskService;

	/** BoardService - 보드 정보 조회를 위한 서비스 */
	@Resource(name = "boardServiceImpl")
	private BoardService boardService;

	/** TaskVersionService */
	@Resource(name = "taskVersionServiceImpl")
	private TaskVersionService taskVersionService;

	@Resource(name = "amazonS3")
	private AmazonS3 amazonS3;

	/** KanbanRedisService - Redis 캐싱을 위한 서비스 */
	@Autowired
	private KanbanRedisService kanbanRedisService;

	/**
	 * 업무(Task) 정보 목록을 조회합니다. 프로젝트별 태스크 조회 시 Redis 캐싱을 적용하여 성능을 최적화합니다.
	 *
	 * @param taskVo 업무(Task) 정보
	 * @return 목록조회 결과
	 * @throws Exception
	 */
	@ElService(key = "TaskList")
	@RequestMapping(value = "TaskList")
	@ElDescription(sub = "업무(Task) 정보 목록조회", desc = "페이징을 처리하여 업무(Task) 정보 목록 조회를 한다.")
	@SuppressWarnings("unchecked")
	public TaskListVo selectListTask(TaskVo taskVo) throws Exception {

		System.out.println("🔍 태스크 목록 조회 요청 - boardId: " + taskVo.getBoardId() + ", tags: " + taskVo.getTags());

		// boardId가 있는 경우 Redis 캐싱 적용 (칸반보드용)
		// TaskVo에는 projectId가 없으므로 boardId를 통해 프로젝트를 추정
		if (taskVo.getBoardId() != null && !taskVo.getBoardId().trim().isEmpty()) {
			return selectTasksWithRedisCache(taskVo);
		}

		// 일반적인 목록 조회는 기존 방식 유지
		List<TaskVo> taskList = taskService.selectListTask(taskVo);
		long totCnt = taskService.selectListCountTask(taskVo);

		TaskListVo retTaskList = new TaskListVo();
		retTaskList.setTaskVoList(taskList);
		retTaskList.setTotalCount(totCnt);
		retTaskList.setPageSize(taskVo.getPageSize());
		retTaskList.setPageIndex(taskVo.getPageIndex());

		return retTaskList;
	}

	/**
	 * Redis 캐싱을 적용한 태스크 목록 조회 (칸반보드용)
	 */
	private TaskListVo selectTasksWithRedisCache(TaskVo taskVo) throws Exception {
		// 정렬 파라미터 추출
		String sortField = taskVo.getSortField();
		String sortOrder = taskVo.getSortOrder();

		System.out.println("🔄 정렬 파라미터 확인 - sortField: " + sortField + ", sortOrder: " + sortOrder);

		// 기본값 설정
		if (sortField == null || sortField.trim().isEmpty()) {
			sortField = "startDate";
		}
		if (sortOrder == null || sortOrder.trim().isEmpty()) {
			sortOrder = "asc";
		}

		// boardId를 통해 projectId 찾기
		String projectId = null;
		if (taskVo.getBoardId() != null) {
			try {
				BoardVo boardVo = new BoardVo();
				boardVo.setBoardId(taskVo.getBoardId());
				BoardVo board = boardService.selectBoard(boardVo);
				if (board != null) {
					projectId = board.getProjectId();
				}
			} catch (Exception e) {
				System.err.println("❌ boardId로 projectId 조회 실패: " + e.getMessage());
			}
		}

		if (projectId == null) {
			System.out.println("⚠️ projectId를 찾을 수 없어 일반 DB 조회로 진행");
			List<TaskVo> taskList = taskService.selectListTask(taskVo);
			long totCnt = taskService.selectListCountTask(taskVo);

			TaskListVo retTaskList = new TaskListVo();
			retTaskList.setTaskVoList(taskList);
			retTaskList.setTotalCount(totCnt);
			retTaskList.setPageSize(taskVo.getPageSize());
			retTaskList.setPageIndex(taskVo.getPageIndex());

			return retTaskList;
		}

		try {
			// 1. Redis 캐시에서 프로젝트의 전체 태스크 목록 조회 (정렬 적용)
			List<java.util.Map<String, Object>> cachedTasks = kanbanRedisService
					.getProjectTasksFromCacheWithSort(projectId, sortField, sortOrder);

			if (cachedTasks != null) {
				System.out.println("✅ Redis 캐시에서 정렬된 태스크 목록 조회 성공: " + cachedTasks.size() + "개 (정렬: " + sortField + " "
						+ sortOrder + ")");

				// 캐시된 데이터를 TaskVo로 변환하고 필터링 (이미 정렬됨)
				List<TaskVo> taskList = convertAndFilterTasks(cachedTasks, taskVo);

				System.out.println(
						"📊 Redis 캐시에서 필터링된 태스크 개수: " + taskList.size() + "개 (boardId: " + taskVo.getBoardId() + ")");

				TaskListVo retTaskList = new TaskListVo();
				retTaskList.setTaskVoList(taskList);
				retTaskList.setTotalCount(taskList.size());
				retTaskList.setPageSize(taskVo.getPageSize());
				retTaskList.setPageIndex(taskVo.getPageIndex());

				// 최종 응답 데이터 로깅
				System.out.println("📤 클라이언트로 전송할 최종 응답 데이터 (boardId: " + taskVo.getBoardId() + "):");
				for (TaskVo task : taskList) {
					System.out.println("  - taskId: " + task.getTaskId() + ", boardId: " + task.getBoardId()
							+ ", title: " + task.getTaskTitle());
				}

				return retTaskList;
			}

			// 3. 캐시 미스 - DB에서 프로젝트 전체 태스크 조회
			System.out.println("⚠️ Redis 캐시 미스 - 프로젝트 전체 태스크 DB 조회 시작");

			// 프로젝트 전체 태스크 조회 (새로운 메서드 사용)
			List<TaskVo> allProjectTasks = taskService.selectTasksByProject(projectId);

			System.out.println("📊 프로젝트 전체 태스크 조회 완료: " + (allProjectTasks != null ? allProjectTasks.size() : 0) + "개");

			// 각 태스크의 boardId 확인 (디버깅용)
			if (allProjectTasks != null) {
				for (TaskVo task : allProjectTasks) {
					System.out.println("DEBUG - 조회된 태스크: taskId=" + task.getTaskId() + ", boardId=" + task.getBoardId()
							+ ", taskTitle=" + task.getTaskTitle());
				}
			}

			// 4. 프로젝트 전체 태스크를 정렬 후 Redis에 캐싱
			if (allProjectTasks != null && !allProjectTasks.isEmpty()) {
				// 4-1. 정렬 적용
				allProjectTasks = sortTaskList(allProjectTasks, sortField, sortOrder);
				System.out.println("🔄 DB 조회 데이터 정렬 완료: " + sortField + " " + sortOrder);

				// 4-2. TaskVo 리스트를 Map 리스트로 변환
				List<java.util.Map<String, Object>> taskMapList = convertTaskVoListToMapList(allProjectTasks);
				kanbanRedisService.cacheProjectTasks(projectId, taskMapList);
				System.out.println("💾 정렬된 프로젝트 전체 태스크를 Redis에 캐싱 완료: " + allProjectTasks.size() + "개");
			}

			// 5. 현재 요청한 보드의 태스크만 필터링하여 반환
			List<TaskVo> taskList = new java.util.ArrayList<>();
			if (allProjectTasks != null) {
				for (TaskVo task : allProjectTasks) {
					System.out.println("DEBUG - 태스크 필터링 확인: taskId=" + task.getTaskId() + ", boardId="
							+ task.getBoardId() + ", 요청 boardId=" + taskVo.getBoardId() + ", tags=" + task.getTags());

					// boardId 필터링
					boolean boardMatch = true;
					if (taskVo.getBoardId() != null && !taskVo.getBoardId().trim().isEmpty()) {
						boardMatch = taskVo.getBoardId().equals(task.getBoardId());
					}

					// 태그 필터링은 프론트엔드에서 처리
					boolean tagMatch = true;

					// 모든 조건 만족 시 추가
					if (boardMatch && tagMatch) {
						taskList.add(task);
						System.out.println("DEBUG - 필터링된 태스크: " + task.getTaskTitle());
					}
				}
			}

			long totCnt = taskList.size();
			System.out.println("📊 필터링된 보드별 태스크 개수: " + totCnt + "개 (boardId: " + taskVo.getBoardId() + ")");

			TaskListVo retTaskList = new TaskListVo();
			retTaskList.setTaskVoList(taskList);
			retTaskList.setTotalCount(totCnt);
			retTaskList.setPageSize(taskVo.getPageSize());
			retTaskList.setPageIndex(taskVo.getPageIndex());

			return retTaskList;

		} catch (Exception e) {
			System.err.println("❌ 태스크 목록 조회 중 오류 발생: " + e.getMessage());
			e.printStackTrace();
			// Redis 오류 시 DB에서 직접 조회
			System.out.println("🔄 Redis 오류로 인한 DB 직접 조회 시도");

			List<TaskVo> taskList = taskService.selectListTask(taskVo);
			long totCnt = taskService.selectListCountTask(taskVo);

			TaskListVo retTaskList = new TaskListVo();
			retTaskList.setTaskVoList(taskList);
			retTaskList.setTotalCount(totCnt);
			retTaskList.setPageSize(taskVo.getPageSize());
			retTaskList.setPageIndex(taskVo.getPageIndex());

			return retTaskList;
		}
	}

	/**
	 * 캐시된 태스크 데이터를 TaskVo로 변환하고 조건에 따라 필터링
	 */
	private List<TaskVo> convertAndFilterTasks(List<java.util.Map<String, Object>> cachedTasks, TaskVo filterVo) {
		List<TaskVo> taskList = new java.util.ArrayList<>();

		for (java.util.Map<String, Object> map : cachedTasks) {
			TaskVo task = convertMapToTaskVo(map);

			// boardId 필터링 적용
			boolean boardMatch = true;
			if (filterVo.getBoardId() != null && !filterVo.getBoardId().trim().isEmpty()) {
				boardMatch = filterVo.getBoardId().equals(task.getBoardId());
			}

			// 태그 필터링은 프론트엔드에서 처리
			boolean tagMatch = true;

			// 모든 조건이 만족하는 경우만 추가
			if (boardMatch && tagMatch) {
				taskList.add(task);
			}
		}

		System.out.println("🔍 캐시 필터링 결과: " + taskList.size() + "개 태스크");
		return taskList;
	}

	/**
	 * Map을 TaskVo로 변환
	 */
	private TaskVo convertMapToTaskVo(java.util.Map<String, Object> map) {
		TaskVo task = new TaskVo();

		// Map에서 TaskVo 필드로 변환 (실제 VO 필드명에 맞게 수정)
		if (map.get("taskId") != null)
			task.setTaskId(map.get("taskId").toString());
		if (map.get("boardId") != null)
			task.setBoardId(map.get("boardId").toString());
		if (map.get("projectUserId") != null)
			task.setProjectUserId(map.get("projectUserId").toString());
		if (map.get("projectRepoId") != null)
			task.setProjectRepoId(map.get("projectRepoId").toString());
		if (map.get("taskTitle") != null)
			task.setTaskTitle(map.get("taskTitle").toString());
		if (map.get("priority") != null)
			task.setPriority(map.get("priority").toString());
		if (map.get("startDate") != null)
			task.setStartDate(map.get("startDate").toString());
		if (map.get("endDate") != null)
			task.setEndDate(map.get("endDate").toString());
		if (map.get("tags") != null)
			task.setTags(map.get("tags").toString());

		return task;
	}

	/**
	 * TaskVo 리스트를 Map 리스트로 변환
	 */
	private List<java.util.Map<String, Object>> convertTaskVoListToMapList(List<TaskVo> taskList) {
		List<java.util.Map<String, Object>> mapList = new java.util.ArrayList<>();

		for (TaskVo task : taskList) {
			java.util.Map<String, Object> map = new java.util.HashMap<>();

			// TaskVo 필드를 Map으로 변환 (실제 DB 컬럼명에 맞춤)
			map.put("taskId", task.getTaskId());
			map.put("taskTitle", task.getTaskTitle());
			map.put("boardId", task.getBoardId());
			map.put("projectUserId", task.getProjectUserId());
			map.put("projectRepoId", task.getProjectRepoId());
			map.put("priority", task.getPriority());
			map.put("startDate", task.getStartDate());
			map.put("endDate", task.getEndDate());
			map.put("tags", task.getTags());

			mapList.add(map);
		}

		return mapList;
	}

	/**
	 * TaskVo 리스트를 정렬합니다.
	 */
	private List<TaskVo> sortTaskList(List<TaskVo> taskList, String sortField, String sortOrder) {
		if (taskList == null || taskList.isEmpty()) {
			return taskList;
		}

		System.out.println(
				"🔄 TaskController에서 정렬 시작 - 필드: " + sortField + ", 순서: " + sortOrder + ", 개수: " + taskList.size());

		taskList.sort((a, b) -> {
			String valueA = null;
			String valueB = null;

			// 정렬 필드에 따라 값 추출
			if ("startDate".equals(sortField)) {
				valueA = a.getStartDate();
				valueB = b.getStartDate();
			} else if ("endDate".equals(sortField)) {
				valueA = a.getEndDate();
				valueB = b.getEndDate();
			} else {
				// 기본값으로 startDate 사용
				valueA = a.getStartDate();
				valueB = b.getStartDate();
			}

			// null/empty 처리
			if ((valueA == null || valueA.trim().isEmpty()) && (valueB == null || valueB.trim().isEmpty())) {
				return 0;
			}
			if (valueA == null || valueA.trim().isEmpty()) {
				return 1; // null은 뒤로
			}
			if (valueB == null || valueB.trim().isEmpty()) {
				return -1; // null은 뒤로
			}

			try {
				// 날짜 문자열을 Date 객체로 변환하여 비교
				java.util.Date dateA = parseDate(valueA);
				java.util.Date dateB = parseDate(valueB);

				int comparison = dateA.compareTo(dateB);

				// 내림차순인 경우 결과를 뒤집음
				return "desc".equals(sortOrder) ? -comparison : comparison;

			} catch (Exception e) {
				System.err.println("날짜 비교 중 오류 발생: " + e.getMessage());
				// 문자열 비교로 폴백
				int comparison = valueA.compareTo(valueB);
				return "desc".equals(sortOrder) ? -comparison : comparison;
			}
		});

		System.out.println("✅ TaskController 정렬 완료: " + taskList.size() + "개");
		return taskList;
	}

	/**
	 * 날짜 문자열을 Date 객체로 변환합니다.
	 */
	private java.util.Date parseDate(String dateString) {
		if (dateString == null || dateString.trim().isEmpty()) {
			return new java.util.Date(0); // 기본값
		}

		try {
			// 다양한 날짜 형식 지원
			java.text.SimpleDateFormat[] formats = { new java.text.SimpleDateFormat("yyyy-MM-dd"),
					new java.text.SimpleDateFormat("yyyy/MM/dd"), new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss"),
					new java.text.SimpleDateFormat("yyyy/MM/dd HH:mm:ss") };

			for (java.text.SimpleDateFormat format : formats) {
				try {
					return format.parse(dateString.trim());
				} catch (java.text.ParseException e) {
					// 다음 형식 시도
				}
			}

			// 모든 형식 실패 시 기본값
			System.err.println("날짜 파싱 실패: " + dateString);
			return new java.util.Date(0);

		} catch (Exception e) {
			System.err.println("날짜 파싱 중 예외 발생: " + e.getMessage());
			return new java.util.Date(0);
		}
	}

	/**
	 * 업무(Task) 정보를 등록 처리 한다. 등록 후 관련 프로젝트의 Redis 캐시를 무효화합니다.
	 *
	 * @param taskVo 업무(Task) 정보
	 * @return 생성된 태스크 정보
	 * @throws Exception
	 */
	@ElService(key = "task/create")
	@RequestMapping(value = "task/create")
	@ElDescription(sub = "업무(Task) 정보 등록처리", desc = "업무(Task) 정보를 등록 처리 한다.")
	public TaskVo insertTask(TaskVo taskVo) throws Exception {
		System.out.println("TaskController.insertTask - 요청 데이터: " + taskVo.toString());

		int result = taskService.insertTask(taskVo);

		if (result > 0) {
			System.out.println("TaskController.insertTask - 생성 성공, taskId: " + taskVo.getTaskId());

			// Redis 캐시에 새 태스크 추가 (캐시 무효화 대신 캐시 업데이트)
			addTaskToProjectCacheByBoardId(taskVo.getBoardId(), taskVo, "태스크 등록");

			return taskVo; // 생성된 태스크 정보 반환 (AUTO_INCREMENT로 생성된 taskId 포함)
		} else {
			throw new RuntimeException("태스크 생성에 실패했습니다.");
		}
	}

	/**
	 * 업무(Task) 정보를 갱신 처리 한다.
	 *
	 * @param taskVo 업무(Task) 정보
	 * @throws Exception
	 */
	@ElService(key = "TaskUpd")
	@RequestMapping(value = "TaskUpd")
	@ElValidator(errUrl = "/task/taskRegister", errContinue = true)
	@ElDescription(sub = "업무(Task) 정보 갱신처리", desc = "업무(Task) 정보를 갱신 처리 한다.")
	public void updateTask(TaskVo taskVo) throws Exception {

		taskService.updateTask(taskVo);

		// Redis 캐시 무효화 - 프로젝트의 태스크 정보가 변경되었음
		invalidateProjectCacheByBoardId(taskVo.getBoardId(), "태스크 갱신");
	}

	/**
	 * 업무(Task) 정보와 업무버전(TaskVersion)을 처리함
	 *
	 * @param taskVo 업무(Task) 정보
	 * @throws Exception
	 */
	@ElService(key = "task/update")
	@RequestMapping(value = "task/update")
	@ElDescription(sub = "업무(Task) 정보와 업무버전(TaskVersion)을 처리", desc = "업무(Task) 정보와 업무버전(TaskVersion)을 처리한다")
	public void saveTask(@RequestParam("taskJson") String taskJson, @RequestParam("fileUpload") String fileUploadJson,
			@RequestParam(value = "files", required = false) List<MultipartFile> files) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		TaskUpdateVo updateVo = mapper.readValue(taskJson, TaskUpdateVo.class);
		List<FileSrcVo> fileUpdateList = Arrays.asList(mapper.readValue(fileUploadJson, FileSrcVo[].class));

		// 1. 파일 업로드
		for (int i = 0; i < fileUpdateList.size(); i++) {
			FileSrcVo fileMeta = fileUpdateList.get(i);
			String path = fileMeta.getFilePath();

			if (path == null || path.trim().isEmpty()) {
				MultipartFile file = (files != null && files.size() > i) ? files.get(i) : null;
				String uploadedUrl = uploadS3(file);
				fileMeta.setFilePath(uploadedUrl);
			}
		}

		updateVo.setFileSrcVo(fileUpdateList);

		// 2. HTML 내부 src 교체
		String rawHtml = updateVo.getContent();
		Document doc = Jsoup.parse(rawHtml);
		Elements fileElements = doc.getElementsByClass("file-data");

		for (int i = 0; i < fileElements.size() && i < fileUpdateList.size(); i++) {
			Element el = fileElements.get(fileElements.size() - 1 - i); // 순서 반대로 접근
			String s3Url = fileUpdateList.get(i).getFilePath();
			el.attr("src", s3Url);
		}

		// 3. 치환된 HTML로 반영
		updateVo.setContent(doc.body().html());
		String html = convertLinksWithTitle(updateVo.getContent());
		//updateVo.setContent(html);

		// 4. 기타 처리 및 저장
		taskService.saveTask(updateVo);
	}

	private String uploadS3(MultipartFile file) throws IOException {
		if (file == null || file.isEmpty())
			return null;
		String bucketName = "collabee";
		String originalName = file.getOriginalFilename();
		String s3Key = "taskImage/" + System.currentTimeMillis() + "_" + originalName;

		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentLength(file.getSize());
		metadata.setContentType(file.getContentType());

		amazonS3.putObject(new PutObjectRequest(bucketName, s3Key, file.getInputStream(), metadata));

		return "https://" + bucketName + ".s3.ap-northeast-2.amazonaws.com/" + s3Key;
	}

	public String convertLinksWithTitle(String html) {
		Document doc = Jsoup.parse(html);

		// 텍스트 노드 중 링크 형태를 찾아 처리
		for (Element el : doc.getAllElements()) {
			for (TextNode tn : el.textNodes()) {
				String text = tn.text();
				if (text.matches(".*(www\\.|http).*")) {
					String[] parts = text.split("\\s+");
					for (String part : parts) {
						if (part.matches("(www\\.[^\\s]+)|(https?://[^\\s]+)")) {
							String url = part.startsWith("www.") ? "http://" + part : part;
							try {
								String title = Jsoup.connect(url).get().title();
								Element link = doc.createElement("a");
								link.attr("href", url);
								link.text(title);

								Node parentNode = tn.parent();
								if (parentNode instanceof Element) {
									Element parent = (Element) parentNode;
									tn.remove();
									parent.appendChild(link);
								}

							} catch (Exception e) {
								// 실패 시 원래 텍스트 유지
							}
						}
					}
				}
			}
		}

		return doc.body().html();
	}

	/**
	 * 업무(Task) 정보 목록을 조회합니다.
	 *
	 * @param taskVo 업무(Task) 정보
	 * @return 목록조회 결과
	 * @throws Exception
	 */
	/*
	 * @ElService(key = "TaskList")
	 * 
	 * @RequestMapping(value = "TaskList")
	 * 
	 * @ElDescription(sub = "업무(Task) 정보 목록조회", desc =
	 * "페이징을 처리하여 업무(Task) 정보 목록 조회를 한다.") public TaskListVo selectListTask(TaskVo
	 * taskVo) throws Exception {
	 * 
	 * List<TaskVo> taskList = taskService.selectListTask(taskVo); long totCnt =
	 * taskService.selectListCountTask(taskVo);
	 * 
	 * TaskListVo retTaskList = new TaskListVo();
	 * retTaskList.setTaskVoList(taskList); retTaskList.setTotalCount(totCnt);
	 * retTaskList.setPageSize(taskVo.getPageSize());
	 * retTaskList.setPageIndex(taskVo.getPageIndex());
	 * 
	 * return retTaskList; }
	 */

	/**
	 * 업무(Task) 정보을 단건 조회 처리 한다.
	 *
	 * @param taskVo 업무(Task) 정보
	 * @return 단건 조회 결과
	 * @throws Exception
	 */
	@ElService(key = "task/{taskId}")
	@RequestMapping(value = "task/{taskId}")
	@ElDescription(sub = "업무(Task) 정보 갱신 폼을 위한 조회", desc = "업무(Task) 정보 갱신 폼을 위한 조회를 한다.")
	public TaskUpdateVo selectTask(@PathVariable("taskId") int taskId,
			@RequestParam(value = "projectId", required = false) Integer projectId) throws Exception {
		TaskUpdateVo taskVo = new TaskUpdateVo();
		taskVo.setTaskId(String.valueOf(taskId));
		taskVo.setProjectId(projectId.toString());

		TaskUpdateVo selectTaskVo = taskService.selectTask(taskVo);

		return selectTaskVo;
	}

	/**
	 * 업무(Task) 정보를 갱신 처리 한다.
	 *
	 * @param taskVo 업무(Task) 정보
	 * @throws Exception
	 */
	/*
	 * @ElService(key = "TaskUpd")
	 * 
	 * @RequestMapping(value = "TaskUpd")
	 * 
	 * @ElValidator(errUrl = "/task/taskRegister", errContinue = true)
	 * 
	 * @ElDescription(sub = "업무(Task) 정보 갱신처리", desc = "업무(Task) 정보를 갱신 처리 한다.")
	 * public void updateTask(TaskVo taskVo) throws Exception {
	 * 
	 * taskService.updateTask(taskVo); }
	 */

	/**
	 * 업무(Task) 정보를 삭제 처리한다. 삭제 후 관련 프로젝트의 Redis 캐시를 무효화합니다.
	 *
	 * @param taskVo 업무(Task) 정보
	 * @throws Exception
	 */
	@ElService(key = "task/delete")
	@RequestMapping(value = "task/delete")
	@ElDescription(sub = "업무(Task) 정보 삭제처리", desc = "업무(Task) 정보를 삭제 처리한다.")
	public void deleteTask(TaskVo taskVo) throws Exception {
		taskService.deleteTask(taskVo);

		// Redis 캐시 무효화 - 프로젝트의 태스크 목록이 변경되었음
		invalidateProjectCacheByBoardId(taskVo.getBoardId(), "태스크 삭제");
	}

	/**
	 * 보드 정보를 갱신 처리한다.
	 *
	 * @param boardVo 보드 정보 (WebSquare에서 전달되는 객체)
	 * @throws Exception
	 */
	@ElService(key = "task/updateBoard")
	@RequestMapping(value = "task/updateBoard")
	@ElDescription(sub = "보드 정보 갱신처리", desc = "보드 정보를 갱신 처리한다.")
	public void updateBoard(Object boardData) throws Exception {
		try {
			// Object를 BoardVo로 변환
			BoardVo boardVo = new BoardVo();

			if (boardData instanceof java.util.Map) {
				@SuppressWarnings("unchecked")
				java.util.Map<String, Object> dataMap = (java.util.Map<String, Object>) boardData;

				if (dataMap.get("boardId") != null) {
					boardVo.setBoardId(String.valueOf(dataMap.get("boardId")));
				}
				if (dataMap.get("projectId") != null) {
					boardVo.setProjectId(String.valueOf(dataMap.get("projectId")));
				}
				if (dataMap.get("boardTitle") != null) {
					boardVo.setBoardTitle(String.valueOf(dataMap.get("boardTitle")));
				}
			}

			boardService.updateBoard(boardVo);

			// Redis 캐시 무효화
			if (boardVo.getProjectId() != null) {
				kanbanRedisService.invalidateProjectCache(boardVo.getProjectId());
				System.out.println("🗑️ 보드 갱신으로 인한 프로젝트 캐시 무효화: " + boardVo.getProjectId());
			}

		} catch (Exception e) {
			System.err.println("❌ 보드 갱신 중 오류 발생: " + e.getMessage());
			throw e;
		}
	}

	/**
	 * boardId를 통해 projectId를 찾아 Redis 캐시 무효화
	 */
	private void invalidateProjectCacheByBoardId(String boardId, String action) {
		if (boardId == null) {
			System.out.println("⚠️ boardId가 null이어서 캐시 무효화를 건너뜁니다.");
			return;
		}

		try {
			BoardVo boardVo = new BoardVo();
			boardVo.setBoardId(boardId);
			BoardVo board = boardService.selectBoard(boardVo);

			if (board != null && board.getProjectId() != null) {
				kanbanRedisService.invalidateProjectCache(board.getProjectId());
				System.out.println("🗑️ " + action + "으로 인한 프로젝트 캐시 무효화: " + board.getProjectId());
			} else {
				System.out.println("⚠️ boardId로 프로젝트를 찾을 수 없어 캐시 무효화를 건너뜁니다: " + boardId);
			}
		} catch (Exception e) {
			System.err.println("❌ 캐시 무효화 중 오류 발생: " + e.getMessage());
		}
	}

	/**
	 * boardId를 통해 projectId를 찾아 Redis 캐시에 새 태스크 추가
	 */
	private void addTaskToProjectCacheByBoardId(String boardId, TaskVo taskVo, String action) {
		if (boardId == null) {
			System.out.println("⚠️ boardId가 null이어서 캐시 업데이트를 건너뜁니다.");
			return;
		}

		try {
			BoardVo boardVo = new BoardVo();
			boardVo.setBoardId(boardId);
			BoardVo board = boardService.selectBoard(boardVo);

			if (board != null && board.getProjectId() != null) {
				kanbanRedisService.addTaskToProjectCache(board.getProjectId(), taskVo);
				System.out.println("✅ " + action + "으로 인한 프로젝트 캐시 업데이트: " + board.getProjectId());
			} else {
				System.out.println("⚠️ boardId로 프로젝트를 찾을 수 없어 캐시 업데이트를 건너뜁니다: " + boardId);
			}
		} catch (Exception e) {
			System.err.println("❌ 캐시 업데이트 중 오류 발생: " + e.getMessage());
			// 실패 시 기존 방식으로 대체
			invalidateProjectCacheByBoardId(boardId, action + " (캐시 업데이트 실패로 무효화)");
		}
	}

	/**
	 * 사용자 이름을 포함한 업무(Task) 정보 목록 조회 처리한다.
	 *
	 * @param taskVo 업무(Task) 정보 TaskVo
	 * @return TaskListVo 사용자 이름이 포함된 업무(Task) 정보 목록 TaskListVo
	 * @throws Exception
	 */
	@ElService(key = "TaskListWithUserName")
	@RequestMapping(value = "TaskListWithUserName")
	@ElDescription(sub = "사용자 이름을 포함한 업무(Task) 정보 목록 조회", desc = "조건에 맞는 사용자 이름을 포함한 업무(Task) 정보 목록을 조회한다.")
	public TaskListVo selectTaskListWithUserName(TaskVo taskVo) throws Exception {

		System.out.println("🔍 TaskListWithUserName 호출 - 입력 파라미터: " + taskVo.toString());
		System.out.println("  - boardId: " + taskVo.getBoardId());
		System.out.println("  - projectUserId: " + taskVo.getProjectUserId());
		System.out.println("  - tags: " + taskVo.getTags());

		List<TaskVo> taskVoList = taskService.selectTaskListWithUserName(taskVo);

		System.out.println("📊 TaskListWithUserName 결과 개수: " + (taskVoList != null ? taskVoList.size() : 0));
		if (taskVoList != null && !taskVoList.isEmpty()) {
			System.out.println("📋 조회된 태스크 목록:");
			for (TaskVo task : taskVoList) {
				System.out.println("  - taskId: " + task.getTaskId() + ", boardId: " + task.getBoardId() + ", title: "
						+ task.getTaskTitle() + ", userName: " + task.getUserName());
			}
		} else {
			System.out.println("⚠️ 조회된 태스크가 없습니다. boardId=" + taskVo.getBoardId() + " 조건 확인 필요");
		}

		TaskListVo taskListVo = new TaskListVo();
		taskListVo.setTaskVoList(taskVoList);

		return taskListVo;
	}

}