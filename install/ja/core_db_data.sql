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


/* 以下のEL_APP情報はシステムの状況に応じて修正が必要 */
Insert into EL_APP
   (APP_ID, APP_NAME, APP_ORDER, JAVA_HOME, JAVAC_OPTION, 
    APP_JAR_DIR, APP_CLASS_PATH)
 Values
   ('InsWebApp', 'InsWebApp_WebApplication', 100, '/opt/jdk-11.0.15-ojdkbuild-linux-x64', '-encoding UTF-8 -g', 
    '/home/proworks/context/InsWebApp/WEB-INF/lib:/home/proworks/context/InsWebApp/WEB-INF/lib-provided', '/home/proworks/context/InsWebApp/WEB-INF/classes');
   
/* 以下のEL_APP_CMT_DEPL情報はシステムの状況に応じて修正が必要 - 最低1つの基本Copy配布が必要   */
Insert into EL_APP_CMT_DEPL
   (APP_ID, APP_DEPL_ID, APP_DEPL_GBN, APP_DEPL_NAME, APP_DEPL_CLASS, 
    APP_DEPL_YN, DEPL_SRC_PATH, DEPL_RES_PATH, DEPL_CLS_PATH, DEPL_WEB_PATH)
 Values
('InsWebApp', 'InsWebApp_node1', 'DEV', 'InsWebApp_WebApplication_基本ノード', 'com.inswave.elfw.deploy.DefaultDeployCopyAdapter',
    'Y', '/home/proworks/resources/InsWebApp', '/home/proworks/context/InsWebApp/WEB-INF/classes', '/home/proworks/context/InsWebApp/WEB-INF/classes', '/home/proworks/context/InsWebApp');
    
   
COMMIT;

Insert into EL_NOTIFY
   (APP_ID, SERVER_NO, NOTIFY_BASE_URL, USED_YN, SERVER_ID)
 Values
   ('InsWebApp', 1, 'http://localhost:8080/InsWebApp', 'Y', 'InsWebApp');



Insert into EL_SYS_PROP
   (APP_ID, SYS_GROUP_ID, SYS_KEY, SYS_SUB, SYS_VAL, SYS_DESC, SORT_SEQ)
 Values
('InsWebApp', 'EL_CORE_PROP', 'SERVER_MODE', 'サーバーモード', 'DEV', 'サーバーモード(DEV、TST、RUN) - 開発モードはValidatorがリアルタイムで反映、F/W用サーバーモード、業務用は新規作成を推奨', 100);
Insert into EL_SYS_PROP
   (APP_ID, SYS_GROUP_ID, SYS_KEY, SYS_SUB, SYS_VAL, SYS_DESC, SORT_SEQ)
 Values
('InsWebApp', 'EL_CORE_PROP', 'CONTROLLER_VALIDATOR_USE_YN', 'Validatorの使用有無', 'Y', 'Validatorの使用有無 ( Y / N )', 300);
Insert into EL_SYS_PROP
   (APP_ID, SYS_GROUP_ID, SYS_KEY, SYS_SUB, SYS_VAL, SYS_DESC)
 Values
('InsWebApp', 'EL_CORE_PROP', 'APPLICATION_LOG_FACTORY_IMPL', 'Appログ実装クラス', 'com.inswave.elfw.log.DefaultApplicationLogFactoryImpl', 'Log Factory実装（カスタマイズ可能）');
Insert into EL_SYS_PROP
   (APP_ID, SYS_GROUP_ID, SYS_KEY, SYS_SUB, SYS_VAL, SYS_DESC, SORT_SEQ)
 Values
('InsWebApp', 'EL_CORE_PROP', 'APPLICATION_LOG_DIR', 'Appログ保存場所', '/home/proworks/logs/InsWebApp', 'ログファイルの場所', 210);
Insert into EL_SYS_PROP
   (APP_ID, SYS_GROUP_ID, SYS_KEY, SYS_SUB, SYS_VAL, SYS_DESC)
 Values
('InsWebApp', 'EL_CORE_PROP', 'APPLICATION_LOG_SEND_TARGET_LOGGER_NAME', 'Appログを外部に送信する名前', 'elfw.appLogger', 'AppLogを外部Appenderに送信する名前 - 存在しない場合は送信されません');
Insert into EL_SYS_PROP
   (APP_ID, SYS_GROUP_ID, SYS_KEY, SYS_SUB, SYS_DESC)
 Values
('InsWebApp', 'EL_CORE_PROP', 'APPLICATION_LOG_APPEND_LOGGERS', 'APPログに追加する外部ロガー名', 'インポートする外部ロガー（,で区切る）- 外部ロガーの設定を強制的に変更する場合、ロガーの後にレベルを指定する。例：java.sql:ERROR');
Insert into EL_SYS_PROP
   (APP_ID, SYS_GROUP_ID, SYS_KEY, SYS_SUB, SYS_VAL, SYS_DESC)
 Values
('InsWebApp', 'EL_CORE_PROP', 'LOG4J_LOADING_BASE_FILE', 'ロードするlog4jファイル名', 'log4j.xml', 'ロードするlog4j.xml - クラスパスに直接存在する必要がある');
Insert into EL_SYS_PROP
   (APP_ID, SYS_GROUP_ID, SYS_KEY, SYS_SUB, SYS_VAL, SYS_DESC)
 Values
('InsWebApp', 'EL_CORE_PROP', 'USER_HEADER_CLASS_NAME', 'ユーザーヘッダー実装クラス名', 'com.demo.proworks.cmmn.ProworksUserHeader', 'ユーザーHeaderクラス名');
Insert into EL_SYS_PROP
   (APP_ID, SYS_GROUP_ID, SYS_KEY, SYS_SUB, SYS_VAL, SYS_DESC)
 Values
