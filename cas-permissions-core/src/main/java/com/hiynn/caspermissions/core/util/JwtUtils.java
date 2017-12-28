package com.hiynn.caspermissions.core.util;

import io.jsonwebtoken.*;
import org.springframework.util.CollectionUtils;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author yanchao
 * @date 2017/12/25 17:08
 */
public class JwtUtils {

    /**
     * 生成JWT
     * @param issuer 该JWT的签发者
     * @param subject 该JWT所面向的用户
     * @param ttlMillis 有效毫秒数
     * @param messages 用户自定义payload信息
     * @param secretKey 秘钥
     * @return
     */
    public static String createJWT(String issuer, String subject, long ttlMillis, Map<String, Object> messages, String secretKey) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS512;
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        Key signingKey = new SecretKeySpec(secretKey.getBytes(), signatureAlgorithm.getJcaName());
        JwtBuilder builder = Jwts.builder()
                .setIssuedAt(now)
                .setSubject(subject)
                .setIssuer(issuer)
                .signWith(signatureAlgorithm, signingKey);
        if (!CollectionUtils.isEmpty(messages)) {
            builder.addClaims(messages);
        }
        if (ttlMillis >= 0) {
            Date expiration = new Date(nowMillis + ttlMillis);
            builder.setExpiration(expiration);
        }
        return builder.compact();
    }

    public static Claims parseJWT(String jwt, String secretKey) throws ExpiredJwtException, UnsupportedJwtException,
            MalformedJwtException, SignatureException, IllegalArgumentException {
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey.getBytes())
                .parseClaimsJws(jwt).getBody();
        return claims;
    }

    public static void main(String[] args) {
        String secret = "123456";
        Map<String, Object> map = new HashMap<>();
        map.put("username", "yanchao");
        String jwt = createJWT(
                "server",
                "clients",
                TimeUnit.MINUTES.toMillis(1),
                map,
                secret
        );
        try {
            Claims claims = parseJWT(jwt, secret);
            System.out.println(claims);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
