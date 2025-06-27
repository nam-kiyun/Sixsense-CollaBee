/*
DELETE FROM EL_APP   WHERE APP_ID = 'InsWebApp';

DELETE FROM EL_SVC_MENU WHERE APP_ID = 'InsWebApp';

DELETE FROM EL_APP_CMT_DEPL   WHERE APP_ID = 'InsWebApp';

DELETE FROM EL_APP_CMT_DEPL_PARAM   WHERE APP_ID = 'InsWebApp';

DELETE FROM EL_RSC   WHERE APP_ID = 'InsWebApp';

DELETE FROM EL_NOTIFY WHERE APP_ID = 'InsWebApp';

DELETE FROM EL_SVC_AUTH WHERE APP_ID = 'InsWebApp';

DELETE FROM EL_SVC_CTR WHERE APP_ID = 'InsWebApp';

DELETE FROM EL_SVC_GROUP_TREE WHERE APP_ID = 'InsWebApp';

DELETE FROM EL_SVC_GROUP WHERE APP_ID = 'InsWebApp';

DELETE FROM EL_SVC WHERE APP_ID = 'InsWebApp';

DELETE FROM EL_SYS_PROP WHERE APP_ID = 'InsWebApp';

DELETE FROM EL_MENU_AUTH WHERE APP_ID = 'InsWebApp';

DELETE FROM EL_USER WHERE APP_ID = 'InsWebApp';

DELETE FROM EL_USER_GROUP WHERE APP_ID = 'InsWebApp';

DELETE FROM EL_MENU WHERE APP_ID = 'InsWebApp';

DELETE FROM EL_MENU_GROUP WHERE APP_ID = 'InsWebApp';

COMMIT;
 */


/* 아래 EL_APP 정보는 시스템 상황에 맞게 수정 필요  */  
Insert into EL_APP
   (APP_ID, APP_NAME, APP_ORDER, JAVA_HOME, JAVAC_OPTION, 
    APP_JAR_DIR, APP_CLASS_PATH)
 Values
   ('InsWebApp', 'InsWebApp_WebApplication', 100, '/opt/jdk-11.0.15-ojdkbuild-linux-x64', '-encoding UTF-8 -g', 
    '/home/proworks/context/InsWebApp/WEB-INF/lib:/home/proworks/context/InsWebApp/WEB-INF/lib-provided', '/home/proworks/context/InsWebApp/WEB-INF/classes');
   
/* 아래 EL_APP_CMT_DEPL 정보는 시스템 상황에 맞게 수정 필요 - 최소 1개의 기본 Copy 배포는 필요함   */  
Insert into EL_APP_CMT_DEPL
   (APP_ID, APP_DEPL_ID, APP_DEPL_GBN, APP_DEPL_NAME, APP_DEPL_CLASS, 
    APP_DEPL_YN, DEPL_SRC_PATH, DEPL_RES_PATH, DEPL_CLS_PATH, DEPL_WEB_PATH)
 Values
   ('InsWebApp', 'InsWebApp_node1', 'DEV', 'InsWebApp_WebApplication_기본노드', 'com.inswave.elfw.deploy.DefaultDeployCopyAdapter', 
    'Y', '/home/proworks/resources/InsWebApp', '/home/proworks/context/InsWebApp/WEB-INF/classes', '/home/proworks/context/InsWebApp/WEB-INF/classes', '/home/proworks/context/InsWebApp');
    
   
COMMIT;

Insert into EL_NOTIFY
   (APP_ID, SERVER_NO, NOTIFY_BASE_URL, USED_YN, SERVER_ID)
 Values
   ('InsWebApp', 1, 'http://localhost:8080/InsWebApp', 'Y', 'InsWebApp');



Insert into EL_SYS_PROP
   (APP_ID, SYS_GROUP_ID, SYS_KEY, SYS_SUB, SYS_VAL, SYS_DESC, SORT_SEQ)
 Values
   ('InsWebApp', 'EL_CORE_PROP', 'SERVER_MODE', '서버 모드', 'DEV', '서버 모드(DEV, TST, RUN) - 개발 모드는 Validator 실시간 반영, F/W용 서버 모드, 업무용은 신규 생성 권장', 100);
Insert into EL_SYS_PROP
   (APP_ID, SYS_GROUP_ID, SYS_KEY, SYS_SUB, SYS_VAL, SYS_DESC, SORT_SEQ)
 Values
   ('InsWebApp', 'EL_CORE_PROP', 'CONTROLLER_VALIDATOR_USE_YN', 'Validator 사용여부', 'Y', 'Validator 사용우무 ( Y / N )', 300);
Insert into EL_SYS_PROP
   (APP_ID, SYS_GROUP_ID, SYS_KEY, SYS_SUB, SYS_VAL, SYS_DESC)
 Values
   ('InsWebApp', 'EL_CORE_PROP', 'APPLICATION_LOG_FACTORY_IMPL', 'App 로그 구현체 클래스 ', 'com.inswave.elfw.log.DefaultApplicationLogFactoryImpl', 'Log Factory 구현체 ( 커스트 마이징 가능 )');
Insert into EL_SYS_PROP
   (APP_ID, SYS_GROUP_ID, SYS_KEY, SYS_SUB, SYS_VAL, SYS_DESC, SORT_SEQ)
 Values
   ('InsWebApp', 'EL_CORE_PROP', 'APPLICATION_LOG_DIR', 'App 로그 저장위치', '/home/proworks/logs/InsWebApp', '로그 파일의 위치', 210);
Insert into EL_SYS_PROP
   (APP_ID, SYS_GROUP_ID, SYS_KEY, SYS_SUB, SYS_VAL, SYS_DESC)
 Values
   ('InsWebApp', 'EL_CORE_PROP', 'APPLICATION_LOG_SEND_TARGET_LOGGER_NAME', 'App 로그 외부로 보낼 이름', 'elfw.appLogger', 'AppLog를 외부 Appender 로 보낼 이름 - 존재하지 않을시에 보내지 않음');
Insert into EL_SYS_PROP
   (APP_ID, SYS_GROUP_ID, SYS_KEY, SYS_SUB, SYS_DESC)
 Values
   ('InsWebApp', 'EL_CORE_PROP', 'APPLICATION_LOG_APPEND_LOGGERS', 'APP 로그에 추가할 외부로거 이름', '가져올 외부로거 (,로 구분)- 외부 로거의 설정을 강제로 변경할 경우 로거 뒤에 레벨을 명시함. ex) java.sql:ERROR');
