package com.demo.proworks.user.web;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.demo.proworks.user.service.UserService;
import com.demo.proworks.user.vo.LoginVo;
import com.demo.proworks.user.vo.RecaptchaVo;
import com.demo.proworks.user.vo.UserListVo;
import com.demo.proworks.user.vo.UserVo;
import com.inswave.elfw.annotation.ElDescription;
import com.inswave.elfw.annotation.ElService;
import com.inswave.elfw.log.AppLog;
import com.inswave.elfw.login.LoginInfo;
import com.inswave.elfw.login.LoginProcessor;

/**
 * @subject : 사용자 정보 관련 처리를 담당하는 컨트롤러
 * @description : 사용자 정보 관련 처리를 담당하는 컨트롤러
 * @author : 국다인
 * @since : 2025/06/27
 * @modification ===========================================================
 *               DATE AUTHOR DESC
 *               ===========================================================
 *               2025/06/27 국다인 최초 생성
 * 
 */
@Controller
public class UserController {

	/** UserService */
	@Resource(name = "userServiceImpl")
	private UserService userService;

	@Resource(name = "passwordEncoder")
	private PasswordEncoder passwordEncoder;

	@Resource(name = "loginProcess")
	protected LoginProcessor loginProcess;

	@Resource(name = "amazonS3")
	private AmazonS3 amazonS3;

	@Value("${recaptcha.secret}")
	private String secretKey;

	private static final String VERIFY_URL = "https://www.google.com/recaptcha/api/siteverify";

	/**
	 * 로그인을 처리한다.
	 * 
	 * @param loginVo 로그인 정보 LoginVo
	 * @param request 요청 정보 HttpServletRequest
	 * @throws Exception
	 */
	@ElService(key = "user/login")
	@RequestMapping(value = "user/login")
	@ElDescription(sub = "로그인", desc = "로그인을 처리한다.")
	public void login(LoginVo loginVo, HttpServletRequest request,HttpSession session) throws Exception {
		String id = loginVo.getId();
		String password = loginVo.getPassword();

		LoginInfo info = loginProcess.processLogin(request, id, password);

		session.setAttribute("userId", id);
		System.out.println("==================="+session.getAttribute("userId"));
		if (info != null) {
			AppLog.debug("- Login 정보 : " + info.toString());
		} else {
			AppLog.debug("사용자없음");
		}

	}

	/**
	 * 리캡챠 로그인시에 처리한다.
	 * 
	 * @param loginVo payload LoginVo
	 * @param request 요청 정보 HttpServletRequest
	 * @throws Exception
	 */
	@ElService(key = "user/recaptcha")
	@RequestMapping(value = "user/recaptcha")
	@ElDescription(sub = "리캡챠", desc = "로그인시 봇인지 사림인지에 대해 파악한다")
	public Map<String, Object> verifyRecaptcha(RecaptchaVo recaptchaVo) {

		String token = recaptchaVo.getToken();
		String secret = recaptchaVo.getSecretKey();

		RestTemplate restTemplate = new RestTemplate();

		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("secret", secretKey);
		params.add("response", token);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);

