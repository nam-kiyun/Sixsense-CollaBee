package com.demo.proworks.cmmn;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import java.io.IOException;
import java.util.Properties;

@Configuration
public class AppConfig {

    @Bean
    public static PropertySourcesPlaceholderConfigurer recaptchaPropertyConfigurer() throws IOException {
        // 1. .env 로드
        Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();

        // 2. 필요한 키만 추출 (전체 항목 불필요 시)
        Properties props = new Properties();
        String recaptchaSecret = dotenv.get("RECAPTCHA_SECRET_KEY");
        if (recaptchaSecret != null) {
            props.setProperty("recaptcha.secret", recaptchaSecret);
        }

        // 3. Spring Property Placeholder에 주입
        PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
        configurer.setProperties(props);

        return configurer;
    }
}

