package com.demo.proworks.user.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.http.HttpStatus;

import com.demo.proworks.user.service.UserService;
import com.demo.proworks.user.vo.UserVo;
import com.demo.proworks.user.vo.LoginVo;
import com.demo.proworks.user.vo.RecaptchaVo;
import com.demo.proworks.user.vo.UserListVo;

import com.inswave.elfw.annotation.ElDescription;
import com.inswave.elfw.annotation.ElService;
import com.inswave.elfw.annotation.ElValidator;
import com.inswave.elfw.log.AppLog;
import com.inswave.elfw.login.LoginInfo;
import com.inswave.elfw.login.LoginProcessor;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;

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

	private static final String VERIFY_URL = "https://www.google.com/recaptcha/api/siteverify";

	/**
	 * 로그인을 처리한다.
	 * 
	 * @param loginVo 로그인 정보 LoginVo
	 * @param request 요청 정보 HttpServletRequest
	 * @throws Exception
	 */
	@ElService(key = "UserLogin")
	@RequestMapping(value = "UserLogin")
	@ElDescription(sub = "로그인", desc = "로그인을 처리한다.")
	public void login(LoginVo loginVo, HttpServletRequest request) throws Exception {
		String id = loginVo.getId();
		String password = loginVo.getPassword();

		LoginInfo info = loginProcess.processLogin(request, id, password);

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
	@ElService(key = "verifyRecaptcha")
	@RequestMapping(value = "verifyRecaptcha")
	@ElDescription(sub = "리캡챠", desc = "로그인시 봇인지 사림인지에 대해 파악한다")
	public Map<String, Object> verifyRecaptcha(RecaptchaVo recaptchaVo) {

		String token = recaptchaVo.getToken();
		String secret = recaptchaVo.getSecretKey();

		RestTemplate restTemplate = new RestTemplate();

		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("secret", secret);
		params.add("response", token);
		System.out.println(secret);
		System.out.println("값 ++++++" + token);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);

		try {
			ResponseEntity<Map> response = restTemplate.postForEntity(VERIFY_URL, entity, Map.class);
			Boolean success = (Boolean) response.getBody().get("success");

			Map<String, Object> result = new HashMap<>();
			result.put("verified", success);
			
			System.out.println("결과값:     "+success);

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
	@ElService(key = "UserList")
	@RequestMapping(value = "UserList")
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
	@ElService(key = "UserUpdView")
	@RequestMapping(value = "UserUpdView")
	@ElDescription(sub = "사용자 정보 갱신 폼을 위한 조회", desc = "사용자 정보 갱신 폼을 위한 조회를 한다.")
	public UserVo selectUser(UserVo userVo) throws Exception {
		UserVo selectUserVo = userService.selectUser(userVo);

		return selectUserVo;
	}

	/**
	 * 사용자 중복 체크
	 *
	 * @param userVo 사용자 정보
	 * @return 사용자 존재 여부 확인
	 * @throws Exception
	 */
	@ElService(key = "UserEmailCheck")
	@RequestMapping(value = "UserEmailCheck")
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
	@ElService(key = "UserIns")
	@RequestMapping(value = "UserIns")
	@ElDescription(sub = "사용자 정보 등록처리", desc = "사용자 정보를 등록 처리 한다.")
	public void insertUser(UserVo userVo) throws Exception {
		String rawPassword = userVo.getPassword();
		String encodedPassword = passwordEncoder.encode(rawPassword);
		userVo.setPassword(encodedPassword);
		userService.insertUser(userVo);
	}

	/**
	 * 사용자 정보를 갱신 처리 한다.
	 *
	 * @param userVo 사용자 정보
	 * @throws Exception
	 */
	@ElService(key = "UserUpd")
	@RequestMapping(value = "UserUpd")
	@ElValidator(errUrl = "/user/userRegister", errContinue = true)
	@ElDescription(sub = "사용자 정보 갱신처리", desc = "사용자 정보를 갱신 처리 한다.")
	public void updateUser(UserVo userVo) throws Exception {
		String rawPassword = userVo.getPassword();
		String encodedPassword = passwordEncoder.encode(rawPassword);
		userVo.setPassword(encodedPassword);
		userService.updateUser(userVo);
	}

	/**
	 * 사용자 정보를 삭제 처리한다.
	 *
	 * @param userVo 사용자 정보
	 * @throws Exception
	 */
	@ElService(key = "UserDel")
	@RequestMapping(value = "UserDel")
	@ElDescription(sub = "사용자 정보 삭제처리", desc = "사용자 정보를 삭제 처리한다.")
	public void deleteUser(UserVo userVo) throws Exception {
		userService.deleteUser(userVo);

	}

}
