package com.demo.proworks.email.web;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.annotation.Resource;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import javax.servlet.http.HttpServletRequest;

import com.demo.proworks.email.vo.EmailVo;
import com.demo.proworks.project.service.ProjectService;
import com.inswave.elfw.annotation.ElDescription;
import com.inswave.elfw.annotation.ElService;
import org.springframework.web.bind.annotation.RequestMethod;
import com.inswave.elfw.annotation.ElValidator;

/**
 * @subject : 이메일 발송을 처리하는 컨트롤러
 * @description : 회원가입 인증 메일, 사용자 추가 메일 컨트롤러
 * @author : 개발팀
 * @since : 2025/06/26
 * @modification ===========================================================
 *               DATE AUTHOR NOTE
 *               ===========================================================
 *               2025/06/26 6조_식스센스 국다인 최초 생성
 * 
 */
@Controller
@RequestMapping(value = "/send")
public class EmailController {

    @Resource(name = "mailSender")
    protected JavaMailSender mailSender;
    
    @Resource(name = "projectServiceImpl")
    private ProjectService projectService;
	
	@Value("${spring.mail.username}")
	private String username;
	
	@Value("${spring.mail.password}")
	private String password;

    /**
     * 이메일을 발송한다
     * 
     * @param emailVo 이메일 정보 EmailVo
     * @throws Exception
     */
    @ElService(key = "SendEmail")
    @RequestMapping(value = "SendEmail")
    @ElDescription(sub = "인증 메일을 발송", desc = "인증 메일을 발송합니다")
    public Map<String, Object> sendEmail(EmailVo emailVo) throws Exception {
        Map<String, Object> result = new HashMap<>();

        String email = emailVo.getEmail();
 
        //제목
        String subject = "[COLLABEE] 인증메일 발송";

        // 무작위 6자리 숫자 생성
        String code = generateVerificationCode();

        // 메일 내용
        String content = "<html lang='ko'>"
                + "<head><meta charset='UTF-8'/><title>인증 메일</title>"
                + "<style>"
                + "body { margin: 0; padding: 40px; font-family: 'Arial', sans-serif; text-align: center; }"
                + ".header { max-width: 607px; margin: 0 auto; display: flex; align-items: center; justify-content: center; background-color: rgb(104, 101, 101); border-radius: 10px 10px 0 0; padding: 30px 20px; }"
                + ".header img { width: 48px; height: 48px; margin-right: 14px; object-fit: contain; }"
                + ".email-title { margin: 0; font-size: 32px; color: #ffb823; text-shadow: 1px 1px 3px rgba(0, 0, 0, 0.3); }"
                + ".email-container { max-width: 600px; margin: 0 auto; background-color: white; border-radius: 0 0 10px 10px; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); padding: 24px; text-align: center; }"
                + ".email-content { font-size: 15px; line-height: 1.5; margin-top: 10px; }"
                + ".code-box { margin: 30px 0; text-align: center; }"
                + ".code-inner { display: inline-block; padding: 20px 40px; border-radius: 8px; font-size: 40px; font-weight: bold; color: #333; }"
                + ".email-footer { text-align: center; font-size: 14px; color: gray; margin-top: 20px; }"
                + "</style></head>"
                + "<body>"
                + "<div class='header' style='max-width: 607px; margin: 0 auto; display: flex; align-items: center; justify-content: center; background-color: rgb(104, 101, 101); border-radius: 10px 10px 0 0; padding: 30px 20px;'>"
                + "<img src='https://github.com/dorazi0423/test/blob/main/collabee.png?raw=true' alt='COLLABEE 로고' style='width: 48px; height: 48px; margin-right: 14px; object-fit: contain;' />"
                + "<h1 class='email-title' style='margin: 0; font-size: 32px; color: #ffb823; text-shadow: 1px 1px 3px rgba(0, 0, 0, 0.3);'>COLLABEE</h1>"
                + "</div>"
                + "<div class='email-container' style='max-width: 600px; margin: 0 auto; background-color: white; border-radius: 0 0 10px 10px; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); padding: 24px;'>"
                + "<p class='email-content' style='font-size: 15px; line-height: 1.5; margin-top: 10px; text-align: center; '>"
                + "안녕하십니까.<br />"
                + "인증을 위한 코드가 발급되었습니다.<br />"
                + "아래의 인증 코드를 입력하여 주세요."
                + "</p>"
                + "<div class='code-box' style='margin: 30px 0; text-align: center;'>"
                + "<div class='code-inner' style='display: inline-block; padding: 20px 40px; border-radius: 8px; font-size: 40px; font-weight: bold; color: #333;'>" + code + "</div>"
                + "</div>"
                + "<p class='email-footer' style='text-align: center; font-size: 14px; color: gray; margin-top: 20px;'>*10분 후에 만료됩니다</p>"
                + "</div>"
                + "</body>"
                + "</html>";

        System.out.println("발송할 이메일: " + email);
        JavaMailSenderImpl impl = (JavaMailSenderImpl) mailSender;
		System.out.println("📨 SMTP Host: " + impl.getHost());
		System.out.println("📨 SMTP Port: " + impl.getPort());
		System.out.println("📨 Username: " + impl.getUsername());
		System.out.println("📨 Properties: " + impl.getJavaMailProperties());
		System.out.println("📨 Password: " + impl.getPassword());
		
		impl.setUsername(username);
		impl.setPassword(password);
        

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(email);
            helper.setSubject(subject);
            helper.setText(content, true); // true -> HTML 형식 허용

            mailSender.send(message);

            result.put("success", true);
            result.put("message", "메일 발송 성공");
            result.put("code", code);
        } catch (Exception e) {
        	System.out.println(e);
            result.put("success", false);
            result.put("message", "메일 발송 실패: " + e.getMessage());
        }

