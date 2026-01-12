package com.hdu.forum.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;

public class JwtUtil {
    
    private static final String SECRET_KEY = "hdu-forum-secret-key-2026";
    private static final long EXPIRE_TIME = 7 * 24 * 60 * 60 * 1000; // 7天
    
    /**
     * 生成token
     */
    public static String generateToken(Long userId, String username) {
        return JWT.create()
                .withClaim("userId", userId)
                .withClaim("username", username)
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRE_TIME))
                .sign(Algorithm.HMAC256(SECRET_KEY));
    }
    
    /**
     * 验证token
     */
    public static DecodedJWT verifyToken(String token) {
        try {
            return JWT.require(Algorithm.HMAC256(SECRET_KEY))
                    .build()
                    .verify(token);
        } catch (Exception e) {
            return null;
        }
    }
    
    /**
     * 从token中获取用户ID
     */
    public static Long getUserIdFromToken(String token) {
        DecodedJWT jwt = verifyToken(token);
        return jwt != null ? jwt.getClaim("userId").asLong() : null;
    }
}
