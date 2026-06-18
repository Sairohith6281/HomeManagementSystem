package com.hsms.gateway.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

@Component
public class JwtUtil {

    private static final String SECRET_KEY =
            "JFKJU484334939ERFDDFDFJDFJDFKDFOR25DFDDFKFDIDFKDFODFLDFLDODFLDFODF2EODDFDFPDF";

    public boolean validateToken(String token) {
        try {
        	Jwts.parserBuilder()
            .setSigningKey(getSigningKey())
            .build()
            .parseClaimsJws(token);

            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}