('InsWebApp', 'EL_CORE_PROP', 'LOGIN_ADAPTER_CLASS_NAME', 'ログインアダプター実装クラス名', 'com.demo.proworks.cmmn.ProworksLoginAdapter', 'ログインアダプタークラス名');
Insert into EL_SYS_PROP
   (APP_ID, SYS_GROUP_ID, SYS_KEY, SYS_SUB, SYS_VAL, SYS_DESC)
 Values
('InsWebApp', 'EL_CORE_PROP', 'SESSION_ADAPTER_CLASS_NAME', 'セッションアダプター実装クラス名', 'com.inswave.elfw.session.HttpSessionAdapter', 'セッションのストレージアダプタークラス名');
Insert into EL_SYS_PROP
   (APP_ID, SYS_GROUP_ID, SYS_KEY, SYS_SUB, SYS_VAL, SYS_DESC)
 Values
('InsWebApp', 'EL_CORE_PROP', 'SESSION_DATA_ADAPTER_CLASS_NAME', 'セッションデータアダプタ実装クラス名', 'com.demo.proworks.cmmn.ProworksSessionDataAdapter', 'セッションに保存するデータアダプタクラス名');
Insert into EL_SYS_PROP
   (APP_ID, SYS_GROUP_ID, SYS_KEY, SYS_SUB, SYS_VAL, SYS_DESC)
 Values
('InsWebApp', 'EL_CORE_PROP', 'HEADER_SET_ADAPTER_CLASS_NAME', 'ヘッダー設定アダプター実装クラス名', 'com.demo.proworks.cmmn.ProworksUserHeaderSetAdapter', 'ヘッダー設定アダプタークラス名');
   
Insert into EL_SYS_PROP
   (APP_ID, SYS_GROUP_ID, SYS_KEY, SYS_SUB, SYS_VAL, SYS_DESC, SORT_SEQ)
 Values
('InsWebApp', 'EL_CORE_PROP', 'OUTPUT_XML_DATA_SERVICE_YN', 'XML入出力使用有無', 'Y', 'XML Output サービス全体基本設定', 330);
Insert into EL_SYS_PROP
   (APP_ID, SYS_GROUP_ID, SYS_KEY, SYS_SUB, SYS_VAL, SYS_DESC, SORT_SEQ)
 Values
('InsWebApp', 'EL_CORE_PROP', 'OUTPUT_JSON_DATA_SERVICE_YN', 'JSON入出力使用有無', 'Y', 'JSON Output サービス全体基本設定', 340);

Insert into EL_SYS_PROP
   (APP_ID, SYS_GROUP_ID, SYS_KEY, SYS_SUB, SYS_VAL, SYS_DESC, SORT_SEQ)
 Values
('InsWebApp', 'EL_CORE_PROP', 'OUTPUT_FLD_DATA_SERVICE_YN', 'FLD入出力使用有無', 'Y', 'FLD入出力使用有無', 350);
   
   
Insert into EL_SYS_PROP
   (APP_ID, SYS_GROUP_ID, SYS_KEY, SYS_SUB, SYS_VAL, SYS_DESC)
 Values
('InsWebApp', 'EL_CORE_PROP', 'INPUT_XML_RESOLVER_ADAPTER_CLASS_NAME', 'XML入出力実装クラス名', 'com.inswave.elfw.resolver.DefaultXmlAgumentResolver2Adapter', '存在する場合、XML OUTサービス時にInputをHttp Requestではなくxmlデータとして受け取る。（requestパラメータのdataフィールドに文字列として受け取る。）');
Insert into EL_SYS_PROP
   (APP_ID, SYS_GROUP_ID, SYS_KEY, SYS_SUB, SYS_VAL, SYS_DESC)
 Values
('InsWebApp', 'EL_CORE_PROP', 'INPUT_JSON_RESOLVER_ADAPTER_CLASS_NAME', 'JSON入出力実装クラス名', 'com.inswave.elfw.resolver.DefaultJsonAgumentResolverAdapter', '存在する場合、JSON OUT サービス時にInputをHttp Requestではなくjsonデータとして受け取る。（requestパラメータのdataフィールドに文字列として受け取る。）');
Insert into EL_SYS_PROP
   (APP_ID, SYS_GROUP_ID, SYS_KEY, SYS_SUB, SYS_VAL, SYS_DESC, SORT_SEQ)
 Values
('InsWebApp', 'EL_CORE_PROP', 'AUTH_CHECK_YN', '全体権限チェックの使用有無', 'N', '全体コントローラーでの権限チェック', 310);
Insert into EL_SYS_PROP
   (APP_ID, SYS_GROUP_ID, SYS_KEY, SYS_SUB, SYS_VAL, SYS_DESC, SORT_SEQ)
 Values
('InsWebApp', 'EL_CORE_PROP', 'SESSION_CHECK_YN', '全体セッションチェックの使用有無', 'Y', '全コントローラーでのセッションチェック', 320);

Insert into EL_SYS_PROP
   (APP_ID, SYS_GROUP_ID, SYS_KEY, SYS_SUB, SYS_VAL, SYS_DESC, SORT_SEQ)
 Values
('InsWebApp', 'EL_CORE_PROP', 'FRAMEWORK_FORCE_SESSION_CHECK_YN', 'フレームワーク強制セッションチェック有無', 'Y', 'フレームワーク強制セッションチェック有無', 321);
    
Insert into EL_SYS_PROP
   (APP_ID, SYS_GROUP_ID, SYS_KEY, SYS_SUB, SYS_VAL, SYS_DESC)
 Values