		try {
			ResponseEntity<Map> response = restTemplate.postForEntity(VERIFY_URL, entity, Map.class);
			Boolean success = (Boolean) response.getBody().get("success");

			Map<String, Object> result = new HashMap<>();
			result.put("verified", success);

			return result;

		} catch (Exception e) {
			Map<String, Object> error = new HashMap<>();
			error.put("verified", false);
			error.put("error", "Verification failed");
			return error;
		}
	}

	/**
	 * 사용자 정보 목록을 조회합니다.
	 *
	 * @param userVo 사용자 정보
	 * @return 목록조회 결과
	 * @throws Exception
	 */
	@ElService(key = "user/userList")
	@RequestMapping(value = "user/userList")
	@ElDescription(sub = "사용자 정보 목록조회", desc = "페이징을 처리하여 사용자 정보 목록 조회를 한다.")
	public UserListVo selectListUser(UserVo userVo) throws Exception {

		List<UserVo> userList = userService.selectListUser(userVo);
		long totCnt = userService.selectListCountUser(userVo);

		UserListVo retUserList = new UserListVo();
		retUserList.setUserVoList(userList);
		retUserList.setTotalCount(totCnt);
		retUserList.setPageSize(userVo.getPageSize());
		retUserList.setPageIndex(userVo.getPageIndex());

		return retUserList;
	}

	/**
	 * 사용자 정보을 단건 조회 처리 한다.
	 *
	 * @param userVo 사용자 정보
	 * @return 단건 조회 결과
	 * @throws Exception
	 */
	@ElService(key = "user/checkpassword")
	@RequestMapping(value = "user/checkpassword")
	@ElDescription(sub = "사용자 정보 갱신 폼을 위한 조회", desc = "사용자 정보 갱신 폼을 위한 조회를 한다.")
	public UserVo selectUser(UserVo userVo) throws Exception {
		// 사용자 조회
		UserVo selectUserVo = userService.selectUser(userVo);

		if (selectUserVo == null) {
			throw new IllegalArgumentException("사용자 정보가 존재하지 않습니다.");
		}

		// 비밀번호 비교
		String rawPassword = userVo.getPassword();

		String encodedPassword = selectUserVo.getPassword();

		if (!passwordEncoder.matches(rawPassword, encodedPassword)) {
			throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
		}

		return selectUserVo;
	}

	/**
	 * 사용자 중복 체크
	 *
	 * @param userVo 사용자 정보
	 * @return 사용자 존재 여부 확인
	 * @throws Exception
	 */
	@ElService(key = "user/checkid")
	@RequestMapping(value = "user/checkid")
	@ElDescription(sub = "사용자 이메일 중복 체크", desc = "사용자 회원가입시 이메일 중복 체크")
	public Map<String, Boolean> checkUser(UserVo userVo) throws Exception {
		UserVo selectUserVo = userService.selectUser(userVo);

		Map<String, Boolean> result = new HashMap<>();
		result.put("isDup", selectUserVo != null);
		return result;
	}

	/**
	 * 사용자 정보를 등록 처리 한다.
	 *
	 * @param userVo 사용자 정보
	 * @throws Exception
	 */
	@ElService(key = "user/signup")
	@RequestMapping(value = "user/signup")
	@ElDescription(sub = "사용자 정보 등록처리", desc = "사용자 정보를 등록 처리 한다.")
	public void insertUser(UserVo userVo) throws Exception {
		String rawPassword = userVo.getPassword();
		String encodedPassword = passwordEncoder.encode(rawPassword);
		userVo.setPassword(encodedPassword);
		userService.insertUser(userVo);
	}

	/**
	 * 사용자 비밀번호를 업데이트 한다
	 *
	 * @param userVo 사용자 정보
	 * @throws Exception
	 */
	@ElService(key = "user/updatePassword")
	@RequestMapping(value = "user/updatePassword")
	@ElDescription(sub = "사용자 정보 갱신처리", desc = "사용자 정보를 갱신 처리 한다.")
	public void updatePassword(UserVo userVo) throws Exception {
		String rawPassword = userVo.getPassword();
		String encodedPassword = passwordEncoder.encode(rawPassword);
		userVo.setPassword(encodedPassword);
		userService.updateUser(userVo);
	}

	/**
	 * 사용자 정보를 업데이트 한다
	 *
	 * @param userVo 사용자 정보
	 * @throws Exception
	 */
	@ElService(key = "user/update")
	@RequestMapping(value = "user/update")
	@ElDescription(sub = "FormData 사용자 정보 갱신", desc = "파일 포함 사용자 정보 갱신")
	public void updateUserWithFile(@ModelAttribute UserVo userVo,
			@RequestParam(value = "fileData", required = false) MultipartFile file) throws Exception {
		String bucketName = "collabee";

		// 1. 비밀번호 암호화
		String rawPassword = userVo.getPassword();
		if (rawPassword != null && !rawPassword.isEmpty()) {
			String encodedPassword = passwordEncoder.encode(rawPassword);
			userVo.setPassword(encodedPassword);
		}

		// 2. S3 업로드
//		if (file != null && !file.isEmpty()) {
//			String originalName = file.getOriginalFilename();
//			String s3Key = "userImage/" + System.currentTimeMillis() + "_" + originalName;
//
//			ObjectMetadata metadata = new ObjectMetadata();
//			metadata.setContentLength(file.getSize());
//			metadata.setContentType(file.getContentType());
//
//			amazonS3.putObject(new PutObjectRequest(bucketName, s3Key, file.getInputStream(), metadata));
//
//			String fileUrl = "https://" + bucketName + ".s3.ap-northeast-2.amazonaws.com/" + s3Key;
//			userVo.setProfileImageUrl(fileUrl);
//		}

		userService.updateUser(userVo);
	}

	/**
	 * 사용자 정보를 삭제 처리한다.
	 *
	 * @param userVo 사용자 정보
	 * @throws Exception
	 */
	@ElService(key = "user/delete")
	@RequestMapping(value = "user/delete")
	@ElDescription(sub = "사용자 정보 삭제처리", desc = "사용자 정보를 삭제 처리한다.")
	public void deleteUser(UserVo userVo) throws Exception {
		userService.deleteUser(userVo);

	}

	/**
	 * 세션에서 사용자 정보를 가져온다.
	 *
	 * @param session HttpSession
	 * @return 사용자 정보
	 * @throws Exception
	 */
	@ElService(key = "user/sessionInfo")
	@RequestMapping(value = "user/sessionInfo")
	@ElDescription(sub = "세션 사용자 정보 조회", desc = "세션에서 사용자 정보를 조회한다.")
	public UserVo getUserFromSession(HttpSession session) throws Exception {
		System.out.println(1111111);
		String userId = (String) session.getAttribute("userId");
		
		if (userId == null) {
			throw new IllegalArgumentException("세션에 사용자 정보가 없습니다.");
		}
		
		UserVo userVo = new UserVo();
		userVo.setUserId(userId);
		
		UserVo userInfo = userService.selectUser(userVo);
		
		if (userInfo == null) {
			throw new IllegalArgumentException("사용자 정보를 찾을 수 없습니다.");
		}
		
		return userInfo;
	}

}
