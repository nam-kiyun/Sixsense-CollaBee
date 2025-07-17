package com.demo.proworks.project.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.demo.proworks.project.service.ProjectService;
import com.demo.proworks.project.vo.ProjectVo;
import com.demo.proworks.project.dao.ProjectDAO;
import com.demo.proworks.board.service.BoardService;
import com.demo.proworks.board.vo.BoardVo;
import com.demo.proworks.email.vo.EmailVo;

/**  
 * @subject     : 프로젝트 정보 관련 처리를 담당하는 ServiceImpl
 * @description	: 프로젝트 정보 관련 처리를 담당하는 ServiceImpl
 * @author      : 국다인
 * @since       : 2025/07/01
 * @modification
 * ===========================================================
 * DATE				AUTHOR				DESC
 * ===========================================================
 * 2025/07/01			 국다인	 		최초 생성
 * 
 */
@Service("projectServiceImpl")
public class ProjectServiceImpl implements ProjectService {

    @Resource(name="projectDAO")
    private ProjectDAO projectDAO;
    
    @Resource(name="boardServiceImpl")
    private BoardService boardService;
	
	@Resource(name = "messageSource")
	private MessageSource messageSource;

    /**
     * 프로젝트 정보 목록을 조회합니다.
     *
     * @process
     * 1. 프로젝트 정보 페이징 처리하여 목록을 조회한다.
     * 2. 결과 List<ProjectVo>을(를) 리턴한다.
     * 
     * @param  projectVo 프로젝트 정보 ProjectVo
     * @return 프로젝트 정보 목록 List<ProjectVo>
     * @throws Exception
     */
	public List<ProjectVo> selectListProject(ProjectVo projectVo) throws Exception {
		List<ProjectVo> list = projectDAO.selectListProject(projectVo);	
	
		return list;
	}

    /**
     * 조회한 프로젝트 정보 전체 카운트
     *
     * @process
     * 1. 프로젝트 정보 조회하여 전체 카운트를 리턴한다.
     * 
     * @param  projectVo 프로젝트 정보 ProjectVo
     * @return 프로젝트 정보 목록 전체 카운트
     * @throws Exception
     */
	public long selectListCountProject(ProjectVo projectVo) throws Exception {
		return projectDAO.selectListCountProject(projectVo);
	}

    /**
     * 프로젝트 정보를 상세 조회한다.
     *
     * @process
     * 1. 프로젝트 정보를 상세 조회한다.
     * 2. 결과 ProjectVo을(를) 리턴한다.
     * 
     * @param  projectVo 프로젝트 정보 ProjectVo
     * @return 단건 조회 결과
     * @throws Exception
     */
	public ProjectVo selectProject(ProjectVo projectVo) throws Exception {
		ProjectVo resultVO = projectDAO.selectProject(projectVo);			
        
        return resultVO;
	}

    /**
     * 프로젝트 정보를 등록 처리 한다.
     * 프로젝트 생성 시 기본 보드 3개 (할 일, 진행중, 완료)를 자동으로 생성한다.
     *
     * @process
     * 1. 프로젝트 정보를 등록 처리 한다.
     * 2. 생성된 프로젝트 ID로 기본 보드 3개를 생성한다.
     * 3. 트랜잭션으로 처리하여 실패 시 전체 롤백한다.
     * 
     * @param  projectVo 프로젝트 정보 ProjectVo
     * @return 번호
     * @throws Exception
     */
	@Transactional
	public int insertProject(ProjectVo projectVo) throws Exception {
		System.out.println("ProjectServiceImpl.insertProject - 프로젝트 생성 시작: " + projectVo.getProjectName());
		
		// 1. 프로젝트 생성
		System.out.println("🔧 프로젝트 생성 전 - projectId: " + projectVo.getProjectId());
		int result = projectDAO.insertProject(projectVo);
		System.out.println("🔧 프로젝트 생성 후 - projectId: " + projectVo.getProjectId() + ", result: " + result);
		
		if (result > 0) {
			System.out.println("✅ 프로젝트 생성 성공 - projectId: " + projectVo.getProjectId());
			
			// 2. 생성된 프로젝트 ID로 기본 보드 3개 생성
			String createdProjectId = projectVo.getProjectId();
			if (createdProjectId != null && !createdProjectId.trim().isEmpty()) {
				createDefaultBoards(createdProjectId);
				System.out.println("✅ 기본 보드 3개 생성 완료 - projectId: " + createdProjectId);
			} else {
				System.err.println("❌ 생성된 프로젝트 ID가 null이거나 빈 문자열입니다: " + createdProjectId);
				throw new Exception("프로젝트 ID를 가져올 수 없습니다.");
			}
		} else {
			System.err.println("❌ 프로젝트 생성 실패 - result: " + result);
			throw new Exception("프로젝트 생성에 실패했습니다.");
		}
		
		return result;	
	}
	