('InsWebApp', 'EL_CORE_PROP', 'AUTH_ADAPTER_CLASS_NAME', '権限チェック実装クラス名', 'com.inswave.elfw.auth.DefaultAuthAdapter', '権限チェックを担当するアダプタクラス名');
Insert into EL_SYS_PROP
   (APP_ID, SYS_GROUP_ID, SYS_KEY, SYS_SUB, SYS_VAL, SYS_DESC)
 Values
('InsWebApp', 'EL_CORE_PROP', 'SCRN_AUTH_ADAPTER_CLASS_NAME', '画面権限チェック実装クラス名', 'com.inswave.elfw.auth.DefaultScrnAuthAdapter', '画面権限チェックを担当するアダプタクラス名');
   
Insert into EL_SYS_PROP
   (APP_ID, SYS_GROUP_ID, SYS_KEY, SYS_SUB, SYS_VAL, SYS_DESC)
 Values
('InsWebApp', 'EL_CORE_PROP', 'SCRN_ID_ADAPTER_CLASS_NAME', '画面ID実装クラス名', 'com.inswave.elfw.auth.DefaultScrnIdAdapter', '画面IDを担当するアダプタクラス名');
   
   
Insert into EL_SYS_PROP
   (APP_ID, SYS_GROUP_ID, SYS_KEY, SYS_SUB, SYS_VAL, SYS_DESC, SORT_SEQ)
 Values
('InsWebApp', 'EL_CORE_PROP', 'APPLICATION_LOG_LEVEL', 'Appログレベル', 'DEBUG', 'ログレベル（appログのみ設定） - 外部ロガーの場合は外部ロガーの設定に従う。', 230);

Insert into EL_SYS_PROP
   (APP_ID, SYS_GROUP_ID, SYS_KEY, SYS_SUB, SYS_VAL, SYS_DESC)
 Values
('InsWebApp', 'EL_CORE_PROP', 'SPRING_CONFIG_BASE_CLASSPATH', 'Spring設定ファイルの場所', 'inswave/spring', 'Spring設定ファイルの場所（Validator Managerで使用）');
Insert into EL_SYS_PROP
   (APP_ID, SYS_GROUP_ID, SYS_KEY, SYS_SUB, SYS_VAL, SYS_DESC, SORT_SEQ)
 Values
('InsWebApp', 'EL_CORE_PROP', 'SQL_LOG_LINE_FEED_YN', 'SQLログの改行使用有無', 'Y', 'SQLログの改行処理有無', 250);
Insert into EL_SYS_PROP
   (APP_ID, SYS_GROUP_ID, SYS_KEY, SYS_SUB, SYS_VAL, SYS_DESC, SORT_SEQ)
 Values
('InsWebApp', 'EL_CORE_PROP', 'SQL_WRITE_LOG_LEVEL', 'SQLを記録するログレベル', 'DEBUG', 'SQL文を記録するログレベル（DEBUG、OFF） - デフォルトはDEBUG', 240);
   
Insert into EL_SYS_PROP
   (APP_ID, SYS_GROUP_ID, SYS_KEY, SYS_SUB, SYS_VAL, 
    SYS_DESC, SORT_SEQ)
 Values
('InsWebApp', 'EL_CORE_PROP', 'APPLICATION_LOG_WRITE_YN', 'ログ記録有無', 'Y',
'ログ記録の有無（Y/N） - デフォルトはY', 235);
   
Insert into EL_SYS_PROP
   (APP_ID, SYS_GROUP_ID, SYS_KEY, SYS_SUB, SYS_VAL, SYS_DESC)
 Values
('InsWebApp', 'EL_CORE_PROP', 'SYSTEM_HANDLE_CLASS_NAME', 'システム前処理/後処理実装クラス名', 'com.demo.proworks.cmmn.ProworksSystemHandleAdapter', 'システム前処理/後処理を処理するアダプタクラス名');
Insert into EL_SYS_PROP
   (APP_ID, SYS_GROUP_ID, SYS_KEY, SYS_SUB, SYS_VAL, SYS_DESC)
 Values
('InsWebApp', 'EL_CORE_PROP', 'SERVICE_USER_CONTROL_CLASS_NAME', 'サービス制御実装クラス名', 'com.inswave.elfw.intercept.service.DefaultElServiceUserControlAdapter', 'カスタムサービス制御アダプタクラス名');
Insert into EL_SYS_PROP
   (APP_ID, SYS_GROUP_ID, SYS_KEY, SYS_SUB, SYS_VAL, SYS_DESC)
 Values
('InsWebApp', 'EL_CORE_PROP', 'TEST_SERVICE_RUN_YN', 'テストのためのTestサービスの動作有無', 'Y', 'テストのためのTestサービスの動作有無（Y/N）');
Insert into EL_SYS_PROP
   (APP_ID, SYS_GROUP_ID, SYS_KEY, SYS_SUB, SYS_DESC)
 Values
('InsWebApp', 'EL_CORE_PROP', 'INPUT_XFORM_RESOLVER_ADAPTER_CLASS_NAME', 'XFORM入力実装クラス名', 'X-Internet入力アダプター');
   
    
Insert into EL_SYS_PROP
   (APP_ID, SYS_GROUP_ID, SYS_KEY, SYS_SUB, SYS_VAL, SYS_DESC, SORT_SEQ)
 Values
('InsWebApp', 'EL_CORE_PROP', 'SCM_CLIENT_CLASS_NAME', '構成管理連携実装クラス名', 'com.inswave.elfw.scm.SvnClientHandleAdapter', '構成管理連携アダプター', 410);
   
