package com.demo.proworks.githubapptoken.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * @subject     : GitHub App 인증을 위한 JWT 토큰 생성 유틸리티
 * @description : GitHub App의 private key를 사용하여 JWT 토큰을 생성하고 인증을 처리
 * @author      : 남기윤
 * @since       : 2025/07/07
 */
@Component
public class GitHubAppAuthUtil {
    
    @Value("${github.app.id:}")
    private String appId;
    
    @Value("${github.app.private.key.path:}")
    private String privateKeyPath;
    
    private PrivateKey privateKey;
    
    /**
     * Private key를 로드한다.
     */
    public void loadPrivateKey() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        if (privateKeyPath == null || privateKeyPath.isEmpty()) {
            throw new IllegalArgumentException("GitHub App private key path가 설정되지 않았습니다.");
        }
        
        File keyFile = new File(privateKeyPath);
        if (!keyFile.exists()) {
            throw new IOException("Private key 파일을 찾을 수 없습니다: " + privateKeyPath);
        }
        
        String keyContent = new String(Files.readAllBytes(keyFile.toPath()));
        
        // PEM 형식에서 헤더와 푸터 제거
        keyContent = keyContent.replace("-----BEGIN PRIVATE KEY-----", "")
                              .replace("-----END PRIVATE KEY-----", "")
                              .replace("-----BEGIN RSA PRIVATE KEY-----", "")
                              .replace("-----END RSA PRIVATE KEY-----", "")
                              .replaceAll("\\s", "");
        
        // Base64 디코딩
        byte[] keyBytes = Base64.getDecoder().decode(keyContent);
        
        // PKCS8 형식으로 키 생성
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        this.privateKey = keyFactory.generatePrivate(keySpec);
    }
    
    /**
     * GitHub App JWT 토큰을 생성한다.
     * 
     * @return JWT 토큰
     */
    public String generateJWT() throws Exception {
        if (privateKey == null) {
            loadPrivateKey();
        }
        
        if (appId == null || appId.isEmpty()) {
            throw new IllegalArgumentException("GitHub App ID가 설정되지 않았습니다.");
        }
        
        Instant now = Instant.now();
        
        return Jwts.builder()
                .setIssuer(appId)
                .setIssuedAt(Date.from(now.minus(60, ChronoUnit.SECONDS))) // 60초 전에 발급된 것으로 설정
                .setExpiration(Date.from(now.plus(10, ChronoUnit.MINUTES))) // 10분 후 만료
                .signWith(privateKey, SignatureAlgorithm.RS256)
                .compact();
    }
    
    /**
     * App ID를 반환한다.
     * 
     * @return GitHub App ID
     */
    public String getAppId() {
        return appId;
    }
    
    /**
     * Private key path를 반환한다.
     * 
     * @return Private key 파일 경로
     */
    public String getPrivateKeyPath() {
        return privateKeyPath;
    }
}
