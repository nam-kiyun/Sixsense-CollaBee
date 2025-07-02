package com.demo.proworks.cmmn;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:inswave/properties/elfw.properties")
public class AppConfig {
    // 이 클래스는 Spring 컨테이너에 프로퍼티 파일의 위치를 알려주는 역할을 합니다.
    // 특별한 Bean을 등록할 필요가 없다면 비어있어도 괜찮습니다.
}