Insert into EL_SYS_PROP
   (APP_ID, SYS_GROUP_ID, SYS_KEY, SYS_SUB, SYS_VAL, SYS_DESC)
 Values
   ('InsWebApp', 'EL_CORE_PROP', 'LOG4J_LOADING_BASE_FILE', '로딩할 log4j 파일명 ', 'log4j.xml', '로딩할 log4.xml - 클래스 패스에 바로 존재하여야 함');
Insert into EL_SYS_PROP
   (APP_ID, SYS_GROUP_ID, SYS_KEY, SYS_SUB, SYS_VAL, SYS_DESC)
 Values
   ('InsWebApp', 'EL_CORE_PROP', 'USER_HEADER_CLASS_NAME', '사용자 헤더 구현체 클래스명 ', 'com.demo.proworks.cmmn.ProworksUserHeader', '사용자 Header 클래스 명 ');
Insert into EL_SYS_PROP
   (APP_ID, SYS_GROUP_ID, SYS_KEY, SYS_SUB, SYS_VAL, SYS_DESC)
 Values
   ('InsWebApp', 'EL_CORE_PROP', 'LOGIN_ADAPTER_CLASS_NAME', '로그인 어댑터 구현체 클래스명', 'com.demo.proworks.cmmn.ProworksLoginAdapter', '로그인 어댑터 클래스명');
Insert into EL_SYS_PROP
   (APP_ID, SYS_GROUP_ID, SYS_KEY, SYS_SUB, SYS_VAL, SYS_DESC)
 Values
   ('InsWebApp', 'EL_CORE_PROP', 'SESSION_ADAPTER_CLASS_NAME', '세션 어댑터 구현체 클래스명', 'com.inswave.elfw.session.HttpSessionAdapter', '세션의 저장소 어댑터 클래스명');
Insert into EL_SYS_PROP
   (APP_ID, SYS_GROUP_ID, SYS_KEY, SYS_SUB, SYS_VAL, SYS_DESC)
 Values
   ('InsWebApp', 'EL_CORE_PROP', 'SESSION_DATA_ADAPTER_CLASS_NAME', '세션 테이터 어댑터 구현체 클래스명', 'com.demo.proworks.cmmn.ProworksSessionDataAdapter', '세션에 저장할 데이터 어댑터 클래스명');
Insert into EL_SYS_PROP
   (APP_ID, SYS_GROUP_ID, SYS_KEY, SYS_SUB, SYS_VAL, SYS_DESC)
 Values
   ('InsWebApp', 'EL_CORE_PROP', 'HEADER_SET_ADAPTER_CLASS_NAME', '헤더설정 어댑터 구현체 클래스명', 'com.demo.proworks.cmmn.ProworksUserHeaderSetAdapter', '헤더설정 어댑터 클래스명');
   
Insert into EL_SYS_PROP
   (APP_ID, SYS_GROUP_ID, SYS_KEY, SYS_SUB, SYS_VAL, SYS_DESC, SORT_SEQ)
 Values
   ('InsWebApp', 'EL_CORE_PROP', 'OUTPUT_XML_DATA_SERVICE_YN', 'XML 입출력 사용유무', 'Y', 'XML Output 서비스 전체 기본설정', 330);
Insert into EL_SYS_PROP
   (APP_ID, SYS_GROUP_ID, SYS_KEY, SYS_SUB, SYS_VAL, SYS_DESC, SORT_SEQ)
 Values
   ('InsWebApp', 'EL_CORE_PROP', 'OUTPUT_JSON_DATA_SERVICE_YN', 'JSON 입출력 사용유무', 'Y', 'JSON Output 서비스 전체 기본설정', 340);

Insert into EL_SYS_PROP
   (APP_ID, SYS_GROUP_ID, SYS_KEY, SYS_SUB, SYS_VAL, SYS_DESC, SORT_SEQ)
 Values
   ('InsWebApp', 'EL_CORE_PROP', 'OUTPUT_FLD_DATA_SERVICE_YN', 'FLD 입출력 사용유무', 'Y', 'FLD 입출력사용여부 ', 350);
   
   
Insert into EL_SYS_PROP
   (APP_ID, SYS_GROUP_ID, SYS_KEY, SYS_SUB, SYS_VAL, SYS_DESC)
 Values
   ('InsWebApp', 'EL_CORE_PROP', 'INPUT_XML_RESOLVER_ADAPTER_CLASS_NAME', 'XML 입출력 구현체 클래스명', 'com.inswave.elfw.resolver.DefaultXmlAgumentResolver2Adapter', '존재하면 XML OUT 서비스 시에 Input을 Http Request 가 아닌 xml 데이터로 받는다. ( reqeust 파라미터의 data 필드에 문자로 받는다.)');
Insert into EL_SYS_PROP
   (APP_ID, SYS_GROUP_ID, SYS_KEY, SYS_SUB, SYS_VAL, SYS_DESC)
 Values
   ('InsWebApp', 'EL_CORE_PROP', 'INPUT_JSON_RESOLVER_ADAPTER_CLASS_NAME', 'JSON 입출력 구현체 클래스명', 'com.inswave.elfw.resolver.DefaultJsonAgumentResolverAdapter', '존재하면 JSO OUT 서비스 시에 Input을 Http Request 가 아닌 json 데이터로 받는다. ( reqeust 파라미터의 data 필드에 문자로 받는다.)');
Insert into EL_SYS_PROP
   (APP_ID, SYS_GROUP_ID, SYS_KEY, SYS_SUB, SYS_VAL, SYS_DESC, SORT_SEQ)
 Values
   ('InsWebApp', 'EL_CORE_PROP', 'AUTH_CHECK_YN', '전체 권한체크 사용유무', 'N', '전체 컨트롤러에서 권한체크', 310);
Insert into EL_SYS_PROP
   (APP_ID, SYS_GROUP_ID, SYS_KEY, SYS_SUB, SYS_VAL, SYS_DESC, SORT_SEQ)
 Values
   ('InsWebApp', 'EL_CORE_PROP', 'SESSION_CHECK_YN', '전체 세션체크 사용유무', 'Y', '전체 컨트롤러에서 세션 체크', 320);

Insert into EL_SYS_PROP
   (APP_ID, SYS_GROUP_ID, SYS_KEY, SYS_SUB, SYS_VAL, SYS_DESC, SORT_SEQ)
 Values
   ('InsWebApp', 'EL_CORE_PROP', 'FRAMEWORK_FORCE_SESSION_CHECK_YN', '프레임워크 강제 세션 체크 여부', 'Y', '프레임워크 강제 세션 체크 여부', 321);
    