Insert into EL_SYS_PROP
   (APP_ID, SYS_GROUP_ID, SYS_KEY, SYS_SUB, SYS_VAL, SYS_DESC, SORT_SEQ)
 Values
('InsWebApp', 'EL_CORE_PROP', 'PROJECT_SOURCE_SVN_URL', '構成管理SVN URL', '', '構成管理SVN URL（該当するJavaソースがある場所まで指定する）、指定された場所にファイルが保存され、これはソース分析のためのプロジェクトソース位置として使用される。', 420);
   
Insert into EL_SYS_PROP
   (APP_ID, SYS_GROUP_ID, SYS_KEY, SYS_SUB, SYS_VAL, SYS_DESC, SORT_SEQ)
 Values
('InsWebApp', 'EL_CORE_PROP', 'PROJECT_SOURCE_SVN_USER', '構成管理SVN USER', '', '構成管理SVN USER',430);
   

Insert into EL_SYS_PROP
   (APP_ID, SYS_GROUP_ID, SYS_KEY, SYS_SUB, SYS_VAL, SYS_DESC, SORT_SEQ)
 Values
('InsWebApp', 'EL_CORE_PROP', 'PROJECT_SOURCE_SVN_PASS', '構成管理SVNパスワード', '', '構成管理SVNパスワード', 440);
   

Insert into EL_SYS_PROP
   (APP_ID, SYS_GROUP_ID, SYS_KEY, SYS_SUB, SYS_VAL, SYS_DESC,SORT_SEQ)
 Values
('InsWebApp', 'EL_CORE_PROP', 'APPLICATION_LOG_SIZE', 'アプリログサイズ', '100M', 'ログファイルサイズ',245);

Insert into EL_SYS_PROP
   (APP_ID, SYS_GROUP_ID, SYS_KEY, SYS_SUB, SYS_VAL, SYS_DESC, SORT_SEQ)
 Values
('InsWebApp', 'EL_CORE_PROP', 'SCRN_AUTH_CACHE_SIZE', '画面権限情報キャッシュサイズ', '10000', '画面権限情報キャッシュサイズ', 300);
   
     
Insert into EL_SYS_PROP
   (APP_ID, SYS_GROUP_ID, SYS_KEY, SYS_SUB, SYS_VAL, 
    SYS_DESC)
 Values
('InsWebApp', 'EL_CORE_PROP', 'DEFAULT_LOG_FORMATTER_NAME', 'ログフォーマッター実装クラス名', 'com.inswave.elfw.log.DefaultAppFormatter',
'ログフォーマッター実装クラス名 - AppLogFormatterを継承して実装');

    
COMMIT;

/* Add 20190608 */
Insert into EL_SYS_PROP
   (APP_ID, SYS_GROUP_ID, SYS_KEY, SYS_SUB, SYS_VAL, 
    SYS_DESC, SORT_SEQ)
 Values
('InsWebApp', 'EL_CORE_PROP', 'IMAGE_LOG_USE_YN', 'イメージログ使用有無', 'N',
'イメージログ使用有無', 300);
Insert into EL_SYS_PROP
   (APP_ID, SYS_GROUP_ID, SYS_KEY, SYS_SUB, SYS_VAL, 
    SYS_DESC, SORT_SEQ)
 Values
('InsWebApp', 'EL_CORE_PROP', 'IMAGE_LOG_QUEUE_SIZE', 'イメージログのQueueサイズ', '10',
'イメージログキューサイズ', 301);
Insert into EL_SYS_PROP
   (APP_ID, SYS_GROUP_ID, SYS_KEY, SYS_SUB, SYS_VAL, 
    SYS_DESC, SORT_SEQ)
 Values
('InsWebApp', 'EL_CORE_PROP', 'IMAGE_LOG_TIMER_SEC', 'イメージログのTimer時間(秒)', '30',
'イメージログのタイマー時間（秒）', 302);
Insert into EL_SYS_PROP
   (APP_ID, SYS_GROUP_ID, SYS_KEY, SYS_SUB, SYS_VAL, 
    SYS_DESC, SORT_SEQ)
 Values
('InsWebApp', 'EL_CORE_PROP', 'IMAGE_LOG_QUEUE_WARN_SIZE', '画像ログQueueの警告サイズ', '0',
'イメージログキューの警告サイズ - 0の場合は動作しない', 303);
Insert into EL_SYS_PROP
   (APP_ID, SYS_GROUP_ID, SYS_KEY, SYS_SUB, SYS_VAL, 
    SYS_DESC, SORT_SEQ)
 Values
('InsWebApp', 'EL_CORE_PROP', 'IMAGE_LOG_QUEUE_ERROR_SIZE', 'イメージログQueueエラーSize', '0',
'イメージログキューエラーサイズ - 0の場合は動作しない', 304);
Insert into EL_SYS_PROP
   (APP_ID, SYS_GROUP_ID, SYS_KEY, SYS_SUB, SYS_VAL, 
    SYS_DESC, SORT_SEQ)
 Values
('InsWebApp', 'EL_CORE_PROP', 'IMAGE_LOG_PROCESS_CLS_NAME', 'イメージログ実装クラス', 'com.inswave.elfw.log.img.DefaultDBImageLogProcess',
'イメージログ実装クラス', 305);
Insert into EL_SYS_PROP
   (APP_ID, SYS_GROUP_ID, SYS_KEY, SYS_SUB, SYS_VAL, 
    SYS_DESC, SORT_SEQ)
 Values
('InsWebApp', 'EL_CORE_PROP', 'IMAGE_LOGGER_CLS_NAME', 'イメージロガー実装クラス', 'com.inswave.elfw.log.img.DefaultDBImageLogger',
'イメージロガー実装クラス - ElAbstractDBImageLoggerを継承して実装する必要がある', 306);

