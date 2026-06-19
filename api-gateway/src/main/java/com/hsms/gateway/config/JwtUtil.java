package com.hsms.gateway.config;

import java.security.Key;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

    private static final String SECRET =
            "VGhpc0lzTXlTZWNyZXRLZXlGb3JKV1RUb2tlbjEyMzQ1Njc4OTA=";

    private SecretKey getSignKey() {

        byte[] keyBytes =
                Decoders.BASE64.decode(SECRET);

        return Keys.hmacShaKeyFor(keyBytes);
    }

//    public boolean validateToken(String token) {
//
//        Jwts.parserBuilder()
//            .setSigningKey(getSignKey())
//            .build()
//            .parseClaimsJws(token);
//
//        return true;
//    }
//    public String extractUsername(String token) {
//        return Jwts.parser()
//                .verifyWith((SecretKey) getSignKey())
//                .build()
//                .parseSignedClaims(token)
//                .getPayload()
//                .getSubject();
//    }
//
//    public String extractRole(String token) {
//        return Jwts.parser()
//                .verifyWith((SecretKey) getSignKey())
//                .build()
//                .parseSignedClaims(token)
//                .getPayload()
//                .get("role", String.class);
//    }
    
    
    public boolean validateToken(String token) {

    	System.out.println(token);
        Jwts.parser()
                .verifyWith((SecretKey) getSignKey())
                .build()
                .parseSignedClaims(token);

        return true;
    }

    public String extractUsername(String token) {

        return Jwts.parser()
                .verifyWith((SecretKey) getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public String extractRole(String token) {

        return Jwts.parser()
                .verifyWith((SecretKey) getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("role", String.class);
    }

    public Long extractUserId(String token) {

        Claims claims =
                Jwts.parser()
                        .verifyWith((SecretKey) getSignKey())
                        .build()
                        .parseSignedClaims(token)
                        .getPayload();

        Object userId = claims.get("userId");

        if (userId instanceof Integer) {
            return ((Integer) userId).longValue();
        }

        return (Long) userId;
    }
}