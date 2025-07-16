package com.demo.proworks.project.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.demo.proworks.project.service.ProjectService;
import com.demo.proworks.project.vo.ProjectVo;
import com.demo.proworks.project.dao.ProjectDAO;
import com.demo.proworks.email.vo.EmailVo;
import com.demo.proworks.projectuser.dao.ProjectUserDAO;
import com.demo.proworks.board.dao.BoardDAO;
import com.demo.proworks.task.dao.TaskDAO;
import com.demo.proworks.taskversion.dao.TaskVersionDAO;
import com.demo.proworks.comment.dao.CommentDAO;
import com.demo.proworks.filesrc.dao.FileSrcDAO;
import com.demo.proworks.projectuser.vo.ProjectUserVo;
import com.demo.proworks.board.vo.BoardVo;
import com.demo.proworks.task.vo.TaskVo;
import com.demo.proworks.taskversion.vo.TaskVersionVo;
import com.demo.proworks.comment.vo.CommentVo;
import com.demo.proworks.filesrc.vo.FileSrcVo;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;

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
	
	@Resource(name = "messageSource")
	private MessageSource messageSource;
	
	@Resource(name = "projectUserDAO")
	private ProjectUserDAO projectUserDAO;
	
	@Resource(name = "boardDAO")
	private BoardDAO boardDAO;
	
	@Resource(name = "taskDAO")
	private TaskDAO taskDAO;
	
	@Resource(name = "taskVersionDAO")
	private TaskVersionDAO taskVersionDAO;
	
	@Resource(name = "commentDAO")
	private CommentDAO commentDAO;
	
	@Resource(name = "fileSrcDAO")
	private FileSrcDAO fileSrcDAO;
	
	@Resource(name = "amazonS3")
	private AmazonS3 amazonS3;

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
     *
     * @process
     * 1. 프로젝트 정보를 등록 처리 한다.
     * 
     * @param  projectVo 프로젝트 정보 ProjectVo
     * @return 번호
     * @throws Exception
     */
	public int insertProject(ProjectVo projectVo) throws Exception {
		return projectDAO.insertProject(projectVo);	
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
     * 프로젝트와 관련된 모든 데이터를 완전히 삭제 처리한다.
     *
     * @process
     * 1. 프로젝트 관련 파일들을 S3에서 삭제한다.
     * 2. 데이터베이스에서 다음 순서로 삭제한다:
     *    - FILESRC (파일 정보)
     *    - COMMENT (댓글)
     *    - TASK_VERSION (작업 버전)
     *    - TASK (작업)
     *    - BOARD (보드)
     *    - PROJECT_USER (프로젝트 멤버)
     *    - PROJECT (프로젝트)
     * 
     * @param  projectId 프로젝트 ID
     * @return 삭제된 프로젝트 수
     * @throws Exception
     */
	@Transactional
	public int deleteProjectCompletely(String projectId) throws Exception {
		try {
			// 1. 프로젝트 관련 파일들을 S3에서 삭제
			deleteS3Files(projectId);
			
			// 2. 데이터베이스에서 순서대로 삭제
			
			// 2-1. FILESRC 삭제
			FileSrcVo fileSrcVo = new FileSrcVo();
			fileSrcVo.setProjectId(projectId);
			fileSrcDAO.deleteFileSrcByProjectId(fileSrcVo);
			
			// 2-2. COMMENT 삭제
			CommentVo commentVo = new CommentVo();
			commentVo.setProjectId(projectId);
			commentDAO.deleteCommentByProjectId(commentVo);
			
			// 2-3. TASK_VERSION 삭제
			TaskVersionVo taskVersionVo = new TaskVersionVo();
			taskVersionVo.setProjectId(projectId);
			taskVersionDAO.deleteTaskVersionByProjectId(taskVersionVo);
			
			// 2-4. TASK 삭제
			TaskVo taskVo = new TaskVo();
			taskVo.setProjectId(projectId);
			taskDAO.deleteTaskByProjectId(taskVo);
			
			// 2-5. BOARD 삭제
			BoardVo boardVo = new BoardVo();
			boardVo.setProjectId(projectId);
			boardDAO.deleteBoardByProjectId(boardVo);
			
			// 2-6. PROJECT_USER 삭제
			ProjectUserVo projectUserVo = new ProjectUserVo();
			projectUserVo.setProjectId(projectId);
			projectUserDAO.deleteProjectUserByProjectId(projectUserVo);
			
			// 2-7. PROJECT 삭제
			ProjectVo projectVo = new ProjectVo();
			projectVo.setProjectId(projectId);
			int deletedCount = projectDAO.deleteProject(projectVo);
			
			return deletedCount;
			
		} catch (Exception e) {
			throw new Exception("프로젝트 삭제 중 오류가 발생했습니다: " + e.getMessage(), e);
		}
	}
	
	/**
     * S3에서 프로젝트 관련 파일들을 삭제한다.
     *
     * @param  projectId 프로젝트 ID
     * @throws Exception
     */
	private void deleteS3Files(String projectId) throws Exception {
		try {
			String bucketName = "collabee";
			
			// 1. 프로젝트 이미지 URL 조회 및 삭제
			try {
				ProjectVo projectVo = new ProjectVo();
				projectVo.setProjectId(projectId);
				ProjectVo existingProject = projectDAO.selectProject(projectVo);
				
				if (existingProject != null && existingProject.getProjectImageUrl() != null) {
					String imageUrl = existingProject.getProjectImageUrl();
					
					// 기본 이미지가 아닌 경우에만 삭제
					if (!imageUrl.contains("default_project_image.jpg")) {
						// S3 Key 추출: https://collabee.s3.ap-northeast-2.amazonaws.com/projectImage/project_15_xxxxx.png
						// -> projectImage/project_15_xxxxx.png
						String s3Key = imageUrl.substring(imageUrl.indexOf(".com/") + 5);
						
						System.out.println("프로젝트 이미지 S3 삭제: " + s3Key);
						amazonS3.deleteObject(bucketName, s3Key);
					}
				}
			} catch (Exception e) {
				System.err.println("프로젝트 이미지 S3 삭제 실패: " + e.getMessage());
			}
			
			// 2. 작업 관련 파일들 조회 및 삭제
			try {
				// FileSrc 테이블에서 파일 경로들 조회
				FileSrcVo fileSrcVo = new FileSrcVo();
				fileSrcVo.setProjectId(projectId);
				
				// 프로젝트 관련 파일들 조회 (SQL에서 JOIN으로 조회)
				List<FileSrcVo> fileSrcList = fileSrcDAO.selectFileSrcByProjectId(fileSrcVo);
				
				if (fileSrcList != null && !fileSrcList.isEmpty()) {
					for (FileSrcVo fileSrc : fileSrcList) {
						if (fileSrc.getFilePath() != null && !fileSrc.getFilePath().trim().isEmpty()) {
							try {
								// S3 Key 추출: https://collabee.s3.ap-northeast-2.amazonaws.com/projectFiles/15/xxxxx.pdf
								// -> projectFiles/15/xxxxx.pdf
								String filePath = fileSrc.getFilePath();
								String s3Key = filePath.substring(filePath.indexOf(".com/") + 5);
								
								System.out.println("작업 파일 S3 삭제: " + s3Key);
								amazonS3.deleteObject(bucketName, s3Key);
							} catch (Exception e) {
								System.err.println("작업 파일 S3 삭제 실패: " + fileSrc.getFilePath() + " - " + e.getMessage());
							}
						}
					}
				}
			} catch (Exception e) {
				System.err.println("작업 파일들 S3 삭제 실패: " + e.getMessage());
			}
			
		} catch (Exception e) {
			// S3 삭제 실패는 로그만 남기고 계속 진행
			System.err.println("S3 파일 삭제 처리 실패: " + e.getMessage());
		}
	}
	
}
