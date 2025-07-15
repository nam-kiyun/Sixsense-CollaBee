package com.demo.proworks.githubwebhook.service.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.demo.proworks.githubapptoken.service.GithubAppTokenService;
import com.demo.proworks.githubapptoken.util.GitHubApiClient;
import com.demo.proworks.githubwebhook.dao.GithubWebhookDAO;
import com.demo.proworks.githubwebhook.service.GithubWebhookService;
import com.demo.proworks.githubwebhook.vo.GithubWebhookVo;
import com.demo.proworks.projectrepo.service.ProjectRepositoryService;
import com.demo.proworks.projectrepo.vo.ProjectRepositoryVo;
import com.demo.proworks.repobranch.service.RepositoryBranchService;
import com.demo.proworks.repobranch.vo.RepositoryBranchVo;
import com.fasterxml.jackson.databind.ObjectMapper;

/**  
 * @subject     : 깃허브 웹훅 관련 처리를 담당하는 ServiceImpl
 * @description	: 깃허브 웹훅 관련 처리를 담당하는 ServiceImpl
 * @author      : 남기윤
 * @since       : 2025/07/01
 * @modification
 * ===========================================================
 * DATE				AUTHOR				DESC
 * ===========================================================
 * 2025/07/01			 남기윤	 		최초 생성
 * 
 */
@Service("githubWebhookServiceImpl")
public class GithubWebhookServiceImpl implements GithubWebhookService {

    @Resource(name="githubWebhookDAO")
    private GithubWebhookDAO githubWebhookDAO;
	
	@Resource(name = "messageSource")
	private MessageSource messageSource;
	
	@Autowired
	private GithubAppTokenService githubAppTokenService;
	
	@Autowired
	private GitHubApiClient gitHubApiClient;
	
	@Autowired
	private ProjectRepositoryService projectRepositoryService;
	
	@Autowired
	private RepositoryBranchService repositoryBranchService;
	
	@Value("${webhook.url:https://smee.io/aYKJH3maBXQp0hDE}")
	private String defaultWebhookUrl;
	
	private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	private ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 깃허브 웹훅 목록을 조회합니다.
     *
     * @process
     * 1. 깃허브 웹훅 페이징 처리하여 목록을 조회한다.
     * 2. 결과 List<GithubWebhookVo>을(를) 리턴한다.
     * 
     * @param  githubWebhookVo 깃허브 웹훅 GithubWebhookVo
     * @return 깃허브 웹훅 목록 List<GithubWebhookVo>
     * @throws Exception
     */
	public List<GithubWebhookVo> selectListGithubWebhook(GithubWebhookVo githubWebhookVo) throws Exception {
		List<GithubWebhookVo> list = githubWebhookDAO.selectListGithubWebhook(githubWebhookVo);	
	
		return list;
	}

    /**
     * 조회한 깃허브 웹훅 전체 카운트
     *
     * @process
     * 1. 깃허브 웹훅 조회하여 전체 카운트를 리턴한다.
     * 
     * @param  githubWebhookVo 깃허브 웹훅 GithubWebhookVo
     * @return 깃허브 웹훅 목록 전체 카운트
     * @throws Exception
     */
	public long selectListCountGithubWebhook(GithubWebhookVo githubWebhookVo) throws Exception {
		return githubWebhookDAO.selectListCountGithubWebhook(githubWebhookVo);
	}

    /**
     * 깃허브 웹훅를 상세 조회한다.
     *
     * @process
     * 1. 깃허브 웹훅를 상세 조회한다.
     * 2. 결과 GithubWebhookVo을(를) 리턴한다.
     * 
     * @param  githubWebhookVo 깃허브 웹훅 GithubWebhookVo
     * @return 단건 조회 결과
     * @throws Exception
     */
	public GithubWebhookVo selectGithubWebhook(GithubWebhookVo githubWebhookVo) throws Exception {
		GithubWebhookVo resultVO = githubWebhookDAO.selectGithubWebhook(githubWebhookVo);			
        
        return resultVO;
	}

    /**
     * 깃허브 웹훅를 등록 처리 한다.
     *
     * @process
     * 1. 깃허브 웹훅를 등록 처리 한다.
     * 
     * @param  githubWebhookVo 깃허브 웹훅 GithubWebhookVo
     * @return 번호
     * @throws Exception
     */
	public int insertGithubWebhook(GithubWebhookVo githubWebhookVo) throws Exception {
		return githubWebhookDAO.insertGithubWebhook(githubWebhookVo);	
	}
	
