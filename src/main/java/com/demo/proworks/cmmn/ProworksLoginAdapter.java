package com.demo.proworks.cmmn;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.demo.proworks.user.service.UserService;
import com.demo.proworks.user.vo.UserVo;
import com.inswave.elfw.exception.ElException;
import com.inswave.elfw.log.AppLog;
import com.inswave.elfw.login.LoginAdapter;
import com.inswave.elfw.login.LoginException;
import com.inswave.elfw.login.LoginInfo;
import com.inswave.elfw.util.ElBeanUtils;

/**
 * @subject : ProworksLoginAdapter.java
 * @description : 프로젝트 로그인 어댑터
 * @author : 개발팀
 * @since : 2025/05/19
 * @modification ===========================================================
 *               DATE AUTHOR NOTE
 *               ===========================================================
 *               2025/05/19 샘플개발팀 최초 생성
 * 
 */
public class ProworksLoginAdapter extends LoginAdapter {

	/**
	 * 데모용 로그인 어댑터의 생성자
	 * 
	 * @param adapterInfoMap Adapter 정보
	 */
	public ProworksLoginAdapter(Map<String, Object> adapterInfoMap) {
		super(adapterInfoMap);
	}

	/**
	 * 데모용 로그인 처리를 담당하는 구현체 메소드. 프레임워크 DefaultLoginAdapter 추상클래스의 로그인 구현체 메소드
	 * 
	 * @param request
	 * @param id
	 * @param params  기타 동적 파라미터에 추가할 수 있다.(ex. 서비스 구현체 )
	 * @return LoginInfo
	 * @throws LoginException
	 */
	@Override
	public LoginInfo login(HttpServletRequest request, String id, Object... params) throws LoginException {

		// 로그인 체크를 수행 (샘플 예제)
		try {
			String inputPassword = (String) params[0];

			UserService userService = (UserService) ElBeanUtils.getBean("userServiceImpl");
			UserVo userVo = new UserVo();
			userVo.setUserId(id); // 아이디 기준

			UserVo savedUser = userService.selectUser(userVo);
			if (savedUser == null) {
				throw new LoginException("EL.ERROR.LOGIN.0001"); // 사용자 없음
			}

			// 계정 잠금 여부 먼저 체크
			if ("Y".equals(savedUser.getAccountLocked())) {
				throw new LoginException("EL.ERROR.LOGIN.LOCKED"); // 계정 잠김
			}

			// 비밀번호 비교
			PasswordEncoder passwordEncoder = (PasswordEncoder) ElBeanUtils.getBean("passwordEncoder");
			String storedPassword = savedUser.getPassword();

			if (!passwordEncoder.matches(inputPassword, storedPassword)) {
				// 비밀번호 틀림 처리
				int failCount = savedUser.getLoginFailCount() == 0 ? 0 : savedUser.getLoginFailCount();
				failCount += 1;

				UserVo updateFailInfo = new UserVo();
				updateFailInfo.setUserId(id);
				updateFailInfo.setLoginFailCount(failCount);
				updateFailInfo.setAccountLocked("");
				updateFailInfo.setUserName("");
				updateFailInfo.setPassword("");
				updateFailInfo.setProfileImageUrl("");
				updateFailInfo.setIsActive("");
				updateFailInfo.setCreatedAt("");

				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String now = sdf.format(new Date());
				updateFailInfo.setLastFailTime(now); // String으로 저장 시

				if (failCount >= 6) {
					updateFailInfo.setAccountLocked("Y"); // 계정 잠금
				}

				userService.updateUser(updateFailInfo);

				if (failCount >= 6) {
					throw new LoginException("EL.ERROR.LOGIN.LOCKED"); // 계정 잠김 알림
				} else {
					throw new LoginException("EL.ERROR.LOGIN.0002"); // 비밀번호 틀림
				}
			}

			// 성공 시 로그인 실패 정보 초기화
			UserVo resetFailInfo = new UserVo();
			resetFailInfo.setUserId(id);
			resetFailInfo.setLoginFailCount(1);
			resetFailInfo.setAccountLocked("N");
			resetFailInfo.setUserName("");
			resetFailInfo.setPassword("");
			resetFailInfo.setProfileImageUrl("");
			resetFailInfo.setIsActive("");
			resetFailInfo.setCreatedAt("");
			resetFailInfo.setLastFailTime("");
			System.out.println("정보 :"+resetFailInfo);
			userService.updateUser(resetFailInfo);

		} catch (NumberFormatException e) {
			AppLog.error("login Error1", e);
			throw new LoginException("EL.ERROR.LOGIN.0001");
		} catch (ElException e) {
			AppLog.error("login Error2", e);
			throw e;
		} catch (Exception e) {
			AppLog.error("login Error3", e);
			throw new LoginException("EL.ERROR.LOGIN.0003");
		}

		// 3. 로그인 성공 설정
		LoginInfo info = new LoginInfo();
		info.setSuc(true);
		AppLog.debug("[Login] Proworks Login 성공.....");

		return info;
	}

	/**
	 * 데모용 로그아웃 처리를 담당하는 구현체 메소드. 프레임워크 DefaultLoginAdapter 추상클래스의 로그아웃 구현체 메소드
	 * 
	 * @param request
	 * @param id
	 * @param params  기타 동적 파라미터에 추가할 수 있다.
	 * @return LoginInfo
	 * @throws LoginException
	 */
	@Override
	public LoginInfo logout(HttpServletRequest request, String id, Object... params) throws LoginException {
		LoginInfo info = new LoginInfo();
		try {
			// 1. 로그아웃 처리로직 추가

			// 2. 로그아웃 성공 설정
			info.setSuc(true);
			AppLog.debug("[Logout] Proworks Logout 성공.....");

		} catch (Exception e) {
			throw new LoginException(e);
		}
		return info;
	}

}
