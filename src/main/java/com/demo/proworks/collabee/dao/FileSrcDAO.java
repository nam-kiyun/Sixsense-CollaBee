package com.demo.proworks.collabee.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.inswave.elfw.exception.ElException;
import com.demo.proworks.collabee.vo.FileSrcVo;
import com.demo.proworks.collabee.dao.FileSrcDAO;

/**  
 * @subject     : 업로드한 파일 저장 경로 관련 처리를 담당하는 DAO
 * @description : 업로드한 파일 저장 경로 관련 처리를 담당하는 DAO
 * @author      : 남기윤
 * @since       : 2025/07/01
 * @modification
 * ===========================================================
 * DATE				AUTHOR				DESC
 * ===========================================================
 * 2025/07/01			 남기윤	 		최초 생성
 * 
 */
@Repository("fileSrcDAO")
public class FileSrcDAO extends com.demo.proworks.cmmn.dao.ProworksDefaultAbstractDAO {

    /**
     * 업로드한 파일 저장 경로 상세 조회한다.
     *  
     * @param  FileSrcVo 업로드한 파일 저장 경로
     * @return FileSrcVo 업로드한 파일 저장 경로
     * @throws ElException
     */
    public FileSrcVo selectFileSrc(FileSrcVo vo) throws ElException {
        return (FileSrcVo) selectByPk("com.demo.proworks.collabee.selectFileSrc", vo);
    }

    /**
     * 페이징을 처리하여 업로드한 파일 저장 경로 목록조회를 한다.
     *  
     * @param  FileSrcVo 업로드한 파일 저장 경로
     * @return List<FileSrcVo> 업로드한 파일 저장 경로
     * @throws ElException
     */
    public List<FileSrcVo> selectListFileSrc(FileSrcVo vo) throws ElException {      	
        return (List<FileSrcVo>)list("com.demo.proworks.collabee.selectListFileSrc", vo);
    }

    /**
     * 업로드한 파일 저장 경로 목록 조회의 전체 카운트를 조회한다.
     *  
     * @param  FileSrcVo 업로드한 파일 저장 경로
     * @return 업로드한 파일 저장 경로 조회의 전체 카운트
     * @throws ElException
     */
    public long selectListCountFileSrc(FileSrcVo vo)  throws ElException{               
        return (Long)selectByPk("com.demo.proworks.collabee.selectListCountFileSrc", vo);
    }
        
    /**
     * 업로드한 파일 저장 경로를 등록한다.
     *  
     * @param  FileSrcVo 업로드한 파일 저장 경로
     * @return 번호
     * @throws ElException
     */
    public int insertFileSrc(FileSrcVo vo) throws ElException {    	
        return insert("com.demo.proworks.collabee.insertFileSrc", vo);
    }

    /**
     * 업로드한 파일 저장 경로를 갱신한다.
     *  
     * @param  FileSrcVo 업로드한 파일 저장 경로
     * @return 번호
     * @throws ElException
     */
    public int updateFileSrc(FileSrcVo vo) throws ElException {
        return update("com.demo.proworks.collabee.updateFileSrc", vo);
    }

    /**
     * 업로드한 파일 저장 경로를 삭제한다.
     *  
     * @param  FileSrcVo 업로드한 파일 저장 경로
     * @return 번호
     * @throws ElException
     */
    public int deleteFileSrc(FileSrcVo vo) throws ElException {
        return delete("com.demo.proworks.collabee.deleteFileSrc", vo);
    }

}