Insert into EL_SYS_PROP
   (APP_ID, SYS_GROUP_ID, SYS_KEY, SYS_SUB, SYS_VAL, 
    SYS_DESC, SORT_SEQ)
 Values
('InsWebApp', 'EL_CORE_PROP', 'CONTROLLER_TIME_OUT', 'Controllerタイムアウトのグローバル設定 ms', '0',
'Controller タイムアウト グローバル設定 単位：ms（-1の場合は動作しない）', 260);
       
INSERT INTO EL_SYS_PROP 
  ( APP_ID ,SYS_GROUP_ID ,SYS_KEY ,SYS_SUB ,SYS_VAL ,SYS_DESC ,SORT_SEQ )  
values  
('InsWebApp', 'EL_CORE_PROP', 'CRYPTO_CASS_NAME', '暗号化実装クラス', '', '暗号化実装クラスとして、フレームワークの抽象クラスであるElAbstractCryptoを継承し、getEncrypt、getDecryptの各メソッドを実装', 270);

Insert into EL_SYS_PROP
   (APP_ID, SYS_GROUP_ID, SYS_KEY, SYS_SUB, SYS_VAL, SYS_DESC, SORT_SEQ)
 Values
('InsWebApp', 'EL_CORE_PROP', 'FRAMEWORK_LOGIN_CHECK_YN', 'フレームワークアカウントIDログインチェックの有無', 'N', 'フレームワークアカウントIDログインチェックの有無（Y/N）、Nに設定する場合は権限チェックの有無がfalseになっている必要がある', 310);
   
COMMIT;
/* Add 20190608 */

Insert into EL_SYS_PROP
   (APP_ID, SYS_GROUP_ID, SYS_KEY, SYS_SUB, SYS_VAL, SYS_DESC, SORT_SEQ)
 Values
('InsWebApp', 'EL_AUTH_CD', 'A', '全体権限', '全体', '全体権限', 10);
Insert into EL_SYS_PROP
   (APP_ID, SYS_GROUP_ID, SYS_KEY, SYS_SUB, SYS_VAL, SYS_DESC, SORT_SEQ)
 Values
('InsWebApp', 'EL_AUTH_CD', 'P', '出力権限', '出力', '出力権限', 30);
Insert into EL_SYS_PROP
   (APP_ID, SYS_GROUP_ID, SYS_KEY, SYS_SUB, SYS_VAL, SYS_DESC, SORT_SEQ)
 Values
('InsWebApp', 'EL_AUTH_CD', 'R', '読み取り権限', '読み取り', '読み取り権限', 20);
COMMIT;


/* Add 20200312 */
Insert into EL_SYS_PROP
   (APP_ID, SYS_GROUP_ID, SYS_KEY, SYS_SUB, SYS_VAL, 
    SYS_DESC, SORT_SEQ)
 Values
('InsWebApp', 'EL_CORE_PROP', 'EL_HEALTH_DB_CHECK_QUERY', 'DBヘルスチェッククエリ', '',
'DB Heal チェッククエリ - Oracle : SELECT 1 as msg FROM DUAL', 500);
COMMIT;



Insert into EL_SYS_PROP
   (APP_ID, SYS_GROUP_ID, SYS_KEY, SYS_SUB, SYS_VAL, 
    SYS_DESC)
 Values
('InsWebApp', 'EL_CORE_PROP', 'FLD_SERVICE_DB_BASE_YN', 'EL_SVCフィールド追加に伴う基本オプション', 'Y',
'EL_SVC テーブルフィールド追加に伴うオプションのデフォルト設定 - 新規サイトへの適用必須');
COMMIT;    

Insert into EL_SYS_PROP
   (APP_ID, SYS_GROUP_ID, SYS_KEY, SYS_SUB, SYS_VAL, 
    SYS_DESC)
 Values
('InsWebApp', 'EL_CORE_PROP', 'EL_SERVICE_RETURN_VO_USED_YN', 'コントローラーのリターンVOサポート有無', 'Y',
'コントローラーのリターンVOサポート有無');
COMMIT;

Insert into EL_SYS_PROP
   (APP_ID, SYS_GROUP_ID, SYS_KEY, SYS_SUB, SYS_VAL, 
    SYS_DESC, SORT_SEQ)
 Values
('InsWebApp', 'EL_CORE_PROP', 'OPEN_API_HANDLE_CLASS_NAME', 'オープンAPI Handleの実装クラス', '',
'オープンAPI Handleの実装クラス', 311);

Insert into EL_SYS_PROP
   (APP_ID, SYS_GROUP_ID, SYS_KEY, SYS_SUB, SYS_VAL, 
    SYS_DESC, SORT_SEQ)
 Values
('InsWebApp', 'EL_CORE_PROP', 'EL_SERVICE_BASE_LOG_USE_YN', 'サービス別ログ使用有無', 'N',
'サービス別ログ使用有無 - 開発サーバーのみ適用', 236);
COMMIT;

INSERT INTO EL_SYS_PROP  ( APP_ID ,SYS_GROUP_ID ,SYS_KEY ,SYS_SUB ,SYS_VAL ,SYS_DESC ,SORT_SEQ )  
values  
('InsWebApp', 'EL_CORE_PROP', 'SVC_INFO_BASE_URL', 'サービス入出力情報のBase Url')
,'http://localhost:8080/InsWebApp' ,'サービス入出力情報のためのBase Url' ,300 ) ;
COMMIT;


Insert into EL_SYS_PROP
   (APP_ID, SYS_GROUP_ID, SYS_KEY, SYS_SUB, SYS_VAL, 
    SYS_DESC, SORT_SEQ)
 Values