    /**
     * 깃허브 웹훅를 갱신 처리 한다.
     *
     * @process
     * 1. 깃허브 웹훅를 갱신 처리 한다.
     * 
     * @param  githubWebhookVo 깃허브 웹훅 GithubWebhookVo
     * @return 번호
     * @throws Exception
     */
	public int updateGithubWebhook(GithubWebhookVo githubWebhookVo) throws Exception {				
		return githubWebhookDAO.updateGithubWebhook(githubWebhookVo);	   		
	}

    /**
     * 깃허브 웹훅를 삭제 처리 한다.
     *
     * @process
     * 1. 깃허브 웹훅를 삭제 처리 한다.
     * 
     * @param  githubWebhookVo 깃허브 웹훅 GithubWebhookVo
     * @return 번호
     * @throws Exception
     */
	public int deleteGithubWebhook(GithubWebhookVo githubWebhookVo) throws Exception {
		return githubWebhookDAO.deleteGithubWebhook(githubWebhookVo);
	}
	
	/**
     * GitHub API를 통해 웹훅을 생성한다.
     */
	@Override
	@Transactional
	public GithubWebhookVo createWebhookViaAPI(String projectRepoId, String repoFullName, String webhookUrl, String userAccessToken) throws Exception {
		try {
			// 이미 웹훅이 존재하는지 확인
			GithubWebhookVo existingWebhook = getWebhookByProjectRepoId(projectRepoId);
			if (existingWebhook != null) {
				throw new Exception("웹훅이 이미 존재합니다.");
			}
			
			// 웹훅 설정
			Map<String, Object> webhookConfig = new HashMap<>();
			webhookConfig.put("name", "web");
			webhookConfig.put("active", true);
			webhookConfig.put("events", Arrays.asList("push", "pull_request", "create", "delete", "repository"));
			
			Map<String, Object> config = new HashMap<>();
			config.put("url", webhookUrl != null ? webhookUrl : defaultWebhookUrl);
			config.put("content_type", "json");
			config.put("insecure_ssl", "0");
			webhookConfig.put("config", config);
			
			// Installation Token 시도
			String installationToken = null;
			try {
				String repoOwner = repoFullName.split("/")[0];
				installationToken = githubAppTokenService.generateInstallationToken(userAccessToken, repoOwner);
			} catch (Exception e) {
				System.out.println("⚠️ Installation Token 생성 실패, OAuth 토큰 사용: " + e.getMessage());
			}
			
			// GitHub API로 웹훅 생성
			String authToken = installationToken != null ? installationToken : userAccessToken;
			Map<String, Object> webhookResponse = gitHubApiClient.createWebhook(authToken, repoFullName, webhookConfig);
			
			// DB에 웹훅 정보 저장
			GithubWebhookVo webhookVo = new GithubWebhookVo();
			webhookVo.setGithubWebhookId(generateWebhookId());
			webhookVo.setProjectRepoId(projectRepoId);
			webhookVo.setHookId(webhookResponse.get("id").toString());
			webhookVo.setEvents(objectMapper.writeValueAsString(webhookResponse.get("events")));
			webhookVo.setConfigUrl(((Map<String, Object>)webhookResponse.get("config")).get("url").toString());
			webhookVo.setCreatedAt(LocalDateTime.now().format(DATE_FORMATTER));
			
			githubWebhookDAO.insertGithubWebhook(webhookVo);
			
			System.out.println("✅ 웹훅 생성 완료: " + webhookVo.getHookId());
			return webhookVo;
			
		} catch (Exception e) {
			System.err.println("❌ 웹훅 생성 실패: " + e.getMessage());
			throw new Exception("웹훅 생성 실패: " + e.getMessage());
		}
	}
	
