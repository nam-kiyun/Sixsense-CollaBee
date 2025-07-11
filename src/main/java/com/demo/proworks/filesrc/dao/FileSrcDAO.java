package com.demo.proworks.filesrc.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.inswave.elfw.exception.ElException;
import com.demo.proworks.filesrc.vo.FileSrcVo;
import com.demo.proworks.filesrc.dao.FileSrcDAO;

/**
 * @subject : 파일 관련 처리를 담당하는 DAO
 * @description : 파일 관련 처리를 담당하는 DAO
 * @author : 국다인
 * @since : 2025/07/01
 * @modification ===========================================================
 *               DATE AUTHOR DESC
 *               ===========================================================
 *               2025/07/01 국다인 최초 생성
 * 
 */
@Repository("fileSrcDAO")
public class FileSrcDAO extends com.demo.proworks.cmmn.dao.ProworksDefaultAbstractDAO {

	/**
	 * 파일 상세 조회한다.
	 * 
	 * @param FileSrcVo 파일
	 * @return FileSrcVo 파일
	 * @throws ElException
	 */
	public List<FileSrcVo> selectFileSrcByTaskVersionId(FileSrcVo vo) throws ElException {
		return (List<FileSrcVo>) list("com.demo.proworks.filesrc.selectFileSrcByTaskVersionId", vo);
	}

	/**
	 * 파일 상세 조회한다.
	 * 
	 * @param FileSrcVo 파일
	 * @return FileSrcVo 파일
	 * @throws ElException
	 */
	public FileSrcVo selectFileSrc(FileSrcVo vo) throws ElException {
		return (FileSrcVo) selectByPk("com.demo.proworks.filesrc.selectFileSrc", vo);
	}

	/**
	 * 페이징을 처리하여 파일 목록조회를 한다.
	 * 
	 * @param FileSrcVo 파일
	 * @return List<FileSrcVo> 파일
	 * @throws ElException
	 */
	public List<FileSrcVo> selectListFileSrc(FileSrcVo vo) throws ElException {
		return (List<FileSrcVo>) list("com.demo.proworks.filesrc.selectListFileSrc", vo);
	}

	/**
	 * 파일 목록 조회의 전체 카운트를 조회한다.
	 * 
	 * @param FileSrcVo 파일
	 * @return 파일 조회의 전체 카운트
	 * @throws ElException
	 */
	public long selectListCountFileSrc(FileSrcVo vo) throws ElException {
		return (Long) selectByPk("com.demo.proworks.filesrc.selectListCountFileSrc", vo);
	}

	/**
	 * 파일 를 등록한다.
	 * 
	 * @param FileSrcVo 파일
	 * @return 번호
	 * @throws ElException
	 */
	public int insertFileSrc(FileSrcVo vo) throws ElException {
		return insert("com.demo.proworks.filesrc.insertFileSrc", vo);
	}

	/**
	 * 파일 를 갱신한다.
	 * 
	 * @param FileSrcVo 파일
	 * @return 번호
	 * @throws ElException
	 */
	public int updateFileSrc(FileSrcVo vo) throws ElException {
		return update("com.demo.proworks.filesrc.updateFileSrc", vo);
	}

	/**
	 * 파일 를 삭제한다.
	 * 
	 * @param FileSrcVo 파일
	 * @return 번호
	 * @throws ElException
	 */
	public int deleteFileSrc(FileSrcVo vo) throws ElException {
		return delete("com.demo.proworks.filesrc.deleteFileSrc", vo);
	}

}
