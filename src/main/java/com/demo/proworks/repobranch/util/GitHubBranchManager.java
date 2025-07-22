package com.demo.proworks.repobranch.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.demo.proworks.githubapptoken.service.GithubAppTokenService;
import com.demo.proworks.githubapptoken.vo.GithubAppTokenVo;
import com.demo.proworks.githubapptoken.util.GitHubApiClient;
import com.demo.proworks.userpersonaltoken.service.UserPersonalTokenService;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @subject : GitHub Repository Branch 관리를 위한 유틸리티
 * @description : GitHub API를 통한 브랜치 관리 기능 제공
 * @author : 남기윤
 * @since : 2025/07/07W
 */
@Component
public class GitHubBranchManager {

	private static final String GITHUB_API_BASE_URL = "https://api.github.com";

	@Autowired
	private GithubAppTokenService githubAppTokenService;
	
	@Autowired
	private UserPersonalTokenService userPersonalTokenService;
	
	@Autowired
	private GitHubApiClient gitHubApiClient;

	private RestTemplate restTemplate = new RestTemplate();
	private ObjectMapper objectMapper = new ObjectMapper();

	/**
	 * 레포지토리의 브랜치 목록을 조회한다.
	 * 
	 * @param repoFullName 레포지토리 전체 이름 (owner/repo)
	 * @param accessToken  액세스 토큰
	 * @return 브랜치 목록
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getBranches(String repoFullName, String accessToken) throws Exception {
		String url = GITHUB_API_BASE_URL + "/repos/" + repoFullName + "/branches";

		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "token " + accessToken);
		headers.set("Accept", "application/vnd.github.v3+json");
		headers.set("User-Agent", "ProWorks5-GitHub-Manager");

		HttpEntity<String> entity = new HttpEntity<>(headers);

		ResponseEntity<List> response = restTemplate.exchange(url, HttpMethod.GET, entity, List.class);

		if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
			return response.getBody();
		}

		throw new RuntimeException("브랜치 목록 조회에 실패했습니다.");
	}

	/**
	 * 특정 브랜치의 상세 정보를 조회한다.
	 * 
	 * @param repoFullName 레포지토리 전체 이름
	 * @param branchName   브랜치 이름
	 * @param accessToken  액세스 토큰
	 * @return 브랜치 상세 정보
	 */
	public Map<String, Object> getBranchDetails(String repoFullName, String branchName, String accessToken)
			throws Exception {
		String url = GITHUB_API_BASE_URL + "/repos/" + repoFullName + "/branches/" + branchName;

		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "token " + accessToken);
		headers.set("Accept", "application/vnd.github.v3+json");
		headers.set("User-Agent", "ProWorks5-GitHub-Manager");