('InsWebApp', 'EL_CORE_PROP', 'EL_HTTP_BODY_ALWAYS', 'HttpBodyモードのみ常に使用', 'Y',
'HttpBodyモードのみ常に使用', 390);
 COMMIT;
 

 Insert into EL_SYS_PROP
   (APP_ID, SYS_GROUP_ID, SYS_KEY, SYS_SUB, SYS_VAL, 
    SYS_DESC)
 Values
('InsWebApp', 'EL_CORE_PROP', 'QUERY_TIMEOUT_FOR_SVC_TIMEOUT', 'サービスタイムアウトのためのクエリタイムアウト設定の有無', 'N',
'サービスタイムアウトのためのクエリタイムアウト設定の有無');
 COMMIT;

/* 20210630 */
Insert into EL_SYS_PROP
   (APP_ID, SYS_GROUP_ID, SYS_KEY, SYS_SUB, SYS_VAL, 
    SYS_DESC, SORT_SEQ)
 Values
('InsWebApp', 'EL_CORE_PROP', 'EL_JSON_PROPERTY_NAMING_STRATEGY_CLASS_NAME', 'UI リクエスト/レスポンスのJson処理時にVOバインディング処理のためのルールを適用するためのクラス名実装', 'com.inswave.elfw.databind.DefaultPropertyNamingStrategy',
'Json変換フィールド名称処理クラス名Annotationの使用 - com.inswave.elfw.databind.DefaultPropertyNamingStrategy', 350);


Insert into EL_SYS_PROP
   (APP_ID, SYS_GROUP_ID, SYS_KEY, SYS_SUB, SYS_VAL, 
    SYS_DESC, SORT_SEQ)
 Values
('InsWebApp', 'EL_CORE_PROP', 'EL_INPUT_JSON_FAIL_ON_UNKNOWN_PROPERTIES_YN', 'UI Jsonリクエスト時にVOに定義されていない項目のエラーチェックの有無', 'N',
'UI Jsonリクエスト時にVOに定義されていない項目のエラーチェックの有無', 350);

/* 20220224 */
Insert into EL_SYS_PROP
   (APP_ID, SYS_GROUP_ID, SYS_KEY, SYS_SUB, SYS_VAL, 
    SYS_DESC, SORT_SEQ)
 Values
('InsWebApp', 'EL_CORE_PROP', 'EL_CTRL_NO_PARAM_NORMAL_DATA_YN', 'Controllerにパラメータがない場合、正常フォーマットでデータ処理を行うかどうか', 'N',
'Controller パラメータがない場合に正常フォーマットでデータ処理するかどうか：デフォルト値 N の場合はデータ部のみ送信 - 以前のサイトとの互換性', 400);
    
Insert into EL_SYS_PROP
	(APP_ID, SYS_GROUP_ID, SYS_KEY, SYS_SUB, SYS_VAL, SYS_DESC, SORT_SEQ)
 Values
('InsWebApp', 'EL_CORE_PROP', 'REGSIST_SERVICE_CHECK_YN', 'サービスチェック使用有無', 'Y', 'サービスチェック使用有無', 501);
 	
/* 20231115 */
/*
INSERT INTO EL_SYS_PROP
(APP_ID, SYS_GROUP_ID, SYS_KEY, SYS_SUB, SYS_VAL, SYS_DESC, SORT_SEQ)
VALUES('InsWebApp', 'EL_CORE_PROP', 'KAFKA_ADAPTER_CLASS_NAME', 'Kafkaアダプター実装クラス名', 'com.inswave.elfw.kafka.adapter.DefaultKafkaHandlerAdapter', 'Kafkaアダプタークラス名', NULL);

INSERT INTO EL_SYS_PROP
(APP_ID, SYS_GROUP_ID, SYS_KEY, SYS_SUB, SYS_VAL, SYS_DESC, SORT_SEQ)
VALUES('InsWebApp', 'EL_CORE_PROP', 'KAFKA_BROCKING_TIME_OUT', 'Kafkaブロッキングタイムアウト時間', '10', 'Kafkaブローカータイムアウト時間(秒)', NULL);

INSERT INTO EL_SYS_PROP
(APP_ID, SYS_GROUP_ID, SYS_KEY, SYS_SUB, SYS_VAL, SYS_DESC, SORT_SEQ)
VALUES('InsWebApp', 'EL_CORE_PROP', 'KAFKA_PROCESS_ADAPTER_CLASS_NAME', 'Kafkaプロセスアダプター実装クラス名', 'com.inswave.elfw.kafka.adapter.process.DefaultKafkaProcessAdapter', 'Kafkaプロセスアダプター実装クラス名', NULL);

INSERT INTO EL_SYS_PROP
(APP_ID, SYS_GROUP_ID, SYS_KEY, SYS_SUB, SYS_VAL, SYS_DESC, SORT_SEQ)
VALUES('InsWebApp', 'EL_CORE_PROP', 'KAFKA_IMAGE_LOG_PROCESS_CLS_NAME', 'Kafkaイメージログプロセス', 'com.inswave.elfw.kafka.img.DafaultKafkaImageLogProcess', 'Kafkaイメージログプロセス', NULL);

INSERT INTO EL_SYS_PROP
(APP_ID, SYS_GROUP_ID, SYS_KEY, SYS_SUB, SYS_VAL, SYS_DESC, SORT_SEQ)
VALUES('InsWebApp', 'EL_CORE_PROP', 'KAFKA_BOOTSTRAP_SERVERS', 'Kafkaアクセス情報', '192.168.100.186:29092,192.168.100.186:39092,192.168.100.186:49092', 'Kafkaアクセス情報', NULL);

INSERT INTO EL_SYS_PROP
(APP_ID, SYS_GROUP_ID, SYS_KEY, SYS_SUB, SYS_VAL, SYS_DESC, SORT_SEQ)
VALUES('InsWebApp', 'EL_CORE_PROP', 'KAFKA_CONSUMER_GROUPID', 'Kafkaデフォルトリスナーグループ ID', 'listenMessage', 'Kafkaデフォルトリスナーグループ ID', NULL);

INSERT INTO EL_SYS_PROP
(APP_ID, SYS_GROUP_ID, SYS_KEY, SYS_SUB, SYS_VAL, SYS_DESC, SORT_SEQ)
VALUES('InsWebApp', 'EL_CORE_PROP', 'KAFKA_TOPICS', 'Kafkaトピックリスト', 'producerTest_INFID_FLD,DmoEmpList_INFIDLIST_FLD', 'Kafkaトピックリスト', NULL);

INSERT INTO sp1_el_core.el_sys_prop
(APP_ID, SYS_GROUP_ID, SYS_KEY, SYS_SUB, SYS_VAL, SYS_DESC, SORT_SEQ)
VALUES('InsWebApp', 'EL_CORE_PROP', 'KAFKA_CONSUMER_BASE_URL', 'Kafkaコンシューマー URL', 'http://localhost:8080/InsWebApp/', 'Kafkaコンシューマー URL', NULL);
*/
COMMIT;
 