	/**
     * 웹훅 이벤트를 처리한다.
     */
	@Override
	public String processWebhookEvent(String eventType, String payload, String signature) throws Exception {
		try {
			System.out.println("🔔 웹훅 이벤트 수신: " + eventType);
			
			// 페이로드 파싱
			Map<String, Object> payloadMap = objectMapper.readValue(payload, Map.class);
			
			// 레포지토리 정보 추출
			Map<String, Object> repository = (Map<String, Object>) payloadMap.get("repository");
			if (repository == null) {
				return "Repository 정보가 없습니다.";
			}
			
			String repoFullName = (String) repository.get("full_name");
			System.out.println("📍 Repository: " + repoFullName);
			
			// 이벤트 타입별 처리
			switch (eventType) {
				case "push":
					return handlePushEvent(payloadMap);
				case "pull_request":
					return handlePullRequestEvent(payloadMap);
				case "create":
					return handleCreateEvent(payloadMap);
				case "delete":
					return handleDeleteEvent(payloadMap);
				case "repository":
					return handleRepositoryEvent(payloadMap);
				case "ping":
					System.out.println("🏓 Ping 이벤트 수신 - 웹훅이 정상 작동 중입니다!");
					return "Pong! 웹훅이 정상 작동 중입니다.";
				default:
					System.out.println("🤷 처리되지 않은 이벤트 타입: " + eventType);
					return "처리되지 않은 이벤트 타입: " + eventType;
			}
			
		} catch (Exception e) {
			System.err.println("❌ 웹훅 이벤트 처리 실패: " + e.getMessage());
			throw new Exception("웹훅 이벤트 처리 실패: " + e.getMessage());
		}
	}
	
