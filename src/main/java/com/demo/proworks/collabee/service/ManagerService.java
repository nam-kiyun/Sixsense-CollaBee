package com.demo.proworks.collabee.service;

import java.util.List;

import com.demo.proworks.collabee.vo.ManagerVo;

/**  
 * @subject     : 업무 담당자 관련 처리를 담당하는 인터페이스
 * @description : 업무 담당자 관련 처리를 담당하는 인터페이스
 * @author      : 남기윤
 * @since       : 2025/07/01
 * @modification
 * ===========================================================
 * DATE				AUTHOR				DESC
 * ===========================================================
 * 2025/07/01			 남기윤	 		최초 생성
 * 
 */
public interface ManagerService {
	
    /**
     * 업무 담당자 페이징 처리하여 목록을 조회한다.
     *
     * @param  managerVo 업무 담당자 ManagerVo
     * @return 업무 담당자 목록 List<ManagerVo>
     * @throws Exception
     */
	public List<ManagerVo> selectListManager(ManagerVo managerVo) throws Exception;
	
    /**
     * 조회한 업무 담당자 전체 카운트
     * 
     * @param  managerVo 업무 담당자 ManagerVo
     * @return 업무 담당자 목록 전체 카운트
     * @throws Exception
     */
	public long selectListCountManager(ManagerVo managerVo) throws Exception;
	
    /**
     * 업무 담당자를 상세 조회한다.
     *
     * @param  managerVo 업무 담당자 ManagerVo
     * @return 단건 조회 결과
     * @throws Exception
     */
	public ManagerVo selectManager(ManagerVo managerVo) throws Exception;
		
    /**
     * 업무 담당자를 등록 처리 한다.
     *
     * @param  managerVo 업무 담당자 ManagerVo
     * @return 번호
     * @throws Exception
     */
	public int insertManager(ManagerVo managerVo) throws Exception;
	
    /**
     * 업무 담당자를 갱신 처리 한다.
     *
     * @param  managerVo 업무 담당자 ManagerVo
     * @return 번호
     * @throws Exception
     */
	public int updateManager(ManagerVo managerVo) throws Exception;
	
    /**
     * 업무 담당자를 삭제 처리 한다.
     *
     * @param  managerVo 업무 담당자 ManagerVo
     * @return 번호
     * @throws Exception
     */
	public int deleteManager(ManagerVo managerVo) throws Exception;
	
}
