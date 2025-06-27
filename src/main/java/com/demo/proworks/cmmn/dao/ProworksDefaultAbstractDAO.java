package com.demo.proworks.cmmn.dao;

import javax.annotation.Resource;

import com.inswave.elfw.db.ElAbstractMybatisDAO;

import org.apache.ibatis.session.SqlSessionFactory;

/**  
 * @subject		: 프로젝트 Default DAO 상위 클래스 - 각 업무 DAO 에서 상속받아 사용됨(기본 데이터 소스 사용시) 
 * @description : 프로젝트 Default DAO 상위 클래스 - 각 업무 DAO 에서 상속받아 사용됨(기본 데이터 소스 사용시) 
 * @author		: 개발팀
 * @since 		: 2025/05/19
 * @modification
 * ===========================================================
 * DATE				AUTHOR				NOTE
 * ===========================================================
 * 2025/05/19		샘플개발팀			최초 생성
 * 
 */
public class ProworksDefaultAbstractDAO extends ElAbstractMybatisDAO {
	
	/**
	 *  기본 데이터 소스의 SQL MAP 를 로드하기 위한 setter 
	 * @param sqlMapClient
	 */
	@Resource(name = "db1SqlSessionFactory")
    public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
        super.setSqlSessionFactory(sqlSessionFactory);
    }
}