Insert into EL_SYS_PROP
   (APP_ID, SYS_GROUP_ID, SYS_KEY, SYS_SUB, SYS_VAL, SYS_DESC)
 Values
   ('InsWebApp', 'EL_CORE_PROP', 'AUTH_ADAPTER_CLASS_NAME', '권한체크 구현체 클래스명', 'com.inswave.elfw.auth.DefaultAuthAdapter', '권한체크를 담당하는 어댑터 클래스명');
Insert into EL_SYS_PROP
   (APP_ID, SYS_GROUP_ID, SYS_KEY, SYS_SUB, SYS_VAL, SYS_DESC)
 Values
   ('InsWebApp', 'EL_CORE_PROP', 'SCRN_AUTH_ADAPTER_CLASS_NAME', '화면권한체크 구현체 클래스명', 'com.inswave.elfw.auth.DefaultScrnAuthAdapter', '화면권한체크를 담당하는 어댑터 클래스명');
   
Insert into EL_SYS_PROP
   (APP_ID, SYS_GROUP_ID, SYS_KEY, SYS_SUB, SYS_VAL, SYS_DESC)
 Values
   ('InsWebApp', 'EL_CORE_PROP', 'SCRN_ID_ADAPTER_CLASS_NAME', '화면ID 구현체 클래스명', 'com.inswave.elfw.auth.DefaultScrnIdAdapter', '화면ID 담당하는 어댑터 클래스명');
   
   
Insert into EL_SYS_PROP
   (APP_ID, SYS_GROUP_ID, SYS_KEY, SYS_SUB, SYS_VAL, SYS_DESC, SORT_SEQ)
 Values
   ('InsWebApp', 'EL_CORE_PROP', 'APPLICATION_LOG_LEVEL', 'App 로그레벨', 'DEBUG', '로그레벨 ( app 로그만 설정 ) - 외부 로거의 경우 외부 로거의 설정을 따라간다.', 230);

Insert into EL_SYS_PROP
   (APP_ID, SYS_GROUP_ID, SYS_KEY, SYS_SUB, SYS_VAL, SYS_DESC)
 Values
   ('InsWebApp', 'EL_CORE_PROP', 'SPRING_CONFIG_BASE_CLASSPATH', 'Spring 설정파일 위치', 'inswave/spring', '스프링 설정파일 위치 ( Validator Manager 에서 사용 )');
Insert into EL_SYS_PROP
   (APP_ID, SYS_GROUP_ID, SYS_KEY, SYS_SUB, SYS_VAL, SYS_DESC, SORT_SEQ)
 Values
   ('InsWebApp', 'EL_CORE_PROP', 'SQL_LOG_LINE_FEED_YN', 'SQL 로그 줄바꿈 사용여부', 'Y', 'SQL로그 줄바꿈 처리여부', 250);
Insert into EL_SYS_PROP
   (APP_ID, SYS_GROUP_ID, SYS_KEY, SYS_SUB, SYS_VAL, SYS_DESC, SORT_SEQ)
 Values
   ('InsWebApp', 'EL_CORE_PROP', 'SQL_WRITE_LOG_LEVEL', 'SQL 기록할 로그레벨', 'DEBUG', 'SQL문 기록할 로그레벨( DEBUG, OFF ) - 기본은 DEBUG', 240);
   
Insert into EL_SYS_PROP
   (APP_ID, SYS_GROUP_ID, SYS_KEY, SYS_SUB, SYS_VAL, 
    SYS_DESC, SORT_SEQ)
 Values
   ('InsWebApp', 'EL_CORE_PROP', 'APPLICATION_LOG_WRITE_YN', '로그 기록여부', 'Y', 
    '로그 기록여부( Y/N ) - 기본은 Y', 235);   
   
Insert into EL_SYS_PROP
   (APP_ID, SYS_GROUP_ID, SYS_KEY, SYS_SUB, SYS_VAL, SYS_DESC)
 Values
   ('InsWebApp', 'EL_CORE_PROP', 'SYSTEM_HANDLE_CLASS_NAME', '시스템 선/후처리 구현체 클래스명', 'com.demo.proworks.cmmn.ProworksSystemHandleAdapter', '시스템 선/후처리를 처리하는 어댑터 클래스명');
Insert into EL_SYS_PROP
   (APP_ID, SYS_GROUP_ID, SYS_KEY, SYS_SUB, SYS_VAL, SYS_DESC)
 Values
   ('InsWebApp', 'EL_CORE_PROP', 'SERVICE_USER_CONTROL_CLASS_NAME', '서비스 제어 구현체 클래스명', 'com.inswave.elfw.intercept.service.DefaultElServiceUserControlAdapter', '사용자정의 서비스 제어 어댑터 클래스명');
Insert into EL_SYS_PROP
   (APP_ID, SYS_GROUP_ID, SYS_KEY, SYS_SUB, SYS_VAL, SYS_DESC)
 Values
   ('InsWebApp', 'EL_CORE_PROP', 'TEST_SERVICE_RUN_YN', '테스트를 위한 Test 서비스 작동유무', 'Y', '테스트를 위한 Test 서비스 작동유무 ( Y/N )');
Insert into EL_SYS_PROP
   (APP_ID, SYS_GROUP_ID, SYS_KEY, SYS_SUB, SYS_DESC)
 Values
   ('InsWebApp', 'EL_CORE_PROP', 'INPUT_XFORM_RESOLVER_ADAPTER_CLASS_NAME', 'XFORM 입력 구현체 클래스명 ', 'X-Internet 입력 어댑터 ');
   
    
Insert into EL_SYS_PROP
   (APP_ID, SYS_GROUP_ID, SYS_KEY, SYS_SUB, SYS_VAL, SYS_DESC, SORT_SEQ)
 Values
   ('InsWebApp', 'EL_CORE_PROP', 'SCM_CLIENT_CLASS_NAME', '형상관리 연계 구현 클래스명 ', 'com.inswave.elfw.scm.SvnClientHandleAdapter', '형상관리 연계 어댑터 ', 410);
   
