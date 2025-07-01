package com.demo.proworks.collabee.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.demo.proworks.collabee.service.ManagerService;
import com.demo.proworks.collabee.vo.ManagerVo;
import com.demo.proworks.collabee.dao.ManagerDAO;

/**  
 * @subject     : 업무 담당자 관련 처리를 담당하는 ServiceImpl
 * @description	: 업무 담당자 관련 처리를 담당하는 ServiceImpl
 * @author      : 남기윤
 * @since       : 2025/07/01
 * @modification
 * ===========================================================
 * DATE				AUTHOR				DESC
 * ===========================================================
 * 2025/07/01			 남기윤	 		최초 생성
 * 
 */
@Service("managerServiceImpl")
public class ManagerServiceImpl implements ManagerService {

    @Resource(name="managerDAO")
    private ManagerDAO managerDAO;
	
	@Resource(name = "messageSource")
	private MessageSource messageSource;

    /**
     * 업무 담당자 목록을 조회합니다.
     *
     * @process
     * 1. 업무 담당자 페이징 처리하여 목록을 조회한다.
     * 2. 결과 List<ManagerVo>을(를) 리턴한다.
     * 
     * @param  managerVo 업무 담당자 ManagerVo
     * @return 업무 담당자 목록 List<ManagerVo>
     * @throws Exception
     */
	public List<ManagerVo> selectListManager(ManagerVo managerVo) throws Exception {
		List<ManagerVo> list = managerDAO.selectListManager(managerVo);	
	
		return list;
	}

    /**
     * 조회한 업무 담당자 전체 카운트
     *
     * @process
     * 1. 업무 담당자 조회하여 전체 카운트를 리턴한다.
     * 
     * @param  managerVo 업무 담당자 ManagerVo
     * @return 업무 담당자 목록 전체 카운트
     * @throws Exception
     */
	public long selectListCountManager(ManagerVo managerVo) throws Exception {
		return managerDAO.selectListCountManager(managerVo);
	}

    /**
     * 업무 담당자를 상세 조회한다.
     *
     * @process
     * 1. 업무 담당자를 상세 조회한다.
     * 2. 결과 ManagerVo을(를) 리턴한다.
     * 
     * @param  managerVo 업무 담당자 ManagerVo
     * @return 단건 조회 결과
     * @throws Exception
     */
	public ManagerVo selectManager(ManagerVo managerVo) throws Exception {
		ManagerVo resultVO = managerDAO.selectManager(managerVo);			
        
        return resultVO;
	}

    /**
     * 업무 담당자를 등록 처리 한다.
     *
     * @process
     * 1. 업무 담당자를 등록 처리 한다.
     * 
     * @param  managerVo 업무 담당자 ManagerVo
     * @return 번호
     * @throws Exception
     */
	public int insertManager(ManagerVo managerVo) throws Exception {
		return managerDAO.insertManager(managerVo);	
	}
	
    /**
     * 업무 담당자를 갱신 처리 한다.
     *
     * @process
     * 1. 업무 담당자를 갱신 처리 한다.
     * 
     * @param  managerVo 업무 담당자 ManagerVo
     * @return 번호
     * @throws Exception
     */
	public int updateManager(ManagerVo managerVo) throws Exception {				
		return managerDAO.updateManager(managerVo);	   		
	}

    /**
     * 업무 담당자를 삭제 처리 한다.
     *
     * @process
     * 1. 업무 담당자를 삭제 처리 한다.
     * 
     * @param  managerVo 업무 담당자 ManagerVo
     * @return 번호
     * @throws Exception
     */
	public int deleteManager(ManagerVo managerVo) throws Exception {
		return managerDAO.deleteManager(managerVo);
	}
	
}