INSERT INTO EL_SVC 
	( APP_ID ,SVC_ID ,CLASS_NM ,METHOD_NM ,SVC_SUB ,SVC_DESC ,DEL_YN ,REQ_URL ,IN_INF_ID ,OUT_INF_ID )  
VALUES  
	( 'InsWebApp' ,'CmmLogin' ,'com.demo.proworks.emp.web.EmpController' ,
		'login(com.demo.proworks.emp.vo.LoginVo,javax.servlet.http.HttpServletRequest)' ,
'ログイン' ,'ログインを処理する' ,'N' ,
		'CmmLogin' ,'com.demo.proworks.emp.vo.LoginVo' ,null ) ;

INSERT INTO EL_SVC 
	( APP_ID ,SVC_ID ,CLASS_NM ,METHOD_NM ,SVC_SUB ,SVC_DESC ,DEL_YN ,REQ_URL ,IN_INF_ID ,OUT_INF_ID )  
VALUES  
	( 'InsWebApp' ,'CmmLoginFrm' ,'com.demo.proworks.emp.web.EmpController' ,
		'loginFrm(com.demo.proworks.emp.vo.LoginVo,javax.servlet.http.HttpServletRequest)' ,
'ログインフォーム' ,'ログインフォームページをロードする。' ,'N' ,
		'CmmLoginFrm' ,'com.demo.proworks.emp.vo.LoginVo' ,null ) ;

INSERT INTO EL_SVC 
	( APP_ID ,SVC_ID ,CLASS_NM ,METHOD_NM ,SVC_SUB ,SVC_DESC ,DEL_YN ,REQ_URL ,IN_INF_ID ,OUT_INF_ID )  
VALUES  
	( 'InsWebApp' ,'DmoEmpDel' ,'com.demo.proworks.emp.web.EmpController' ,
		'deleteEmp(com.demo.proworks.emp.vo.EmpVo)' ,
'社員情報削除' ,'社員情報を削除処理する。' ,'N' ,
		'DmoEmpDel' ,'com.demo.proworks.emp.vo.EmpVo' ,null ) ;

INSERT INTO EL_SVC 
	( APP_ID ,SVC_ID ,CLASS_NM ,METHOD_NM ,SVC_SUB ,SVC_DESC ,DEL_YN ,REQ_URL ,IN_INF_ID ,OUT_INF_ID )  
VALUES  
	( 'InsWebApp' ,'DmoEmpDown' ,'com.demo.proworks.emp.web.EmpController' ,
		'downEmpFile(com.demo.proworks.emp.vo.EmpVo,javax.servlet.http.HttpServletResponse)' ,
'従業員添付ファイルのダウンロード' , '従業員添付ファイルをダウンロードする。' ,'N' ,
		'DmoEmpDown' ,'com.demo.proworks.emp.vo.EmpVo' ,null ) ;

INSERT INTO EL_SVC 
	( APP_ID ,SVC_ID ,CLASS_NM ,METHOD_NM ,SVC_SUB ,SVC_DESC ,DEL_YN ,REQ_URL ,IN_INF_ID ,OUT_INF_ID )  
VALUES  
	( 'InsWebApp' ,'DmoEmpIns' ,'com.demo.proworks.emp.web.EmpController' ,
		'addEmp(com.demo.proworks.emp.vo.EmpVo)' ,
'社員登録処理' ,'社員情報を登録処理する。' ,'N' ,
		'DmoEmpIns' ,'com.demo.proworks.emp.vo.EmpVo' ,'com.demo.proworks.emp.vo.DeptListVo' ) ;

INSERT INTO EL_SVC 
	( APP_ID ,SVC_ID ,CLASS_NM ,METHOD_NM ,SVC_SUB ,SVC_DESC ,DEL_YN ,REQ_URL ,IN_INF_ID ,OUT_INF_ID )  
VALUES  
	( 'InsWebApp' ,'DmoEmpList' ,'com.demo.proworks.emp.web.EmpController' ,
		'selectEmpList(com.demo.proworks.emp.vo.EmpVo)' ,
'社員リスト照会' ,'ページング処理を行い社員リストを照会する。' ,'N' ,
		'DmoEmpList' ,'com.demo.proworks.emp.vo.EmpVo' ,'com.demo.proworks.emp.vo.EmpListVo' ) ;