		HttpEntity<String> entity = new HttpEntity<>(headers);

		ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);

		if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
			return response.getBody();
		}

		throw new RuntimeException("브랜치 상세 정보 조회에 실패했습니다.");
	}

	/**
	 * 새로운 브랜치를 생성한다. Installation Token 우선 사용, Personal Token 폴백
	 * 
	 * @param repoFullName 레포지토리 전체 이름
	 * @param branchName   새 브랜치 이름
	 * @param sourceBranch 소스 브랜치 이름
	 * @param projectRepoId 프로젝트 레포 ID (Installation Token 조회용)
	 * @param userId 사용자 ID (Personal Token 폴백용)
	 * @return 생성된 브랜치 정보
	 */
	public Map<String, Object> createBranch(String repoFullName, String branchName, String sourceBranch,
			String projectRepoId, String userId) throws Exception {
		
		// Installation Token 우선 사용
		String accessToken = getEffectiveAccessToken(projectRepoId, userId);
		return createBranchWithToken(repoFullName, branchName, sourceBranch, accessToken);
	}
	
	/**
	 * 레거시 메소드 - 기존 호환성 유지
	 * @deprecated Use createBranch(String, String, String, String, String) instead
	 */
	@Deprecated
	public Map<String, Object> createBranch(String repoFullName, String branchName, String sourceBranch,
			String accessToken) throws Exception {
		return createBranchWithToken(repoFullName, branchName, sourceBranch, accessToken);
	}
	
	/**
	 * 실제 브랜치 생성 로직
	 */
	private Map<String, Object> createBranchWithToken(String repoFullName, String branchName, String sourceBranch,
			String accessToken) throws Exception {
		// 1. 소스 브랜치의 SHA 조회
		String sourceSha = getBranchSha(repoFullName, sourceBranch, accessToken);

		// 2. 새 브랜치 생성
		String url = GITHUB_API_BASE_URL + "/repos/" + repoFullName + "/git/refs";

		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "token " + accessToken);
		headers.set("Accept", "application/vnd.github.v3+json");
		headers.set("User-Agent", "ProWorks5-GitHub-Manager");
		headers.set("Content-Type", "application/json");

		Map<String, Object> requestBody = new HashMap<>();
		requestBody.put("ref", "refs/heads/" + branchName);
		requestBody.put("sha", sourceSha);

		String requestBodyJson = objectMapper.writeValueAsString(requestBody);
		HttpEntity<String> entity = new HttpEntity<>(requestBodyJson, headers);

		ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, entity, Map.class);

		if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
			Map<String, Object> result = new HashMap<>();
			result.put("name", branchName);
			result.put("sha", sourceSha);
			result.put("ref", response.getBody().get("ref"));
			result.put("source_branch", sourceBranch);
			return result;
		}

		throw new RuntimeException("브랜치 생성에 실패했습니다.");
	}
	
	/**
	 * 효과적인 액세스 토큰 선택 - Installation Token 우선, Personal Token 폴백
	 * @param projectRepoId 프로젝트 레포 ID
	 * @param userId 사용자 ID
	 * @return 사용할 액세스 토큰
	 */
	private String getEffectiveAccessToken(String projectRepoId, String userId) {
		try {
			// 1. 사용자 ID로 Installation ID 조회 시도
			GithubAppTokenVo appToken = githubAppTokenService.selectGithubAppTokenByUserId(userId);
			if (appToken != null && appToken.getGithubAppInstallationId() != null && !appToken.getGithubAppInstallationId().isEmpty()) {
				// private-key.pem을 사용하여 Installation Token 생성
				try {
					String installationToken = gitHubApiClient.getInstallationToken(appToken.getGithubAppInstallationId());
					System.out.println("✅ Installation Token 생성 성공: " + appToken.getGithubAppInstallationId());
					System.out.println("🔑 생성된 Token (앞 20자): " + installationToken.substring(0, Math.min(20, installationToken.length())) + "...");
					return installationToken;
				} catch (Exception e2) {
					System.out.println("❌ Installation Token 생성 실패: " + e2.getMessage());
					// Installation Token 실패 시 Personal Token으로 폴백
				}
			}
		} catch (Exception e) {
			System.out.println("Installation ID 조회 실패, Personal Token으로 폴백: " + e.getMessage());
		}
			
		try {
			// 2. Personal Token 폴백
			String personalToken = userPersonalTokenService.getToken(userId);
			if (personalToken != null && !personalToken.isEmpty()) {
				System.out.println("Personal Token 폴백 사용");
				return personalToken;
			}
		} catch (Exception e) {
			System.out.println("Personal Token 조회도 실패: " + e.getMessage());
		}
		
		throw new RuntimeException("사용 가능한 GitHub 토큰이 없습니다. GitHub App 설치 또는 Personal Token 설정이 필요합니다.");
	}
	

	/**
	 * 브랜치를 삭제한다.
	 * 
	 * @param repoFullName 레포지토리 전체 이름
	 * @param branchName   삭제할 브랜치 이름
	 * @param accessToken  액세스 토큰
	 * @return 삭제 성공 여부
	 */
	public boolean deleteBranch(String repoFullName, String branchName, String accessToken) throws Exception {
		String url = GITHUB_API_BASE_URL + "/repos/" + repoFullName + "/git/refs/heads/" + branchName;

		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "token " + accessToken);
		headers.set("Accept", "application/vnd.github.v3+json");
		headers.set("User-Agent", "ProWorks5-GitHub-Manager");

		HttpEntity<String> entity = new HttpEntity<>(headers);

		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.DELETE, entity, String.class);

		return response.getStatusCode().is2xxSuccessful();
	}

	/**
	 * 브랜치의 SHA를 조회한다.
	 * 
	 * @param repoFullName 레포지토리 전체 이름
	 * @param branchName   브랜치 이름
	 * @param accessToken  액세스 토큰
	 * @return 브랜치 SHA
	 */
	private String getBranchSha(String repoFullName, String branchName, String accessToken) throws Exception {
		String url = GITHUB_API_BASE_URL + "/repos/" + repoFullName + "/git/refs/heads/" + branchName;

		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "token " + accessToken);
		headers.set("Accept", "application/vnd.github.v3+json");
		headers.set("User-Agent", "ProWorks5-GitHub-Manager");

		HttpEntity<String> entity = new HttpEntity<>(headers);

		ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);

		if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
			Map<String, Object> responseBody = response.getBody();
			Map<String, Object> object = (Map<String, Object>) responseBody.get("object");
			if (object != null) {
				return (String) object.get("sha");
			}
		}

		throw new RuntimeException("브랜치 SHA 조회에 실패했습니다: " + branchName);
	}

	/**
	 * Installation Token을 사용하여 브랜치를 생성한다.
	 * 
	 * @param repoFullName    레포지토리 전체 이름
	 * @param branchName      새 브랜치 이름
	 * @param sourceBranch    소스 브랜치 이름
	 * @param userAccessToken 사용자 액세스 토큰
	 * @param projectRepoId   프로젝트 레포지토리 ID
	 * @return 생성된 브랜치 정보
	 */
	public Map<String, Object> createBranchWithAppToken(String repoFullName, String branchName, String sourceBranch,
			String userAccessToken, String projectRepoId) throws Exception {

		try {
			// Installation Token 시도
			String repoOwner = repoFullName.split("/")[0];
			String installationToken = githubAppTokenService.generateInstallationToken(userAccessToken, repoOwner);

			return createBranch(repoFullName, branchName, sourceBranch, installationToken);

		} catch (Exception e) {
			System.out.println("⚠️ Installation Token 실패, OAuth 토큰으로 재시도: " + e.getMessage());

			// OAuth 토큰으로 재시도
			return createBranch(repoFullName, branchName, sourceBranch, userAccessToken);
		}
	}

	/**
	 * Installation Token을 사용하여 브랜치를 삭제한다.
	 * 
	 * @param repoFullName    레포지토리 전체 이름
	 * @param branchName      삭제할 브랜치 이름
	 * @param userAccessToken 사용자 액세스 토큰
	 * @param projectRepoId   프로젝트 레포지토리 ID
	 * @return 삭제 성공 여부
	 */
	public boolean deleteBranchWithAppToken(String repoFullName, String branchName, String userAccessToken,
			String projectRepoId) throws Exception {

		try {
			// Installation Token 시도
			String repoOwner = repoFullName.split("/")[0];
			String installationToken = githubAppTokenService.generateInstallationToken(userAccessToken, repoOwner);

			return deleteBranch(repoFullName, branchName, installationToken);

		} catch (Exception e) {
			System.out.println("⚠️ Installation Token 실패, OAuth 토큰으로 재시도: " + e.getMessage());

			// OAuth 토큰으로 재시도
			return deleteBranch(repoFullName, branchName, userAccessToken);
		}
	}

	/**
	 * 레포지토리 기본 정보를 조회한다.
	 * 
	 * @param repoFullName 레포지토리 전체 이름 (owner/repo)
	 * @param accessToken  액세스 토큰
	 * @return 레포지토리 정보
	 */
	public Map<String, Object> getRepositoryInfo(String repoFullName, String accessToken) throws Exception {
		String url = GITHUB_API_BASE_URL + "/repos/" + repoFullName;

		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "token " + accessToken);
		headers.set("Accept", "application/vnd.github.v3+json");
		headers.set("User-Agent", "ProWorks5-GitHub-Manager");

		HttpEntity<String> entity = new HttpEntity<>(headers);

		ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);

		if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
			Map<String, Object> repoData = response.getBody();

			// 필요한 정보만 추출하여 반환
			Map<String, Object> result = new HashMap<>();
			result.put("id", repoData.get("id"));
			result.put("name", repoData.get("name"));
			result.put("full_name", repoData.get("full_name"));
			result.put("description", repoData.get("description"));
			result.put("default_branch", repoData.get("default_branch"));
			result.put("private", repoData.get("private"));
			result.put("html_url", repoData.get("html_url"));
			result.put("clone_url", repoData.get("clone_url"));
			result.put("ssh_url", repoData.get("ssh_url"));
			result.put("language", repoData.get("language"));
			result.put("created_at", repoData.get("created_at"));
			result.put("updated_at", repoData.get("updated_at"));

			// 소유자 정보
			Map<String, Object> owner = (Map<String, Object>) repoData.get("owner");
			if (owner != null) {
				Map<String, Object> ownerInfo = new HashMap<>();
				ownerInfo.put("login", owner.get("login"));
				ownerInfo.put("type", owner.get("type"));
				ownerInfo.put("avatar_url", owner.get("avatar_url"));
				result.put("owner", ownerInfo);
			}

			return result;
		}

		throw new RuntimeException("레포지토리 정보 조회에 실패했습니다.");
	}

	/**
	 * Installation Token을 사용하여 레포지토리 정보를 조회한다.
	 * 
	 * @param repoFullName    레포지토리 전체 이름
	 * @param userAccessToken 사용자 액세스 토큰
	 * @param projectRepoId   프로젝트 레포지토리 ID
	 * @return 레포지토리 정보
	 */
	public Map<String, Object> getRepositoryInfoWithAppToken(String repoFullName, String userAccessToken,
			String projectRepoId) throws Exception {

		try {
			// Installation Token 시도
			String repoOwner = repoFullName.split("/")[0];
			String installationToken = githubAppTokenService.generateInstallationToken(userAccessToken, repoOwner);

			return getRepositoryInfo(repoFullName, installationToken);

		} catch (Exception e) {
			System.out.println("⚠️ Installation Token 실패, OAuth 토큰으로 재시도: " + e.getMessage());

			// OAuth 토큰으로 재시도
			return getRepositoryInfo(repoFullName, userAccessToken);
		}
	}

	/**
	 * 레포지토리의 최근 커밋 정보를 조회한다.
	 * 
	 * @param repoFullName 레포지토리 전체 이름
	 * @param branchName   브랜치 이름 (null이면 기본 브랜치)
	 * @param accessToken  액세스 토큰
	 * @param limit        조회할 커밋 개수 (기본값: 10)
	 * @return 커밋 목록
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getRecentCommits(String repoFullName, String branchName, String accessToken,
			Integer limit) throws Exception {

		String url = GITHUB_API_BASE_URL + "/repos/" + repoFullName + "/commits";

		// 쿼리 파라미터 추가
		if (branchName != null && !branchName.trim().isEmpty()) {
			url += "?sha=" + branchName;
		}

		if (limit != null && limit > 0) {
			String separator = url.contains("?") ? "&" : "?";
			url += separator + "per_page=" + Math.min(limit, 100); // GitHub API 제한: 최대 100개
		}

		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "token " + accessToken);
		headers.set("Accept", "application/vnd.github.v3+json");
		headers.set("User-Agent", "ProWorks5-GitHub-Manager");

		HttpEntity<String> entity = new HttpEntity<>(headers);

		ResponseEntity<List> response = restTemplate.exchange(url, HttpMethod.GET, entity, List.class);

		if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
			List<Map<String, Object>> commits = response.getBody();

			// 필요한 정보만 추출하여 반환
			return commits.stream().map(commit -> {
				Map<String, Object> result = new HashMap<>();
				result.put("sha", commit.get("sha"));
				result.put("html_url", commit.get("html_url"));

				Map<String, Object> commitData = (Map<String, Object>) commit.get("commit");
				if (commitData != null) {
					result.put("message", commitData.get("message"));

					Map<String, Object> author = (Map<String, Object>) commitData.get("author");
					if (author != null) {
						result.put("author_name", author.get("name"));
						result.put("author_email", author.get("email"));
						result.put("author_date", author.get("date"));
					}
				}

				Map<String, Object> authorInfo = (Map<String, Object>) commit.get("author");
				if (authorInfo != null) {
					result.put("author_login", authorInfo.get("login"));
					result.put("author_avatar_url", authorInfo.get("avatar_url"));
				}

				return result;
			}).collect(java.util.stream.Collectors.toList());
		}

		throw new RuntimeException("커밋 목록 조회에 실패했습니다.");
	}

	/**
	 * 브랜치 보호 규칙을 조회한다.
	 * 
	 * @param repoFullName 레포지토리 전체 이름
	 * @param branchName   브랜치 이름
	 * @param accessToken  액세스 토큰
	 * @return 브랜치 보호 정보
	 */
	public Map<String, Object> getBranchProtection(String repoFullName, String branchName, String accessToken)
			throws Exception {
		String url = GITHUB_API_BASE_URL + "/repos/" + repoFullName + "/branches/" + branchName + "/protection";

		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "token " + accessToken);
		headers.set("Accept", "application/vnd.github.v3+json");
		headers.set("User-Agent", "ProWorks5-GitHub-Manager");

		HttpEntity<String> entity = new HttpEntity<>(headers);

		try {
			ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);

			if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
				return response.getBody();
			}
		} catch (Exception e) {
			// 브랜치 보호가 설정되지 않은 경우
			if (e.getMessage().contains("404")) {
				Map<String, Object> result = new HashMap<>();
				result.put("protected", false);
				result.put("message", "브랜치 보호 규칙이 설정되지 않았습니다.");
				return result;
			}
			throw e;
		}

		throw new RuntimeException("브랜치 보호 정보 조회에 실패했습니다.");
	}
}