Insert into EL_SYS_PROP
   (APP_ID, SYS_GROUP_ID, SYS_KEY, SYS_SUB, SYS_VAL, SYS_DESC, SORT_SEQ)
 Values
   ('InsWebApp', 'EL_CORE_PROP', 'PROJECT_SOURCE_SVN_URL', '형상관리 SVN URL', '', '형상관리 SVN URL (해당 Java 소스가 있는 위치까지 지정한다), 지정된 위치로 파일이 저장되고 이는 소스 분석을 위한 프로젝트 소스위치로 사용된다.', 420);
   
Insert into EL_SYS_PROP
   (APP_ID, SYS_GROUP_ID, SYS_KEY, SYS_SUB, SYS_VAL, SYS_DESC, SORT_SEQ)
 Values
   ('InsWebApp', 'EL_CORE_PROP', 'PROJECT_SOURCE_SVN_USER', '형상관리 SVN USER', '', '형상관리 SVN USER',430);
   

Insert into EL_SYS_PROP
   (APP_ID, SYS_GROUP_ID, SYS_KEY, SYS_SUB, SYS_VAL, SYS_DESC, SORT_SEQ)
 Values
   ('InsWebApp', 'EL_CORE_PROP', 'PROJECT_SOURCE_SVN_PASS', '형상관리 SVN 패스워드', '', '형상관리 SVN 패스워드', 440);
   

Insert into EL_SYS_PROP
   (APP_ID, SYS_GROUP_ID, SYS_KEY, SYS_SUB, SYS_VAL, SYS_DESC,SORT_SEQ)
 Values
   ('InsWebApp', 'EL_CORE_PROP', 'APPLICATION_LOG_SIZE', 'App 로그크기', '100M', '로그파일크기',245);

Insert into EL_SYS_PROP
   (APP_ID, SYS_GROUP_ID, SYS_KEY, SYS_SUB, SYS_VAL, SYS_DESC, SORT_SEQ)
 Values
   ('InsWebApp', 'EL_CORE_PROP', 'SCRN_AUTH_CACHE_SIZE', '화면권한 정보 캐쉬 크리', '10000', '화면권한 정보 캐쉬 크리', 300);
   
     
Insert into EL_SYS_PROP
   (APP_ID, SYS_GROUP_ID, SYS_KEY, SYS_SUB, SYS_VAL, 
    SYS_DESC)
 Values
   ('InsWebApp', 'EL_CORE_PROP', 'DEFAULT_LOG_FORMATTER_NAME', '로그 포멧터 구현 클래스명', 'com.inswave.elfw.log.DefaultAppFormatter', 
    '로그 포멧터 구현 클래스명 - AppLogFormatter 상속받아 구현 ');

    
COMMIT;

/* Add 20190608 */
Insert into EL_SYS_PROP
   (APP_ID, SYS_GROUP_ID, SYS_KEY, SYS_SUB, SYS_VAL, 
    SYS_DESC, SORT_SEQ)
 Values
   ('InsWebApp', 'EL_CORE_PROP', 'IMAGE_LOG_USE_YN', '이미지로그 사용여부', 'N', 
    '이미지로그 사용여부', 300);
Insert into EL_SYS_PROP
   (APP_ID, SYS_GROUP_ID, SYS_KEY, SYS_SUB, SYS_VAL, 
    SYS_DESC, SORT_SEQ)
 Values
   ('InsWebApp', 'EL_CORE_PROP', 'IMAGE_LOG_QUEUE_SIZE', '이미지로그 Queue크기', '10', 
    '이미지로그 Queue크기', 301);
Insert into EL_SYS_PROP
   (APP_ID, SYS_GROUP_ID, SYS_KEY, SYS_SUB, SYS_VAL, 
    SYS_DESC, SORT_SEQ)
 Values
   ('InsWebApp', 'EL_CORE_PROP', 'IMAGE_LOG_TIMER_SEC', '이미지로그 Timer시간(초)', '30', 
    '이미지로그 Timer시간(초)', 302);
Insert into EL_SYS_PROP
   (APP_ID, SYS_GROUP_ID, SYS_KEY, SYS_SUB, SYS_VAL, 
    SYS_DESC, SORT_SEQ)
 Values
   ('InsWebApp', 'EL_CORE_PROP', 'IMAGE_LOG_QUEUE_WARN_SIZE', '이미지로그 Queue 경고 Size', '0', 
    '이미지로그 Queue 경고 Size - 0 이면 동작 안함 ', 303);
Insert into EL_SYS_PROP
   (APP_ID, SYS_GROUP_ID, SYS_KEY, SYS_SUB, SYS_VAL, 
    SYS_DESC, SORT_SEQ)
 Values
   ('InsWebApp', 'EL_CORE_PROP', 'IMAGE_LOG_QUEUE_ERROR_SIZE', '이미지로그 Queue 에러 Size', '0', 
    '이미지로그 Queue 에러 Size - 0 이면 동작 안함 ', 304);
Insert into EL_SYS_PROP
   (APP_ID, SYS_GROUP_ID, SYS_KEY, SYS_SUB, SYS_VAL, 
    SYS_DESC, SORT_SEQ)
 Values
   ('InsWebApp', 'EL_CORE_PROP', 'IMAGE_LOG_PROCESS_CLS_NAME', '이미지로그 구현체 클래스', 'com.inswave.elfw.log.img.DefaultDBImageLogProcess', 
    '이미지로그 구현체 클래스', 305);
Insert into EL_SYS_PROP
   (APP_ID, SYS_GROUP_ID, SYS_KEY, SYS_SUB, SYS_VAL, 
    SYS_DESC, SORT_SEQ)
 Values
   ('InsWebApp', 'EL_CORE_PROP', 'IMAGE_LOGGER_CLS_NAME', '이미지로거 구현체 클래스', 'com.inswave.elfw.log.img.DefaultDBImageLogger', 
    '이미지로거 구현체 클래스 - ElAbstractDBImageLogger 상속 받아 구현해야 함', 306);

Insert into EL_SYS_PROP
   (APP_ID, SYS_GROUP_ID, SYS_KEY, SYS_SUB, SYS_VAL, 
    SYS_DESC, SORT_SEQ)
 Values
   ('InsWebApp', 'EL_CORE_PROP', 'CONTROLLER_TIME_OUT', 'Controller 타임아웃 전역설정 ms', '0', 
    'Controller 타임아웃 전역설정 단위 : ms  (-1 이면 동작 안함 ) ', 260);
       
INSERT INTO EL_SYS_PROP 
  ( APP_ID ,SYS_GROUP_ID ,SYS_KEY ,SYS_SUB ,SYS_VAL ,SYS_DESC ,SORT_SEQ )  
