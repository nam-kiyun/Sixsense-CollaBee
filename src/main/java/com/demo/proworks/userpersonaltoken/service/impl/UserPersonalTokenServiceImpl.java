package com.demo.proworks.userpersonaltoken.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.demo.proworks.userpersonaltoken.service.UserPersonalTokenService;
import com.demo.proworks.userpersonaltoken.vo.UserPersonalTokenVo;
import com.demo.proworks.userpersonaltoken.dao.UserPersonalTokenDAO;

/**  
 * @subject     : 깃허브 개인 처리를 위한 PAT토큰 관련 처리를 담당하는 ServiceImpl
 * @description	: 깃허브 개인 처리를 위한 PAT토큰 관련 처리를 담당하는 ServiceImpl
 * @author      : 남기윤
 * @since       : 2025/07/01
 * @modification
 * ===========================================================
 * DATE				AUTHOR				DESC
 * ===========================================================
 * 2025/07/01			 남기윤	 		최초 생성
 * 
 */
@Service("userPersonalTokenServiceImpl")
public class UserPersonalTokenServiceImpl implements UserPersonalTokenService {

    @Resource(name="userPersonalTokenDAO")
    private UserPersonalTokenDAO userPersonalTokenDAO;
	
	@Resource(name = "messageSource")
	private MessageSource messageSource;

    /**
     * 깃허브 개인 처리를 위한 PAT토큰 목록을 조회합니다.
     *
     * @process
     * 1. 깃허브 개인 처리를 위한 PAT토큰 페이징 처리하여 목록을 조회한다.
     * 2. 결과 List<UserPersonalTokenVo>을(를) 리턴한다.
     * 
     * @param  userPersonalTokenVo 깃허브 개인 처리를 위한 PAT토큰 UserPersonalTokenVo
     * @return 깃허브 개인 처리를 위한 PAT토큰 목록 List<UserPersonalTokenVo>
     * @throws Exception
     */
	public List<UserPersonalTokenVo> selectListUserPersonalToken(UserPersonalTokenVo userPersonalTokenVo) throws Exception {
		List<UserPersonalTokenVo> list = userPersonalTokenDAO.selectListUserPersonalToken(userPersonalTokenVo);	
	
		return list;
	}

    /**
     * 조회한 깃허브 개인 처리를 위한 PAT토큰 전체 카운트
     *
     * @process
     * 1. 깃허브 개인 처리를 위한 PAT토큰 조회하여 전체 카운트를 리턴한다.
     * 
     * @param  userPersonalTokenVo 깃허브 개인 처리를 위한 PAT토큰 UserPersonalTokenVo
     * @return 깃허브 개인 처리를 위한 PAT토큰 목록 전체 카운트
     * @throws Exception
     */
	public long selectListCountUserPersonalToken(UserPersonalTokenVo userPersonalTokenVo) throws Exception {
		return userPersonalTokenDAO.selectListCountUserPersonalToken(userPersonalTokenVo);
	}

    /**
     * 깃허브 개인 처리를 위한 PAT토큰를 상세 조회한다.
     *
     * @process
     * 1. 깃허브 개인 처리를 위한 PAT토큰를 상세 조회한다.
     * 2. 결과 UserPersonalTokenVo을(를) 리턴한다.
     * 
     * @param  userPersonalTokenVo 깃허브 개인 처리를 위한 PAT토큰 UserPersonalTokenVo
     * @return 단건 조회 결과
     * @throws Exception
     */
	public UserPersonalTokenVo selectUserPersonalToken(UserPersonalTokenVo userPersonalTokenVo) throws Exception {
		UserPersonalTokenVo resultVO = userPersonalTokenDAO.selectUserPersonalToken(userPersonalTokenVo);			
        
        return resultVO;
	}