INSERT INTO EL_SVC 
	( APP_ID ,SVC_ID ,CLASS_NM ,METHOD_NM ,SVC_SUB ,SVC_DESC ,DEL_YN ,REQ_URL ,IN_INF_ID ,OUT_INF_ID )  
VALUES  
	( 'InsWebApp' ,'DmoEmpSelect' ,'com.demo.proworks.emp.web.EmpController' ,
		'selectEmpView(com.demo.proworks.emp.vo.EmpVo)' ,
'社員情報の詳細照会（TCP内部テスト用）' ,'社員情報の詳細照会（TCP内部テスト用）を行う。' ,'N' ,
		'DmoEmpSelect' ,'com.demo.proworks.emp.vo.EmpVo' ,'com.demo.proworks.emp.vo.EmpVo' ) ;

INSERT INTO EL_SVC 
	( APP_ID ,SVC_ID ,CLASS_NM ,METHOD_NM ,SVC_SUB ,SVC_DESC ,DEL_YN ,REQ_URL ,IN_INF_ID ,OUT_INF_ID )  
VALUES  
	( 'InsWebApp' ,'DmoEmpUpd' ,'com.demo.proworks.emp.web.EmpController' ,
		'updateEmp(com.demo.proworks.emp.vo.EmpVo)' ,
'社員リスト更新処理' ,'社員情報を更新処理する。' ,'N' ,
		null ,'com.demo.proworks.emp.vo.EmpVo' ,'com.demo.proworks.emp.vo.DeptListVo' ) ;

INSERT INTO EL_SVC 
	( APP_ID ,SVC_ID ,CLASS_NM ,METHOD_NM ,SVC_SUB ,SVC_DESC ,DEL_YN ,REQ_URL ,IN_INF_ID ,OUT_INF_ID )  
values  
	( 'InsWebApp' ,'DmoEmpUpdView' ,'com.demo.proworks.emp.web.EmpController' ,
		'updateEmpView(com.demo.proworks.emp.vo.EmpVo)' ,
'社員情報更新フォームのための照会' ,'社員情報更新フォームのための照会を行う' ,'N' ,
		'DmoEmpUpdView' ,'com.demo.proworks.emp.vo.EmpVo' ,'com.demo.proworks.emp.vo.EmpListVo' ) ;

INSERT INTO EL_SVC 
	( APP_ID ,SVC_ID ,CLASS_NM ,METHOD_NM ,SVC_SUB ,SVC_DESC ,DEL_YN ,REQ_URL ,IN_INF_ID ,OUT_INF_ID )  
VALUES  
	( 'InsWebApp' ,'DmoEmpUpdateUploadEmpFile' ,'com.demo.proworks.emp.web.EmpController' ,
		'updateUploadEmpFile(javax.servlet.http.HttpServletRequest,com.demo.proworks.emp.vo.EmpVo,org.springframework.ui.Model)' ,
'ファイルアップロード修正' ,'ファイルアップロードを修正処理する。' ,'N' ,
		'DmoEmpUpdateUploadEmpFile' ,'com.demo.proworks.emp.vo.EmpVo' ,null ) ;

INSERT INTO EL_SVC 
	( APP_ID ,SVC_ID ,CLASS_NM ,METHOD_NM ,SVC_SUB ,SVC_DESC ,DEL_YN ,REQ_URL ,IN_INF_ID ,OUT_INF_ID )  
VALUES  
	( 'InsWebApp' ,'DmoEmpUploadEmpFile' ,'com.demo.proworks.emp.web.EmpController' ,
		'uploadEmpFileToDb(javax.servlet.http.HttpServletRequest,com.demo.proworks.emp.vo.EmpVo,org.springframework.ui.Model)' ,
'従業員添付ファイルのアップロード' ,'従業員添付ファイルをアップロードする' ,'N' ,
		'DmoEmpUploadEmpFile' ,'com.demo.proworks.emp.vo.EmpVo' ,null ) ;

INSERT INTO EL_SVC 
	( APP_ID ,SVC_ID ,CLASS_NM ,METHOD_NM ,SVC_SUB ,SVC_DESC ,DEL_YN ,REQ_URL ,IN_INF_ID ,OUT_INF_ID )  
VALUES  
	( 'InsWebApp' ,'DmoEmpView' ,'com.demo.proworks.emp.web.EmpController' ,
		'addEmpView(com.demo.proworks.emp.vo.EmpVo)' ,
'社員登録フォームのための照会' ,'社員登録フォームのための照会処理を行う。' ,'N' ,
		'DmoEmpView' ,'com.demo.proworks.emp.vo.EmpVo' ,'com.demo.proworks.emp.vo.DeptListVo' ) ;

INSERT INTO EL_SVC 
	( APP_ID ,SVC_ID ,CLASS_NM ,METHOD_NM ,SVC_SUB ,SVC_DESC ,DEL_YN ,REQ_URL ,IN_INF_ID ,OUT_INF_ID )  
VALUES  
	( 'InsWebApp' ,'DmoTcpSendTest' ,'com.demo.proworks.emp.web.EmpController' ,
		'tcpSendTest(com.demo.proworks.emp.vo.EmpVo)' ,
'TCP送信テスト' ,'TCP送信テストを行う。' ,'N' ,
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
'社員情報更新フォームのための照会JSP' ,'社員情報更新フォームのための照会JSPを行う。' ,'N' ,
		'DmoEmpUpdView' ,'com.demo.proworks.emp.vo.EmpVo' ,'com.demo.proworks.emp.vo.EmpListVo' ) ;
		
COMMIT;
