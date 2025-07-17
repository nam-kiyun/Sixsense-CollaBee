package com.demo.proworks.userpersonaltoken.service;

import java.util.List;

import com.demo.proworks.userpersonaltoken.vo.UserPersonalTokenVo;

/**  
 * @subject     : 깃허브 개인 처리를 위한 PAT토큰 관련 처리를 담당하는 인터페이스
 * @description : 깃허브 개인 처리를 위한 PAT토큰 관련 처리를 담당하는 인터페이스
 * @author      : 남기윤
 * @since       : 2025/07/01
 * @modification
 * ===========================================================
 * DATE				AUTHOR				DESC
 * ===========================================================
 * 2025/07/01			 남기윤	 		최초 생성
 * 
 */
public interface UserPersonalTokenService {
	
    /**
     * 깃허브 개인 처리를 위한 PAT토큰 페이징 처리하여 목록을 조회한다.
     *
     * @param  userPersonalTokenVo 깃허브 개인 처리를 위한 PAT토큰 UserPersonalTokenVo
     * @return 깃허브 개인 처리를 위한 PAT토큰 목록 List<UserPersonalTokenVo>
     * @throws Exception
     */
	public List<UserPersonalTokenVo> selectListUserPersonalToken(UserPersonalTokenVo userPersonalTokenVo) throws Exception;
	
    /**
     * 조회한 깃허브 개인 처리를 위한 PAT토큰 전체 카운트
     * 
     * @param  userPersonalTokenVo 깃허브 개인 처리를 위한 PAT토큰 UserPersonalTokenVo
     * @return 깃허브 개인 처리를 위한 PAT토큰 목록 전체 카운트
     * @throws Exception
     */
	public long selectListCountUserPersonalToken(UserPersonalTokenVo userPersonalTokenVo) throws Exception;
	
    /**
     * 깃허브 개인 처리를 위한 PAT토큰를 상세 조회한다.
     *
     * @param  userPersonalTokenVo 깃허브 개인 처리를 위한 PAT토큰 UserPersonalTokenVo
     * @return 단건 조회 결과
     * @throws Exception
     */
	public UserPersonalTokenVo selectUserPersonalToken(UserPersonalTokenVo userPersonalTokenVo) throws Exception;
	
    /**
     * 사용자 ID로 깃허브 개인 처리를 위한 PAT토큰을 조회한다.
     *
     * @param  userPersonalTokenVo 깃허브 개인 처리를 위한 PAT토큰 UserPersonalTokenVo (userId 필드 사용)
     * @return 단건 조회 결과
     * @throws Exception
     */
	public UserPersonalTokenVo selectUserPersonalTokenByUserId(UserPersonalTokenVo userPersonalTokenVo) throws Exception;
		
    /**
     * 깃허브 개인 처리를 위한 PAT토큰를 등록 처리 한다.
     *
     * @param  userPersonalTokenVo 깃허브 개인 처리를 위한 PAT토큰 UserPersonalTokenVo
     * @return 번호
     * @throws Exception
     */
	public int insertUserPersonalToken(UserPersonalTokenVo userPersonalTokenVo) throws Exception;
	
    /**
     * 깃허브 개인 처리를 위한 PAT토큰를 갱신 처리 한다.
     *
     * @param  userPersonalTokenVo 깃허브 개인 처리를 위한 PAT토큰 UserPersonalTokenVo
     * @return 번호
     * @throws Exception
     */
	public int updateUserPersonalToken(UserPersonalTokenVo userPersonalTokenVo) throws Exception;
	
    /**
     * 깃허브 개인 처리를 위한 PAT토큰를 삭제 처리 한다.
     *
     * @param  userPersonalTokenVo 깃허브 개인 처리를 위한 PAT토큰 UserPersonalTokenVo
     * @return 번호
     * @throws Exception
     */
	public int deleteUserPersonalToken(UserPersonalTokenVo userPersonalTokenVo) throws Exception;
	
    /**
     * 사용자 ID로 깃허브 개인 처리를 위한 PAT토큰을 무효화(삭제) 처리한다.
     * 401 에러 발생 시 호출되어 만료된 토큰을 제거한다.
     *
     * @param  userId 사용자 ID
     * @return 삭제된 행 수
     * @throws Exception
     */
	public int invalidateUserPersonalTokenByUserId(String userId) throws Exception;
	
}