    /**
     * 사용자 ID로 깃허브 개인 처리를 위한 PAT토큰을 조회한다.
     *
     * @process
     * 1. 사용자 ID로 깃허브 개인 처리를 위한 PAT토큰을 조회한다.
     * 2. 결과 UserPersonalTokenVo을(를) 리턴한다.
     * 
     * @param  userPersonalTokenVo 깃허브 개인 처리를 위한 PAT토큰 UserPersonalTokenVo (userId 필드 사용)
     * @return 단건 조회 결과
     * @throws Exception
     */
	public UserPersonalTokenVo selectUserPersonalTokenByUserId(UserPersonalTokenVo userPersonalTokenVo) throws Exception {
		UserPersonalTokenVo resultVO = userPersonalTokenDAO.selectUserPersonalTokenByUserId(userPersonalTokenVo);			
        
        return resultVO;
	}

    /**
     * 깃허브 개인 처리를 위한 PAT토큰를 등록 처리 한다.
     *
     * @process
     * 1. 깃허브 개인 처리를 위한 PAT토큰를 등록 처리 한다.
     * 
     * @param  userPersonalTokenVo 깃허브 개인 처리를 위한 PAT토큰 UserPersonalTokenVo
     * @return 번호
     * @throws Exception
     */
	public int insertUserPersonalToken(UserPersonalTokenVo userPersonalTokenVo) throws Exception {
		return userPersonalTokenDAO.insertUserPersonalToken(userPersonalTokenVo);	
	}
	
    /**
     * 깃허브 개인 처리를 위한 PAT토큰를 갱신 처리 한다.
     *
     * @process
     * 1. 깃허브 개인 처리를 위한 PAT토큰를 갱신 처리 한다.
     * 
     * @param  userPersonalTokenVo 깃허브 개인 처리를 위한 PAT토큰 UserPersonalTokenVo
     * @return 번호
     * @throws Exception
     */
	public int updateUserPersonalToken(UserPersonalTokenVo userPersonalTokenVo) throws Exception {				
		return userPersonalTokenDAO.updateUserPersonalToken(userPersonalTokenVo);	   		
	}

    /**
     * 깃허브 개인 처리를 위한 PAT토큰를 삭제 처리 한다.
     *
     * @process
     * 1. 깃허브 개인 처리를 위한 PAT토큰를 삭제 처리 한다.
     * 
     * @param  userPersonalTokenVo 깃허브 개인 처리를 위한 PAT토큰 UserPersonalTokenVo
     * @return 번호
     * @throws Exception
     */
	public int deleteUserPersonalToken(UserPersonalTokenVo userPersonalTokenVo) throws Exception {
		return userPersonalTokenDAO.deleteUserPersonalToken(userPersonalTokenVo);
	}
	
    /**
     * 사용자 ID로 깃허브 개인 처리를 위한 PAT토큰을 무효화(삭제) 처리한다.
     * 401 에러 발생 시 호출되어 만료된 토큰을 제거한다.
     *
     * @process
     * 1. 사용자 ID로 깃허브 개인 처리를 위한 PAT토큰을 삭제 처리한다.
     * 
     * @param  userId 사용자 ID
     * @return 삭제된 행 수
     * @throws Exception
     */
	public int invalidateUserPersonalTokenByUserId(String userId) throws Exception {
		UserPersonalTokenVo userPersonalTokenVo = new UserPersonalTokenVo();
		userPersonalTokenVo.setUserId(userId);
		return userPersonalTokenDAO.deleteUserPersonalToken(userPersonalTokenVo);
	}
	
	/**
     * 사용자 ID로 개인 토큰을 조회한다.
     *
     * @param userId 사용자 ID
     * @return 개인 토큰 문자열 또는 null
     * @throws Exception
     */
	@Override
	public String getToken(String userId) throws Exception {
		UserPersonalTokenVo searchVo = new UserPersonalTokenVo();
		searchVo.setUserId(userId);
		
		UserPersonalTokenVo tokenVo = userPersonalTokenDAO.selectUserPersonalTokenByUserId(searchVo);
		if (tokenVo != null && tokenVo.getAccessToken() != null) {
			return tokenVo.getAccessToken();
		}
		
		return null;
	}
	
}