values  
  ( 'InsWebApp' ,'EL_CORE_PROP' ,'CRYPTO_CASS_NAME' ,'암호화 구현 클래스' ,'' ,'암호화 구현 클래스로 프레임워크의 추상클래스인 ElAbstractCrypto 을 상혹 받아 getEncrypt, getDecrypt 의 각 메소드를 구현' ,270 ) ;

Insert into EL_SYS_PROP
   (APP_ID, SYS_GROUP_ID, SYS_KEY, SYS_SUB, SYS_VAL, SYS_DESC, SORT_SEQ)
 Values
   ('InsWebApp', 'EL_CORE_PROP', 'FRAMEWORK_LOGIN_CHECK_YN', '프레임워크 계정 ID 로그인 체크 여부', 'N', '프레임워크 계정 ID 로그인 체크 여부 ( Y/N ), N로 설정시에 권한체크여부가 false 로 되어 있어야 함', 310);
   
COMMIT;
/* Add 20190608 */

Insert into EL_SYS_PROP
   (APP_ID, SYS_GROUP_ID, SYS_KEY, SYS_SUB, SYS_VAL, SYS_DESC, SORT_SEQ)
 Values
   ('InsWebApp', 'EL_AUTH_CD', 'A', '전체권한', '전체', '전체권한', 10);
Insert into EL_SYS_PROP
   (APP_ID, SYS_GROUP_ID, SYS_KEY, SYS_SUB, SYS_VAL, SYS_DESC, SORT_SEQ)
 Values
   ('InsWebApp', 'EL_AUTH_CD', 'P', '출력권한', '출력', '출력권한', 30);
Insert into EL_SYS_PROP
   (APP_ID, SYS_GROUP_ID, SYS_KEY, SYS_SUB, SYS_VAL, SYS_DESC, SORT_SEQ)
 Values
   ('InsWebApp', 'EL_AUTH_CD', 'R', '읽기권한', '읽기', '읽기권한', 20);
COMMIT;


/* Add 20200312 */
Insert into EL_SYS_PROP
   (APP_ID, SYS_GROUP_ID, SYS_KEY, SYS_SUB, SYS_VAL, 
    SYS_DESC, SORT_SEQ)
 Values
   ('InsWebApp', 'EL_CORE_PROP', 'EL_HEALTH_DB_CHECK_QUERY', 'DB Heal 체크 쿼리', '', 
    'DB Heal 체크 쿼리 - Oracle : SELECT 1 as msg FROM DUAL', 500);
COMMIT;



Insert into EL_SYS_PROP
   (APP_ID, SYS_GROUP_ID, SYS_KEY, SYS_SUB, SYS_VAL, 
    SYS_DESC)
 Values
   ('InsWebApp', 'EL_CORE_PROP', 'FLD_SERVICE_DB_BASE_YN', 'EL_SVC 필드 추가에 따른 기본 욥션', 'Y', 
    'EL_SVC 테이블 필드 추가에 따른 옵션 기본 설정 - 신규 사이트 적용 필수');
COMMIT;    

Insert into EL_SYS_PROP
   (APP_ID, SYS_GROUP_ID, SYS_KEY, SYS_SUB, SYS_VAL, 
    SYS_DESC)
 Values
   ('InsWebApp', 'EL_CORE_PROP', 'EL_SERVICE_RETURN_VO_USED_YN', '컨트로러의 리턴 VO 지원여부', 'Y', 
    '컨트로러의 리턴 VO 지원여부 ');
COMMIT;

Insert into EL_SYS_PROP
   (APP_ID, SYS_GROUP_ID, SYS_KEY, SYS_SUB, SYS_VAL, 
    SYS_DESC, SORT_SEQ)
 Values
   ('InsWebApp', 'EL_CORE_PROP', 'OPEN_API_HANDLE_CLASS_NAME', '오픈API Handle 구현체 클래스', '', 
    '오픈API Handle 구현체 클래스', 311);

Insert into EL_SYS_PROP
   (APP_ID, SYS_GROUP_ID, SYS_KEY, SYS_SUB, SYS_VAL, 
    SYS_DESC, SORT_SEQ)
 Values
   ('InsWebApp', 'EL_CORE_PROP', 'EL_SERVICE_BASE_LOG_USE_YN', '서비스별 로그 사용여부', 'N', 
    '서비스별 로그 사용여부 - 개발 서버만 적용 ', 236);
COMMIT;

INSERT INTO EL_SYS_PROP  ( APP_ID ,SYS_GROUP_ID ,SYS_KEY ,SYS_SUB ,SYS_VAL ,SYS_DESC ,SORT_SEQ )  
values  
( 'InsWebApp' ,'EL_CORE_PROP' ,'SVC_INFO_BASE_URL' ,'서비스 입출럭 정보를 위한 Base Url' 
,'http://localhost:8080/InsWebApp' ,'서비스 입출럭 정보를 위한 Base Url' ,300 ) ;
COMMIT;


Insert into EL_SYS_PROP
   (APP_ID, SYS_GROUP_ID, SYS_KEY, SYS_SUB, SYS_VAL, 
    SYS_DESC, SORT_SEQ)
 Values
   ('InsWebApp', 'EL_CORE_PROP', 'EL_HTTP_BODY_ALWAYS', 'HttpBody 모드만 항상 사용', 'Y', 
    'HttpBody 모드만 항상 사용', 390);
 COMMIT;
 

 Insert into EL_SYS_PROP
   (APP_ID, SYS_GROUP_ID, SYS_KEY, SYS_SUB, SYS_VAL, 
    SYS_DESC)
 Values
   ('InsWebApp', 'EL_CORE_PROP', 'QUERY_TIMEOUT_FOR_SVC_TIMEOUT', '서비스 타임아웃을 위한 쿼리 타임아웃 설정여부', 'N', 
    '서비스 타임아웃을 위한 쿼리 타임아웃 설정여부');
 COMMIT;

/* 20210630 */
Insert into EL_SYS_PROP
   (APP_ID, SYS_GROUP_ID, SYS_KEY, SYS_SUB, SYS_VAL, 
    SYS_DESC, SORT_SEQ)
 Values
   ('InsWebApp', 'EL_CORE_PROP', 'EL_JSON_PROPERTY_NAMING_STRATEGY_CLASS_NAME', 'UI 요청/응답 Json 처리시 VO 바인딩 처리를 위한 규칙을 적용하기 위한 클래스명 구현체', 'com.inswave.elfw.databind.DefaultPropertyNamingStrategy', 
    'Json 변환 필드 명칭 처리 클래스명 Annotation사용 - com.inswave.elfw.databind.DefaultPropertyNamingStrategy', 350);


