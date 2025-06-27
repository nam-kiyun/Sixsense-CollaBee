package com.demo.proworks.cmmn;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.inswave.elfw.adapter.AdapterException;
import com.inswave.elfw.exception.ElException;
import com.inswave.elfw.log.AppLog;
import com.inswave.elfw.session.SessionDataAdapter;
import com.inswave.elfw.util.ElBeanUtils;

import com.demo.proworks.emp.service.EmpService;
import com.demo.proworks.emp.vo.EmpVo;

/**  
 * @Class Name : ProworksSessionDataAdapter.java
 * @Description : 프로젝트 세션 데이터 어댑터 - 로그인 후 사용자 헤더 정보를 Setting 한다. 
 * @Modification Information  
 * @
 * @  수정일                  수정자                  수정내용
 * @ ---------   ---------   -------------------------------
 * @ 2019.08.01   EL개발팀             최초생성
 * 
 * @author EL개발팀
 * @since 2013.08.01
 * @version 1.0
 * @see
 * 
 *  Copyright (C) by Inswave All right reserved.
 */
public class ProworksSessionDataAdapter extends SessionDataAdapter {
	/**
	 * SessionAdapter 생성자이다.
	 * 
	 * @param adapterInfoMap Adapter 정보
	 */
	public ProworksSessionDataAdapter(Map<String, Object> adapterInfoMap) {
		super(adapterInfoMap);
	}

	/**
	 * 데모용 세션 터이터의 로드를 담당하는 구현체 메소드.
	 * - 프레임워크 SessionDataAdapter 추상클래스의 세션 데이터를 Set 하는 구현체 메소드
	 * - 프로젝트에 필요한 헤더 정보를 세팅한다. 
	 * -  해당 헤더 정보는 로그인 후에 사용가능하다. 
	 * 
	 * @param request HttpServletRequest
	 * @param id
	 * @param obj 기타 동적 파라미터에 추가할 수 있다.(ex. 서비스 구현체 )
	 * @return ProworksUserHeader
	 * @throws AdapterException
	 */
	@Override
	public ProworksUserHeader setSessionData(HttpServletRequest request, String id, Object... obj) throws AdapterException{
		
		// 로그인 후에 id 기반으로 세션 정보를 세팅하여 반환한다.		
		ProworksUserHeader userHeader = new ProworksUserHeader();
		userHeader.setUserId( id );

		// 사용자 세션을 UserHeader 에 설정 (샘플 예제)
		try{
			EmpService empService = (EmpService)ElBeanUtils.getBean("empServiceImpl");
			EmpVo empVo = new EmpVo();

			empVo.setEmpno(Integer.parseInt(id));
			EmpVo resEmpVo = empService.selectEmp(empVo);

			if( resEmpVo == null ) {
				throw new AdapterException("EL.ERROR.LOGIN.0004", new String[]{id});
			}
			
			// 사용자 세션 설정
			userHeader.setTestDeptNo(resEmpVo.getDeptno());
			userHeader.setTestDeptName(resEmpVo.getDname());
		}catch(ElException e){
			AppLog.error("setSessionData Error1",e);
			throw e;
		}catch(Exception e){
			AppLog.error("setSessionData Error2",e);
			throw new AdapterException("EL.ERROR.LOGIN.0005");
		}
		
		return userHeader;
	}

}
