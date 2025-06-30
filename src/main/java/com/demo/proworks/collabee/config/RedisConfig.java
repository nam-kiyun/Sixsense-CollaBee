package com.demo.proworks.collabee.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import redis.clients.jedis.JedisPoolConfig;

@Configuration
public class RedisConfig {
    
    /**
     * Redis 연결 팩토리 설정
     */
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        JedisConnectionFactory factory = new JedisConnectionFactory();
        factory.setHostName("localhost");  // Redis 서버 주소
        factory.setPort(6379);            // Redis 포트
        factory.setPassword("");          // 비밀번호 (없으면 빈 문자열)
        factory.setDatabase(0);           // 데이터베이스 번호
        factory.setTimeout(2000);         // 타임아웃 설정
        
        // 커넥션 풀 설정
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(100);      // 최대 연결 수
        poolConfig.setMaxIdle(50);        // 최대 유휴 연결 수
        poolConfig.setMinIdle(10);        // 최소 유휴 연결 수
        poolConfig.setTestOnBorrow(true); // 연결 검증
        factory.setPoolConfig(poolConfig);
        
        return factory;
    }
    
    /**
     * RedisTemplate 설정 - 채팅 메시지용
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory());
        
        // Key는 문자열로 직렬화
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        
        // Value는 JSON으로 직렬화
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
        
        template.setDefaultSerializer(new StringRedisSerializer());
        template.afterPropertiesSet();
        
        return template;
    }
} 