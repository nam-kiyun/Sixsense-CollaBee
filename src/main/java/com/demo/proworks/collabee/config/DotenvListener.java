package com.demo.proworks.collabee.config;

import io.github.cdimascio.dotenv.Dotenv;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class DotenvListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // 웹 애플리케이션이 시작될 때 .env 파일을 로드하여 시스템 프로퍼티에 설정합니다.
        System.out.println("Loading .env file into system properties...");
        Dotenv.configure()
              .systemProperties() // .env 변수들을 시스템 프로퍼티에 추가
              .ignoreIfMissing()  // .env 파일이 없어도 에러를 발생시키지 않음
              .load();
        System.out.println(".env file loaded successfully.");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // 애플리케이션 종료 시 특별히 할 작업은 없습니다.
    }
}