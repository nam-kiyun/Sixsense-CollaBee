package com.demo.proworks.videochat.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import redis.clients.jedis.JedisPoolConfig;

/**
 * 통합 Redis 설정 클래스
 * 화상채팅과 칸반보드 Redis 설정을 모두 관리
 * - Database 0: 화상채팅 데이터
 * - Database 1: 칸반보드 데이터
 */
@Configuration
public class RedisConfig {
    
    public RedisConfig() {
        //System.out.println("🔧 RedisConfig 클래스 생성됨!");
    }

    /**
     * 화상채팅용 Redis 연결 팩토리 설정 (Database 0)
     */
    @Bean
    @Primary
    public RedisConnectionFactory redisConnectionFactory() {
        //System.out.println("🔧 Redis 연결 설정: localhost:6379");
        
        JedisConnectionFactory factory = new JedisConnectionFactory();
        factory.setHostName("localhost");
//        factory.setHostName("redis-server");  // Redis 서버 호스트
        factory.setPort(6379);             // Redis 서버 포트	
        factory.setDatabase(0);            // 기본 데이터베이스
        factory.setTimeout(10000);         // 10초 타임아웃
        factory.setUsePool(true);
        
        // Connection Pool 설정
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(20);        // 최대 연결 수
        poolConfig.setMaxIdle(10);         // 최대 유휴 연결 수
        poolConfig.setMinIdle(2);          // 최소 유휴 연결 수
        poolConfig.setTestOnBorrow(true);  // 연결 획득 시 검증
        poolConfig.setTestOnReturn(true);  // 연결 반환 시 검증
        factory.setPoolConfig(poolConfig);
        
        //System.out.println("🔧 Redis 연결 팩토리 설정 완료");
        return factory;
    }
    
    /**
     * RedisTemplate 설정 - String만 사용 (직렬화 문제 해결)
     */
    @Bean
    public RedisTemplate<String, String> redisTemplate(@Qualifier("redisConnectionFactory") RedisConnectionFactory connectionFactory) {
        //System.out.println("🔧 RedisTemplate 설정 시작...");
        
        RedisTemplate<String, String> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        
        // 모든 Serializer를 String으로 통일 (직렬화 호환성 문제 해결)
        StringRedisSerializer stringSerializer = new StringRedisSerializer();
        template.setKeySerializer(stringSerializer);
        template.setValueSerializer(stringSerializer);
        template.setHashKeySerializer(stringSerializer);
        template.setHashValueSerializer(stringSerializer);
        template.setDefaultSerializer(stringSerializer);
        
        template.afterPropertiesSet();
        
        //System.out.println("🔧 RedisTemplate 설정 완료 (StringRedisSerializer 사용)");
        
        // Redis 연결 테스트
        try {
            template.opsForValue().set("test:connection", "OK");
            String result = template.opsForValue().get("test:connection");
            template.delete("test:connection");
            
            if ("OK".equals(result)) {
                //System.out.println("🔍 Redis 연결 상태: 정상 ✅");
            } else {
                //System.err.println("🔍 Redis 연결 상태: 비정상 ❌");
            }
        } catch (Exception e) {
            //System.err.println("🔍 Redis 연결 상태: 오류 ❌ - " + e.getMessage());
        }
        
        return template;
    }

    /**
     * 칸반보드용 Redis 연결 팩토리 설정 (Database 1)
     */
    @Bean(name = "kanbanRedisConnectionFactory")
    public RedisConnectionFactory kanbanRedisConnectionFactory() {
        System.out.println("🔧 칸반보드용 Redis 연결 설정: localhost:6379, DB:1");
        
        JedisConnectionFactory factory = new JedisConnectionFactory();
        factory.setHostName("localhost");
//        factory.setHostName("redis-server");  // Redis 서버 호스트
        factory.setPort(6379);             // Redis 서버 포트
        factory.setDatabase(1);            // 칸반보드 전용 데이터베이스
        factory.setTimeout(10000);         // 10초 타임아웃
        factory.setUsePool(true);
        
        // Connection Pool 설정 (칸반보드용 최적화)
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(50);        // 최대 연결 수 (칸반보드 고성능)
        poolConfig.setMaxIdle(20);         // 최대 유휴 연결 수
        poolConfig.setMinIdle(10);         // 최소 유휴 연결 수
        poolConfig.setTestOnBorrow(true);  // 연결 획득 시 검증
        poolConfig.setTestOnReturn(true);  // 연결 반환 시 검증
        poolConfig.setTestWhileIdle(true); // 유휴 연결 검증
        poolConfig.setMaxWaitMillis(3000); // 연결 대기 시간 (3초)
        factory.setPoolConfig(poolConfig);
        
        System.out.println("🔧 칸반보드용 Redis 연결 팩토리 설정 완료");
        return factory;
    }
    
    /**
     * 칸반보드용 RedisTemplate 설정 (Object 지원)
     */
    @Bean(name = "kanbanRedisTemplate")
    public RedisTemplate<String, Object> kanbanRedisTemplate(@Qualifier("kanbanRedisConnectionFactory") RedisConnectionFactory kanbanConnectionFactory) {
        System.out.println("🔧 칸반보드용 RedisTemplate 설정 시작...");
        
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(kanbanConnectionFactory);
        
        // 직렬화 설정 (String + JSON 혼합)
        StringRedisSerializer stringSerializer = new StringRedisSerializer();
        GenericJackson2JsonRedisSerializer jsonSerializer = new GenericJackson2JsonRedisSerializer();
        
        // Key는 String, Value는 JSON으로 직렬화
        template.setKeySerializer(stringSerializer);
        template.setValueSerializer(jsonSerializer);
        template.setHashKeySerializer(stringSerializer);
        template.setHashValueSerializer(jsonSerializer);
        
        template.afterPropertiesSet();
        
        System.out.println("🔧 칸반보드용 RedisTemplate 설정 완료");
        return template;
    }
} 