package com.hsms.authservice.security;

import java.security.Key;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.hsms.authservice.entity.User;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenUtil {

    @Value("${app.jwt-secret}")
    private String jwtSecret;

    @Value("${app.jwt-expiration-milliseconds}")
    private long jwtExpirationDate;

//    public String generateToken(User user) {
//
//        Date currentDate = new Date();
//        Date expireDate = new Date(currentDate.getTime() + jwtExpirationDate);
//
//        return Jwts.builder().subject(user.getEmail())
//
//                .claim("userId", user.getId())
//                .claim("role", user.getRoles())
//
//                .issuedAt(currentDate)
//                .expiration(expireDate)
//
//                .signWith(key())
//                .compact();
//    }
    
    public String generateToken(User user) {

        String role = user.getRoles()
                .iterator()
                .next()
                .getRoleName();

        return Jwts.builder()
                .subject(user.getEmail())
                .claim("userId", user.getUserId())
                .claim("role", role)
                .issuedAt(new Date())
                .expiration(
                        new Date(System.currentTimeMillis()
                                + 86400000))
                .signWith(key())
                .compact();
    }
    
   


	private Key key() {

        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public boolean validateToken(String token) {

        Jwts.parser().verifyWith((SecretKey) key()).build().parse(token);
        return true;
    }

    public String getUsername(String token) {

        return Jwts.parser().verifyWith((SecretKey) key()).build().parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }
}