        return result;
    }

    /**
     * 무작위 5자리 인증 코드 생성
     * 
     * @return 5자리 인증 코드 String
     */
    public String generateVerificationCode() {
        Random random = new Random();
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            code.append(random.nextInt(10));  // 0-9까지의 숫자
        }
        return code.toString();
    }

    /**
     * 프로젝트 초대 이메일을 발송한다
     * 
     * @param emailVo 이메일 정보 (email, projectId 포함)
     * @return Map<String, Object> 발송 결과
     * @throws Exception
     */
    @ElService(key = "SendProjectInviteEmail")
    @RequestMapping(value = "SendProjectInviteEmail", method = RequestMethod.POST)
    @ElDescription(sub = "프로젝트 초대 메일 발송", desc = "프로젝트 초대 메일을 발송합니다")
    public Map<String, Object> sendProjectInviteEmail(EmailVo emailVo) throws Exception {
        Map<String, Object> result = new HashMap<>();

        try {
            System.out.println("=== sendProjectInviteEmail 디버깅 ===");
            System.out.println("받은 EmailVo: " + emailVo.toString());
            System.out.println("받은 userId: " + emailVo.getUserId());
            System.out.println("받은 email: " + emailVo.getEmail());
            System.out.println("받은 projectName: " + emailVo.getProjectName());
            
            // WebSquare에서 받은 데이터 확인
            String userId = emailVo.getUserId();     // 초대받을 사용자 ID 또는 projectId
            String email = emailVo.getEmail();       // 초대받을 사용자 이메일
            
            String targetUserEmail = null;  // 초대받을 사용자 이메일
            String projectId = null;        // 프로젝트 ID
            
            // 파라미터 추정 (userId가 이메일 형식이면 그것이 초대받을 사용자, 아니면 projectId)
            if (userId != null && userId.contains("@")) {
                targetUserEmail = userId;  // userId가 이메일 형식
                projectId = email;         // email 필드에 projectId가 있을 수 있음
            } else {
                targetUserEmail = email;   // email 필드가 실제 이메일
                projectId = userId;        // userId 필드에 projectId가 있을 수 있음
            }
            
            System.out.println("추정된 targetUserEmail: " + targetUserEmail);
            System.out.println("추정된 projectId: " + projectId);
            
            if (targetUserEmail == null || targetUserEmail.trim().isEmpty()) {
                result.put("success", false);
                result.put("message", "초대받을 사용자 이메일이 필요합니다.");
                return result;
            }
            
            if (projectId == null || projectId.trim().isEmpty()) {
                result.put("success", false);
                result.put("message", "프로젝트 ID가 필요합니다.");
                return result;
            }
            
            // 1. 프로젝트 정보 조회
            EmailVo projectInfo = projectService.selectProjectForEmail(projectId);
            
            if (projectInfo == null) {
                result.put("success", false);
                result.put("message", "프로젝트 정보를 찾을 수 없습니다.");
                return result;
            }

            // 현재 시간 설정
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String currentTime = sdf.format(new java.util.Date());
            projectInfo.setEmailSendTime(currentTime);

            // 2. 이메일 정보 설정
            String toEmail = targetUserEmail; // 초대받을 사용자 이메일
            String subject = "[COLLABEE] " + projectInfo.getProjectName() + " 프로젝트 초대";

            // 3. HTML 이메일 내용 작성
            String content = createProjectInviteEmailContent(projectInfo);

            System.out.println("발송할 이메일: " + toEmail);
            JavaMailSenderImpl impl = (JavaMailSenderImpl) mailSender;
            impl.setUsername(username);
            impl.setPassword(password);

            // 4. 이메일 발송
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(content, true); // HTML 형식

            mailSender.send(message);

            result.put("success", true);
            result.put("message", "프로젝트 초대 메일 발송 성공");
            
        } catch (Exception e) {
            System.out.println("프로젝트 초대 메일 발송 오류: " + e);
            result.put("success", false);
            result.put("message", "프로젝트 초대 메일 발송 실패: " + e.getMessage());
        }

        return result;
    }

    /**
     * 프로젝트 초대 이메일 HTML 템플릿을 생성한다
     * 
     * @param projectInfo 프로젝트 정보
     * @return String HTML 이메일 내용
     */
    private String createProjectInviteEmailContent(EmailVo projectInfo) {
        String content = "<html lang='ko'>"
                + "<head><meta charset='UTF-8'/><title>프로젝트 초대</title>"
                + "<style>"
                + "body { margin: 0; padding: 40px; font-family: 'Arial', sans-serif; text-align: center; }"
                + ".header { max-width: 607px; margin: 0 auto; display: flex; align-items: center; justify-content: center; background-color: rgb(104, 101, 101); border-radius: 10px 10px 0 0; padding: 30px 20px; }"
                + ".header img { width: 48px; height: 48px; margin-right: 14px; object-fit: contain; }"
                + ".email-title { margin: 0; font-size: 32px; color: #ffb823; text-shadow: 1px 1px 3px rgba(0, 0, 0, 0.3); }"
                + ".email-container { max-width: 600px; margin: 0 auto; background-color: white; border-radius: 0 0 10px 10px; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); padding: 24px; text-align: center; }"
                + ".email-content { font-size: 15px; line-height: 1.5; margin-top: 10px; }"
                + ".project-card { margin: 30px 0; text-align: center; padding: 20px; border: 1px solid #ddd; border-radius: 8px; background-color: #f9f9f9; }"
                + ".project-image { width: 80px; height: 80px; border-radius: 8px; margin-bottom: 15px; object-fit: cover; }"
                + ".project-name { font-size: 24px; font-weight: bold; color: #333; margin-bottom: 10px; }"
                + ".project-info { font-size: 14px; color: #666; margin: 5px 0; }"
                + ".join-button { display: inline-block; padding: 15px 30px; background-color: #ffb823; color: #000; text-decoration: none; border-radius: 8px; font-weight: bold; margin-top: 20px; }"
                + ".email-footer { text-align: center; font-size: 14px; color: gray; margin-top: 20px; }"
                + "</style></head>"
                + "<body>"
                + "<div class='header'>"
                + "<img src='https://github.com/dorazi0423/test/blob/main/collabee.png?raw=true' alt='COLLABEE 로고' />"
                + "<h1 class='email-title'>COLLABEE</h1>"
                + "</div>"
                + "<div class='email-container'>"
                + "<p class='email-content'>"
                + "안녕하세요!<br />"
                + "<strong>" + projectInfo.getProjectName() + "</strong> 프로젝트에 초대되었습니다.<br />"
                + "아래 프로젝트에 참여해보세요!"
                + "</p>"
                + "<div class='project-card'>"
                + (projectInfo.getProjectImageUrl() != null ? 
                    "<img src='" + projectInfo.getProjectImageUrl() + "' alt='프로젝트 이미지' class='project-image' />" : "")
                + "<div class='project-name'>" + projectInfo.getProjectName() + "</div>"
                + "<div class='project-info'>팀장: " + projectInfo.getUserName() + "</div>"
                + "<div class='project-info'>초대 시각: " + projectInfo.getEmailSendTime() + "</div>"
                + "<a href='#' class='join-button'>프로젝트 참여하기</a>"
                + "</div>"
                + "<p class='email-footer'>수락 기능은 곧 추가될 예정입니다.</p>"
                + "</div>"
                + "</body>"
                + "</html>";
        
        return content;
    }
}