Insert into EL_SYS_PROP
   (APP_ID, SYS_GROUP_ID, SYS_KEY, SYS_SUB, SYS_VAL, 
    SYS_DESC, SORT_SEQ)
 Values
   ('InsWebApp', 'EL_CORE_PROP', 'EL_INPUT_JSON_FAIL_ON_UNKNOWN_PROPERTIES_YN', 'UI Json 요청시 VO 에 정의되지 않은 항목 에러 체크 여부', 'N', 
    'UI Json 요청시 VO 에 정의되지 않은 항목 에러 체크 여부', 350);

/* 20220224 */
Insert into EL_SYS_PROP
   (APP_ID, SYS_GROUP_ID, SYS_KEY, SYS_SUB, SYS_VAL, 
    SYS_DESC, SORT_SEQ)
 Values
   ('InsWebApp', 'EL_CORE_PROP', 'EL_CTRL_NO_PARAM_NORMAL_DATA_YN', 'Controller 파라미터 없는 경우 정상 포멧으로 데이터 처리 여부', 'N', 
    'Controller 파라미터 없는 경우 정상 포멧으로 데이터 처리 여부 : 기본값 N 인경우 데이터부만 내려감 - 이전 사이트 호환성', 400);
    
Insert into EL_SYS_PROP
	(APP_ID, SYS_GROUP_ID, SYS_KEY, SYS_SUB, SYS_VAL, SYS_DESC, SORT_SEQ)
 Values
 	('InsWebApp', 'EL_CORE_PROP', 'REGSIST_SERVICE_CHECK_YN', '서비스체크사용유무', 'Y', '서비스체크사용유무', 501);
 	
/* 20231115 */
/*
INSERT INTO EL_SYS_PROP
(APP_ID, SYS_GROUP_ID, SYS_KEY, SYS_SUB, SYS_VAL, SYS_DESC, SORT_SEQ)
VALUES('InsWebApp', 'EL_CORE_PROP', 'KAFKA_ADAPTER_CLASS_NAME', '카프카 어댑터 구현체 클래스명', 'com.inswave.elfw.kafka.adapter.DefaultKafkaHandlerAdapter', '카프카 어댑터 클래스명', NULL);

INSERT INTO EL_SYS_PROP
(APP_ID, SYS_GROUP_ID, SYS_KEY, SYS_SUB, SYS_VAL, SYS_DESC, SORT_SEQ)
VALUES('InsWebApp', 'EL_CORE_PROP', 'KAFKA_BROCKING_TIME_OUT', '카프카 블로킹 타임아웃 시간', '10', '카프카 브로커 타임아웃 시간(초)', NULL);

INSERT INTO EL_SYS_PROP
(APP_ID, SYS_GROUP_ID, SYS_KEY, SYS_SUB, SYS_VAL, SYS_DESC, SORT_SEQ)
VALUES('InsWebApp', 'EL_CORE_PROP', 'KAFKA_PROCESS_ADAPTER_CLASS_NAME', '카프카 프로세스 어댑터 구현체 클래스명', 'com.inswave.elfw.kafka.adapter.process.DefaultKafkaProcessAdapter', '카프카 프로세스 어댑터 구현체 클래스명', NULL);

INSERT INTO EL_SYS_PROP
(APP_ID, SYS_GROUP_ID, SYS_KEY, SYS_SUB, SYS_VAL, SYS_DESC, SORT_SEQ)
VALUES('InsWebApp', 'EL_CORE_PROP', 'KAFKA_IMAGE_LOG_PROCESS_CLS_NAME', '카프카 이미지 로그 프로세스', 'com.inswave.elfw.kafka.img.DafaultKafkaImageLogProcess', '카프카 이미지 로그 프로세스', NULL);

INSERT INTO EL_SYS_PROP
(APP_ID, SYS_GROUP_ID, SYS_KEY, SYS_SUB, SYS_VAL, SYS_DESC, SORT_SEQ)
VALUES('InsWebApp', 'EL_CORE_PROP', 'KAFKA_BOOTSTRAP_SERVERS', '카프카 접속 정보', '192.168.100.186:29092,192.168.100.186:39092,192.168.100.186:49092', '카프카 접속 정보', NULL);

INSERT INTO EL_SYS_PROP
(APP_ID, SYS_GROUP_ID, SYS_KEY, SYS_SUB, SYS_VAL, SYS_DESC, SORT_SEQ)
VALUES('InsWebApp', 'EL_CORE_PROP', 'KAFKA_CONSUMER_GROUPID', '카프카 기본 리스너 그룹 아이디', 'listenMessage', '카프카 기본 리스너 그룹 아이디', NULL);

INSERT INTO EL_SYS_PROP
(APP_ID, SYS_GROUP_ID, SYS_KEY, SYS_SUB, SYS_VAL, SYS_DESC, SORT_SEQ)
VALUES('InsWebApp', 'EL_CORE_PROP', 'KAFKA_TOPICS', '카프카 토픽 목록', 'producerTest_INFID_FLD,DmoEmpList_INFIDLIST_FLD', '카프카 토픽 목록', NULL);

INSERT INTO sp1_el_core.el_sys_prop
(APP_ID, SYS_GROUP_ID, SYS_KEY, SYS_SUB, SYS_VAL, SYS_DESC, SORT_SEQ)
VALUES('InsWebApp', 'EL_CORE_PROP', 'KAFKA_CONSUMER_BASE_URL', '카프카 컨슈머 URL', 'http://localhost:8080/InsWebApp/', '카프카 컨슈머 URL', NULL);
*/
COMMIT;
 
INSERT INTO EL_SVC 
	( APP_ID ,SVC_ID ,CLASS_NM ,METHOD_NM ,SVC_SUB ,SVC_DESC ,DEL_YN ,REQ_URL ,IN_INF_ID ,OUT_INF_ID )  
VALUES  
	( 'InsWebApp' ,'CmmLogin' ,'com.demo.proworks.emp.web.EmpController' ,
		'login(com.demo.proworks.emp.vo.LoginVo,javax.servlet.http.HttpServletRequest)' ,
		'로그인 ' ,'로그인을 처리한다.' ,'N' ,
		'CmmLogin' ,'com.demo.proworks.emp.vo.LoginVo' ,null ) ;