	/**
     * 웹훅 상태를 확인한다.
     */
	@Override
	public Map<String, Object> getWebhookStatus(String projectRepoId, String userAccessToken) throws Exception {
		try {
			Map<String, Object> status = new HashMap<>();
			
			// DB에서 웹훅 정보 조회
			GithubWebhookVo webhook = getWebhookByProjectRepoId(projectRepoId);
			if (webhook == null) {
				status.put("exists", false);
				status.put("message", "웹훅이 설정되지 않았습니다.");
				return status;
			}
			
			// 프로젝트 레포지토리 정보 조회
			ProjectRepositoryVo repoSearchVo = new ProjectRepositoryVo();
			repoSearchVo.setProjectRepoId(projectRepoId);
			ProjectRepositoryVo repoInfo = projectRepositoryService.selectProjectRepository(repoSearchVo);
			
			if (repoInfo == null) {
				status.put("exists", false);
				status.put("message", "프로젝트 레포지토리 정보를 찾을 수 없습니다.");
				return status;
			}
			
			// 수동 웹훅인지 확인
			if (webhook.getHookId() != null && webhook.getHookId().startsWith("manual_")) {
				status.put("exists", true);
				status.put("manual", true);
				status.put("webhook", createWebhookStatusMap(webhook, true));
				status.put("note", "수동으로 설정된 웹훅입니다. GitHub API 상태 확인이 불가능합니다.");
				return status;
			}
			
			// GitHub에서 웹훅 상태 확인
			try {
				String repoFullName = repoInfo.getRepoOwner() + "/" + repoInfo.getRepoName();
				
				// Installation Token 시도
				String authToken = userAccessToken;
				try {
					String installationToken = githubAppTokenService.generateInstallationToken(userAccessToken, repoInfo.getRepoOwner());
					if (installationToken != null) {
						authToken = installationToken;
					}
				} catch (Exception e) {
					System.out.println("⚠️ Installation Token 생성 실패, OAuth 토큰 사용: " + e.getMessage());
				}
				
				org.springframework.web.client.RestTemplate restTemplate = new org.springframework.web.client.RestTemplate();
				org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
				headers.set("Authorization", "token " + authToken);
				headers.set("Accept", "application/vnd.github.v3+json");
				headers.set("User-Agent", "ProWorks5-GitHub-Manager");
				
				org.springframework.http.HttpEntity<String> entity = new org.springframework.http.HttpEntity<>(headers);
				
				String getUrl = "https://api.github.com/repos/" + repoFullName + "/hooks/" + webhook.getHookId();
				org.springframework.http.ResponseEntity<Map> response = restTemplate.exchange(getUrl, org.springframework.http.HttpMethod.GET, entity, Map.class);
				
				if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
					Map<String, Object> githubWebhook = response.getBody();
					
					status.put("exists", true);
					status.put("webhook", createWebhookStatusMapFromGitHub(webhook, githubWebhook));
				} else {
					status.put("exists", false);
					status.put("message", "GitHub에서 웹훅 정보를 가져올 수 없습니다.");
				}
				
			} catch (org.springframework.web.client.HttpClientErrorException e) {
				if (e.getStatusCode().value() == 404) {
					status.put("exists", false);
					status.put("message", "웹훅이 DB에는 있지만 GitHub에는 존재하지 않습니다.");
					status.put("local_webhook", createWebhookStatusMap(webhook, false));
				} else {
					throw e;
				}
			}
			
			return status;
			
		} catch (Exception e) {
			System.err.println("❌ 웹훅 상태 확인 실패: " + e.getMessage());
			throw new Exception("웹훅 상태 확인 실패: " + e.getMessage());
		}
	}
	
	// 헬퍼 메서드들
	
	private String handlePushEvent(Map<String, Object> payload) {
		Map<String, Object> repository = (Map<String, Object>) payload.get("repository");
		String ref = (String) payload.get("ref");
		Map<String, Object> pusher = (Map<String, Object>) payload.get("pusher");
		List<Map<String, Object>> commits = (List<Map<String, Object>>) payload.get("commits");
		
		String branchName = ref != null ? ref.replace("refs/heads/", "") : "unknown";
		String pusherName = pusher != null ? (String) pusher.get("name") : "unknown";
		int commitCount = commits != null ? commits.size() : 0;
		
		System.out.println("📦 Push to " + repository.get("full_name"));
		System.out.println("🌿 Branch: " + branchName);
		System.out.println("👤 Pusher: " + pusherName);
		System.out.println("💾 Commits: " + commitCount);
		
		return "Push 이벤트 처리 완료: " + branchName + " 브랜치에 " + commitCount + "개 커밋";
	}
	
	private String handlePullRequestEvent(Map<String, Object> payload) {
		String action = (String) payload.get("action");
		Map<String, Object> pullRequest = (Map<String, Object>) payload.get("pull_request");
		
		if (pullRequest != null) {
			String title = (String) pullRequest.get("title");
			Map<String, Object> head = (Map<String, Object>) pullRequest.get("head");
			Map<String, Object> base = (Map<String, Object>) pullRequest.get("base");
			Map<String, Object> user = (Map<String, Object>) pullRequest.get("user");
			
			String headRef = head != null ? (String) head.get("ref") : "unknown";
			String baseRef = base != null ? (String) base.get("ref") : "unknown";
			String userName = user != null ? (String) user.get("login") : "unknown";
			
			System.out.println("🔄 Pull Request " + action + ": " + title);
			System.out.println("📍 " + headRef + " → " + baseRef);
			System.out.println("👤 Author: " + userName);
		}
		
		return "Pull Request 이벤트 처리 완료: " + action;
	}
	
	private String handleCreateEvent(Map<String, Object> payload) {
		String refType = (String) payload.get("ref_type");
		String ref = (String) payload.get("ref");
		Map<String, Object> sender = (Map<String, Object>) payload.get("sender");
		
		String senderLogin = sender != null ? (String) sender.get("login") : "unknown";
		
		System.out.println("🆕 Created " + refType + ": " + ref);
		System.out.println("👤 Creator: " + senderLogin);
		
		return "Create 이벤트 처리 완료: " + refType + " " + ref + " 생성";
	}
	
	private String handleDeleteEvent(Map<String, Object> payload) {
		String refType = (String) payload.get("ref_type");
		String ref = (String) payload.get("ref");
		Map<String, Object> sender = (Map<String, Object>) payload.get("sender");
		Map<String, Object> repository = (Map<String, Object>) payload.get("repository");
		
		String senderLogin = sender != null ? (String) sender.get("login") : "unknown";
		String repoFullName = repository != null ? (String) repository.get("full_name") : "unknown";
		
		System.out.println("🗑️ Deleted " + refType + ": " + ref);
		System.out.println("👤 Deleter: " + senderLogin);
		System.out.println("📁 Repository: " + repoFullName);
		
		// 브랜치 삭제 이벤트인 경우 DB에서 삭제
		if ("branch".equals(refType)) {
			try {
				deleteBranchFromDatabase(repoFullName, ref);
				System.out.println("✅ DB에서 브랜치 삭제 완료: " + ref);
			} catch (Exception e) {
				System.out.println("❌ DB 브랜치 삭제 실패: " + e.getMessage());
				e.printStackTrace();
			}
		}
		
		return "Delete 이벤트 처리 완료: " + refType + " " + ref + " 삭제";
	}
	
	/**
	 * 데이터베이스에서 브랜치 정보를 삭제하는 메서드
	 * 
	 * @param repoFullName 저장소 전체 이름 (예: nam-kiyun/hello-world)
	 * @param branchName 삭제할 브랜치 이름
	 * @throws Exception
	 */
	private void deleteBranchFromDatabase(String repoFullName, String branchName) throws Exception {
		// 저장소 전체 이름에서 소유자와 저장소 이름 분리
		String[] parts = repoFullName.split("/");
		if (parts.length != 2) {
			throw new Exception("Invalid repository full name format: " + repoFullName);
		}
		
		String repoOwner = parts[0];
		String repoName = parts[1];
		
		// 프로젝트 저장소 정보 조회
		ProjectRepositoryVo projectRepoVo = new ProjectRepositoryVo();
		projectRepoVo.setRepoOwner(repoOwner);
		projectRepoVo.setRepoName(repoName);
		
		ProjectRepositoryVo existingRepo = projectRepositoryService.selectProjectRepositoryByOwnerAndName(projectRepoVo);
		if (existingRepo == null) {
			throw new Exception("Repository not found in database: " + repoFullName);
		}
		
		// 브랜치 삭제
		RepositoryBranchVo branchVo = new RepositoryBranchVo();
		branchVo.setProjectRepoId(existingRepo.getProjectRepoId());
		branchVo.setBranchName(branchName);
		
		int deletedRows = repositoryBranchService.deleteRepositoryBranchByProjectRepoIdAndBranchName(branchVo);
		
		if (deletedRows > 0) {
			System.out.println("✅ 브랜치 삭제 성공: " + branchName + " (project_repo_id: " + existingRepo.getProjectRepoId() + ")");
		} else {
			System.out.println("⚠️ 삭제할 브랜치를 찾을 수 없음: " + branchName);
		}
	}
	
	private String handleRepositoryEvent(Map<String, Object> payload) {
		String action = (String) payload.get("action");
		Map<String, Object> repository = (Map<String, Object>) payload.get("repository");
		
		String repoFullName = repository != null ? (String) repository.get("full_name") : "unknown";
		
		System.out.println("🏠 Repository " + action + ": " + repoFullName);
		
		return "Repository 이벤트 처리 완료: " + action;
	}
	
	private Map<String, Object> createWebhookStatusMap(GithubWebhookVo webhook, boolean active) {
		Map<String, Object> webhookMap = new HashMap<>();
		webhookMap.put("id", webhook.getGithubWebhookId());
		webhookMap.put("github_id", webhook.getHookId());
		webhookMap.put("url", webhook.getConfigUrl());
		webhookMap.put("events", webhook.getEvents());
		webhookMap.put("active", active);
		webhookMap.put("created_at", webhook.getCreatedAt());
		return webhookMap;
	}
	
	private Map<String, Object> createWebhookStatusMapFromGitHub(GithubWebhookVo webhook, Map<String, Object> githubWebhook) {
		Map<String, Object> webhookMap = new HashMap<>();
		webhookMap.put("id", webhook.getGithubWebhookId());
		webhookMap.put("github_id", githubWebhook.get("id"));
		
		Map<String, Object> config = (Map<String, Object>) githubWebhook.get("config");
		webhookMap.put("url", config != null ? config.get("url") : webhook.getConfigUrl());
		webhookMap.put("events", githubWebhook.get("events"));
		webhookMap.put("active", githubWebhook.get("active"));
		webhookMap.put("created_at", webhook.getCreatedAt());
		webhookMap.put("last_response", githubWebhook.get("last_response"));
		return webhookMap;
	}
	
	private String generateWebhookId() {
		return "GWH_" + System.currentTimeMillis();
	}
	
	/**
     * GitHub API를 통해 웹훅을 삭제한다.
     */
    @Override
    public boolean deleteWebhookViaAPI(String projectRepoId, String userAccessToken) throws Exception {
        // TODO: 여기에 projectRepoId와 userAccessToken을 사용하여
        // GitHub API를 호출하고 웹훅을 삭제하는 로직을 구현해야 합니다.
        // 예를 들어, repoFullName과 hookId를 찾고 GitHubApiClient를 호출합니다.
        System.out.println("deleteWebhookViaAPI 메소드 구현이 필요합니다.");
        return false; // 임시 반환값
    }

    /**
     * 프로젝트 레포지토리 ID로 웹훅을 조회한다.
     */
    @Override
    public GithubWebhookVo getWebhookByProjectRepoId(String projectRepoId) throws Exception {
        // TODO: 여기에 projectRepoId를 사용하여
        // DB에서 웹훅 정보를 조회하는 로직을 구현해야 합니다.
        // GithubWebhookVo vo = new GithubWebhookVo();
        // vo.setProjectRepoId(projectRepoId);
        // return githubWebhookDAO.selectGithubWebhook(vo); 와 같은 형태가 될 수 있습니다.
        System.out.println("getWebhookByProjectRepoId 메소드 구현이 필요합니다.");
        return null; // 임시 반환값
    }
	
}