	/**
	 * 프로젝트 생성 시 기본 보드 3개를 자동으로 생성한다.
	 * 
	 * @param projectId 생성된 프로젝트 ID
	 * @throws Exception
	 */
	private void createDefaultBoards(String projectId) throws Exception {
		System.out.println("ProjectServiceImpl.createDefaultBoards - 기본 보드 생성 시작: " + projectId);
		
		// 기본 보드 정보 정의
		String[] defaultBoardTitles = {"할 일", "진행중", "완료"};
		
		for (String boardTitle : defaultBoardTitles) {
			BoardVo boardVo = new BoardVo();
			boardVo.setProjectId(projectId);
			boardVo.setBoardTitle(boardTitle);
			
			System.out.println("🔧 기본 보드 생성 중: " + boardTitle + " (projectId: " + projectId + ")");
			
			int boardResult = boardService.insertBoard(boardVo);
			
			if (boardResult > 0) {
				System.out.println("✅ 기본 보드 생성 성공: " + boardTitle + " (boardId: " + boardVo.getBoardId() + ", result: " + boardResult + ")");
			} else {
				System.err.println("❌ 기본 보드 생성 실패: " + boardTitle + " (result: " + boardResult + ")");
				throw new Exception("기본 보드 생성에 실패했습니다: " + boardTitle);
			}
		}
		
		System.out.println("✅ 모든 기본 보드 생성 완료");
	}
	
    /**
     * 프로젝트 정보를 갱신 처리 한다.
     *
     * @process
     * 1. 프로젝트 정보를 갱신 처리 한다.
     * 
     * @param  projectVo 프로젝트 정보 ProjectVo
     * @return 번호
     * @throws Exception
     */
	public int updateProject(ProjectVo projectVo) throws Exception {				
		return projectDAO.updateProject(projectVo);	   		
	}

    /**
     * 프로젝트 정보를 삭제 처리 한다.
     *
     * @process
     * 1. 프로젝트 정보를 삭제 처리 한다.
     * 
     * @param  projectVo 프로젝트 정보 ProjectVo
     * @return 번호
     * @throws Exception
     */
	public int deleteProject(ProjectVo projectVo) throws Exception {
		return projectDAO.deleteProject(projectVo);
	}

    /**
     * 프로젝트 초대 이메일을 위한 프로젝트 정보와 팀장 정보를 조회합니다.
     *
     * @process
     * 1. projectId로 프로젝트 정보와 팀장 정보를 JOIN하여 조회한다.
     * 2. 조회된 데이터를 EmailVo에 매핑하여 리턴한다.
     * 
     * @param  projectId 프로젝트 ID
     * @return EmailVo 이메일 발송을 위한 프로젝트 정보
     * @throws Exception
     */
	public EmailVo selectProjectForEmail(String projectId) throws Exception {
		return projectDAO.selectProjectForEmail(projectId);
	}
	
	/**
     * 사용자가 참여한 프로젝트 목록을 조회합니다.
     *
     * @process
     * 1. userId로 project_user 테이블에서 참여한 project_id들을 조회한다.
     * 2. 조회된 project_id들로 project 테이블에서 프로젝트 상세 정보를 조회한다.
     * 
     * @param  userId 사용자 ID
     * @return List<ProjectVo> 사용자가 참여한 프로젝트 목록
     * @throws Exception
     */
	public List<ProjectVo> selectUserParticipatedProjects(String userId) throws Exception {
		return projectDAO.selectUserParticipatedProjects(userId);
	}
	
}