INSERT INTO EL_SVC 
	( APP_ID ,SVC_ID ,CLASS_NM ,METHOD_NM ,SVC_SUB ,SVC_DESC ,DEL_YN ,REQ_URL ,IN_INF_ID ,OUT_INF_ID )  
VALUES  
	( 'InsWebApp' ,'CmmLoginFrm' ,'com.demo.proworks.emp.web.EmpController' ,
		'loginFrm(com.demo.proworks.emp.vo.LoginVo,javax.servlet.http.HttpServletRequest)' ,
		'로그인 폼 ' ,'로그인 폼페이지를 로드한다.' ,'N' ,
		'CmmLoginFrm' ,'com.demo.proworks.emp.vo.LoginVo' ,null ) ;

INSERT INTO EL_SVC 
	( APP_ID ,SVC_ID ,CLASS_NM ,METHOD_NM ,SVC_SUB ,SVC_DESC ,DEL_YN ,REQ_URL ,IN_INF_ID ,OUT_INF_ID )  
VALUES  
	( 'InsWebApp' ,'DmoEmpDel' ,'com.demo.proworks.emp.web.EmpController' ,
		'deleteEmp(com.demo.proworks.emp.vo.EmpVo)' ,
		'사원정보삭제' ,'사원정보를 삭제 처리한다.' ,'N' ,
		'DmoEmpDel' ,'com.demo.proworks.emp.vo.EmpVo' ,null ) ;

INSERT INTO EL_SVC 
	( APP_ID ,SVC_ID ,CLASS_NM ,METHOD_NM ,SVC_SUB ,SVC_DESC ,DEL_YN ,REQ_URL ,IN_INF_ID ,OUT_INF_ID )  
VALUES  
	( 'InsWebApp' ,'DmoEmpDown' ,'com.demo.proworks.emp.web.EmpController' ,
		'downEmpFile(com.demo.proworks.emp.vo.EmpVo,javax.servlet.http.HttpServletResponse)' ,
		'사원 첨부파일 다운로드' , '사원 첨부파일을 다운로드한다.' ,'N' ,
		'DmoEmpDown' ,'com.demo.proworks.emp.vo.EmpVo' ,null ) ;

INSERT INTO EL_SVC 
	( APP_ID ,SVC_ID ,CLASS_NM ,METHOD_NM ,SVC_SUB ,SVC_DESC ,DEL_YN ,REQ_URL ,IN_INF_ID ,OUT_INF_ID )  
VALUES  
	( 'InsWebApp' ,'DmoEmpIns' ,'com.demo.proworks.emp.web.EmpController' ,
		'addEmp(com.demo.proworks.emp.vo.EmpVo)' ,
		'사원등록처리' ,'사원정보를 등록 처리 한다.' ,'N' ,
		'DmoEmpIns' ,'com.demo.proworks.emp.vo.EmpVo' ,'com.demo.proworks.emp.vo.DeptListVo' ) ;

INSERT INTO EL_SVC 
	( APP_ID ,SVC_ID ,CLASS_NM ,METHOD_NM ,SVC_SUB ,SVC_DESC ,DEL_YN ,REQ_URL ,IN_INF_ID ,OUT_INF_ID )  
VALUES  
	( 'InsWebApp' ,'DmoEmpList' ,'com.demo.proworks.emp.web.EmpController' ,
		'selectEmpList(com.demo.proworks.emp.vo.EmpVo)' ,
		'사원목록조회' ,'페이징을 처리하여 사원목록 조회를 한다.' ,'N' ,
		'DmoEmpList' ,'com.demo.proworks.emp.vo.EmpVo' ,'com.demo.proworks.emp.vo.EmpListVo' ) ;

INSERT INTO EL_SVC 
	( APP_ID ,SVC_ID ,CLASS_NM ,METHOD_NM ,SVC_SUB ,SVC_DESC ,DEL_YN ,REQ_URL ,IN_INF_ID ,OUT_INF_ID )  
VALUES  
	( 'InsWebApp' ,'DmoEmpSelect' ,'com.demo.proworks.emp.web.EmpController' ,
		'selectEmpView(com.demo.proworks.emp.vo.EmpVo)' ,
		'사원정보 상세조회 (TCP 내부 테스트용)' ,'사원정보 상세조회 (TCP 내부 테스트용)를 한다.' ,'N' ,
		'DmoEmpSelect' ,'com.demo.proworks.emp.vo.EmpVo' ,'com.demo.proworks.emp.vo.EmpVo' ) ;

INSERT INTO EL_SVC 
	( APP_ID ,SVC_ID ,CLASS_NM ,METHOD_NM ,SVC_SUB ,SVC_DESC ,DEL_YN ,REQ_URL ,IN_INF_ID ,OUT_INF_ID )  
VALUES  
	( 'InsWebApp' ,'DmoEmpUpd' ,'com.demo.proworks.emp.web.EmpController' ,
		'updateEmp(com.demo.proworks.emp.vo.EmpVo)' ,
		'사원목록갱신처리' ,'사원정보를 갱신 처리 한다.' ,'N' ,
		null ,'com.demo.proworks.emp.vo.EmpVo' ,'com.demo.proworks.emp.vo.DeptListVo' ) ;

INSERT INTO EL_SVC 
	( APP_ID ,SVC_ID ,CLASS_NM ,METHOD_NM ,SVC_SUB ,SVC_DESC ,DEL_YN ,REQ_URL ,IN_INF_ID ,OUT_INF_ID )  
values  
	( 'InsWebApp' ,'DmoEmpUpdView' ,'com.demo.proworks.emp.web.EmpController' ,
		'updateEmpView(com.demo.proworks.emp.vo.EmpVo)' ,
		'사원정보 갱신 폼을 위한 조회' ,'사원정보 갱신 폼을 위한 조회를 한다.' ,'N' ,
		'DmoEmpUpdView' ,'com.demo.proworks.emp.vo.EmpVo' ,'com.demo.proworks.emp.vo.EmpListVo' ) ;

INSERT INTO EL_SVC 
	( APP_ID ,SVC_ID ,CLASS_NM ,METHOD_NM ,SVC_SUB ,SVC_DESC ,DEL_YN ,REQ_URL ,IN_INF_ID ,OUT_INF_ID )  
VALUES  
	( 'InsWebApp' ,'DmoEmpUpdateUploadEmpFile' ,'com.demo.proworks.emp.web.EmpController' ,
		'updateUploadEmpFile(javax.servlet.http.HttpServletRequest,com.demo.proworks.emp.vo.EmpVo,org.springframework.ui.Model)' ,
		'파일업로드 수정' ,'파일 업로드를 수정 처리 한다.' ,'N' ,
		'DmoEmpUpdateUploadEmpFile' ,'com.demo.proworks.emp.vo.EmpVo' ,null ) ;

INSERT INTO EL_SVC 
	( APP_ID ,SVC_ID ,CLASS_NM ,METHOD_NM ,SVC_SUB ,SVC_DESC ,DEL_YN ,REQ_URL ,IN_INF_ID ,OUT_INF_ID )  
VALUES  
	( 'InsWebApp' ,'DmoEmpUploadEmpFile' ,'com.demo.proworks.emp.web.EmpController' ,
		'uploadEmpFileToDb(javax.servlet.http.HttpServletRequest,com.demo.proworks.emp.vo.EmpVo,org.springframework.ui.Model)' ,
		'사원 첨부파일 업로드' ,'사원 첨부파일을 업로드한다.' ,'N' ,
		'DmoEmpUploadEmpFile' ,'com.demo.proworks.emp.vo.EmpVo' ,null ) ;

INSERT INTO EL_SVC 
	( APP_ID ,SVC_ID ,CLASS_NM ,METHOD_NM ,SVC_SUB ,SVC_DESC ,DEL_YN ,REQ_URL ,IN_INF_ID ,OUT_INF_ID )  
VALUES  
	( 'InsWebApp' ,'DmoEmpView' ,'com.demo.proworks.emp.web.EmpController' ,
		'addEmpView(com.demo.proworks.emp.vo.EmpVo)' ,
		'사원등록 폼을 위한 조회' ,'사원등록 폼을 위한 조회 처리를 한다.' ,'N' ,
		'DmoEmpView' ,'com.demo.proworks.emp.vo.EmpVo' ,'com.demo.proworks.emp.vo.DeptListVo' ) ;

INSERT INTO EL_SVC 
	( APP_ID ,SVC_ID ,CLASS_NM ,METHOD_NM ,SVC_SUB ,SVC_DESC ,DEL_YN ,REQ_URL ,IN_INF_ID ,OUT_INF_ID )  
VALUES  
	( 'InsWebApp' ,'DmoTcpSendTest' ,'com.demo.proworks.emp.web.EmpController' ,
		'tcpSendTest(com.demo.proworks.emp.vo.EmpVo)' ,
		'TCP 송신 테스트' ,'TCP 송신 테스트를 한다.' ,'N' ,
		'DmoTcpSendTest' ,'com.demo.proworks.emp.vo.EmpVo' ,'com.demo.proworks.emp.vo.EmpVo' ) ;


INSERT INTO EL_SVC_CTR 
	( APP_ID ,SVC_ID ,AUTH_CHECK_YN ,SESSION_CHECK_YN ,XML_SVC_YN ,JSON_SVC_YN ,
		LOCK_YN ,ENABLE_START_TIME ,ENABLE_END_TIME ,SVC_PRE_POST_CLASS ,LOG_LEVEL ,
		FLD_SVC_YN ,SVC_TIME_OUT ,SVC_THREAD_MAX ,OPEN_API_USE_YN )  
VALUES  
	( 'InsWebApp' ,'CmmLogin' ,null ,'N' ,null ,null ,null ,null ,null ,null ,null ,null ,null ,null ,null ) ;

INSERT INTO EL_SVC_CTR 
	( APP_ID ,SVC_ID ,AUTH_CHECK_YN ,SESSION_CHECK_YN ,XML_SVC_YN ,JSON_SVC_YN ,
		LOCK_YN ,ENABLE_START_TIME ,ENABLE_END_TIME ,SVC_PRE_POST_CLASS ,LOG_LEVEL ,
		FLD_SVC_YN ,SVC_TIME_OUT ,SVC_THREAD_MAX ,OPEN_API_USE_YN )  
VALUES  
	( 'InsWebApp' ,'CmmLoginFrm' ,null ,'N' ,null ,null ,null ,null ,null ,null ,null ,null ,null ,null ,null ) ;

INSERT INTO EL_SVC_CTR 
	( APP_ID ,SVC_ID ,AUTH_CHECK_YN ,SESSION_CHECK_YN ,XML_SVC_YN ,JSON_SVC_YN ,
		LOCK_YN ,ENABLE_START_TIME ,ENABLE_END_TIME ,SVC_PRE_POST_CLASS ,LOG_LEVEL ,
		FLD_SVC_YN ,SVC_TIME_OUT ,SVC_THREAD_MAX ,OPEN_API_USE_YN )  
VALUES  
	( 'InsWebApp' ,'DmoEmpSelect' ,null ,'N' ,null ,null ,null ,null ,null ,null ,null ,null ,null ,null ,null ) ;

INSERT INTO EL_SVC_CTR 
	( APP_ID ,SVC_ID ,AUTH_CHECK_YN ,SESSION_CHECK_YN ,XML_SVC_YN ,JSON_SVC_YN ,
		LOCK_YN ,ENABLE_START_TIME ,ENABLE_END_TIME ,SVC_PRE_POST_CLASS ,LOG_LEVEL ,
		FLD_SVC_YN ,SVC_TIME_OUT ,SVC_THREAD_MAX ,OPEN_API_USE_YN )  
VALUES  
	( 'InsWebApp' ,'DmoTcpSendTest' ,null ,'N' ,null ,null ,null ,null ,null ,null ,null ,null ,null ,null ,null ) ;

INSERT INTO EL_SVC 
	( APP_ID ,SVC_ID ,CLASS_NM ,METHOD_NM ,SVC_SUB ,SVC_DESC ,DEL_YN ,REQ_URL ,IN_INF_ID ,OUT_INF_ID )  
VALUES  
	( 'InsWebApp' ,'DmoEmpUpdViewJsp' ,'com.demo.proworks.emp.web.EmpController' ,
		'updateEmpViewJsp(com.demo.proworks.emp.vo.EmpVo)' ,
		'사원정보 갱신 폼을 위한 조회 JSP ' ,'사원정보 갱신 폼을 위한 조회 JSP 를 한다.' ,'N' ,
		'DmoEmpUpdView' ,'com.demo.proworks.emp.vo.EmpVo' ,'com.demo.proworks.emp.vo.EmpListVo' ) ;
		
COMMIT;
